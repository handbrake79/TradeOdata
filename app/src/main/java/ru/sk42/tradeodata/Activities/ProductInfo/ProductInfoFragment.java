package ru.sk42.tradeodata.Activities.ProductInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.DividerDecoration;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link InteractionInterface}
 * interface.
 */
public class ProductInfoFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "ProductInfo***";
    public ProductInfo productInfo;
    RecyclerView mRecyclerView;
    private int mColumnCount = 1;
    private InteractionInterface mListener;
    private ProductInfo_Adapter mAdapter;
    private ProgressDialog progress;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public ProductInfoFragment() {
    }


    public static ProductInfoFragment newInstance(String refkey) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putString("ref_Key", refkey);
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
            String ref_Key = getArguments().getString("ref_Key");
            try {
                productInfo = MyHelper.getInstance().getDao(ProductInfo.class).queryForEq("ref_Key", ref_Key).get(0);
            } catch (Exception e) {
                Toast.makeText(this.getContext(), "не найден продакт_инфо в базе по ссылке " + ref_Key, Toast.LENGTH_LONG).show();
            }
        }

        View view = inflater.inflate(R.layout.product_info_fragment, container, false);
        progress = new ProgressDialog(this.getContext());

        TextView tvProductDescription = (TextView) view.findViewById(R.id.tvProductInfo_ProductDescription);
        tvProductDescription.setText(productInfo.getDescription());

        View rvView = view.findViewById(R.id.rvStock);
        Button btnSelect = (Button) view.findViewById(R.id.btnSelectStock);
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
            mAdapter = new ProductInfo_Adapter(productInfo.getArrayList(), mListener);
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
