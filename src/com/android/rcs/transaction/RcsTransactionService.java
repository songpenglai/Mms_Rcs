
package com.android.rcs.transaction;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Telephony.Mms;
import android.util.Log;
import android.widget.Toast;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.util.MultiSimUtility;
import com.android.rcs.message.RcsNewFileTransferInvateMessage;
import com.android.rcs.util.RcsFileUtil;
import com.android.rcs.util.RcsMiscInterface;

public class RcsTransactionService extends Service {
	private static final String TAG = "TransactionService";

	/**
	 * Used to identify notification intents broadcasted by the
	 * TransactionService when a Transaction is completed.
	 */
	public static final String TRANSACTION_COMPLETED_ACTION = "android.intent.action.TRANSACTION_COMPLETED_ACTION";

	/**
	 * Action for the Intent which is sent by Alarm service to launch
	 * TransactionService.
	 */
	public static final String ACTION_ONALARM = "android.intent.action.ACTION_ONALARM";

	/**
	 * Action for the Intent which is sent when the user turns on the
	 * auto-retrieve setting. This service gets started to auto-retrieve any
	 * undownloaded messages.
	 */
	public static final String ACTION_ENABLE_AUTO_RETRIEVE = "android.intent.action.ACTION_ENABLE_AUTO_RETRIEVE";

	/**
	 * Used as extra key in notification intents broadcasted by the
	 * TransactionService when a Transaction is completed
	 * (TRANSACTION_COMPLETED_ACTION intents). Allowed values for this key are:
	 * TransactionState.INITIALIZED, TransactionState.SUCCESS,
	 * TransactionState.FAILED.
	 */
	public static final String STATE = "state";

	/**
	 * Used as extra key in notification intents broadcasted by the
	 * TransactionService when a Transaction is completed
	 * (TRANSACTION_COMPLETED_ACTION intents). Allowed values for this key are
	 * any valid content uri.
	 */
	public static final String STATE_URI = "uri";

	private static final int EVENT_TRANSACTION_REQUEST = 1;
	private static final int EVENT_CONTINUE_MMS_CONNECTIVITY = 3;
	private static final int EVENT_HANDLE_NEXT_PENDING_TRANSACTION = 4;
	private static final int EVENT_NEW_INTENT = 5;
	private static final int EVENT_QUIT = 100;

	private static final int TOAST_MSG_QUEUED = 1;
	private static final int TOAST_DOWNLOAD_LATER = 2;
	private static final int TOAST_NO_APN = 3;
	private static final int TOAST_NONE = -1;

	private ServiceHandler mServiceHandler;
	private Looper mServiceLooper;
	private final ArrayList<RcsTransaction> mProcessing = new ArrayList<RcsTransaction>();
	private final ArrayList<RcsTransaction> mPending = new ArrayList<RcsTransaction>();
	private static RcsTransactionService sInstance;

	public Handler mToastHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String str = null;

			if (msg.what == TOAST_MSG_QUEUED) {
				str = getString(R.string.message_queued);
			} else if (msg.what == TOAST_DOWNLOAD_LATER) {
				str = getString(R.string.download_later);
			} else if (msg.what == TOAST_NO_APN) {
				str = getString(R.string.no_apn);
			}

			if (str != null) {
				Toast.makeText(RcsTransactionService.this, str,
						Toast.LENGTH_LONG).show();
			}
		}
	};

	public static RcsTransactionService getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
			Log.v(TAG, "Creating TransactionService");
		}
		sInstance = this;

		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.
		HandlerThread thread = new HandlerThread("RcsTransactionService");
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	private String getTxnIdFromDb(Uri uri) {
		String txnId = null;
		Cursor c = getApplicationContext().getContentResolver().query(uri,
				null, null, null, null);
		if (c != null) {
			try {
				if (c.moveToFirst()) {
					txnId = c.getString(c.getColumnIndex(Mms.TRANSACTION_ID));
					Log.d(TAG, "TxnId in db=" + txnId);
					c.close();
					c = null;
					return txnId;
				}
			} finally {
				if (c != null) {
					c.close();
				}
			}
		}
		Log.d(TAG, "TxnId in db=" + txnId);
		return txnId;

	}

	private int getSubIdFromDb(Uri uri) {
		int subId = 0;
		Cursor c = getApplicationContext().getContentResolver().query(uri,
				null, null, null, null);
		Log.d(TAG, "Cursor= " + DatabaseUtils.dumpCursorToString(c));
		if (c != null) {
			try {
				if (c.moveToFirst()) {
					subId = c.getInt(c.getColumnIndex(Mms.SUB_ID));
					Log.d(TAG, "subId in db=" + subId);
					c.close();
					c = null;
				}
			} finally {
				if (c != null) {
					c.close();
				}
			}
		}
		return subId;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Log.d(TAG, "onStartCommand(): E");

			Message msg = mServiceHandler.obtainMessage(EVENT_NEW_INTENT);
			msg.arg1 = startId;
			msg.obj = intent;
			mServiceHandler.sendMessage(msg);
		}
		return Service.START_NOT_STICKY;
	}

	public void onNewIntent(Intent intent, int serviceId) {
		Bundle extras = intent.getExtras();
		String action = intent.getAction();
		if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
			Log.v(TAG, "onNewIntent: launch transaction...");
		}
		String uriStr = intent.getStringExtra("uri");
		int destSub = intent.getIntExtra(Mms.SUB_ID, -1);
		int originSub = intent.getIntExtra(MultiSimUtility.ORIGIN_SUB_ID, -1);

		// For launching NotificationTransaction and test purpose.
		Bundle args = new Bundle(intent.getExtras());
		launchTransaction(serviceId, args);
	}

	private void launchTransaction(int serviceId, Bundle txnBundle) {
		Message msg = mServiceHandler.obtainMessage(EVENT_TRANSACTION_REQUEST);
		msg.arg1 = serviceId;
		msg.obj = txnBundle;

		if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
			Log.v(TAG, "launchTransaction: sending message " + msg);
		}
		mServiceHandler.sendMessage(msg);
	}

	@Override
	public void onDestroy() {
		if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
			Log.v(TAG, "Destroying TransactionService");
		}
		if (!mPending.isEmpty()) {
			Log.w(TAG,
					"TransactionService exiting with transaction still pending");
		}

		mServiceHandler.sendEmptyMessage(EVENT_QUIT);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		private String decodeMessage(Message msg) {
			if (msg.what == EVENT_QUIT) {
				return "EVENT_QUIT";
			} else if (msg.what == EVENT_CONTINUE_MMS_CONNECTIVITY) {
				return "EVENT_CONTINUE_MMS_CONNECTIVITY";
			} else if (msg.what == EVENT_TRANSACTION_REQUEST) {
				return "EVENT_TRANSACTION_REQUEST";
			} else if (msg.what == EVENT_HANDLE_NEXT_PENDING_TRANSACTION) {
				return "EVENT_HANDLE_NEXT_PENDING_TRANSACTION";
			} else if (msg.what == EVENT_NEW_INTENT) {
				return "EVENT_NEW_INTENT";
			}
			return "unknown message.what";
		}

		/**
		 * Handle incoming transaction requests.
		 */
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "Handling incoming message: " + msg + " = "
					+ decodeMessage(msg));

			RcsTransaction transaction = null;

			switch (msg.what) {
			case EVENT_NEW_INTENT:
				onNewIntent((Intent) msg.obj, msg.arg1);
				break;

			case EVENT_QUIT:
				getLooper().quit();
				return;

			case EVENT_TRANSACTION_REQUEST:
				int serviceId = msg.arg1;
				try {
					Bundle args = (Bundle) msg.obj;

					int transactionType = args.getInt(RcsTransaction.RCS_TRANSACTION_TYPE);

					// Create appropriate transaction
					switch (transactionType) {
					case RcsTransaction.RCS_SEND_SESSION_IM_TRANSACTION:
					case RcsTransaction.RCS_SEND_PAGE_IM_TRANSACTION:
					case RcsTransaction.RCS_SEND_LARGE_IM_TRANSACTION:
						transaction = new RcsImTransaction(RcsTransactionService.this, transactionType, args);
						break;
					case RcsTransaction.RCS_SEND_FILE_TRANSACTION:
					case RcsTransaction.RCS_SEND_GRP_FILE_TRANSACTION:
					case RcsTransaction.RCS_RECEIVE_FILE_TRANSACTION:
					case RcsTransaction.RCS_RECEIVE_GRP_FILE_TRANSACTION:
						transaction = new RcsFileTransaction(
								RcsTransactionService.this, transactionType, args);
						break;
					default:
						Log.w(TAG, "Invalid transaction type: " + serviceId);
						transaction = null;
						return;
					}

					if (!processTransaction(transaction)) {
						transaction = null;
						return;
					}

					if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
						Log.v(TAG, "Started processing of incoming message: "
								+ msg);
					}
				} catch (Exception ex) {
					Log.w(TAG, "Exception occurred while handling message: "
							+ msg, ex);

					if (transaction != null) {
						
					}
				} finally {
					if (transaction == null) {
						
					}
				}
				return;
			case EVENT_HANDLE_NEXT_PENDING_TRANSACTION:
				processPendingTransaction(transaction);
				return;
			default:
				Log.w(TAG, "what=" + msg.what);
				return;
			}
		}

		public void processPendingTransaction(RcsTransaction transaction) {

			if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
				Log.v(TAG, "processPendingTxn: transaction=" + transaction);
			}

			int numProcessTransaction = 0;
			synchronized (mProcessing) {
				if (mPending.size() != 0) {
					transaction = mPending.remove(0);
				}
				numProcessTransaction = mProcessing.size();
			}

			if (transaction != null) {

				/*
				 * Process deferred transaction
				 */
				try {

					if (processTransaction(transaction)) {
						if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
							Log.v(TAG,
									"Started deferred processing of transaction  "
											+ transaction);
						}
					} else {
						transaction = null;
					}
				} catch (IOException e) {
					Log.w(TAG, e.getMessage(), e);
				}
			} else {
				if (numProcessTransaction == 0) {
					if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
						Log.v(TAG,
								"processPendingTxn: no more transaction, endMmsConnectivity");
					}
				}
			}
		}

		/**
		 * Internal method to begin processing a transaction.
		 * 
		 * @param transaction
		 *            the transaction. Must not be {@code null}.
		 * @return {@code true} if process has begun or will begin.
		 *         {@code false} if the transaction should be discarded.
		 * @throws IOException
		 *             if connectivity for MMS traffic could not be established.
		 */
		private boolean processTransaction(RcsTransaction transaction)
				throws IOException {
			// Check if transaction already processing
			synchronized (mProcessing) {
				for (RcsTransaction t : mPending) {
					if (t.isEquivalent(transaction)) {
						return true;
					}
				}
				for (RcsTransaction t : mProcessing) {
					if (t.isEquivalent(transaction)) {
						return true;
					}
				}

				transaction.process();
				return true;
			}
		}

	}
	
	/**
	 * @param sendType 发送page/session/large
	 * */
	public static void sendImMessage(Context context, int cookieId, int sessionId, int sendType, String uri, String message) {
		Intent sendIntent = new Intent();
		sendIntent.setClass(context, RcsTransactionService.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt(RcsTransaction.RCS_COOKIE_ID, cookieId);
		bundle.putInt(RcsTransaction.RCS_SESSION_ID, sessionId);
		bundle.putInt(RcsTransaction.RCS_TRANSACTION_TYPE, sendType);
		bundle.putString(RcsTransaction.RCS_SEND_URI, uri);
		bundle.putString(RcsTransaction.RCS_SEND_TEXT, message);
		
		sendIntent.putExtras(bundle);
		context.startService(sendIntent);
		
	}
	
	/**
	 * @param sendType 发送page/session/large
	 * */
	public static void sendFile(Context context, int sendType, int cookieId, String uri, String title, String filePath, String fileType, int phoneId) {
		Intent sendIntent = new Intent();
		sendIntent.setClass(context, RcsTransactionService.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt(RcsTransaction.RCS_TRANSACTION_TYPE, sendType);//RcsTransaction.RCS_SEND_FILE_TRANSACTION
		bundle.putInt(RcsTransaction.RCS_COOKIE_ID, cookieId);
		bundle.putString(RcsTransaction.RCS_SEND_URI, uri);
		bundle.putString(RcsTransaction.RCS_SEND_TITLE, title);
		
		bundle.putString(RcsTransaction.RCS_SEND_FILE_NAME, filePath);
		bundle.putString(RcsTransaction.RCS_SEND_FILE_TYPE, fileType);
		bundle.putInt(RcsTransaction.RCS_TRANSACTION_PHONE_ID, phoneId);
		
		sendIntent.putExtras(bundle);
		context.startService(sendIntent);
		
	}
	
	/**
	 * @param sendType 发送page/session/large
	 * */
	public static void sendGrpFile(Context context, int sendType, int cookieId, String pcGroupChatId, String pcSessIdentity, String filePath, String fileType, int phoneId) {
		Intent sendIntent = new Intent();
		sendIntent.setClass(context, RcsTransactionService.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt(RcsTransaction.RCS_TRANSACTION_TYPE, sendType);//RcsTransaction.RCS_SEND_FILE_TRANSACTION
		bundle.putInt(RcsTransaction.RCS_COOKIE_ID, cookieId);
		bundle.putString(RcsTransaction.RCS_FILE_GRP_CHAT_ID, pcGroupChatId);
		bundle.putString(RcsTransaction.RCS_FILE_SESS_IDENTITY, pcSessIdentity);
		
		bundle.putString(RcsTransaction.RCS_SEND_FILE_NAME, filePath);
		bundle.putString(RcsTransaction.RCS_SEND_FILE_TYPE, fileType);
		bundle.putInt(RcsTransaction.RCS_TRANSACTION_PHONE_ID, phoneId);
		
		sendIntent.putExtras(bundle);
		context.startService(sendIntent);
		
	}
	
	/**
	 * @param sendType 发送page/session/large
	 * */
	public static int acceptFile(Context context, RcsNewFileTransferInvateMessage fileInvate) {
		String uri = fileInvate.getUri();
		String fileName = fileInvate.getFileName();
		Intent sendIntent = new Intent();
		int sessId = fileInvate.getSessId();
		int phoneId = fileInvate.getPhoneId();
		int userType = fileInvate.getUserType();
		int size = fileInvate.getFileSize();
		
		String filePath = RcsFileUtil.getImFilepath() + fileName;
		String fileType = RcsFileUtil.getFileTypeFromUri(filePath);
		
		int msgId = RcsFileUtil.saveAcceptFileInDb(context, fileInvate);
		sendIntent.setClass(context, RcsTransactionService.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt(RcsTransaction.RCS_TRANSACTION_TYPE, RcsTransaction.RCS_RECEIVE_FILE_TRANSACTION);//RcsTransaction.RCS_SEND_FILE_TRANSACTION
		bundle.putInt(RcsTransaction.RCS_COOKIE_ID, msgId);
		bundle.putString(RcsTransaction.RCS_SEND_URI, uri);
		bundle.putInt(RcsTransaction.RCS_SESSION_ID, sessId);
		bundle.putString(RcsTransaction.RCS_SEND_FILE_NAME, filePath);
		bundle.putString(RcsTransaction.RCS_SEND_FILE_TYPE, fileType);
		bundle.putInt(RcsTransaction.RCS_FILE_SIZE, size);
		bundle.putInt(RcsTransaction.RCS_TRANSACTION_PHONE_ID, phoneId);

		sendIntent.putExtras(bundle);
		context.startService(sendIntent);
		
		return msgId;
		
	}
}
