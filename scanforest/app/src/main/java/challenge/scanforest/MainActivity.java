package challenge.scanforest;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.BaseError;
import challenge.scanforest.api.callbacks.OnObjectSaved;
import challenge.scanforest.models.Alert;
import challenge.scanforest.utils.GPSTracker;
import challenge.scanforest.utils.ServiceUtils;
import challenge.scanforest.utils.Session;


public class MainActivity extends ActionBarActivity
        implements OnMapReadyCallback{

    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    GPSTracker tracker;
    ArrayList<Alert> alerts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Session.getInstance().getToken().equals("")){
         Intent intent = new Intent(this,InitialActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }else{
            setContentView(R.layout.activity_main);
            mapFragment=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            ApiManager.alertService().getAlerts(new OnObjectSaved<ArrayList<Alert>>(){
                @Override
                public void onSuccess(ArrayList<Alert> object) {
                    alerts=object;
                    showAlertsInMap();
                }

                @Override
                public void onError(BaseError error) {

                }
            });
            tracker=new GPSTracker(this);
        }
    }

    private void showAlertsInMap() {
        if(mGoogleMap!=null && alerts !=null){
            for(int i=0;i<alerts.size();i++){
                Alert alert =alerts.get(i);
                LatLng alertLocation = new LatLng(alert.getLatitud(),alert.getLongitud());
                mGoogleMap.addMarker(new MarkerOptions()
                        .title(alert.getType())
                        .position(alertLocation));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_report)
        {
            Intent intent =new Intent(getApplicationContext(),ReportIncident.class);
            startActivity(intent);
        }else if(id ==R.id.log_out){
            Session.getInstance().setToken("");
            Intent intent = new Intent(this,InitialActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }else if(id==R.id.settings){
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap=map;
        Location location;
        if(tracker.canGetLocation()){
            location = tracker.getLocation();
            if(location != null){
                LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));

            map.addMarker(new MarkerOptions()
                    .title("I am here")
                    .position(myPosition));
            }
        }
        showAlertsInMap();
    }


}
