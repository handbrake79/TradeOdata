package ru.sk42.tradeodata.Activities.Document;

import android.support.design.widget.TextInputLayout;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by хрюн моржов on 21.10.2016.
 */
public interface ShippingInterface {
    void onShippingChanged(boolean needShipping);

    void onUnloadChanged(boolean needUnload);

    void onShippingCostChanged(int shippingCost);

    void onUnloadCostChanged(int unloadCost);

    void onWorkersChanged(int workers);

    void onAddressChanged(String address);

    void onShippingDateChanged(Calendar shippingDate, EditText editText);

    void onShippingTimeFromChanged(Calendar timeFrom, EditText editText);

    void onShippingTimeToChanged(Calendar timeTo, EditText editText);

    void onRouteChanged(String route, ErrorInterface fragment, EditText til);

    void onStartingPointChanged(String startingPoint);

    void onVehicleTypeChanged(String vehicleType);
}
