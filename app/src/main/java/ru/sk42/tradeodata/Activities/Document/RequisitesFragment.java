package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class RequisitesFragment extends Fragment {
    static String TAG = "ReqFrag";
    private DocumentListenerInterface mActivityListener;

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

    @Bind(R.id.doc_page_req_til_discount_card)
    TextInputLayout tilDiscountCard;

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

        View view = inflater.inflate(R.layout.doc__page_req, container, false);
        ButterKnife.bind(this, view);

        mActivityListener = (DocumentListenerInterface) getActivity();

        return view;
    }

    public void initView() {
        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        if (mPassPersonEditText == null) {
            return;
        }
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
        mDiscountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!docSale.getDiscountCard().isEmpty()) {

                }
            }
        });
        if (card != null) {
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
    public void onStart() {
        super.onStart();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    void btnFindCardClick() {
        String mText = mDiscountCard.getText().toString();
        mActivityListener.onDiscountCardNumberEntered(mText, tilDiscountCard);
    }

    public void notifyDataChanged() {
        initView();
    }
}
