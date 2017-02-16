package ru.sk42.tradeodata.Activities.Product;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsList.DividerDecoration;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link InteractionInterface}
 * interface.
 */
public class StockFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "ProductInfo***";
    public ProductInfo productInfo;
    RecyclerView mRecyclerView;
    private int mColumnCount = 1;
    private InteractionInterface mListener;
    private Stock_Adapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public StockFragment() {
    }


    public static StockFragment newInstance(String refkey) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString(Constants.REF_KEY_LABEL, refkey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            String ref_Key = getArguments().getString(Constants.REF_KEY_LABEL);
            try {
                productInfo = MyHelper.getInstance().getDao(ProductInfo.class).queryForEq(Constants.REF_KEY_LABEL, ref_Key).get(0);
            } catch (Exception e) {
                Toast.makeText(this.getContext(), "не найден продакт_инфо в базе по ссылке " + ref_Key, Toast.LENGTH_LONG).show();
                throw new RuntimeException("не найден продакт_инфо в базе по ссылке " + ref_Key);
            }
        }else {
            throw new RuntimeException("Жопа какая-то!");
        }

        View view = inflater.inflate(R.layout.product_info__stock_fragment, container, false);

        TextView tvOutOfStock = (TextView) view.findViewById(R.id.tvProductInfo_outofstock);
        Button btnSelect = (Button) view.findViewById(R.id.btnSelectStock);
        if(productInfo.getArrayList().size() == 0){
            tvOutOfStock.setVisibility(View.VISIBLE);
            btnSelect.setVisibility(View.GONE);
        }

        View rvView = view.findViewById(R.id.rvStock);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStockSelected();
            }
        });

        // Set the adapter
        if (rvView instanceof RecyclerView) {
            Context context = rvView.getContext();
            mRecyclerView = (RecyclerView) rvView;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            productInfo = ProductInfo.getObject(ProductInfo.class, productInfo.getRef_Key());
            mAdapter = new Stock_Adapter(productInfo.getArrayList(), mListener);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionInterface) {
            mListener = (InteractionInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionInterface");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;

    }


    public void onStockSelected() {
        if (productInfo.getStocks().size() == 0)
            return;
        int pos = mAdapter.getSelectedItem();
        if (pos >= 0) {
            Stock stock = (Stock) productInfo.getStocks().toArray()[pos];
            mListener.onItemSelected(stock);
        }

    }


}
