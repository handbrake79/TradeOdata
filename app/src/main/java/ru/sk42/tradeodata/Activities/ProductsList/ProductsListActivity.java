package ru.sk42.tradeodata.Activities.ProductsList;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.Product.ProductActivity;
import ru.sk42.tradeodata.Activities.Product.StockFragment;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;

import static ru.sk42.tradeodata.Model.Constants.MODE_LABEL;
import static ru.sk42.tradeodata.Model.Constants.SHOW_PRODUCTS_LIST;
import static ru.sk42.tradeodata.Model.Constants.OPERATION_SUCCESS_LABEL;
import static ru.sk42.tradeodata.R.id.frame_view_products_list;

public class ProductsListActivity extends AppCompatActivity implements ServiceResultReceiver.ServiceResultReceiverInterface, InteractionInterface {

    private static final String TAG = "ProdListAct***";

    ProductsListFragment productsListFragment;
    ServiceResultReceiver mReceiver = new ServiceResultReceiver(new Handler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.products_list__view_products_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setTitle("");
        mReceiver.setReceiver(this);

        productsListFragment = new ProductsListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(frame_view_products_list, productsListFragment)
                .addToBackStack(productsListFragment.getClass().getName())
                .commit();

    }

    private void onButtonBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(frame_view_products_list);
        if (fragment instanceof StockFragment) {
            getSupportFragmentManager().popBackStack();
        } else {
            productsListFragment.showParentProducts();
        }

    }

    public void setActionBarTitle(String title) {

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
        StockFragment fragment = StockFragment.newInstance(productInfo.getRef_Key());

        getSupportFragmentManager().beginTransaction()
                .replace(frame_view_products_list, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

    }

    public void requestProductInfo(String product_key) {
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra(MODE_LABEL, Constants.REQUESTS.PRODUCT_INFO.ordinal());
        i.putExtra("ref_Key", product_key);
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "DocList");
        startService(i);
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        if (requestCode == Constants.SHOW_PRODUCTS_LIST && resultCode == 0) {

            Stock stock = null;
            int id = data.getIntExtra("id", -1);
            if (id != -1) {
                try {
                    stock = MyHelper.getStockDao().queryForId(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (stock != null) {
                    onItemSelected(stock);
                }
            }
        }
    }

    @Override
    public void onReceiveResultFromService(int resultCode, Bundle resultData) {
        if (resultCode == Constants.REQUESTS.PRODUCT_INFO.ordinal()) {
            boolean success = resultData.getBoolean(OPERATION_SUCCESS_LABEL, false);
            if (success) {
                Intent intent = new Intent(this, ProductActivity.class);
                String ref_Key = resultData.getString(Constants.REF_KEY_LABEL);
                intent.putExtra(Constants.REF_KEY_LABEL, ref_Key);
                startActivityForResult(intent, SHOW_PRODUCTS_LIST);
            }
        }
    }
}
