package androidyuan.com.rxbroadcaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.androidyuan.rxbroadcast.RxLocalBroadcastManager;
import com.androidyuan.rxbroadcast.component.RxBroadcastReceiver;
import com.androidyuan.rxbroadcast.component.RxBroadcastReceiverBackground;

public class MainActivity extends AppCompatActivity {

    RxBroadcastReceiver broadcastReceiverAsync = new RxBroadcastReceiver(){

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("RxLocalBroadcastManager send:", intent.getAction()+ " ,onReceive is MainThraed=" + isMainTread());
        }
    };

    RxBroadcastReceiver broadcastReceiverback = new RxBroadcastReceiverBackground() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * 测试发现 多次 commit 都没有收到多条 解决 使用　LocalBroadCastManager　时带来的问题
             */
            Log.d("RxLocalBroadcastManager send:", intent.getAction() + " ,onReceive is MainThraed=" + isMainTread());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filters=new IntentFilter();

        filters.addAction("testthread");

        RxLocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverAsync,filters);
        //testing multiple test registration.
        RxLocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverback,filters);
        RxLocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverback,filters);
        RxLocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverback,filters);
        RxLocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverback,filters);

        RxLocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("testthread"));
    }


    private boolean isMainTread() {

        return Looper.myLooper() == Looper.getMainLooper();
    }

    // we need unRegister.
    @Override
    protected void onDestroy() {

        super.onDestroy();

        RxLocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverAsync);
        RxLocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverback);
    }
}
