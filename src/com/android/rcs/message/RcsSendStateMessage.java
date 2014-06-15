package com.android.rcs.message;



public class RcsSendStateMessage extends RcsMessage {

	private static String TAG = "RcsSendStateMessage";
	
	int cookieId;
	int imdnType;
	public RcsSendStateMessage(int sessid, int cookieId, int errCode) {
		this.sessId = sessid;
		this.cookieId = cookieId;
		this.errCode = errCode;
	}
	public int getCookieId() {
		return cookieId;
	}
	public int getImdnType() {
		return imdnType;
	}
}
