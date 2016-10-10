package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 * this is use computation thread
 */
public abstract class RxBroadcastReceiverBackground extends RxBroadcastReceiver {

    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadMode.THREAD_COMPUTION;
    }
}
