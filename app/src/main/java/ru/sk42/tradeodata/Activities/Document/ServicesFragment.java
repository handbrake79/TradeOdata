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
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.DividerDecoration;

import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class ServicesFragment extends Fragment {
    static String TAG = "DocumentFragment";
    SaleRowServiceRecyclerViewAdapter servicesAdapter;
    @Bind(R.id.rvDocPageServices)
    RecyclerView mServicesRecyclerView;
    private DocSale docSale;
    private MyActivityFragmentInteractionInterface mListener;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onFragmentDetached(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        View view = inflater.inflate(R.layout.doc_page_services, container, false);
        ButterKnife.bind(this, view);

        mServicesRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
        mServicesRecyclerView.setSelected(true);
        mServicesRecyclerView.setLayoutManager(new LinearLayoutManager(mServicesRecyclerView.getContext()));
        servicesAdapter = new SaleRowServiceRecyclerViewAdapter(docSale.getServicesList(), (MyActivityFragmentInteractionInterface) getActivity());
        mServicesRecyclerView.setAdapter(servicesAdapter);
        servicesAdapter.notifyDataSetChanged();

        return view;

    }


}
