package challenge.scanforest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gerardo on 4/11/15.
 */
public class User {
    @SerializedName("email")
    private String mUserName;

    @SerializedName("password")
    private String mPassword;

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
