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

    @Bind(R.id.qty_fragment_product_caption)
    TextView qtyFragmentProductCaption;

    String mDescription;
    private double quantity;
    private double mPrice;

    private OnQtyFragmentInteractionListener mListener;

    public QtyPickerFragment() {
        // Required empty public constructor
    }


    public static QtyPickerFragment newInstance(String description, double price, double qty) {
        QtyPickerFragment fragment = new QtyPickerFragment();
        fragment.mDescription = description;
        fragment.quantity = qty;
        fragment.mPrice = price;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the shake_anim for this fragment
        View view = inflater.inflate(R.layout.doc__fragment_qty_picker, container, false);
        ButterKnife.bind(this, view);

        inputQtyNumberEdittext.setText(String.valueOf(quantity));
        qtyFragmentProductCaption.setText(mDescription);
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
                quantity = 0;
                try {
                    quantity = Double.valueOf(editable.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                recalc();
            }
        });

        return view;

    }

    private void recalc() {
        mTotal.setText("Цена " + Uttils.formatDoubleToMoney(mPrice) + " руб., сумма " + Uttils.formatDoubleToMoney(mPrice * quantity) + " руб.");
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
                if (quantity > 0) {
                    if (quantity < 1) {
                        quantity = 0;
                    } else {
                        quantity--;
                    }

                }
                break;
            case R.id.qty_fragment_plus:
                quantity++;
                break;

            case R.id.qty_fragment_ok:

                try {
                    quantity = Double.valueOf(inputQtyNumberEdittext.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                mListener.onQtyFragmentInteraction(quantity);
                break;
        }
        recalc();
    }


    public interface OnQtyFragmentInteractionListener {
        // TODO: Update argument type and name
        void onQtyFragmentInteraction(double quantity);
    }
}
