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
import android.widget.EditText;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class DocumentFragment extends Fragment implements TextWatcher, RadialTimePickerDialogFragment.OnTimeSetListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    /**
     * Formats a {@link Date} object to time string of format HH:mm e.g. 15:25
     */
    public final static DateFormat TIME_FORMATTER = DateFormat.getTimeInstance();
    static String TAG = "DocumentFragment";
    final TextView mTimeFromTextView = null;
    Requisites_Adapter requisitesAdapter;
    SaleRowProductAdapter productsAdapter;
    SaleRowServiceAdapter servicesAdapter;
    ArrayAdapter vehicleTypeArrayAdapter = null;
    ArrayAdapter routeArrayAdapter = null;
    ArrayAdapter startingPointArrayAdapter = null;
    View mRequisitesView;
    View mProductsView;
    View mServicesView;

    //    @Bind(R.id.rvDocPageProducts) android.support.v7.widget.RecyclerView mProductsRecyclerView;
//    @Bind(R.id.rvDocPageServices) android.support.v7.widget.RecyclerView mServicesRecyclerView;
//    @Bind(R.id.edtPassPerson) EditText mPassPersonEditText;
//    @Bind(R.id.edtPassVehicle) EditText mPassVehicleEditText;
//    @Bind(R.id.input_route) AutoCompleteTextView mRouteText;
//    @Bind(R.id.input_vehicle_type) Spinner mVehicleTypeSpinner;
//    @Bind(R.id.input_starting_point) Spinner mStartingPointSpinner;
//    @Bind(R.id.input_time_from) TextView mTimeFromText;
//    @Bind(R.id.input_time_to) TextView mTimeToText;
    View mShippingView;
    View mPassView;
    private DocSale docSale;
    private MyActivityFragmentInteractionInterface mListener;
    private int mPage;

    public static DocumentFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        DocumentFragment fragment = new DocumentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onDetachFragment(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE, -1);


    }

    //    private void reloadDocSale(){
//        DocumentActivity activity = (DocumentActivity) getActivity();
//        docSale = DocSale.getList(DocSale.class, activity.getDocRef_Key());
//    }
    private void setActionBarTitle(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        String title = docSale.getNumber();
        actionBar.setTitle(title);
        actionBar.setWindowTitle(" WindowTitle" );
        actionBar.setSubtitle(String.valueOf(docSale.getProducts().size() + " товаров" ));

    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        setActionBarTitle();

        switch (mPage) {
            case 0:

                mRequisitesView = inflater.inflate(R.layout.doc_page_requisites, container, false);
                ButterKnife.bind(this, mRequisitesView);

//                    mRequisistesRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
//                    mRequisistesRecyclerView.setLayoutManager(new LinearLayoutManager(mRequisitesView.getContext()));
//                    Requisites requisites = Requisites.NewInstance(docSale);
//                    requisitesAdapter = new Requisites_Adapter(requisites.getArrayList(), (MyActivityFragmentInteractionInterface) getActivity());
//                    mRequisistesRecyclerView.setAdapter(requisitesAdapter);
//                    requisitesAdapter.notifyDataSetChanged();
//
                return mRequisitesView;
            case 1:
                mProductsView = inflater.inflate(R.layout.doc_page_products, container, false);
                ButterKnife.bind(this, mProductsView);


//                    mProductsRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
//                    mProductsRecyclerView.setSelected(true);
//                    mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(mProductsView.getContext()));
//                    productsAdapter = new SaleRowProductAdapter(docSale.getProductsList(), (MyActivityFragmentInteractionInterface) getActivity());
//                    mProductsRecyclerView.setAdapter(productsAdapter);
//                    productsAdapter.notifyDataSetChanged();
//


                return mProductsView;

            case 2: //SERVICES
                mServicesView = inflater.inflate(R.layout.doc_page_services, container, false);
                //     ButterKnife.bind(this, mServicesView);
//
//                    mServicesRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
//                    mServicesRecyclerView.setSelected(true);
//                    mServicesRecyclerView.setLayoutManager(new LinearLayoutManager(mServicesRecyclerView.getContext()));
//                    servicesAdapter = new SaleRowServiceAdapter(docSale.getServicesList(), (MyActivityFragmentInteractionInterface) getActivity());
//                    mServicesRecyclerView.setAdapter(servicesAdapter);
//                    servicesAdapter.notifyDataSetChanged();

                return mServicesView;

            case 3:
                mShippingView = inflater.inflate(R.layout.doc_page_shipping, container, false);
                //   ButterKnife.bind(this, mShippingView);

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
//                mVehicleTypeSpinner.setAdapter(vehicleTypeArrayAdapter);
//                mStartingPointSpinner.setAdapter(startingPointArrayAdapter);
//
//                mRouteText.setAdapter(routeArrayAdapter);
//                mRouteText.addTextChangedListener(this);


                return mShippingView;

            case 4:

                mPassView = inflater.inflate(R.layout.doc_page_pass, container, false);
                //   ButterKnife.bind(this, mPassView);

                EditText edtPassVehicle = (EditText) mPassView.findViewById(R.id.input_pass_vehicle);
                EditText edtPassPerson = (EditText) mPassView.findViewById(R.id.input_pass_person);
                edtPassVehicle.setText(docSale.getPassVehicle());
                edtPassPerson.setText(docSale.getsPassPerson());
                return mPassView;
        }
        return null;
    }

    private void showMessage(String s) {
        Log.d(TAG, "showProgress: " + s);
        //Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        mTimeFromTextView.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
    }

//    @OnClick(R.id.input_route)
//    public void onClick(){
//        mRouteText.showDropDown();
//    }
//
//    @OnClick({R.id.input_time_from, R.id.input_time_to})
//    public void onClick(View view){
//                long timeMillis = 0;
//                try {
//                    Date date = TIME_FORMATTER.parse(mTimeFromTextView.getText().toString());
//                    timeMillis = date.getTime();
//                } catch (ParseException e) {
//                    Log.e(getTag(), "Error converting input time to Date object");
//                }
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(timeMillis);
//
//                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
//                        .setOnTimeSetListener(DocumentFragment.this)
//                        .setStartTime(Calendar.HOUR_OF_DAY, Calendar.MINUTE)
//                        .setDoneText("Yay")
//                        .setCancelText("Nop")
//                        .setThemeDark();
//                rtpd.show(getFragmentManager(), "time_picker_dialog_fragment");
//
//
//    }


}
