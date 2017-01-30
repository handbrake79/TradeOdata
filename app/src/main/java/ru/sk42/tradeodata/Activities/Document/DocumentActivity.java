package ru.sk42.tradeodata.Activities.Document;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.generalscan.NotifyStyle;
import com.generalscan.OnConnectedListener;
import com.generalscan.OnDisconnectListener;
import com.generalscan.SendConstant;
import com.generalscan.bluetooth.BluetoothConnect;
import com.generalscan.bluetooth.BluetoothSettings;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.Adapters.DocumentFragmentPageAdapter;
import ru.sk42.tradeodata.Activities.Document.Adapters.DrawerAdapter;
import ru.sk42.tradeodata.Activities.ProductInfo.ProductInfoActivity;
import ru.sk42.tradeodata.Activities.ProductsList.ProductsListActivity;
import ru.sk42.tradeodata.Activities.Scanner.ReadBroadcast;
import ru.sk42.tradeodata.Activities.Settings.Settings;
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
import ru.sk42.tradeodata.Model.St;
import ru.sk42.tradeodata.Model.Stock;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.Services.CommunicationWithServer;
import ru.sk42.tradeodata.Services.ServiceResultReceiver;

public class DocumentActivity extends AppCompatActivity implements SaleRecordInterface,
        ServiceResultReceiver.Receiver,
        DocumentListenerInterface {

    private static final String TAG = "Document ACTIVITY***";
    private static final int REQ_CODE_SPEECH_INPUT = 75334; //speech
    public static final int SHOW_PRODUCTS_LIST = 100;
    public static final int ON_RECORD_SELECTED = 300;

    Toolbar myToolbar;
    Activity mActivity;
    private ReadBroadcast mReadBroadcast;
    private String barcode = "";

    ActionBarDrawerToggle mDrawerToggle;

    DocumentFragmentPageAdapter fragmentPagerAdapter;
    ViewPager viewPager;

    PagerSlidingTabStrip pagerSlidingTabStrip;

    private SaleRecord currentRecord;

    boolean exit = false;

    public ServiceResultReceiver mReceiver;

    public void onToolbarClick(View view) {
    }

    ProgressDialog progressDialog;
    private DocSale docSale;
    private String docRef_Key;

    private long doc_id;

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

    @Bind(R.id.doc_drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.doc_listview)
    ListView mDrawerList;

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
        setContentView(R.layout.document__activity);
        ButterKnife.bind(this, this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressDialog = new ProgressDialog(this);

        fragmentPagerAdapter = new DocumentFragmentPageAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentPagerAdapter);

        // Give the PagerSlidingTabStrip the ViewPager
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        pagerSlidingTabStrip.setViewPager(viewPager);

        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", Constants.ModeNewOrder);

        if (mode == Constants.ModeExistingOrder) {
            docRef_Key = intent.getStringExtra(Constants.REF_KEY_LABEL);
            doc_id = intent.getLongExtra(Constants.ID, -1);
        }

        if (mode == Constants.ModeNewOrder) {
            docSale = DocSale.newInstance();
            docRef_Key = docSale.getRef_Key();
        }

        if (docSale == null)
            loadDocumentFromDB();

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

        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        mDrawerList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        mDrawerList.setDividerHeight(1);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerAdapter<String>(this,
                R.layout.list_item_layout, Constants.DOCUMENT_ACTIONS));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(DocumentActivity.this, "sdfsdfsdf", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(DocumentActivity.this, "sdfsdfsdf", Toast.LENGTH_SHORT).show();

            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerList.bringToFront();
                mDrawerLayout.requestLayout();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        myToolbar = (Toolbar) findViewById(R.id.toolbar_document);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //**
        if (mode == Constants.ModeExistingOrder) {
            networkLoadMissingObjectFrom1C();
        }

        //Scanner
        mActivity = this;
        BluetoothConnect.CurrentNotifyStyle = NotifyStyle.NotificationStyle1;
        BluetoothConnect.BindService(mActivity);

        /**
         * 连接成功
         */
        BluetoothConnect.SetOnConnectedListener(new OnConnectedListener() {

            @Override
            public void Connected() {
                //Toast.makeText(mActivity, "Сканер подключен", Toast.LENGTH_SHORT).show();
            }

        });
        /**
         * 断开连接
         */
        BluetoothConnect.SetOnDisconnectListener(new OnDisconnectListener() {

            @Override
            public void Disconnected() {
                //Toast.makeText(mActivity, "Сканер отключен", Toast.LENGTH_SHORT).show();
            }

        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                connectScaner();
            }
        }, Settings.getScannerStartDelayMillisStatic());

        setActionBarTitle();


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
            e.printStackTrace();
        }
    }

    private void setBroadcast() {
        // 设置数据广播
        mReadBroadcast = new ReadBroadcast(mReceiver);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SendConstant.GetDataAction);
        filter.addAction(SendConstant.GetReadDataAction);
        filter.addAction(SendConstant.GetBatteryDataAction);
        registerReceiver(mReadBroadcast, filter);
    }

    @Override
    protected void onStart() {
        // 设置读取数据的广播
        setBroadcast();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mReadBroadcast != null) {
            // 取消广播
            this.unregisterReceiver(mReadBroadcast);
        }
        super.onStop();
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
                showProductsListFragment();
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

        }
        return super.onOptionsItemSelected(item);
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
    public void onCommentChanged(String mComment) {
        docSale.setComment(mComment);
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
    public void onWorkersChanged(int workers, TextInputLayout til) {
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
        docSale.setShippingAddress(address);
    }

    @Override
    public void onDateChanged(Calendar shippingDate, TextView editText) {
        if (Uttils.isShippingDateValid(shippingDate)) {
            docSale.setShippingDate(shippingDate.getTime());
            editText.setError(null);
        } else {
            editText.setError("Проверьте дату!");
        }
    }

    @Override
    public void onTimeFromChanged(Calendar timeFrom, TextView editText) {
        docSale.setShippingTimeFrom(timeFrom.getTime());
        if (Uttils.isShippingTimeValid(docSale.getShippingTimeFrom(), docSale.getShippingTimeTo())) {
            editText.setError(null);
        } else {
            editText.setError("Проверьте время доставки!");
        }
    }

    @Override
    public void onTimeToChanged(Calendar timeTo, TextView editText) {
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
    public void setDiscountCard(DiscountCard card) {
        docSale.setDiscountCard(card);
        showMessage("Скидка будет применена при записи документа");
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

    class QtyMinusDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    deleteCurrentProductRecord();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    }

    private void deleteCurrentProductRecord() {
        deleteRecord(currentRecord);
    }


    private void showProductsListFragment() {
        if (docSale.getPosted()) {
            showMessage("Документ проведен, изменения запрещены.");
            return;
        }
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivityForResult(intent, SHOW_PRODUCTS_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        if (requestCode == SHOW_PRODUCTS_LIST && resultCode == 0) {

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

        if (requestCode == ON_RECORD_SELECTED && resultCode == 0) {

            SaleRecordProduct record = null;
            long id = data.getLongExtra("id", -1);
            double qty = data.getDoubleExtra("qty", -1);
            if (id != -1) {
                try {
                    record = MyHelper.getSaleRecordProductDao().queryForId(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (record != null) {
                    for (SaleRecordProduct rec :
                            docSale.getProducts()) {
                        if (rec.getId() == id) {
                            rec.setQty(qty);
                        }
                    }
                }
            }
        }

        recalc();


        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(1);
            }
        });

        fragmentPagerAdapter.notifyFragmentDataSetChanged(1);
    }


    @Override
    public void plus(SaleRecord record) {
        if (docSale.getPosted()) {
            showMessage("Документ проведен, изменения запрещены.");
            return;
        }
        double q = record.getQty() + 1;
        record.setQty(q);
        recalc();
    }

    @Override
    public void minus(SaleRecord record) {
        if (docSale.getPosted()) {
            showMessage("Документ проведен, изменения запрещены.");
            return;
        }
        double q = record.getQty();
        if (q <= 1) {
            currentRecord = record;
            QtyMinusDialogListener listener = new QtyMinusDialogListener();

            AlertDialog.Builder adb = new AlertDialog.Builder(this)
                    .setTitle(currentRecord.getProduct().getDescription())
                    .setPositiveButton("Да", listener)
                    .setNegativeButton("Нет", listener)
                    .setMessage("Удалить?");
            adb.create().show();
        }
        if (q > 1) {
            q--;
            record.setQty(q);
            recalc();
        }
    }

    @Override
    public void deleteRecord(SaleRecord record) {
        docSale.getProducts().remove(record);
        recalc();
    }

    @Override
    public void onRecordSelected(SaleRecord record) {
        try {
            docSale.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (record.getProduct_Key().equals(Constants.SHIPPING_GUID) ||
                record.getProduct_Key().equals(Constants.UNLOAD_GUID)) {
            return; //для услуг по доставке и разгрузке не редактируем кол-во
        }

        Intent intent = new Intent(this, QtyInputActivity.class);
        intent.putExtra("id", record.getId());
        intent.putExtra("qty", record.getQty());
        startActivityForResult(intent, 300);
    }


    private void setActionBarTitle() {
        if (docSale.getPosted()) {
            myToolbar.setBackgroundColor(Constants.COLORS.DISABLED);
        } else {
            myToolbar.setBackgroundColor(Constants.COLORS.REGULAR_COLOR);
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

    private void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void saveLocal() {

        //проверим некоторые поля на null
        if (docSale.getDiscountCard() == null) {
            docSale.setDiscountCard(DiscountCard.newInstance());
        }
        try {
            docSale.save();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void refreshTotal() {

        String total = "Итого " + Uttils.formatDoubleToMoney(docSale.getTotal()) + " руб, вес "
                + Uttils.formatDoubleToMoney(docSale.getWeight()) + " кг, объем "
                + Uttils.formatDoubleToMoney(docSale.getVolume()) + " м3, "
                + docSale.getProducts().size() + " товаров";

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

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.d(TAG, "onItemClick: DrawerItemClickListener");
            selectItem(position);
        }

    }

    //Это в дравере
    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        String selectedAction = Constants.DOCUMENT_ACTIONS.get(position);
        if (St.getApp().getResources().getString(R.string.ACTION_PRINT).equals(selectedAction)) {
            print();
        }

        if (St.getApp().getResources().getString(R.string.ACTION_CLOSE).equals(selectedAction)) {
            onBackPressed();
        }

        if (St.getApp().getResources().getString(R.string.ACTION_SAVE).equals(selectedAction)) {
            saveLocal();
        }

        if (St.getApp().getResources().getString(R.string.ACTION_SAVE_1C).equals(selectedAction)) {
            saveTo1C(false);
        }

        if (St.getApp().getResources().getString(R.string.ACTION_POST_1C).equals(selectedAction)) {
            saveTo1C(true);
        }
    }

    private void print() {
        showMessage("НЕ РЕАЛИЗОВАНО");
    }

    private void saveTo1C(boolean post) {
        if (docSale.getPosted()) {
            showMessage("Документ проведен, изменения запрещены.");
            return;
        }

        if (docSale.getProducts().size() > 0) {
            saveLocal();
        } else {
            showMessage("В документе нет товаров");
            return;
        }
        if (docSale.getRef_Key().equals(Constants.ZERO_GUID) && post) {
            showMessage("Документ не записан в 1С, проведение возможно только после записи.");
            return;
        }
        if (post) {
            Intent i = new Intent(this, CommunicationWithServer.class);
            i.putExtra("mode", Constants.DATALOADER_MODE.POST_IN_1C.name());
            i.putExtra("id", docSale.getId());
            i.putExtra("receiverTag", mReceiver);
            i.putExtra("from", "DocList");
            startService(i);

        } else {
            Intent i = new Intent(this, CommunicationWithServer.class);
            i.putExtra("mode", Constants.DATALOADER_MODE.SAVE_TO_1C.name());
            i.putExtra("id", docSale.getId());
            i.putExtra("receiverTag", mReceiver);
            i.putExtra("from", "DocList");
            startService(i);
        }
    }

    private void networkLoadMissingObjectFrom1C() {
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra("mode", Constants.DATALOADER_MODE.LOAD_MISSING_FOR_DOCUMENT.name());
        i.putExtra("from", "Document");
        i.putExtra(Constants.REF_KEY_LABEL, getDocRef_Key());
        i.putExtra("receiverTag", mReceiver);
        startService(i);
    }

    @Override
    public void onReceiveResult(int code, Bundle mResult) {
        if (code == Constants.BARCODE_REQUEST_FINISHED) {
            if (mResult.getBoolean("ok")) {
                Intent intent = new Intent(this, ProductInfoActivity.class);
                intent.putExtra(Constants.REF_KEY_LABEL, mResult.getString(Constants.REF_KEY_LABEL));
                startActivityForResult(intent, SHOW_PRODUCTS_LIST);
            } else {
                showMessage(mResult.getString("error"));

            }
        }
        if (code == Constants.SCANNER_EVENT) {
            String barchar = mResult.getString(Constants.SCANNER_DATA_LABEL, "");
            if ((barchar.equals("\n") || barchar.equals("\r")) && barcode.length() > 0) {

                onBarcodeAquired(barcode);
                barcode = "";
            }
            if (!barchar.equals("\n") && !barchar.equals("\r")) {
                barcode += barchar;
            }
        }
        if (code == Constants.LOAD_FINISHED) {
            try {
                Log.d(TAG, "onReceiveResult: колво документов в базе = " + String.valueOf(MyHelper.getDocSaleDao().countOf()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onReceiveResult: SFO COMPLETED");
            boolean ok = mResult.getBoolean("ok");
            if (!ok) {
                if (mResult.containsKey("error")) {
                    String error = mResult.getString("error", "Ошибка не передана сервисом!");
                    showMessage(error);
                }
            } else {
                docRef_Key = mResult.getString(Constants.REF_KEY_LABEL, "");
                //Видимо здесь нужно показывать фрагмент
                loadDocumentFromDB();
                setActionBarTitle();
                fragmentPagerAdapter.notifyFragmentDataSetChanged(1);
                refreshTotal();
            }
        }
    }

    private void onBarcodeAquired(String barcode) {
        barcode = barcode.replaceAll("[^0-9.]", "");
        showMessage(barcode);
        Intent i = new Intent(this, CommunicationWithServer.class);
        i.putExtra("mode", Constants.DATALOADER_MODE.REQUEST_BARCODE.name());
        i.putExtra(Constants.BARCODE_LABEL, barcode);
        i.putExtra("receiverTag", mReceiver);
        i.putExtra("from", "DocList");
        startService(i);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}
