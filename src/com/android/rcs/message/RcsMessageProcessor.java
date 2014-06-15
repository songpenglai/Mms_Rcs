package com.android.rcs.message;

import android.os.Handler;



public abstract class RcsMessageProcessor extends Handler {

	private static String TAG = "RcsMessageProcessor";
	
	public abstract void process(int messageType, RcsMessage rcsMessage);

}
