package ru.sk42.tradeodata.RetroRequests;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ru.sk42.tradeodata.Model.Document.DocSaleList;

/**
 * Created by я on 12.09.2016.
 */
public interface SingleDocRequest {
        @GET("ut836/odata/standard.odata/Document_РеализацияТоваровУслуг")
        Call<DocSaleList> call(
                @QueryMap(encoded = true) Map<String, String> options
        );
}
