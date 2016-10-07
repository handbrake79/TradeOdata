package ru.sk42.tradeodata.RetroRequests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sk42.tradeodata.Model.ProductInfo;

/**
 * Created by я on 12.09.2016.
 */
public interface ProductInfoRequest {
        @GET("ut836/hs/TradeOdata/ProductInfo")
        Call<ProductInfo> call(
                @Query("ref_Key") String ref_Key
        );
}
