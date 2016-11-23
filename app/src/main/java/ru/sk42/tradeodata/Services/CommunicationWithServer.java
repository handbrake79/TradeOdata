package ru.sk42.tradeodata.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.j256.ormlite.table.TableUtils;

import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import ru.sk42.tradeodata.Model.Catalogs.Contract;
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
import ru.sk42.tradeodata.RetroRequests.SingleDocRequest;
import ru.sk42.tradeodata.XML.WrapperXML_DocSale;
import ru.sk42.tradeodata.Model.Catalogs.Unit;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Document.DocSale;
import ru.sk42.tradeodata.Model.Document.DocSaleList;
import ru.sk42.tradeodata.Model.SettingsOld;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.RetroRequests.CharRequest;
import ru.sk42.tradeodata.RetroRequests.ContractsRequest;
import ru.sk42.tradeodata.RetroRequests.CurrencyRequest;
import ru.sk42.tradeodata.RetroRequests.CustomersRequest;
import ru.sk42.tradeodata.RetroRequests.DiscountCardsRequest;
import ru.sk42.tradeodata.RetroRequests.DocsRequest;
import ru.sk42.tradeodata.RetroRequests.OrganisationsRequest;
import ru.sk42.tradeodata.RetroRequests.PatchDocument;
import ru.sk42.tradeodata.RetroRequests.PostDocument;
import ru.sk42.tradeodata.RetroRequests.ProductsRequest;
import ru.sk42.tradeodata.RetroRequests.RecordsCountRequest;
import ru.sk42.tradeodata.RetroRequests.RetroConstants;
import ru.sk42.tradeodata.RetroRequests.RoutesRequest;
import ru.sk42.tradeodata.RetroRequests.ShippingRatesRequest;
import ru.sk42.tradeodata.RetroRequests.StartingPointsRequest;
import ru.sk42.tradeodata.RetroRequests.StoresRequest;
import ru.sk42.tradeodata.RetroRequests.UnitsRequest;
import ru.sk42.tradeodata.RetroRequests.UsersRequest;
import ru.sk42.tradeodata.RetroRequests.VehicleTypesRequest;
import ru.sk42.tradeodata.XML.DateConverter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CommunicationWithServer extends IntentService {

    NotificationManager manager;
    final String TAG = "Service ***";
    ResultReceiver resultReceiver;
    Intent intent;

    public CommunicationWithServer() {
        super("CommunicationWithServer");
    }

    public void onCreate() {
        super.onCreate();
//        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.intent = intent;
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
                loadSingleDocument();
                return;
            }

            if (mode.equals(Constants.DATALOADER_MODE.SAVE_TO_1C.name())) {
                saveTo1C();
                return;
            }


        }


    }

    private class SaveResult {
        public SaveResult() {
            ok = false;
            guid = "";
            error = "";
        }

        boolean ok;
        String guid;
        String error;
        int code;
    }

    //вернет guid, если
    private void saveTo1C() {
        final SaveResult result = new SaveResult();
        DocSale docSale = DocSale.getDocument(Constants.ZERO_GUID); // пока сохраняем новый док в базу с нулевым гидом


        String xml = writeDocSaleToXMLString(docSale);

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), xml);
        if (docSale.getRef_Key().equals(Constants.ZERO_GUID)) {
            //это новый документ, гуида еще нет
            //вызываем метод Пост
            PostDocument mPostRequest = ServiceGenerator.createXMLService(PostDocument.class);
            Call<ResponseBody> call = mPostRequest.call(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    result.code = response.raw().code();
                    String location = response.headers().get("Location");
                    if (location == null) {
                        result.guid = Constants.ZERO_GUID;
                        result.error = "location not found!";
                    }
                    int i = location.indexOf("guid'");
                    if (i > 0) {
                        result.guid = location.substring(i + 5, location.length() - 3);
                        result.ok = true;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.error = t.toString();
                }
            });
        }

        if (!docSale.getRef_Key().equals(Constants.ZERO_GUID)) {
            //вызываем метод patch такой документ уже есть в базе 1с
            PatchDocument mPatchRequest = ServiceGenerator.createXMLService(PatchDocument.class);
            Call<ResponseBody> call = mPatchRequest.call(docSale.getRef_Key(), body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    result.code = response.raw().code();
                    //разбираемся
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    result.error = t.toString();
                }
            });
        }

        String msg;
        if(result.ok){
            msg = "Документ успешно сохранен";
        }
        else
        {
            msg = "Ошибка сохранения: " + result.error;
        }

        Bundle bundle = new Bundle();
        bundle.putString("guid", result.guid);
        bundle.putString("error", msg);
        bundle.putInt("code", result.code);
        bundle.putString("guid", result.guid);

        resultReceiver.send(Constants.SAVE_DOCUMENT_RESULT, bundle);

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
        loadDiscountCards();

        sendFeedback("loadCharacts");
        loadCharacts();

        sendFeedback("loadUnits");
        loadUnits();

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

        int count = 0;

        try {
            count = MyHelper.getShippingRouteDao().queryForAll().size();
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
            list.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContracts() {
        Contract object = new Contract();

        if (!isLoadRequired(object)) return;

        ContractsRequest request = ServiceGenerator.createService(ContractsRequest.class);
        Call<ContractsList> call = request.call(RetroConstants.getMap("Owner_Key eq guid'" + Constants.CUSTOMER_GUID + "'"));
        try {
            Response<ContractsList> response = call.execute();
            ContractsList list = response.body();
            TableUtils.dropTable(MyHelper.getContractDao(), false);
            TableUtils.createTable(MyHelper.getContractDao());
            list.save();
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
            list.save();
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
            SettingsOld.renewCurrentUser();
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

        Call<Integer> call = request.call(SettingsOld.getInfoBaseName() + "/odata/standard.odata/" + object.getMaintainedTableName() + "/$count" + filter);
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
        MissingDataLoader.LoadMissingDataForObject(null, DocSaleList.class);
        for (DocSale docSale :
                DocSaleList.getList().getValues()) {
            docSale.setForeignObjects();
        }

        sendServiceFinished("Загрузка объектов по ссылкам для списка завершена");

    }

    private void loadMissingDataForSingleDocument(String ref_Key) {
        if(ref_Key == null) {
            ref_Key = intent.getStringExtra("ref_Key");
        }
        DocSale doc = DocSale.getDocument(ref_Key);
        MissingDataLoader.LoadMissingDataForObject(doc, DocSale.class);
        doc.setForeignObjects();

        sendServiceFinished("Загрузка объектов по ссылкам для документа завершена");

    }

    private void loadDocuments() {

        Calendar d1 = GregorianCalendar.getInstance();
        d1.setTimeInMillis(intent.getLongExtra("StartDate", 0));
        Calendar d2 = GregorianCalendar.getInstance();
        d2.setTimeInMillis(d1.getTimeInMillis());
        d2.add(Calendar.DATE, 1);

        String df1 = Uttils.DATE_FORMAT_1C.format(d1.getTime());
        String df2 = Uttils.DATE_FORMAT_1C.format(d2.getTime());

        String filter = "Date gt datetime'" + df1 + "' and Date lt datetime'" + df2 + "'" + " and Контрагент_Key eq guid'" + Constants.CUSTOMER_GUID + "'" + " and Ответственный_Key eq guid'" + SettingsOld.getCurrentUser().getRef_Key() + "'";


        sendFeedback("Загрузка документов на " + Uttils.DATE_FORMATTER.format(d1.getTime()));

        DocsRequest request = ServiceGenerator.createService(DocsRequest.class);
        Call<DocSaleList> callDocuments = request.call(RetroConstants.getMap(filter));
        callDocuments.enqueue(new Callback<DocSaleList>() {
            @Override
            public void onResponse(Call<DocSaleList> call, Response<DocSaleList> response) {
                DocSaleList list = response.body();
                if (list == null) {
                    Log.wtf(TAG, "onResponse: DocSaleList is null", new Exception());
                    return;
                }

                MyHelper.getInstance().deleteDocSaleList();

                try {
                    list.save();
                } catch (Exception e) {
                    Log.w(TAG, "list.save(): " + e.toString());
                }

                loadMissingDataForDocumentsList();

                sendServiceFinished("Загрузка списка документов завершена");

            }

            @Override
            public void onFailure(Call<DocSaleList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());

            }
        });


    }

    private void loadSingleDocument() {
        final String ref_Key = intent.getStringExtra("ref_Key");
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

                if(list.size() == 1)
                {
                    DocSale docSale = list.getValues().iterator().next();
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
        sendNotification(msg);
        sendFeedback(msg);
        resultReceiver.send(Constants.LOAD_FINISHED, null);
    }


    private boolean isLoadRequired(CDO object) {
        Integer serverRecordsCount = getRecordsCountFromServer(object);
        Integer localRecordsCount = 0;
        try {
            localRecordsCount = object.getDao().queryForAll().size();
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

        String msg = object.getMaintainedTableName() + " локально " + localRecordsCount.toString() + " записей, на сервере " + serverRecordsCount.toString() + " записей";
        Log.d(TAG, "isLoadRequired: класс " + msg);

        if (serverRecordsCount.intValue() != localRecordsCount.intValue()) {
            sendFeedback(msg);
            return true;
        } else
            return false;

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


    private String writeDocSaleToXMLString(DocSale docSale) {
        String xmlString;

        Registry registry = new Registry();
        Strategy strategy = new RegistryStrategy(registry);
        DateConverter dateConverter = new DateConverter();
        try {
            registry.bind(Date.class, dateConverter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Persister persister = new Persister(strategy);
        StringWriter writer = new StringWriter();

        WrapperXML_DocSale XMLDocSaleWrapper = new WrapperXML_DocSale(docSale);


        try {
            persister.write(XMLDocSaleWrapper, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        xmlString = writer.toString();
        return xmlString;
    }


}