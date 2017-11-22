package com.syncguard;

import com.syncguard.sync.JobSchedulerService;
import com.syncguard.sync.MainService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	public static final String AUTHORITY = "com.syncguard.provider";
	public static final String ACCOUNT_TYPE = "syncguard.com";

	public static final long SECONDS_PER_MINUTE = 5L;
	public static final long SYNC_INTERVAL_IN_MINUTES = 1L;
	public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES
			* SECONDS_PER_MINUTE;
	public static final String ACCOUNT = "守护账户";
	Account mAccount;
	ContentResolver mResolver;
	JobScheduler mJobScheduler;
	public static final int JOB_ID=1002;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		mAccount = CreateSyncAccount(this);
//
//		mResolver = getContentResolver();
//		//设置系统总同步开关开启
//		ContentResolver.setMasterSyncAutomatically(true);
//		//设置当前服务同步开关开启
//		ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
//
//		/*
//		 * 定期运行
//		 */
//		ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY,
//				SYNC_INTERVAL);
	//	initJobScheduler();
		
		startMyService();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startMyService(){
		Intent i = new Intent();
		i.setAction("com.syncguard.sync.MainService");
		startService(i);
	}
	
	@SuppressLint("NewApi")
	public void initJobScheduler(){
		Log.e("Buile Version", Build.VERSION.SDK_INT+"");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		     mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
		      JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
		              new ComponentName(getPackageName(), JobSchedulerService.class.getName()));

		      builder.setPeriodic(5000); //每隔60秒运行一次
		      builder.setRequiresCharging(false);//充电执行
		      builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
		      builder.setRequiresDeviceIdle(false);//长时间没有使用

		     if (mJobScheduler.schedule(builder.build()) <= 0) {
		         //If something goes wrong
		     }
		 }
	}

	/**
	 * Create a new dummy account for the sync adapter
	 *
	 * @param context
	 *            The application context
	 */
	public static Account CreateSyncAccount(Context context) {
		// Create the account type and default account
		Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
		// Get an instance of the Android account manager
		AccountManager accountManager = (AccountManager) context
				.getSystemService(ACCOUNT_SERVICE);
		/*
		 * Add the account and account type, no password or user data If
		 * successful, return the Account object, otherwise report an error.
		 */
		if (accountManager.addAccountExplicitly(newAccount, null, null)) {
			/*
			 * If you don't set android:syncable="true" in in your <provider>
			 * element in the manifest, then call context.setIsSyncable(account,
			 * AUTHORITY, 1) here.
			 */
		} else {
			/*
			 * The account exists or some other error occurred. Log this, report
			 * it, or handle it internally.
			 */
		}
		return newAccount;
	}

}
