package ru.sk42.tradeodata.NetworkRequests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by я on 12.09.2016.
 */
public interface PostDocument {
        //?$format=json&$filter=Parent_Key eq guid'91650e3d-51b0-11e3-8235-984be1645107' or Parent_Key eq guid'91650e34-51b0-11e3-8235-984be1645107'";
        @Headers("Content-Type: application/atom+xml")
        @POST("/tradeodata/odata/standard.odata/Document_РеализацияТоваровУслуг(guid'{guid}')/Post()")
        Call<ResponseBody> call(@Path("guid") String guid);
}
