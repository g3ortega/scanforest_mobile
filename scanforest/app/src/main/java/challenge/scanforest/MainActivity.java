package challenge.scanforest;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import challenge.scanforest.Fragment.LoginDialog;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.BaseError;
import challenge.scanforest.api.callbacks.OnObjectSaved;
import challenge.scanforest.models.Alert;
import challenge.scanforest.utils.CLog;
import challenge.scanforest.utils.GPSTracker;
import challenge.scanforest.utils.Session;


public class MainActivity extends ActionBarActivity
        implements OnMapReadyCallback,
        View.OnClickListener, LoginDialog.LoginDialogListener,
        GPSTracker.LocationListener,GoogleMap.OnInfoWindowClickListener {

    private static final String TAG= MainActivity.class.getSimpleName();

    MapFragment mapFragment;
    GoogleMap mGoogleMap;
    GPSTracker tracker;
    ArrayList<Alert> alerts;
    Marker mMarker;

    String selectedType = "fire";
    String alertTypes[];
    Location myLocation;
    MenuItem item;

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
        tracker.setLocationListener(this);
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
                mGoogleMap.addCircle(new CircleOptions()
                                .center(alertLocation)
                                .radius(alert.getArea())
                                .strokeColor(getResources().getColor(R.color.secundaryColor))
                                .fillColor(getResources().getColor(R.color.lightSecundary))
                );
                mGoogleMap.addMarker(new MarkerOptions()
                                .title(getResources().getString(R.string.view_details))
                                .position(alertLocation)
                );
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.log_in_out);
        if(Session.getInstance().isUserLogged()){
            item.setTitle(getResources().getString(R.string.log_out));
        }else{
            item.setTitle(getResources().getString(R.string.Login));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_in_out) {
            if(Session.getInstance().isUserLogged()){
                Session.getInstance().setToken("");
                item.setTitle(getResources().getString(R.string.Login));
            }else {
                LoginDialog dialogFragment = new LoginDialog();
                Bundle args = new Bundle();
                args.putBoolean(LoginDialog.DISPATCH, false);
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(), "LoginFragment");
            }
        } else if (id == R.id.settings) {

        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.setOnInfoWindowClickListener(this);
        Location location;
        if (tracker.canGetLocation()) {
            location = tracker.getLocation();
            updateMyPosition(location);
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
            Bundle args = new Bundle();
            args.putBoolean(LoginDialog.DISPATCH, true);
            dialogFragment.setArguments(args);
            dialogFragment.show(getSupportFragmentManager(), "LoginFragment");
        } else {
            showAlertForm(selectedType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RegisterActivity.SIGN_UP) {
            String result = data.getStringExtra(RegisterActivity.REASON);
            if(result!=null && result=="SEND"){
                showAlertForm(selectedType);
                item.setTitle(getResources().getString(R.string.log_out));
            }
        }
    }

    @Override
    public void onPositiveLogin(Boolean forAddingAlert) {
        if(forAddingAlert)
            showAlertForm(selectedType);
        item.setTitle(getResources().getString(R.string.log_out));
    }

    @Override
    public void onRegisterRequest() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(RegisterActivity.REASON,"SEND");
        startActivityForResult(intent, RegisterActivity.SIGN_UP);
        startActivity(intent);
    }

    public void showAlertForm(String type) {
        Intent intent = new Intent(getApplicationContext(), ReportIncident.class);
        intent.putExtra("ALERT_TYPE", type);
        startActivity(intent);
    }

    @Override
    public void onLocationChange(Location loc) {
        updateMyPosition(loc);
    }

    private void updateMyPosition(Location location){
        myLocation=location;
        //Toast.makeText(this,"Updating Location",Toast.LENGTH_SHORT).show();
        if(mGoogleMap!=null && myLocation!=null){
            LatLng myPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));
            if(mMarker==null){
            mMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .title("I am here")
                    .position(myPosition));
            }else {
                mMarker.setPosition(myPosition);
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Alert alert = getAlertByPosition(marker.getPosition());
        showDetails(alert);
    }

    private Alert getAlertByPosition(LatLng position) {
        if(alerts==null)return null ;
        for(int i=0;i<alerts.size();i++){
            Alert alert = alerts.get(i);
            LatLng alertLocation = new LatLng(alert.getLatitud(), alert.getLongitud());
            if(alertLocation.latitude-position.latitude==0.0 && alertLocation.longitude-position.longitude==0.0){
                CLog.i(TAG,"Alert Found!");
                return alert;
            }
        }
        return null;
    }

    public void showDetails(Alert alert)
    {
        if(alert!=null){
            Intent intent = new Intent(this,ViewAlertActivity.class);
            Bundle bundle =new Bundle();
            bundle.putParcelable(ViewAlertActivity.ALERT_IDENTIFIER, alert);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
