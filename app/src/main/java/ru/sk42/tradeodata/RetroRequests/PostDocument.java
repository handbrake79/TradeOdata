package ru.sk42.tradeodata.RetroRequests;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.CharList;
import ru.sk42.tradeodata.Model.Document.DocSale;

/**
 * Created by я on 12.09.2016.
 */

public interface PostDocument {
    @Headers(
            {
                    "Content-Type: application/atom+xml",
                    "Accept: */*",
                    "User-Agent: 1C+Enterprise/8.3",
                    "Content-Type: application/octet-stream"
            }
    )
    @POST("ut836/odata/standard.odata/Document_РеализацияТоваровУслуг")
    Call<ResponseBody> call(@Body RequestBody body);
}
