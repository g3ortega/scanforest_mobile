package challenge.scanforest.api;

import android.media.Image;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;

/**
 * Created by gerardo on 4/12/15.
 */
public interface IncidentsResource {

    @Multipart
    @PUT("/user/photo")
    void UploadImage(@Part("photo") Image photo, Callback<String> cb);
}
