package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class PassFragment extends Fragment {
    static String TAG = "pass_frag";
    @Bind(R.id.input_pass_person)
    EditText mPassPersonEditText;
    @Bind(R.id.input_pass_vehicle)
    EditText mPassVehicleEditText;
    private DocSale docSale;
    private MyActivityFragmentInteractionInterface mListener;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onDetachFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();


        View view = inflater.inflate(R.layout.doc_page_pass, container, false);
        ButterKnife.bind(this, view);

        mPassPersonEditText.setText(docSale.getsPassPerson());
        mPassVehicleEditText.setText(docSale.getPassVehicle());
        return view;

    }


}
