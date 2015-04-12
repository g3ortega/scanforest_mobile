package challenge.scanforest.api;

import challenge.scanforest.models.User;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Part;

/**
 * Created by gerardo on 4/11/15.
 */
public class ApiManager {

    public static RestAdapter.Builder getDefaultBuilder(){
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(getDefaultRequestInterceptor())
                .setEndpoint("http://scanforest.ga");
        return  builder;
    }

    private static RequestInterceptor getDefaultRequestInterceptor(){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type","application/json");
                request.addHeader("Accept", "application/json");
            }
        };
        return requestInterceptor;
    }

    private static UserService userService;
    public static UserService userService(){
        if(userService == null){
            userService = new UserService();
        }
        return userService;
    }
}
