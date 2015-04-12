package challenge.scanforest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.callbacks.OnSessionCreated;
import challenge.scanforest.models.User;
import challenge.scanforest.utils.Session;


public class InitialActivity extends ActionBarActivity implements View.OnClickListener {

    EditText etUserName;
    EditText etPassword;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        Button btnRegister=(Button)findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        Button btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        etUserName = (EditText)findViewById(R.id.et_userName);
        etPassword = (EditText)findViewById(R.id.et_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        if (id == R.id.btn_login) {
            User user = getUser();
            if (isUserValid(user)) {
                ApiManager.userService().Login(user, new OnSessionCreated() {
                    @Override
                    public void onSuccess(String token) {
                        Session.getInstance().setToken(token);
                        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private boolean isUserValid(User user) {
        if(user.getmUserName().equals("") || user.getmPassword().equals("")){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.required_user_login), Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    public User getUser() {
        User user=new User();
        user.setmUserName(etUserName.getText().toString());
        user.setmPassword(etPassword.getText().toString());
        return user;
    }
}
