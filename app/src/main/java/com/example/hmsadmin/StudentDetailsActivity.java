package com.example.hmsadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
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

public class StudentDetailsActivity extends AppCompatActivity {
    String sid, sname, semail, scontact, secontact, sadd, rid, bid, pass;
    String roomid, bedid;

    EditText name, email, contact, emergencycontact, add;

    Spinner bedrromspinnerd, bedspinnerd;
    Button updatebtn, deletebtn, removeassignment;

    List<AllBedroom> bedroomlist = new ArrayList<>();
    ScrollView layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);


        name = (EditText) findViewById(R.id.sname_edtd);
        email = (EditText) findViewById(R.id.semail_edtd);
        contact = (EditText) findViewById(R.id.scontact_edtd);
        emergencycontact = (EditText) findViewById(R.id.scontactemergency_edtd);
        add = (EditText) findViewById(R.id.sadd_editextd);
        updatebtn = (Button) findViewById(R.id.student_update_btn);
        deletebtn = (Button) findViewById(R.id.student_delete_btn);
        removeassignment = (Button) findViewById(R.id.remove_assignmentbtn);
        layout = (ScrollView) findViewById(R.id.sdlayout);

        bedrromspinnerd = (Spinner) findViewById(R.id.spinner_getbedroomsd);
        bedspinnerd = (Spinner) findViewById(R.id.spinner_getbedsd);


        getSupportActionBar().setTitle("Student Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        sname = intent.getStringExtra("sname");
        semail = intent.getStringExtra("semail");
        scontact = intent.getStringExtra("scontact");
        secontact = intent.getStringExtra("secontact");
        sadd = intent.getStringExtra("sadd");
        rid = intent.getStringExtra("rid");
        bid = intent.getStringExtra("bid");
        pass = intent.getStringExtra("pass");


        name.setText(sname);
        email.setText(semail);
        contact.setText(scontact);
        emergencycontact.setText(secontact);
        add.setText(sadd);


        removeassignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removealertassignment();

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletealertdialog();
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatealertdialog();


            }
        });

//        new Getbedrooms().execute(rid);

    }

    public class RemoveAssignment extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.Remove_Assignment(strings[0]);
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
                    Snackbar snackbar = Snackbar
                            .make(layout, "Assignment Removed!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Remove Assignment Failed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void updatealertdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do You Want To Update Student?");
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (isValidate()) {
                    new UpdateStudents().execute(sid, name.getText().toString(), email.getText().toString(),
                            contact.getText().toString(), add.getText().toString(),
                            emergencycontact.getText().toString(), roomid, bedid);
                }

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deletealertdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do You Want To Remove Student?");
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                new DeleteStudent().execute(sid);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public class UpdateStudents extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.UpdateStudent(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("resu", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("true") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Student Updated!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    finish();
                } else if (response.compareTo("Email") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Email ALready Exits", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (response.compareTo("Contact") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Contact Already Exits!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (response.compareTo("Room") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Number Already Exits!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Something Went Wrong!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class DeleteStudent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.DeleteStudent(strings[0]);
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
                    Snackbar snackbar = Snackbar
                            .make(layout, "Student Removed!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Student Not Removed!", Snackbar.LENGTH_LONG);
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


                    JSONArray array = jsonObject.getJSONArray("Data");
                    Log.d("data", array.toString());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject json = array.getJSONObject(i);
                        String bedr = json.getString("data2");

                        AllBedroom bedroom = new AllBedroom(json.getString("data0"),
                                json.getString("data1"),
                                json.getString("data2"),
                                json.getString("data3"));

                        bedroomlist.add(bedroom);

                    }


                    bedrromspinnerd.setOnItemSelectedListener(this);
                    ArrayAdapter<AllBedroom> aa = new ArrayAdapter<AllBedroom>(getApplicationContext(), android.R.layout.simple_spinner_item, bedroomlist);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    bedrromspinnerd.setAdapter(aa);

                    for (int j = 0; j < bedroomlist.size(); j++) {
                        String ridn = bedroomlist.get(j).getData0();
                        if (ridn.compareTo(rid) == 0) {
                            bedrromspinnerd.setSelection(j);
                            new Getbeds().execute(roomid);
                        }


                    }


                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Could not load Bedroom", Snackbar.LENGTH_LONG);
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

            if (roomid != null) {
                new Getbeds().execute(roomid);
            } else {
                new Getbeds().execute(rid);
            }

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


                    bedspinnerd.setOnItemSelectedListener(this);
                    ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, bedlist);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bedspinnerd.setAdapter(adapter1);
//
                    for (int k = 0; k < bedlist.size(); k++) {
                        String bidn = bedlist.get(k).getData0();
                        if (bidn.compareTo(bid) == 0) {
                            bedspinnerd.setSelection(k);
                        }
                    }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attendancemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.attendanceview:
                Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                intent.putExtra("sid", sid);
                intent.putExtra("rid", rid);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Invalid Email", Snackbar.LENGTH_LONG);
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
        if (add.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Address", Snackbar.LENGTH_LONG);
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
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Getbedrooms().execute(rid);
    }

    void removealertassignment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do You Want To Remove Assignment?");
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new RemoveAssignment().execute(sid);


            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}