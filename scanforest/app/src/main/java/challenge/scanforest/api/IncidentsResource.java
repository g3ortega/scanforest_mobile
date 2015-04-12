package challenge.scanforest.api;

import android.media.Image;

import challenge.scanforest.models.Alert;
import challenge.scanforest.models.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;

/**
 * Created by gerardo on 4/12/15.
 */
public interface IncidentsResource {

    @Multipart
    @PUT("/api/alerts")
    void UploadImage(@Part("photo") Image photo, Callback<String> cb);

    @POST("/api/alerts")
    void SendAlert(@Body Alert alert, Callback<String> cb);
}
