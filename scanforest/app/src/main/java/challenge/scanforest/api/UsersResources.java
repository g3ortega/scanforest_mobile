package challenge.scanforest.api;

import challenge.scanforest.models.RegisterUser;
import challenge.scanforest.models.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by gerardo on 4/11/15.
 */
public interface UsersResources {


    @POST("/api/user")
    void register(@Body RegisterUser user, Callback<String> cb);

    @POST("/api/user")
    void Login(@Body User user, Callback<String> cb);
}
