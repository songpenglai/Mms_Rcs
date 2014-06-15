/*
 * Copyright (C) 2007-2008 Esmertec AG.
 * Copyright (C) 2007-2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.rcs.transaction;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.mms.R;
import com.android.rcs.util.RcsFileUtil;
import com.android.rcs.util.RcsMiscInterface;

/**
 * The NotificationTransaction is responsible for handling multimedia
 * message notifications (M-Notification.ind).  It:
 *
 * <ul>
 * <li>Composes the notification response (M-NotifyResp.ind).
 * <li>Sends the notification response to the MMSC server.
 * <li>Stores the notification indication.
 * <li>Notifies the TransactionService about succesful completion.
 * </ul>
 *
 * NOTE: This MMS client handles all notifications with a <b>deferred
 * retrieval</b> response.  The transaction service, upon succesful
 * completion of this transaction, will trigger a retrieve transaction
 * in case the client is in immediate retrieve mode.
 */
public class RcsFileTransaction extends RcsTransaction implements Runnable {

	private static final String TAG = "RcsFileTransaction";
    private static final boolean DEBUG = false;
    private static final boolean LOCAL_LOGV = false;

    private static final int RCS_FILE_GET_SEND_SIZE = 1;

    private static final int RCS_FILE_GET_RECV_SIZE = 2;
    
    int mSessId;
    
    private String mUri;

    private String mTitle;

    private String mFileName;

    private String mFileType;

    private String mFileTransId;
    
    private int mFileSize = 0;
    
    String mGroupChatId;

    String mSessIdentity;
    
    public RcsFileTransaction(Context context, int transactionType, Bundle extras) {
		super(context, transactionType, extras);
		// TODO Auto-generated constructor stub
	}

    @Override
    public void process() {
        new Thread(this, "RcsFileTransaction").start();
    }

	@Override
	public void run() {
		// 获取参数
		getExtras();
		
		int ret = -1;
		switch (mTransactionType) {
		case RCS_SEND_FILE_TRANSACTION:
			ret = RcsMiscInterface.sendFile(mCookie, mUri, mTitle, mFileName, mFileType, 1);
			if (ret > 0) {
				
			}
			break;
		case RCS_RECEIVE_FILE_TRANSACTION:
			ret = RcsMiscInterface.acceptFileTrans(mSessId, mFileName, 1);
			if (ret > 0) {
				
			}
			break;
		case RCS_SEND_GRP_FILE_TRANSACTION:
			ret = RcsMiscInterface.sendFileToGrpWithoutSess(mCookie, mGroupChatId, mSessIdentity, mFileName, mFileType, 1);
			if (ret > 0) {
				
			}
			break;

		default:
			break;
		}
		
	}
	
	private void getExtras() {
		mCookie = mExtras.getInt(RCS_COOKIE_ID);
		mUri = mExtras.getString(RCS_SEND_URI);
		mTitle = mExtras.getString(RCS_SEND_TITLE);
		mFileName = mExtras.getString(RCS_SEND_FILE_NAME);
		mFileType = mExtras.getString(RCS_SEND_FILE_TYPE);
		mSessId = mExtras.getInt(RCS_SESSION_ID);
		
		if (mTransactionType == RCS_SEND_FILE_TRANSACTION
				|| mTransactionType == RCS_SEND_GRP_FILE_TRANSACTION) {
			mFileSize = RcsFileUtil.getFileSize(mFileName);
		} else {
			mFileSize = mExtras.getInt(RCS_FILE_SIZE);
		}
		
		if (mTransactionType == RCS_SEND_GRP_FILE_TRANSACTION) {
			mGroupChatId = mExtras.getString(RCS_FILE_GRP_CHAT_ID);
			mSessIdentity = mExtras.getString(RCS_FILE_SESS_IDENTITY);
		}
		Log.d(TAG, toString());
	}
	

	public Handler mToastHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String str = null;

//			if (msg.what == TOAST_MSG_QUEUED) {
//				str = getString(R.string.message_queued);
//			} else if (msg.what == TOAST_DOWNLOAD_LATER) {
//				str = getString(R.string.download_later);
//			} else if (msg.what == TOAST_NO_APN) {
//				str = getString(R.string.no_apn);
//			}
//
//			if (str != null) {
//				Toast.makeText(RcsTransactionService.this, str,
//						Toast.LENGTH_LONG).show();
//			}
		}
	};
	
	@Override
	public int getType() {
		return mTransactionType;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("mCookie=").append(mCookie)
				.append(", mUri=").append(mUri)
				.append(", mTitle=").append(mTitle)
				.append(", mFileName").append(mFileName)
				.append(", mFileType").append(mFileType);
		return sb.toString();
	}
}
