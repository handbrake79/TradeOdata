package ru.sk42.tradeodata.Activities.Document;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.DividerDecoration;
import ru.sk42.tradeodata.Model.DocumentRequisites.Requisites;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;

// In this case, the fragment displays simple text based on the page
public class DocumentFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private DocSale docSale;

    static String TAG = "DocumentFragment";
    private MyActivityFragmentInteractionInterface mListener;

    private int mPage;

    Requisites_Adapter requisitesAdapter;
    SaleRowProductAdapter productsAdapter;
    SaleRowServiceAdapter servicesAdapter;
    RecyclerView rvReq;
    RecyclerView rvProducts;
    RecyclerView rvServices;


    public static DocumentFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        DocumentFragment fragment = new DocumentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onDetachFragment(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE, -1);

    }

//    private void reloadDocSale(){
//        DocumentActivity activity = (DocumentActivity) getActivity();
//        docSale = DocSale.getList(DocSale.class, activity.getDocRef_Key());
//    }
    private void setActionBarTitle(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        String title = docSale.getsNumber() ;
        actionBar.setTitle(title);
        actionBar.setWindowTitle(" WindowTitle" );
        actionBar.setSubtitle(String.valueOf(docSale.getProducts().size() + " товаров" ));

    }


    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        View rvView;

        DocumentActivity activity = (DocumentActivity) getActivity();
        docSale = activity.getDocSale();

        setActionBarTitle();

        showMessage(String.valueOf(mPage));

        switch (mPage) {
            case 0:
                rvView = inflater.inflate(R.layout.doc_page_requisites, container, false);

                // Set the adapter
                if (rvView instanceof RecyclerView) {
                    rvReq = (RecyclerView) rvView;
                    rvReq.addItemDecoration(new DividerDecoration(this.getContext()));

                    Context context = rvView.getContext();
                    //rvReq.setSelected(true);
                    rvReq.setLayoutManager(new LinearLayoutManager(context));
                    Requisites requisites = Requisites.NewInstance(docSale);
                    requisitesAdapter = new Requisites_Adapter(requisites.getArrayList(), (MyActivityFragmentInteractionInterface) getActivity());
                    rvReq.setAdapter(requisitesAdapter);
                    requisitesAdapter.notifyDataSetChanged();

                }


                return rvView;
            case 1:
                view = inflater.inflate(R.layout.doc_page_products, container, false);

                rvView = view.findViewById(R.id.rvDocPageProducts);

                // Set the adapter
                if (rvView instanceof RecyclerView) {
                    Context context = rvView.getContext();
                    rvProducts = (RecyclerView) rvView;
                    rvProducts.addItemDecoration(new DividerDecoration(this.getContext()));
                    rvProducts.setSelected(true);
                    rvProducts.setLayoutManager(new LinearLayoutManager(context));
                    productsAdapter = new SaleRowProductAdapter(docSale.getProductsList(), (MyActivityFragmentInteractionInterface) getActivity());
                    rvProducts.setAdapter(productsAdapter);
                    productsAdapter.notifyDataSetChanged();
                }


                return view;

            case 2: //SERVICES
                view = inflater.inflate(R.layout.doc_page_services, container, false);

                rvView = view.findViewById(R.id.rvDocPageServices);

                // Set the adapter
                if (rvView instanceof RecyclerView) {
                    Context context = rvView.getContext();
                    rvServices = (RecyclerView) rvView;
                    rvServices.addItemDecoration(new DividerDecoration(this.getContext()));
                    rvServices.setSelected(true);
                    rvServices.setLayoutManager(new LinearLayoutManager(context));
                    servicesAdapter = new SaleRowServiceAdapter(docSale.getServicesList(), (MyActivityFragmentInteractionInterface) getActivity());
                    rvServices.setAdapter(servicesAdapter);
                    servicesAdapter.notifyDataSetChanged();
                }


                return view;
            case 3:
                view = inflater.inflate(R.layout.doc_page_shipping, container, false);

                return view;

            case 4:
                view = inflater.inflate(R.layout.doc_page_pass, container, false);
                EditText edtPassVehicle = (EditText) view.findViewById(R.id.edtPassVehicle);
                EditText edtPassPerson = (EditText) view.findViewById(R.id.edtPassPerson);
                edtPassVehicle.setText(docSale.getPassVehicle());
                edtPassPerson.setText(docSale.getsPassPerson());
                return view;
        }
        return null;
    }

    private void showMessage(String s) {
        Log.d(TAG, "showProgress: " + s);
        //Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT).show();
    }


}
