package challenge.scanforest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import challenge.scanforest.adapters.ImagesAdapter;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.BaseError;
import challenge.scanforest.api.callbacks.OnObjectSaved;
import challenge.scanforest.models.Alert;
import challenge.scanforest.models.Image;


public class ReportIncident extends ActionBarActivity {

    RecyclerView recyclerView;
    ArrayList<Image> mImages;
    Spinner mAlertType;
    ArrayAdapter<CharSequence> mAlertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        mAlertType = (Spinner) findViewById(R.id.alert_type);

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
        return true;
    }

    public Alert getAlert() {
        Alert alert=new Alert();
        return alert;
    }
}
