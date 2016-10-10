package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 */
public abstract class RxBroadcastReceiverMainThread extends RxBroadcastReceiver {


    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadMode.THREAD_MAINTHREAD;
    }
}
