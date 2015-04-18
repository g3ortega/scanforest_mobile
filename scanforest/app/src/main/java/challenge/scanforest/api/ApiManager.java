package challenge.scanforest.api;

import android.app.DownloadManager;

import java.util.HashMap;

import challenge.scanforest.models.User;
import challenge.scanforest.utils.Session;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Part;

/**
 * Created by gerardo on 4/11/15.
 */
public class ApiManager {



    public static RestAdapter.Builder getDefaultBuilder(){
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(getDefaultRequestInterceptor())
                .setEndpoint(getUrl());
        return  builder;
    }

    public static RestAdapter.Builder getImageBuilder(){
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(getImageRequestInterceptor())
                .setEndpoint(getUrl());
        return  builder;
    }

    private static RequestInterceptor getDefaultRequestInterceptor(){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type","application/json");
                request.addHeader("Accept", "application/json");
                Session session = Session.getInstance();
                if(!session.getToken().equals("")){
                    request.addHeader("Authorization","Token token="+session.getToken());
                }
            }
        };
        return requestInterceptor;
    }

    private static RequestInterceptor getImageRequestInterceptor(){
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
                Session session = Session.getInstance();
                if(!session.getToken().equals("")){
                    request.addHeader("Authorization","Token token="+session.getToken());
                }
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

    private static AlertService alertService;
    public static AlertService alertService(){
        if(alertService == null){
            alertService = new AlertService();
        }
        return alertService;
    }

    public static String getUrl() {
        final String url="http://192.168.0.106:3000";
        //String url="http://scanforest.ga";
        return url;
    }
}
