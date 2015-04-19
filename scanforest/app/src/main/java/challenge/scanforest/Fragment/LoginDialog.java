package challenge.scanforest.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import challenge.scanforest.R;
import challenge.scanforest.api.ApiManager;
import challenge.scanforest.api.callbacks.OnSessionCreated;
import challenge.scanforest.models.User;
import challenge.scanforest.utils.Session;

/**
 * Created by Gustavo on 19/04/2015.
 */
public class LoginDialog extends DialogFragment implements View.OnClickListener {

    EditText etUserName;
    EditText etPassword;
    ProgressBar progressBar;
    LinearLayout form;

    private User user;

    public interface LoginDialogListener {
        public void onPositiveLogin(DialogFragment dialog);

        public void onRegisterRequest(DialogFragment dialog);
    }

    LoginDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_signin, null);

        Button btnRegister = (Button) view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        Button btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        Button cancel = (Button) view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        etUserName = (EditText) view.findViewById(R.id.et_userName);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_circular);
        form = (LinearLayout) view.findViewById(R.id.login_form);
        dialog.setContentView(view);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signin, container);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (LoginDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginDialogListener");
        }

    }

    private boolean isUserValid(User user) {
        if (user.getmUserName().equals("") || user.getmPassword().equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.required_user_login), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public User getUser() {
        User user = new User();
        user.setmUserName(etUserName.getText().toString());
        user.setmPassword(etPassword.getText().toString());
        return user;
    }

    public void SignIn() {
        User user = getUser();
        if (isUserValid(user)) {
            progressBar.setVisibility(View.VISIBLE);
            form.setVisibility(View.INVISIBLE);
            ApiManager.userService().Login(user, new OnSessionCreated() {
                @Override
                public void onSuccess(String token) {
                    if (!token.equals("")) {
                        Session.getInstance().setToken(token);
                        mListener.onPositiveLogin(LoginDialog.this);
                        getDialog().dismiss();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.an_error_occured), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    form.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    form.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_register) {
            mListener.onRegisterRequest(this);
            getDialog().dismiss();
        }
        if (id == R.id.btn_login) {
            SignIn();
        }
        if (id == R.id.btn_cancel) {
            getDialog().dismiss();
        }
    }
}
