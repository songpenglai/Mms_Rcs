
package com.android.rcs.transaction;


import android.content.Context;

public abstract class RcsTransaction {

    /**
     * Identifies send session mode IM.
     */
    public static final int RCS_SEND_IM_TRANSACTION = 0;
    
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
     * Identifies send file.
     */
    public static final int RCS_SEND_FILE_TRANSACTION = 4;
    
    /**
     * Identifies receive file.
     */
    public static final int RCS_RECEIVE_FILE_TRANSACTION  = 5;
    
    /**
     * Identifies send file to grp.
     */
    public static final int RCS_SEND_GRP_FILE_TRANSACTION = 6;
    
    /**
     * Identifies receive grp file.
     */
    public static final int RCS_RECEIVE_GRP_FILE_TRANSACTION = 7;

    protected Context mContext;
    protected String mId;
    int mTransactionType;
    
    public RcsTransaction(Context context, int transactionType) {
        mContext = context;
        mTransactionType = transactionType;
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
