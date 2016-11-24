package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class RequisitesFragment extends Fragment {
    static String TAG = "ReqFrag";
    private ShippingInterface mListenerShipping;

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

    @Bind(R.id.input_pass_person)
    EditText mPassPersonEditText;

    @Bind(R.id.input_pass_vehicle)
    EditText mPassVehicleEditText;

    @Bind(R.id.input_comment)
    EditText mComment;

    @Bind(R.id.doc_page_req_disc_text)
    TextView mDiscountCard;

    private DocSale docSale;

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerShipping = null;
    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        View view = inflater.inflate(R.layout.doc_page_req, container, false);
        ButterKnife.bind(this, view);

        mListenerShipping = (ShippingInterface) getActivity();

        initView();
        return view;
    }

    public void initView() {
        mAuthorText.setText(docSale.getAuthor().toString());
        docPageReqContractText.setText(docSale.getContract().toString());
        docPageReqContractText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Изменить или создать новый договор", Toast.LENGTH_SHORT).show();
            }
        });
        docPageReqCustomerText.setText(docSale.getCustomer().toString());
        docPageReqNumberText.setText(docSale.getNumber());
        docPageReqDateText.setText(Uttils.DATE_FORMATTER.format(docSale.getDate()));

        docPageReqShippingCostText.setText(Uttils.formatInt(docSale.getShippingCost()));
        docPageReqUnloadCostText.setText(Uttils.formatInt(docSale.getUnloadCost()));

        mPassPersonEditText.setText(docSale.getPassPerson());
        mPassPersonEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mListenerShipping.onPassPersonChanged(editable.toString());
            }
        });

        mPassVehicleEditText.setText(docSale.getPassVehicle());
        mPassVehicleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mListenerShipping.onPassVehicleChanged(editable.toString());
            }
        });

        mComment.setText(docSale.getComment());
        mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mListenerShipping.onCommentChanged(editable.toString());
            }
        });

        DiscountCard card = docSale.getDiscountCard();
        if(card != null){
            mDiscountCard.setText(docSale.getDiscountCard().toString());
        }
        mAuthorText.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
