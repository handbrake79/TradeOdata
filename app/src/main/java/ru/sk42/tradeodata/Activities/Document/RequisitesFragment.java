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
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.DividerDecoration;

import ru.sk42.tradeodata.Model.DocumentRequisites.Requisites;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;


// In this case, the fragment displays simple text based on the page
public class RequisitesFragment extends Fragment {
    static String TAG = "ReqFrag";
    Requisites_Adapter requisitesAdapter;

    @Bind(R.id.rvDocPageRequisites)
    RecyclerView mRequisitesView;

    private DocSale docSale;
    private MyActivityFragmentInteractionInterface mListener;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onDetachFragment(this);
    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        View view = inflater.inflate(R.layout.doc_page_requisites, container, false);
        ButterKnife.bind(this, view);

        mRequisitesView.addItemDecoration(new DividerDecoration(this.getContext()));
        mRequisitesView.setLayoutManager(new LinearLayoutManager(mRequisitesView.getContext()));
        Requisites requisites = Requisites.NewInstance(docSale);
        requisitesAdapter = new Requisites_Adapter(requisites.getArrayList(), (MyActivityFragmentInteractionInterface) getActivity());
        mRequisitesView.setAdapter(requisitesAdapter);
        requisitesAdapter.notifyDataSetChanged();

        return view;
    }

}
