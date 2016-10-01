package com.androidyuan.rxbroadcast.component;

/**
 * Created by wei on 16/9/19.
 * this is use io thread
 */
public class RxBroadCastReceiverIO extends RxBroadCastReceiver {

    public RxBroadCastReceiverIO(RxOnReceive onEv) {

        super(onEv);
    }

    @Override
    protected int getThreadMode() {

        return BroadCastReceiveThreadModel.THREAD_IO;
    }
}
