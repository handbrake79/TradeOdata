package ru.sk42.tradeodata.Activities.Document;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;

import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Activities.ProductInfo.ProductInfo_Fragment;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.ProductsList_Fragment;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.Model.Documents.SaleRecordProduct;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.LoadDataFromServer;
import ru.sk42.tradeodata.Services.MyResultReceiver;

public class DocumentActivity extends AppCompatActivity implements MyActivityFragmentInteractionInterface,
        MyResultReceiver.Receiver,
        QtyPickerFragment.OnQtyFragmentInteractionListener,
        ShippingFragment.ShippingInterface
{

    private static final String TAG = "Document ACTIVITY";

    boolean exit = false;

    public MyResultReceiver mReceiver;

    //Fragments
    DocMainFragment mainFragment;
    ProductsList_Fragment productsList_fragment;
    QtyPickerFragment qtyPickerFragment;
    ProductInfo_Fragment productInfo_Fragment;
    //fragments


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

        mainFragment = new DocMainFragment();

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
        if (docSale == null)
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
            getSupportFragmentManager().popBackStack();
            if (docSale != null) {
                addNewProductRow((Stock) object);
            } else {
                Toast.makeText(this, "не выбран документ", Toast.LENGTH_SHORT).show();
            }

        }
        if (object instanceof SaleRecordProduct) {
            showQtyPickerFragment((SaleRecordProduct) object);
        }
    }

    private void addNewProductRow(Stock stock) {
        SaleRecordProduct row = new SaleRecordProduct();
        row.setDocSale(docSale);
        row.setRef_Key(docSale.getRef_Key());
        row.setLineNumber(docSale.getProducts().size() + 1);
        row.setProduct(Product.getObject(Product.class, stock.getProductInfo().getRef_Key()));
        row.setProduct_Key(row.getProduct().getRef_Key());
        row.setCharact(stock.getCharact());
        row.setStore(stock.getStore());
        row.setUnit(stock.getUnit());
        row.setPrice(stock.getPrice());
        row.setTotal(row.getPrice());
        row.setQty(1f);
        row.setDiscountPercentAuto(0);
        row.setDiscountPercentManual(0);

        docSale.getProducts().add(row);

        reloadDocSale();
        //showDocumentFragment();


    }

    @Override
    public void onFragmentDetached(Fragment fragment) {
////        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mainFragment).commit();
        if (fragment instanceof DocMainFragment) {
            Log.d(TAG, "onFragmentDetached: finish!");
            onBackPressed();
        } else {
            showProductsPage();
        }
    }

    private void showProductsPage() {
        mainFragment.viewPager.post(new Runnable() {
            @Override
            public void run() {
                mainFragment.viewPager.setCurrentItem(1);
            }
        });

    }


    @Override
    public void onRequestSuccess(Object object) {
        if (object instanceof ProductInfo)
            showStockInfoFragment((ProductInfo) object);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 1) {
            Log.d(TAG, "onBackPressed: popBackStack");
            fm.popBackStack();
        } else {

            SaveDialogListener listener = new SaveDialogListener();

            AlertDialog.Builder adb = new AlertDialog.Builder(this)
                    .setTitle("Документ").setPositiveButton("Да", listener)
                    .setNegativeButton("Нет", listener)
                    .setNeutralButton("Отмена", listener)
                    .setMessage("Сохранить изменения локально?");
            adb.create().show();

            if (exit) {
                finish();
            }

        }
    }

    @Override
    public void onShippingChanged(boolean needShipping) {

    }

    @Override
    public void onUnloadChanged(boolean needUnload) {

    }

    @Override
    public void onShippingCostChanged(int shippingCost) {
        docSale.setShippingCost(shippingCost);
        docSale.reCalculateTotal();
        mainFragment.refreshTotal();

    }

    @Override
    public void onUnloadCostChanged(int unloadCost) {

    }

    @Override
    public void onWorkersChanged(int workers) {

    }

    @Override
    public void onAddressChanged(String address) {

    }

    @Override
    public void onShippingDateChanged(String shippingDate) {

    }

    @Override
    public void onShippingTimeFromChanged(String timeFrom) {

    }

    @Override
    public void onShippingTimeToChanged(String timeTo) {

    }

    @Override
    public void onRouteChanged(String route) {

    }

    @Override
    public void onStartingPointChanged(String startingPoint) {

    }

    @Override
    public void onVehicleTypeChanged(String vehicleType) {

    }

    class SaveDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    saveLocal();
                    finish();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    finish();
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    DocumentActivity.this.exit = false;
                    break;
            }
        }
    }


    private void showProductsListFragment() {

        if (productsList_fragment != null && productsList_fragment.isVisible()) {
            return;
        }

        productsList_fragment = new ProductsList_Fragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, productsList_fragment, String.valueOf(R.id.rvProductsListFragment))
                .addToBackStack(productsList_fragment.getClass().getName())
                .commit();

    }

    private void showStockInfoFragment(ProductInfo productInfo) {

        if (productInfo == null) {
            Log.e(TAG, "showStockInfoFragment: productInfo is NULL", new Exception());
            finish();
        }

        productInfo_Fragment = ProductInfo_Fragment.newInstance(productInfo.getRef_Key());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, productInfo_Fragment, String.valueOf(R.id.linearlayoutProductInfo))
                .addToBackStack(productInfo_Fragment.getClass().getCanonicalName())
                .commit();

    }

    private void showDocumentFragment() {
        setActionBarTitle();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mainFragment, String.valueOf(R.id.llDocMainFragment))
                .addToBackStack(mainFragment.getClass().getName())
                .commit();

    }

    private void showQtyPickerFragment(SaleRecordProduct rowProduct) {
        qtyPickerFragment = QtyPickerFragment.newInstance(rowProduct.getQty().floatValue(), rowProduct.getLineNumber());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, qtyPickerFragment, qtyPickerFragment.getClass().getName())
                .addToBackStack(qtyPickerFragment.getClass().getName())
                .commit();

    }


    private void setActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        String title = docSale.getNumber() + " от " + Constants.DATE_FORMATTER.format(docSale.getDate());
        actionBar.setTitle(title);
        actionBar.setWindowTitle("WindowTitle");
        actionBar.setSubtitle((!docSale.getRef_Key().equals(Constants.NULL_GUID) ? "записан, " : "не записан, ") + (docSale.getPosted() ? " проведен" : "не проведен"));

    }

    private void reloadDocSale() {
        docSale = DocSale.getObject(DocSale.class, getDocRef_Key());
    }


    private void callDataLoaderService() {
        Intent i = new Intent(this, LoadDataFromServer.class);
        i.putExtra("mode", Constants.DATALOADER_MODE.DOC.name());
        i.putExtra("from", "Document");
        i.putExtra("ref_Key", getDocRef_Key());
        i.putExtra("receiverTag", mReceiver);
        startService(i);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 1) {
            Log.d(TAG, "onReceiveResult: SFO COMPLETED");
            //Видимо здесь нужно показывать фрагмент
            reloadDocSale();
            showDocumentFragment();
        }


    }


    @Override
    public void onQtyFragmentInteraction(float qty, int lineNumber) {
        getSupportFragmentManager().popBackStack();
        showProductsPage();
        for (SaleRecordProduct row : docSale.getProducts()
                ) {
            if (row.getLineNumber().intValue() == lineNumber)
                row.setQty(qty);

        }
        docSale.reCalculateTotal();
        mainFragment.viewPager.post(new Runnable() {
            @Override
            public void run() {
                mainFragment.viewPager.setCurrentItem(1);
            }
        });


    }

    public void saveLocal() {
        try {
            docSale.save();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
