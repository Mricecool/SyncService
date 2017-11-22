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
 * ͬ��������
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
	 * ���ڴ� Android 3.0 ��ʼ����˵ڶ�����ʽ�Ĺ���
	 * ��������֧�� parallelSyncs ����������������Ҫ����
	 * ������ʽ�Ĺ��캯������֤������
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
		Log.e(TAG, "���MAIN_SERVICE����");
//		if (!ServiceUtils
//				.isServiceWork(getContext(), ServiceUtils.MAIN_SERVICE)) {
//			Log.e(TAG, "MAIN_SERVICE��������");
			Intent i = new Intent();
			i.setAction("com.syncguard.sync.MainService");
			getContext().startService(i);
//		}

	}

}
