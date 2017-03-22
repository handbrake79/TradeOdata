package ru.sk42.tradeodata.NetworkRequests;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

/**
 * Created by я on 12.09.2016.
 */
public interface SaveExistingDocument {
        //?$format=json&$filter=Parent_Key eq guid'91650e3d-51b0-11e3-8235-984be1645107' or Parent_Key eq guid'91650e34-51b0-11e3-8235-984be1645107'";
        @Headers("Content-Type: application/atom+xml")
        @PATCH("/tradeodata/odata/standard.odata/Document_РеализацияТоваровУслуг(guid'{guid}')")
        Call<ResponseBody> call(@Path("guid") String guid, @Body RequestBody body);
}
