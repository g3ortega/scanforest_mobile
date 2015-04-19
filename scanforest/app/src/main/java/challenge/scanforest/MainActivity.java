package challenge.scanforest;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import challenge.scanforest.Fragment.LoginDialog;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.BaseError;
import challenge.scanforest.api.callbacks.OnObjectSaved;
import challenge.scanforest.models.Alert;
import challenge.scanforest.utils.GPSTracker;
import challenge.scanforest.utils.Session;


public class MainActivity extends ActionBarActivity
        implements OnMapReadyCallback, View.OnClickListener, LoginDialog.LoginDialogListener {

    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    GPSTracker tracker;
    ArrayList<Alert> alerts;

    String selectedType = "fire";
    String alertTypes[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertTypes = getResources().getStringArray(R.array.alert_types);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        registerFloatingButtons();


        ApiManager.alertService().getAlerts(new OnObjectSaved<ArrayList<Alert>>() {
            @Override
            public void onSuccess(ArrayList<Alert> object) {
                alerts = object;
                showAlertsInMap();
            }

            @Override
            public void onError(BaseError error) {

            }
        });
        tracker = new GPSTracker(this);
    }

    private void registerFloatingButtons() {
        FloatingActionButton fireBtn = (FloatingActionButton) findViewById(R.id.action_fire);
        fireBtn.setOnClickListener(this);

        FloatingActionButton loggintBtn = (FloatingActionButton) findViewById(R.id.action_logging);
        loggintBtn.setOnClickListener(this);

        FloatingActionButton pestBtn = (FloatingActionButton) findViewById(R.id.action_pest);
        pestBtn.setOnClickListener(this);
    }

    private void showAlertsInMap() {
        if (mGoogleMap != null && alerts != null) {
            for (int i = 0; i < alerts.size(); i++) {
                Alert alert = alerts.get(i);
                LatLng alertLocation = new LatLng(alert.getLatitud(), alert.getLongitud());
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
        if (id == R.id.log_out) {
            Session.getInstance().setToken("");
        } else if (id == R.id.settings) {

        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        Location location;
        if (tracker.canGetLocation()) {
            location = tracker.getLocation();
            if (location != null) {
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_fire) {
            selectedType = alertTypes[0];
        }
        if (view.getId() == R.id.action_logging) {
            selectedType = alertTypes[1];
        }
        if (view.getId() == R.id.action_pest) {
            selectedType = alertTypes[2];
        }

        if (!Session.getInstance().isUserLogged()) {
            LoginDialog dialogFragment = new LoginDialog();
            dialogFragment.show(getSupportFragmentManager(), "LoginFragment");
        } else {
            showAlertForm(selectedType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPositiveLogin(DialogFragment dialog) {
        showAlertForm(selectedType);
    }

    @Override
    public void onRegisterRequest(DialogFragment dialog) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void showAlertForm(String type) {
        Intent intent = new Intent(getApplicationContext(), ReportIncident.class);
        intent.putExtra("ALERT_TYPE", type);
        startActivity(intent);
    }
}
