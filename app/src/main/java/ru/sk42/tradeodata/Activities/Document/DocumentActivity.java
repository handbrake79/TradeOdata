package ru.sk42.tradeodata.Activities.Document;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.sql.SQLException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.Adapters.DocumentFragmentPageAdapter;
import ru.sk42.tradeodata.Activities.InteractionInterface;
import ru.sk42.tradeodata.Activities.ProductInfo.ProductInfo_Fragment;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.ProductsListFragment;
import ru.sk42.tradeodata.Activities.ProductsListBrowser.ViewProductsList;
import ru.sk42.tradeodata.Activities.QtyInputActivity;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.Model.Document.SaleRecordProduct;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.LoadDataFromServer;
import ru.sk42.tradeodata.Services.ServiceResultReciever;

public class DocumentActivity extends AppCompatActivity implements InteractionInterface,
        ServiceResultReciever.Receiver,
        QtyPickerFragment.OnQtyFragmentInteractionListener,
        ShippingInterface {

    private static final String TAG = "Document ACTIVITY***";

    ProductsListFragment productsList_fragment;

    DocumentFragmentPageAdapter fragmentPagerAdapter;
    ViewPager viewPager;

    PagerSlidingTabStrip pagerSlidingTabStrip;

    boolean exit = false;

    public ServiceResultReciever mReceiver;

    //Fragments
    QtyPickerFragment qtyPickerFragment;
    ProductInfo_Fragment productInfo_Fragment;

    Menu menu;
    ProgressDialog progressDialog;
    private DocSale docSale;
    private String docRef_Key;

    @Bind(R.id.doc_footer_total_text)
    TextView mTotalText;


    @Bind(R.id.doc_total_need_shipping_text)
    TextView mNeedShippingText;

    @Bind(R.id.doc_total_shipping_cost_text)
    TextView mShippingCostText;

    @Bind(R.id.doc_total_need_unload_text)
    TextView mNeedUnloadText;

    @Bind(R.id.doc_total_unload_cost_text)
    TextView mUnloadCostText;

    @Bind(R.id.doc_total_workers_count_text)
    TextView mWorkersCountText;

    @Bind(R.id.doc_total_shipping_total_text)
    TextView mShippingTotalText;

    @Bind(R.id.footer_total_products_value)
    TextView mProductsTotalText;

    @Bind(R.id.footer_total_services_value)
    TextView mServicesTotalText;

    public String getDocRef_Key() {
        return docRef_Key;
    }

    public DocSale getDocSale() {
        return docSale;
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
        ButterKnife.bind(this, this);

        progressDialog = new ProgressDialog(this);

        fragmentPagerAdapter = new DocumentFragmentPageAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentPagerAdapter);

        // Give the PagerSlidingTabStrip the ViewPager
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        pagerSlidingTabStrip.setViewPager(viewPager);

        mReceiver = new ServiceResultReciever(new Handler());
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

        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fragmentPagerAdapter.notifyFragmentDataSetChanged(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        callDataLoaderService();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method isEmpty
        if (item.getItemId() == R.id.product_selection_menu_item)
            showProductsListFragment();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Object object) {
        //это внутри фрагмента СтокИнфо при выборе строки склада
        if (object instanceof Stock) {
            getSupportFragmentManager().popBackStack();
            if (docSale != null) {
                addNewProductRow((Stock) object);
            } else {
                Toast.makeText(this, "не выбран документ", Toast.LENGTH_SHORT).show();
            }

        }
        if (object instanceof SaleRecord) {
            showQtyPickerActivity((SaleRecord) object);
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

    }


    private void showProductsPage() {
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void showServicesPage() {
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(2);
            }
        });
    }

    @Override
    public void onRequestSuccess(Object object) {
        if (object instanceof ProductInfo)
            showProductInfoActivity((ProductInfo) object);
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
    public void onPassPersonChanged(String mPassPerson) {
        docSale.setPassPerson(mPassPerson);
    }

    @Override
    public void onPassVehicleChanged(String mPassVehicle) {
        docSale.setPassVehicle(mPassVehicle);
    }

    @Override
    public void onNeedShippingChanged(boolean needShipping) {
        docSale.setNeedShipping(needShipping);
        recalc();
    }

    private void recalc() {
        docSale.reCalculateTotal();
        refreshTotal();
        fragmentPagerAdapter.notifyFragmentDataSetChanged(1);
        fragmentPagerAdapter.notifyFragmentDataSetChanged(2);
    }

    @Override
    public void onNeedUnloadChanged(boolean needUnload) {
        docSale.setNeedUnload(needUnload);
        recalc();
    }

    @Override
    public void onShippingCostChanged(int shippingCost, TextInputLayout til) {

        docSale.setShippingCost(shippingCost);
        if (docSale.getNeedShipping() &&
                (shippingCost == 0 || shippingCost < docSale.getReferenceShipingCost())) {
            til.setError("Проверьте стоимость!");
        } else {
            til.setError(null);
        }
        recalc();
    }

    @Override
    public void onUnloadCostChanged(int unloadCost, TextInputLayout til) {
        docSale.setUnloadCost(unloadCost);
        if (unloadCost == 0 && docSale.getNeedUnload()) {
            til.setError("Проверьте стоимость!");
        } else {
            til.setError(null);
        }
        recalc();

    }

    @Override
    public void onWorkersChanged(int workers) {
        docSale.setWorkersCount(workers);
        recalc();
    }

    @Override
    public void onAddressChanged(String address) {
        docSale.setShippingAddress(address);
    }

    @Override
    public void onDateChanged(Calendar shippingDate, EditText editText) {
        if (Uttils.isShippingDateValid(shippingDate)) {
            docSale.setShippingDate(shippingDate.getTime());
            editText.setError(null);
        } else {
            editText.setError("Проверьте дату!");
        }
    }

    @Override
    public void onTimeFromChanged(Calendar timeFrom, EditText editText) {
        docSale.setShippingTimeFrom(timeFrom.getTime());
        if (Uttils.isShippingTimeValid(docSale.getShippingTimeFrom(), docSale.getShippingTimeTo())) {
            editText.setError(null);
        } else {
            editText.setError("Проверьте время доставки!");
        }
    }

    @Override
    public void onTimeToChanged(Calendar timeTo, EditText editText) {
        docSale.setShippingTimeTo(timeTo.getTime());
        if (Uttils.isShippingTimeValid(docSale.getShippingTimeFrom(), docSale.getShippingTimeTo())) {
            editText.setError(null);
        } else {
            editText.setError("Проверьте время доставки!");
        }
    }

    @Override
    public void onRouteChanged(String mRoute, TextView textView) {
        if (!docSale.getNeedShipping()) {
            return;
        }
        Route route = Route.getObjectByName(mRoute);
        docSale.setRoute(route);

        if (mRoute.isEmpty() && docSale.getNeedShipping()) {
            textView.setError("Укажите маршрут");
        } else {
            textView.setError(null);
        }
        recalc();
    }

    @Override
    public void onStartingPointChanged(String mStartingPoint, TextView textView) {
        if (!docSale.getNeedShipping()) {
            return;
        }
        StartingPoint startingPoint = StartingPoint.getObjectByName(mStartingPoint);
        docSale.setStartingPoint(startingPoint);

        recalc();

        if (textView != null) {
            if (mStartingPoint.isEmpty() && docSale.getNeedShipping()) {
                textView.setError("Укажите начальную точку маршрута!");
            } else {
                if (textView != null) {
                    textView.setError(null);
                }
            }
        }

    }

    @Override
    public void onVehicleTypeChanged(String mVehicleType, TextView textView) {
        if (!docSale.getNeedShipping()) {
            return;
        }
        VehicleType vehicleType = VehicleType.getObjectByName(mVehicleType);
        docSale.setVehicleType(vehicleType);

        recalc();

        if (textView != null) {
            if (mVehicleType.isEmpty() && docSale.getNeedShipping()) {
                textView.setError("Укажите тип ТС!");
            } else {
                textView.setError(null);
            }
        }


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
        Intent intent = new Intent(this, ViewProductsList.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
        {
            return;
        }
        if (requestCode == 100 && resultCode == 0) {
            ProductInfo productInfo = null;
            String ref_Key = data.getStringExtra("ref_Key");
            if (ref_Key != null) {
                try {
                    productInfo = ProductInfo.getObject(ProductInfo.class, ref_Key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (productInfo != null) {
                    showProductInfoActivity(productInfo);
                }
            }
        }
        if (requestCode == 200 && resultCode == 0) {

            Stock stock = null;
            int id = data.getIntExtra("id", -1);
            if (id != -1) {
                try {
                    stock = MyHelper.getStockDao().queryForId(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (stock != null) {
                    addNewProductRow(stock);
                }
            }
        }

        if (requestCode == 300 && resultCode == 0) {

            SaleRecord record = null;
            int id = data.getIntExtra("id", -1);
            double qty = data.getDoubleExtra("qty", -1);
            if (id != -1) {
                try {
                    record = MyHelper.getSaleRecordDao().queryForId(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (record != null) {
                    for (SaleRecordProduct rec :
                            docSale.getProducts()) {
                        if (rec.getId() == id) {
                            rec.setQty(qty);
                            recalc();
                        }
                    }
                }
            }
        }
    }


    private void showProductInfoActivity(ProductInfo productInfo) {

        Intent intent = new Intent(this, ViewProductsList.class);
        startActivityForResult(intent, 200);

    }


    private void showQtyPickerActivity(SaleRecord record) {
        if (record.getProduct_Key().equals(Constants.SHIPPING_GUID) ||
                record.getProduct_Key().equals(Constants.UNLOAD_GUID)) {
            return; //для услуг по доставке и разгрузке не редактируем кол-во
        }

        Intent intent = new Intent(this, QtyInputActivity.class);
        intent.putExtra("id", record.getId());
        startActivityForResult(intent, 300);
    }


    private void setActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        String title = docSale.getNumber() + " от " + Uttils.DATE_FORMATTER.format(docSale.getDate());
        actionBar.setTitle(title);
        actionBar.setWindowTitle("WindowTitle");
        actionBar.setSubtitle((!docSale.getRef_Key().equals(Constants.NULL_GUID) ? "записан, " : "не записан, ") + (docSale.getPosted() ? " проведен" : "не проведен"));

    }

    private void reloadDocSale() {
        docSale = DocSale.getObject(DocSale.class, getDocRef_Key());
    }


    private void callDataLoaderService() {
        Intent i = new Intent(this, LoadDataFromServer.class);
        i.putExtra("mode", Constants.DATALOADER_MODE.LOAD_MISSING_FOR_DOCUMENT.name());
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
            setActionBarTitle();
            refreshTotal();
        }
    }

    @Override
    public void onQtyFragmentInteraction(SaleRecord record) {
        getSupportFragmentManager().popBackStack();

        recalc();
    }

    public void saveLocal() {
        //что у нас не сохраняется сразу?
        //уберем setOnFocusListener
        //будем сохранять адрес, стоимость, грузчиков и т.п.
        //т.е. всё что не спиннеры и не дата время
        //shipp
        try {
            docSale.save();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void refreshTotal() {

        String total = "Итого " + Uttils.fd(docSale.getTotal()) + " руб, вес "
                + Uttils.fd(docSale.getWeight()) + " кг, объем "
                + Uttils.fd(docSale.getVolume()) + " м3, "
                + docSale.getProducts().size() + " товаров";

        mTotalText.setText(total);

        mProductsTotalText.setText(Uttils.fd(docSale.getProductsTotal()));

        mServicesTotalText.setText(Uttils.fd(docSale.getServicesTotal()));

        mNeedShippingText.setText(docSale.getNeedShipping() ? "Доставка" : "Самовывоз");

        mShippingCostText.setText(docSale.getShippingCost().toString());

        mNeedUnloadText.setText(docSale.getNeedUnload() ? "Грузчики" : "Без разгрузки");

        mUnloadCostText.setText(docSale.getUnloadCost().toString());

        mWorkersCountText.setText(docSale.getWorkersCount().toString());

        mShippingTotalText.setText(docSale.getShippingTotal().toString());
    }

}
