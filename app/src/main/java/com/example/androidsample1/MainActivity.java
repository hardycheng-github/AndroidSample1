package com.example.androidsample1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //static string TAG for logcat debug
    private static final String TAG = MainActivity.class.getSimpleName();
    private SampleService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
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
    };

    private Button btnStartService, btnStopService, btnBindService, btnUnbindService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Lifecycle.onCreate");
        findView();
        initView();
    }

    private void findView(){
        //old compiler need class transform
        btnStartService = findViewById(R.id.btn_start_service);
        btnStopService = findViewById(R.id.btn_stop_service);
        btnBindService = findViewById(R.id.btn_bind_service);
        btnUnbindService = findViewById(R.id.btn_unbind_service);
    }

    private void initView(){
        //without LAMBDA expression
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSampleService();
            }
        });
        //create interface with LAMBDA expression
        btnStopService.setOnClickListener(v-> stopSampleService());
        btnBindService.setOnClickListener(v-> bindSampleService());
        btnUnbindService.setOnClickListener(v-> unbindSampleService());
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "Lifecycle.onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "Lifecycle.onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "Lifecycle.onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "Lifecycle.onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Lifecycle.onDestroy");
    }

    private void bindSampleService(){
        Intent intent = new Intent(this, SampleService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindSampleService(){
        unbindService(mConnection);
    }

    private void startSampleService(){
        Intent intent = new Intent(this, SampleService.class);
        startService(intent);
    }

    private void stopSampleService(){
        Intent intent = new Intent(this, SampleService.class);
        stopService(intent);
    }
}