package ru.sk42.tradeodata.Activities.Document;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.generalscan.NotifyStyle;
import com.generalscan.OnDataReceive;
import com.generalscan.bluetooth.BluetoothConnect;
import com.generalscan.bluetooth.BluetoothSettings;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.Adapters.DocumentFragmentPageAdapter;
import ru.sk42.tradeodata.Activities.LoadingFragment;
import ru.sk42.tradeodata.Activities.Product.ProductActivity;
import ru.sk42.tradeodata.Activities.ProductsList.ProductsListActivity;
import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.Activities.Settings.SettingsActivity;
import ru.sk42.tradeodata.BardoceReader.IntentIntegrator;
import ru.sk42.tradeodata.BardoceReader.IntentResult;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.SaleRecord;
import ru.sk42.tradeodata.Model.Document.SaleRecordProduct;
import ru.sk42.tradeodata.Model.Document.SaleRecordService;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;
import ru.sk42.tradeodata.View.SlidingTabLayout;

import static ru.sk42.tradeodata.Model.Constants.MODE_LABEL;


public class DocumentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SaleRecordInterface,
        ServiceResultReceiver.ServiceResultReceiverInterface,
        DocumentListenerInterface {

    private static final String TAG = "Document ACTIVITY***";
    View view;
    View headerView;
    private int productsCount;

    Activity mActivity;
    private String barcode = "";

    ActionBarDrawerToggle mDrawerToggle;

    DocumentFragmentPageAdapter fragmentPagerAdapter;

    RequisitesFragment requisitesFragment;
    ProductsFragment productsFragment;
    ServicesFragment servicesFragment;
    ShippingFragment shippingFragment;

    ViewPager mViewPager;

    boolean exit = false;

    public ServiceResultReceiver mReceiver;

    ProgressDialog progressDialog;
    private DocSale docSale;
    private String docRef_Key;

    private long doc_id;

    Snackbar bar;

    @Bind(R.id.doc_footer_total_text)
    TextView mTotalText;

    @Bind(R.id.document_toolbar)
    Toolbar myToolbar;

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

    @Bind(R.id.doc_drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.document_nav_view)
    NavigationView navigationView;

    private SlidingTabLayout mSlidingTabLayout;

    public String getDocRef_Key() {
        return docRef_Key;
    }

    public DocSale getDocSale() {
        return docSale;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doc_menu_toolbar, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc__activity);
        ButterKnife.bind(this, this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressDialog = new ProgressDialog(this);

        fragmentPagerAdapter = new DocumentFragmentPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.doc__viewpager);
        mViewPager.setPageMargin(1);
        int pagerPadding = 1;
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(pagerPadding, 0, pagerPadding, 0);
        mViewPager.setPageMarginDrawable(R.drawable.dotted_underline);

        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setCurrentItem(1);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        requisitesFragment = (RequisitesFragment) fragmentPagerAdapter.getItem(0);
        productsFragment = (ProductsFragment) fragmentPagerAdapter.getItem(1);
        servicesFragment = (ServicesFragment) fragmentPagerAdapter.getItem(2);
        shippingFragment = (ShippingFragment) fragmentPagerAdapter.getItem(3);

        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        setToolbar();

        setDrawer();

        Intent intent = getIntent();
        int mode = intent.getIntExtra(Constants.MODE_LABEL, Constants.ModeNewOrder);

        if (mode == Constants.ModeExistingOrder) {
            docRef_Key = intent.getStringExtra(Constants.REF_KEY_LABEL);
            doc_id = intent.getLongExtra(Constants.ID, -1);
        }

        if (mode == Constants.ModeNewOrder) {
            docSale = DocSale.newInstance();
            docRef_Key = docSale.getRef_Key();
        }

        if (docSale == null) {
            loadDocumentFromDB();
        }

        if (mode == Constants.ModeExistingOrder) {
            LoadMissingObjectsFrom1C();
        }

        attachScanner();

        updateActionBarTitle();

    }


    private void findProductByName(String name) {
        //mDrawerLayout.closeDrawer(Gravity.LEFT);
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.FIND_PRODUCT_BY_DESCRIPTION.ordinal());
        i.putExtra("from", "Document");
        i.putExtra(Constants.DESCRIPTION, name);
        i.putExtra("receiverTag", mReceiver);
        callService(i);
    }

    private void setDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, myToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        final EditText search = (EditText) headerView.findViewById(R.id.doc__nav_header_search_edittext);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                             @Override
                                             public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                                                 if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == 0) {
                                                     String text = textView.getText().toString();
                                                     if (!text.isEmpty() && text.length() > 3) {
                                                         findProductByName(text);
                                                         return true;
                                                     } else {
//                                                         showMessage("Минимум 3 символа для поиска");
                                                         return false;
                                                     }
                                                 } else {
                                                     return false;
                                                 }
                                             }
                                         }
        );


        mDrawerToggle.syncState();
    }

    private void setToolbar() {
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

    }

    private void attachScanner() {
        mActivity = this;
        BluetoothConnect.CurrentNotifyStyle = NotifyStyle.NotificationStyle1;
        BluetoothConnect.BindService(mActivity);

        /**
         * 连接成功
         */
        BluetoothConnect.SetOnDataReceive(new OnDataReceive() {
            @Override
            public void DataReceive(String s) {
                Log.d(TAG, "DataReceive: s = " + s);
                if ((s.equals("\n") || s.equals("\r")) && barcode.length() > 0) {

                    onBarcodeAquired(barcode);
                    barcode = "";
                }
                if (!s.equals("\n") && !s.equals("\r")) {
                    barcode += s;
                }
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                connectScaner();
            }
        }, Settings.getScannerStartDelayMillisStatic());

    }

    public void pairScaner() {
        BluetoothSettings.ACTION_BLUETOOTH_SETTINGS(mActivity);
    }

    public void setScaner() {
        BluetoothSettings.SetScaner(mActivity);
    }

    public void connectScaner() {
        try {
            BluetoothConnect.Connect();
        } catch (Exception e) {
            BluetoothConnect.Connect();
        }
    }

    @Override
    protected void onDestroy() {
        BluetoothConnect.UnBindService(mActivity);
        super.onDestroy();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method isEmpty
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.action_view_product_list:
                showProductsListActivity();
                break;
            case R.id.action_scanner_pair:
                pairScaner();
                break;
            case R.id.action_scanner_set:
                setScaner();
                break;
            case R.id.action_scanner_connect:
                connectScaner();
                break;
            case R.id.action_find_barcode:
                inputBarcode();
                break;
            case R.id.action_find_barcode_using_camera:
                inputBarcodeUsingCamera();
                break;
            case R.id.action_find_product_by_description:
                //TODO поиск по названию товара
                break;
            case R.id.action_settings:
                showSettingsActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void inputBarcode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите штрихкод");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String barcode = input.getText().toString();
                onBarcodeAquired(barcode);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void inputBarcodeUsingCamera() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    private void addProduct(Stock stock) {
        Product product = Product.getObject(Product.class, stock.getProductInfo().getRef_Key());
        if (product.isService()) {
            addServiceToDocument(stock, product);
        } else {
            addProductToDocument(stock, product);
        }

    }

    private void notifyItemAdded(SaleRecord record) {
        recalc();
        if (record instanceof SaleRecordProduct) {
            mViewPager.setCurrentItem(1);
            productsFragment.notifyItemAdded();
        } else {
            mViewPager.setCurrentItem(2);
            servicesFragment.notifyItemAdded();
        }
    }

    private void addProductToDocument(Stock stock, Product product) {
        SaleRecordProduct record = new SaleRecordProduct();
        record.setDocSale(docSale);
        record.setRef_Key(docSale.getRef_Key());
        record.setLineNumber(docSale.getProducts().size() + 1);
        record.setProduct(product);
        record.setProduct_Key(record.getProduct().getRef_Key());
        record.setCharact(stock.getCharact());
        record.setStore(stock.getStore());
        record.setUnit(stock.getUnit());
        record.setPrice(stock.getPrice());
        record.setTotal(record.getPrice());
        record.setQty(1f);
        record.setDiscountPercentAuto(0);
        record.setDiscountPercentManual(0);

        docSale.getProducts().add(record);
        notifyItemAdded(record);

    }


    private void addServiceToDocument(Stock stock, Product product) {
        SaleRecordService record = new SaleRecordService();
        record.setDocSale(docSale);
        record.setRef_Key(docSale.getRef_Key());
        record.setLineNumber(docSale.getProducts().size() + 1);
        record.setProduct(product);
        record.setProduct_Key(record.getProduct().getRef_Key());
        record.setPrice(stock.getPrice());
        record.setTotal(record.getPrice());
        record.setQty(1f);
        docSale.getServices().add(record);
        notifyItemAdded(record);
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 1) {
            Log.d(TAG, "onBackPressed: popBackStack");
            fm.popBackStack();
        } else {
            saveLocal();
            finish();
        }
    }

    @Override
    public void onCommentChanged(String mComment) {

        docSale.setChanged(true);

        docSale.setComment(mComment);
    }

    @Override
    public void onPassPersonChanged(String mPassPerson) {

        docSale.setChanged(true);

        docSale.setPassPerson(mPassPerson);
    }

    @Override
    public void onPassVehicleChanged(String mPassVehicle) {

        docSale.setChanged(true);

        docSale.setPassVehicle(mPassVehicle);
    }

    @Override
    public void onNeedShippingChanged(boolean needShipping) {

        docSale.setChanged(true);

        docSale.setNeedShipping(needShipping);
        recalc();
    }

    private void recalc() {


        docSale.setChanged(true);

        docSale.reCalculateTotal();

        //docSale.save();

        updateViewTotal();

    }

    @Override
    public void onNeedUnloadChanged(boolean needUnload) {

        docSale.setChanged(true);

        docSale.setNeedUnload(needUnload);
        recalc();
    }

    @Override
    public void onShippingCostChanged(int shippingCost, TextInputLayout til) {

        docSale.setChanged(true);
        if (shippingCost < docSale.getCalculatedShippingCost()) {
            til.setError("Минимальная стоимость " + Uttils.formatDoubleToMoney(docSale.getCalculatedShippingCost()) + "!");
            return;
        }
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

        docSale.setChanged(true);

        docSale.setUnloadCost(unloadCost);
        if (unloadCost == 0 && docSale.getNeedUnload()) {
            til.setError("Проверьте стоимость!");
        } else {
            til.setError(null);
        }
        recalc();

    }

    @Override
    public void onWorkersChanged(int workers, TextInputLayout til) {

        docSale.setChanged(true);

        docSale.setWorkersCount(workers);
        if (workers == 0 && docSale.getNeedUnload() || workers > 5) {
            til.setError("проверьте грузчиков!");
        } else {
            til.setError(null);
        }
        recalc();
    }

    @Override
    public void onAddressChanged(String address) {

        docSale.setChanged(true);

        docSale.setShippingAddress(address);
    }

    @Override
    public void onDateChanged(Calendar shippingDate, TextView editText) {

        docSale.setChanged(true);

        if (Uttils.isShippingDateValid(shippingDate)) {
            docSale.setShippingDate(shippingDate.getTime());
            editText.setError(null);
        } else {
            editText.setError("Проверьте дату!");
        }
    }

    @Override
    public void onTimeFromChanged(Calendar timeFrom, TextView editText) {

        docSale.setChanged(true);

        docSale.setShippingTimeFrom(timeFrom.getTime());
        if (Uttils.isShippingTimeValid(docSale.getShippingTimeFrom(), docSale.getShippingTimeTo())) {
            editText.setError(null);
        } else {
            editText.setError("Проверьте время доставки!");
        }
    }

    @Override
    public void onTimeToChanged(Calendar timeTo, TextView editText) {

        docSale.setChanged(true);

        docSale.setShippingTimeTo(timeTo.getTime());
        if (Uttils.isShippingTimeValid(docSale.getShippingTimeFrom(), docSale.getShippingTimeTo())) {
            editText.setError(null);
        } else {
            editText.setError("Проверьте время доставки!");
        }
    }

    @Override
    public void onRouteChanged(String mRoute, TextView textView) {

        docSale.setChanged(true);

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

        docSale.setChanged(true);

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

        docSale.setChanged(true);

        if (!docSale.getNeedShipping()) {
            return;
        }
        VehicleType vehicleType = VehicleType.getObjectByName(mVehicleType);
        if (vehicleType.getMaxTonnage() < docSale.getWeight()) {
            if (textView != null) {
                textView.setError("Слишком большой вес!");
                return;
            }
        }
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

    @Override
    public void onContactPersonChanged(String text, TextInputLayout tilContactPerson) {
        docSale.setChanged(true);

        docSale.setContactPerson(text);

        if (tilContactPerson != null) {
            if (text.isEmpty() && docSale.getNeedShipping()) {
                tilContactPerson.setError("Укажите, кому звонить!");
            } else {
                tilContactPerson.setError(null);
            }
        }

    }

    @Override
    public void onContactPersonPhoneChanged(String text, TextInputLayout tilContactPersonPhone) {

        docSale.setChanged(true);

        docSale.setContactPersonPhone(text);

        if (tilContactPersonPhone != null) {
            if (text.isEmpty() && docSale.getNeedShipping()) {
                tilContactPersonPhone.setError("Укажите телефон!");
            } else {
                tilContactPersonPhone.setError(null);
            }
        }


    }

    @Override
    public void onDiscountCardNumberEntered(String cardNumber, TextInputLayout tilCardNumber) {

        hideKeyboard();

        docSale.setChanged(true);

        if (cardNumber.isEmpty()) {
            docSale.setDiscountCard(DiscountCard.newInstance());
            return;
        }

        if (docSale.getPosted()) {
            tilCardNumber.setError("Документ проведен!");
            showError("Документ проведен, действие отменено");
            return;
        }

        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.DISCOUNT_CARD.ordinal());
        i.putExtra(Constants.REF_KEY_LABEL, cardNumber);
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "Document_Activity");
        callService(i);

    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.drawer_item_save:
                saveTo1C(false);
                break;
            case R.id.drawer_item_post:
                saveTo1C(true);
                break;
            case R.id.drawer_item_print:
                print();
                break;
            case R.id.doclist__item_settings:
                showSettingsActivity();
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fab_onclick(View view) {
        showProductsListActivity();
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

    class RemoveRecordDialogListener implements DialogInterface.OnClickListener {
        SaleRecord record;

        RemoveRecordDialogListener(SaleRecord mRecord) {
            record = mRecord;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    actuallyRemoveRecord(record);
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    }


    @Override
    public void removeRecord(SaleRecord record) {
        if (docSale.getPosted()) {
            showError("Документ проведен, изменения запрещены.");
            return;
        }
        handleRecordRemoved(record);
    }


    private void actuallyRemoveRecord(SaleRecord record) {
        SaleRecord currentRecord = findRecordInCollection(record);
        if (record instanceof SaleRecordProduct) {
            docSale.getProducts().remove(currentRecord);
            recalc();
            productsFragment.notifyItemRemoved();
            mViewPager.setCurrentItem(1);
        }

        if (record instanceof SaleRecordService) {
            docSale.getServices().remove(currentRecord);
            recalc();
            servicesFragment.notifyItemRemoved();
            mViewPager.setCurrentItem(2);
        }
    }


    private void showProductsListActivity() {
        if (docSale.getPosted()) {
            showError("Документ проведен, изменения запрещены.");
            return;
        }
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivityForResult(intent, Constants.SHOW_PRODUCTS_LIST);
    }

    private void showProductsListActivity(int id, String criteria) {
        if (docSale.getPosted()) {
            showError("Документ проведен, изменения запрещены.");
            return;
        }
        Intent intent = new Intent(this, ProductsListActivity.class);
        intent.putExtra(Constants.ID, id);
        intent.putExtra(Constants.MESSAGE_LABEL, criteria);
        startActivityForResult(intent, Constants.SHOW_PRODUCTS_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            IntentResult intentResult =
                    IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (intentResult != null) {

                String contents = intentResult.getContents();
                String format = intentResult.getFormatName();

                onBarcodeAquired(contents);
                Log.d("SEARCH_EAN", "OK, EAN: " + contents + ", FORMAT: " + format);
            } else {
                Log.e("SEARCH_EAN", "IntentResult je NULL!");
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("SEARCH_EAN", "CANCEL");
        }

        if (data == null) {
            return;
        }

        if (requestCode == Constants.SHOW_PRODUCTS_LIST && resultCode == 0 && !docSale.getPosted()) {

            Stock stock = null;
            int id = data.getIntExtra("id", -1);
            if (id != -1) {
                try {
                    stock = MyHelper.getStockDao().queryForId(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (stock != null) {
                    addProduct(stock);
                }
            }
        }

        if (requestCode == Constants.RECORD_SELECTED_IN_DOCUMENT && resultCode == 0) {
            //вызывается только для товаров, не для услуг
            double qty = data.getDoubleExtra("qty", -1);
            long id = data.getLongExtra(Constants.ID, -1);
            if (id != -1) {
                SaleRecordProduct record = getRecordByID(id);
                record.setQty(qty);
                mViewPager.setCurrentItem(1);
                notifyItemChanged(record);
            }
        }
    }


    private void notifyItemChanged(SaleRecord record) {

        recalc();

        if (record instanceof SaleRecordProduct) {
            //mViewPager.setCurrentItem(1);
            productsFragment.notifyItemChanged(record);
        }
        if (record instanceof SaleRecordService) {
            //mViewPager.setCurrentItem(2);
            servicesFragment.notifyItemChanged(record);
        }
    }

    private SaleRecord findRecordInCollection(SaleRecord record) {
        SaleRecord currentRecord = null;
        if (record instanceof SaleRecordProduct) {
            Iterator<SaleRecordProduct> it = docSale.getProducts().iterator();
            while (it.hasNext()) {
                currentRecord = it.next();
                if (currentRecord.getLineNumber() == record.getLineNumber()) {
                    return currentRecord;
                }
            }
        }
        if (record instanceof SaleRecordService) {
            Iterator<SaleRecordService> it = docSale.getServices().iterator();
            while (it.hasNext()) {
                currentRecord = it.next();
                if (currentRecord.getLineNumber() == record.getLineNumber()) {
                    return currentRecord;
                }
            }
        }
        if (currentRecord == null) {
            throw new RuntimeException("Ошибка поиска строки");
        }
        return currentRecord;
    }

    private SaleRecordProduct getRecordByID(long id) {
        SaleRecordProduct currentRecord = null;
        Iterator<SaleRecordProduct> it = docSale.getProducts().iterator();
        while (it.hasNext()) {
            currentRecord = it.next();
            if (currentRecord.getId() == id) {
                return currentRecord;
            }
        }
        if (currentRecord == null) {
            throw new RuntimeException("Ошибка поиска строки");
        }
        return currentRecord;
    }

    @Override
    public void plus(SaleRecord record) {
        if (docSale.getPosted()) {
            showError("Документ проведен, изменения запрещены.");
            return;
        }
        if (record.getProduct().getRef_Key().equals(Constants.SHIPPING_SERVICE_GUID) || record.getProduct().getRef_Key().equals(Constants.UNLOAD_SERVICE_GUID)) {
            showError("Услуги доставки и разгрузки не редактируются, пользуйтесь страницей Доставка");
            return;
        }
        docSale.save();
        SaleRecord changedRecord = findRecordInCollection(record);
        changedRecord.setQty(record.getQty() + 1);
        notifyItemChanged(changedRecord);
    }

    @Override
    public void minus(SaleRecord record) {

        if (docSale.getPosted()) {
            showError("Документ проведен, изменения запрещены.");
            return;
        }
        docSale.save();
        SaleRecord changedRecord = findRecordInCollection(record);

        double q = record.getQty();
        if (q <= 1) {
            handleRecordRemoved(changedRecord);
        }
        if (q > 1) {
            q--;
            changedRecord.setQty(q);
            notifyItemChanged(changedRecord);
        }
    }

    private void handleRecordRemoved(SaleRecord record) {
        RemoveRecordDialogListener listener = new RemoveRecordDialogListener(record);

        AlertDialog.Builder adb = new AlertDialog.Builder(this)
                .setTitle(record.getProduct().getDescription())
                .setPositiveButton("Да", listener)
                .setNegativeButton("Нет", listener)
                .setMessage("Удалить?");
        adb.create().show();

    }

    @Override
    public void onQtyChanged(SaleRecord mRecord, int action) {
        if (action == Constants.QTY_CHANGED) {
            if (mRecord.getProduct_Key().equals(Constants.SHIPPING_SERVICE_GUID) ||
                    mRecord.getProduct_Key().equals(Constants.UNLOAD_SERVICE_GUID)) {
                return; //для услуг по доставке и разгрузке не редактируем кол-во
            }

            SaleRecordProduct record = getRecordByID(mRecord.getId());
            record.setQty(mRecord.getQty());
            recalc();
            mViewPager.setCurrentItem(1);
            notifyItemChanged(record);


        }
    }

    @Override
    public void onRecordSelected(SaleRecord record, int action) {
        if (action == Constants.SELECT_RECORD_FOR_CHANGE) {
            if (record.getProduct_Key().equals(Constants.SHIPPING_SERVICE_GUID) ||
                    record.getProduct_Key().equals(Constants.UNLOAD_SERVICE_GUID)) {
                return; //для услуг по доставке и разгрузке не редактируем кол-во
            }

            recalc();
            Intent intent = new Intent(this, QtyInputActivity.class);
            intent.putExtra(Constants.ID, record.getId());
            intent.putExtra(Constants.QUANTITY, record.getQty());
            intent.putExtra(Constants.PRICE, record.getPrice());
            intent.putExtra(Constants.DESCRIPTION, record.getProduct().getDescription());
            startActivityForResult(intent, Constants.RECORD_SELECTED_IN_DOCUMENT);
        }
        if (action == Constants.SELECT_RECORD_FOR_VIEW_PRODUCT) {
            requestProductInfo(record.getProduct_Key());
        }
    }

    public void requestProductInfo(String product_key) {
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra(MODE_LABEL, Constants.REQUESTS.PRODUCT_INFO.ordinal());
        i.putExtra("ref_Key", product_key);
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "document");
        showLoading("Запрос товара");
        startService(i);
    }


    private void updateActionBarTitle() {
        if (docSale.getPosted()) {
            myToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.disabled_element));
        } else {
            myToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        }
        String title = docSale.getNumber().isEmpty() ? "Без номера " : docSale.getNumber();
        //title += " от " + Uttils.DATE_FORMATTER.format(docSale.getDate());
        myToolbar.setTitle(title);
        myToolbar.setSubtitle((!docSale.getRef_Key().equals(Constants.ZERO_GUID) ? "записан, " : "не записан, ") + (docSale.getPosted() ? " проведен" : "не проведен"));
    }

    private void loadDocumentFromDB() {
        if (!getDocRef_Key().equals(Constants.ZERO_GUID)) {
            docSale = DocSale.getObject(DocSale.class, getDocRef_Key());
        } else {
            docSale = DocSale.getByID(doc_id);
        }
    }

    private void soundNotification() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
        } catch (Exception e) {
            Log.d(TAG, "soundNotification: error");
            e.printStackTrace();
        }
    }

    private void showMessage(String text) {
        Snackbar.make(getWindow().getDecorView(), text, Toast.LENGTH_SHORT).show();
    }

    public void saveLocal() {
        if (docSale.getProductsList().isEmpty() && docSale.getServicesList().isEmpty()) {
            return;
        }
        //проверим некоторые поля на null
        if (docSale.getDiscountCard() == null) {
            docSale.setDiscountCard(DiscountCard.newInstance());
        }

        recalc();

        docSale.setDate(new Date());

        docSale.save();
    }

    public void updateViewTotal() {

        String total = "Итого " + Uttils.formatDoubleToMoney(docSale.getTotal()) + " руб, "
                + Uttils.formatDoubleToMoney(docSale.getWeight()) + " кг, V "
                + Uttils.formatDoubleToMoney(docSale.getVolume()) + " м3, "
                + docSale.getProducts().size() + " тов.";

        mTotalText.setText(total);

        mProductsTotalText.setText(Uttils.formatDoubleToMoney(docSale.getProductsTotal()));

        mServicesTotalText.setText(Uttils.formatDoubleToMoney(docSale.getServicesTotal()));

        mNeedShippingText.setText(docSale.getNeedShipping() ? "Доставка" : "Самовывоз");

        mShippingCostText.setText(Uttils.formatInt(docSale.getShippingCost()));

        mNeedUnloadText.setText(docSale.getNeedUnload() ? "Грузчики" : "Без разгрузки");

        mUnloadCostText.setText(Uttils.formatInt(docSale.getUnloadCost()));

        mWorkersCountText.setText(Uttils.formatInt(docSale.getWorkersCount()));

        mShippingTotalText.setText(Uttils.formatInt(docSale.getShippingTotal()));
    }


    private void print() {
        if (!docSale.getPosted()) {
            showError("Документ не проведен, печать не выполнена");
            return;
        }
        String printer = Settings.getPrinterStatic();
        if (printer == null) {
            showError("Не выбран принтер!");
            return;
        }
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.PRINT_DOCUMENT.ordinal());
        i.putExtra(Constants.DOC_NUMBER, docSale.getNumber());
        i.putExtra(Constants.PRINTER_NAME, printer);
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "Document_Activity");
        callService(i);

    }

    private void callService(Intent i) {
        String s = Constants.REQUESTS.values()[i.getIntExtra(Constants.MODE_LABEL, -1)].name();
        try {
            showLoading(s);
        } catch (Exception e) {
            Log.d(TAG, "callService: " + e.getMessage());
            e.printStackTrace();
        }
        startService(i);
    }

    private void showLoading(String... params) {
        LoadingFragment fragment = LoadingFragment.newInstance(params);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.doc__coord, fragment, "loading")
                .addToBackStack("loading")
                .commit();

    }


    private void hideLoading() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("loading");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getSupportFragmentManager().popBackStack();
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

    }

    private void showError(String s) {
        //здесь навалить всяких вибраций и звуков, ошибка же!
        soundNotification();
        vibrate();
        showMessage(s);
    }

    private void saveTo1C(boolean post) {
        saveLocal();
        productsCount = docSale.getProducts().size();
        Calendar shippingDate = GregorianCalendar.getInstance();
        shippingDate.setTime(docSale.getShippingDate());
        if (docSale.getNeedShipping() && !Uttils.isShippingDateValid(shippingDate)) {
            showError("Дата доставки указана неверно!");
            return;
        }
        if (post && docSale.getNeedShipping() && docSale.getWeight() == 0) {
            showError("Не указан вес товаров, оформление доставки невозможно!");
            return;
        }

        if (post && docSale.getNeedUnload() && docSale.getWorkersCount() == 0) {
            showError("Указана разгрузка, но нет грузчиков!");
            return;
        }

        if (docSale.getPosted()) {
            showError("Документ проведен, изменения запрещены.");
            return;
        }

        if (docSale.getRef_Key().equals(Constants.ZERO_GUID) && post) {
            showError("Документ не записан в 1С, проведение возможно только после записи.");
            return;
        }
        if (docSale.getNeedUnload()) {
            if (docSale.getWorkersCount() == 0 || docSale.getUnloadCost() == 0) {
                showError("Проверьте количество грузчиков и оплату за разгрузку.");
                return;
            }
        }

        if (docSale.getProducts().size() > 0 || docSale.getServices().size() > 0) {
            saveLocal();
        } else {
            showError("В документе нет товаров");
            return;
        }

        if (post) {
            Intent i = new Intent(this, CommunicationWithServer.class);
            i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.POST_DOCUMENT.ordinal());
            i.putExtra(Constants.ID, docSale.getId());
            i.putExtra(Constants.REF_KEY_LABEL, docRef_Key);
            i.putExtra("receiverTag", mReceiver);
            i.putExtra("from", "Document_Activity");
            callService(i);

        } else {
            Intent i = new Intent(this, CommunicationWithServer.class);
            i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.SAVE_DOCUMENT.ordinal());
            i.putExtra("id", docSale.getId());
            i.putExtra("receiverTag", mReceiver);
            i.putExtra("from", "Document_Activity");
            callService(i);
        }
    }

    private void LoadMissingObjectsFrom1C() {
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.LOAD_MISSING_FOR_DOCUMENT.ordinal());
        i.putExtra("from", "Document");
        i.putExtra(Constants.REF_KEY_LABEL, getDocRef_Key());
        i.putExtra("receiverTag", mReceiver);
        callService(i);
    }

    private void showProductActivity(String ref_Key) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(Constants.REF_KEY_LABEL, ref_Key);
        startActivityForResult(intent, Constants.SHOW_PRODUCTS_LIST);

    }

    private void onBarcodeAquired(String barcode) {
        barcode = barcode.replaceAll("[^0-9.]", "");
        Intent intent = new Intent(this, CommunicationWithServer.class);
        intent.putExtra(Constants.MODE_LABEL, Constants.REQUESTS.BARCODE.ordinal());
        intent.putExtra(Constants.BARCODE_LABEL, barcode);
        intent.putExtra("receiverTag", mReceiver);
        intent.putExtra("from", "Document_Activity");
        callService(intent);

    }

    @Override
    public void onReceiveResultFromService(int code, Bundle mResult) {
        if (code == Constants.FEEDBACK) {
            showMessage(mResult.getString(Constants.MESSAGE_LABEL));
            return;
        }

        Constants.REQUESTS requestedOperation = Constants.REQUESTS.values()[code];
        boolean success = mResult.getBoolean(Constants.OPERATION_SUCCESS_LABEL);
        String message = mResult.getString(Constants.MESSAGE_LABEL, "Ошибка не передана сервисом!");
        int resultCode = mResult.getInt(Constants.RESULT_CODE_LABEL);

        if (requestedOperation == Constants.REQUESTS.FIND_PRODUCT_BY_DESCRIPTION) {
            //получили список товаров размером от 0 до Х
            if (success) {
                int id = mResult.getInt(Constants.ID);
                showProductsListActivity(id, message);
            } else {
                showError(message);
            }
        }

        if (requestedOperation == Constants.REQUESTS.PRINT_DOCUMENT) {
            showMessage(message + "(" + String.valueOf(resultCode) + ")");
        }

        if (requestedOperation == Constants.REQUESTS.DISCOUNT_CARD) {
            if (success) {
                onDiscountCardFound(mResult.getString(Constants.REF_KEY_LABEL));
            } else {
                showError(message + "(" + String.valueOf(resultCode) + ")");
            }
        }

        if (requestedOperation == Constants.REQUESTS.PRODUCT_INFO) {
            if (mResult.getBoolean(Constants.OPERATION_SUCCESS_LABEL)) {
                showProductActivity(mResult.getString(Constants.REF_KEY_LABEL));
            } else {
                showError(mResult.getString("message"));
            }
        }

        if (requestedOperation == Constants.REQUESTS.BARCODE) {
            if (success) {
                showProductActivity(mResult.getString(Constants.REF_KEY_LABEL));
            } else {
                showError(message + "(" + String.valueOf(resultCode) + ")");
            }
        }

        if (requestedOperation == Constants.REQUESTS.SAVE_DOCUMENT || requestedOperation == Constants.REQUESTS.POST_DOCUMENT || requestedOperation == Constants.REQUESTS.LOAD_MISSING_FOR_DOCUMENT) {
            if (requestedOperation != Constants.REQUESTS.LOAD_MISSING_FOR_DOCUMENT) {
                showMessage(message + "(" + String.valueOf(resultCode) + ")");
            }
            if (success) {
                //Видимо здесь нужно показывать фрагмент
                docRef_Key = mResult.getString(Constants.REF_KEY_LABEL);
                loadDocumentFromDB();
                updateActionBarTitle();
//                fragmentPagerAdapter.notifyFragmentDataSetChanged(1);
                updateViewTotal();
                updateFragments();
                if (code == Constants.SAVE_COMPLETE) {
                    //покажем подарки
                    int gifts = docSale.getProducts().size() - productsCount;
                    if (gifts > 0) {
                        showMessage("Были добавлены подарки (" + String.valueOf(gifts) + ")!");
                    }
                }
            } else {
                showError(message + "(" + resultCode + ")");
            }
        }
        hideLoading();
    }

    private void updateFragments() {
        requisitesFragment.initView();
        productsFragment.initView();
        servicesFragment.initView();
        shippingFragment.initView();
    }

    private void onDiscountCardFound(String ref_Key) {
        DiscountCard card = DiscountCard.getObject(DiscountCard.class, ref_Key);
        if (card != null) {
            docSale.setDiscountCard(card);
            showMessage("Скидочная карта " + card.getDescription() + " выбрана");
            requisitesFragment.notifyDataChanged();
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}

