package androidyuan.com.rxbroadcaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.androidyuan.rxbroadcast.RxLocalBroadCastManager;
import com.androidyuan.rxbroadcast.component.RxBroadCastReceiver;
import com.androidyuan.rxbroadcast.component.RxBroadCastReceiverBackground;

public class MainActivity extends AppCompatActivity {

    RxBroadCastReceiver broadCastReceiverAsync = new RxBroadCastReceiver(){

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("RxLocalBroadCastManager send:", intent.getAction()+ " ,onReceive is MainThraed=" + isMainTread());
        }
    };

    RxBroadCastReceiver broadCastReceiverback = new RxBroadCastReceiverBackground() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * 测试发现 多次 commit 都没有收到多条 解决 使用　LocalBroadCastManager　时带来的问题
             */
            Log.d("RxLocalBroadCastManager send:", intent.getAction() + " ,onReceive is MainThraed=" + isMainTread());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filters=new IntentFilter();

        filters.addAction("testthread");

        RxLocalBroadCastManager.getInstance(this).registerReceiver(broadCastReceiverAsync,filters);
        //testing multiple test registration.
        RxLocalBroadCastManager.getInstance(this).registerReceiver(broadCastReceiverback,filters);
        RxLocalBroadCastManager.getInstance(this).registerReceiver(broadCastReceiverback,filters);
        RxLocalBroadCastManager.getInstance(this).registerReceiver(broadCastReceiverback,filters);
        RxLocalBroadCastManager.getInstance(this).registerReceiver(broadCastReceiverback,filters);

        RxLocalBroadCastManager.getInstance(this).sendBroadcast(new Intent("testthread"));
    }


    private boolean isMainTread() {

        return Looper.myLooper() == Looper.getMainLooper();
    }

    // we need unRegister.
    @Override
    protected void onDestroy() {

        super.onDestroy();

        RxLocalBroadCastManager.getInstance(this).unregisterReceiver(broadCastReceiverAsync);
        RxLocalBroadCastManager.getInstance(this).unregisterReceiver(broadCastReceiverback);
    }
}
