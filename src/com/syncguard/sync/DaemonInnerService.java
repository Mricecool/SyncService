package com.syncguard.sync;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DaemonInnerService extends Service {
	
	public static final String TAG=DaemonInnerService.class.getSimpleName();
	
	
    @Override
    public void onCreate() {
        Log.i(TAG, "InnerService -> onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "InnerService -> onStartCommand");
        startForeground(ServiceUtils.GRAY_SERVICE_ID, new Notification());
        //stopForeground(true);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "InnerService -> onDestroy");
        super.onDestroy();
    }
}
