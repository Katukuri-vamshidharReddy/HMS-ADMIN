package com.example.hmsadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllBedAdapter extends RecyclerView.Adapter<AllBedAdapter.myview> {
    Context context;
    List<Beds> bedlist = new ArrayList<>();
    String bedid, roomid;
    View v;
    BedInterface listener;


    public AllBedAdapter(Context context, List<Beds> bedlist, BedInterface listener) {
        this.context = context;
        this.bedlist = bedlist;
        this.listener = listener;
    }


    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_bed, parent, false);
        // set the view's size, margins, paddings and layout parameters
        myview vh = new myview(v); // pass the view to View Holder
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {

        Beds beds = bedlist.get(position);
        holder.txt.setText("Bed No : " + beds.getData2());
        bedid = beds.getData0();
        roomid = beds.getData1();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", beds.getData0());
                PopupMenu popup = new PopupMenu(context, holder.itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.updatebed:
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.bed_alert_dialog, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        view.getContext());

                                // set alert_dialog.xml to alertdialog builder
                                alertDialogBuilder.setView(promptsView);


                                final EditText userInput = (EditText) promptsView.findViewById(R.id.edtbedno);
                                userInput.setText(beds.getData2());
                                userInput.setInputType(InputType.TYPE_CLASS_NUMBER);

                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(true)
                                        .setPositiveButton("Update Bed", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // get user input and set it to result
                                                // edit text
                                                if (userInput.getText().toString().isEmpty()) {
                                                    Snackbar snackbar1 = Snackbar.make(v, "Enter Bed Number", Snackbar.LENGTH_SHORT);
                                                    snackbar1.show();

                                                } else {

                                                    new UpdateBed().execute(beds.getData0(), roomid, userInput.getText().toString());
                                                }
//

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

                                return true;
                            case R.id.deletebed:
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                                builder.setTitle("Do You Want To Remove Bed?");
                                builder.setCancelable(false);
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        new DeleteBed().execute(beds.getData0());
//                                        Intent intent = new Intent(context, AllBedActivity.class);
//                                        intent.putExtra("rid", roomid);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        context.startActivity(intent);
//                                        ((Activity) view.getContext()).finish();

                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return bedlist.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView txt;

        public myview(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.single_item_bed);
        }
    }

    public class UpdateBed extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.UpdateBeds(strings[0], strings[1], strings[2]);
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
                    Snackbar snackbar1 = Snackbar.make(v, "Bed Updated!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                    listener.foo();


                } else if (response.compareTo("already") == 0) {
                    Snackbar snackbar1 = Snackbar.make(v, "Bed Already Exits!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();


                } else {

                    Snackbar snackbar1 = Snackbar.make(v, "Bed Update Failed!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class DeleteBed extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {

                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.DeleteBeds(strings[0]);
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
                    Snackbar snackbar1 = Snackbar.make(v, "Bed Removed!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                    listener.foo();

                } else {
                    Snackbar snackbar1 = Snackbar.make(v, "Bed Remove Failed!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public interface BedInterface {
        public void foo();
    }

}
