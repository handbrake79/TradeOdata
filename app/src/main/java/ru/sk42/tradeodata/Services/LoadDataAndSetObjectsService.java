package ru.sk42.tradeodata.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Response;
import ru.sk42.tradeodata.Helpers.MyHelper;
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
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.RoutesList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.StartingPointsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.StoreList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UnitsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UsersList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.VehicleTypesList;
import ru.sk42.tradeodata.Model.Catalogs.Organisation;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.Unit;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.Model.Documents.DocSaleList;
import ru.sk42.tradeodata.Model.Settings;
import ru.sk42.tradeodata.RetroRequests.CharRequest;
import ru.sk42.tradeodata.RetroRequests.ContractsRequest;
import ru.sk42.tradeodata.RetroRequests.CurrencyRequest;
import ru.sk42.tradeodata.RetroRequests.CustomersRequest;
import ru.sk42.tradeodata.RetroRequests.DiscountCardsRequest;
import ru.sk42.tradeodata.RetroRequests.OrganisationsRequest;
import ru.sk42.tradeodata.RetroRequests.RecordsCountRequest;
import ru.sk42.tradeodata.RetroRequests.RetroConstants;
import ru.sk42.tradeodata.RetroRequests.RetroDataLoader;
import ru.sk42.tradeodata.RetroRequests.RoutesRequest;
import ru.sk42.tradeodata.RetroRequests.ServiceGenerator;
import ru.sk42.tradeodata.RetroRequests.StartingPointsRequest;
import ru.sk42.tradeodata.RetroRequests.StoresRequest;
import ru.sk42.tradeodata.RetroRequests.UnitsRequest;
import ru.sk42.tradeodata.RetroRequests.UsersRequest;
import ru.sk42.tradeodata.RetroRequests.VehicleTypesRequest;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LoadDataAndSetObjectsService extends IntentService {


    final String TAG = "Service ***";
    ResultReceiver resultReceiver;
    Intent intent;

    public LoadDataAndSetObjectsService() {
        super("LoadDataAndSetObjectsService");
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
        if(mode != null){
            if(mode.equals(Constants.DATALOADER_MODE.PRELOAD.name())){
                Preload();
                return;
            }
            if(mode.equals(Constants.DATALOADER_MODE.DOCLIST.name())){
                LoadDataFromServer();
                return;
            }
            if(mode.equals(Constants.DATALOADER_MODE.DOC.name())){
                LoadDataFromServer();
                return;
            }
        }


    }

    private void Preload() {

        sendMessage("Начата предзагрузка");

        loadDiscountCards();

        loadCharacts();

        loadUnits();

        loadContracts();

        loadVehicleTypes();

        loadStores();

        loadUsers();

        loadRoutes();

        loadStartingPoints();

        loadCurrency();

        loadOrganisations();

        loadCustomers();

        sendServiceFinished();
    }

    private void loadCustomers() {
        Customer object = new Customer();
        if (!isLoadRequired(object)) return;
            CustomersRequest request = ServiceGenerator.createService(CustomersRequest.class);
            Call<CustomersList> call = request.call(RetroConstants.getMap("Ref_Key eq '"+ Constants.CUSTOMER_GUID+"'"));
            try {
                //sendMessage("Контрагенты");

                Response<CustomersList> response = call.execute();
                CustomersList list = response.body();
                //sendMessage("Контрагенты получены");
                MyHelper.getCustomerDao().delete(MyHelper.getCustomerDao().queryForAll());
                list.save();
                //sendMessage("Контрагенты сохранены в базу");
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
//                sendMessage("Валюты");

                Response<CurrencyList> response = call.execute();
                CurrencyList list = response.body();
                //              sendMessage("Валюты получены");
                MyHelper.getCurrencyDao().delete(MyHelper.getCurrencyDao().queryForAll());
                list.save();
                //            sendMessage("Валюты сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void loadOrganisations() {
        Organisation object = new Organisation();

        if (!isLoadRequired(object)) return;

            OrganisationsRequest request = ServiceGenerator.createService(OrganisationsRequest.class);
            Call<OrganisationsList> call = request.call(RetroConstants.getMap(""));
            try {
//                sendMessage("Организации");

                Response<OrganisationsList> response = call.execute();
                OrganisationsList list = response.body();
                //              sendMessage("Организации получены");
                MyHelper.getOrganisationDao().delete(MyHelper.getOrganisationDao().queryForAll());
                list.save();
                //            sendMessage("Организации сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    private void loadVehicleTypes() {
        VehicleType object = new VehicleType();

        if (!isLoadRequired(object)) return;
            VehicleTypesRequest request = ServiceGenerator.createService(VehicleTypesRequest.class);
            Call<VehicleTypesList> call = request.call(RetroConstants.getMap(""));
            try {
                //      sendMessage("Типы ТС");

                Response<VehicleTypesList> response = call.execute();
                VehicleTypesList list = response.body();
                //    sendMessage("Типы ТС получены");
                MyHelper.getVehicleTypesDao().delete(MyHelper.getVehicleTypesDao().queryForAll());
                list.save();
                //  sendMessage("Типы ТС сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    private void loadStores() {
        Store object = new Store();

        if (!isLoadRequired(object)) return;
            StoresRequest request = ServiceGenerator.createService(StoresRequest.class);
            Call<StoreList> call = request.call(RetroConstants.getMap(""));
            try {
//                sendMessage("Склады ");
                Response<StoreList> response = call.execute();
                StoreList list = response.body();
                //              sendMessage("Склады получены");
                MyHelper.getStoreDao().delete(MyHelper.getStoreDao().queryForAll());
                list.save();
                //            sendMessage("Склады сохранены в базу");
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
                sendMessage("Единицы измерения ");
                Response<UnitsList> response = call.execute();
                UnitsList list = response.body();
                TableUtils.dropTable(MyHelper.getUnitDao(), false);
                TableUtils.createTable(MyHelper.getUnitDao());
                MyHelper.getUnitDao().delete(MyHelper.getUnitDao().queryForAll());
                sendMessage("Единицы получены ");
                list.save();
                sendMessage("Единицы сохранены в базу ");
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
                sendMessage("Договоры контрагента ");
                Response<ContractsList> response = call.execute();
                ContractsList list = response.body();
                TableUtils.dropTable(MyHelper.getContractDao(), false);
                TableUtils.createTable(MyHelper.getContractDao());
                MyHelper.getContractDao().delete(MyHelper.getContractDao().queryForAll());
                sendMessage("Договоры получены в количестве " + String.valueOf(list.getValues().size()));
                list.save();
                sendMessage("Договоры сохранены в базу ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    private void loadDiscountCards() {

        DiscountCard object = new DiscountCard();

        if (!isLoadRequired(object)) return;


        DiscountCardsRequest request = ServiceGenerator.createService(DiscountCardsRequest.class);
        Call<DiscountCardsList> call = request.call(RetroConstants.getMap(RetroConstants.FILTERS.DISCOUNT_CARDS));
            try {
                sendMessage("Скидочные карты ");
                Response<DiscountCardsList> response = call.execute();
                DiscountCardsList list = response.body();
                TableUtils.dropTable(MyHelper.getDiscountCardDao(), false);
                TableUtils.createTable(MyHelper.getDiscountCardDao());
                sendMessage("карты получены в количестве " + String.valueOf(list.getValues().size()));
                list.save();
                sendMessage("карты сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




    private void loadCharacts() {
        Charact object = new Charact();

        if (!isLoadRequired(object)) return;

        CharRequest request1 = ServiceGenerator.createService(CharRequest.class);
            Call<CharList> call1 = request1.call(RetroConstants.getMap(""));
            try {
                sendMessage("Характеристики ");
                Response<CharList> response = call1.execute();
                CharList list = response.body();
                TableUtils.dropTable(MyHelper.getCharactDao(), false);
                TableUtils.createTable(MyHelper.getCharactDao());
                sendMessage("Характеристики получены");
                list.save();
                sendMessage("Характеристики сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private void loadUsers() {
        User object = new User();

        if (!isLoadRequired(object)) return;


        UsersRequest request1 = ServiceGenerator.createService(UsersRequest.class);
        Call<UsersList> call1 = request1.call(RetroConstants.getMap(RetroConstants.FILTERS.USERS));
            try {
                // sendMessage("Пользователи");
                Response<UsersList> response = call1.execute();
                UsersList list = response.body();
                MyHelper.getUserDao().delete(MyHelper.getUserDao().queryForAll());
                list.save();
                //    sendMessage("Пользователи сохранены");
                Settings.renewCurrentUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    private void loadRoutes() {
        Route object = new Route();

        if (!isLoadRequired(object)) return;


        RoutesRequest request1 = ServiceGenerator.createService(RoutesRequest.class);
            Call<RoutesList> call1 = request1.call(RetroConstants.getMap("DeletionMark eq false"));
            try {
                //sendMessage("Маршруты " );
                Response<RoutesList> response = call1.execute();
                RoutesList list = response.body();
                MyHelper.getRouteDao().delete(MyHelper.getRouteDao().queryForAll());
                list.save();
                //sendMessage("Маршруты сохранены");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private void loadStartingPoints() {
        StartingPoint object = new StartingPoint();

        if (!isLoadRequired(object)) return;


        StartingPointsRequest request1 = ServiceGenerator.createService(StartingPointsRequest.class);
        Call<StartingPointsList> call1 = request1.call(RetroConstants.getMap(""));
            try {
                //sendMessage("Начальные точки маршрутов ");
                Response<StartingPointsList> response = call1.execute();
                StartingPointsList list = response.body();
                MyHelper.getStartingPointDao().delete(MyHelper.getStartingPointDao().queryForAll());
                list.save();
                //sendMessage("Точки сохранены");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    private Integer getRecordsCountFromServer(CDO object) {
        RecordsCountRequest request = ServiceGenerator.createService(RecordsCountRequest.class);
        Call<Integer> call = request.call(Settings.getInfoBaseName() + "/odata/standard.odata/" + object.getMaintainedTableName() + "/$count" + "&$filter=" + object.getRetroFilterString());
        Integer count = 0;
        try {
            Response<Integer> response = call.execute();
            count = response.body();
            return  count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }


    private void LoadDataFromServer() {
        Integer id = intent.getIntExtra("id", -1);
        String ref_Key = intent.getStringExtra("ref_Key");

//        Log.d(LOG_TAG, "onHandleIntent start: docSaleListID = " + id.toString() + " docSaleRefKey = " + ref_Key);

        CDO cdo = null;
        Class clazz = null;
        if(id != -1) {
            cdo = null;
            clazz = DocSaleList.class;
        }
        else
        {
            cdo = (CDO) DocSale.getDocument(ref_Key);
            clazz = DocSale.class;
        }

        RetroDataLoader.LoadMissingDataForObject(cdo, clazz);

        Log.d(TAG, "onHandleIntent: ВЫЗЫВАЕМ SFO");
        //cdo.setForeignObjects();
        Log.d(TAG, "onHandleIntent: END SFO");

        sendServiceFinished();

    }


    void sendMessage(String message){
        Bundle b = new Bundle();
        b.putString("Message",message);
        resultReceiver.send(0, b);

    }

    public void onDestroy() {
        super.onDestroy();
//        Log.d(LOG_TAG, "onDestroy");
    }

    private void sendServiceFinished() {
        resultReceiver.send(1, null);
    }


    private boolean isLoadRequired(CDO object) {
        Integer serverRecordsCount = getRecordsCountFromServer(object);
        Integer localRecordsCount = 0;
        try {
            localRecordsCount = object.getDao().queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "isLoadRequired: класс " + object.getMaintainedTableName() + " локально " + localRecordsCount.toString() + " записей, на сервере " + serverRecordsCount.toString() + " записей");

        if (serverRecordsCount != localRecordsCount)
            return true;
        else
            return false;

    }

}
