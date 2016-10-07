package ru.sk42.tradeodata.Activities;

import android.support.v4.app.Fragment;

/**
 * Created by я on 27.09.2016.
 */

public interface MyActivityFragmentInteractionInterface {
        void onItemSelection(Object selectedObject); //stock?

        void onDetachFragment(Fragment fragment);

        void onAttachFragment(Fragment fragment);

        void onRequestSuccess(Object obj); //productInfo?
}
