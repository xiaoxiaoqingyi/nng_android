package com.terminus.sz.nnglibrarydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;


public class MainActivity extends AppCompatActivity {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    TextView txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.txt);

//        final Runnable beeper = new Runnable() {
//            public void run() {   nng_req(); }
//        };
//        final ScheduledFuture<?> beeperHandle =
//                scheduler.scheduleAtFixedRate(beeper, 1000, 1000, MILLISECONDS);

        new Thread(new Runnable() {
            @Override
            public void run() {
                nng_sub();
            }
        }).start();
    }

//    private void nng_req(){
//        String url="tcp://192.168.2.145:4567";
//        NNGSocket.ByReference nngSocket2 = new NNGSocket.ByReference();
//        if(NngDelegate.INSTANCE.nng_req0_open(nngSocket2) != 0){
//            return;
//        }
//        NNGSocket.ByValue nngSocket = new NNGSocket.ByValue(nngSocket2.getPointer());
//        int diaRes = NngDelegate.INSTANCE.nng_dial(nngSocket, url, null, 0);
//        Log.e("aaaaa","nng_dial:"+diaRes);
//        if(diaRes != 0){
//            return;
//        }
//        Log.e("aaaaa","nng_send");
//        String sendTxt = "what is the answer?";
//        if (NngDelegate.INSTANCE.nng_send(nngSocket, sendTxt,sendTxt.getBytes().length, 0) != 0) {
//            return;
//        }
//        Log.e("aaaaa","nng_recv");
//        byte[] res = new byte[128];
//        IntByReference len = new IntByReference(64);
//        if (NngDelegate.INSTANCE.nng_recv(nngSocket,res, len, 0 ) != 0) {
//            return;
//        }
//        String reply = new String(res,0, len.getValue());
//        Log.e("aaaaa","nng_recv,reply:"+reply);
//
////        Pointer m = new Memory(reply.length() + 1); // WARNING: assumes ascii-only string
////        m.setString(0, reply);
////        CLibrary.INSTANCE.nng_free(m, len);
//
//        NngDelegate.INSTANCE.nng_close(nngSocket);
//    }

    private void nng_pub(){
        String url="tcp://192.168.2.118:4567";
        NNGSocket.ByReference nngSocket2 = new NNGSocket.ByReference();
        if(NngDelegate.INSTANCE.nng_pub0_open(nngSocket2) != 0){
            return;
        }

        NNGSocket.ByValue nngSocket = new NNGSocket.ByValue(nngSocket2.getPointer());
        String addr="ipc:///grandpa_clock";
        String NNG_OPT_SUB_SUBSCRIBE = "sub:subscribe";
        int diaRes = NngDelegate.INSTANCE.nng_listen(nngSocket, url, null, 0);
        Log.e("aaaaa","nng_dial:"+diaRes);
        if(diaRes != 0){
            return;
        }
//        byte[] res = new byte[128];
//        IntByReference len = new IntByReference(128);
        while (true){
//            int recRes = NngDelegate.INSTANCE.nng_recv(nngSocket,res, len, 1 );
//            if (recRes != 0) {
//                Log.e("aaaaa","nng_recv,error:"+recRes);
//                return;
//            }
            String sendTxt = "pubmsg";
            byte[] dd = new byte[6];
            try {
                dd =sendTxt.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (NngDelegate.INSTANCE.nng_send(nngSocket, dd,sendTxt.getBytes().length, 0) != 0) {
                return;
            }
            try {
                Thread.sleep(1000);
                Log.e("aaaaa","nng_send:pub msg!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }

    private void nng_sub(){
        String url="tcp://192.168.2.145:4567";
        NNGSocket.ByReference nngSocket2 = new NNGSocket.ByReference();
        if(NngDelegate.INSTANCE.nng_sub0_open(nngSocket2) != 0){
            return;
        }

        NNGSocket.ByValue nngSocket = new NNGSocket.ByValue(nngSocket2.getPointer());
        String NNG_OPT_SUB_SUBSCRIBE = "sub:subscribe";
        String topic  = "/TOP/";
        if(NngDelegate.INSTANCE.nng_setopt(nngSocket, NNG_OPT_SUB_SUBSCRIBE,topic, 0) != 0){
            return;
        }
        int diaRes = NngDelegate.INSTANCE.nng_dial(nngSocket, url, null, 0);
        Log.e("aaaaa","nng_dial:"+diaRes);
        if(diaRes != 0){
            return;
        }
        while (true){
            byte[] res = new byte[128];
            IntByReference len = new IntByReference(128);
            int recRes = NngDelegate.INSTANCE.nng_recv(nngSocket,res, len, 0 );
            if (recRes != 0) {
                Log.e("aaaaa","nng_recv,error:"+recRes);
                return;
            }
            String reply = new String(res,0, len.getValue());
            Log.e("aaaaa","nng_recv,reply:"+reply);
            res = null;
        }


    }



}
