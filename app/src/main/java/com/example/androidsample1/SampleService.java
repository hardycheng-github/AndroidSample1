package com.example.androidsample1;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;

// LifecycleService dependency: 'androidx.lifecycle:lifecycle-extensions'
public class SampleService extends LifecycleService {
    private static final String TAG = SampleService.class.getSimpleName();

    private long startTime;
    private Thread runtimeCounter = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Runtime Counter Start!");
            while(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)){
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

    public SampleService(){
        super();
        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                Log.d(TAG, "onStateChanged: " + event.name());
                if(event.equals(Lifecycle.Event.ON_START)){
                    startTime = System.currentTimeMillis();
                    runtimeCounter.start();
                } else if(event.equals(Lifecycle.Event.ON_STOP)){
                    startTime = 0;
                    runtimeCounter.interrupt();
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent); //always return null
        return new SampleBinder(this);
    }

    public int getRuntimeSeconds(){
        return (int)(System.currentTimeMillis() - startTime) / 1000;
    }
}