package challenge.scanforest.models;

import com.google.gson.annotations.SerializedName;
/**
 * Created by gerardo on 4/11/15.
 */
public class RegisterUser extends User {
    @SerializedName("email")
    private String mEmail;

    @SerializedName("password_confirmation")
    private String mPasswordConfirmation;

    @SerializedName("type")
    private String mType;

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPasswordConfirmation() {
        return mPasswordConfirmation;
    }

    public void setmPasswordConfirmation(String mPasswordConfirmation) {
        this.mPasswordConfirmation = mPasswordConfirmation;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
