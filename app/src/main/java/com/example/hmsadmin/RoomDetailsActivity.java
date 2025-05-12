package com.example.hmsadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomDetailsActivity extends AppCompatActivity {
    EditText edtname, edttype, edtno;
    String rname, rtype, rno, rid;
    Button fabbed;
    Button updatebtn, deletebtn;
    Button bedbtn;
    ProgressBar pb;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        edtname = (EditText) findViewById(R.id.editextroom_name);
        edttype = (EditText) findViewById(R.id.editextroom_type);
        edtno = (EditText) findViewById(R.id.editextroom_no);
        fabbed = (Button) findViewById(R.id.fab_bed);
        updatebtn = (Button) findViewById(R.id.updateroom_btn);
        deletebtn = (Button) findViewById(R.id.deleteroom_btn);
        bedbtn = (Button) findViewById(R.id.allbeds);
        pb = (ProgressBar) findViewById(R.id.progressbar_roomdaetails);
        layout = (RelativeLayout) findViewById(R.id.rdlayout);

        getSupportActionBar().setTitle("Room Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        rname = intent.getStringExtra("roomname");
        rtype = intent.getStringExtra("roomtype");
        rno = intent.getStringExtra("roomno");
        rid = intent.getStringExtra("roomid");

        edttype.setText(rtype);
        edtno.setText(rno);
        edtname.setText(rname);

        if (rtype.compareTo("Bedroom") == 0) {
            fabbed.setVisibility(View.VISIBLE);
//            bedbtn.setVisibility(View.VISIBLE);
        }
        fabbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), AllBedActivity.class);
                intent1.putExtra("rid", rid);
                startActivity(intent1);

            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdiaaglogupdate();

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdiaaglogdelete();


            }
        });


    }


    private void alertdiaaglogupdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do You Want To Update Room?");
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (isValidate()) {
                    pb.setVisibility(View.VISIBLE);
                    new UpdateRoom().execute(rid, edttype.getText().toString(), edtno.getText().toString(), edtname.getText().toString());
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void alertdiaaglogdelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Do You Want To Remove Room?");
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                pb.setVisibility(View.VISIBLE);
                new DeleteRoom().execute(rid);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public class UpdateRoom extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.UpdateRooms(strings[0], strings[1], strings[2], strings[3]);
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
                    pb.setVisibility(View.GONE);

                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Updated!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    finish();

                } else if (response.compareTo("already") == 0) {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Already Exits! ", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Not Updated!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class DeleteRoom extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.DeleteRooms(strings[0]);
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
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Room Removed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    finish();
                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Remove Room Failed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isValidate() {
        if (edtname.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Room Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }


        if (edtno.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Room Number", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

}