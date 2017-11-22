package com.syncguard.sync;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
/**
 * 服务工具类
 * @author app
 *
 */
public class ServiceUtils {
	
	/*
	 * 主服务
	 */
	public static final String MAIN_SERVICE = "com.syncguard.sync.MainService";
	public static final int GRAY_SERVICE_ID=1001;
		
	/**
	 * 判断进程是否运行
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
	 * 判断某个服务是否正在运行的方法
	 * 
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
	 * @return true代表正在运行，false代表服务没有正在运行
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
