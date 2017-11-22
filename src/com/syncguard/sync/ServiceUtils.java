package com.syncguard.sync;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
/**
 * ���񹤾���
 * @author app
 *
 */
public class ServiceUtils {
	
	/*
	 * ������
	 */
	public static final String MAIN_SERVICE = "com.syncguard.sync.MainService";
	public static final int GRAY_SERVICE_ID=1001;
		
	/**
	 * �жϽ����Ƿ�����
	 * @return
	 */
	public static boolean isProessRunning(Context context, String proessName) {

		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : lists) {
			if (info.processName.equals(proessName)) {
				isRunning = true;
			}
		}

		return isRunning;
	}
	
	/**
	 * �ж�ĳ�������Ƿ��������еķ���
	 * 
	 * @param mContext
	 * @param serviceName
	 *            �ǰ���+��������������磺net.loonggg.testbackstage.TestService��
	 * @return true�����������У�false�������û����������
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM
				.getRunningServices(Integer.MAX_VALUE);
		if (myList.size() <= 0) {
			return false;
		}

		for (RunningServiceInfo runningServiceInfo : myList) {
			if (runningServiceInfo.service.getClassName().equals(serviceName)) {
				isWork = true;
				break;
			}
		}

		return isWork;
	}
}
