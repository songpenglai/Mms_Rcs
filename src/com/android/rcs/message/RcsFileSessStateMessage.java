package com.android.rcs.message;



public class RcsFileSessStateMessage extends RcsMessage {

	private static String TAG = "RcsFileTransStateMessage";
	
	int cookieId;
	int imdnType;
	public RcsFileSessStateMessage(int sessId, int sessStatCode) {
		this.sessId = sessId;
		this.uri = uri;
		this.sessStatCode = sessStatCode;
	}
}
