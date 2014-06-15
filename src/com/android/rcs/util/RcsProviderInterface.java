package com.android.rcs.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Sms;

public class RcsProviderInterface {

	
	// 插入信息
	public static Uri insertMessage(Context context, ContentValues values) {
        ContentResolver resolver = context.getContentResolver();

//        Uri insertedUri = SqliteWrapper.insert(context, resolver, Inbox.CONTENT_URI, values);

        Uri insertedUri = context.getContentResolver().insert(Uri.parse("content://sms"), values);
        return insertedUri;
	}
	
	// 根据id更新
	public static Uri updateMessage(Context context, ContentValues values, long messageId) {
		ContentResolver resolver = context.getContentResolver();
        Uri messageUri = ContentUris.withAppendedId(
                Sms.CONTENT_URI, messageId);

        SqliteWrapper.update(context, resolver, messageUri,
                            values, null, null);
        return messageUri;
		
	}
	
	// 删
	public static void deleteMessage(Context context, String selection, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();

        SqliteWrapper.delete(context, resolver, Sms.CONTENT_URI, selection, selectionArgs);
		
	}
	
	
	
	// 插入group信息
	// 插入的values用RcsModelGroups对象的getValues
	public static Uri insertGroups(Context context, ContentValues values) {
        Uri insertedUri = context.getContentResolver().insert(Uri.parse("content://sms/groups"), values);
        return insertedUri;
	}
	
	// 自己写where条件更新group
	public static void updateGroups(Context context, ContentValues values, String where) {
		ContentResolver resolver = context.getContentResolver();

        SqliteWrapper.update(context, resolver, Uri.parse("content://sms/groups"),
                            values, where, null);
		
	}
	
	// 自己写where条件删group
	public static void deleteGroups(Context context, String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();

        SqliteWrapper.delete(context, resolver, Uri.parse("content://sms/groups"), where, selectionArgs);
		
	}
	
	// 自己写where条件删group
	public static Cursor queryGroups(Context context, String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		Cursor c = null; 
		try {
			c = SqliteWrapper.query(context, resolver, Uri.parse("content://sms/groups"), selectionArgs, where, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return c;
	}
}
