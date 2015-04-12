package challenge.scanforest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    ImagesAdapter mImageAdapter;
    Button pic;
    Bitmap mbitmap;

    EditText mDescriptioin;
    EditText mMagnitude;
    EditText mArea;
    ImageView viewImage;

    Image image;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    protected Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);


        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        viewImage=(ImageView)findViewById(R.id.image);

        mAlertType = (Spinner) findViewById(R.id.alert_type);
        pic = (Button) findViewById(R.id.picture);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mDescriptioin = (EditText) findViewById(R.id.et_description);
        mMagnitude = (EditText) findViewById(R.id.et_magnitude);
        mArea = (EditText) findViewById(R.id.et_area);

        //TODO: change alert type to non static.
        mAlertAdapter = ArrayAdapter.createFromResource(this,
                R.array.alert_types, R.layout.spiner_item);
        mAlertType.setAdapter(mAlertAdapter);
        /*recyclerView = (RecyclerView)findViewById(R.id.pictures);
        mImages=new ArrayList<>();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        mImages = new ArrayList<>();
        mImageAdapter = new ImagesAdapter(this,mImages);
        recyclerView.setAdapter(mImageAdapter);*/
    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ReportIncident.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
//                else if (options[item].equals("Choose from Gallery"))
//                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
//
//                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);


                    mbitmap=bitmap;

                  viewImage.setImageBitmap(bitmap);



                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    image = new Image();
                    image.setName(file.getName());
                    image.setContentType("image/jpg");
                    image.setContent(getImageContent(bitmap));

                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("Path",""+ picturePath + "");

                /*Image image= new Image();
                image.setPath(picturePath);
                mImages.add(image);
                mImageAdapter.notifyDataSetChanged();*/
                mbitmap=thumbnail;
                viewImage.setImageBitmap(thumbnail);
            }
        }
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
        alert.setDescription("{description1:'"+description.getDescription()+"'}");
        alert.setMagnitude(TypeConverter.toInt(mMagnitude.getText().toString(), 0));
        alert.setArea(TypeConverter.toFloat(mArea.getText().toString(), 0));
        alert.setType(mAlertAdapter.getItem(mAlertType.getSelectedItemPosition()).toString());
        if(mLocation!=null){
            alert.setLatitud(mLocation.getLatitude());
            alert.setLongitud(mLocation.getLongitude());
        }
        alert.setPhoto(image);
        return alert;
    }

    private String getImageContent(Bitmap mbitmap){
        if(mbitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return encoded;
        }
        return "";
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
