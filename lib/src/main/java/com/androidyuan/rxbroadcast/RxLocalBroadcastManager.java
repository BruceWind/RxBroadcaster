package com.androidyuan.rxbroadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.SparseArray;

import com.androidyuan.rxbroadcast.component.RxBroadcastReceiver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wei on 16-9-10.
 * <p>
 * 高度模拟 LocalBroadCastManager 的用法 同时解决LocalBroadcast多次注册带来的bug
 */
public class RxLocalBroadcastManager {

    private static RxLocalBroadcastManager instance;

    private final Context mAppContext;


    SparseArray<List<RxBroadcastReceiver>> mSparseArrOnBroadCastReveive;
    CompositeSubscription mCompositeSubscription;

    private RxLocalBroadcastManager(Context context) {

        this.mAppContext=context.getApplicationContext();
        mSparseArrOnBroadCastReveive = new SparseArray<>();
        mCompositeSubscription = new CompositeSubscription();
    }

    public static RxLocalBroadcastManager getInstance(Context context) {

        if (instance == null) {
            instance = new RxLocalBroadcastManager(context);
        }
        return instance;
    }


    private synchronized void putRecevier(String filter, RxBroadcastReceiver broadCastReveive) {

        if (!TextUtils.isEmpty(filter) && broadCastReveive != null) {

            List<RxBroadcastReceiver> list = new ArrayList<>();
            if (isHaveKey(filter)) {
                list = mSparseArrOnBroadCastReveive.get(filter.hashCode());
            }

            if (!list.contains(broadCastReveive)) {//防止多次的注册 导致LocalBroadCastManger的那种多次commit的bug
                list.add(broadCastReveive);
            }

            if (!isHaveKey(filter)) {
                mSparseArrOnBroadCastReveive.put(filter.hashCode(), list);
            }
        }

    }


    /**
     * 暂时关闭使用 这个方法  只需要移除 BroadCastReceive即可满足现有需求
     * <p>
     * <p>
     * !!! 谨慎使用
     * 解绑
     *
     * @param filter
     */
    private synchronized void unRegister(String filter) {

        if (TextUtils.isEmpty(filter))
            return;

        if (isHaveKey(filter)) {
            mSparseArrOnBroadCastReveive.remove(filter.hashCode());
        }
    }

    private boolean isHaveKey(String filter) {

        return mSparseArrOnBroadCastReveive.indexOfKey(filter.hashCode()) > -1;
    }


    /**
     * 不带线程切换 功能
     * action的触发会在发送的observable所在线程线程执行
     *
     * @param intent
     */
    public void sendBroadcast(Intent intent) {

        if(intent==null)
            return;


        String filter=intent.getAction();

        if (TextUtils.isEmpty(filter))
            return;

        if (isHaveKey(filter)) {//设计的 就像  广播一样 发送出来 如果没有人接受 就丢弃了

            List<RxBroadcastReceiver> list = mSparseArrOnBroadCastReveive.get(filter.hashCode());

            for (RxBroadcastReceiver onEv : list) {

                Subscription sbus = onEv.send(mAppContext,Observable.just(intent));

                // Log.d("on send ,isUnsubscribed", sbus.isUnsubscribed() + "");
            }
        }
    }

    //自动容错 防止多次 registerReceive
    public void registerReceiver(RxBroadcastReceiver rxBroadcastReceiver, IntentFilter filters) {

        if (filters != null && rxBroadcastReceiver != null) {
            Iterator<String> iterator = filters.actionsIterator();
            if (iterator.hasNext()) {
                putRecevier(iterator.next(), rxBroadcastReceiver);
            }
        }
    }


    /**
     * 解绑
     *
     * @param rxBroadcastReceiver
     */
    public void unregisterReceiver(RxBroadcastReceiver rxBroadcastReceiver) {

        if (rxBroadcastReceiver == null)
            return;

        for (int i = 0; i < mSparseArrOnBroadCastReveive.size(); i++) {
            int key = mSparseArrOnBroadCastReveive.keyAt(i);
            // get the object by the key.
            List<RxBroadcastReceiver> list = mSparseArrOnBroadCastReveive.get(key);
            if (list.contains(rxBroadcastReceiver)) {
                list.remove(rxBroadcastReceiver);
            }
        }
    }
}
