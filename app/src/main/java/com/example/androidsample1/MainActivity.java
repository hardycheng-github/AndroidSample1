package com.example.androidsample1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    //static string TAG for logcat debug
    private static final String TAG = MainActivity.class.getSimpleName();
    private SampleService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        bindSampleService();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void bindSampleService(){
        Intent intent = new Intent(this, SampleService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d(TAG, "onServiceConnected");
                try {
                    mService = ((SampleService.SampleBinder) iBinder).getService();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG, "onServiceDisconnected");
                mService = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }
}