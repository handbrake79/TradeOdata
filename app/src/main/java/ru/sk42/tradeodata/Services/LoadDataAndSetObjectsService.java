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
import ru.sk42.tradeodata.Helpers.Helper;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Catalogs.Customer;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CharList;
import ru.sk42.tradeodata.Model.Catalogs.Charact;
import ru.sk42.tradeodata.Model.Catalogs.Contract;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ContractsList;
import ru.sk42.tradeodata.Model.Catalogs.Currency;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CurrencyList;
import ru.sk42.tradeodata.Model.Catalogs.DiscountCard;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CustomersList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.DiscountCardsList;
import ru.sk42.tradeodata.Model.Catalogs.Organisation;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.OrganisationsList;
import ru.sk42.tradeodata.Model.Catalogs.Route;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.RoutesList;
import ru.sk42.tradeodata.Model.Catalogs.StartingPoint;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.StartingPointsList;
import ru.sk42.tradeodata.Model.Catalogs.Store;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.StoreList;
import ru.sk42.tradeodata.Model.Catalogs.Unit;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UnitsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UsersList;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.VehicleTypesList;
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


    ResultReceiver resultReceiver;
    Intent intent;

    final String TAG = "Service ***";

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
                LoadDataAndSFO();
                return;
            }
            if(mode.equals(Constants.DATALOADER_MODE.DOC.name())){
                LoadDataAndSFO();
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

        locadOrganisations();

        loadCustomers();

        sendServiceFinished();
    }

    private void loadCustomers() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Customer().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Customer.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            CustomersRequest request = ServiceGenerator.createService(CustomersRequest.class);
            Call<CustomersList> call = request.call(RetroConstants.getMap("Ref_Key eq '"+ Constants.CUSTOMER_GUID+"'"));
            try {
                sendMessage("Валюты");

                Response<CustomersList> response = call.execute();
                CustomersList list = response.body();
                sendMessage("Валюты получены");
                Helper.getCustomerDao().delete(Helper.getCustomerDao().queryForAll());
                list.save();
                sendMessage("Валюты сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCurrency() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Currency().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Currency.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            CurrencyRequest request = ServiceGenerator.createService(CurrencyRequest.class);
            Call<CurrencyList> call = request.call(RetroConstants.getMap(""));
            try {
                sendMessage("Валюты");

                Response<CurrencyList> response = call.execute();
                CurrencyList list = response.body();
                sendMessage("Валюты получены");
                Helper.getCurrencyDao().delete(Helper.getCurrencyDao().queryForAll());
                list.save();
                sendMessage("Валюты сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void locadOrganisations() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Organisation().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Organisation.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            OrganisationsRequest request = ServiceGenerator.createService(OrganisationsRequest.class);
            Call<OrganisationsList> call = request.call(RetroConstants.getMap(""));
            try {
                sendMessage("Организации");

                Response<OrganisationsList> response = call.execute();
                OrganisationsList list = response.body();
                sendMessage("Организации получены");
                Helper.getOrganisationDao().delete(Helper.getOrganisationDao().queryForAll());
                list.save();
                sendMessage("Организации сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void loadVehicleTypes() {
        Integer serverRecordsCount = getRecordsCountFromServer(new VehicleType().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(VehicleType.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            VehicleTypesRequest request = ServiceGenerator.createService(VehicleTypesRequest.class);
            Call<VehicleTypesList> call = request.call(RetroConstants.getMap(""));
            try {
                sendMessage("Типы ТС");

                Response<VehicleTypesList> response = call.execute();
                VehicleTypesList list = response.body();
                sendMessage("Типы ТС получены");
                Helper.getVehicleTypesDao().delete(Helper.getVehicleTypesDao().queryForAll());
                list.save();
                sendMessage("Типы ТС сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void loadStores() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Store().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Store.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            StoresRequest request = ServiceGenerator.createService(StoresRequest.class);
            Call<StoreList> call = request.call(RetroConstants.getMap(""));
            try {
                sendMessage("Склады " + serverRecordsCount.toString());
                Response<StoreList> response = call.execute();
                StoreList list = response.body();
                sendMessage("Склады получены");
                Helper.getStoreDao().delete(Helper.getStoreDao().queryForAll());
                list.save();
                sendMessage("Склады сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUnits() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Unit().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Unit.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            UnitsRequest request = ServiceGenerator.createService(UnitsRequest.class);
            Call<UnitsList> call = request.call(RetroConstants.getMap(""));
            try {
                sendMessage("Единицы измерения " + serverRecordsCount.toString());
                Response<UnitsList> response = call.execute();
                UnitsList list = response.body();
                TableUtils.dropTable(Helper.getUnitsDao(), false);
                TableUtils.createTable(Helper.getUnitsDao());
                Helper.getUnitsDao().delete(Helper.getUnitsDao().queryForAll());
                sendMessage("Единицы получены ");
                list.save();
                sendMessage("Единицы сохранены в базу ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadContracts() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Contract().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Contract.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            ContractsRequest request = ServiceGenerator.createService(ContractsRequest.class);
            Call<ContractsList> call = request.call(RetroConstants.getMap("Owner_Key eq guid'" + Constants.CUSTOMER_GUID + "'"));
            try {
                sendMessage("Договоры контрагента " + serverRecordsCount.toString());
                Response<ContractsList> response = call.execute();
                ContractsList list = response.body();
                TableUtils.dropTable(Helper.getContractDao(), false);
                TableUtils.createTable(Helper.getContractDao());
                Helper.getContractDao().delete(Helper.getContractDao().queryForAll());
                sendMessage("Договоры получены ");
                list.save();
                sendMessage("Договоры сохранены в базу ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void loadDiscountCards() {
        Integer serverRecordsCount = getRecordsCountFromServer(new DiscountCard().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(DiscountCard.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            DiscountCardsRequest request1 = ServiceGenerator.createService(DiscountCardsRequest.class);
            Call<DiscountCardsList> call1 = request1.call(RetroConstants.getMap("DeletionMark eq false"));
            try {
                sendMessage("Скидочные карты " + serverRecordsCount.toString());
                Response<DiscountCardsList> response = call1.execute();
                DiscountCardsList list = response.body();
                TableUtils.dropTable(Helper.getDiscountCardDao(),false);
                TableUtils.createTable(Helper.getDiscountCardDao());
                sendMessage("карты получены");
                list.save();
                sendMessage("карты сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void loadCharacts() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Charact().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Charact.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            CharRequest request1 = ServiceGenerator.createService(CharRequest.class);
            Call<CharList> call1 = request1.call(RetroConstants.getMap(""));
            try {
                sendMessage("Характеристики " + serverRecordsCount.toString());
                Response<CharList> response = call1.execute();
                CharList list = response.body();
                TableUtils.dropTable(Helper.getCharDao(),false);
                TableUtils.createTable(Helper.getCharDao());
                sendMessage("Характеристики получены");
                list.save();
                sendMessage("Характеристики сохранены в базу");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUsers() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Charact().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Charact.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            UsersRequest request1 = ServiceGenerator.createService(UsersRequest.class);
            Call<UsersList> call1 = request1.call(RetroConstants.getMap(""));
            try {
                sendMessage("Пользователи");
                Response<UsersList> response = call1.execute();
                UsersList list = response.body();
                Helper.getUserDao().delete(Helper.getUserDao().queryForAll());
                list.save();
                sendMessage("Пользователи сохранены");
                Settings.renewCurrentUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void loadRoutes() {
        Integer serverRecordsCount = getRecordsCountFromServer(new Route().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Route.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            RoutesRequest request1 = ServiceGenerator.createService(RoutesRequest.class);
            Call<RoutesList> call1 = request1.call(RetroConstants.getMap("DeletionMark eq false"));
            try {
                sendMessage("Маршруты " + serverRecordsCount.toString());
                Response<RoutesList> response = call1.execute();
                RoutesList list = response.body();
                Helper.getRoutesDao().delete(Helper.getRoutesDao().queryForAll());
                list.save();
                sendMessage("Маршруты сохранены");
                Settings.renewCurrentUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadStartingPoints() {
        Integer serverRecordsCount = getRecordsCountFromServer(new StartingPoint().getMaintainedTableName());
        Integer localRecordsCount = getRecordsCountLocal(Route.class);
        if(serverRecordsCount == null || localRecordsCount == null || serverRecordsCount > localRecordsCount){
            StartingPointsRequest request1 = ServiceGenerator.createService(StartingPointsRequest.class);
            Call<StartingPointsList> call1 = request1.call(RetroConstants.getMap("DeletionMark eq false"));
            try {
                sendMessage("Начальные точки маршрутов " + serverRecordsCount.toString());
                Response<StartingPointsList> response = call1.execute();
                StartingPointsList list = response.body();
                Helper.getStartingPointsDao().delete(Helper.getStartingPointsDao().queryForAll());
                list.save();
                sendMessage("Точки сохранены");
                Settings.renewCurrentUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Integer getRecordsCountLocal(Class<?> clazz) {
        try {
            return Helper.getInstance().getDao(clazz).queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private  Integer getRecordsCountFromServer(String tableName){
        RecordsCountRequest request = ServiceGenerator.createService(RecordsCountRequest.class);
        Call<Integer> call = request.call(Settings.getInfoBaseName() + "/odata/standard.odata/" + tableName + "/$count");
        Integer count = null;
        try {
            Response<Integer> response = call.execute();
            count = response.body();
            return  count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }



    private void LoadDataAndSFO(){
        Integer id = intent.getIntExtra("id", -1);
        String ref_Key = intent.getStringExtra("ref_Key");

//        Log.d(LOG_TAG, "onHandleIntent start: docSaleListID = " + id.toString() + " docSaleRefKey = " + ref_Key);

        CDO cdo = null;
        Class clazz = null;
        if(id != -1) {
            cdo = (CDO) DocSaleList.getList();
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



}
