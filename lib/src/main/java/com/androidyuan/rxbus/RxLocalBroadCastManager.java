package com.androidyuan.rxbus;

import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.SparseArray;

import com.androidyuan.rxbus.component.RxBroadCastReceiver;

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
public class RxLocalBroadCastManager {

    private static RxLocalBroadCastManager instance;

    //use SparseArray,because is high performance
    SparseArray<List<RxBroadCastReceiver>> mSparseArrOnBroadCastReveive;
    CompositeSubscription mCompositeSubscription;

    private RxLocalBroadCastManager() {

        mSparseArrOnBroadCastReveive = new SparseArray<>();
        mCompositeSubscription = new CompositeSubscription();
    }


    public static RxLocalBroadCastManager getInstance() {

        if (instance == null) {
            instance = new RxLocalBroadCastManager();
        }
        return instance;
    }


    private synchronized void putRecevier(String filter, RxBroadCastReceiver broadCastReveive) {

        if (!TextUtils.isEmpty(filter) && broadCastReveive != null) {

            List<RxBroadCastReceiver> list = new ArrayList<>();
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
     * @param filter
     * @param obj
     */
    public void sendBroadcast(String filter, Object obj) {

        if (TextUtils.isEmpty(filter) || obj == null)
            return;

        if (isHaveKey(filter)) {//设计的 就像  广播一样 发送出来 如果没有人接受 就丢弃了

            List<RxBroadCastReceiver> list = mSparseArrOnBroadCastReveive.get(filter.hashCode());

            for (RxBroadCastReceiver onEv : list) {

                Subscription sbus = onEv.send(Observable.just(obj));

                // Log.d("on send ,isUnsubscribed", sbus.isUnsubscribed() + "");
            }
        }
    }

    //自动容错 防止多次 registerReceive
    public void registerReceiver(RxBroadCastReceiver rxBroadCastReceiver, IntentFilter filters) {

        if (filters != null && rxBroadCastReceiver != null) {
            Iterator<String> iterator = filters.actionsIterator();
            if (iterator.hasNext()) {
                putRecevier(iterator.next(), rxBroadCastReceiver);
            }
        }
    }


    /**
     * 解绑
     *
     * @param rxBroadCastReceiver
     */
    public void unregisterReceiver(RxBroadCastReceiver rxBroadCastReceiver) {

        if (rxBroadCastReceiver == null)
            return;

        for (int i = 0; i < mSparseArrOnBroadCastReveive.size(); i++) {
            int key = mSparseArrOnBroadCastReveive.keyAt(i);
            // get the object by the key.
            List<RxBroadCastReceiver> list = mSparseArrOnBroadCastReveive.get(key);
            if (list.contains(rxBroadCastReceiver)) {
                list.remove(rxBroadCastReceiver);
            }
        }
    }
}
