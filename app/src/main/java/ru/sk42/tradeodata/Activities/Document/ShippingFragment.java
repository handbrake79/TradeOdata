package ru.sk42.tradeodata.Activities.Document;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;

import static ru.sk42.tradeodata.Model.Constants.TIME_FORMATTER;


// In this case, the fragment displays simple text based on the page
public class ShippingFragment extends Fragment implements ErrorInterface {

    ShippingInterface mListenerShipping;

    static String TAG = "shipping";

    ArrayAdapter mVehicleTypeArrayAdapter = null;
    ArrayAdapter mRouteArrayAdapter = null;
    ArrayAdapter mStartingPointArrayAdapter = null;

    @Bind(R.id.input_route_text)
    AutoCompleteTextView mRouteText;

    @Bind(R.id.input_vehicle_type)
    Spinner mVehicleTypeSpinner;

    @Bind(R.id.input_starting_point)
    Spinner mStartingPointSpinner;

    @Bind(R.id.input_time_from)
    TextView mTimeFromText;

    @Bind(R.id.input_time_to)
    TextView mTimeToText;

    @Bind(R.id.input_shipping_date)
    TextView mShippingDateText;

    @Bind(R.id.input_shipping_address)
    EditText mShippingAddress;

    @Bind(R.id.input_shipping_cost)
    EditText mShippingCostEditText;

    @Bind(R.id.input_unload_cost)
    EditText mUnloadCostEditText;

    @Bind(R.id.input_workers_count)
    EditText mWorkersCountEditText;

    @Bind(R.id.input_need_shipping_checkbox)
    CheckBox mNeedShippingCheckBox;

    @Bind(R.id.input_need_unload_checkbox)
    CheckBox mNeedUnloadCheckBox;


    Calendar mTimeFrom, mTimeTo;


    @Bind(R.id.btnVoiceAddress)
    Button btnVoiceAddress;


    @Bind(R.id.doc_page_shipping_workers_caption)
    TextView docPageShippingWorkersCaption;

    @Bind(R.id.tilRoute)
    TextInputLayout tilRoute;

    @Bind(R.id.tilDate)
    TextInputLayout tilDate;

    private DocSale docSale;
    private MyActivityFragmentInteractionInterface mListener;
    private Calendar mShippingDate;


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onFragmentDetached(this);
    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doc_page_shipping, container, false);
        ButterKnife.bind(this, view);

        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        mTimeFrom = GregorianCalendar.getInstance();
        mTimeTo = GregorianCalendar.getInstance();

        mTimeFrom.setTime(docSale.getShippingTimeFrom());
        mTimeTo.setTime(docSale.getShippingTimeTo());

        mShippingDate = GregorianCalendar.getInstance();
        mShippingDate.setTime(docSale.getShippingDate());
        setTimeFromText(mTimeFrom);
        setTimeToText(mTimeTo);
        setShippingDateText(mShippingDate);

        try {
            mStartingPointArrayAdapter = new ArrayAdapter(this.getContext(),
                    android.R.layout.simple_spinner_item, MyHelper.getStartingPointDao().queryForAll().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            mVehicleTypeArrayAdapter = new ArrayAdapter(this.getContext(),
                    android.R.layout.simple_spinner_item, MyHelper.getVehicleTypesDao().queryForAll().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            mRouteArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, MyHelper.getRouteDao().queryForAll().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mVehicleTypeSpinner.setAdapter(mVehicleTypeArrayAdapter);
        mVehicleTypeSpinner.setSelection(getIndexOfSpinnerValue(mVehicleTypeSpinner, docSale.getVehicleType().toString()));

        mStartingPointSpinner.setAdapter(mStartingPointArrayAdapter);
        mStartingPointSpinner.setSelection(getIndexOfSpinnerValue(mStartingPointSpinner, docSale.getStartingPoint().toString()));

        mRouteText.setAdapter(mRouteArrayAdapter);
        mRouteText.addTextChangedListener(new RouteTextChangeListener());
        mRouteText.setText(docSale.getRoute().toString());

        mShippingAddress.setText(docSale.getShippingAddress());

        mNeedShippingCheckBox.setChecked(docSale.getNeedShipping());

        mNeedUnloadCheckBox.setChecked(docSale.getNeedUnload());

        mShippingCostEditText.setText(docSale.getShippingCost().toString());

        mUnloadCostEditText.setText(docSale.getUnloadCost().toString());

        mWorkersCountEditText.setText(docSale.getWorkersCount().toString());

        return view;

    }

    private void setShippingDateText(Calendar mShippingDate) {
        mShippingDateText.setText(Constants.DATE_FORMATTER.format(mShippingDate.getTime()).toString());
    }



    @OnClick(R.id.input_route_text)
    public void onClickRoute() {
        mRouteText.showDropDown();
    }

    @OnClick({R.id.input_time_from, R.id.input_time_to})
    public void onClickTime(View view) {
        Log.d(TAG, "onClick: v" + view.toString());
        long timeMillis = 0;
        try {
            Date date = TIME_FORMATTER.parse(mTimeFromText.getText().toString());
            timeMillis = date.getTime();
        } catch (ParseException e) {
            Log.v(getTag(), "Error converting input time to Date object");
            timeMillis = 0;
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        TimeFromListener mTimeFromListener = new TimeFromListener();
        TimeToListener mTimeToListener = new TimeToListener();
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setForced24hFormat()
                .setOnTimeSetListener(view.getId() == R.id.input_time_from ? mTimeFromListener : mTimeToListener)
                .setStartTime(Calendar.HOUR_OF_DAY, Calendar.MINUTE)
                .setDoneText("Выбрать")
                .setCancelText("Отмена")
                .setThemeLight();
        rtpd.show(getFragmentManager(), "time_picker_dialog_fragment");


    }

    private void setTimeFromText(Calendar mTime) {
        mTimeFromText.setText(TIME_FORMATTER.format(mTime.getTime()).toString());
    }

    private void setTimeToText(Calendar mTime) {
        mTimeToText.setText(TIME_FORMATTER.format(mTime.getTime()).toString());
    }

    private int getIndexOfSpinnerValue(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @OnClick(R.id.input_shipping_date)
    public void onShippingDateClick(View v) {
        long dateMillis = 0;
        try {
            Date date = Constants.DATE_FORMATTER.parse(((TextView) v).getText().toString());
            dateMillis = date.getTime();
        } catch (ParseException e) {
            Log.e(getTag(), "Error converting input time to Date object");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMillis);

        class OnDateSetListener implements CalendarDatePickerDialogFragment.OnDateSetListener {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                mShippingDate.set(year, monthOfYear, dayOfMonth);
                setShippingDateText(mShippingDate);
            }
        }

        CalendarDatePickerDialogFragment datePickerDialog = new CalendarDatePickerDialogFragment()
                .setPreselectedDate(mShippingDate.get(Calendar.YEAR), mShippingDate.get(Calendar.MONTH), mShippingDate.get(Calendar.DAY_OF_MONTH))
                .setOnDateSetListener(new OnDateSetListener())
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setDoneText("Выбрать")
                .setCancelText("Отмена")
                .setThemeLight();
        datePickerDialog.show(getFragmentManager(), "date_picker_dialog_fragment");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.input_need_shipping_checkbox,
            R.id.input_shipping_date,
            R.id.input_time_from,
            R.id.input_time_to,
            R.id.btnVoiceAddress,
            R.id.input_shipping_address,
            R.id.input_shipping_cost,
            R.id.input_need_unload_checkbox,
            R.id.input_workers_count,
            R.id.input_unload_cost})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_need_shipping_checkbox:
                break;
            case R.id.input_shipping_date:
                break;
            case R.id.input_time_from:
                break;
            case R.id.input_time_to:
                break;
            case R.id.btnVoiceAddress:
                break;
            case R.id.input_shipping_address:
                break;
            case R.id.input_shipping_cost:
                break;
            case R.id.input_need_unload_checkbox:
                break;
            case R.id.input_workers_count:
                break;
            case R.id.input_unload_cost:
                break;
        }
    }

    @Override
    public void onError(int resourceID, String error) {

    }

    class TimeFromListener implements RadialTimePickerDialogFragment.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            mTimeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTimeFrom.set(Calendar.MINUTE, minute);
            setTimeFromText(mTimeFrom);

        }
    }

    class TimeToListener implements RadialTimePickerDialogFragment.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            mTimeTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTimeTo.set(Calendar.MINUTE, minute);
            setTimeToText(mTimeTo);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ShippingInterface) {
            mListenerShipping = (ShippingInterface) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ShippingInterface");
        }

    }

    class RouteTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            mListenerShipping.onRouteChanged(charSequence.toString(), ShippingFragment.this, tilRoute);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
