package ru.sk42.tradeodata.Services;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by я on 27.09.2016.
 */

public class ServiceResultReceiver extends ResultReceiver {

    private ReceiverInterface mReceiverInterface;

    public ServiceResultReceiver(Handler handler) {
        super(handler);
    }

    public interface ReceiverInterface {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(ReceiverInterface receiverInterface) {
        mReceiverInterface = receiverInterface;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiverInterface != null) {
            mReceiverInterface.onReceiveResult(resultCode, resultData);
        }
    }

}