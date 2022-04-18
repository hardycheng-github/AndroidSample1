package com.example.androidsample1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //static string TAG for logcat debug
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext = this;
    private SampleService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected");
            mService = ((SampleService.SampleBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //this section only when service crash
            Log.d(TAG, "onServiceDisconnected");
            mService = null;
        }
    };

    private Button btnStartService, btnStopService, btnBindService, btnUnbindService, btnGetRuntime;

    public MainActivity(){
        super();
        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                Log.d(TAG, "onStateChanged: " + event.name());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Lifecycle.onCreate");
        findView();
        initView();
        //Lifecycle only use for AppCompatActivity, not for Activity

    }

    private void findView(){
        //old compiler need class transform
        btnStartService = findViewById(R.id.btn_start_service);
        btnStopService = findViewById(R.id.btn_stop_service);
        btnBindService = findViewById(R.id.btn_bind_service);
        btnUnbindService = findViewById(R.id.btn_unbind_service);
        btnGetRuntime = findViewById(R.id.btn_get_runtime);
    }

    private void initView(){
        //without LAMBDA expression
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SampleService.class);
                startService(intent);
            }
        });
        //create interface with LAMBDA expression
        btnStopService.setOnClickListener(v-> {
            Intent intent = new Intent(mContext, SampleService.class);
            stopService(intent);
        });
        btnBindService.setOnClickListener(v-> {
            Intent intent = new Intent(this, SampleService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        });
        btnUnbindService.setOnClickListener(v-> unbindService(mConnection));
        btnGetRuntime.setOnClickListener(v-> {
            Toast.makeText(this, String.format("runtime: %ds", mService.getRuntimeSeconds())
                    , Toast.LENGTH_SHORT).show();
        });
    }
}