package ru.sk42.tradeodata.NetworkRequests;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.ShippingRatesList;

/**
 * Created by я on 12.09.2016.
 */
public interface ShippingRatesRequest {
        @GET("ut836/odata/standard.odata/InformationRegister_ТарифыПеревозкиПоМаршрутам/SliceLast()")
        Call<ShippingRatesList> call(
                @QueryMap Map<String, String> options
        );
}
