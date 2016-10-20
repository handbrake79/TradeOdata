package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class RequisitesFragment extends Fragment {
    static String TAG = "ReqFrag";

    @Bind(R.id.doc_page_req_number_text)
    TextView docPageReqNumberText;
    @Bind(R.id.doc_page_req_date_text)
    TextView docPageReqDateText;
    @Bind(R.id.doc_page_req_customer_text)
    TextView docPageReqCustomerText;
    @Bind(R.id.doc_page_req_contract_text)
    TextView docPageReqContractText;
    @Bind(R.id.doc_page_req_author_text)
    TextView mAuthorText;
    @Bind(R.id.doc_page_req_shipping_cost_text)
    TextView docPageReqShippingCostText;
    @Bind(R.id.doc_page_req_unload_cost_text)
    TextView docPageReqUnloadCostText;
    @Bind(R.id.doc_page_req_total_text)
    TextView docPageReqTotalText;

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
        mListener.onFragmentDetached(this);
    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        View view = inflater.inflate(R.layout.doc_page_req, container, false);
        ButterKnife.bind(this, view);

        refreshViewData();
        return view;
    }

    public void refreshViewData() {
        mAuthorText.setText(docSale.getAuthor().toString());
        docPageReqContractText.setText(docSale.getContract().toString());
        docPageReqCustomerText.setText(docSale.getCustomer().toString());
        docPageReqNumberText.setText(docSale.getNumber());
        docPageReqDateText.setText(Constants.DATE_FORMATTER.format(docSale.getDate()));
        docPageReqShippingCostText.setText(docSale.getShippingCost().toString());
        docPageReqUnloadCostText.setText(docSale.getUnloadCost().toString());
        docPageReqTotalText.setText(docSale.getTotal().toString());
        mPassPersonEditText.setText(docSale.getPassPerson());
        mPassVehicleEditText.setText(docSale.getPassVehicle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
