package com.android.rcs.message;

public class RcsNewFileTransferInvateMessage extends RcsMessage {

	private static String TAG = "RcsNewFileTransferInvateMessage";
	private static String RCS_MESSAGE_RECEIVER = "com.android.rcs.message.RECEIVER";
	String fileName;
	int fileSize;
	String grpId;

	public RcsNewFileTransferInvateMessage(int sessId, String fileName, String uri, int size, long time,
			int phoneId) {
		this.sessId = sessId;
		this.fileName = fileName;
		this.uri = uri;
		this.fileSize = size;
		this.time = time;
		this.phoneId = phoneId;
		
	}
	
	public String getGrpId() {
		return grpId;
	}

	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
}
