package challenge.scanforest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gerardo on 4/12/15.
 */
public class AlertImage {

    @SerializedName("id")
    private Integer id;
    @SerializedName("url")
    private String url="";
    @SerializedName("uploaded_image_file_name")
    private String fileName="";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
