package com.syncguard.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * ��Ȩ���󶨷���ͬ����ܻ��ڵ�һ�η�����Ȩ��ʱ������Service��
 * @author app
 *
 */
public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;
    @Override
    public void onCreate() {
    	
    	//ʵ������Ȩ��
        mAuthenticator = new Authenticator(this);
    }
    /*
     * ��ϵͳ�󶨷���ʱ���ô˷���
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
