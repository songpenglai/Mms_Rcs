package com.android.rcs.message;

import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class RcsMessageService extends Service {

	private static String TAG = "RcsMessageService";
	
	private static HashMap<Integer, RcsMessageProcessor> rcsMessageMap = new HashMap<Integer, RcsMessageProcessor>();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	public static void registerMessage(int msgTpye, RcsMessageProcessor processor) {
		if (rcsMessageMap.containsKey(msgTpye)) {
			Log.w(TAG, "message map contains the key, instead, msgTpye = " + msgTpye);
		}
		rcsMessageMap.put(msgTpye, processor);
	}

	public static void unRegisterMessage(int msgTpye) {
		Log.w(TAG, "unRegisterMessage msgTpye = " + msgTpye);
		if (!rcsMessageMap.containsKey(msgTpye)) {
			Log.w(TAG, "unRegisterMessage not in map");
		}
		rcsMessageMap.remove(msgTpye);
	}
}
