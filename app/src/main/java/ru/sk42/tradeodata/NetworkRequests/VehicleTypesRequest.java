package ru.sk42.tradeodata.NetworkRequests;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.VehicleTypesList;

/**
 * Created by я on 12.09.2016.
 */
public interface VehicleTypesRequest {
        @GET("ut836/odata/standard.odata/Catalog_ТипыТС")
        Call<VehicleTypesList> call(
                @QueryMap Map<String, String> options
        );
}
