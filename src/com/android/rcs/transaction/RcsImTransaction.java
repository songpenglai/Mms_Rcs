

package com.android.rcs.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony.Mms.Outbox;
import android.text.TextUtils;
import android.util.Log;

import com.android.rcs.util.RcsMiscInterface;
import com.android.rcs.util.RcsModelMessage;
import com.android.rcs.util.RcsProviderInterface;

public class RcsImTransaction extends RcsTransaction implements Runnable {
    private static final String TAG = "RcsImTransaction";

    private static final int RETRY_COUNT= 3;
    private int mSessId;
    private int mPhoneId = 1;
    private String mText;
    private String mUri;
    

    /**
     * This constructor is only used for test purposes.
     */
    public RcsImTransaction(Context context, int transactionType, Bundle extras) {
        super(context, transactionType, extras);

    }

    /*
     * (non-Javadoc)
     * @see com.google.android.mms.pdu.Transaction#process()
     */
    @Override
    public void process() {
        new Thread(this, "RcsImTransaction").start();
    }

    public void run() {
    	// 获取参数
    	getExtras();
    	
    	String imdnStr = "";
    	int count = RETRY_COUNT;
    	while (count > 0) {
    		count--;
        	switch (mTransactionType) {
    		case RcsTransaction.RCS_SEND_SESSION_IM_TRANSACTION:
    			imdnStr = RcsMiscInterface.sendImText(mCookie, mSessId, mText, 2, mPhoneId);
    			break;
    		case RcsTransaction.RCS_SEND_PAGE_IM_TRANSACTION:
    			imdnStr = RcsMiscInterface.sendPageMsg(mCookie, mUri, mText, mPhoneId);
    			break;
    		case RcsTransaction.RCS_SEND_LARGE_IM_TRANSACTION:
    			imdnStr = RcsMiscInterface.sendLMsg(mCookie, mUri, mText, 0, mPhoneId);
    			break;
    		case RcsTransaction.RCS_SEND_SESSION_GRP_IM_TRANSACTION:
        			imdnStr = RcsMiscInterface.sendLMsg(mCookie, mUri, mText, 0, mPhoneId);
        			break;

    		default:
    			break;
    		}
        	
        	if (!TextUtils.isEmpty(imdnStr)) {
        		RcsModelMessage rmm = new RcsModelMessage();
        		rmm.setImdn_string(imdnStr);
        		RcsProviderInterface.updateMessage(mContext, rmm.getContentValues(), mCookie);
    			break;
    		} else {
    			Log.d(TAG, "message send failed: count = " + count);
			}
        	
		}

    }

	@Override
	public int getType() {
		return mTransactionType;
	}
	
	private void getExtras() {
		mCookie = mExtras.getInt(RCS_COOKIE_ID);
		mSessId = mExtras.getInt(RCS_SESSION_ID);
		mText = mExtras.getString(RCS_SEND_TEXT);
		mUri = mExtras.getString(RCS_SEND_URI);
		Log.d(TAG, toString());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("mCookie=").append(mCookie).append(", mSessId=")
				.append(mSessId).append(", mUri=").append(mUri)
				.append(", mText").append(mText);
		return sb.toString();
	}
}
