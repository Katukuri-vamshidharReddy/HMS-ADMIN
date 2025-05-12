package com.example.hmsadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRoomActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    Spinner spinner;
    EditText editTextroomno, editTextroomname;
    Button addbtn;
    String[] country = {"General", "Bedroom"};
    String roomtype;
    ProgressBar pb;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        spinner = (Spinner) findViewById(R.id.spinner);
        pb = (ProgressBar) findViewById(R.id.progressbar_addroom);
        editTextroomname = (EditText) findViewById(R.id.edtroomname);
        editTextroomno = (EditText) findViewById(R.id.edtroomno);
        addbtn = (Button) findViewById(R.id.addbutton);
        layout = (RelativeLayout) findViewById(R.id.layoutaddroom);
        getSupportActionBar().setTitle("Add Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    new AddRoom().execute(roomtype, editTextroomno.getText().toString(), editTextroomname.getText().toString());
                    pb.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        roomtype = country[i];
        Log.d("res", roomtype);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        roomtype = "General";
        Log.d("res", roomtype);

    }

    private boolean isValidate() {
        if (editTextroomno.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Room Number", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (editTextroomname.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Room Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    public class AddRoom extends AsyncTask<String, String, String> {
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
                JSONObject json = restAPI.AddRooms(strings[0], strings[1], strings[2]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("response", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("true") == 0) {
                    pb.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddRoomActivity.this);

                    builder.setTitle("Room Added Successfully");
                    builder.setCancelable(true);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editTextroomname.getText().clear();
                            editTextroomno.getText().clear();
                            dialog.dismiss();


                        }
                    });
                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            finish();

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                } else if (response.compareTo("already") == 0) {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Already Exits", Snackbar.LENGTH_LONG);
                    snackbar.show();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    // finish();

                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Not Added!", Snackbar.LENGTH_LONG);
                    snackbar.show();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //  finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}