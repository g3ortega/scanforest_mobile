package challenge.scanforest.api;

import challenge.scanforest.api.callbacks.OnSessionCreated;
import challenge.scanforest.models.RegisterUser;
import challenge.scanforest.models.User;
import challenge.scanforest.utils.CLog;
import challenge.scanforest.utils.Credentials;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by gerardo on 4/11/15.
 */
public class UserService {
    final String TAG= "UserService";
    RestAdapter.Builder builder;
    UsersResources resource;

    public UserService(){
        builder = ApiManager.getDefaultBuilder();
        resource = builder
                .build()
                .create(UsersResources.class);
    }

    public void Register(RegisterUser user, final OnSessionCreated onRegister){
        resource.register(user,new Callback<Credentials>() {
            @Override
            public void success(Credentials credentials, Response response) {
                CLog.i(TAG,credentials.getToken());
                onRegister.onSuccess(credentials.getToken());
            }

            @Override
            public void failure(RetrofitError error) {
                CLog.i(TAG,error.getMessage());
                onRegister.onError(error.getMessage());
            }
        });
    }

    public void Login(User user, final OnSessionCreated onSessionCreated){
        resource.Login(user,new Callback<Credentials>() {
            @Override
            public void success(Credentials s, Response response) {
                onSessionCreated.onSuccess(s.getToken());
            }

            @Override
            public void failure(RetrofitError error) {
                onSessionCreated.onError(error.getMessage());
            }
        });
    }
}
