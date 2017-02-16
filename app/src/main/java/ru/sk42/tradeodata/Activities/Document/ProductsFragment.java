package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.Adapters.ProductRecordsAdapter;
import ru.sk42.tradeodata.Activities.ProductsList.DividerDecoration;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class ProductsFragment extends Fragment {
    static String TAG = "ProductsFragment";
    ProductRecordsAdapter adapter;
    @Bind(R.id.rvDocPageProducts)
    android.support.v7.widget.RecyclerView mProductsRecyclerView;
    private DocSale docSale;

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        mProductsRecyclerView = (RecyclerView) inflater.inflate(R.layout.doc__page_products, container, false);
        ButterKnife.bind(this, mProductsRecyclerView);

        mProductsRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
        mProductsRecyclerView.setSelected(true);
        mProductsRecyclerView.setLayoutManager(new LinearLayoutManager(mProductsRecyclerView.getContext()));
        setAdapter();
        return mProductsRecyclerView;
    }

    void setAdapter() {
        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();
        adapter = new ProductRecordsAdapter(docSale.getProductsList(), (SaleRecordInterface) getActivity());
        mProductsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void notifyDataChanged() {
        setAdapter();
        mProductsRecyclerView.scrollToPosition(docSale.getProducts().size() - 1);
    }

}
