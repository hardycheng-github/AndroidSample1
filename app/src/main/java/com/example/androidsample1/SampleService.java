package com.example.androidsample1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SampleService extends Service {
    private static final String TAG = SampleService.class.getSimpleName();

    private long startTime;
    private boolean isRunning = false;
    private Thread runtimeCounter = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Runtime Counter Start!");
            while(isRunning){
                try {
                    Log.i(TAG, String.format("Service already running: %d secs", getRuntimeSeconds()));
                    runtimeCounter.sleep(1000);
                } catch (InterruptedException e){
                    Log.d(TAG, "Thread sleep interrupted!");
                }
            }
            Log.d(TAG, "Runtime Counter Finish.");
        }
    });

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
        Log.d(TAG, "Lifecycle.onBind");
        return new SampleBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent){
        Log.d(TAG, "Lifecycle.onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "Lifecycle.onCreate");
        isRunning = true;
        startTime = System.currentTimeMillis();
        runtimeCounter.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Lifecycle.onDestroy");
        isRunning = false;
        runtimeCounter.interrupt();
    }

    public int getRuntimeSeconds(){
        return (int)(System.currentTimeMillis() - startTime) / 1000;
    }
}