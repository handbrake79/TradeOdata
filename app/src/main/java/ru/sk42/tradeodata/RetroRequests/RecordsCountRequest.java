package ru.sk42.tradeodata.RetroRequests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by я on 12.09.2016.
 */
public interface RecordsCountRequest {
        //?$format=json&$filter=Parent_Key eq guid'91650e3d-51b0-11e3-8235-984be1645107' or Parent_Key eq guid'91650e34-51b0-11e3-8235-984be1645107'";
        @GET
        Call<Integer> call(@Url String url);
}
