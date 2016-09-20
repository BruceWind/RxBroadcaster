package com.androidyuan.rxbus.component;

import android.content.IntentFilter;
import com.androidyuan.rxbus.RxBroadCastManager;
import com.androidyuan.rxbus.exception.REventIsNullException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wei on 16/9/19.
 * <p>
 * 高度模拟 android 官方 BroadCastRecevier的用法
 * <p>
 * 这是个最基础recevier 没有线程切换的功能  发送的时候在哪个线程接受就在哪个线程
 */
public class RxBroadCastReceiver {

    IntentFilter filter;
    RxOnReceive mEvent;

    public RxBroadCastReceiver(RxOnReceive onEv) {

        filter = new IntentFilter();

        mEvent = onEv;
    }

    /**
     * onEvent执行时所在的线程
     *
     * @return 发送的线程
     */
    protected int getThreadMode() {

        return BroadCastReceiveThreadModel.THREAD_IMMEDIATE;
    }

    public Subscription send(Observable obs) {

        if (mEvent == null)//抛出异常
            throw new REventIsNullException();

        switch (getThreadMode()) {
            case BroadCastReceiveThreadModel.THREAD_ASYNC: {
                return obs.observeOn(Schedulers.newThread()).subscribe(mEvent);
            }
            case BroadCastReceiveThreadModel.THREAD_IO: {
                return obs.observeOn(Schedulers.io()).subscribe(mEvent);
            }
            case BroadCastReceiveThreadModel.THREAD_MAINTHREAD: {
                return obs.observeOn(AndroidSchedulers.mainThread()).subscribe(mEvent);
            }
            case BroadCastReceiveThreadModel.THREAD_COMPUTION: {
                return obs.observeOn(Schedulers.computation()).subscribe(mEvent);
            }
            default:
                return obs.subscribe(mEvent);
        }
    }


    public void putFilter(String action) {

        filter.addAction(action);
    }

    public void commit() {

        RxBroadCastManager.getInstance().registerReceiver(this, filter);
    }

    public void unRegister() {

        RxBroadCastManager.getInstance().unregisterReceiver(this);
    }


}
