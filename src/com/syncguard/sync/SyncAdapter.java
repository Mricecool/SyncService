package com.syncguard.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
/**
 * 同步适配器
 * @author app
 *
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

	public static final String TAG = "SyncAdapter";

	ContentResolver mContentResolver;
	
	Context context;

	/**
	 * Set up the sync adapter
	 */
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		/*
		 * If your app uses a content resolver, get an instance of it from the
		 * incoming Context
		 */
		context=context;
		mContentResolver = context.getContentResolver();
	}

	/**
	 * 由于从 Android 3.0 开始添加了第二种形式的构造
	 * 函数，来支持 parallelSyncs 参数，所以我们需要创建
	 * 两种形式的构造函数来保证兼容性
	 */
	public SyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);

		mContentResolver = context.getContentResolver();
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		// TODO Auto-generated method stub
		Log.e(TAG, "检测MAIN_SERVICE服务");
//		if (!ServiceUtils
//				.isServiceWork(getContext(), ServiceUtils.MAIN_SERVICE)) {
//			Log.e(TAG, "MAIN_SERVICE服务重启");
			Intent i = new Intent();
			i.setAction("com.syncguard.sync.MainService");
			getContext().startService(i);
//		}

	}

}
