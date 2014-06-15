
package com.android.rcs.transaction;


import android.content.Context;
import android.os.Bundle;

public abstract class RcsTransaction {

    /**
     * Identifies send session mode IM.
     */
    public static final int RCS_SEND_SESSION_IM_TRANSACTION = 0;
    
    /**
     * Identifies send page mode IM.
     */
    public static final int RCS_SEND_PAGE_IM_TRANSACTION = 1;

    /**
     * Identifies send large IM.
     */
    public static final int RCS_SEND_LARGE_IM_TRANSACTION = 2;
    
    /**
     * Identifies receive IM.
     */
    public static final int RCS_RECEIVE_IM_TRANSACTION  = 3;

    /**
     * Identifies receive IM.
     */
    public static final int RCS_SEND_SESSION_GRP_IM_TRANSACTION  = 4;
    
    /**
     * Identifies send file.
     */
    public static final int RCS_SEND_FILE_TRANSACTION = 10;
    
    /**
     * Identifies receive file.
     */
    public static final int RCS_RECEIVE_FILE_TRANSACTION  = 11;
    
    /**
     * Identifies send file to grp.
     */
    public static final int RCS_SEND_GRP_FILE_TRANSACTION = 12;
    
    /**
     * Identifies receive grp file.
     */
    public static final int RCS_RECEIVE_GRP_FILE_TRANSACTION = 13;
    
    

    public static String RCS_TRANSACTION_TYPE = "rcs_transaction_type";
    
    public static String RCS_COOKIE_ID = "rcs_cookie_id";
    
    public static String RCS_SESSION_ID = "rcs_session_id";

    /**send page/session/large*/
    public static String RCS_SEND_TYPE = "rcs_send_type";
    
    public static String RCS_SEND_TEXT = "rcs_send_text";
    
    public static String RCS_SEND_URI = "rcs_send_uri";
    
    public static String RCS_SEND_TITLE = "rcs_send_title";
    
    public static String RCS_SEND_FILE_NAME = "rcs_send_file_name";
    
    public static String RCS_SEND_FILE_TYPE = "rcs_send_file_type";

    public static String RCS_FILE_SIZE = "rcs_file_size";

    public static String RCS_FILE_GRP_CHAT_ID = "rcs_file_grp_chat_id";

    public static String RCS_FILE_SESS_IDENTITY = "rcs_file_sess_identity";
    
    public static String RCS_TRANSACTION_PHONE_ID = "rcs_transaction_phone_id";
    
    protected Context mContext;
    protected String mId;
    int mTransactionType;
    public int mCookie;
    public Bundle mExtras;
    
    public RcsTransaction(Context context, int transactionType, Bundle extras) {
        mContext = context;
        mTransactionType = transactionType;
        mExtras = extras;
    }

    /**
     * An instance of Transaction encapsulates the actions required
     * during a MMS Client transaction.
     */
    public abstract void process();

    /**
     * Used to determine whether a transaction is equivalent to this instance.
     *
     * @param transaction the transaction which is compared to this instance.
     * @return true if transaction is equivalent to this instance, false otherwise.
     */
    public boolean isEquivalent(RcsTransaction transaction) {
        return mId.equals(transaction.mId);
    }

    /**
     * Get the type of the transaction.
     *
     * @return Transaction type in integer.
     */
    abstract public int getType();
}
