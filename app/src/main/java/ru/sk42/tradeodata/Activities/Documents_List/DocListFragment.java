package ru.sk42.tradeodata.Activities.Documents_List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.DocSaleList;
import ru.sk42.tradeodata.R;


public class DocListFragment extends android.support.v4.app.Fragment  {

    static String TAG = "DocListFragment";
    RecyclerView mRecyclerView;
    DocList_Adapter mAdapter;
    DocSaleList doc_list;
    Date startDate = new Date();
    MyActivityFragmentInteractionInterface mListener;


    private HashMap<Class<?>, ArrayList<String>> unresolvedLinks;


    public DocListFragment() {
        // Required empty public constructor
    }

    public static DocListFragment newInstance(Bundle b) {
        DocListFragment fragment = new DocListFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        startDate.setTime(getArguments().getLong("startDate"));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.doc_list__frame, container, false);

        View rvView = view.findViewById(R.id.rvDocList);


        //create RecyclerView
        if (rvView instanceof RecyclerView) {
            Context context = rvView.getContext();
            mRecyclerView = (RecyclerView) rvView;
            mRecyclerView.setSelected(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            setAdapter();
            setActionBarTitle();

        }
        return view;


    }



    String getFormattedStartDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String d1 = fmt.format(startDate.getTime());
        return d1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyActivityFragmentInteractionInterface) {
            mListener = (MyActivityFragmentInteractionInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnQtyFragmentInteractionListener");
        }
    }

    private void setActionBarTitle(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        String title = getFormattedStartDate();
        if(doc_list != null)
            title += " документов " + doc_list.size().toString() + "" ;
        else
            title += " документов еще нет";
        actionBar.setTitle(title);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getActivity().finish();

    }




    private void setAdapter(){

        mAdapter = new DocList_Adapter(new ArrayList<DocSale>(), (MyActivityFragmentInteractionInterface) getActivity());
        doc_list = DocSaleList.getList();
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));
        mAdapter.setmValues(doc_list.getArrayList());
        mAdapter.notifyDataSetChanged();
    }






}
