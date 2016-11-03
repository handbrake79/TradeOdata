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
import ru.sk42.tradeodata.Activities.Document.Adapters.SaleRowServiceRecyclerViewAdapter;
import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.DividerDecoration;

import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class ServicesFragment extends Fragment {
    static String TAG = "***ServiceFragment";
    SaleRowServiceRecyclerViewAdapter adapter;
    @Bind(R.id.rvDocPageServices)
    RecyclerView mServicesRecyclerView;
    private DocSale docSale;
    private InteractionInterface mListener;
    View view;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (InteractionInterface) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        view = inflater.inflate(R.layout.doc_page_services, container, false);
        ButterKnife.bind(this, view);

        mServicesRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
        mServicesRecyclerView.setSelected(true);
        mServicesRecyclerView.setLayoutManager(new LinearLayoutManager(mServicesRecyclerView.getContext()));
        adapter = new SaleRowServiceRecyclerViewAdapter(docSale.getServicesList(), (InteractionInterface) getActivity());
        mServicesRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;

    }

    public void notifyDataChanged(){
        adapter.notifyDataSetChanged();
    }
}
