package challenge.scanforest.api.callbacks;

import challenge.scanforest.api.BaseError;

/**
 * Created by gerardo on 4/12/15.
 */
public interface OnObjectSaved<T> {
    public void onSuccess(T object);
    public void onError(BaseError error);
}
