package com.lucky.roadpilot;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GeoLocation {
    
    public  static void getAddress(String locationAddress, final Context context, Handler handler){
        Thread thread = new Thread(){
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                String result1 = null;
                try {
                    List addressList = geocoder.getFromLocationName(locationAddress,1);
                    if(addressList !=null && addressList.size() >0){
                        Address address = (Address) addressList.get(0);
                        StringBuilder stringBuilder = new StringBuilder();
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder.append(address.getLatitude()).append("/n");
                        stringBuilder1.append(address.getLongitude()).append("/n");
                        result = stringBuilder.toString();
                        result1 = stringBuilder1.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    Message message =  Message.obtain();
                    message.setTarget(handler);
                    if(result != null){
                        message.what = 11;
                        Bundle bundle = new Bundle();
                        result = result;
                        bundle.putString("address",result);
                        bundle.putString("addresses",result1);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

}
