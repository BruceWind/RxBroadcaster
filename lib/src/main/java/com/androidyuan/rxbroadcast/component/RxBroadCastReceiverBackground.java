package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 * this is use computation thread
 */
public class RxBroadCastReceiverBackground extends RxBroadCastReceiver {

    public RxBroadCastReceiverBackground(RxOnReceive onEv) {

        super(onEv);
    }

    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadModel.THREAD_COMPUTION;
    }
}
