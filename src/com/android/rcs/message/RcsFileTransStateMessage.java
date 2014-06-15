package com.android.rcs.message;

public class RcsFileTransStateMessage extends RcsMessage {

	private static String TAG = "RcsFileTransStateMessage";

	int cookieId;
	int imdnType;
	int transferedSize;
	int totalSize;

	public RcsFileTransStateMessage(int sessId, int cookieId, int errCode, int transferedSize, int totalSize) {
		this.sessId = sessId;
		this.cookieId = cookieId;
		this.transferedSize = transferedSize;
		this.totalSize = totalSize;
		this.errCode = errCode;
	}

	public int getCookieId() {
		return cookieId;
	}

	public int getImdnType() {
		return imdnType;
	}

	public int getTransferedSize() {
		return transferedSize;
	}

	public void setTransferedSize(int transferedSize) {
		this.transferedSize = transferedSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
}
