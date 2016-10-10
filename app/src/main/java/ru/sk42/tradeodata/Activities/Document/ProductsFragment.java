package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.DividerDecoration;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class ProductsFragment extends Fragment {
    static String TAG = "ProductsFragment";
    SaleRowProductAdapter productsAdapter;
    @Bind(R.id.rvDocPageProducts)
    android.support.v7.widget.RecyclerView mProductsRecyclerView;
    private DocSale docSale;
    private MyActivityFragmentInteractionInterface mListener;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onDetachFragment(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();


        mProductsRecyclerView = (RecyclerView) inflater.inflate(R.layout.doc_page_products, container, false);
        ButterKnife.bind(this, mProductsRecyclerView);


        mProductsRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
        mProductsRecyclerView.setSelected(true);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(mProductsRecyclerView.getContext()));
        productsAdapter = new SaleRowProductAdapter(docSale.getProductsList(), (MyActivityFragmentInteractionInterface) getActivity());
        mProductsRecyclerView.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();


        return mProductsRecyclerView;
    }

    private void showMessage(String s) {
        Log.d(TAG, "showProgress: " + s);
        //Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT).show();
    }


}
