package ru.sk42.tradeodata.Activities.Scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.generalscan.SendConstant;

import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;

/**
 * Created by хрюн моржов on 25.11.2016.
 */


public class ReadBroadcast extends BroadcastReceiver {

    final static String TAG = "ReadBroadcast ***";
    ServiceResultReceiver mReceiver;

    public ReadBroadcast(ServiceResultReceiver resultReciever) {
        this.mReceiver = resultReciever;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //接受电量数据广播
        if (intent.getAction().equals(SendConstant.GetBatteryDataAction)) {

            String data = intent.getStringExtra(SendConstant.GetBatteryData);

            Log.d(TAG, "onReceive:GetBatteryDataAction " + data);
        }
        // 接收数据的广播
        if (intent.getAction().equals(SendConstant.GetDataAction)) {

            String data = intent.getStringExtra(SendConstant.GetData);
            if(mReceiver != null){
                Bundle b = new Bundle();
                b.putString(Constants.SCANNER_DATA_LABEL, data);

                mReceiver.send(Constants.SCANNER_EVENT, b);
            }
            //Log.d(TAG, "onReceive:GetDataAction " + data);
        }
        // 接收发送数据的广播
        if (intent.getAction().equals(SendConstant.GetReadDataAction)) {

            String name = intent.getStringExtra(SendConstant.GetReadName);
            String data = intent.getStringExtra(SendConstant.GetReadData);

            Log.d(TAG, "onReceive: *** WTF *** " + name + "   " + data);
        }
    }

}

