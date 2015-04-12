package challenge.scanforest.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gerardo on 4/12/15.
 */
public class Credentials {
    @SerializedName("token")
    private String token="";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
