package com.android.rcs.session;

import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Inbox;
import android.provider.Telephony.Threads;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.mms.data.Conversation;
import com.android.mms.transaction.MessagingNotification;
import com.android.rcs.message.RcsFileSessStateMessage;
import com.android.rcs.message.RcsFileTransStateMessage;
import com.android.rcs.message.RcsMessage;
import com.android.rcs.message.RcsMessageProcessor;
import com.android.rcs.message.RcsMessageService;
import com.android.rcs.message.RcsNewFileTransferInvateMessage;
import com.android.rcs.message.RcsNewImMessage;
import com.android.rcs.message.RcsNewImSessionInvateMessage;
import com.android.rcs.message.RcsSendStateMessage;
import com.android.rcs.message.RcsSessionStateMessage;
import com.android.rcs.transaction.RcsTransactionService;
import com.android.rcs.util.RcsMiscInterface;
import com.android.rcs.util.RcsModelGroups;
import com.android.rcs.util.RcsModelMessage;
import com.android.rcs.util.RcsProviderInterface;



public class RcsSessionManager {

	private static String TAG = "RcsSessionmanager";
	
	private static RcsSessionManager pThis;
	
	private static Context mContext;
	
	RcsSessStatelistener mStatelistener;

	private HashMap<String, RcsSessionEntity> mSessMap = new HashMap<String, RcsSessionEntity>();
	private HashMap<String, RcsSessionEntity> mGrpMap = new HashMap<String, RcsSessionEntity>();

	private HashMap<Integer, Integer> mRecvFileMap = new HashMap<Integer, Integer>();
	public static RcsSessionManager getDefault(Context context) {
		mContext = context;
		if (pThis == null) {
			pThis = new RcsSessionManager();
		}
		
		return pThis;
	}
	
	public void init() {
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_NEW_IM_SESSION_INVATE, processor);
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_SESSION_STATE, processor);
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_IM_NOTIFY, processor);
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_NEW_IM, processor);
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_SEND_STATE, processor);
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_TMP_GRP_STATE, processor);
		
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_NEW_FILE_TRANS_INVATE, processor);
		RcsMessageService.registerMessage(RcsMessage.RCS_MESSAGE_FILE_TRANS_STATE, processor);
	}
	
	public int getSessId(String uri) {
		int ret = -1;
		Iterator iter = mSessMap.keySet().iterator();
		while (iter.hasNext()) {
		    String key = (String) iter.next();
		    RcsSessionEntity val = (RcsSessionEntity) mSessMap.get(key);
		    if (uri.equals(val.uri) && isNumeric(key)) {
		    	ret = Integer.valueOf(key);
				break;
			}
		}
		return ret;
	}
	
	public int getGrpChatSessId(String grpChatId) {
		int ret = -1;
		if (!TextUtils.isEmpty(grpChatId) && mGrpMap.containsKey(grpChatId)) {
			RcsSessionEntity entity = mGrpMap.get(grpChatId);
			ret = entity.getSessId();
		}
		return ret;
	}

	public void setGrpEntity(int sessId, String uri, RcsModelGroups groupsModel) {
		RcsSessionEntity entity = new RcsSessionEntity(sessId, "");
		entity.setGrpChatParam("", groupsModel.getOrganicer_phone(),
				groupsModel.getName(), groupsModel.getGroup_chat_id(),
				groupsModel.getSession_idertity(), groupsModel.getGroup_id());
	}
	
	public RcsSessionEntity getGrpEntity(String grpChatId) {
		RcsSessionEntity entity = null;
		if (!TextUtils.isEmpty(grpChatId) && mGrpMap.containsKey(grpChatId)) {
			entity = mGrpMap.get(grpChatId);
		}
		return entity;
	}
	
	public String getGrpChatId(String uri) {
		String ret = "";
		Iterator iter = mSessMap.keySet().iterator();
		while (iter.hasNext()) {
		    String key = (String) iter.next();
		    RcsSessionEntity val = (RcsSessionEntity) mSessMap.get(key);
		    if (uri.equals(val.uri) && isNumeric(key)) {
//		    	ret = Integer.valueOf(key);
				break;
			}
		}
		return ret;
	}
	
	public static boolean isNumeric(String str) {
		  for (int i = str.length();--i>=0;){   
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
		 }
	
	public int createSessContainMsg (int cookie, String titleString, String message, String uri, StringBuffer imdnId, int mPhoneId) {
		int sessId = RcsMiscInterface.createSessContainMsg(cookie, titleString, message, uri, imdnId, mPhoneId);
		if (sessId > 0) {
			RcsSessionEntity entity = new RcsSessionEntity(sessId, uri);
			mSessMap.put(String.valueOf(sessId), entity);
		}
		return sessId;
	}
	
	RcsMessageProcessor processor = new RcsMessageProcessor() {
		
		@Override
		public void process(int messageType, RcsMessage rcsMessage) {
			Message msg = new Message();
			msg.what = messageType;
			msg.obj = rcsMessage;
			this.sendMessage(msg);
			
		}

		@Override
		public void handleMessage(Message msg) {
			int sessId;
			String uriNumber;
			long time;
			RcsModelMessage rmm;
			switch (msg.what) {
			case RcsMessage.RCS_MESSAGE_NEW_IM_SESSION_INVATE:
				RcsNewImSessionInvateMessage newImSessInvate = (RcsNewImSessionInvateMessage) msg.obj;
				uriNumber = newImSessInvate.getUri();
				sessId = newImSessInvate.getSessId();
				RcsSessionEntity entity = new RcsSessionEntity(sessId, uriNumber);
				RcsMiscInterface.acceptSess(sessId, 1);
				
				time = newImSessInvate.getTime();
				if (time == 0) {
					time = System.currentTimeMillis();
				}
				int userType = newImSessInvate.getUserType();
				if (userType == RcsMiscInterface.USER_TYPE_TMP_GRP || userType == RcsMiscInterface.USER_TYPE_GRP) {
					String orgName = "";
					String orgUri = newImSessInvate.getOrgUri();
					String subject = newImSessInvate.getSubject();
					String grpChatId = newImSessInvate.getGrpChatId();
					String grpIdent = newImSessInvate.getGrpIdent();
					String grpId = newImSessInvate.getGrpUri();
					String where = "group_chat_id='"+grpChatId + "'";
					Cursor c = RcsProviderInterface.queryGroups(mContext, where, new String[] {"group_chat_id"});
					if (!hasGroupRecode(c, grpChatId)) {
						// 2
						String group_chat_id = String.valueOf(System.currentTimeMillis());
						long threadId = Conversation.getOrCreateThreadId(mContext, group_chat_id);
						
						RcsModelGroups rmg = new RcsModelGroups();
						rmg.setThread_id((int)threadId);
						
						rmg.setName(subject);
						rmg.setType(userType);
						rmg.setOrganicer_phone(orgUri);
						rmg.setBegin_time(time);
						rmg.setGroup_chat_id(grpChatId);
						rmg.setGroup_id(grpId);
						rmg.setSession_idertity(grpIdent);
						// 2
//						Values.put("address", System.currentTimeMillis());
						RcsProviderInterface.insertGroups(mContext, rmg.getValues());
						
						// 用threadid把recipientId查出来
						long recipientId = 0;
						Cursor cursor = null;
						try {
							cursor = mContext.getContentResolver().query(
									Threads.CONTENT_URI.buildUpon()
											.appendQueryParameter("simple", "true").build(),
									new String[] { Threads.RECIPIENT_IDS },
									"_id=" + Long.toString(threadId), null, null);

							if (cursor != null && cursor.getCount() > 0) {
								if (cursor.moveToFirst()) {
									recipientId = cursor.getLong(0);
								}
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (cursor != null) {
								cursor.close();
							}
						}
						
					    if (TextUtils.isEmpty(subject)) {
					    	subject = "群聊";
						}
						// 更新CanonicalAddresses表里的address字段为群聊的名称
				        final ContentValues values = new ContentValues();
				        values.put(Telephony.CanonicalAddressesColumns.ADDRESS, subject);

				        final StringBuilder buf = new StringBuilder(Telephony.CanonicalAddressesColumns._ID);
				        buf.append('=').append(recipientId);

				        final Uri uri = ContentUris.withAppendedId(Uri.parse("content://mms-sms/canonical-address"), recipientId);
				        final ContentResolver cr = mContext.getContentResolver();

				        new Thread("updateCanonicalAddressInDb") {
				            public void run() {
				                cr.update(uri, values, buf.toString(), null);
				            }
				        }.start();
				        
				        MessagingNotification.blockingUpdateNewMessageIndicator(mContext, threadId, false);

					}
					
					entity.setGrpChatParam(orgName, orgUri, subject, grpChatId, grpIdent, grpId);
					mGrpMap.put(grpChatId, entity);
			        
//	            	Intent intent = new Intent();
//	            	intent.setClass(mContext, ComposeMessageActivity.class);
//					intent.putExtra(ComposeMessageActivity.GRP_CHAT, true);
//					intent.putExtra(ComposeMessageActivity.GRP_SUBJECT, subject);
//					intent.putExtra(ComposeMessageActivity.GRP_CHAT_ID, grpChatId);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////					intent.putExtra(ComposeMessageActivity.GRP_RECIPIENTS, recipients);
//	                mContext.startActivity(intent);
				} else {
					mSessMap.put(String.valueOf(sessId), entity);
				}
				String messageText = newImSessInvate.getText();
				if (!TextUtils.isEmpty(messageText)) {
					rmm = new RcsModelMessage();
					String address = newImSessInvate.getUri();
					long threadid = Conversation.getOrCreateThreadId(mContext, address);
					rmm.setThread_id((int)threadid);
					rmm.setBody(messageText);
					rmm.setType(Sms.MESSAGE_TYPE_INBOX);
					rmm.setDate(time);
					RcsProviderInterface.insertMessage(mContext, rmm.getContentValues());
				}
				
				break;
			case RcsMessage.RCS_MESSAGE_SESSION_STATE:
				RcsSessionStateMessage sessionState = (RcsSessionStateMessage) msg.obj;
				sessId = sessionState.getSessId();
				int sessStateCode = sessionState.getSessStatCode();
				userType = sessionState.getUserType();
				Log.d(TAG, "sessId = " + sessId + ", sessStateCode  = " + sessStateCode);
				switch (sessStateCode) {
				case RcsSessionStateMessage.STAT_ACCPETED:
					time = sessionState.getTime();
					if (time == 0) {
						time = System.currentTimeMillis();
					}
					if (userType == RcsMiscInterface.USER_TYPE_TMP_GRP || userType == RcsMiscInterface.USER_TYPE_GRP) {
						String orgName = "";
						String orgUri = sessionState.getOrgUri();
						String subject = sessionState.getSubject();
						String grpChatId = sessionState.getGrpChatId();
						String grpIdent = sessionState.getGrpIdent();
						String grpId = sessionState.getGrpUri();
						String where = "group_chat_id='"+grpChatId + "'";
						Cursor c = RcsProviderInterface.queryGroups(mContext, where, new String[] {"group_chat_id"});
						if (!hasGroupRecode(c, grpChatId)) {
							// 2
							String group_chat_id = String.valueOf(System.currentTimeMillis());
							long threadid = Conversation.getOrCreateThreadId(mContext, group_chat_id);
							
							RcsModelGroups rmg = new RcsModelGroups();
							rmg.setThread_id((int)threadid);
							rmg.setName(subject);
							rmg.setType(userType);
							rmg.setOrganicer_phone(orgUri);
							rmg.setBegin_time(time);
							rmg.setGroup_chat_id(grpChatId);
							rmg.setGroup_id(grpId);
							rmg.setSession_idertity(grpIdent);
							RcsProviderInterface.insertGroups(mContext, rmg.getValues());
							
							// 2
							
						}
						
						if (mStatelistener != null) {
							mStatelistener.sessStateChanged(sessStateCode, userType, grpChatId);
						}
						RcsSessionEntity entity1 = new RcsSessionEntity(sessId, orgUri);
						entity1.setGrpChatParam(orgName, orgUri, subject, grpChatId, grpIdent, grpId);
						mGrpMap.put(grpChatId, entity1);
					}
//					else {
//						mSessMap.put(String.valueOf(sessId), entity);
//					}
					break;
				case RcsSessionStateMessage.STAT_REJECTED:
					if (mSessMap.containsKey(String.valueOf(sessId))) {
						mSessMap.remove(String.valueOf(sessId));
					}
					break;
				case RcsSessionStateMessage.STAT_CANCELED:

					break;
				case RcsSessionStateMessage.STAT_RELEASED:
					if (userType == RcsMiscInterface.USER_TYPE_TMP_GRP || userType == RcsMiscInterface.USER_TYPE_GRP) {
						String grpChatId = sessionState.getGrpChatId();
						mGrpMap.remove(grpChatId);
					} else {
						mSessMap.remove(sessId);
					}
					break;
				default:
					break;
				}
				break;
			case RcsMessage.RCS_MESSAGE_SEND_STATE:
				RcsSendStateMessage sendState = (RcsSendStateMessage) msg.obj;
				sessId = sendState.getSessId();
				int cookieId = sendState.getCookieId();
				int errCode = sendState.getErrCode();
				rmm = new RcsModelMessage();
				if (errCode == 0) {
					rmm.setType(Sms.MESSAGE_TYPE_SENT);
				} else {
					rmm.setType(Sms.MESSAGE_TYPE_FAILED);
				}
				rmm.setError_code(errCode);
				RcsProviderInterface.updateMessage(mContext, rmm.getContentValues(), cookieId);
				break;
			case RcsMessage.RCS_MESSAGE_NEW_IM:
				RcsNewImMessage newMsg = (RcsNewImMessage) msg.obj;
				sessId = newMsg.getSessId();
				
				rmm = new RcsModelMessage();
				String address = newMsg.getUri();
//				address = "+8619001230274";
				
				time = newMsg.getTime();
				if (time == 0) {
					time = System.currentTimeMillis();
				}
				long threadid = Conversation.getOrCreateThreadId(mContext, address);
				rmm.setThread_id((int)threadid);
				rmm.setBody(newMsg.getText());
				rmm.setType(Inbox.MESSAGE_TYPE_INBOX);
				rmm.setDate(time);
				RcsProviderInterface.insertMessage(mContext, rmm.getContentValues());
				
				break;
			case RcsMessage.RCS_MESSAGE_NEW_FILE_TRANS_INVATE:
				RcsNewFileTransferInvateMessage fileInvate = (RcsNewFileTransferInvateMessage) msg.obj;
				sessId = fileInvate.getSessId();
				String fileName = fileInvate.getFileName();
				int phoneId = fileInvate.getPhoneId();
				int msgId = RcsTransactionService.acceptFile(mContext, fileInvate);
				if(msgId > 0) {
					mRecvFileMap.put(sessId, msgId);
				}
				break;
			case RcsMessage.RCS_MESSAGE_FILE_SESS_STATE:
				RcsFileSessStateMessage fileSessStat = (RcsFileSessStateMessage) msg.obj;
				int sessStatCode = fileSessStat.getSessStatCode();
				switch (sessStatCode) {
				case RcsSessionStateMessage.STAT_ACCPETED:

					break;
				case RcsSessionStateMessage.STAT_REJECTED:
					break;
				case RcsSessionStateMessage.STAT_CANCELED:

					break;
				case RcsSessionStateMessage.STAT_RELEASED:

					break;
				default:
					break;
				}
				break;
			case RcsMessage.RCS_MESSAGE_FILE_TRANS_STATE:
				RcsFileTransStateMessage fileTrans = (RcsFileTransStateMessage) msg.obj;
				sessId = fileTrans.getSessId();
				cookieId = fileTrans.getCookieId();
				errCode = fileTrans.getErrCode();
				int transferedSize = fileTrans.getTransferedSize();
				int totalSize = fileTrans.getTotalSize();
				if (totalSize == transferedSize) {
					rmm = new RcsModelMessage();
					if (errCode == 0) {
						rmm.setType(Sms.MESSAGE_TYPE_SENT);
					} else {
						rmm.setType(Sms.MESSAGE_TYPE_FAILED);
					}
					rmm.setError_code(errCode);
					if (cookieId <= 0) {
						cookieId = mRecvFileMap.get(sessId);
						mRecvFileMap.remove(sessId);
					} else {
						RcsProviderInterface.updateMessage(mContext, rmm.getContentValues(), cookieId);
					}
					
					Toast.makeText(mContext, "OK", Toast.LENGTH_SHORT).show();
				} else {
					String present = transferedSize * 100 / totalSize + "%100";
					Toast.makeText(mContext, present, Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private boolean hasGroupRecode(Cursor c, String grpChatId) {
		if (c == null || c.getCount() == 0) {
			return false;
		}
		
		 if (c.moveToFirst()) {
			 return grpChatId.equals(c.getString(0));
		}  else {
			return false;
		}
	}
	
	public void setmStatelistener(RcsSessStatelistener mStatelistener) {
		this.mStatelistener = mStatelistener;
	}
	
	public interface RcsSessStatelistener {
		public void sessStateChanged(int sessState, int userType, String grpChatId);
	}
	
	public class RcsSessionEntity {
		private int sessId;
		private String uri;
		
		String orgName = "";
		String orgUri = "";
		String subject = "";
		String grpChatId = "";
		String grpIdent = "";
		String grpUri = "";
		
		public RcsSessionEntity(int sessId, String uri) {
			this.sessId = sessId;
			this.uri = uri;
		}
		
		public void setGrpChatParam(String orgName, String orgUri, String subject, String grpChatId, String grpIdent, String grpUri) {
			this.orgName = orgName;
			this.orgUri = orgUri;
			this.subject = subject;
			this.grpChatId = grpChatId;
			this.grpIdent = grpIdent;
			this.grpUri = grpUri;
		}
		
		public int getSessId() {
			return sessId;
		}

		public void setSessId(int sessId) {
			this.sessId = sessId;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public String getOrgUri() {
			return orgUri;
		}

		public void setOrgUri(String orgUri) {
			this.orgUri = orgUri;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getGrpChatId() {
			return grpChatId;
		}

		public void setGrpChatId(String grpChatId) {
			this.grpChatId = grpChatId;
		}

		public String getGrpIdent() {
			return grpIdent;
		}

		public void setGrpIdent(String grpIdent) {
			this.grpIdent = grpIdent;
		}

		public String getGrpUri() {
			return grpUri;
		}

		public void setGrpUri(String grpUri) {
			this.grpUri = grpUri;
		}

	}
}
