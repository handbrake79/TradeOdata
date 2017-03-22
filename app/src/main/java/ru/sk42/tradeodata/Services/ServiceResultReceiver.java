package ru.sk42.tradeodata.Services;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by я on 27.09.2016.
 */

public class ServiceResultReceiver extends ResultReceiver {

    private ServiceResultReceiverInterface mServiceResultReceiverInterface;

    public ServiceResultReceiver(Handler handler) {
        super(handler);
    }

    public interface ServiceResultReceiverInterface {
        void onReceiveResultFromService(int resultCode, Bundle resultData);
    }

    public void setReceiver(ServiceResultReceiverInterface serviceResultReceiverInterface) {
        mServiceResultReceiverInterface = serviceResultReceiverInterface;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mServiceResultReceiverInterface != null) {
            mServiceResultReceiverInterface.onReceiveResultFromService(resultCode, resultData);
        }
    }

}