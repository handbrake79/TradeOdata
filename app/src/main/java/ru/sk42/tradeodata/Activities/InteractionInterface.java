package ru.sk42.tradeodata.Activities;

import android.support.v4.app.Fragment;

/**
 * Created by я on 27.09.2016.
 */

public interface InteractionInterface {

        void onItemSelected(Object selectedObject); //stock?

        void onRequestSuccess(Object obj); //productInfo?
}
