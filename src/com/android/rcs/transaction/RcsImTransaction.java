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
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.android.mms.MmsApp;
import com.android.mms.util.DownloadManager;

public class RcsImTransaction extends RcsTransaction implements Runnable {
    private static final String TAG = "RcsImTransaction";
    private static final boolean DEBUG = false;
    private static final boolean LOCAL_LOGV = false;

    private Uri mUri;
    private String mContentLocation;


    /**
     * This constructor is only used for test purposes.
     */
    public RcsImTransaction(Context context, int transactionType) {
        super(context, transactionType);

    }

    /*
     * (non-Javadoc)
     * @see com.google.android.mms.pdu.Transaction#process()
     */
    @Override
    public void process() {
        new Thread(this, "NotificationTransaction").start();
    }

    public void run() {
    	
    }

	@Override
	public int getType() {
		return mTransactionType;
	}
}
