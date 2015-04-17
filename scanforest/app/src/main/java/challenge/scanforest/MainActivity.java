package challenge.scanforest;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import challenge.scanforest.utils.ServiceUtils;
import challenge.scanforest.utils.Session;


public class MainActivity extends ActionBarActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener {

    MapFragment mapFragment;
    GoogleMap map;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Session.getInstance().getToken().equals("")){
         Intent intent = new Intent(this,InitialActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }else{
            locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            setContentView(R.layout.activity_main);
            mapFragment=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_report)
        {
            Intent intent =new Intent(getApplicationContext(),ReportIncident.class);
            startActivity(intent);
        }
        //noinspection SimplifiableIfStatement

        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Location location = new ServiceUtils().getLocation();
        if(location != null){
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

            /*map.addMarker(new MarkerOptions()
                    .title("Sydney")
                    .snippet("The most populous city in Australia.")
                    .position(sydney));*/
        }
    }

    @Override
    public void onConnected(Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if(map != null){
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

            map.addMarker(new MarkerOptions()
                    .title("CCNN")
                    .position(sydney));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
