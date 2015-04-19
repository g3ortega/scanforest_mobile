package challenge.scanforest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gerardo on 4/12/15.
 */
public class Description {
    @SerializedName("reason1")
    private
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
