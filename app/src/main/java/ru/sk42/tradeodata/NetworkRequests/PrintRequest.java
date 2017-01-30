package ru.sk42.tradeodata.NetworkRequests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.ProductInfo;

/**
 * Created by я on 12.09.2016.
 */
public interface PrintRequest {
        @GET("ut836/hs/TradeOdata/PrintDoc")
        Call<String> call(
                @Query(Constants.DOC_NUMBER) String ref_Key,
                @Query(Constants.PRINTER_NAME) String printerName
        );
}
