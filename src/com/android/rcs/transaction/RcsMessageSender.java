
package com.android.rcs.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Telephony.Mms.Outbox;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Inbox;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.MSimConstants;
import com.android.mms.LogTag;
import com.android.mms.transaction.MessageSender;
import com.android.mms.transaction.SmsReceiver;
import com.android.mms.transaction.SmsReceiverService;
import com.android.mms.ui.MessagingPreferenceActivity;
import com.google.android.mms.MmsException;

public class RcsMessageSender implements MessageSender {
    protected final Context mContext;
    protected final int mNumberOfDests;
    private final String[] mDests;
    protected final String mMessageText;
    protected final String mServiceCenter;
    protected final long mThreadId;
    protected long mTimestamp;
    protected int mSubscription;
    protected int mSessId = 0;
    protected String mGrpChatId = "";
    
    private static final String TAG = "SmsMessageSender";

    // Default preference values
    private static final boolean DEFAULT_DELIVERY_REPORT_MODE  = false;

    private static final String[] SERVICE_CENTER_PROJECTION = new String[] {
        Sms.Conversations.REPLY_PATH_PRESENT,
        Sms.Conversations.SERVICE_CENTER,
    };

    private static final int COLUMN_REPLY_PATH_PRESENT = 0;
    private static final int COLUMN_SERVICE_CENTER     = 1;

    public RcsMessageSender(Context context, String[] dests,
                 String msgText, long threadId, int subscription) {
        mContext = context;
        mMessageText = msgText;
        if (dests != null) {
            mNumberOfDests = dests.length;
            mDests = new String[mNumberOfDests];
            System.arraycopy(dests, 0, mDests, 0, mNumberOfDests);
        } else {
            mNumberOfDests = 0;
            mDests = null;
        }
        mTimestamp = System.currentTimeMillis();
        mThreadId = threadId;
        mServiceCenter = getOutgoingServiceCenter(mThreadId);
        mSubscription = subscription;
    }

    public boolean sendMessage(long token) throws MmsException {
        // In order to send the message one by one, instead of sending now, the message will split,
        // and be put into the queue along with each destinations
        return queueMessage(token);
    }

    public boolean sendMessage(long token, int sessId) throws MmsException {
    	mSessId = sessId;
        // In order to send the message one by one, instead of sending now, the message will split,
        // and be put into the queue along with each destinations
        return queueMessage(token);
    }

    public boolean sendMessage(long token, int sessId, String grpChatId) throws MmsException {
    	mSessId = sessId;
    	mGrpChatId = grpChatId;
        // In order to send the message one by one, instead of sending now, the message will split,
        // and be put into the queue along with each destinations
        return queueMessage(token);
    }
    
    private boolean queueMessage(long token) throws MmsException {
        if ((mMessageText == null) || (mNumberOfDests == 0)) {
            // Don't try to send an empty message.
            throw new MmsException("Null message body or dest.");
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean requestDeliveryReport = prefs.getBoolean(
                MessagingPreferenceActivity.SMS_DELIVERY_REPORT_MODE,
                DEFAULT_DELIVERY_REPORT_MODE);

        int inserCount = mNumberOfDests;
        if (!TextUtils.isEmpty(mGrpChatId)) {
        	inserCount = 1;
		}
        Uri uri = null;
        for (int i = 0; i < inserCount; i++) {
            try {
                if (LogTag.DEBUG_SEND) {
                    Log.v(TAG, "queueMessage mDests[i]: " + mDests[i] + " mThreadId: " + mThreadId);
                }
                log("updating Database with sub = " + mSubscription);
//                Sms.addMessageToUri(mContext.getContentResolver(),
//                        Uri.parse("content://sms/queued"), mDests[i],
//                        mMessageText, null, mTimestamp,
//                        true /* read */,
//                        requestDeliveryReport,
//                        mThreadId, mSubscription);

                ContentValues values = new ContentValues();
                values.put(Sms.THREAD_ID, mThreadId);
                values.put(Sms.BODY, mMessageText);
                values.put(Sms.DATE, System.currentTimeMillis());
                values.put(Sms.TYPE, Outbox.MESSAGE_BOX_OUTBOX);
                if (!TextUtils.isEmpty(mGrpChatId)) {
                	values.put("group_chat_id", mGrpChatId);
                	StringBuilder number = new StringBuilder();
                	for (int j = 0; j < mDests.length; j++) {
                		number.append(mDests[j]).append(";");
					}
                	String numbers = number.substring(0, number.length() - 1);
                	values.put(Sms.ADDRESS, numbers);
				} else {
					values.put(Sms.ADDRESS, mDests[0]);
				}
                
                uri = mContext.getContentResolver().insert(Uri.parse("content://sms"), values);
            } catch (SQLiteException e) {
                if (LogTag.DEBUG_SEND) {
                    Log.e(TAG, "queueMessage SQLiteException", e);
                }
                SqliteWrapper.checkSQLiteException(mContext, e);
            }
        }
        
        if (uri != null) {
        	int sendType = RcsTransaction.RCS_SEND_PAGE_IM_TRANSACTION;
        	String id = uri.getLastPathSegment();
        	boolean imMode = MessagingPreferenceActivity.getRcsImType(mContext);
        	if (!imMode || !TextUtils.isEmpty(mGrpChatId)) {
        		sendType = RcsTransaction.RCS_SEND_SESSION_IM_TRANSACTION;
			}
        	RcsTransactionService.sendImMessage(mContext, Integer.valueOf(id), mSessId, sendType, mDests[0], mMessageText);
		}
        
//        Intent intent = new Intent(SmsReceiverService.ACTION_SEND_MESSAGE, null, mContext,
//                SmsReceiver.class);
//        intent.putExtra(MSimConstants.SUBSCRIPTION_KEY, mSubscription);
//        // Notify the SmsReceiverService to send the message out
//        mContext.sendBroadcast(intent);
        return false;
    }
    
    public int saveMessage(String grpChatId) {
    	mGrpChatId = grpChatId;
    	return saveMessage();
    }
    
    public int saveMessage() {

        if ((mMessageText == null) || (mNumberOfDests == 0)) {
            // Don't try to send an empty message.
            return -1;
        }
        Uri uri = null;
        for (int i = 0; i < mNumberOfDests; i++) {
            try {
                ContentValues values = new ContentValues();
                values.put(Sms.THREAD_ID, mThreadId);
                values.put(Sms.BODY, mMessageText);
                values.put(Sms.ADDRESS, mDests[0]);
                values.put(Sms.DATE, System.currentTimeMillis());
                values.put(Sms.TYPE, Outbox.MESSAGE_BOX_OUTBOX);
                if (!TextUtils.isEmpty(mGrpChatId)) {
                	values.put("group_chat_id", mGrpChatId);
				}
                
                uri = mContext.getContentResolver().insert(Uri.parse("content://sms"), values);
            } catch (SQLiteException e) {
                if (LogTag.DEBUG_SEND) {
                    Log.e(TAG, "queueMessage SQLiteException", e);
                }
                SqliteWrapper.checkSQLiteException(mContext, e);
            }
        }
        int id = -1;
        if (uri != null) {
        	String Segment = uri.getLastPathSegment();
        	id = Integer.valueOf(Segment);
//        	RcsTransactionService.sendImMessage(mContext, id, 0, RcsTransaction.RCS_SEND_PAGE_IM_TRANSACTION, mDests[0], mMessageText);
		}
        
        return id;
    
    }

    public void sendSessMessage() {
    	
    }
    
    /**
     * Get the service center to use for a reply.
     *
     * The rule from TS 23.040 D.6 is that we send reply messages to
     * the service center of the message to which we're replying, but
     * only if we haven't already replied to that message and only if
     * <code>TP-Reply-Path</code> was set in that message.
     *
     * Therefore, return the service center from the most recent
     * message in the conversation, but only if it is a message from
     * the other party, and only if <code>TP-Reply-Path</code> is set.
     * Otherwise, return null.
     */
    private String getOutgoingServiceCenter(long threadId) {
        Cursor cursor = null;

        try {
            cursor = SqliteWrapper.query(mContext, mContext.getContentResolver(),
                            Inbox.CONTENT_URI, SERVICE_CENTER_PROJECTION,
                            "thread_id = " + threadId, null, "date DESC");

            if ((cursor == null) || !cursor.moveToFirst()) {
                return null;
            }

            boolean replyPathPresent = (1 == cursor.getInt(COLUMN_REPLY_PATH_PRESENT));
            return replyPathPresent ? cursor.getString(COLUMN_SERVICE_CENTER) : null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void log(String msg) {
        Log.d(LogTag.TAG, "[SmsMsgSender] " + msg);
    }
}
