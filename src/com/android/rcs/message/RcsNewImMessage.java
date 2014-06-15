package com.android.rcs.message;



public class RcsNewImMessage extends RcsMessage {

	private static String TAG = "RcsNewImMessage";
	String text;
	int userType;
	
	public RcsNewImMessage(String uri, String text, int userType, long time) {
		this.text = text;
		this.userType = userType;
		this.time = time;
		this.uri = uri;
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
}
