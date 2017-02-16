package ru.sk42.tradeodata.Activities.Product;

import android.graphics.Bitmap;

/**
 * Created by хрюн моржов on 15.02.2017.
 */

public interface ProductPresenterContract {
    interface ProductDescriptionContract {
        void showImage(Bitmap bitmap);
    }
}
