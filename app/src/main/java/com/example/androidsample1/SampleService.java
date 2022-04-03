package com.example.androidsample1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SampleService extends Service {
    private static final String TAG = SampleService.class.getSimpleName();

    private long startTime;
    private boolean isRunning = false;

    public class SampleBinder extends Binder {
        private SampleService mService;
        public SampleBinder(SampleService service){
            mService = service;
        }
        public SampleService getService(){
            return mService;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new SampleBinder(this);
    }

    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate");
        isRunning = true;
        startTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning){
                    try {
                        Log.i(TAG, String.format("Service already running: %d secs", getRuntimeSeconds()));
                        Thread.sleep(1000);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        isRunning = false;
    }

    public int getRuntimeSeconds(){
        return (int)(System.currentTimeMillis() - startTime) / 1000;
    }
}