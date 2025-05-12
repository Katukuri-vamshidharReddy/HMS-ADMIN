package com.example.hmsadmin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    EditText name, email, contact, emergencycontact, add;
    Button addbtn;
    Spinner bedrromspinner, bedspinner;
//    List<Beds> bedlist = new ArrayList<>();
//    List<String> bedlist = new ArrayList<String>();

    List<AllBedroom> bedroomlist = new ArrayList<>();
    String roomid, bedid;
    ScrollView layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getSupportActionBar().setTitle("Add Student");
        name = (EditText) findViewById(R.id.sname_edt);
        email = (EditText) findViewById(R.id.semail_edt);
        contact = (EditText) findViewById(R.id.scontact_edt);
        emergencycontact = (EditText) findViewById(R.id.scontactemergency_edt);
        add = (EditText) findViewById(R.id.sadd_editext);
        addbtn = (Button) findViewById(R.id.addbutton_student);
        bedrromspinner = (Spinner) findViewById(R.id.spinner_getbedrooms);
        bedspinner = (Spinner) findViewById(R.id.spinner_getbeds);
        layout = (ScrollView) findViewById(R.id.saddlayout);


        getAllbedroms();


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {

                    new AddStudent().execute(name.getText().toString(), email.getText().toString(),
                            contact.getText().toString(), add.getText().toString(), emergencycontact.getText().toString(),
                            roomid, bedid);

                }

            }
        });


    }

    private void getAllbedroms() {
        new Getbedrooms().execute("Bedroom");

    }

    private boolean isValidate() {
        if (name.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Email", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (contact.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Contact", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (emergencycontact.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Emergency Contact", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (contact.getText().toString().length() != 10) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Invalid Contact", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (emergencycontact.getText().toString().length() != 10) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Invalid Contact", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Invalid Email", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (add.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Address", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }


    public class AddStudent extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.AddStudent(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6]);
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
                if (response.compareTo("already") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Already Exits", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (response.compareTo("true") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Student Added!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    finish();
                } else if (response.compareTo("Email") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Email Already Exits!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else if (response.compareTo("Contact") == 0) {

                    Snackbar snackbar = Snackbar
                            .make(layout, "Contact Already Exits!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (response.compareTo("Room") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Already Exists!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Something Went Wrong", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class Getbedrooms extends AsyncTask<String, String, String>
            implements AdapterView.OnItemSelectedListener {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.getRooms(strings[0]);
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
                if (response.compareTo("ok") == 0) {

//                    List<String> bedlist = new ArrayList<String>();

                    JSONArray array = jsonObject.getJSONArray("Data");
                    Log.d("data", array.toString());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject json = array.getJSONObject(i);
                        String bedr = json.getString("data2");

//                        rid = json.getString("data0");

//                        bedroomlist.add("Room No - " + bedr);
                        AllBedroom bedroom = new AllBedroom(json.getString("data0"),
                                json.getString("data1"),
                                json.getString("data2"),
                                json.getString("data3"));
                        bedroomlist.add(bedroom);

                    }


                    bedrromspinner.setOnItemSelectedListener(this);
                    ArrayAdapter<AllBedroom> aa = new ArrayAdapter<AllBedroom>(getApplicationContext(), android.R.layout.simple_spinner_item, bedroomlist);

                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bedrromspinner.setAdapter(aa);


                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Could not Load Bedroom", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            AllBedroom bedroom = bedroomlist.get(i);
            roomid = bedroom.getData0();
            new Getbeds().execute(roomid);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {


        }
    }

    public class Getbeds extends AsyncTask<String, String, String> implements AdapterView.OnItemSelectedListener {
        List<Beds> bedlist = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.getBeds(strings[0]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("respp", s);
//            List<String> bedlist = new ArrayList<String>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("ok") == 0) {


                    JSONArray Arr = jsonObject.getJSONArray("Data");
                    Log.d("data", Arr.toString());

                    for (int i = 0; i < Arr.length(); i++) {
                        JSONObject json = Arr.getJSONObject(i);
                        String bedr = json.getString("data2");

                        Beds bed = new Beds(json.getString("data0"), json.getString("data1"),
                                json.getString("data2"));
                        bedlist.add(bed);

//                        bedlist.add("Bed No - " + bedr);

                    }


                    bedspinner.setOnItemSelectedListener(this);
                    ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, bedlist);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bedspinner.setAdapter(adapter1);


                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Could not Load Bedroom", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            bedid = bedlist.get(i).getData0().toString();

            Log.d("id", bedid);
//
//


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}
