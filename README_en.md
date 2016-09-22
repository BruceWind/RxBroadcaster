# RxBroadcaster

## use of RxJava to achieve Broadcaster 
Due to the performance of the broadcast itself, so we have been using a program is to send V4 packet LocalBroadcastManger to send the broadcast, which uses handler to achieve, the performance is very high.

But because there is no freedom to switch the thread, so this is the meaning of the library.

## The use of
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
    
            broadCastReceiverback.putFilter("testthread");//Registered to the same filter 
            broadCastReceiverback.commit();//the test repeated registration.
            
            
            broadCastReceiverback.commit();//Registration will only take effect once many times 
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
    
//log  ：

RxBroadCastManager send:: scream ,onReceive is MainThraed=true
RxBroadCastManager send:: scream ,onReceive is MainThraed=false
```

 
## features 
 
1. The height of the thread of freedom 
 
2. Like LocalBroadcastManger high performance 
 
3. Solve the radio in the process of using multiple registrations to accept two bugs 
 
 
## notice 
 
1. RxBroadCastReceiver with official difference is that there is no switch threads, and use LocalBoradCastManger, will the UI thread OnReceive, 
So here need to pay attention to, it is necessary to use RxBroadCastReceiverMainThread must return to the UI thread. 
 
2. Remember to unRegister after register, otherwise easy to cause memory leaks because it is a strong reference. 
 
 
 
## test log 
1. I didn't get a multiple test found that commit many times to solve the problems when using LocalBroadCastManager 
 
2. There is no evidence of a synchronous test remove and add the exception 

