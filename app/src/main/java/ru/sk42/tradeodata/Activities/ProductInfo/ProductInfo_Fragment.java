package ru.sk42.tradeodata.Activities.ProductInfo;

import android.app.ProgressDialog;
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

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Activities.MyCallBackInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.DividerDecoration;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link MyActivityFragmentInteractionInterface}
 * interface.
 */
public class ProductInfo_Fragment extends  android.support.v4.app.Fragment  implements MyCallBackInterface {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public ProductInfo productInfo;
    RecyclerView mRecyclerView;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyActivityFragmentInteractionInterface mListener;
    private ProductInfo_Adapter mAdapter;
    private ProgressDialog progress;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public ProductInfo_Fragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductInfo_Fragment newInstance(String refkey) {
        ProductInfo_Fragment fragment = new ProductInfo_Fragment();
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
        Button btnSelect = (Button) view.findViewById(R.id.btnSelect);
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
        if (context instanceof MyActivityFragmentInteractionInterface) {
            mListener = (MyActivityFragmentInteractionInterface) context;
            mListener.onAttachFragment(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MyActivityFragmentInteractionInterface");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener.onDetachFragment(this);
        mListener = null;

    }


    public void onStockSelected() {
        int pos = mAdapter.getSelectedItem();
        Stock stock = (Stock) productInfo.getStocks().toArray()[pos];

        mListener.onItemSelection(stock);
    }

    @Override
    public void onAllRequestsComplete() {
        progress.dismiss();

    }

    public void showMessage(String title, String message) {
//        tvLog.append(title + " : " + message + "\n");
        progress.setIndeterminate(true);
        progress.setTitle(title);
        progress.setMessage(message);
        progress.show();
    }


}
