package ru.sk42.tradeodata.Activities.Document;

import android.support.design.widget.TextInputLayout;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;

/**
 * Created by хрюн моржов on 21.10.2016.
 */
public interface DocumentListenerInterface {

    void onCommentChanged(String mPassPerson);

    void onPassPersonChanged(String mPassPerson);

    void onPassVehicleChanged(String mPassVehicle);

    void onNeedShippingChanged(boolean needShipping);

    void onNeedUnloadChanged(boolean needUnload);

    void onShippingCostChanged(int shippingCost, TextInputLayout til);

    void onUnloadCostChanged(int unloadCost, TextInputLayout til);

    void onWorkersChanged(int workers, TextInputLayout til);

    void onAddressChanged(String address);

    void onDateChanged(Calendar shippingDate, TextView editText);

    void onTimeFromChanged(Calendar timeFrom, TextView editText);

    void onTimeToChanged(Calendar timeTo, TextView editText);

    void onRouteChanged(String route, TextView textView);

    void onStartingPointChanged(String startingPoint, TextView textView);

    void onVehicleTypeChanged(String vehicleType, TextView textView);

    void onContactPersonChanged(String text, TextInputLayout tilContactPerson);

    void onContactPersonPhoneChanged(String text, TextInputLayout tilContactPersonPhone);

    void setDiscountCard(DiscountCard card);
}
