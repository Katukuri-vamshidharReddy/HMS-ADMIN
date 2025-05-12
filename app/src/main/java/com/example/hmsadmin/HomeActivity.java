package com.example.hmsadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {
    CardView cardroom, cardstudents, cardattendance;
    public static final String MYPRef = "mypref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cardattendance = (CardView) findViewById(R.id.cardview_viewattendance);
        cardstudents = (CardView) findViewById(R.id.cardview_managestudents);
        cardroom = (CardView) findViewById(R.id.cardview_managerrom);

        getSupportActionBar().setTitle("Home");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AttendanceActivity.class));
            }
        });

        cardroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        cardstudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StudentsActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.logout) {

            SharedPreferences sharedPreferences = getSharedPreferences(MYPRef, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("login", false);
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}