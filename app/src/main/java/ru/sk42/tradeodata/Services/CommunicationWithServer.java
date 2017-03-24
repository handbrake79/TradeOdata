package ru.sk42.tradeodata.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

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
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ImageList;
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
import ru.sk42.tradeodata.Model.Catalogs.ImageProduct;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.InformationRegisters.ShippingRate;
import ru.sk42.tradeodata.Model.PrintResult;
import ru.sk42.tradeodata.Model.Printer;
import ru.sk42.tradeodata.Model.Printers;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.Activities.Settings.Settings;
import ru.sk42.tradeodata.NetworkRequests.BarcodeRequest;
import ru.sk42.tradeodata.NetworkRequests.PostDocument;
import ru.sk42.tradeodata.NetworkRequests.PrintRequest;
import ru.sk42.tradeodata.NetworkRequests.PrintersRequest;
import ru.sk42.tradeodata.NetworkRequests.ImageRequest;
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
 * TODO: Customize class - initView mIntent actions, extra parameters and static
 * helper methods.
 */
public class CommunicationWithServer extends IntentService {

    NotificationManager manager;
    final String TAG = "Service ***";
    ResultReceiver mResultReceiver;
    Intent mIntent;

    public CommunicationWithServer() {
        super("CommunicationWithServer");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent currentIntent) {

        this.mIntent = currentIntent;
        mResultReceiver = currentIntent.getParcelableExtra("receiverTag");
        int op = currentIntent.getIntExtra(Constants.MODE_LABEL, -1);
        Constants.REQUESTS mRequestedOperation = Constants.REQUESTS.values()[op];
        switch (mRequestedOperation) {
            case PRELOAD:
                preload();
                break;
            case LOAD_MISSING_FOR_DOCUMENT:
                loadMissingForDocument();
                break;
            case LOAD_DOCUMENTS:
                loadDocuments();
                break;
            case SAVE_DOCUMENT:
                saveDocumentTo1C();
                break;
            case POST_DOCUMENT:
                postDocumentIn1C();
                break;
            case BARCODE:
                barcodeRequest();
                break;
            case PRODUCT_INFO:
                productRequest();
                break;
            case PRINT_DOCUMENT:
                printDocument();
                break;
            case LOAD_IMAGE:
                loadImage();
                break;
            case DISCOUNT_CARD:
                findDiscountCard();
                break;
        }

    }

    private void printDocument() {
        final String docNumber = mIntent.getStringExtra(Constants.DOC_NUMBER);
        final String printerName = mIntent.getStringExtra(Constants.PRINTER_NAME);

        final RequestResult result = new RequestResult(Constants.REQUESTS.PRINT_DOCUMENT);

        PrintRequest request = ServiceGenerator.createService(PrintRequest.class);
        Call<PrintResult> call = request.call(docNumber, printerName);
        call.enqueue(
                new Callback<PrintResult>() {
                    @Override
                    public void onResponse(Call<PrintResult> call, Response<PrintResult> response) {
                        PrintResult printResult = response.body();
                        result.resultCode = response.raw().code();
                        result.message = printResult.getResult();
                        if (result.resultCode == 200) {
                            result.success = true;
                        } else {
                            //Разбираемся что за хня
                            Log.d(TAG, "onResponse: хуйня какая-то опять...");
                            result.success = false;
                        }
                        sendRequestFinished(result);
                    }

                    @Override
                    public void onFailure(Call<PrintResult> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.toString());
                    }
                }
        );
    }


    private boolean cachedProductInfoIsValid(ProductInfo productInfo, RequestResult result) {

        if (productInfo != null) {
            Date now = GregorianCalendar.getInstance().getTime();
            long timeDiff = (now.getTime() - productInfo.getRequestDate().getTime()) / 1000;
            if (timeDiff < Constants.PRODUCT_INFO_EXPIRATION_TIME_SECONDS) {
                result.message = "По GUID " + productInfo.getRef_Key() + " найден товар " + productInfo.getDescription();
                result.ref_Key = productInfo.getRef_Key();
                result.success = true;
                DataLoader.LoadMissingDataForObject(productInfo, ProductInfo.class);
                sendRequestFinished(result);

                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private void productRequest() {
        final String ref_Key = mIntent.getStringExtra(Constants.REF_KEY_LABEL);
        final RequestResult result = new RequestResult(Constants.REQUESTS.PRODUCT_INFO);

        ProductInfo productInfo = ProductInfo.getObject(ProductInfo.class, ref_Key);
        if (cachedProductInfoIsValid(productInfo, result)) {
            return;
        }
        ProductInfoRequest request = ServiceGenerator.createService(ProductInfoRequest.class);
        if (request == null) {
            result.message = "Ошибка при обращении к серверу, попробуйте позже";
            result.ref_Key = ref_Key;
            result.success = false;
            result.resultCode = -1;
        }
        Call<ProductInfo> call = request.call(ref_Key);
        call.enqueue(new Callback<ProductInfo>() {
            @Override
            public void onResponse(Call<ProductInfo> call, Response<ProductInfo> response) {
                ProductInfo productInfo = response.body();
                if (productInfo != null) {
                    productInfo.save();
                    result.message = "По GUID " + ref_Key + " найден товар " + productInfo.getDescription();
                    result.ref_Key = productInfo.getRef_Key();
                    result.success = true;
                    DataLoader.LoadMissingDataForObject(productInfo, ProductInfo.class);
                    sendRequestFinished(result);
                }
            }

            @Override
            public void onFailure(Call<ProductInfo> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });

    }

    private void barcodeRequest() {


        final RequestResult result = new RequestResult(Constants.REQUESTS.BARCODE);
        final String barcode = mIntent.getStringExtra(Constants.BARCODE_LABEL);
        ProductInfo productInfo = ProductInfo.getByBarcode(barcode);
        if (cachedProductInfoIsValid(productInfo, result)) {
            return;
        }
        final BarcodeRequest request = ServiceGenerator.createService(BarcodeRequest.class);
        Call<ProductInfo> call = request.call(barcode);
        call.enqueue(new Callback<ProductInfo>() {
            @Override
            public void onResponse(Call<ProductInfo> call, Response<ProductInfo> response) {
                result.resultCode = response.code();
                ProductInfo productInfo = response.body();
                if (productInfo != null) {
                    productInfo.setBarcode(barcode);
                    productInfo.save();
                    result.message = "По штрихкоду " + barcode + " найден товар " + productInfo.getDescription();
                    result.ref_Key = productInfo.getRef_Key();
                    result.success = true;
                    DataLoader.LoadMissingDataForObject(productInfo, ProductInfo.class);

                } else {
                    if (response.code() == 404) {
                        result.message = "По штрихкоду " + barcode + " товар не найден";
                    } else {
                        result.message = "Неизвестная ошибка";
                    }
                }
                sendRequestFinished(result);
            }

            @Override
            public void onFailure(Call<ProductInfo> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });

    }

    private class RequestResult {
        boolean success;
        String ref_Key;
        String message;
        int resultCode;
        Constants.REQUESTS requestedOperation;

        public RequestResult(Constants.REQUESTS mRequested) {
            requestedOperation = mRequested;
            success = false;
            ref_Key = "";
            message = "";
        }
    }

    private void preload() {


        sendFeedback("Получаем список подключенных принтеров");
        getPrinters();

        sendFeedback("Загрузка контрагентов");
        loadCustomers();

        sendFeedback("Загрузка типов ТС");
        loadVehicleTypes();

        sendFeedback("Загрузка складов");
        loadStores();

        loadProducts();
        //sendFeedback("loadDiscountCards");
        //loadDiscountCards();

        //sendFeedback("loadCharacts");
        //loadCharacts();

        //sendFeedback("loadUnits");
        //loadUnits();

        sendFeedback("Загрузка договоров");
        loadContracts();

        sendFeedback("Загрузка пользователей");
        loadUsers();

        sendFeedback("Загрузка маршрутов");
        loadRoutes();

        sendFeedback("Загрузка точет начала маршрута");
        loadStartingPoints();

        sendFeedback("Загрузка валют");
        loadCurrency();

        sendFeedback("Загрузка организаций");
        loadOrganisations();

        sendFeedback("Загрузка тарифов");
        loadShippingRates();

        RequestResult result = new RequestResult(Constants.REQUESTS.PRELOAD);
        result.message = "Загрузка завершена";
        result.success = true;
        sendRequestFinished(result);
    }

    private void getPrinters() {
        PrintersRequest request = ServiceGenerator.createService(PrintersRequest.class);
        Call<Printers> call = request.call();
        try {
            Response<Printers> response = call.execute();
            List<String> printers = response.body().getPrinters();
            MyHelper.getPrinterDao().delete(MyHelper.getPrinterDao().queryForAll());
            for (String name : printers
                    ) {
                Printer mPrinter = new Printer();
                mPrinter.setPrinterName(name);
                MyHelper.getInstance().getDao(Printer.class).createOrUpdate(mPrinter);
            }
            Log.d(TAG, "getPrinters: " + response.toString());
        } catch (Exception e) {
            Log.d(TAG, "getPrinters: " + e.toString());
        }
    }

    private boolean needToLoadShippingRates() {
        try {
            ShippingRate rate = MyHelper.getShippingRouteDao().queryBuilder().queryForFirst();
            if (rate == null) {
                return true;
            }

            Calendar c = Uttils.getEndOfYesterday();
            Date date = rate.getDate();

            //тариф доставки загружали вчера или еще раньше, нужно обновить
//тариф свежий, можно использовать из базы
            return date != null && c.after(rate.getDate());
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }

    }

    private void loadShippingRates() {

        if (!needToLoadShippingRates()) {
            sendFeedback("Тарифы не обновлялись");
            return;
        }
        sendFeedback("Начата загрузка тарифов на доставку");

        ShippingRatesRequest request = ServiceGenerator.createService(ShippingRatesRequest.class);
        Call<ShippingRatesList> call = null;
        try {
            List<VehicleType> list = MyHelper.getVehicleTypesDao().queryForEq("enabled", true);
            String eq = "ТипТС_Key eq guid'";
            Iterator<VehicleType> it = list.iterator();
            String condition = "";
            int i = 0;
            while (it.hasNext()) {
                VehicleType v = it.next();
                if (i > 0) {
                    condition += " or ";
                }
                condition += eq + v.getRef_Key() + "'";
                i++;
            }
            call = request.call(RetroConstants.getMapWithCondition(condition));
        } catch (SQLException e) {
            call = request.call(RetroConstants.getMap(""));
        }

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
        if (!isLoadRequired(object)) {
            return;
        }
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
            Call<ProductsList> call = request.call(RetroConstants.getMapWithFieldRestriction("Ref_Key eq guid'"
                            + Constants.SHIPPING_GUID + "'"
                            + " or Ref_Key eq guid'"
                            + Constants.UNLOAD_GUID + "'",
                    RetroConstants.productFieldsList));
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

    @Nullable
    private Integer getRecordsCountFromServer(CDO object) {
        RecordsCountRequest request = ServiceGenerator.createService(RecordsCountRequest.class);
        String filter = object.getRetroFilterString();
        if (filter == null || filter.isEmpty()) {
            filter = "";
        } else {
            filter = "?$filter=" + filter;
        }

        Call<Integer> call = request.call(Settings.getServerAddressStatic() + "/tradeodata/odata/standard.odata/" + object.getMaintainedTableName() + "/$count" + filter);
        if (call == null) {
            return null;
        }
        Integer count = 0;
        try {
            Response<Integer> response = call.execute();
            count = response.body();
            if (count == null) {
                return 0;
            } else {
                return count;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private void fillResultBundle(RequestResult result, Bundle mResultBundle) {
        mResultBundle.putString(Constants.REF_KEY_LABEL, result.ref_Key);
        mResultBundle.putBoolean(Constants.OPERATION_SUCCESS_LABEL, result.success);
        mResultBundle.putString(Constants.MESSAGE_LABEL, result.message);
        mResultBundle.putInt(Constants.RESULT_CODE_LABEL, result.resultCode);
    }

    private void loadMissingForDocument() {

        String ref_Key = mIntent.getStringExtra(Constants.REF_KEY_LABEL);
        Bundle mResultBundle = new Bundle();

        DocSale doc = DocSale.getDocument(ref_Key);
        DataLoader.LoadMissingDataForObject(doc, DocSale.class);
        doc.setForeignObjects();

        RequestResult result = new RequestResult(Constants.REQUESTS.LOAD_MISSING_FOR_DOCUMENT);
        result.ref_Key = ref_Key;
        result.success = true;
        sendRequestFinished(result);

    }

    private void loadDocuments() {

        final RequestResult result = new RequestResult(Constants.REQUESTS.LOAD_DOCUMENTS);

        Calendar d1 = GregorianCalendar.getInstance();
        d1.setTimeInMillis(mIntent.getLongExtra("StartDate", 0));
        Calendar d2 = GregorianCalendar.getInstance();
        d2.setTimeInMillis(d1.getTimeInMillis());
        d2.add(Calendar.DATE, 1);

        String df1 = Uttils.DATE_FORMAT_1C.format(d1.getTime());
        String df2 = Uttils.DATE_FORMAT_1C.format(d2.getTime());

        String filter = "Date gt datetime'" + df1 + "' and Date lt datetime'" + df2 + "'" + " and Контрагент_Key eq guid'"
                + Constants.CUSTOMER_GUID + "'" + " and Ответственный_Key eq guid'" + Settings.getCurrentUserStatic().getRef_Key() + "'";

        sendFeedback("Загрузка документов на " + Uttils.DATE_FORMATTER.format(d1.getTime()));

        final DocsRequest request = ServiceGenerator.createService(DocsRequest.class);
        Call<DocSaleList> callDocuments = request.call(RetroConstants.getMap(filter));
        callDocuments.enqueue(new Callback<DocSaleList>() {
            @Override
            public void onResponse(Call<DocSaleList> call, Response<DocSaleList> response) {
                DocSaleList list = response.body();
                result.message = "Произошла ошибка передачи данных";
                if (list == null) {
                    Log.wtf(TAG, "onResponse: DocSaleList is null", new Exception());
                    sendRequestFinished(result);
                    return;
                }

                MyHelper.getInstance().deleteDocSaleList();

                try {
                    list.save();
                } catch (Exception e) {
                    Log.w(TAG, "list.save(): " + e.toString());
                    sendRequestFinished(result);
                    return;
                }

                DataLoader.LoadMissingDataForObject(null, DocSaleList.class);
                for (DocSale docSale :
                        DocSaleList.getList().getValues()) {
                    docSale.setForeignObjects();
                }

                sendRequestFinished(result);

            }

            @Override
            public void onFailure(Call<DocSaleList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                result.message = t.toString();
                sendRequestFinished(result);
            }
        });


    }

    private void loadImage() {

        Log.d(TAG, "loadImage: -----------------------------------------------------------------------");
        Log.d(TAG, "loadImage: -----------------------------------------------------------------------");

        final String ref_Key = mIntent.getStringExtra(Constants.REF_KEY_LABEL);
        //Объект eq cast(guid'0d4a79cb-9843-4147-bcd9-80ac3ca2b9c7', 'Catalog_Товары')
        String filter = "Объект eq cast(guid'" + ref_Key + "', 'Catalog_Номенклатура')"; // and ВидДанных eq 'Изображение'
        Log.d(TAG, "loadImage: " + filter);

        final RequestResult result = new RequestResult(Constants.REQUESTS.LOAD_IMAGE);

        ImageProduct imageProduct = ImageProduct.getObject(ImageProduct.class, ref_Key);
        if (imageProduct != null) {
            result.ref_Key = ref_Key;
            result.success = true;
            sendRequestFinished(result);
            return;
        }

        ImageRequest request = ServiceGenerator.createService(ImageRequest.class);
        Call<ImageList> callDocuments = request.call(RetroConstants.getMap(filter));
        callDocuments.enqueue(new Callback<ImageList>() {
            @Override
            public void onResponse(Call<ImageList> call, Response<ImageList> response) {
                ImageList list = response.body();
                if (list == null) {
                    Log.wtf(TAG, "onResponse: DocSaleList is null", new Exception());
                    return;
                }

                if (list.size() > 0) {
                    ImageProduct image = list.getValues().iterator().next();
                    try {
                        image.save();
                        result.success = true;
                        result.ref_Key = image.getRef_Key();
                    } catch (Exception e) {
                        Log.w(TAG, "image.save(): " + e.toString());
                    }

                }
                sendRequestFinished(result);
            }

            @Override
            public void onFailure(Call<ImageList> call, Throwable t) {
                Log.d(TAG, "ImageList onFailure: " + t.toString());
            }
        });

    }

    private void sendRequestFinished(RequestResult result) {
        if (mResultReceiver == null) {
            return;
        }
        Bundle mResultBundle = new Bundle();
        fillResultBundle(result, mResultBundle);
        mResultReceiver.send(result.requestedOperation.ordinal(), mResultBundle);
    }


    private void loadDocumentFromServer(final RequestResult result) {

        final String ref_Key = result.ref_Key;
        final long id = mIntent.getLongExtra("id", -1);
        String filter = "Ref_Key eq guid'" + ref_Key + "'";

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
                        throw new RuntimeException("Ошибка сохранения документа локально!!!");
                    }

                    DataLoader.LoadMissingDataForObject(docSale, DocSale.class);
                    docSale.setForeignObjects();

                    sendRequestFinished(result);


                }

            }

            @Override
            public void onFailure(Call<DocSaleList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });

    }

    void sendFeedback(String message) {
        Bundle b = new Bundle();
        b.putString(Constants.MESSAGE_LABEL, message);
        mResultReceiver.send(Constants.FEEDBACK, b);
    }

    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    private Boolean isLoadRequired(CDO object) {
        Integer serverRecordsCount = getRecordsCountFromServer(object);
        if (serverRecordsCount == null) {
            RequestResult result = new RequestResult(Constants.REQUESTS.NONE);
            result.message = "Ошибка связи!";
            result.success = false;
            result.resultCode = -1;
            sendRequestFinished(result);
        }
        if (serverRecordsCount < 0) {
            return false;

        }
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

        final RequestResult result = new RequestResult(Constants.REQUESTS.SAVE_DOCUMENT);
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
                                 result.resultCode = response.raw().code();
                                 if (result.resultCode == 200 || result.resultCode == 201) {
                                     String location = response.headers().get("location");
                                     if (location == null) {
                                         result.ref_Key = Constants.ZERO_GUID;
                                         result.message = "location not found!";
                                     }
                                     int i = location.indexOf("guid'");
                                     if (i > 0) {
                                         result.message = "Документ записан";
                                         result.ref_Key = location.substring(i + 5, location.length() - 2);
                                         result.success = true;
                                         onSaveComplete(result);
                                     }
                                 } else {
                                     result.message = response.raw().message();
                                     onSaveComplete(result);
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 result.message = t.toString();
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
                                 result.resultCode = response.raw().code();
                                 if (result.resultCode == 200) {
                                     result.ref_Key = docSale.getRef_Key();
                                     result.message = "документ сохранен";
                                     result.success = true;
                                     onSaveComplete(result);
                                 } else {
                                     //Разбираемся что за хня
                                     Log.d(TAG, "onResponse: хуйня какая-то опять...");
                                     onSaveComplete(result);
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 result.message = t.toString();
                                 onSaveComplete(result);
                             }
                         }
            );
        }
    }

    private void postDocumentIn1C() {

        final RequestResult result = new RequestResult(Constants.REQUESTS.POST_DOCUMENT);
        long id = mIntent.getLongExtra("id", -1);
        final DocSale docSale = DocSale.getByID(id); // пока сохраняем новый док в базу с нулевым гидом

        //вызываем метод patch такой документ уже есть в базе 1с
        PostDocument mPostRequest = ServiceGenerator.createXMLService(PostDocument.class);
        Call<ResponseBody> call = mPostRequest.call(docSale.getRef_Key());
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             result.resultCode = response.raw().code();
                             if (result.resultCode == 200) {
                                 result.ref_Key = docSale.getRef_Key();
                                 result.message = "Документ проведен";
                                 result.success = true;
                             } else {
                                 result.message = response.raw().message();
                                 //Разбираемся что за хня
                                 Log.d(TAG, "onResponse: хуйня какая-то опять...");
                             }
                             onSaveComplete(result);
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {
                             result.message = t.toString();
                             onSaveComplete(result);
                         }
                     }
        );
    }

    private void onSaveComplete(RequestResult result) {

        Bundle mResultBundle = new Bundle();
        fillResultBundle(result, mResultBundle);

        if (result.success) {
            ///если успешно сохранили или провели документ в 1с, нужно скачать его с сервера
            //т.к. в нем могли произойти изменения, например добавились или удалились подарки...
            //и да, это неправильно.
            loadDocumentFromServer(result);
        } else {
            sendRequestFinished(result);
        }
    }


    private void findDiscountCard() {
        final RequestResult result = new RequestResult(Constants.REQUESTS.DISCOUNT_CARD);
        final String cardNumber = mIntent.getStringExtra(Constants.REF_KEY_LABEL);
        DiscountCard card = DiscountCard.findCardByNumber(cardNumber);

        if (card != null) {
            result.success = true;
            result.ref_Key = card.getRef_Key();
            sendRequestFinished(result);
        }


        String filter = "КодКарты eq '" + cardNumber + "'";
        DiscountCardsRequest request = ServiceGenerator.createService(DiscountCardsRequest.class);
        Call<DiscountCardsList> call = request.call(RetroConstants.getMap(filter));
        call.enqueue(new Callback<DiscountCardsList>() {
            @Override
            public void onResponse(Call<DiscountCardsList> call, Response<DiscountCardsList> response) {
                result.resultCode = response.code();
                DiscountCardsList list = response.body();
                if (list == null) {
                    result.message = "Сервер вернул неверный результат";
                } else {
                    if (list.size() > 0) {
                        DiscountCard card = list.getValues().iterator().next();
                        card.save();
                        result.ref_Key = card.getRef_Key();
                        result.success = true;
                    } else {
                        result.message = "Карта номер " + cardNumber + " не найдена";
                    }
                }
                sendRequestFinished(result);
            }

            @Override
            public void onFailure(Call<DiscountCardsList> call, Throwable t) {
                result.message = t.toString();
                result.resultCode = 0;
                sendRequestFinished(result);
            }
        });
    }


}
