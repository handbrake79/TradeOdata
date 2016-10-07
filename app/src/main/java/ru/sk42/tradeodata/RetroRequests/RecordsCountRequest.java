package ru.sk42.tradeodata.RetroRequests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by я on 12.09.2016.
 */
public interface RecordsCountRequest {
        @GET
        Call<Integer> call(@Url String url);
}
