package ru.sk42.tradeodata.Activities.Settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.generalscan.bluetooth.BluetoothConnect;
import com.generalscan.bluetooth.BluetoothSettings;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.sk42.tradeodata.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScannerFragment extends Fragment {


    @Bind(R.id.btnPair)
    Button btnPair;
    @Bind(R.id.btnSelect)
    Button btnSelect;
    @Bind(R.id.btnConnect)
    Button btnConnect;
    @Bind(R.id.scannerStartDelayMillis)
    EditText mScannerStartDelayMillis;
    @Bind(R.id.settings_barcode)
    EditText mBarcode;

    public ScannerFragment() {
        // Required empty public constructor
    }

    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings__fragment_scanner, container, false);
        ButterKnife.bind(this, view);
        mScannerStartDelayMillis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int millis = Integer.valueOf(editable.toString());
                Settings settings = Settings.getSettings();
                settings.setScannerStartDelayMillis(millis);
                settings.save();

            }
        });
        return view;
    }

    // TODO: Rename method, initView argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btnPair, R.id.btnSelect, R.id.btnConnect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPair:
                BluetoothSettings.ACTION_BLUETOOTH_SETTINGS(this.getActivity());
                break;
            case R.id.btnSelect:
                BluetoothSettings.SetScaner(this.getActivity());
                break;
            case R.id.btnConnect:
                BluetoothConnect.Connect();
                break;
        }
    }


}
