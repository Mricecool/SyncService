package com.syncguard.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * 授权器绑定服务（同步框架会在第一次访问授权器时启动该Service）
 * @author app
 *
 */
public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;
    @Override
    public void onCreate() {
    	
    	//实例化授权器
        mAuthenticator = new Authenticator(this);
    }
    /*
     * 当系统绑定服务时调用此方法
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
