package com.androidyuan.rxbroadcast.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.androidyuan.rxbroadcast.exception.REventIsNullException;

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
public abstract class RxBroadcastReceiver extends BroadcastReceiver {

    //用这个字段方便对这个类，也可以自由切换的功能
    private int mThreadMode=0;

    RxOnReceive mEvent;

    private Context mAppContext;


    public RxBroadcastReceiver() {

        initData();
    }

    public RxBroadcastReceiver(int tMode) {

        mThreadMode=tMode;
        initData();
    }

    private void initData()
    {
        mEvent = new RxOnReceive() {
            @Override
            public void call(Intent o) {
                onReceive(mAppContext, o);
            }
        };
    }


    /**
     * onEvent执行时所在的线程
     *
     * @return 发送的线程
     */
    protected int getThreadMode() {

        return mThreadMode==0?BroadCastReceiveThreadMode.THREAD_IMMEDIATE:mThreadMode;
    }

    public Subscription send(Context context,Observable obs) {
        mAppContext=context;

        if (mEvent == null)//抛出异常
            throw new REventIsNullException();

        switch (getThreadMode()) {
            case BroadCastReceiveThreadMode.THREAD_ASYNC: {
                return obs.observeOn(Schedulers.newThread()).subscribe(mEvent);
            }
            case BroadCastReceiveThreadMode.THREAD_IO: {
                return obs.observeOn(Schedulers.io()).subscribe(mEvent);
            }
            case BroadCastReceiveThreadMode.THREAD_MAINTHREAD: {
                return obs.observeOn(AndroidSchedulers.mainThread()).subscribe(mEvent);
            }
            case BroadCastReceiveThreadMode.THREAD_COMPUTION: {
                return obs.observeOn(Schedulers.computation()).subscribe(mEvent);
            }
            default:
                return obs.subscribe(mEvent);
        }
    }



}
