package com.android.rcs.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class RcsMessageBroadcastReceiver extends BroadcastReceiver {

	private static String TAG = "RcsMessageBroadcastReceiver";
	private static String RCS_MESSAGE_RECEIVER = "com.android.rcs.message.RECEIVER";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.d(TAG, "action = " + action);
		
		Intent messageServiceIntent = new Intent(intent);
		messageServiceIntent.setClass(context, RcsMessageService.class);
		context.startService(messageServiceIntent);
	}
	
}
