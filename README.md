# RxBroadcaster 


## 使用RxJava实现Broadcaster 

由于广播本身存在性能问题，所以我们一直使用一种方案就是V4包下面的LocalBroadcastManger去发送广播，这使用handler实现的，性能极高。
但是由于无法自由的切换线程,所以这就是这个库出现的意义。

## 使用
```
MainActivity extends AppCompatActivity implements RxOnReceive {

    RxBroadCastReceiver broadCastReceiverAsync = new RxBroadCastReceiver(this);
    RxBroadCastReceiver broadCastReceiverback = new RxBroadCastReceiverBackground(this);
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
    
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    
            broadCastReceiverAsync.putFilter("testthread");
            broadCastReceiverAsync.commit();
    
            broadCastReceiverback.putFilter("testthread");//注册到相同的filter
            broadCastReceiverback.commit();
            //the test repeated registration.
            broadCastReceiverback.commit();//多次注册 只会生效一次
            broadCastReceiverback.commit();
            broadCastReceiverback.commit();
    
            RxBroadCastManager.getInstance().sendBroadcast("testthread", "scream");//sendBroadcast "testthread" 
    
        }
        
        @Override
        public void call(Object o) {
        
            Log.d("RxBroadCastManager send:", "" + o + " ,onReceive is MainThraed=" + isMainTread());
        }

...
...

}
    
//log如下 ：

RxBroadCastManager send:: scream ,onReceive is MainThraed=true
RxBroadCastManager send:: scream ,onReceive is MainThraed=false


```

## 特性

1. 高度的线程切换的自由

2. 像LocalBroadcastManger一样的高性能

3. 解决广播使用过程中多次注册带来的 接受两次的bug


## 注意

1. RxBroadCastReceiver跟官方不同点在于这里没有切换线程，而使用LocalBoradCastManger的时候，就会在UI线程OnReceive，
所以这里需要注意，需要非要回到UI线程使用 RxBroadCastReceiverMainThread.

2. register之后记得要unRegister,否则容易导致内存泄露因为这是强引用.



## 测试日志　

1. 测试发现 多次 commit 都没有收到多条 解决 使用　LocalBroadCastManager　时带来的问题

2. 测试中没有发现　同步 remove　和 add 带来的异常




