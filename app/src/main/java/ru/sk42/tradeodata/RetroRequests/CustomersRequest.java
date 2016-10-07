package ru.sk42.tradeodata.RetroRequests;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CustomersList;

/**
 * Created by я on 12.09.2016.
 */
public interface CustomersRequest {
        //?$format=json&$filter=Parent_Key eq guid'91650e3d-51b0-11e3-8235-984be1645107' or Parent_Key eq guid'91650e34-51b0-11e3-8235-984be1645107'";
        @GET("ut836/odata/standard.odata/Catalog_Контрагенты")
        Call<CustomersList> call(
                @QueryMap Map<String, String> options
        );
}
