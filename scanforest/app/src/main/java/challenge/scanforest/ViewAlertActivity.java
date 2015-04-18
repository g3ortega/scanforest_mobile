package challenge.scanforest;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import challenge.scanforest.api.AlertService;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.models.Alert;
import challenge.scanforest.models.AlertImage;


public class ViewAlertActivity extends ActionBarActivity implements OnMapReadyCallback {
    ImageView mAlertImage;
    TextView mLocation, mDescription, mArea, mMagnitude;
    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    Alert alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alert);
        mAlertImage = (ImageView) findViewById(R.id.alert_image);
//        mLocation = (TextView) findViewById(R.id.tv_location);
        mDescription = (TextView) findViewById(R.id.tv_description);
        mArea = (TextView) findViewById(R.id.tv_area);
        mMagnitude = (TextView) findViewById(R.id.tv_magnitude);
        mapFragment=(com.google.android.gms.maps.MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        alert = new Alert();
        alert.setDescription("BALBALBALAB");
        alert.setCreated("2014-04-15");
        alert.setArea(5);
        alert.setMagnitude(5);
        alert.setLatitud(40.5487);
        alert.setLongitud(68.5455);

        mDescription.setText(alert.getDescription());
        mArea.setText(String.valueOf(alert.getArea()));
        mMagnitude.setText(String.valueOf(alert.getMagnitude()));




//        Geocoder gcd = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = gcd.getFromLocation(alert.getLatitud(), alert.getLongitud(), 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (addresses.size() > 0){
//            mLocation.setText(addresses.get(0).getLocality());
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_alert, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap=map;
        LatLng alerPosition = new LatLng(alert.getLatitud(), alert.getLongitud());
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(alerPosition, 13));

        map.addMarker(new MarkerOptions()
                .title("I am here")
                .position(alerPosition));
    }
}
