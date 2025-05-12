package com.example.hmsadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText emailedt, passedt;
    Button login;
    ProgressBar pb;
    public static final String MYPRef = "mypref";
    RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailedt = (EditText) findViewById(R.id.emaileditext);
        passedt = findViewById(R.id.passesditext);
        login = (Button) findViewById(R.id.loginbtn);
        pb = (ProgressBar) findViewById(R.id.progresslogin);
        layout= (RelativeLayout) findViewById(R.id.loginlayout);

        getSupportActionBar().setTitle("Admin Login");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidate()) {
                    new Login().execute(emailedt.getText().toString(), passedt.getText().toString());
                    pb.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private boolean isValidate() {
        if (emailedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Username", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (passedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    public class Login extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.Alogin(strings[0], strings[1]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("res", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("true") == 0) {

                    pb.setVisibility(View.INVISIBLE);

                    SharedPreferences sharedPreferences = getSharedPreferences(MYPRef, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("login", true);

                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Wrong Password or Email ", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}