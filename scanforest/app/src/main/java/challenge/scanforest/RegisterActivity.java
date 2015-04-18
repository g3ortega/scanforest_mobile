package challenge.scanforest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.callbacks.OnSessionCreated;
import challenge.scanforest.models.RegisterUser;
import challenge.scanforest.models.User;
import challenge.scanforest.utils.Session;


public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {

    Button btnRegister;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmation;
    EditText etFirstName;
    EditText etLastName;
    EditText etCellphone;


    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;




    ApiManager api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        api=new ApiManager();
        spinner = (Spinner) findViewById(R.id.spiner_type);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, R.layout.spiner_item);

        adapter.setDropDownViewResource( R.layout.spiner_item);
        spinner.setAdapter(adapter);
        btnRegister = (Button)findViewById(R.id.btn_submit_register);
        btnRegister.setOnClickListener(this);
        etFirstName =(EditText)findViewById(R.id.et_first_name);
        etLastName =(EditText)findViewById(R.id.et_last_Name);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPassword= (EditText)findViewById(R.id.et_password);
        etConfirmation = (EditText)findViewById(R.id.et_confirm_password);
        etCellphone =(EditText)findViewById(R.id.et_cellphone);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_submit_register){
            RegisterUser user = getUser();
            if(isUserValid(user)){
                ApiManager.userService().Register(user,new OnSessionCreated() {
                    @Override
                    public void onSuccess(String token) {
                        Session.getInstance().setToken(token);
                        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private boolean isUserValid(User user) {
        if(user==null){
            return false;
        }else{
            //TODO:make validations;
            return true;
        }
    }

    public RegisterUser getUser() {
        RegisterUser user =new RegisterUser();
        user.setFirstName(etFirstName.getText().toString());
        user.setLastName(etLastName.getText().toString());
        user.setmUserName(etEmail.getText().toString());
        user.setmPassword(etPassword.getText().toString());
        user.setmPasswordConfirmation(etConfirmation.getText().toString());
        user.setmType(adapter.getItem(spinner.getSelectedItemPosition()).toString());
        user.setCelphone(etCellphone.getText().toString());
        return  user;
    }
}
