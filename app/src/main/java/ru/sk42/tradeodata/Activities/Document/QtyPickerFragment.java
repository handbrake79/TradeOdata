package ru.sk42.tradeodata.Activities.Document;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    // TODO: Rename and change types of parameters
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
    public static QtyPickerFragment newInstance(double mQty, double mPrice, int lineNumber) {
        QtyPickerFragment fragment = new QtyPickerFragment();
        Bundle args = new Bundle();
        args.putDouble(tagQty, mQty);
        args.putDouble(tagPrice, mPrice);
        args.putInt(tagLineNumber, lineNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQty = getArguments().getDouble(tagQty);
            mPrice = getArguments().getDouble(tagPrice);
            mLineNumber = getArguments().getInt(tagLineNumber);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qty_picker, container, false);
        ButterKnife.bind(this, view);

        initViewData();

        return view;

    }

    private void initViewData() {

        inputQtyNumberEdittext.setText(String.valueOf(mQty));
        double total = mQty * mPrice;
        mTotal.setText("На сумму " + String.valueOf(total));
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
                mListener.onQtyFragmentInteraction(mQty, mLineNumber);
                //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
        }
        initViewData();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnQtyFragmentInteractionListener {
        // TODO: Update argument type and name
        void onQtyFragmentInteraction(double qty, int lineNumber);
    }
}
