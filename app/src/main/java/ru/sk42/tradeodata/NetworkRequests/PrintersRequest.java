package ru.sk42.tradeodata.NetworkRequests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Printers;

/**
 * Created by я on 12.09.2016.
 */
public interface PrintersRequest {
        @GET("ut836/hs/TradeOdata/Printers")
        Call<Printers> call(
        );
}
