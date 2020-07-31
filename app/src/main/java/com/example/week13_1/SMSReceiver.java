//package com.example.week13_1;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.telephony.SmsMessage;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.util.Objects;
//
//public class SMSReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent){
//        Bundle bundle = intent.getExtras();
//        SmsMessage[] mssgs = null;
//        String str = "SMS from ";
//        if (bundle!=null){
//            Object[] pdus = (Object[]) bundle.get("pdus");
//            mssgs = new SmsMessage[pdus.length];
//            for(int i=0; i<mssgs.length; i++){
//                mssgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
//                if(i==0){
//                    str+= mssgs[i].getOriginatingAddress();
//                    str+= ": ";
//                }
//                str+= mssgs[i].getMessageBody().toString();
//            }
//            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
//            Log.d("SMS Receiver",str);
//        }
//    }
//}
