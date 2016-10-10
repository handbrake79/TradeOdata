package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class ShippingFragment extends Fragment implements TextWatcher {
    /**
     * Formats a {@link Date} object to time string of format HH:mm e.g. 15:25
     */
    public final static DateFormat TIME_FORMATTER = DateFormat.getTimeInstance();
    static String TAG = "shipping";
    ArrayAdapter vehicleTypeArrayAdapter = null;
    ArrayAdapter routeArrayAdapter = null;
    ArrayAdapter startingPointArrayAdapter = null;
    @Bind(R.id.input_route)
    AutoCompleteTextView mRouteText;
    @Bind(R.id.input_vehicle_type)
    Spinner mVehicleTypeSpinner;
    @Bind(R.id.input_starting_point)
    Spinner mStartingPointSpinner;
    @Bind(R.id.input_time_from)
    TextView mTimeFromText;
    @Bind(R.id.input_time_to)
    TextView mTimeToText;
    Calendar mTimeFrom, mTimeTo;
    private DocSale docSale;
    private MyActivityFragmentInteractionInterface mListener;
    private Calendar mShippingDate;


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onDetachFragment(this);
    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTimeFrom = GregorianCalendar.getInstance();
        mTimeTo = GregorianCalendar.getInstance();
        mTimeFrom.setTime(docSale.getShippingTimeFrom());
        mTimeTo.setTime(docSale.getShippingTimeTo());
        mShippingDate = GregorianCalendar.getInstance();
        mShippingDate.setTime(docSale.getShippingDate());

        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();


        View view = inflater.inflate(R.layout.doc_page_shipping, container, false);
        ButterKnife.bind(this, view);

        try {
            startingPointArrayAdapter = new ArrayAdapter(this.getContext(),
                    android.R.layout.simple_spinner_item, MyHelper.getStartingPointDao().queryForAll().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            vehicleTypeArrayAdapter = new ArrayAdapter(this.getContext(),
                    android.R.layout.simple_spinner_item, MyHelper.getVehicleTypesDao().queryForAll().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            routeArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, MyHelper.getRouteDao().queryForAll().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mVehicleTypeSpinner.setAdapter(vehicleTypeArrayAdapter);
        mVehicleTypeSpinner.setSelection(vehicleTypeArrayAdapter.getPosition(docSale.getVehicleType()));
        mStartingPointSpinner.setAdapter(startingPointArrayAdapter);
        mStartingPointSpinner.setSelection(startingPointArrayAdapter.getPosition(docSale.getStartingPoint()));

        mRouteText.setAdapter(routeArrayAdapter);
        mRouteText.addTextChangedListener(this);

        return view;

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @OnClick(R.id.input_route)
    public void onClick() {
        mRouteText.showDropDown();
    }

    @OnClick({R.id.input_time_from, R.id.input_time_to})
    public void onClick(View view) {
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
                .setThemeDark();
        rtpd.show(getFragmentManager(), "time_picker_dialog_fragment");


    }


    class TimeFromListener implements RadialTimePickerDialogFragment.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            Calendar cal = new GregorianCalendar(0, 0, 0, hourOfDay, minute);
            mTimeFromText.setText(TIME_FORMATTER.format(cal.getTime()));
            mTimeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTimeFrom.set(Calendar.MINUTE, minute);

        }
    }

    class TimeToListener implements RadialTimePickerDialogFragment.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            Calendar cal = new GregorianCalendar(0, 0, 0, hourOfDay, minute);
            mTimeToText.setText(TIME_FORMATTER.format(cal.getTime()));
            mTimeTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTimeTo.set(Calendar.MINUTE, minute);

        }
    }

}
