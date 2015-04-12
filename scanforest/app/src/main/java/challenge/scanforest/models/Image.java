package challenge.scanforest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gerardo on 4/12/15.
 */
public class Image {

    @SerializedName("filename")
    private String name="";
    @SerializedName("content_type")
    private String contentType="";
    @SerializedName("data")
    private String content="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
