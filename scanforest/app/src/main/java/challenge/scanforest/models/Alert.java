package challenge.scanforest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gerardo on 4/12/15.
 */
public class Alert implements Parcelable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("lat")
    private double latitud;
    @SerializedName("lon")
    private double longitud;
    @SerializedName("magnitude")
    private int magnitude;
    @SerializedName("description")
    private String description;
    @SerializedName("area")
    private float area;
    @SerializedName("created")
    private String created;
    @SerializedName("alert_type")
    private String type;
    @SerializedName("uploaded_image")
    private String image;

    public Alert(){}

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(int magnitude) {
        this.magnitude = magnitude;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {return description; }

    public void setDescription(String description) { this.description = description;}

    protected Alert(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        latitud = in.readDouble();
        longitud = in.readDouble();
        magnitude = in.readInt();
        description = in.readString();
        area = in.readFloat();
        created = in.readString();
        type = in.readString();
        image =in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
        dest.writeInt(magnitude);
        dest.writeString(description);
        dest.writeFloat(area);
        dest.writeString(created);
        dest.writeString(type);
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Alert> CREATOR = new Parcelable.Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        @Override
        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}