package com.example.hmsadmin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText dateedt;
    Spinner spinner1, spinner2;

    String[] optionlist = {"All", "Room No", "Student Name"};
    String rid, sid;
    String todaydate;
    String date;
    ImageView img;
    RecyclerView recyclerView;
    AttendanceAdapter adapter;
    String roomid, studentid;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        img = (ImageView) findViewById(R.id.datepickerimg);
        layout = (RelativeLayout) findViewById(R.id.attendancelayout);

        getSupportActionBar().setTitle("Attendance");

        Intent intent = getIntent();
        roomid = intent.getStringExtra("rid");
        studentid = intent.getStringExtra("sid");


        dateedt = (EditText) findViewById(R.id.date_editext);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_attendance);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        todaydate = df.format(c);
        Log.d("date", todaydate);

        dateedt.setText(todaydate);

        if (roomid != null && studentid != null) {

            new GetAttendance().execute(dateedt.getText().toString(), roomid, studentid);

        } else {

            new GetAttendance().execute(dateedt.getText().toString(), "All", "All");
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int mYear, int mMonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */

                        Date d = new Date(mYear, mMonth, selectedday);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "20YY/MM/dd");
                        String strDate = dateFormatter.format(d);
                        Log.d("format", strDate);
                        dateedt.setText(strDate);
                        new GetAttendance().execute(strDate, "All", "All");
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();


            }
        });

        spinner1.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, optionlist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner1.setAdapter(aa);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_attendance);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_attendance);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        //  call the constructor of CustomAdapter to send the reference and data to Adapter
//        adapter = new AttendanceAdapter(attendanceList, getApplicationContext());
//        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item_position = String.valueOf(i);

        int positonInt = Integer.valueOf(item_position);

        if (studentid != null) {
            spinner1.setSelection(2);

        }

        if (positonInt == 0) {
            spinner2.setVisibility(View.GONE);

        }

        if (positonInt == 1) {
            recyclerView.setAdapter(null);
            spinner2.setVisibility(View.VISIBLE);

            new GetRooms().execute("All");
        }
        if (positonInt == 2) {
            recyclerView.setAdapter(null);
            spinner2.setVisibility(View.VISIBLE);

            new GetStudents().execute("All", "value");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


    public class GetRooms extends AsyncTask<String, String, String> implements AdapterView.OnItemSelectedListener {

        List<AllBedroom> bedroomlist = new ArrayList<>();

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
                    spinner2.setOnItemSelectedListener(this);
                    ArrayAdapter<AllBedroom> aa = new ArrayAdapter<AllBedroom>(getApplicationContext(), android.R.layout.simple_spinner_item, bedroomlist);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner2.setAdapter(aa);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            rid = bedroomlist.get(i).getData0();

            recyclerView.setAdapter(null);
            new GetAttendance().execute(dateedt.getText().toString(), rid, "All");


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class GetAttendance extends AsyncTask<String, String, String> {
        List<Attendance> attendanceList = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.AgetAttedance(strings[0], strings[1], strings[2]);
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
                String StatusValue = jsonObject.getString("status");
                if (StatusValue.compareTo("no") == 0) {

                    recyclerView.setAdapter(null);

                    Snackbar snackbar = Snackbar
                            .make(layout, "No Attendance Found!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (StatusValue.compareTo("ok") == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        Attendance att =
                                new Attendance(jsonObj.getString("data0"),
                                        jsonObj.getString("data1"),
                                        jsonObj.getString("data2"),
                                        jsonObj.getString("data3"),
                                        jsonObj.getString("data4"),
                                        jsonObj.getString("data5"),
                                        jsonObj.getString("data6"),
                                        jsonObj.getString("data7"));
                        attendanceList.add(att);

                    }


                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    adapter = new AttendanceAdapter(attendanceList, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


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

    public class GetStudents extends AsyncTask<String, String, String> implements AdapterView.OnItemSelectedListener {
        List<Students> studentsList = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.getStudent(strings[0], strings[1]);
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
                String StatusValue = jsonObject.getString("status");
                if (StatusValue.compareTo("no") == 0) {

                    Snackbar snackbar = Snackbar
                            .make(layout, "No Student Found!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (StatusValue.compareTo("ok") == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        Students students =
                                new Students(jsonObj.getString("data0"),
                                        jsonObj.getString("data1"),
                                        jsonObj.getString("data2"),
                                        jsonObj.getString("data3"),
                                        jsonObj.getString("data4"),
                                        jsonObj.getString("data5"),
                                        jsonObj.getString("data6"),
                                        jsonObj.getString("data7"),
                                        jsonObj.getString("data8"),
                                        jsonObj.getString("data9"));
                        studentsList.add(students);

                        spinner2.setOnItemSelectedListener(this);
                        ArrayAdapter<Students> aa = new ArrayAdapter<Students>(getApplicationContext(), android.R.layout.simple_spinner_item, studentsList);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner2.setAdapter(aa);

                        if (studentid != null) {
                            for (int j = 0; j < studentsList.size(); j++) {
                                String stuid = studentsList.get(j).getData0();
                                if (stuid.compareTo(studentid) == 0) {
                                    spinner2.setSelection(j);
                                    new GetAttendance().execute(dateedt.getText().toString(), roomid, studentid);

                                }
                            }

                        }

                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Something went Wrong!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            sid = studentsList.get(i).getData0();
            rid = studentsList.get(i).getData6();
            recyclerView.setAdapter(null);

            new GetAttendance().execute(dateedt.getText().toString(), rid, sid);


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


}