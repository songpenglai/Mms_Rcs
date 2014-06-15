package com.android.rcs.message;



public class RcsSessionStateMessage extends RcsMessage {

	private static String TAG = "RcsSessionStateMessage";
	
	/** SESS_INVITE_SUSS. */
	public static final int STAT_ACCPETED = 0;
	/** SESS_INVITE_REJECT. */
	public static final int STAT_REJECTED = 1;
	/** SESS_CANCEL. */
	public static final int STAT_CANCELED = 3;
	/** SESS_RELEASED. */
	public static final int STAT_RELEASED = 4;
	
	public RcsSessionStateMessage(int sessId, int sessState, int userType) {
		this.sessId = sessId;
		this.sessStatCode = sessState;
		this.userType = userType;
	}
	
}
