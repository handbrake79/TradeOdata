package ru.sk42.tradeodata.Activities.Document;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnQtyFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QtyPickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QtyPickerFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String tagQty = "tagQty";
    private static final String tagPrice = "tagPrice";
    private static final String tagLineNumber = "tagLineNumber";

    @Bind(R.id.qty_fragment_minus)
    Button inputQtyMinusButton;

    @Bind(R.id.qty_fragment_input_qty)
    EditText inputQtyNumberEdittext;

    @Bind(R.id.qty_fragment_plus)
    Button inputQtyPlusButton;

    @Bind(R.id.qty_fragment_ok)
    Button inputQtyOkButton;

    @Bind(R.id.qty_fragment_total)
    TextView mTotal;


    SaleRecord record;
    @Bind(R.id.qty_fragment_product_caption)
    TextView qtyFragmentProductCaption;
    private double mQty;
    private double mPrice;
    private int mLineNumber;

    private OnQtyFragmentInteractionListener mListener;

    public QtyPickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QtyPickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QtyPickerFragment newInstance(SaleRecord record) {
        QtyPickerFragment fragment = new QtyPickerFragment();
        fragment.record = record;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQty = record.getQty();
        mPrice = record.getPrice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qty_picker, container, false);
        ButterKnife.bind(this, view);

        inputQtyNumberEdittext.setText(String.valueOf(record.getQty()));
        qtyFragmentProductCaption.setText(record.getProduct().getDescription());
        recalc();

        inputQtyNumberEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mQty = 0;
                try {
                    mQty = Float.valueOf(editable.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                record.setQty(mQty);
                recalc();
            }
        });

        return view;

    }

    private void recalc() {
        mTotal.setText("Цена " + Uttils.formatDoubleToMoney(record.getPrice()) + " руб., сумма " + Uttils.formatDoubleToMoney(record.getTotal()) + " руб.");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQtyFragmentInteractionListener) {
            mListener = (OnQtyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnQtyFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.qty_fragment_minus, R.id.qty_fragment_plus, R.id.qty_fragment_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qty_fragment_minus:
                if (mQty > 0) {
                    if (mQty < 1) {
                        mQty = 0;
                    } else {
                        mQty--;
                    }

                }
                break;
            case R.id.qty_fragment_plus:
                mQty++;
                break;

            case R.id.qty_fragment_ok:

                try {
                    mQty = Float.valueOf(inputQtyNumberEdittext.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                record.setQty(mQty);
                mListener.onQtyFragmentInteraction(record);
                break;
        }
        record.setQty(mQty);
        recalc();
    }


    public interface OnQtyFragmentInteractionListener {
        // TODO: Update argument type and name
        void onQtyFragmentInteraction(SaleRecord record);
    }
}
