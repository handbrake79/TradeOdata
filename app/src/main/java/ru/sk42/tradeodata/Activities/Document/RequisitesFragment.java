package ru.sk42.tradeodata.Activities.Document;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class RequisitesFragment extends Fragment {
    static String TAG = "ReqFrag";
    private DocumentListenerInterface mActivityListener;

    @Bind(R.id.doc_page_req_number_text)
    TextView docPageReqNumberText;

    @Bind(R.id.doc_page_req_date_text)
    TextView docPageReqDateText;

//    @Bind(R.id.doc_page_req_customer_text)
//    TextView docPageReqCustomerText;

//    @Bind(R.id.doc_page_req_contract_text)
//    TextView docPageReqContractText;

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
    TextInputEditText mDiscountCard;

    @Bind(R.id.doc_page_req_find_card)
    Button btnFindCard;

    private DocSale docSale;

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityListener = null;
    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doc_page_req, container, false);
        ButterKnife.bind(this, view);

        mActivityListener = (DocumentListenerInterface) getActivity();

        return view;
    }

    public void initView() {
        docPageReqNumberText.setFocusable(true);
        docPageReqNumberText.requestFocus();
        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        if(docSale == null){
            Log.d(TAG, "initView: fuck");
            Log.wtf(TAG, "initView: ", new Throwable());
        }
        if(docSale.getAuthor() == null){
            Log.d(TAG, "initView: WTF!!!");
        }
        if(mAuthorText == null){
            Log.d(TAG, "initView: WTF!!!");
        }
        mAuthorText.setText(docSale.getAuthor().toString());
//        docPageReqContractText.setText(docSale.getContract().toString());
//        docPageReqContractText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "Изменить или создать новый договор", Toast.LENGTH_SHORT).show();
//            }
//        });
//        docPageReqCustomerText.setText(docSale.getCustomer().toString());
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
                mActivityListener.onPassPersonChanged(editable.toString());
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
                mActivityListener.onPassVehicleChanged(editable.toString());
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
                mActivityListener.onCommentChanged(editable.toString());
            }
        });

        DiscountCard card = docSale.getDiscountCard();
        if(card != null){
            mDiscountCard.setText(docSale.getDiscountCard().toString());
        }

        btnFindCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFindCardClick();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        initView();
    }
    @Override
    public void onResume(){
        super.onResume();
        initView();
    }

    private void findCard(String code){
        DiscountCard card = null;
        try {
            List<DiscountCard> list = MyHelper.getDiscountCardDao().queryForEq("code",code);
            card = list.get(0);
        }
        catch (Exception e){

        }
        if(card != null){
            mActivityListener.setDiscountCard(card);
            mDiscountCard.setText(card.toString());
        }
    }

    void btnFindCardClick() {

        String mText = mDiscountCard.getText().toString();

        if(!mText.isEmpty()){
            findCard(mText);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Номер карты");

// Set up the input
        final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = input.getText().toString();
                findCard(code);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
