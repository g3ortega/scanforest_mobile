package challenge.scanforest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import challenge.scanforest.adapters.ImagesAdapter;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.BaseError;
import challenge.scanforest.api.callbacks.OnObjectSaved;
import challenge.scanforest.models.Alert;
import challenge.scanforest.models.Description;
import challenge.scanforest.models.Image;
import challenge.scanforest.utils.TypeConverter;


public class ReportIncident extends ActionBarActivity implements LocationListener{

    RecyclerView recyclerView;
    ArrayList<Image> mImages;
    Spinner mAlertType;
    ArrayAdapter<CharSequence> mAlertAdapter;

    EditText mDescriptioin;
    EditText mMagnitude;
    EditText mArea;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    protected Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);


        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        mAlertType = (Spinner) findViewById(R.id.alert_type);

        mDescriptioin = (EditText) findViewById(R.id.et_description);
        mMagnitude = (EditText) findViewById(R.id.et_magnitude);
        mArea = (EditText) findViewById(R.id.et_area);

        //TODO: change alert type to non static.
        mAlertAdapter = ArrayAdapter.createFromResource(this,
                R.array.alert_types, R.layout.spiner_item);
        mAlertType.setAdapter(mAlertAdapter);
        recyclerView = (RecyclerView)findViewById(R.id.pictures);
        mImages=new ArrayList<>();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new ImagesAdapter(this,mImages));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_incident, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_send)
        {
            Alert alert =getAlert();
            if(isAlrtValid(alert)){
                ApiManager.alertService().SendAlert(alert, new OnObjectSaved<Alert>() {
                    @Override
                    public void onSuccess(Alert object) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_reported), Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onError(BaseError error) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.an_error_occured), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isAlrtValid(Alert alert) {
        if(alert ==null){
            return false;
        }else{
            if(alert.getLatitud()==0 && alert.getLongitud()==0){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.location_not_setted), Toast.LENGTH_LONG).show();
                return false;
            }else{
                return true;
            }
        }
    }

    public Alert getAlert() {
        Alert alert=new Alert();
        ArrayList<Description> descriptions= new ArrayList<>();
        Description description=new Description();
        description.setDescription(mDescriptioin.getText().toString());
        descriptions.add(description);
        HashMap<String,String> hash=new HashMap<>();
        hash.put("description1",description.getDescription());

        alert.setDescription(hash);
        alert.setMagnitude(TypeConverter.toInt(mMagnitude.getText().toString(), 0));
        alert.setArea(TypeConverter.toFloat(mArea.getText().toString(), 0));
        alert.setType(mAlertAdapter.getItem(mAlertType.getSelectedItemPosition()).toString());
        if(mLocation!=null){
            alert.setLatitud(mLocation.getLatitude());
            alert.setLongitud(mLocation.getLongitude());
        }
        return alert;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
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
