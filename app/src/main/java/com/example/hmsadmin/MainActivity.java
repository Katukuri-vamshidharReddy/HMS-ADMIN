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

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addimg;
    //    List<AllRooms> roomsList = new ArrayList<>();
    RecyclerView recyclerView;
    AllRoomAdapter adapter;
    ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Rooms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout = (ConstraintLayout) findViewById(R.id.layoutmain);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_allroom);
        addimg = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
//        adapter = new AllRoomAdapter(getApplicationContext(), roomsList);
//        recyclerView.setAdapter(adapter);

//        getrooms();


        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), AddRoomActivity.class));

            }
        });

    }


    private void getrooms() {
        new GetRooms().execute("All");
    }

    public class GetRooms extends AsyncTask<String, String, String> {
        List<AllRooms> roomsList = new ArrayList<>();

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
            Log.d("resp", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String StatusValue = jsonObject.getString("status");
                if (StatusValue.compareTo("no") == 0) {
                    recyclerView.setAdapter(null);
                    Snackbar snackbar = Snackbar
                            .make(layout, "No Room Added Right Now", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (StatusValue.compareTo("ok") == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        AllRooms rooms =
                                new AllRooms(jsonObj.getString("data0"),
                                        jsonObj.getString("data1"),
                                        jsonObj.getString("data2"),
                                        jsonObj.getString("data3"));
                        roomsList.add(rooms);

                    }

                    adapter = new AllRoomAdapter(getApplicationContext(), roomsList);
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
        new GetRooms().execute("All");
    }
}





