package ru.sk42.tradeodata.RetroRequests;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.sk42.tradeodata.Model.Document.DocSale;

/**
 * Created by я on 12.09.2016.
 */
public interface PatchDocument {
        //?$format=json&$filter=Parent_Key eq guid'91650e3d-51b0-11e3-8235-984be1645107' or Parent_Key eq guid'91650e34-51b0-11e3-8235-984be1645107'";
        @PATCH("ut836/odata/standard.odata/Document_РеализацияТоваровУслуг(guid'{guid}')")
        Call<ResponseBody> call(@Path("guid") String guid, @Body RequestBody body);
}
