package ru.sk42.tradeodata.Activities.Document;

import android.support.design.widget.TextInputLayout;
import android.widget.AutoCompleteTextView;

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

    void onShippingDateChanged(String shippingDate);

    void onShippingTimeFromChanged(String timeFrom);

    void onShippingTimeToChanged(String timeTo);

    void onRouteChanged(String route, ErrorInterface fragment, TextInputLayout til);

    void onStartingPointChanged(String startingPoint);

    void onVehicleTypeChanged(String vehicleType);
}
