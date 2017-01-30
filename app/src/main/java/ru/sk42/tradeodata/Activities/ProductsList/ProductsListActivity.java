package ru.sk42.tradeodata.Activities.ProductsList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.ProductInfo.ProductInfoFragment;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;

import static ru.sk42.tradeodata.R.id.frame_view_products_list;

public class ProductsListActivity extends AppCompatActivity implements ServiceResultReceiver.Receiver, InteractionInterface {

    private static final String TAG = "ProdListAct***";

    ProductsListFragment productsListFragment;
    ServiceResultReceiver mReceiver = new ServiceResultReceiver(new Handler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.view_products_list);
        //setTitle("");
        mReceiver.setReceiver(this);

        productsListFragment = new ProductsListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(frame_view_products_list, productsListFragment)
                .addToBackStack(productsListFragment.getClass().getName())
                .commit();

    }

    private void onButtonBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(frame_view_products_list);
        if (fragment instanceof ProductInfoFragment){
            getSupportFragmentManager().popBackStack();
        }
        else {
            productsListFragment.showParentProducts();
        }

    }

    public void setActionBarTitle(String title) {
//        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
//
//        View actionBarView = inflater.inflate(R.layout.custom_actionbar, null);
        TextView tv = (TextView) findViewById(R.id.products_list_group_caption);
        tv.setText(title);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonBackPressed();
            }
        });
        ImageButton button = (ImageButton) findViewById(R.id.products_list_button_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonBackPressed();
            }
        });

//        getSupportActionBar().setCustomView(actionBarView);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);

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
                .replace(frame_view_products_list, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

    }

    public void requestProductInfo(String product_key){
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra("mode", Constants.DATALOADER_MODE.REQUEST_PRODUCT_INFO.name());
        i.putExtra("ref_Key", product_key);
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "DocList");
        startService(i);


    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }
}
