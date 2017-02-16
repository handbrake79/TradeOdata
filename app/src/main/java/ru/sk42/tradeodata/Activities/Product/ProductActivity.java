package ru.sk42.tradeodata.Activities.Product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Model.Catalogs.ImageProduct;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;

public class ProductActivity extends AppCompatActivity implements InteractionInterface, ServiceResultReceiver.ReceiverInterface {

    ProductPresenterContract.ProductDescriptionContract mProductDescriptionContract;
    ServiceResultReceiver mReceiver;
    String ref_Key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info__activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ref_Key = getIntent().getStringExtra(Constants.REF_KEY_LABEL);

        DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(ref_Key);
        mProductDescriptionContract = descriptionFragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_product_description, descriptionFragment)
                .addToBackStack(descriptionFragment.getClass().getName())
                .commit();


        StockFragment fragment = StockFragment.newInstance(ref_Key);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_product_stock, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        setTitle();
        showImage();

    }

    private void setTitle(){
        String title = "Назад";
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewActionBar = inflater.inflate(R.layout.doclist__custom_actionbar, null);

        TextView tv = (TextView) viewActionBar.findViewById(R.id.customt_actionbar_caption);
        tv.setText(title);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setCustomView(viewActionBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("");

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
    public void onRequestSuccess(Object obj) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void showImage(){
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.LOAD_IMAGE.ordinal());
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", this.getLocalClassName());
        i.putExtra(Constants.REF_KEY_LABEL, ref_Key);
        startService(i);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode == Constants.REQUESTS.LOAD_IMAGE.ordinal()){
            if(resultData.getBoolean(Constants.OPERATION_SUCCESS_LABEL)){
                Bitmap bitmap = ImageProduct.getBitMapByRefKey(resultData.getString(Constants.REF_KEY_LABEL));
                mProductDescriptionContract.showImage(bitmap);
            }
        }
    }
}
