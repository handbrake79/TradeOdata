package ru.sk42.tradeodata.Activities.Settings;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.sk42.tradeodata.NetworkRequests.CheckServerAvailability;
import ru.sk42.tradeodata.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.server_address)
    TextInputEditText serverAddress;
    @Bind(R.id.til_address)
    TextInputLayout tilAddress;
    @Bind(R.id.baseName)
    TextInputEditText baseName;
    @Bind(R.id.til_base)
    TextInputLayout tilBase;
    @Bind(R.id.login)
    TextInputEditText login;
    @Bind(R.id.til_login)
    TextInputLayout tilLogin;
    @Bind(R.id.password)
    TextInputEditText password;
    @Bind(R.id.til_password)
    TextInputLayout tilPassword;
    @Bind(R.id.btn_apply_connection_settings)
    Button btnApplyConnectionSettings;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ConnectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectionFragment newInstance() {
        ConnectionFragment fragment = new ConnectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the shake_anim for this fragment
        View view = inflater.inflate(R.layout.settings_fragment_connection, container, false);
        ButterKnife.bind(this, view);

        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        settingsActivity.setSettingsTitle("Параметры соединения с сервером", "");
        Settings settings = Settings.getSettings();
        serverAddress.setText(settings.getServerAddress());
        baseName.setText(settings.getBaseName());
        login.setText(settings.getLogin());
        password.setText(settings.getPassword());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_apply_connection_settings)
    public void onClick() {
        Settings settings = Settings.getSettings();
        settings.setServerAddress(serverAddress.getText().toString());
        settings.setBaseName(baseName.getText().toString());
        settings.setLogin(login.getText().toString());
        settings.setPassword(password.getText().toString());
        settings.save();
        AsyncTask<Activity, Void, Boolean> task = new CheckServerAvailability();
        task.execute(this.getActivity());

    }
}
