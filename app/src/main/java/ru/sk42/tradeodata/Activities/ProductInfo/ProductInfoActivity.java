package ru.sk42.tradeodata.Activities.ProductInfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

public class ProductInfoActivity extends AppCompatActivity implements InteractionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);
        ProductInfoFragment fragment = ProductInfoFragment.newInstance(getIntent().getStringExtra(Constants.REF_KEY_LABEL));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_product_info, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();

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
}
