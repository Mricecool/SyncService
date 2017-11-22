package com.syncguard.sync;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class JobSchedulerService extends JobService{

	@Override
	public boolean onStartJob(JobParameters params) {
		 mJobHandler.sendMessage(Message.obtain(mJobHandler,1, params));
		 return true;
	}

	@Override
	public boolean onStopJob(JobParameters params) {
		mJobHandler.removeMessages(1);
		return false;
	}
	
	private Handler mJobHandler = new Handler( new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			Log.e("JobSchedulerService", "重启服务");
			Toast.makeText(getApplicationContext(), 
		            "守护进程启动", Toast.LENGTH_SHORT )
		            .show();
		        jobFinished((JobParameters)msg.obj,false);
		        return true;
		}

	} );

}
