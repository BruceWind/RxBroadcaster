package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 */
public abstract class RxBroadcastReceiverAsync extends RxBroadcastReceiver {

    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadMode.THREAD_ASYNC;
    }
}
