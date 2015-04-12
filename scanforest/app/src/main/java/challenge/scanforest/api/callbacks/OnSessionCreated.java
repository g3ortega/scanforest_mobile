package challenge.scanforest.api.callbacks;

/**
 * Created by gerardo on 4/11/15.
 */
public interface OnSessionCreated {
    public void onSuccess(String token);
    public void onError(String message);
}
