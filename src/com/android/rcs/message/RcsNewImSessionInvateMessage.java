package com.android.rcs.message;



public class RcsNewImSessionInvateMessage extends RcsMessage {

	private static String TAG = "RcsNewImSessionInvateMessage";
	private static String RCS_MESSAGE_RECEIVER = "com.android.rcs.message.RECEIVER";
	
	public RcsNewImSessionInvateMessage(int sessId, String uri, String text, long time, int userType) {
		this.sessId = sessId;
		this.uri = uri;
		this.text = text;
		this.time = time;
		this.userType = userType;
	}
}
