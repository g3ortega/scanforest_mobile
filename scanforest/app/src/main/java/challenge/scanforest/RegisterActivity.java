package challenge.scanforest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
    RadioGroup rgRole;
    public static final int SIGN_UP=100;
    public static final String REASON="REASON";



    ApiManager api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        api=new ApiManager();
        rgRole = (RadioGroup) findViewById(R.id.rg_role);
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

    private boolean isUserValid(RegisterUser user) {
        if(user==null){
            return false;
        }else{
            if(user.getFirstName().equals("")){
                validationToast(getResources().getString(R.string.firt_name_required));
                return false;
            }

            if(user.getLastName().equals("")){
                validationToast(getResources().getString(R.string.last_name_required));
                return false;
            }

            if(user.getmUserName().equals("")){
                validationToast(getResources().getString(R.string.user_name_required));
                return false;
            }

            if(user.getmPassword().equals("")){
                validationToast(getResources().getString(R.string.password_required));
                return false;
            }

            if(!user.getmPassword().equals(user.getmPasswordConfirmation())){
                validationToast(getResources().getString(R.string.password_not_match));
                return false;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(user.getmUserName()).matches()){
                validationToast(getResources().getString(R.string.invalid_email));
                return false;
            }
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
        user.setmType(getRole(rgRole.getCheckedRadioButtonId()));
        user.setCelphone(etCellphone.getText().toString());
        return  user;
    }

    public String getRole(int id){
        switch (id) {
            case R.id.rb_technician:
                return "technician";
            case R.id.rb_normal:
                return "regular";
            default:
                return null;
        }
    }

    private void validationToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
