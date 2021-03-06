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
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.wang.avi.AVLoadingIndicatorView;

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

    AVLoadingIndicatorView avLoadingIndicatorView;
    RecyclerView mRecyclerView;

    int mProductListId = -1;
    boolean mSearchresultMode = false;

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
            mProductListId = getArguments().getInt(Constants.ID);
            mSearchresultMode = mProductListId != -1;
        }
        if (!mSearchresultMode) {
            productArrayList = new ProductsList().getArrayList();
        } else {
            try {
                productArrayList = MyHelper.getProductDao().queryForEq("productsList_id", mProductListId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.products_list__recyclerview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvProductsListFragment);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi_product_list);
        avLoadingIndicatorView.setVisibility(View.GONE);
        // Set the adapter
        Context context = mRecyclerView.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ProductsListAdapter(productArrayList, mListener, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext()));

        if (!mSearchresultMode) {
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
            showTopLevelProducts();
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

        showLoading();

        loadImage(product.getRef_Key());

        parentACtivity.setActivityTitle("Группа " + product.getDescription(), false);

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

    private void hideLoading() {
        avLoadingIndicatorView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.hide();
    }

    public void showLoading() {
        mRecyclerView.setVisibility(View.GONE);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();
    }

    public void showTopLevelProducts() {
        currentCategory = Product.getStub();
        parentACtivity.setActivityTitle("Верхний уровень справочника", true);
        String guid = Constants.ZERO_GUID;
        List<Product> list = getProductsByParent(guid);
        if (list == null || list.size() == 0) {
            showLoading();
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
        hideLoading();
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
