package ru.sk42.tradeodata.NetworkRequests;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by я on 12.09.2016.
 */

public interface SaveNewDocument {
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
