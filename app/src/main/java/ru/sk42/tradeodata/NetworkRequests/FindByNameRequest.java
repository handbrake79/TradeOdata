package ru.sk42.tradeodata.NetworkRequests;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ProductsList;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.ProductInfo;

/**
 * Created by я on 12.09.2016.
 */
public interface FindByNameRequest {
    @GET("/tradeodata/odata/standard.odata/Catalog_Номенклатура")
    Call<ProductsList> call(
            @QueryMap Map<String, String> options
            //
    );
}
