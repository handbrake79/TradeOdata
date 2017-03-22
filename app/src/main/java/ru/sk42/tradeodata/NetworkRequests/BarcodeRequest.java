package ru.sk42.tradeodata.NetworkRequests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.ProductInfo;

/**
 * Created by я on 12.09.2016.
 */
public interface BarcodeRequest {
    @GET("/tradeodata/hs/TradeOdata/ProductInfo")
    Call<ProductInfo> call(
            @Query(Constants.REF_KEY_LABEL) String barcode
            //передаем баркод а не рефкей
    );
}
