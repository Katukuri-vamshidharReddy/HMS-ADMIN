package com.example.hmsadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

public class AllBedActivity extends AppCompatActivity implements AllBedAdapter.BedInterface {
    //    List<Beds> bedlist = new ArrayList<>();
    RecyclerView recyclerView;
    AllBedAdapter adapter;

    String rid, bedno;
    FloatingActionButton fab;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bed);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonaddbed);
        layout = (ConstraintLayout) findViewById(R.id.bedlayout);


        Intent intent = getIntent();
        rid = intent.getStringExtra("rid");


        getSupportActionBar().setTitle("Beds");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_allbed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
//        adapter = new AllBedAdapter(getApplicationContext(), bedlist);
//        recyclerView.setAdapter(adapter);


//        new GetBeds().execute(rid);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.bed_alert_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        view.getContext());

                // set alert_dialog.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.edtbedno);
                userInput.setInputType(InputType.TYPE_CLASS_NUMBER);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("ADD BED", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                if (!userInput.getText().toString().isEmpty()) {
                                    Log.d("rid", rid);
                                    new AddBed().execute(rid, userInput.getText().toString());
//                                    Intent intent1 = new Intent(getApplicationContext(), AllBedActivity.class);
//                                    intent1.putExtra("rid", rid);
//                                    startActivity(intent1);
//                                    finish();
                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(layout, "Enter Bed Number", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    @Override
    public void foo() {
        new GetBeds().execute(rid);
    }

    void calladapter(List<Beds> bedlist) {
        adapter = new AllBedAdapter(getApplicationContext(), bedlist, this);
    }

    public class GetBeds extends AsyncTask<String, String, String> {
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
            Log.d("res", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String StatusValue = jsonObject.getString("status");
                if (StatusValue.compareTo("no") == 0) {
                    recyclerView.setAdapter(null);
                    Snackbar snackbar = Snackbar
                            .make(layout, "No Bed Added", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (StatusValue.compareTo("ok") == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        Beds bed =
                                new Beds(jsonObj.getString("data0"),
                                        jsonObj.getString("data1"),
                                        jsonObj.getString("data2"));
                        bedlist.add(bed);


                    }
                    calladapter(bedlist);
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

    public class AddBed extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.AddBeds(strings[0], strings[1]);
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
                            .make(layout, "Bed Added", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    new GetBeds().execute(rid);

                } else if (response.compareTo("already") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Bed Already Exits", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Adding Bed Failed!", Snackbar.LENGTH_LONG);
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
        new GetBeds().execute(rid);

    }
}
