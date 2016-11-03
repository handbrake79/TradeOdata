package ru.sk42.tradeodata.RetroRequests;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import retrofit2.Call;
import ru.sk42.tradeodata.Helpers.CheckRelatedDataToLoad;
import ru.sk42.tradeodata.Model.CDO;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CharList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ContractsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CustomersList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ProductsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.StoreList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UnitsList;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UsersList;
import ru.sk42.tradeodata.Model.Document.DocSaleList;

/**
 * Created by я on 19.08.2016.
 */
public class MissingDataLoader {

    static String TAG = "MissingDataLoader***";

    static private CDO objectToCheck;
    static private HashMap<Class<?>, ArrayList<String>> unresolvedLinks;
    static Runnable myrunnable = new Runnable() {
        @Override
        public void run() {

            try {
                makeRequests();

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "run: " + e.toString());
            }
        }
    };
    boolean needReload;
    Integer requestsPending;

    public static void LoadMissingDataForObject(Object mcdo, Class clazz) {

       // callBackInterface = mCallBackInterface;

        if (clazz.getCanonicalName().equals("ru.sk42.tradeodata.Model.Document.DocSaleList")) {
            unresolvedLinks = CheckRelatedDataToLoad.checkObject(DocSaleList.getList());
        } else {
            CDO cdo = (CDO) mcdo;
            String ref_Key = cdo.getRef_Key();
            objectToCheck = (CDO) CDO.getObject(clazz, ref_Key);
            unresolvedLinks = CheckRelatedDataToLoad.checkObject(objectToCheck);
        }
        sendRequestsInNewThread();


    }

    static private void sendRequestsInNewThread(){
        Thread thread1 = new Thread(myrunnable);
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }

    }

    static private void makeRequests() throws Exception{
        Class clazz;
        Iterator it = unresolvedLinks.entrySet().iterator();
        while (it.hasNext()) {

            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                clazz = (Class) pair.getKey();
                ArrayList<String> refs = (ArrayList<String>) pair.getValue();
                if (refs.isEmpty())
                    continue;

                //needReload = true;

                ArrayList<String> urlList = CheckRelatedDataToLoad.generateURL(clazz, refs);
                String className = clazz.getName();


                switch (className) {
                    case "ru.sk42.tradeodata.Model.Catalogs.Product":
                        for (String url : urlList) {
                            showProgress(" Загружаются товары");
                            ProductsRequest request = ServiceGenerator.createService(ProductsRequest.class);
                            Call<ProductsList> call = request.call(RetroConstants.getMap(url));
                            ProductsList list = call.execute().body();
                            list.save();
                            Log.d(TAG, "makeRequests: сохранили товары! ***");
                        }
                        break;
                    case "ru.sk42.tradeodata.Model.Catalogs.User":
                        for (String url : urlList) {
                            showProgress(" Загружается список пользователей ");
                            UsersRequest request = ServiceGenerator.createService(UsersRequest.class);
                            Call<UsersList> call = request.call(RetroConstants.getMap(url));
                            UsersList list = call.execute().body();
                            list.save();
                        }
                        break;
                    case "ru.sk42.tradeodata.Model.Catalogs.Customer":
                        for (String url : urlList) {
                            showProgress(" Загружается список покупателей ");
                            CustomersRequest request = ServiceGenerator.createService(CustomersRequest.class);
                            Call<CustomersList> call = request.call(RetroConstants.getMap(url));
                            CustomersList list = call.execute().body();
                            list.save();
                        }
                        break;
                    case "ru.sk42.tradeodata.Model.Catalogs.Contract":
                        for (String url : urlList) {
                            showProgress(" Загружается список договоров ");
                            ContractsRequest request = ServiceGenerator.createService(ContractsRequest.class);
                            Call<ContractsList> call = request.call(RetroConstants.getMap(url));
                            ContractsList list = call.execute().body();
                            list.save();
                        }
                        break;
                    case "ru.sk42.tradeodata.Model.Catalogs.Store":
                        for (String url : urlList) {

                            showProgress("load stores");
                            StoresRequest request = ServiceGenerator.createService(StoresRequest.class);
                            Call<StoreList> call = request.call(RetroConstants.getMap(url));
                            StoreList list = call.execute().body();
                            list.save();

                        }
                        break;
                    case "ru.sk42.tradeodata.Model.Catalogs.Unit":
                        for (String url : urlList) {

                            showProgress("load units");
                            UnitsRequest request = ServiceGenerator.createService(UnitsRequest.class);
                            Call<UnitsList> call = request.call(RetroConstants.getMap(url));
                            UnitsList list = call.execute().body();
                            list.save();

                        }
                        break;
                    case "ru.sk42.tradeodata.Model.Catalogs.Charact":
                        for (String url : urlList) {
                            showProgress("  характеристики");
                            CharRequest request = ServiceGenerator.createService(CharRequest.class);
                            Call<CharList> call = request.call(RetroConstants.getMap(url));
                            CharList list = call.execute().body();
                            list.save();

                        }
                        break;

                }
                it.remove(); // avoids a ConcurrentModificationException
            }


        }

    }

    static private void showProgress(String message) {
        Log.d(TAG, "showProgress: " + message);
    }



}
