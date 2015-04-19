package challenge.scanforest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import challenge.scanforest.adapters.ImagesAdapter;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.BaseError;
import challenge.scanforest.api.callbacks.OnObjectSaved;
import challenge.scanforest.models.Alert;
import challenge.scanforest.models.AlertImage;
import challenge.scanforest.models.Description;
import challenge.scanforest.utils.GPSTracker;
import challenge.scanforest.utils.TypeConverter;
import retrofit.mime.TypedFile;


public class ReportIncident extends ActionBarActivity {

    RecyclerView recyclerView;
    ArrayList<AlertImage> mAlertImages;
    RadioGroup mAlertType;
    ArrayAdapter<CharSequence> mAlertAdapter;
    ImagesAdapter mImageAdapter;
    Button pic;
    File photo;

    EditText mDescriptioin;
    SeekBar mMagnitude;
    EditText mArea;
    ImageView viewImage;
    TextView mMagnitudeIndicator;


    Alert alert;
    AlertImage alertImage;

    GPSTracker gps;
    Location mLocation;
    String mCurrentPhotoPath;

    private static final int CAMERA_KEY = 1;
    private static final int GALLERY_KEY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            mLocation = gps.getLocation();
        } else {
            gps.showSettingsAlert();
        }

        alert = new Alert();
        viewImage = (ImageView) findViewById(R.id.alertImage);

        mAlertType = (RadioGroup) findViewById(R.id.rg_alert_type);
        pic = (Button) findViewById(R.id.btn_picture);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mDescriptioin = (EditText) findViewById(R.id.et_description);
        mMagnitude = (SeekBar) findViewById(R.id.sb_magnitud);
        mMagnitudeIndicator = (TextView) findViewById(R.id.tv_magnitude_indicator);
        mArea = (EditText) findViewById(R.id.et_area);

        Picasso.with(this).load(R.mipmap.ic_launcher).resize(200, 200).into(viewImage);
        mMagnitude.setOnSeekBarChangeListener(new AlertSeekerListener());

    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ReportIncident.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(options[0])) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = null;
                    try {
                        f = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(f!=null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, CAMERA_KEY);
                    }

                } else if (options[item].equals(options[1])) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_KEY);

                } else if (options[item].equals(options[2])) {
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

            if (requestCode == CAMERA_KEY) {
                try {
                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                            bitmapOptions);
                    photo=file;
                    //viewImage.setImageBitmap(bitmap);
                    Picasso.with(getApplicationContext()).load(photo).resize(200, 200).into(viewImage);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == GALLERY_KEY) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                photo=new File(picturePath);
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("Path", "" + picturePath + "");
                Picasso.with(getApplicationContext())
                        .load(photo)
                        .resize(200, 200)
                        .into(viewImage);
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
        int id = item.getItemId();
        if (id == R.id.action_send) {
            Alert alert = getAlert();
            if (isAlertValid(alert)) {
                ApiManager.alertService().SendAlert(alert, new OnObjectSaved<Alert>() {
                    @Override
                    public void onSuccess(Alert alert) {
                        TypedFile alertImage = new TypedFile("image/jpg", photo);
                        ApiManager.alertService().SendImage(alertImage, alert.getId(), new OnObjectSaved<AlertImage>() {
                            @Override
                            public void onSuccess(AlertImage object) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_reported), Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onError(BaseError error) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_upload_error), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
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

    private boolean isAlertValid(Alert alert) {
        if (alert == null) {
            return false;
        } else {
            if (alert.getLatitud() == 0 && alert.getLongitud() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.location_not_setted), Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
    }

    public Alert getAlert() {
        alert.setDescription(mDescriptioin.getText().toString());
        alert.setArea(TypeConverter.toFloat(mArea.getText().toString(), 0));
        if (mLocation != null) {
            alert.setLatitud(mLocation.getLatitude());
            alert.setLongitud(mLocation.getLongitude());
        }
        int selected =
                mAlertType.getCheckedRadioButtonId();
        int selectedType = mAlertType.getCheckedRadioButtonId();
        alert.setType(getAlertType(selectedType));

        return alert;
    }

    private String getAlertType(int selectedType){
        switch (selectedType){
            case R.id.rb_fire: return "fire";
            case R.id.rb_logging: return "logging";
            case R.id.rb_pest: return "pest";
            default: return null;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath =image.getAbsolutePath();
        return image;
    }

    private class AlertSeekerListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            alert.setMagnitude(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            changeMagnitudeIndicator(seekBar.getProgress());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            changeMagnitudeIndicator(seekBar.getProgress());
        }

        private void changeMagnitudeIndicator(int progress){
            int indicatorColor, indicatorBackground, indicatorText;
            if(progress < 3){
                indicatorColor = R.color.slight_color;
                indicatorBackground = R.color.slight__background;
                indicatorText = R.string.slight;
            }else if(progress < 6){
                indicatorColor = R.color.moderate_color;
                indicatorBackground = R.color.moderate_background;
                indicatorText = R.string.moderate;
            }else if(progress < 9){
                indicatorColor = R.color.moderate_severe_color;
                indicatorBackground = R.color.moderate_severe_background;
                indicatorText = R.string.moderate_severe;
            }else{
                indicatorColor = R.color.severe_color;
                indicatorBackground = R.color.severe_background;
                indicatorText = R.string.severe;
            }
            mMagnitudeIndicator.setTextColor(indicatorColor);
            mMagnitudeIndicator.setBackgroundColor(indicatorColor);
            mMagnitudeIndicator.setText(indicatorText);
        }
    }
}
