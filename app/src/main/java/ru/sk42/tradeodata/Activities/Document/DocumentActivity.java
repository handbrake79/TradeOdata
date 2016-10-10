package ru.sk42.tradeodata.Activities.Document;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Activities.ProductInfo.ProductInfo_Fragment;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.ProductsList_Fragment;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.LoadDataFromServer;
import ru.sk42.tradeodata.Services.MyResultReceiver;

public class DocumentActivity extends AppCompatActivity implements MyActivityFragmentInteractionInterface, MyResultReceiver.Receiver {

    private static final String TAG = "Document ACTIVITY";
    public MyResultReceiver mReceiver;
    ProductsList_Fragment productsList_fragment;
    Menu menu;
    ProgressDialog progressDialog;
    private DocSale docSale;
    private String docRef_Key;

    public String getDocRef_Key() {
        return docRef_Key;
    }

    public void setDocRef_Key(String docRef_Key) {
        this.docRef_Key = docRef_Key;
    }

    public DocSale getDocSale() {
        return docSale;
    }

    public void setDocSale(DocSale docSale) {
        this.docSale = docSale;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.document, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document__activity);
        progressDialog = new ProgressDialog(this);

        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", Constants.ModeNewOrder);

        if (mode == Constants.ModeExistingOrder) {
            docRef_Key = intent.getStringExtra("ref_Key");
        }

        if (mode == Constants.ModeNewOrder) {
            docSale = new DocSale();
        }
        if(docSale == null)  
            reloadDocSale();

        callDataLoaderService();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method isEmpty
        if (item.getItemId() == R.id.product_selection_menu_item)
            showProductsListFragment();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelection(Object object) {
        //это внутри фрагмента СтокИнфо при выборе строки склада
        if (object instanceof Stock) {
            if (docSale != null) {
                Toast.makeText(this, "добавили товар в документ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "не выбран документ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDetachFragment(Fragment fragment) {
        if (fragment instanceof DocumentFragment) {
            //закрыли документ, нужно закрыть активность
            //проверить изменения, сохранить
            this.finish();
        }
    }


    @Override
    public void onRequestSuccess(Object object) {
        if (object instanceof ProductInfo)
            showStockInfoFragment((ProductInfo) object);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    private void showProductsListFragment() {

        if (productsList_fragment != null && productsList_fragment.isVisible())
            return;

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        productsList_fragment = new ProductsList_Fragment();
        productsList_fragment.setArguments(getIntent().getExtras());

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, productsList_fragment, String.valueOf(R.id.rvProductsListFragment));
        fragmentTransaction.addToBackStack(productsList_fragment.getClass().getName());
        fragmentTransaction.commit();

    }

    private void showStockInfoFragment(ProductInfo productInfo) {

        if (productInfo == null) {
            productInfo = ProductInfo.getStub();
        }

        ProductInfo_Fragment productInfo_Fragment_Fragment = ProductInfo_Fragment.newInstance(productInfo.getRef_Key());
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, productInfo_Fragment_Fragment, String.valueOf(R.id.linearlayoutProductInfo));
        fragmentTransaction.addToBackStack(productInfo_Fragment_Fragment.getClass().getName());
        fragmentTransaction.commit();

    }

    private void showDocumentFragment() {
        setActionBarTitle();
        DocMainFragment fragment = new DocMainFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, String.valueOf(R.id.llDocMainFragment));
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();

    }

    private void setActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        String title = docSale.getNumber();
        actionBar.setTitle(title);
        actionBar.setWindowTitle(" WindowTitle");
        actionBar.setSubtitle(String.valueOf(docSale.getProducts().size() + " товаров"));

    }

    private void reloadDocSale(){

        docSale = DocSale.getObject(DocSale.class, getDocRef_Key());
    }



    private void callDataLoaderService() {
        Intent i = new Intent(this, LoadDataFromServer.class);
        i.putExtra("mode", Constants.DATALOADER_MODE.DOC.name());
        i.putExtra("from","Document");
        i.putExtra("ref_Key", getDocRef_Key());
        i.putExtra("receiverTag", mReceiver);
        startService(i);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode == 1) {
            Log.d(TAG, "onReceiveResult: SFO COMPLETED");
            //Видимо здесь нужно показывать фрагмент
            reloadDocSale();
            showDocumentFragment();
        }
    }




}
