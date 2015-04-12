package challenge.scanforest.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gerardo on 4/12/15.
 */
public class Alert {
    @SerializedName("lat")
    private double latitud;
    @SerializedName("lon")
    private double longitud;
    @SerializedName("magnitude")
    private int magnitude;
    @SerializedName("description")
    private ArrayList<Description> description;
    @SerializedName("area")
    private float area;
    @SerializedName("created")
    private String created;
    @SerializedName("type")
    private String type;

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

    public ArrayList<Description> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<Description> description) {
        this.description = description;
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
}
