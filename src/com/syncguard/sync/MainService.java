package com.syncguard.sync;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import com.syncguard.MainActivity;
import com.syncguard.R;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * 显示Notification的服务
 * 
 * @author Administrator
 * 
 */
public class MainService extends Service {

	public static final String TAG = "MainService";
	/*
	 * 通知管理类
	 */
	private NotificationManager mNotificationManager;
	private Notification notification;
	private RemoteViews mRemoteViews;
	private final Timer timer = new Timer();
	private TimerTask task;
	private int i = 0;
	private MyHandler mHandler;


	@Override
	public void onCreate() {
		super.onCreate();
//		mHandler = new MyHandler(MainService.this);
//		initNotify();
//		initTimer();
//		timer.schedule(task, 0, 1000);
		grayGuard();
	}

	@Override
	public void onTrimMemory(int level) {
		// 内存不足可能会走此方法
		Log.e(TAG, "通过MainService-onTrimMemory重启服务");
	}
	
	
	 private void grayGuard() {
	      if (Build.VERSION.SDK_INT < 18) {
	          //API < 18 ，此方法能有效隐藏Notification上的图标
	          startForeground(ServiceUtils.GRAY_SERVICE_ID, new Notification());
	          Toast.makeText(this, "守护服务-版本"+Build.VERSION.SDK_INT, 1).show();
	      } else {
	          Intent innerIntent = new Intent(this, DaemonInnerService.class);
	          startService(innerIntent);
	          startForeground(ServiceUtils.GRAY_SERVICE_ID, new Notification());
	          Toast.makeText(this, "守护服务-版本"+Build.VERSION.SDK_INT, 1).show();
	      }

//	     //发送唤醒广播来促使挂掉的UI进程重新启动起来
//	     AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//	     Intent alarmIntent = new Intent();
//	     alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
//	     PendingIntent operation = PendingIntent.getBroadcast(this, 
//	         WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//	         alarmManager.setWindow(AlarmManager.RTC_WAKEUP, 
//	             System.currentTimeMillis(), ALARM_INTERVAL, operation);
//	     }else {
//	         alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 
//	             System.currentTimeMillis(), ALARM_INTERVAL, operation);
//	     }
	 }


	/*
	 * 初始化前台服务，startForeground提升Service优先级
	 */
	@SuppressLint("NewApi")
	private void initNotify() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(this);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);
		builder.setContentIntent(contentIntent);
		builder.setSmallIcon(R.drawable.ic_launcher);
		//builder.setNumber(++i);
		mRemoteViews = new RemoteViews(getPackageName(), R.layout.layout_notify);
		mRemoteViews.setImageViewResource(R.id.imageNotify,
				R.drawable.ic_launcher);
		mRemoteViews.setTextViewText(R.id.txtNotify, "服务已运行 " + i + " 秒");
		builder.setContent(mRemoteViews);
		notification = builder.build();

		startForeground(1, notification);
	}

	/*
	 * 初始化TimerTask
	 */
	private void initTimer() {
		if (task == null) {

			task = new TimerTask() {

				@Override
				public void run() {
					Message message = new Message();
					message.what = 1;
					mHandler.sendMessage(message);
				}
			};
		}
	}

	private static class MyHandler extends Handler {

		private final WeakReference<MainService> mWeakService;

		public MyHandler(MainService service) {
			mWeakService = new WeakReference<MainService>(service);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MainService service = mWeakService.get();
			if (service != null) {
				service.i++;
				service.mRemoteViews.setTextViewText(R.id.txtNotify, "服务已运行 "
						+ service.i + " 秒");
				service.mNotificationManager.notify(1, service.notification);
			}else{
				Log.e(TAG, "Service销毁了");
			}
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "服务已启动");
		// 如果系统资源充足时会重启（强杀进程无法重启）
		//Toast.makeText(this, "守护服务", 1).show();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 被系统强杀不会走onDestroy
		Log.e(TAG, "MainService服务销毁");
		mHandler.removeCallbacksAndMessages(null);
		stopForeground(true);
		timer.cancel();
		task.cancel();
		mNotificationManager.cancel(1);
	}

}
