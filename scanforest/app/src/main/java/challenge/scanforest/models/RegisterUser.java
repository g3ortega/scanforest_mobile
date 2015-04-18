package challenge.scanforest.models;

import com.google.gson.annotations.SerializedName;
/**
 * Created by gerardo on 4/11/15.
 */
public class RegisterUser extends User {
    @SerializedName("password_confirmation")
    private String mPasswordConfirmation;
    @SerializedName("type")
    private String mType;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("id")
    private int id;
    @SerializedName("cel_phone")
    private String celphone;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCelphone() {
        return celphone;
    }

    public void setCelphone(String celphone) {
        this.celphone = celphone;
    }
}
