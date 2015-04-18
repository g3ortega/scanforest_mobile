package challenge.scanforest.api;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

import challenge.scanforest.models.Alert;
import challenge.scanforest.models.AlertImage;
import challenge.scanforest.models.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Created by gerardo on 4/12/15.
 */
public interface IncidentsResource {
    @Multipart
    @POST("/api/alerts/{alert_id}/images")
    void UploadImage(@Part("alert_image") TypedFile photo, @Path("alert_id") Integer alertId, Callback<AlertImage> cb);

    @POST("/api/alerts")
    void SendAlert(@Body Alert alert, Callback<Alert> cb);

    @GET("/api/alerts")
    void getAlerts( Callback<ArrayList<Alert>> cb);
}
