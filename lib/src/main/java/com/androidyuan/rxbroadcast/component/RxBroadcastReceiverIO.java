package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 * this is use io thread
 */
public abstract class RxBroadcastReceiverIO extends RxBroadcastReceiver {

    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadMode.THREAD_IO;
    }
}
