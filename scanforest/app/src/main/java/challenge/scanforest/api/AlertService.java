package challenge.scanforest.api;

import android.media.Image;

import challenge.scanforest.api.callbacks.OnObjectSaved;
import challenge.scanforest.models.Alert;
import challenge.scanforest.models.AlertImage;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by gerardo on 4/12/15.
 */
public class AlertService {
    final String TAG= "UserService";
    RestAdapter.Builder builder;
    IncidentsResource resource;

    public AlertService(){
        builder = ApiManager.getDefaultBuilder();
        resource = builder
                .build()
                .create(IncidentsResource.class);
    }

    public void SendAlert(final Alert alert, final OnObjectSaved<Alert> onAlertSaved){
        resource.SendAlert(alert,new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                onAlertSaved.onSuccess(alert);
            }

            @Override
            public void failure(RetrofitError error) {
                onAlertSaved.onError(new BaseError());
            }
        });
    }

    public void SendImage(final TypedFile image, final Integer id, final OnObjectSaved<AlertImage> onImageSave){
        resource.UploadImage(image, id ,new Callback<AlertImage>() {
            @Override
            public void success(AlertImage alertImage, Response response) {
                onImageSave.onSuccess(alertImage);
            }

            @Override
            public void failure(RetrofitError error) {
                onImageSave.onError(new BaseError());
            }
        });
    }
}
