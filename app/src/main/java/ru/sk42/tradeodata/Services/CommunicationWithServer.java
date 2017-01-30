package ru.sk42.tradeodata.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.Currency;
import ru.sk42.tradeodata.Model.Catalogs.Customer;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CharList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ContractsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CurrencyList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CustomersList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.DiscountCardsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.OrganisationsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ProductsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.RoutesList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ShippingRatesList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.StartingPointsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.StoreList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UnitsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UsersList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.VehicleTypesList;
import ru.sk42.tradeodata.Model.Catalogs.Organisation;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.NetworkRequests.BarcodeRequest;
import ru.sk42.tradeodata.NetworkRequests.PostDocument;
import ru.sk42.tradeodata.NetworkRequests.ProductInfoRequest;
import ru.sk42.tradeodata.NetworkRequests.SingleDocRequest;
import ru.sk42.tradeodata.XML.WrapperXML_DocSale;
import ru.sk42.tradeodata.Model.Catalogs.Unit;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.DocSaleList;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.NetworkRequests.CharRequest;
import ru.sk42.tradeodata.NetworkRequests.ContractsRequest;
import ru.sk42.tradeodata.NetworkRequests.CurrencyRequest;
import ru.sk42.tradeodata.NetworkRequests.CustomersRequest;
import ru.sk42.tradeodata.NetworkRequests.DiscountCardsRequest;
import ru.sk42.tradeodata.NetworkRequests.DocsRequest;
import ru.sk42.tradeodata.NetworkRequests.OrganisationsRequest;
import ru.sk42.tradeodata.NetworkRequests.SaveExistingDocument;
import ru.sk42.tradeodata.NetworkRequests.SaveNewDocument;
import ru.sk42.tradeodata.NetworkRequests.ProductsRequest;
import ru.sk42.tradeodata.NetworkRequests.RecordsCountRequest;
import ru.sk42.tradeodata.NetworkRequests.RetroConstants;
import ru.sk42.tradeodata.NetworkRequests.RoutesRequest;
import ru.sk42.tradeodata.NetworkRequests.ShippingRatesRequest;
import ru.sk42.tradeodata.NetworkRequests.StartingPointsRequest;
import ru.sk42.tradeodata.NetworkRequests.StoresRequest;
import ru.sk42.tradeodata.NetworkRequests.UnitsRequest;
import ru.sk42.tradeodata.NetworkRequests.UsersRequest;
import ru.sk42.tradeodata.NetworkRequests.VehicleTypesRequest;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update mIntent actions, extra parameters and static
 * helper methods.
 */
public class CommunicationWithServer extends IntentService {

    NotificationManager manager;
    final String TAG = "Service ***";
    ResultReceiver resultReceiver;
    Intent mIntent;
    Bundle mResultBundle = new Bundle();

    public CommunicationWithServer() {
        super("CommunicationWithServer");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.mIntent = intent;
        String from = intent.getStringExtra("from");
        resultReceiver = intent.getParcelableExtra("receiverTag");
        String mode = intent.getStringExtra("mode");
        Log.d(TAG, "onHandleIntent: FROM = " + from);
        if (mode != null) {
            if (mode.equals(Constants.DATALOADER_MODE.PRELOAD.name())) {
                Preload();
                return;
            }
            if (mode.equals(Constants.DATALOADER_MODE.LOAD_MISSING_FOR_LIST_OF_DOCUMENTS.name())) {
                loadMissingDataForDocumentsList();
                return;
            }
            if (mode.equals(Constants.DATALOADER_MODE.LOAD_MISSING_FOR_DOCUMENT.name())) {
                loadMissingDataForSingleDocument(null);
                return;
            }
            if (mode.equals(Constants.DATALOADER_MODE.REQUEST_DOCUMENTS.name())) {
                loadDocuments();
                return;
            }

            if (mode.equals(Constants.DATALOADER_MODE.REQUEST_SINGLE_DOCUMENT.name())) {
                loadSingleDocumentFromServer();
                return;
            }

            if (mode.equals(Constants.DATALOADER_MODE.SAVE_TO_1C.name())) {
                saveDocumentTo1C();
                return;
            }

            if (mode.equals(Constants.DATALOADER_MODE.POST_IN_1C.name())) {
                postDocumentIn1C();
                return;
            }

            if (mode.equals(Constants.DATALOADER_MODE.REQUEST_BARCODE.name())) {
                requestBarcode();
                return;
            }

            if(mode.equals(Constants.DATALOADER_MODE.REQUEST_PRODUCT_INFO.name())){
                requestProductInfo();
                return;
            }

        }


    }

    private void requestProductInfo() {
        String guid = product.getRef_Key();
        ProductInfoRequest request = ServiceGenerator.createService(ProductInfoRequest.class);
        Call<ProductInfo> call = request.call(guid);
        call.enqueue(new Callback<ProductInfo>() {
            @Override
            public void onResponse(Call<ProductInfo> call, Response<ProductInfo> response) {
                ProductInfo productInfo = response.body();
                if (productInfo != null) {
                    try {
                        MyHelper.getProductInfoDao().delete(MyHelper.getProductInfoDao().queryForAll());
                        MyHelper.getStockDao().delete(MyHelper.getStockDao().queryForAll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    productInfo.save();
                    DataLoader.LoadMissingDataForObject(productInfo, ProductInfo.class);
                    mListener.onRequestSuccess(productInfo);
                }
            }

            @Override
            public void onFailure(Call<ProductInfo> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });

    }

    private void requestBarcode() {

        final RequestResult result = new RequestResult();
        final String barcode = mIntent.getStringExtra(Constants.BARCODE_LABEL);
        final BarcodeRequest request = ServiceGenerator.createService(BarcodeRequest.class);
        Call<ProductInfo> call = request.call(barcode);
        call.enqueue(new Callback<ProductInfo>() {
            @Override
            public void onResponse(Call<ProductInfo> call, Response<ProductInfo> response) {
                ProductInfo productInfo = response.body();
                if (productInfo != null) {
                    try {
                        MyHelper.getProductInfoDao().delete(MyHelper.getProductInfoDao().queryForAll());
                        MyHelper.getStockDao().delete(MyHelper.getStockDao().queryForAll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    productInfo.save();
                    result.error = "По штрихкоду " + barcode + " найден товар " + productInfo.getDescription();
                    result.ref_Key = productInfo.getRef_Key();
                    result.ok = true;
                    DataLoader.LoadMissingDataForObject(productInfo, ProductInfo.class);
                    onProductInfoRequest(result);

                }
                else
                {
                    result.error = "Штрихкод не найден: " + barcode;
                    onProductInfoRequest(result);
                }
            }

            @Override
            public void onFailure(Call<ProductInfo> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });

    }

    private class RequestResult {
        public RequestResult() {
            ok = false;
            ref_Key = "";
            error = "";
        }

        boolean ok;
        String ref_Key;
        String error;
        int code;
    }

    private void Preload() {

        sendFeedback("Начали загрузку с сервера 1С");

        sendFeedback("loadProducts");
        loadProducts();

        sendFeedback("loadCustomers");
        loadCustomers();

        sendFeedback("loadVehicleTypes");
        loadVehicleTypes();

        sendFeedback("loadStores");
        loadStores();

        sendFeedback("loadDiscountCards");
        //loadDiscountCards();

        sendFeedback("loadCharacts");
        //loadCharacts();

        sendFeedback("loadUnits");
        //loadUnits();

        sendFeedback("loadContracts");
        loadContracts();

        sendFeedback("loadUsers");
        loadUsers();

        sendFeedback("loadRoutes");
        loadRoutes();

        sendFeedback("loadStartingPoints");
        loadStartingPoints();

        sendFeedback("loadCurrency");
        loadCurrency();

        sendFeedback("loadOrganisations");
        loadOrganisations();

        sendFeedback("loadShippingRates");
        loadShippingRates();

        sendServiceFinished("Загрузка справочников завершена");
    }

    private void loadShippingRates() {

        long count = 0;

        try {
            count = MyHelper.getShippingRouteDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count > 0) {
            return;
        }

        sendFeedback("Начата загрузка тарифов на доставку");

        ShippingRatesRequest request = ServiceGenerator.createService(ShippingRatesRequest.class);
        Call<ShippingRatesList> call = request.call(RetroConstants.getMap(""));
        try {

            Response<ShippingRatesList> response = call.execute();
            ShippingRatesList list = response.body();
            sendFeedback("Сохраняем " + String.valueOf(list.getValues().size()) + " тарифов доставки");
            TableUtils.dropTable(MyHelper.getShippingRouteDao(), false);
            TableUtils.createTable(MyHelper.getShippingRouteDao());


            list.save();
            sendFeedback("Тарифы загружены");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCustomers() {
        Customer object = new Customer();
        if (!isLoadRequired(object)) return;
        CustomersRequest request = ServiceGenerator.createService(CustomersRequest.class);
        Call<CustomersList> call = request.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {

            Response<CustomersList> response = call.execute();
            CustomersList list = response.body();
            MyHelper.getCustomerDao().delete(MyHelper.getCustomerDao().queryForAll());
            list.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCurrency() {
        Currency object = new Currency();
        if (!isLoadRequired(object)) return;

        CurrencyRequest request = ServiceGenerator.createService(CurrencyRequest.class);
        Call<CurrencyList> call = request.call(RetroConstants.getMap(""));
        try {

            Response<CurrencyList> response = call.execute();
            CurrencyList list = response.body();
            MyHelper.getCurrencyDao().delete(MyHelper.getCurrencyDao().queryForAll());
            list.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOrganisations() {
        Organisation object = new Organisation();

        if (!isLoadRequired(object)) return;

        OrganisationsRequest request = ServiceGenerator.createService(OrganisationsRequest.class);
        Call<OrganisationsList> call = request.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {

            Response<OrganisationsList> response = call.execute();
            OrganisationsList list = response.body();
            MyHelper.getOrganisationDao().delete(MyHelper.getOrganisationDao().queryForAll());
            list.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadVehicleTypes() {
        VehicleType object = new VehicleType();

        if (!isLoadRequired(object)) return;
        VehicleTypesRequest request = ServiceGenerator.createService(VehicleTypesRequest.class);
        Call<VehicleTypesList> call = request.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {

            Response<VehicleTypesList> response = call.execute();
            VehicleTypesList list = response.body();
            MyHelper.getVehicleTypesDao().delete(MyHelper.getVehicleTypesDao().queryForAll());
            list.save();
            VehicleType.createStub();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStores() {
        Store object = new Store();

        if (!isLoadRequired(object)) return;
        StoresRequest request = ServiceGenerator.createService(StoresRequest.class);
        Call<StoreList> call = request.call(RetroConstants.getMap(RetroConstants.FILTERS.STORES));
        try {
            Response<StoreList> response = call.execute();
            StoreList list = response.body();
            MyHelper.getStoreDao().delete(MyHelper.getStoreDao().queryForAll());
            list.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUnits() {
        Unit object = new Unit();

        if (!isLoadRequired(object)) return;
        UnitsRequest request = ServiceGenerator.createService(UnitsRequest.class);
        Call<UnitsList> call = request.call(RetroConstants.getMap(""));
        try {
            Response<UnitsList> response = call.execute();
            UnitsList list = response.body();
            TableUtils.dropTable(MyHelper.getUnitDao(), false);
            TableUtils.createTable(MyHelper.getUnitDao());
            sendFeedback("Сохраняем " + String.valueOf(list.getValues().size()) + " единиц измерения");
            list.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContracts() {
//        Contract object = new Contract();

//        if (!isLoadRequired(object)) return;

        ContractsRequest request = ServiceGenerator.createService(ContractsRequest.class);
        Call<ContractsList> call = request.call(RetroConstants.getMap("Ref_Key eq guid'" + Constants.CONTRACT_GUID + "'"));
        try {
            Response<ContractsList> response = call.execute();
            ContractsList list = response.body();
//            TableUtils.dropTable(MyHelper.getContractDao(), false);
//            TableUtils.createTable(MyHelper.getContractDao());
            list.getValues().iterator().next().save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDiscountCards() {

        DiscountCard object = new DiscountCard();

        if (!isLoadRequired(object)) return;


        DiscountCardsRequest request = ServiceGenerator.createService(DiscountCardsRequest.class);
        Call<DiscountCardsList> call = request.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {
            Response<DiscountCardsList> response = call.execute();
            DiscountCardsList list = response.body();
            TableUtils.dropTable(MyHelper.getDiscountCardDao(), false);
            TableUtils.createTable(MyHelper.getDiscountCardDao());
            sendFeedback("Сохраняем " + String.valueOf(list.getValues().size()) + " дисконтных карт");
            list.save();
            DiscountCard card = DiscountCard.newInstance();
            card.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        Product shipping = Product.getObject(Product.class, Constants.SHIPPING_GUID);
        Product unload = Product.getObject(Product.class, Constants.UNLOAD_GUID);
        if (shipping == null || unload == null) {

            ProductsRequest request = ServiceGenerator.createService(ProductsRequest.class);
            Call<ProductsList> call = request.call(RetroConstants.getMap("Ref_Key eq guid'" + Constants.SHIPPING_GUID + "'" + " or Ref_Key eq guid'" + Constants.UNLOAD_GUID + "'"));
            try {
                Response<ProductsList> response = call.execute();
                ProductsList list = response.body();
                list.save();
                Constants.SHIPPING_SERVICE = Product.getObject(Product.class, Constants.SHIPPING_GUID);
                Constants.UNLOAD_SERVICE = Product.getObject(Product.class, Constants.UNLOAD_GUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Constants.SHIPPING_SERVICE = shipping;
            Constants.UNLOAD_SERVICE = unload;
        }
    }

    private void loadCharacts() {
        Charact object = new Charact();

        if (!isLoadRequired(object)) return;

        CharRequest request1 = ServiceGenerator.createService(CharRequest.class);
        Call<CharList> call1 = request1.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {
            Response<CharList> response = call1.execute();
            CharList list = response.body();
            TableUtils.dropTable(MyHelper.getCharactDao(), false);
            TableUtils.createTable(MyHelper.getCharactDao());
            list.save();
            Charact.createStub();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        User object = new User();

        if (!isLoadRequired(object)) return;


        UsersRequest request1 = ServiceGenerator.createService(UsersRequest.class);
        Call<UsersList> call1 = request1.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {
            Response<UsersList> response = call1.execute();
            UsersList list = response.body();
            MyHelper.getUserDao().delete(MyHelper.getUserDao().queryForAll());
            list.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRoutes() {
        Route object = new Route();

        if (!isLoadRequired(object)) return;


        RoutesRequest request1 = ServiceGenerator.createService(RoutesRequest.class);
        Call<RoutesList> call1 = request1.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {
            Response<RoutesList> response = call1.execute();
            RoutesList list = response.body();
            MyHelper.getRouteDao().delete(MyHelper.getRouteDao().queryForAll());
            sendFeedback("Сохраняем " + String.valueOf(list.getValues().size()) + " маршрутов");
            list.save();
            Route.createStub();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStartingPoints() {
        StartingPoint object = new StartingPoint();

        if (!isLoadRequired(object)) return;


        StartingPointsRequest request1 = ServiceGenerator.createService(StartingPointsRequest.class);
        Call<StartingPointsList> call1 = request1.call(RetroConstants.getMap(object.getRetroFilterString()));
        try {
            Response<StartingPointsList> response = call1.execute();
            StartingPointsList list = response.body();
            MyHelper.getStartingPointDao().delete(MyHelper.getStartingPointDao().queryForAll());
            list.save();
            StartingPoint.createStub();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getRecordsCountFromServer(CDO object) {
        RecordsCountRequest request = ServiceGenerator.createService(RecordsCountRequest.class);
        String filter = object.getRetroFilterString();
        if (filter == null || filter.isEmpty()) {
            filter = "";
        } else {
            filter = "?$filter=" + filter;
        }

        Call<Integer> call = request.call(Settings.getInfoBaseNameStatic() + "/odata/standard.odata/" + object.getMaintainedTableName() + "/$count" + filter);
        Integer count = 0;
        try {
            Response<Integer> response = call.execute();
            count = response.body();
            return count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private void loadMissingDataForDocumentsList() {
        DataLoader.LoadMissingDataForObject(null, DocSaleList.class);
        for (DocSale docSale :
                DocSaleList.getList().getValues()) {
            docSale.setForeignObjects();
        }

        sendServiceFinished("Загрузка объектов по ссылкам для списка завершена");

    }

    private void loadMissingDataForSingleDocument(String ref_Key) {
        if (ref_Key == null) {
            ref_Key = mIntent.getStringExtra(Constants.REF_KEY_LABEL);
        }
        mResultBundle.putString(Constants.REF_KEY_LABEL, ref_Key);
        DocSale doc = DocSale.getDocument(ref_Key);
        DataLoader.LoadMissingDataForObject(doc, DocSale.class);
        doc.setForeignObjects();

        sendServiceFinished("Загрузка объектов по ссылкам для документа завершена");

    }

    private void loadDocuments() {

        Calendar d1 = GregorianCalendar.getInstance();
        d1.setTimeInMillis(mIntent.getLongExtra("StartDate", 0));
        Calendar d2 = GregorianCalendar.getInstance();
        d2.setTimeInMillis(d1.getTimeInMillis());
        d2.add(Calendar.DATE, 1);

        String df1 = Uttils.DATE_FORMAT_1C.format(d1.getTime());
        String df2 = Uttils.DATE_FORMAT_1C.format(d2.getTime());

        String filter = "Date gt datetime'" + df1 + "' and Date lt datetime'" + df2 + "'" + " and Контрагент_Key eq guid'" + Constants.CUSTOMER_GUID + "'" + " and Ответственный_Key eq guid'" + Settings.getCurrentUserStatic().getRef_Key() + "'";

        sendFeedback("Загрузка документов на " + Uttils.DATE_FORMATTER.format(d1.getTime()));

        DocsRequest request = ServiceGenerator.createService(DocsRequest.class);
        Call<DocSaleList> callDocuments = request.call(RetroConstants.getMap(filter));
        callDocuments.enqueue(new Callback<DocSaleList>() {
            @Override
            public void onResponse(Call<DocSaleList> call, Response<DocSaleList> response) {
                DocSaleList list = response.body();
                if (list == null) {
                    Log.wtf(TAG, "onResponse: DocSaleList is null", new Exception());
                    mResultBundle.putString("error", "Произошла ошибка передачи данных");
                    sendServiceFinished("Произошла ошибка передачи данных");
                    return;
                }

                MyHelper.getInstance().deleteDocSaleList();

                try {
                    list.save();
                } catch (Exception e) {
                    Log.w(TAG, "list.save(): " + e.toString());
                    mResultBundle.putString("error", "Произошла ошибка передачи данных");
                    sendServiceFinished("Произошла ошибка сохранения данных");
                    return;
                }

                loadMissingDataForDocumentsList();

                sendServiceFinished("Загрузка списка документов завершена");

            }

            @Override
            public void onFailure(Call<DocSaleList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                mResultBundle.putString("error", "Произошла ошибка передачи данных");
                sendServiceFinished("Произошла ошибка запроса данных");
            }
        });


    }

    private void loadSingleDocumentFromServer() {

        try {
            Log.d(TAG, "loadSingleDocumentFromServer: колво документов в базе = " + String.valueOf(MyHelper.getDocSaleDao().countOf()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String ref_Key = mIntent.getStringExtra(Constants.REF_KEY_LABEL);
        final long id = mIntent.getLongExtra("id", -1);
        String filter = "Ref_Key eq guid'" + ref_Key + "'";

        sendFeedback("Загрузка документа с сервера");

        SingleDocRequest request = ServiceGenerator.createService(SingleDocRequest.class);
        Call<DocSaleList> callDocuments = request.call(RetroConstants.getMap(filter));
        callDocuments.enqueue(new Callback<DocSaleList>() {
            @Override
            public void onResponse(Call<DocSaleList> call, Response<DocSaleList> response) {
                DocSaleList list = response.body();
                if (list == null) {
                    Log.wtf(TAG, "onResponse: DocSaleList is null", new Exception());
                    return;
                }

                if (list.size() == 1) {
                    DocSale docSale = list.getValues().iterator().next();
                    if (id != -1) {
                        docSale.setId(id);
                    }
                    try {
                        docSale.save();
                    } catch (Exception e) {
                        Log.w(TAG, "docSale.save(): " + e.toString());
                    }

                }

                loadMissingDataForSingleDocument(ref_Key);

            }

            @Override
            public void onFailure(Call<DocSaleList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });

    }

    void sendFeedback(String message) {
        Bundle b = new Bundle();
        b.putString("Message", message);
        resultReceiver.send(Constants.FEEDBACK, b);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void sendServiceFinished(String msg) {
        //sendNotification(msg);
        sendFeedback(msg);
        resultReceiver.send(Constants.LOAD_FINISHED, mResultBundle);
    }

    private boolean isLoadRequired(CDO object) {
        long serverRecordsCount = getRecordsCountFromServer(object);
        long localRecordsCount = 0;
        try {
            localRecordsCount = object.getDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (object instanceof Charact)
            localRecordsCount--; //потому что одну характеристику заглушку мы всегда создаем локально
        if (object instanceof VehicleType)
            localRecordsCount--;
        if (object instanceof Route)
            localRecordsCount--;
        if (object instanceof StartingPoint)
            localRecordsCount--;
        if (object instanceof DiscountCard)
            localRecordsCount--; //потому что дефолтную пустую карту создаем автоматически

        String msg = object.getMaintainedTableName() + " " + String.valueOf(localRecordsCount) + "/" + String.valueOf(serverRecordsCount) + " записей";
        Log.d(TAG, "isLoadRequired: класс " + msg);

        if (serverRecordsCount != localRecordsCount) {
            sendFeedback(msg);
            return true;
        } else {
            sendFeedback(msg);
            return false;
        }

    }

    void sendNotification(String msg) {

        Notification noti = new Notification.Builder(getApplicationContext())
                .setContentTitle("TradeOdata")
                .setContentText(msg)
                .setSmallIcon(R.drawable.notification_icon)
                .build();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, noti);
    }

    //вернет ref_Key, если
    private void saveDocumentTo1C() {

        final RequestResult result = new RequestResult();
        long id = mIntent.getLongExtra("id", -1);
        final DocSale docSale = DocSale.getByID(id); // пока сохраняем новый док в базу с нулевым гидом

        String xml = WrapperXML_DocSale.writeDocSaleToXMLString(docSale);

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), xml);
        if (docSale.getRef_Key().equals(Constants.ZERO_GUID)) {
            //это новый документ, гуида еще нет
            //вызываем метод Пост
            SaveNewDocument mPostRequest = ServiceGenerator.createXMLService(SaveNewDocument.class);
            Call<ResponseBody> call = mPostRequest.call(body);
            call.enqueue(new Callback<ResponseBody>() {
                             @Override
                             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                 result.code = response.raw().code();
                                 if (result.code == 200 || result.code == 201) {
                                     String location = response.headers().get("location");
                                     if (location == null) {
                                         result.ref_Key = Constants.ZERO_GUID;
                                         result.error = "location not found!";
                                     }
                                     int i = location.indexOf("guid'");
                                     if (i > 0) {
                                         result.ref_Key = location.substring(i + 5, location.length() - 2);
                                         result.ok = true;
                                         onSaveComplete(result);
                                     }
                                 } else {
                                     result.error = response.raw().message();
                                     onSaveComplete(result);
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 result.error = t.toString();
                                 onSaveComplete(result);
                             }
                         }
            );
        }

        if (!docSale.getRef_Key().equals(Constants.ZERO_GUID)) {
            //вызываем метод patch такой документ уже есть в базе 1с
            SaveExistingDocument mPatchRequest = ServiceGenerator.createXMLService(SaveExistingDocument.class);
            Call<ResponseBody> call = mPatchRequest.call(docSale.getRef_Key(), body);
            call.enqueue(new Callback<ResponseBody>() {
                             @Override
                             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                 result.code = response.raw().code();
                                 if (result.code == 200) {
                                     result.ref_Key = docSale.getRef_Key();
                                     result.error = "документ сохранен";
                                     result.ok = true;
                                     onSaveComplete(result);
                                 } else {
                                     //Разбираемся что за хня
                                     Log.d(TAG, "onResponse: хуйня какая-то опять...");
                                     onSaveComplete(result);
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 result.error = t.toString();
                                 onSaveComplete(result);
                             }
                         }
            );
        }
    }

    private void postDocumentIn1C() {

        final RequestResult result = new RequestResult();
        long id = mIntent.getLongExtra("id", -1);
        final DocSale docSale = DocSale.getByID(id); // пока сохраняем новый док в базу с нулевым гидом

        //вызываем метод patch такой документ уже есть в базе 1с
        PostDocument mPostRequest = ServiceGenerator.createXMLService(PostDocument.class);
        Call<ResponseBody> call = mPostRequest.call(docSale.getRef_Key());
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             result.code = response.raw().code();
                             if (result.code == 200) {
                                 result.ref_Key = docSale.getRef_Key();
                                 result.error = "документ сохранен";
                                 result.ok = true;
                                 onPostComplete(result);
                             } else {
                                 //Разбираемся что за хня
                                 Log.d(TAG, "onResponse: хуйня какая-то опять...");
                                 onPostComplete(result);
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {
                             result.error = t.toString();
                             onPostComplete(result);
                         }
                     }
        );
    }

    private void onSaveComplete(RequestResult result) {
        String msg;
        if (result.ok) {
            msg = "Документ успешно сохранен";
        } else {
            msg = "Ошибка сохранения: " + result.error;
        }

        mResultBundle = new Bundle();
        mResultBundle.putBoolean("ok", result.ok);
        mResultBundle.putString("error", msg);
        mResultBundle.putInt("code", result.code);
        mResultBundle.putString(Constants.REF_KEY_LABEL, result.ref_Key);
        if (result.ok) {
            mIntent.putExtra(Constants.REF_KEY_LABEL, result.ref_Key);
            try {
                Log.d(TAG, "onSaveComplete: колво документов в базе = " + String.valueOf(MyHelper.getDocSaleDao().countOf()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadSingleDocumentFromServer();
        } else {
            sendServiceFinished(msg);
        }
    }
    private void onPostComplete(RequestResult result) {
        String msg;
        if (result.ok) {
            msg = "Документ проведен!";
        } else {
            msg = "Ошибка сохранения: " + result.error;
        }

        mResultBundle = new Bundle();
        mResultBundle.putBoolean("ok", result.ok);
        mResultBundle.putString("error", msg);
        mResultBundle.putInt("code", result.code);
        mResultBundle.putString(Constants.REF_KEY_LABEL, result.ref_Key);
        if (result.ok) {
            mIntent.putExtra(Constants.REF_KEY_LABEL, result.ref_Key);
            loadSingleDocumentFromServer();
        } else {
            sendServiceFinished(msg);
        }
    }

    private void onProductInfoRequest(RequestResult result) {
        String msg;
        if (result.ok) {
            msg = "Данные о товаре получены";
        } else {
            msg = "Ошибка: " + result.error;
        }

        mResultBundle = new Bundle();
        mResultBundle.putBoolean("ok", result.ok);
        mResultBundle.putString("error", msg);
        mResultBundle.putString(Constants.REF_KEY_LABEL, result.ref_Key);
        sendBarcodeRequestFinished();
    }

    private void sendBarcodeRequestFinished() {
        resultReceiver.send(Constants.BARCODE_REQUEST_FINISHED, mResultBundle);
    }


}
