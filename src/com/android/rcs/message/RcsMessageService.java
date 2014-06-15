package com.android.rcs.message;

import java.util.HashMap;

import com.android.rcs.util.RcsMiscInterface;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


public class RcsMessageService extends Service {

	private static String TAG = "RcsMessageService";
	/** �ϱ���IM���������intent */
	public static final String UNSOL_NEW_IM_SESS_INVITE_ACTION = "yulong.provider.Im.UNSOL_NEW_IM_SESS_INVITE_ACTION";

	/** �ϱ��Ự����״̬�����ܡ��ܾ�����ȡ�������뿪����intent */
	public static final String UNSOL_SESS_INVITE_STAT_ACTION = "yulong.provider.Im.UNSOL_SESS_INVITE_STAT_ACTION";

	/** �ϱ���ʱ����ӡ�ɾ���û�״̬�ϱ����ɹ���ʧ�ܡ���ʱ���б�������intent */
	public static final String UNSOL_TMP_GROUP_STAT_ACTION = "yulong.provider.Im.UNSOL_TMP_GROUP_STAT_ACTION";

	/** �ϱ���������ָʾ��intent */
	public static final String UNSOL_SESS_COMPOSING_ACTION = "yulong.provider.Im.UNSOL_SESS_COMPOSING_ACTION";

	/** �ϱ���IM��Ϣ������paging��chat��Ϣ����intent */
	public static final String UNSOL_NEW_IM_MSG_ACTION = "yulong.provider.Im.UNSOL_NEW_IM_MSG_ACTION";

	/** �ϱ���Ϣ״̬���棨����paging��chat��Ϣ�ĵ��ͱ������ʾ���棩��intent */
	public static final String UNSOL_IM_NTFY_ACTION = "yulong.provider.Im.UNSOL_IM_NTFY_ACTION";

	/** �ϱ���Ϣ����״̬������paging��chat��large��Ϣ���ͳɹ���ʧ�ܣ���intent */
	public static final String UNSOL_MSG_SEND_STAT_ACTION = "yulong.provider.Im.UNSOL_MSG_SEND_STAT_ACTION";

	/** �ϱ����ļ����������intent */
	public static final String UNSOL_NEW_FILE_TRANS_INVITE_ACTION = "yulong.provider.Im.UNSOL_NEW_FILE_TRANS_INVITE_ACTION";

	/** �ϱ��ļ��Ự����״̬�����ܡ��ܾ�����ȡ�������뿪����intent */
	public static final String UNSOL_FILE_SESS_STAT_ACTION = "yulong.provider.Im.UNSOL_FILE_SESS_STAT_ACTION";

	/** �ϱ��洢ת���ļ��Ự����״̬�����ܡ��ܾ�����ȡ�������뿪����intent */
	public static final String UNSOL_FILE_STFWD_STAT_ACTION = "yulong.provider.Im.UNSOL_FILE_STFWD_STAT_ACTION";

	/** �ϱ��ļ��շ�����������У��ѷ��͡��ѽ����Լ����շ��Ĵ�С����intent */
	public static final String UNSOL_FILE_TRANS_STAT_ACTION = "yulong.provider.Im.UNSOL_FILE_TRANS_STAT_ACTION";

	/** �ϱ��ļ��շ�����������У��ѷ��͡��ѽ����Լ����շ��Ĵ�С����intent */
	public static final String UNSOL_FILE_STFWD_TRANS_STAT_ACTION = "yulong.provider.Im.UNSOL_FILE_STFWD_TRANS_STAT_ACTION";

	/** �ϱ��¶�ý������������intent */
	public static final String UNSOL_NEW_ISHARE_INVITE_ACTION = "yulong.provider.Im.UNSOL_NEW_ISHARE_INVITE_ACTION";

	/** �ϱ���ý�����Ự����״̬�����ܡ��ܾ�����ȡ�������뿪����intent */
	public static final String UNSOL_ISHARE_SESS_STAT_ACTION = "yulong.provider.Im.UNSOL_ISHARE_SESS_STAT_ACTION";

	/** �ϱ���ý������ļ��շ�����������У��ѷ��͡��ѽ����Լ����շ��Ĵ�С����intent */
	public static final String UNSOL_ISHARE_TRANS_STAT_ACTION = "yulong.provider.Im.UNSOL_ISHARE_TRANS_STAT_ACTION";

	/** �ϱ��µ���λ�÷����������intent */
	public static final String UNSOL_NEW_GEO_LOC_INVITE_ACTION = "yulong.provider.Im.UNSOL_NEW_GEO_LOC_INVITE_ACTION";

	/** �ϱ�����λ�÷���Ự����״̬�����ܡ��ܾ�����ȡ�������뿪����intent */
	public static final String UNSOL_GEO_LOC_SESS_STAT_ACTION = "yulong.provider.Im.UNSOL_GEO_LOC_SESS_STAT_ACTION";

	/** �ϱ��յ�����λ�÷�����Ϣ��intent */
	public static final String UNSOL_NEW_GEO_LOC_MSG_ACTION = "yulong.provider.Im.UNSOL_NEW_GEO_LOC_MSG_ACTION";

	/** �ϱ��յ��Զ�������Ϣintent */
	public static final String UNSOL_NEW_AUTO_CONFIG_ACTION = "yulong.provider.Im.UNSOL_NEW_AUTO_CONFIG_ACTION";
	
	/** �ϱ��´洢ת������intent */
	public static final String UNSOL_NEW_IM_STFWD_INVITE_ACTION = "yulong.provider.Im.UNSOL_NEW_IM_STFWD_INVITE_ACTION";
	
	/** �ϱ��洢ת������״̬ ��intent */
	public static final String UNSOL_STFWD_INVITE_STAT_ACTION = "yulong.provider.Im.UNSOL_STFWD_INVITE_STAT_ACTION";
	
	private static final int EVENT_NEW_INTENT = 1;
	private ServiceHandler mServiceHandler;
	private Looper mServiceLooper;
	private static HashMap<Integer, RcsMessageProcessor> rcsMessageMap = new HashMap<Integer, RcsMessageProcessor>();
	
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	
	@Override
	public void onCreate() {
		HandlerThread thread = new HandlerThread("RcsMessageService");
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		super.onCreate();
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
	
	public static void registerMessage(int msgTpye, RcsMessageProcessor processor) {
		if (rcsMessageMap.containsKey(msgTpye)) {
			Log.w(TAG, "message map contains the key, instead, msgTpye = " + msgTpye);
		}
		rcsMessageMap.put(msgTpye, processor);
	}

	public static void unRegisterMessage(int msgTpye) {
		Log.w(TAG, "unRegisterMessage msgTpye = " + msgTpye);
		if (!rcsMessageMap.containsKey(msgTpye)) {
			Log.w(TAG, "unRegisterMessage not in map");
		}
		rcsMessageMap.remove(msgTpye);
	}
	
	private void dispatchMessage(int msgTpye, RcsMessage message) {
		if (rcsMessageMap.containsKey(msgTpye)) {
			Log.w(TAG, "message map contains the key, instead, msgTpye = " + msgTpye);
			rcsMessageMap.get(msgTpye).process(msgTpye, message);
		}
	}
	
	private final class ServiceHandler extends Handler {

		public ServiceHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == EVENT_NEW_INTENT) {
				Intent intent = (Intent) msg.obj;
				handleIntent(intent);
			}
			super.handleMessage(msg);
		}
	}
	
	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		Log.d(TAG, "handleIntent action = " + action);
		Bundle bundle = intent.getExtras();
		int actionType = -1;
		if (UNSOL_NEW_IM_SESS_INVITE_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_NEW_IM_SESSION_INVATE;
		} else if (UNSOL_NEW_IM_MSG_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_NEW_IM;
		} else if (UNSOL_SESS_INVITE_STAT_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_SESSION_STATE;
		} else if (UNSOL_MSG_SEND_STAT_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_SEND_STATE;
		} else if (UNSOL_NEW_FILE_TRANS_INVITE_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_NEW_FILE_TRANS_INVATE;
		} else if (UNSOL_FILE_SESS_STAT_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_FILE_SESS_STATE;
		} else if (UNSOL_FILE_TRANS_STAT_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_FILE_TRANS_STATE;
		} else if (UNSOL_FILE_STFWD_STAT_ACTION.equals(action)) {
			
		} else if (UNSOL_FILE_STFWD_TRANS_STAT_ACTION.equals(action)) {
			
		} else if (UNSOL_NEW_ISHARE_INVITE_ACTION.equals(action)) {
			
		} else if (UNSOL_ISHARE_SESS_STAT_ACTION.equals(action)) {
			
		} else if (UNSOL_ISHARE_TRANS_STAT_ACTION.equals(action)) {
			
		} else if (UNSOL_NEW_GEO_LOC_INVITE_ACTION.equals(action)) {
			
		} else if (UNSOL_GEO_LOC_SESS_STAT_ACTION.equals(action)) {
			
		} else if (UNSOL_NEW_GEO_LOC_MSG_ACTION.equals(action)) {
			
		} else if (UNSOL_NEW_AUTO_CONFIG_ACTION.equals(action)) {
			
		} else if (UNSOL_NEW_IM_STFWD_INVITE_ACTION.equals(action)) {
			
		} else if (UNSOL_STFWD_INVITE_STAT_ACTION.equals(action)) {
			
		} else if (UNSOL_TMP_GROUP_STAT_ACTION.equals(action)) {
			actionType = RcsMessage.RCS_MESSAGE_TMP_GROUP_STATE;
		}
		
		int sessId = -1;
		int cookieId = -1;
		long time = 0;
		int userType = 0;
		int errCode = -1;
		int sessStateCode = -1;
		int phoneId = 1;
		int size = 0;
		String fileName = "";
		String uri = "";
		String messageText = "";

		int transferedSize = 0;
		int totalSize = 0;
  		switch (actionType) {
		case RcsMessage.RCS_MESSAGE_NEW_IM_SESSION_INVATE:
			if (bundle.containsKey(RcsMessage.SESS_ID_STR)) {
				sessId = bundle.getInt(RcsMessage.SESS_ID_STR);
			}

			if (bundle.containsKey(RcsMessage.URI_STR)) {
				uri = bundle.getString(RcsMessage.URI_STR);
			}

			if (bundle.containsKey(RcsMessage.TEXT_STR)) {
				messageText = bundle.getString(RcsMessage.TEXT_STR);
			}
			if (bundle.containsKey(RcsMessage.TIME_STR)) {
				time = bundle.getLong(RcsMessage.TIME_STR);
			}
			if (bundle.containsKey(RcsMessage.USER_TYPE_STR)) {
				userType = bundle.getInt(RcsMessage.USER_TYPE_STR);
				if (userType == 0) {
					userType = (int) bundle.getLong(RcsMessage.USER_TYPE_STR);
				}
			}
			RcsNewImSessionInvateMessage message = new RcsNewImSessionInvateMessage(sessId, uri, messageText, time, userType);
			if (userType == RcsMiscInterface.USER_TYPE_TMP_GRP) {
				setGrpParam(uri, bundle, message);
			}
			
			dispatchMessage(RcsMessage.RCS_MESSAGE_NEW_IM_SESSION_INVATE, message);
			break;
		case RcsMessage.RCS_MESSAGE_NEW_IM:
			if (bundle.containsKey(RcsMessage.URI_STR)) {
				uri = bundle.getString(RcsMessage.URI_STR);
			}
			if (bundle.containsKey(RcsMessage.TEXT_STR)) {
				messageText = bundle.getString(RcsMessage.TEXT_STR);
			}
			if (bundle.containsKey(RcsMessage.TIME_STR)) {
				time = bundle.getLong(RcsMessage.TIME_STR);
			}
			if (bundle.containsKey(RcsMessage.USER_TYPE_STR)) {
				userType = bundle.getInt(RcsMessage.USER_TYPE_STR);
				if (userType == 0) {
					userType = (int) bundle.getLong(RcsMessage.USER_TYPE_STR);
				}
			}
			RcsNewImMessage newImMessage = new RcsNewImMessage(uri, messageText, userType, time);
			if (userType == RcsMiscInterface.USER_TYPE_TMP_GRP) {
				setGrpParam(uri, bundle, newImMessage);
			}
			dispatchMessage(RcsMessage.RCS_MESSAGE_NEW_IM, newImMessage);
			break;
		case RcsMessage.RCS_MESSAGE_SESSION_STATE:
			if (bundle.containsKey(RcsMessage.SESS_ID_STR)) {
				sessId = bundle.getInt(RcsMessage.SESS_ID_STR);
			}
			if (bundle.containsKey(RcsMessage.SESS_STATE_CODE)) {
				sessStateCode = bundle.getInt(RcsMessage.SESS_STATE_CODE);
			}
			if (bundle.containsKey(RcsMessage.USER_TYPE_STR)) {
				userType = bundle.getInt(RcsMessage.USER_TYPE_STR);
				if (userType == 0) {
					userType = (int) bundle.getLong(RcsMessage.USER_TYPE_STR);
				}
			}
			RcsSessionStateMessage sessStateMessage = new RcsSessionStateMessage(sessId, sessStateCode, userType);
			if (userType == RcsMiscInterface.USER_TYPE_TMP_GRP) {
				setGrpParam(uri, bundle, sessStateMessage);
			}
			
			dispatchMessage(RcsMessage.RCS_MESSAGE_SESSION_STATE, sessStateMessage);
			break;
		case RcsMessage.RCS_MESSAGE_SEND_STATE:
			if (bundle.containsKey(RcsMessage.SESS_ID_STR)) {
				sessId = bundle.getInt(RcsMessage.SESS_ID_STR);
			}
			if (bundle.containsKey(RcsMessage.COOKIE_STR)) {
				cookieId = bundle.getInt(RcsMessage.COOKIE_STR);
			}
			if (bundle.containsKey(RcsMessage.ERROR_CODE_STR)) {
				errCode = bundle.getInt(RcsMessage.ERROR_CODE_STR);
			}
			
			RcsSendStateMessage sendStateMessage = new RcsSendStateMessage(sessId, cookieId, errCode);
			dispatchMessage(RcsMessage.RCS_MESSAGE_SEND_STATE, sendStateMessage);
		break;
		case RcsMessage.RCS_MESSAGE_NEW_FILE_TRANS_INVATE:
			if (bundle.containsKey(RcsMessage.FILE_NAME)) {
				fileName = bundle.getString(RcsMessage.FILE_NAME);
			}
			if (bundle.containsKey(RcsMessage.SESS_ID_STR)) {
				sessId = bundle.getInt(RcsMessage.SESS_ID_STR);
			}
			if (bundle.containsKey(RcsMessage.URI_STR)) {
				uri = bundle.getString(RcsMessage.URI_STR);
			}
			if (bundle.containsKey(RcsMessage.USER_TYPE_STR)) {
				userType = bundle.getInt(RcsMessage.USER_TYPE_STR);
				if (userType == 0) {
					userType = (int) bundle.getLong(RcsMessage.USER_TYPE_STR);
				}
			}
			if (bundle.containsKey(RcsMessage.TIME_STR)) {
				time = bundle.getLong(RcsMessage.TIME_STR);
			}
			
			if (bundle.containsKey(RcsMessage.FILE_SIZE)) {
				size = bundle.getInt(RcsMessage.FILE_SIZE);
			}
			if (bundle.containsKey(RcsMessage.PHONE_ID)) {
				phoneId = bundle.getInt(RcsMessage.PHONE_ID);
			}
			RcsNewFileTransferInvateMessage newFileMessage = new RcsNewFileTransferInvateMessage(sessId, fileName, uri, size, time, phoneId);
			if (userType != RcsMiscInterface.USER_TYPE_SINGLE_CHAT) {
				setGrpParam(uri, bundle, newFileMessage);
			}
			
			newFileMessage.setUserType(userType);
			dispatchMessage(RcsMessage.RCS_MESSAGE_NEW_FILE_TRANS_INVATE, newFileMessage);
			break;
		case RcsMessage.RCS_MESSAGE_FILE_SESS_STATE:
			if (bundle.containsKey(RcsMessage.SESS_ID_STR)) {
				sessId = bundle.getInt(RcsMessage.SESS_ID_STR);
			}

			if (bundle.containsKey(RcsMessage.SESS_STATE_CODE)) {
				sessStateCode = bundle.getInt(RcsMessage.SESS_STATE_CODE);
			}
			
			RcsFileSessStateMessage fileSessStatMessage = new RcsFileSessStateMessage(sessId, sessStateCode);
			dispatchMessage(RcsMessage.RCS_MESSAGE_FILE_SESS_STATE, fileSessStatMessage);
			break;
		case RcsMessage.RCS_MESSAGE_FILE_TRANS_STATE:
			if (bundle.containsKey(RcsMessage.COOKIE_STR)) {
				cookieId = bundle.getInt(RcsMessage.COOKIE_STR);
			}
			if (bundle.containsKey(RcsMessage.SESS_ID_STR)) {
				sessId = bundle.getInt(RcsMessage.SESS_ID_STR);
			}
			if (bundle.containsKey(RcsMessage.ERROR_CODE_STR)) {
				errCode = bundle.getInt(RcsMessage.ERROR_CODE_STR);
			}
			
			if (bundle.containsKey(RcsMessage.FILE_TRANSFER_SIZE)) {
				transferedSize = bundle.getInt(RcsMessage.FILE_TRANSFER_SIZE);
			}
			if (bundle.containsKey(RcsMessage.FILE_TOTAL_SIZE)) {
				totalSize = bundle.getInt(RcsMessage.FILE_TOTAL_SIZE);
			}
			RcsFileTransStateMessage fileTransStatMessage = new RcsFileTransStateMessage(sessId, cookieId, errCode, transferedSize, totalSize);
			dispatchMessage(RcsMessage.RCS_MESSAGE_FILE_TRANS_STATE, fileTransStatMessage);
			break;
		case RcsMessage.RCS_MESSAGE_TMP_GROUP_STATE:
			
			RcsTmpGrpStateMessage tmpGrpMessage = new RcsTmpGrpStateMessage();
			dispatchMessage(RcsMessage.RCS_MESSAGE_TMP_GROUP_STATE, tmpGrpMessage);
			break;
		default:
			break;
		}
	}
	
	private void setGrpParam(String uri, Bundle bundle, RcsMessage message) {
		String orgName = "";
		String orgUri = "";
		String subject = "";
		String grpChatId = "";
		String grpIdent = "";
		String grpUri = "";
		
		if (bundle.containsKey(RcsMessage.ORIG_NAME_STR)) {
			orgName =  bundle.getString(RcsMessage.ORIG_NAME_STR);
		}
		if (bundle.containsKey(RcsMessage.ORIG_URI_STR)) {
			orgUri =  bundle.getString(RcsMessage.ORIG_URI_STR);
		}
		if (bundle.containsKey(RcsMessage.SUBJECT_STR)) {
			subject =  bundle.getString(RcsMessage.SUBJECT_STR);
		}
		
		if (bundle.containsKey(RcsMessage.GROUP_CHAT_ID)) {
			grpChatId =  bundle.getString(RcsMessage.GROUP_CHAT_ID);
		}
		if (bundle.containsKey(RcsMessage.SESSION_IDENTITY)) {
			grpIdent =  bundle.getString(RcsMessage.SESSION_IDENTITY);
		}
		
		if (bundle.containsKey(RcsMessage.GROUP_URI)) {
			grpUri =  bundle.getString(RcsMessage.GROUP_URI);
		}
		
		if (TextUtils.isEmpty(orgUri)) {
			orgUri = uri;
		}
		message.setGrpChatparam(orgName, orgUri, subject, grpChatId, grpIdent, grpUri);
	}
}
