package com.android.rcs.message;

import java.util.HashMap;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class RcsMessageBroadcastReceiver extends BroadcastReceiver {

	private static String TAG = "RcsMessageBroadcastReceiver";
	private static String RCS_MESSAGE_RECEIVER = "com.android.rcs.message.RECEIVER";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (!action.equals(RCS_MESSAGE_RECEIVER)) {
            return;
        }
	}
	
}
