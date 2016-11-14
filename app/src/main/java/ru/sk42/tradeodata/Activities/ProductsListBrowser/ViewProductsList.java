package ru.sk42.tradeodata.Activities.ProductsListBrowser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.ProductsListFragment;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;

public class ViewProductsList extends AppCompatActivity implements InteractionInterface {

    ProductsListFragment productsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products_list);



        productsListFragment = new ProductsListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_view_products_list, productsListFragment)
                .addToBackStack(productsListFragment.getClass().getName())
                .commit();
    }

    public void onBackPressed(){
        finish();
    }

    @Override
    public void onItemSelected(Object selectedObject) {
    }

    @Override
    public void onRequestSuccess(Object selectedObject) {
        Intent intent = new Intent();
        ProductInfo productInfo = (ProductInfo) selectedObject;
        intent.putExtra("ref_Key", productInfo.getRef_Key());
        setResult(0, intent);
        finish();

    }
}
