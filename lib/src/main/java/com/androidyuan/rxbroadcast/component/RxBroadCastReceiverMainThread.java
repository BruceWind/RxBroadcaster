package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 */
public class RxBroadCastReceiverMainThread extends RxBroadCastReceiver {

    public RxBroadCastReceiverMainThread(RxOnReceive onEv) {

        super(onEv);
    }

    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadModel.THREAD_MAINTHREAD;
    }
}