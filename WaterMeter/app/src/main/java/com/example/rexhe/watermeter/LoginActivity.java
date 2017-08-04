package com.example.rexhe.watermeter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rexhe.network.GetUserLogin;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    AutoCompleteTextView emailET;
    EditText passwordET;
    EditText busNoET;
    TextView registrationBtn;

    ProgressDialog progressDialog;
    HashMap<String, String> postDataParams;
    public static final String MY_PREFS_FILE = "loginInfo";
    private String email="";
    private String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailET= (AutoCompleteTextView) findViewById(R.id.email);
        passwordET= (EditText) findViewById(R.id.password);
        //busNoET= (EditText) findViewById(R.id.email_sign_in_button);
        registrationBtn= (TextView) findViewById(R.id.email_sign_in_button);
        registrationBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==registrationBtn)
        {
            attemptLogin();

        }
    }

    private void attemptLogin() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        emailET.setError(null);
        passwordET.setError(null);

        // Store values at the time of the login attempt.
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        //String busNo = busNoET.getText().toString();

        boolean cancel = false;
        boolean giveBusNo = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordET.setError(getString(R.string.error_invalid_password));
            focusView = passwordET;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailET.setError(getString(R.string.error_field_required));
            focusView = emailET;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailET.setError(getString(R.string.error_invalid_email));
            focusView = emailET;
            cancel = true;
        }

        /*if (busNo.length()>0)
        {
            giveBusNo=true;
        }*/

        if (cancel) {
            Toast.makeText(getApplicationContext(), "Fill Up Required Info",Toast.LENGTH_LONG).show();
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            //focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.


            postDataParams = new LinkedHashMap<>();
            postDataParams.put("email",email);
            postDataParams.put("password",password);
            executeInfo(postDataParams);

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void executeInfo(HashMap<String,String> hashMapInfo)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ProgressBar v = (ProgressBar) progressDialog.findViewById(android.R.id.progress);
                v.getIndeterminateDrawable().setColorFilter(0xFF00BFFF,
                        android.graphics.PorterDuff.Mode.MULTIPLY);

            }
        });
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        GetUserLogin getUserLogin = new GetUserLogin(LoginActivity.this,progressDialog, hashMapInfo);    // Call the CalculationByDistance
        getUserLogin.execute();
    }

    public void alertDialog(String message)
    {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.loginLayout), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        txtv.setTextColor(Color.CYAN);
    }

    public void saveInSharedPreference()
    {
        if (email!=""&& password!="") {
            SharedPreferences shared = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("email", email);
            editor.putString("password",password);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Succeed", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}
