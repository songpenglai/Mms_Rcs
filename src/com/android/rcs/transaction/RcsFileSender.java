
package com.android.rcs.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.Telephony.Mms.Outbox;
import android.provider.Telephony.Sms;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.transaction.MessageSender;
import com.android.mms.ui.MessagingPreferenceActivity;
import com.android.rcs.util.RcsFileUtil;
import com.android.rcs.util.RcsModelMessage;
import com.google.android.mms.MmsException;

public class RcsFileSender implements MessageSender {
    protected final Context mContext;
    protected final int mNumberOfDests;
    private final String[] mDests;
    protected final long mThreadId;
    protected long mTimestamp;
    private String mFilePath;
    private String mFileType;
    private int mTransactionType;
    
    private boolean mSendGrpFile;
    private String mGroupChatId;
    private String mSessIdentity;
    
    private static final String TAG = "RcsFileSender";

    public RcsFileSender(Context context, String[] dests,
                 String filePath, String fileType, long threadId, int transactionType) {
        mContext = context;
        if (dests != null) {
            mNumberOfDests = dests.length;
            mDests = new String[mNumberOfDests];
            mFilePath = filePath;
            mFileType = fileType;
            mTransactionType = transactionType;
            System.arraycopy(dests, 0, mDests, 0, mNumberOfDests);
        } else {
            mNumberOfDests = 0;
            mDests = null;
        }
        mTimestamp = System.currentTimeMillis();
        mThreadId = threadId;
    }

    public boolean sendFile(long token) throws MmsException {
        return queueMessage(token);
    }
    
    public boolean sendFile(long token, String groupChatId, String sessIdentity) throws MmsException {
    	mSendGrpFile = true;
		mGroupChatId = groupChatId;
		mSessIdentity = sessIdentity;
		return queueMessage(token);
    }
    
    private boolean queueMessage(long token) throws MmsException {
        if ((mNumberOfDests == 0)) {
            // Don't try to send an empty message.
            throw new MmsException("Null dest.");
        }

        Uri uri = null;
        for (int i = 0; i < mNumberOfDests; i++) {
            try {
                if (LogTag.DEBUG_SEND) {
                    Log.v(TAG, "queueMessage mDests[i]: " + mDests[i] + " mThreadId: " + mThreadId);
                }

                int fileSize = RcsFileUtil.getFileSize(mFilePath);
                String fileName = RcsFileUtil.getFileName(mFilePath);
                RcsModelMessage rmm = new RcsModelMessage();
                rmm.setThread_id((int)mThreadId);
                rmm.setAddress(mDests[0]);
                rmm.setDate(System.currentTimeMillis());
                rmm.setDate_sent((int)System.currentTimeMillis());
                rmm.setType(Outbox.MESSAGE_BOX_OUTBOX);
                rmm.setFile_path(mFilePath);
                rmm.setFile_type(mFileType);
                rmm.setFile_name(fileName);
                rmm.setFile_size(fileSize);
                
                uri = mContext.getContentResolver().insert(Uri.parse("content://sms"), rmm.getContentValues());
            } catch (SQLiteException e) {
                if (LogTag.DEBUG_SEND) {
                    Log.e(TAG, "queueMessage SQLiteException", e);
                }
                SqliteWrapper.checkSQLiteException(mContext, e);
            }
        }
        int id = -1;
        if (uri != null) {
        	String Segment = uri.getLastPathSegment();
        	id = Integer.valueOf(Segment);
        	
        	StringBuilder address = new StringBuilder();
        	for (int i = 0; i < mDests.length; i++) {
        		address.append(mDests[i]).append(";");
			}
        	int len = address.length();
        	if (mSendGrpFile) {
        		RcsTransactionService.sendGrpFile(mContext, mTransactionType, id, mGroupChatId, mSessIdentity, mFilePath, mFileType, 1);
			} else {
				RcsTransactionService.sendFile(mContext, mTransactionType, id, address.toString().substring(0, len - 1), "send file", mFilePath, mFileType, 1);
			}
		}
        
        return false;
    }
    
    public int saveMessage() {

        if (mNumberOfDests == 0) {
            // Don't try to send an empty message.
            return -1;
        }
        Uri uri = null;
        for (int i = 0; i < mNumberOfDests; i++) {
            try {
                ContentValues values = new ContentValues();
                values.put(Sms.THREAD_ID, mThreadId);
                values.put(Sms.ADDRESS, mDests[0]);
                values.put(Sms.DATE, System.currentTimeMillis());
                values.put(Sms.TYPE, Outbox.MESSAGE_BOX_OUTBOX);
                
                
                uri = mContext.getContentResolver().insert(Uri.parse("content://sms"), values);
            } catch (SQLiteException e) {
                if (LogTag.DEBUG_SEND) {
                    Log.e(TAG, "queueMessage SQLiteException", e);
                }
                SqliteWrapper.checkSQLiteException(mContext, e);
            }
        }
        int id = -1;
        if (uri != null) {
        	String Segment = uri.getLastPathSegment();
        	id = Integer.valueOf(Segment);
//        	RcsTransactionService.sendImMessage(mContext, id, 0, RcsTransaction.RCS_SEND_PAGE_IM_TRANSACTION, mDests[0], mMessageText);
		}
        
        return id;
    
    }

    public void sendSessMessage() {
    	
    }

    private void log(String msg) {
        Log.d(LogTag.TAG, "[SmsMsgSender] " + msg);
    }

	@Override
	public boolean sendMessage(long token) throws MmsException {
		return false;
	}
}
