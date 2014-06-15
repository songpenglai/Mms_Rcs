package com.android.rcs.message;

public class RcsMessage {

	private static String TAG = "RcsMessage";

	/** 自动配置 */
	public final static int RCS_MESSAGE_AUTO_CONFIG = 1;

	/** 正在编辑 */
	public final static int RCS_MESSAGE_ISCOMPOSING = 2;

	/** session状态 */
	public final static int RCS_MESSAGE_SESSION_STATE = 3;

	/** IM通知 */
	public final static int RCS_MESSAGE_IM_NOTIFY = 4;

	/** 新IM消息 */
	public final static int RCS_MESSAGE_NEW_IM = 5;

	/** 发送状态 */
	public final static int RCS_MESSAGE_SEND_STATE = 6;

	/** 发送状态 */
	public final static int RCS_MESSAGE_FILE_TRANSFER__STATE = 7;

	/** 临时群状态 */
	public final static int RCS_MESSAGE_TMP_GRP_STATE = 8;

	/** IM 共享请求 */
	public final static int RCS_MESSAGE_NEW_IM_SHARE_INVATE = 9;

	/** 新的IM 会话请求 */
	public final static int RCS_MESSAGE_NEW_IM_SESSION_INVATE = 10;

	/** IM通知 */
	public final static int RCS_MESSAGE_FILE_TRANSFER_INVATE = 11;

	/** 文件发送状态 */
	public final static int RCS_MESSAGE_NEW_FILE_TRANS_INVATE = 12;

	/** 文件发送状态 */
	public final static int RCS_MESSAGE_FILE_SESS_STATE = 13;
	
	/** 文件发送状态 */
	public final static int RCS_MESSAGE_FILE_TRANS_STATE = 14;

	/** 文件发送状态 */
	public final static int RCS_MESSAGE_TMP_GROUP_STATE = 20;
	
	public static final String MSG_TYPE_STR = "msgType";
	public static final String CONTENT_TYPE_STR = "contentType";
	public static final String TEXT_STR = "text";
	public static final String NTFY_TYPE_STR = "notifyType";
	public static final String IMDN_TYPE_STR = "imdnType";
	public static final String IMDN_STRING_STR = "imdnString";
	public static final String ERROR_CODE_STR = "errorCode";
	public static final String SESS_STATE_CODE = "sessStateCode";
	public static final String SESS_ID_STR = "sessId";
	public static final String REPLSESS_ID_STR = "reSessId";
	public static final String ISREPLSESS_ID_STR = "isReSessId";
	public static final String COOKIE_STR = "cookie";
	public static final String IMDN_ID_STR = "imdnId";
	

	public static final String FILE_NAME = "fileName";
	public static final String FILE_SIZE = "fileSize";
	public static final String PHONE_ID = "phoneId";
	
	public static final String TMP_GROUP_ID_STR = "tmpGroupId";

	public static final String ORIG_NAME_STR = "origName";
	public static final String ORIG_URI_STR = "origUri";
	public static final String TIME_STR = "time";
	public static final String NICK_NAME_STR = "nickName";
	public static final String URI_STR = "uri";
	public static final String USER_TYPE_STR = "userType";
	public static final String SUBJECT_STR = "subject";
	public static final String SESSION_IDENTITY = "sessionIdentity";
	public static final String GROUP_CHAT_ID = "GroupChatId";
	public static final String GROUP_URI = "GrpUri";
	public static final String FILE_TRANSFER_SIZE = "transferedSize";
	public static final String FILE_TOTAL_SIZE = "totalSize";

	public int sessId;

	public String uri;

	public String text;
	
	public long time;

	public int messageType;

	public int phoneId;
	
	public int errCode;

	public int userType;
	
	int sessStatCode;
	
	public String orgName = "";
	public String orgUri = "";
	public String subject = "";
	public String grpChatId = "";
	public String grpIdent = "";
	public String grpUri = "";
	
	public int getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(int phoneId) {
		this.phoneId = phoneId;
	}

	public int getSessId() {
		return sessId;
	}

	public void setSessId(int sessionId) {
		this.sessId = sessionId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getSessStatCode() {
		return sessStatCode;
	}

	public void setSessStatCode(int sessStatCode) {
		this.sessStatCode = sessStatCode;
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
	
	public void setGrpChatparam(String orgName, String orgUri, String subject, String grpChatId, String grpIdent, String grpUri) {
		this.orgName = orgName;
		this.orgUri = orgUri;
		this.subject = subject;
		this.grpChatId = grpChatId;
		this.grpIdent = grpIdent;
		this.grpUri = grpUri;
	}
}
