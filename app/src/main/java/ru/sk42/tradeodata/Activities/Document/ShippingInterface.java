package ru.sk42.tradeodata.Activities.Document;

import android.support.design.widget.TextInputLayout;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by хрюн моржов on 21.10.2016.
 */
public interface ShippingInterface {


    void onPassPersonChanged(String mPassPerson);

    void onPassVehicleChanged(String mPassVehicle);

    void onNeedShippingChanged(boolean needShipping);

    void onNeedUnloadChanged(boolean needUnload);

    void onShippingCostChanged(int shippingCost, TextInputLayout til);

    void onUnloadCostChanged(int unloadCost, TextInputLayout til);

    void onWorkersChanged(int workers, TextInputLayout til);

    void onAddressChanged(String address);

    void onDateChanged(Calendar shippingDate, EditText editText);

    void onTimeFromChanged(Calendar timeFrom, EditText editText);

    void onTimeToChanged(Calendar timeTo, EditText editText);

    void onRouteChanged(String route, TextView textView);

    void onStartingPointChanged(String startingPoint, TextView textView);

    void onVehicleTypeChanged(String vehicleType, TextView textView);
}
