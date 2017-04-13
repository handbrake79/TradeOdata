package ru.sk42.tradeodata.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingFragment extends Fragment {


    View view;
    TextView tv;
    LoadingFragment instance;

    public LoadingFragment() {
        // Required empty public constructor
    }

    public static LoadingFragment newInstance(String... params) {
        LoadingFragment fragment = new LoadingFragment();
        if (params.length == 1) {
            Bundle args = new Bundle();
            args.putString(Constants.MESSAGE_LABEL, params[0]);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the shake_anim for this fragment
        view = inflater.inflate(R.layout.fragment_loading, container, false);
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        avi.show();
        if (getArguments() != null) {
            if (!getArguments().isEmpty()) {
                showMessage(getArguments().get(Constants.MESSAGE_LABEL).toString());
            }
        }
        return view;
    }


    public void showMessage(String text) {
        tv = (TextView) view.findViewById(R.id.loading_message);
        tv.setText(text);
    }
}
