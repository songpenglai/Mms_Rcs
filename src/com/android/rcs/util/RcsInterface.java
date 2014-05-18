package com.android.rcs.util;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.mms.MmsApp;
import com.yulong.telephony.ims.im.ImManager;
import com.yulong.telephony.ims.im.PartpInfo;


public class RcsInterface {

	private static String TAG = "RcsInterface";
	private static String RCS_MESSAGE_RECEIVER = "com.android.rcs.message.RECEIVER";
	
	private static Context mContext = MmsApp.getApplication().getApplicationContext();
	public static int acceptFileTrans(int sessId, String fileName, int phoneId) {
		return ImManager.getDefault(mContext).acceptFileTrans(sessId, fileName, phoneId);
	}
	
	public static int acceptSess(int sessId, int phoneId) {
		return ImManager.getDefault(mContext).acceptSess(sessId, phoneId);
	}
	
	public static int acceptStFwd(int sessId, int phoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).acceptStFwd(sessId, phoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static int addUserToSess(int sessId, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).addUserToSess(sessId, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static int cancelFileTrans(int sessId, int mPhoneId) {
		int ret = -1;
		ret = ImManager.getDefault(mContext).cancelFileTrans(sessId, mPhoneId);

		return ret;
	}

	public static int cancelSess(int sessId, int mPhoneId) {
		int ret = -1;
		ret = ImManager.getDefault(mContext).cancelSess(sessId, mPhoneId);

		return ret;
	}
	
	public static int createGroupSess(String titleString, String grpUri, int mPhoneId) {
		int ret = -1;
		ret = ImManager.getDefault(mContext).createGroupSess(titleString, grpUri, mPhoneId);

		return ret;
	}
	
	public static int createSess(int cookie, String titleString, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).createSess(cookie, titleString, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int createSessContainMsg(int cookie, String titleString, String message, String uri, StringBuffer imdnId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).createSessContainMsg(cookie, titleString, message, uri, imdnId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int createTmpGroupSess(int cookie, String titleString, int tmpGrpId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).createTmpGroupSess(cookie, titleString, tmpGrpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int expelSessGrpUser(int sessId, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).expelSessGrpUser(sessId, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int expelSessGrpUser(int cookie, int sessId, int grpId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).extendSess(cookie, sessId, grpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocCancel(int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocCancel(infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPullAccept(int infoId, int infoType, String  freeText, double latitude, double longitude, float radius, String label, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPullAccept(infoId, infoType, freeText, latitude, longitude, radius, label, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPullReject(int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPullReject(infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPullViaFt(int dwCookie, String uri, int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPullViaFt(dwCookie, uri, infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPushAccept(int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPushAccept(infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPushBoth(int dwCookie, String freeText, double latitude, double longitude, float radius, String uri, String label, int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPushBoth(dwCookie, freeText, latitude, longitude, radius, uri, label, infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPushCoord(int dwCookie, String freeText, double latitude, double longitude, float radius, String uri, String label, int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPushCoord(dwCookie, latitude, longitude, radius, uri, label, infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPushReject(int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPushReject(infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int geoLocPushText(int dwCookie, String freeText, String uri, String label, int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).geoLocPushText(dwCookie, freeText, uri, label, infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int getMaxSize1to1(int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).getMaxSize1to1(mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int getPartpCompsStat(int dwPartpId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).getPartpCompsStat(dwPartpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static PartpInfo getPartpInfoById(int dwPartpId, int mPhoneId) {
		return ImManager.getDefault(mContext).getPartpInfoById(dwPartpId, mPhoneId);
	}
	
	public static PartpInfo getPartpInfoByUri(int sessId, String pcUri, int mPhoneId) {
		return ImManager.getDefault(mContext).getPartpInfoByUri(sessId, pcUri, mPhoneId);
	}
	
	public static int getPartpLstFindPartp(int dwLstId, String pcUri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).getPartpLstFindPartp(dwLstId, pcUri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static String getPartpName(int dwPartpId, int mPhoneId) {
		String ret = "";
		try {
			ret = ImManager.getDefault(mContext).getPartpName(dwPartpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int getPartpStat(int dwPartpId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).getPartpStat(dwPartpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static String getPartpUri(int dwPartpId, int mPhoneId) {
		String ret = "";
		try {
			ret = ImManager.getDefault(mContext).getPartpUri(dwPartpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int getRecvSize(int sessId, int mPhoneId) {
		return ImManager.getDefault(mContext).getRecvSize(sessId, mPhoneId);
	}
	
	public static int getSentSize(int sessId, int mPhoneId) {
		return ImManager.getDefault(mContext).getSentSize(sessId, mPhoneId);
	}
	
	public static int getSessGetPartp(int sessId, String name, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).getSessGetPartp(sessId, name, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int getSessPartpLstId(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).getSessPartpLstId(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int getSessPartpType(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).getSessPartpType(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static String getUserSelf(int mPhoneId) {
		String ret = "";
		try {
			ret = ImManager.getDefault(mContext).getUserSelf(mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int grpGeoLocPushCoord(int dwCookie, double latitude, double longitude, float radius, String pcGroupChatId, String pcSessIdentity, String label, int infoId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).grpGeoLocPushCoord(dwCookie, latitude, longitude, radius, pcGroupChatId, pcSessIdentity, label, infoId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int imIShareAccept(int sessId, String fileName, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imIShareAccept(sessId, fileName, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int imIShareCancel(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imIShareCancel(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int imIShareGetRecvSize(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imIShareGetRecvSize(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int imIShareGetSentSize(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imIShareGetSentSize(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int imIShareReject(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imIShareReject(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int imIShareSend(int cookie, String uri, String fileName, int fileType, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imIShareSend(cookie, uri, fileName, fileType, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public static int imIShareStop(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imIShareStop(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int imSessGetStat(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imSessGetStat(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int imSessReJoinGrp(int cookie, String pcGroupChatId, String pcSessIdentity, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imSessReJoinGrp(cookie, pcGroupChatId, pcSessIdentity, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int imSessReJoinGrpContainMsg(int cookie, String pcGroupChatId, String pcSessIdentity, String message, StringBuffer imdnId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imSessReJoinGrpContainMsg(cookie, pcGroupChatId, pcSessIdentity, message, imdnId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int imSessReStartGrp(int cookie, String pcGroupChatId, int partpLstId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imSessReStartGrp(cookie, pcGroupChatId, partpLstId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int imSessStartComposing(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imSessStartComposing(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int imSessStopComposing(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).imSessStopComposing(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int leaveSess(int sessId, int mPhoneId) {
		return ImManager.getDefault(mContext).leaveSess(sessId, mPhoneId);
	}

	public static int rejectFileTrans(int sessId, int mPhoneId) {
		return ImManager.getDefault(mContext).rejectFileTrans(sessId, mPhoneId);
	}

	public static int rejectSess(int sessId, int mPhoneId) {
		return ImManager.getDefault(mContext).rejectSess(sessId, mPhoneId);
	}

	public static int rejectStFwd(int sessId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).rejectStFwd(sessId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int sendDispNty(String imdnString, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).sendDispNty(imdnString, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int sendFile(int cookie, String uri, String title, String fileName, String fileType, int mPhoneId) {
		int ret = -1;
		ret = ImManager.getDefault(mContext).sendFile(cookie, uri, title, fileName, fileType, mPhoneId);

		return ret;
	}

	public static int sendFileToGrp(int cookie, int sessId, String pcFileName, String pcFileType, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).sendFileToGrp(cookie, sessId, pcFileName, pcFileType, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int sendFileToGrpWithoutSess(int cookie, String pcGroupChatId, String pcSessIdentity, String pcFileName, String pcFileType, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).sendFileToGrpWithoutSess(cookie, pcGroupChatId, pcSessIdentity, pcFileName, pcFileType, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static String sendImText(int cookie, int sessId, String text, int type, int mPhoneId) {
		String ret = "";
		ret = ImManager.getDefault(mContext).sendImText(cookie, sessId, text, type, mPhoneId);

		return ret;
	}

	public static String sendLMsg(int cookie, String userName, String text, int type, int mPhoneId) {
		String ret = "";
		try {
			ret = ImManager.getDefault(mContext).sendLMsg(cookie, userName, text, type, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static String sendPageMsg(int cookie, String uri, String text, int mPhoneId) {
		String ret = "";
		ret = ImManager.getDefault(mContext).sendPageMsg(cookie, uri, text, mPhoneId);

		return ret;
	}

	public static int setConfFctyUri(String confFactyUri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).setConfFctyUri(confFactyUri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int setImdnDeliEnable(boolean enable, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).setImdnDeliEnable(enable, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int setImdnDispEnable(boolean enable, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).setImdnDispEnable(enable, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int stopFileTrans(int sessId, int mPhoneId) {
		int ret = -1;
		ret = ImManager.getDefault(mContext).stopFileTrans(sessId, mPhoneId);

		return ret;
	}

	public static int tmpGrpAddUser(int grpId, String userName, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpAddUser(grpId, userName, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpCreate(int grpType, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpCreate(grpType, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpDelete(int grpId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpDelete(grpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpFindUser(int grpId, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpFindUser(grpId, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpGetSize(int grpId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpGetSize(grpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpGetType(int grpId, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpGetType(grpId, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpGetUser(int grpId, int index, Bundle partpInfo, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpGetUser(grpId, index, partpInfo, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpGetUser(int grpId, int index, StringBuffer userName, StringBuffer uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpGetUser(grpId, index, userName, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static int tmpGrpRemoveUser(int grpId, String uri, int mPhoneId) {
		int ret = -1;
		try {
			ret = ImManager.getDefault(mContext).tmpGrpRemoveUser(grpId, uri, mPhoneId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}
}
