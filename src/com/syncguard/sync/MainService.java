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
 * ��ʾNotification�ķ���
 * 
 * @author Administrator
 * 
 */
public class MainService extends Service {

	public static final String TAG = "MainService";
	/*
	 * ֪ͨ������
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
		// �ڴ治����ܻ��ߴ˷���
		Log.e(TAG, "ͨ��MainService-onTrimMemory��������");
	}
	
	
	 private void grayGuard() {
	      if (Build.VERSION.SDK_INT < 18) {
	          //API < 18 ���˷�������Ч����Notification�ϵ�ͼ��
	          startForeground(ServiceUtils.GRAY_SERVICE_ID, new Notification());
	          Toast.makeText(this, "�ػ�����-�汾"+Build.VERSION.SDK_INT, 1).show();
	      } else {
	          Intent innerIntent = new Intent(this, DaemonInnerService.class);
	          startService(innerIntent);
	          startForeground(ServiceUtils.GRAY_SERVICE_ID, new Notification());
	          Toast.makeText(this, "�ػ�����-�汾"+Build.VERSION.SDK_INT, 1).show();
	      }

//	     //���ͻ��ѹ㲥����ʹ�ҵ���UI����������������
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
	 * ��ʼ��ǰ̨����startForeground����Service���ȼ�
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
		mRemoteViews.setTextViewText(R.id.txtNotify, "���������� " + i + " ��");
		builder.setContent(mRemoteViews);
		notification = builder.build();

		startForeground(1, notification);
	}

	/*
	 * ��ʼ��TimerTask
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
				service.mRemoteViews.setTextViewText(R.id.txtNotify, "���������� "
						+ service.i + " ��");
				service.mNotificationManager.notify(1, service.notification);
			}else{
				Log.e(TAG, "Service������");
			}
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "����������");
		// ���ϵͳ��Դ����ʱ��������ǿɱ�����޷�������
		//Toast.makeText(this, "�ػ�����", 1).show();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ��ϵͳǿɱ������onDestroy
		Log.e(TAG, "MainService��������");
		mHandler.removeCallbacksAndMessages(null);
		stopForeground(true);
		timer.cancel();
		task.cancel();
		mNotificationManager.cancel(1);
	}

}
