package ru.sk42.tradeodata.Activities.ProductsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ProductsList;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.NetworkRequests.ProductsRequest;
import ru.sk42.tradeodata.NetworkRequests.RetroConstants;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceGenerator;


public class ProductsListFragment extends Fragment {

    View view;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    static String TAG = "***ProdList_Fragment";
    Product testProduct;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private InteractionInterface mListener;
    private Product currentCategory = new Product();
    private List<Product> productArrayList = new ArrayList<>();
    private ProductsListAdapter mAdapter;

    private ProductsListActivity parentACtivity;
    String lastViewedGroupKey;

    //ProgressDialog progressDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductsListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        productArrayList = new ProductsList().getArrayList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.products_list__fragment_products_browser_recyclerview, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new ProductsListAdapter(productArrayList, mListener, this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));


            lastViewedGroupKey = Settings.getLastViewedProductGroupStatic();
            if (lastViewedGroupKey != null && !lastViewedGroupKey.equals(Constants.ZERO_GUID)) {
                Product lastViewedGroup = Product.getObject(Product.class, lastViewedGroupKey);
                if (lastViewedGroup != null) {
                    showChildProducts(lastViewedGroup);
                }
            } else {
                showTopLevelProducts();
            }


        }

        return view;
    }


    public void onItemSelected(final Product product) {
        if (product.isFolder()) {
            Settings.setLastViewedProductGroupStatic(product.getRef_Key());
            showChildProducts(product);
        } else {

            Settings.setLastViewedProductGroupStatic(product.getParent_key());

            ((ProductsListActivity) getActivity()).requestProductInfo(product.getRef_Key());

        }

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
        parentACtivity = (ProductsListActivity) context;
    }

    public void showParentProducts() {
        if (currentCategory.isEmpty()) {
            return;
        }
        if (currentCategory.isFirstLevelCategory()) {
            showTopLevelProducts();
            return;
        }
        String guid = currentCategory.getParent_key();


        try {

            testProduct = MyHelper.getInstance().getDao(Product.class).queryForEq("ref_key", guid).get(0);
            currentCategory = testProduct;
            showChildProducts(currentCategory);

        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage());
            Toast.makeText(this.getContext(), "ОШИБКА ТЕСТ_ПРОДУКТ", Toast.LENGTH_LONG).show();
            System.exit(0);
        }


    }

    private List<Product> getProductsByParent(String guid) {
        Dao<Product, Object> dao = MyHelper.getProductDao();
        List<Product> list = null;
        try {
            list = dao.queryForEq("Parent_Key", guid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void setTitle(String title) {
        parentACtivity.setActionBarTitle(title);
    }

    void loadImage(String ref_Key) {
        Intent i = new Intent(this.getActivity(), CommunicationWithServer.class);
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.LOAD_IMAGE.ordinal());
        i.putExtra(Constants.REF_KEY_LABEL, ref_Key);
        getActivity().startService(i);
    }


    public void showChildProducts(Product product) {

        if (!product.isFolder()) {
            return; //это не группа, оставляем всё как есть
        }

        showMessage("Запрос к 1С, ждите");

        loadImage(product.getRef_Key());

        setTitle("Группа " + product.getDescription());

        String guid = product.getRef_Key();
        currentCategory = product;
        mAdapter.setParentProduct(currentCategory);
        List<Product> list = getProductsByParent(guid);

        if (list == null || list.size() == 0) {
            final ProductsRequest request = ServiceGenerator.createService(ProductsRequest.class);
            Call<ProductsList> call = request
                    .call(RetroConstants.getMapWithFieldRestriction("Parent_Key eq guid'" + guid + "'",
                            RetroConstants.productFieldsList)
                    );
            call.enqueue(new MyCallBack());
        } else
            updateView(list);
    }

    public void showMessage(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showTopLevelProducts() {
        currentCategory = Product.getStub();
        setTitle("Верхний уровень справочника");
        String guid = Constants.ZERO_GUID;
        List<Product> list = getProductsByParent(guid);
        if (list == null || list.size() == 0) {
            showMessage("Запрос к 1С");
            final ProductsRequest request = ServiceGenerator.createService(ProductsRequest.class);
            Call<ProductsList> call = request.call(RetroConstants.getMapWithFieldRestriction("Parent_Key eq guid'" + guid + "'", RetroConstants.productFieldsList));
            call.enqueue(new MyCallBack());
        } else
            updateView(list);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        currentCategory = null;
    }

    public void updateView(Object list) {
        productArrayList.clear();
        if (list instanceof ProductsList) {
            ProductsList mlist = (ProductsList) list;
            productArrayList.addAll(mlist.getArrayList());
        }
        if (list instanceof List) {
            productArrayList.addAll((ArrayList<Product>) list);
        }
        class ProductComporator implements Comparator<Product> {
            public int compare(Product p1, Product p2) {
                return p1.getDescription().compareTo(p2.getDescription());
            }
        }
        Collections.sort(productArrayList, new ProductComporator());
        mAdapter.notifyDataSetChanged();
    }

    class MyCallBack implements Callback<ProductsList> {
        @Override
        public void onResponse(Call<ProductsList> call, Response<ProductsList> response) {
            ProductsList list = response.body();
            list.save();
            updateView(list);
        }

        @Override
        public void onFailure(Call<ProductsList> call, Throwable t) {
            Log.e("", "onResponse: " + t.toString());
        }
    }


}
