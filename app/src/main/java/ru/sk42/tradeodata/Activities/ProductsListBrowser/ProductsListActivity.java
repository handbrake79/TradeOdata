package ru.sk42.tradeodata.Activities.ProductsListBrowser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.ProductInfo.ProductInfoFragment;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

public class ProductsListActivity extends AppCompatActivity implements InteractionInterface {

    private static final String TAG = "ProdListAct***";

    ProductsListFragment productsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products_list);
        setTitle("");


        productsListFragment = new ProductsListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_view_products_list, productsListFragment)
                .addToBackStack(productsListFragment.getClass().getName())
                .commit();


    }

    public void setActionBarTitle(String title) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View actionBarView = inflater.inflate(R.layout.custom_actionbar, null);
        TextView tv = (TextView) actionBarView.findViewById(R.id.customt_actionbar_caption);
        tv.setText(title);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsListFragment.showParentProducts();
            }
        });
        getSupportActionBar().setCustomView(actionBarView);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            Log.d(TAG, "onBackPressed: popBackStack");
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }

    }

    @Override
    public void onItemSelected(Object selectedObject) {
        Intent intent = new Intent();
        Stock stock = (Stock) selectedObject;
        intent.putExtra("id", stock.getId());
        setResult(0, intent);
        finish();
    }

    @Override
    public void onRequestSuccess(Object selectedObject) {
        setTitle("");

        ProductInfo productInfo = (ProductInfo) selectedObject;
        ProductInfoFragment fragment = ProductInfoFragment.newInstance(productInfo.getRef_Key());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_view_products_list, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

    }

}
