package com.example.hmsadmin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity {
    FloatingActionButton fab;
    //    List<AllStudents> studentsList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter adapter;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        layout = (ConstraintLayout) findViewById(R.id.studentlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_allstudents);

        getSupportActionBar().setTitle("Students");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonaddstudent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddStudentActivity.class));
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
//        adapter = new StudentAdapter(studentsList, getApplicationContext());
//        recyclerView.setAdapter(adapter);

//        new GetStudent().execute("All", "value");

    }

    public class GetStudent extends AsyncTask<String, String, String> {
        List<AllStudents> studentsList = new ArrayList<>();

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
            Log.d("response", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String StatusValue = jsonObject.getString("status");
                if (StatusValue.compareTo("no") == 0) {
                    recyclerView.setAdapter(null);
                    Snackbar snackbar = Snackbar
                            .make(layout, "No Student Added!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (StatusValue.compareTo("ok") == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        AllStudents students =
                                new AllStudents(jsonObj.getString("data0"),
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

                    }

                    adapter = new StudentAdapter(studentsList, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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

    @Override
    protected void onResume() {
        super.onResume();
        new GetStudent().execute("All", "value");
    }
}