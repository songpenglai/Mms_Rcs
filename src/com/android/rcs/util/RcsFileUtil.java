package com.android.rcs.util;

import java.io.File;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Telephony.Sms;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.data.Conversation;
import com.android.mms.ui.ComposeMessageActivity;
import com.android.mms.ui.UriImage;
import com.android.rcs.message.RcsNewFileTransferInvateMessage;



public class RcsFileUtil {

	private static String TAG = "RcsFileUtil";
	public static String IM_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/rcs/";
	private static boolean isLogin = false;
	
	public static String getFilePathFromuri(Context context, int type, Uri fileUri) {
		String filePath = "";
		if (type == ComposeMessageActivity.REQUEST_CODE_ATTACH_IMAGE) {
			UriImage uImage = new UriImage(context, fileUri);
			filePath = uImage.getPath();
//			String uri = fileUri.decode(fileUri.toString());
			// ilePath = fileUri.getPath();
		}
		return filePath;
	}
	
	public static String getFileTypeFromUri(String filePath) {
		String fileType = "*/*";
		if (!TextUtils.isEmpty(filePath)) {
			int index = filePath.lastIndexOf(".");
			String stuff = filePath.substring(index + 1).toLowerCase();
			if ("jpg".equals(stuff) || "jpeg".equals(stuff) || "png".equals(stuff)) {
				fileType = "image/" + stuff;
			} else if ("mp3".equals(stuff) || "amr".equals(stuff) || "acc".equals(stuff)) {
				fileType = "audio/" + stuff;
			}
		}
		return fileType;
	}
	
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return "";
		}
		
		int index = filePath.lastIndexOf("/");
		String fileName = filePath.substring(index + 1);
		
		return fileName;
	}
	
	public static int getFileSize(String filepath) {
		if (TextUtils.isEmpty(filepath)) {
			return 0;
		}

		int fileSize = 0;
		File file = new File(filepath);
		fileSize = (int )file.length();
		return fileSize;
	}
	
	public static BitmapDrawable getImageDrawable(Context context, String filePath) {
		BitmapDrawable bm = new BitmapDrawable(context.getResources(), filePath);
		return bm;
	}
	
	public static String getImFilepath() {
		String path = RcsFileUtil.IM_FILE_PATH;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
	
	public static int saveAcceptFileInDb(Context context, RcsNewFileTransferInvateMessage fileInvate) {
		int threadid = -1;
		String address = fileInvate.getUri();
		String fileName = fileInvate.getFileName();
		int size = fileInvate.getFileSize();
		int sessId = fileInvate.getSessId();
		int phoneid = fileInvate.getPhoneId();
		long time = fileInvate.getTime();
		int userType = fileInvate.getUserType();
		if (userType != RcsMiscInterface.USER_TYPE_SINGLE_CHAT) {
			String grpChatId = fileInvate.getGrpChatId();
			Cursor cursorGroups = null;
			try {
				cursorGroups = RcsProviderInterface.queryGroups(context,
						"group_chat_id='" + grpChatId + "' OR group_id='" +  grpChatId + "'",
						RcsModelGroups.GROUPS_ARRAY);
				if (cursorGroups != null && cursorGroups.getCount() > 0
						&& cursorGroups.moveToFirst()) {
					threadid = cursorGroups.getInt(6);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursorGroups != null ) {
					cursorGroups.close();
				}
			}
		} else {
			threadid = (int) Conversation.getOrCreateThreadId(context, address);
		}
		
		if (time == 0) {
			time = System.currentTimeMillis();
		}
		String fileType = getFileTypeFromUri(fileName);
		RcsModelMessage rmm = new RcsModelMessage();
		rmm.setAddress(address);
		rmm.setThread_id(threadid);
		rmm.setFile_name(fileName);
		rmm.setFile_path(getImFilepath() + fileName);
		rmm.setFile_type(fileType);
		rmm.setFile_size(size);
		rmm.setDate(time);
		rmm.setType(Sms.MESSAGE_TYPE_INBOX);
		
		int msgId = -1;
		try {
			Uri uri = RcsProviderInterface.insertMessage(context, rmm.getContentValues());
			String Segment = uri.getLastPathSegment();
        	msgId = Integer.valueOf(Segment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return msgId;
	}
}
