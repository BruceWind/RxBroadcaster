package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 * this is use computation thread
 */
public abstract class RxBroadCastReceiverBackground extends RxBroadCastReceiver {

    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadMode.THREAD_COMPUTION;
    }
}
