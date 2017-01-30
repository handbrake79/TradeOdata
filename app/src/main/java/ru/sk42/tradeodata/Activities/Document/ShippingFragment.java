package ru.sk42.tradeodata.Activities.Document;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.R;

import static android.app.Activity.RESULT_OK;
import static ru.sk42.tradeodata.Helpers.Uttils.TIME_FORMATTER;


// In this case, the fragment displays simple text based on the page
public class ShippingFragment extends Fragment {

    private DocumentListenerInterface mListenerShipping;

    private Calendar mDate = GregorianCalendar.getInstance();
    private Calendar mTimeFrom = GregorianCalendar.getInstance();
    private Calendar mTimeTo = GregorianCalendar.getInstance();

    private DocSale docSale;

    static String TAG = "shipping";
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private ArrayAdapter mVehicleTypeArrayAdapter = null;
    private ArrayAdapter mRouteArrayAdapter = null;
    private ArrayAdapter mStartingPointArrayAdapter = null;

    @Bind(R.id.llShippingLayout)
    LinearLayout llShippingLayout;

    @Bind(R.id.rlUnloadLayout)
    RelativeLayout rlUnloadLayout;


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
    EditText mShippingAddressEditText;

    @Bind(R.id.input_shipping_cost)
    EditText mShippingCostEditText;

    @Bind(R.id.til_shipping_cost)
    TextInputLayout tilShippingCost;

    @Bind(R.id.input_unload_cost)
    EditText mUnloadCostEditText;

    @Bind(R.id.input_workers_count)
    EditText mWorkersCountEditText;

    @Bind(R.id.input_need_shipping_checkbox)
    SwitchCompat mNeedShippingSwitch;

    @Bind(R.id.input_need_unload_checkbox)
    SwitchCompat mNeedUnloadSwitch;


    @Bind(R.id.til_unload_cost)
    TextInputLayout tilUnloadCost;

    @Bind(R.id.til_Workers)
    TextInputLayout tilWorkers;

    @Bind(R.id.input_contact_person)
    EditText mContactPerson;

    @Bind(R.id.input_contact_person_phone)
    EditText mContactPersonPhone;

    @Bind(R.id.til_ContactPerson)
    TextInputLayout tilContactPerson;

    @Bind(R.id.til_ContactPersonPhone)
    TextInputLayout tilContactPersonPhone;

    @Override
    public void onDetach() {
        super.onDetach();
    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doc_page_shipping, container, false);
        ButterKnife.bind(this, view);

        initView();


        return view;

    }


    private void initView() {

        mNeedShippingSwitch.requestFocus();

        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        mDate.setTime(docSale.getShippingDate());
        setShippingDateText(mDate);

        mTimeFrom.setTime(docSale.getShippingTimeFrom());
        setTimeFromText(mTimeFrom);
        mTimeFromText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    onClickTime(view);
                }
            }
        });

        mTimeTo.setTime(docSale.getShippingTimeTo());
        setTimeToText(mTimeTo);
        mTimeToText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    onClickTime(view);
                }
            }
        });

        try {
            mStartingPointArrayAdapter = new ArrayAdapter(this.getContext(),
                    android.R.layout.simple_dropdown_item_1line, MyHelper.getStartingPointDao().queryForAll().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            mVehicleTypeArrayAdapter = new ArrayAdapter(this.getContext(),
                    android.R.layout.simple_dropdown_item_1line, MyHelper.getVehicleTypesDao().queryForAll().toArray());
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
        mVehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (mVehicleTypeSpinner.getSelectedView() == null) {

                }
                mListenerShipping.onVehicleTypeChanged(adapterView.getItemAtPosition(position).toString(), (TextView) mVehicleTypeSpinner.getSelectedView());
                setShippingCostText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mStartingPointSpinner.setAdapter(mStartingPointArrayAdapter);
        mStartingPointSpinner.setSelection(getIndexOfSpinnerValue(mStartingPointSpinner, docSale.getStartingPoint().toString()));
        mStartingPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                                                mListenerShipping.onStartingPointChanged(adapterView.getItemAtPosition(position).toString(), (TextView) mStartingPointSpinner.getSelectedView());
                                                                setShippingCostText();
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                                            }
                                                        }
        );

        mRouteText.setAdapter(mRouteArrayAdapter);
        mRouteText.setText(docSale.getRoute().toString());
        mRouteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListenerShipping.onRouteChanged(adapterView.getItemAtPosition(i).toString(), mRouteText);
                setShippingCostText();
            }
        });

        mShippingAddressEditText.setText(docSale.getShippingAddress());
        mShippingAddressEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (mShippingAddressEditText.getText() != null) {
                    mListenerShipping.onAddressChanged(mShippingAddressEditText.getText().toString());
                }
                return false;
            }
        });

        mShippingCostEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (mShippingAddressEditText.getText() != null) {
                    int cost = 0;
                    try {
                        cost = Integer.valueOf(mShippingCostEditText.getText().toString());
                    } catch (NumberFormatException e) {
                        cost = 0;
                    }
                    mListenerShipping.onShippingCostChanged(cost, tilShippingCost);
                }
                return false;
            }
        });

        mUnloadCostEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (mUnloadCostEditText.getText() != null) {
                    int cost = 0;
                    try {
                        cost = Integer.valueOf(mUnloadCostEditText.getText().toString());
                    } catch (NumberFormatException e) {
                        cost = 0;
                    }
                    mListenerShipping.onUnloadCostChanged(cost, tilUnloadCost);
                }
                return false;
            }
        });

        mWorkersCountEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (mWorkersCountEditText.getText() != null) {
                    int count = 0;
                    try {
                        count = Integer.valueOf(mWorkersCountEditText.getText().toString());
                    } catch (NumberFormatException e) {
                        count = 0;
                    }
                    mListenerShipping.onWorkersChanged(count, tilWorkers);
                }
                return false;
            }
        });

        mContactPerson.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (mContactPerson.getText() != null) {
                    String text = "";
                    try {
                        text = String.valueOf(mContactPerson.getText().toString());
                    } catch (Exception e) {
                        text = "";
                    }
                    mListenerShipping.onContactPersonChanged(text, tilContactPerson);
                }
                return false;
            }
        });
        mContactPersonPhone.addTextChangedListener(new TextWatcher() {
            private boolean mFormatting; // this is a flag which prevents the  stack overflow.
            private int mAfter;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int after) {
                //nothing to do here...
                mAfter  =   after; // flag to detect backspace..
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Make sure to ignore calls to afterTextChanged caused by the work done below
                if (!mFormatting) {
                    mFormatting = true;
                    // using US formatting...
                    if(mAfter!=0) // in case back space ain't clicked...
                        PhoneNumberUtils.formatNumber(editable,PhoneNumberUtils.getFormatTypeForLocale(Locale.US));
                    mFormatting = false;
                }
            }
        });
        mContactPersonPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (mContactPersonPhone.getText() != null) {
                    String text = "";
                    try {
                        text = String.valueOf(mContactPersonPhone.getText().toString());
                    } catch (Exception e) {
                        text = "";
                    }
                    mListenerShipping.onContactPersonPhoneChanged(text, tilContactPersonPhone);
                }
                return false;
            }
        });


        mNeedShippingSwitch.setChecked(docSale.getNeedShipping());
        toggleShippingElements(mNeedShippingSwitch.isChecked());

        mNeedUnloadSwitch.setChecked(docSale.getNeedUnload());

        mShippingCostEditText.setText(Uttils.formatInt(docSale.getShippingCost()));

        mUnloadCostEditText.setText(Uttils.formatInt(docSale.getUnloadCost()));

        mWorkersCountEditText.setText(Uttils.formatInt(docSale.getWorkersCount()));

        mContactPerson.setText(docSale.getContactPerson());

        mContactPersonPhone.setText(docSale.getContactPersonPhone());

        toggleShippingElements(docSale.getNeedShipping());

        toggleUnload(docSale.getNeedUnload());
    }

    private void setShippingCostText() {
        mShippingCostEditText.setText(Uttils.formatInt(docSale.getShippingCost()));
        String hint = "Стоимость доставки";
        if (docSale.getReferenceShipingCost() > 0) {
            hint += " (мин.цена по выбранному маршруту " + String.valueOf(docSale.getReferenceShipingCost() + " руб)");
        }
        tilShippingCost.setHint(hint);
    }

    private void setShippingDateText(Calendar mShippingDate) {
        if (Uttils.dateIsNotSet(mShippingDate)) {
            mShippingDateText.setText(null);
        } else {
            mShippingDateText.setText(Uttils.DATE_FORMATTER.format(mShippingDate.getTime()).toString());

        }
    }

    private void onShippingDateChanged(Calendar mShippingDate) {
        setShippingDateText(mShippingDate);
        mListenerShipping.onDateChanged(mShippingDate, mShippingDateText);
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
                .setDoneText("Выбрать")
                .setCancelText("Отмена")
                .setThemeLight();
        if (view.getId() == R.id.input_time_from) {
            rtpd.setStartTime(9, 00);

        } else {
            rtpd.setStartTime(18, 00);

        }
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
            Date date = Uttils.DATE_FORMATTER.parse(((TextView) v).getText().toString());
            dateMillis = date.getTime();
        } catch (ParseException e) {
            Log.e(getTag(), "Error converting input time to Date object");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMillis);

        class OnDateSetListener implements CalendarDatePickerDialogFragment.OnDateSetListener {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                mDate.set(year, monthOfYear, dayOfMonth);
                onShippingDateChanged(mDate);
            }
        }

        Calendar preselectedDate = GregorianCalendar.getInstance();
        preselectedDate.set(1, 1, 1);

        if (!preselectedDate.before(mDate)) {
            preselectedDate = GregorianCalendar.getInstance();
        } else {
            preselectedDate = mDate;
        }

        CalendarDatePickerDialogFragment datePickerDialog = new CalendarDatePickerDialogFragment()
                .setPreselectedDate(preselectedDate.get(Calendar.YEAR), preselectedDate.get(Calendar.MONTH), preselectedDate.get(Calendar.DAY_OF_MONTH))
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
            R.id.input_shipping_address,
            R.id.input_shipping_cost,
            R.id.input_need_unload_checkbox,
            R.id.input_workers_count,
            R.id.input_unload_cost
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_need_shipping_checkbox:
                onNeedShippingChanged();
                break;
            case R.id.input_shipping_date:
                break;
            case R.id.input_time_from:
                break;
            case R.id.input_time_to:
                break;
            case R.id.input_shipping_address:
                break;
            case R.id.input_shipping_cost:
                break;
            case R.id.input_need_unload_checkbox:
                onNeedUnloadChanged();
                break;
            case R.id.input_workers_count:
                break;
            case R.id.input_unload_cost:
                break;
        }
    }



    private void onNeedUnloadChanged() {
        boolean needUnload = mNeedUnloadSwitch.isChecked();
        toggleUnload(needUnload);
        mListenerShipping.onNeedUnloadChanged(needUnload);
    }

    private void toggleUnload(boolean state) {
        if (state) {
            rlUnloadLayout.setBackgroundColor(Constants.COLORS.ENABLED);

        } else {
            rlUnloadLayout.setBackgroundColor(Constants.COLORS.DISABLED);
        }

        mWorkersCountEditText.setEnabled(state);
        mUnloadCostEditText.setEnabled(state);
    }

    private void onNeedShippingChanged() {
        boolean needShipping = mNeedShippingSwitch.isChecked();
        toggleShippingElements(needShipping);
        mListenerShipping.onNeedShippingChanged(needShipping);
    }

    private void toggleShippingElements(boolean state) {
        llShippingLayout.setEnabled(state);
        if (!state) {
            llShippingLayout.setBackgroundColor(Constants.COLORS.DISABLED);
        } else {
            llShippingLayout.setBackgroundColor(Constants.COLORS.ENABLED);

        }
        mShippingDateText.setEnabled(state);
        mTimeFromText.setEnabled(state);
        mTimeToText.setEnabled(state);
        mShippingAddressEditText.setEnabled(state);
        mStartingPointSpinner.setEnabled(state);
        mRouteText.setEnabled(state);
        mVehicleTypeSpinner.setEnabled(state);
        mStartingPointSpinner.setEnabled(state);
        mShippingCostEditText.setEnabled(state);

        mNeedUnloadSwitch.setEnabled(state);

    }


    class TimeFromListener implements RadialTimePickerDialogFragment.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            mTimeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTimeFrom.set(Calendar.MINUTE, minute);
            onTimeFromChanged();
        }
    }


    class TimeToListener implements RadialTimePickerDialogFragment.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            mTimeTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTimeTo.set(Calendar.MINUTE, minute);
            onTimeToChanged();
        }
    }

    private void onTimeFromChanged() {
        setTimeFromText(mTimeFrom);
        mListenerShipping.onTimeFromChanged(mTimeFrom, mTimeFromText);
    }

    private void onTimeToChanged() {
        setTimeToText(mTimeTo);
        mListenerShipping.onTimeToChanged(mTimeTo, mTimeToText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DocumentListenerInterface) {
            mListenerShipping = (DocumentListenerInterface) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DocumentListenerInterface");
        }

    }




}
