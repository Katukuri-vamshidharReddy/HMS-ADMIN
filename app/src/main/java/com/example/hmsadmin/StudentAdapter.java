package com.example.hmsadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.myview> {
    List<AllStudents> studentlist = new ArrayList<>();
    Context context;

    public StudentAdapter(List<AllStudents> studentlist, Context context) {
        this.studentlist = studentlist;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_allstudents, parent, false);
        // set the view's size, margins, paddings and layout parameters
        myview vh = new myview(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        AllStudents students = studentlist.get(position);
        holder.name.setText("Name - " + students.getData1());
        holder.email.setText("Email - " + students.getData2());
        holder.contact.setText("contact - " + students.getData3());
//        holder.room.setText("Room No - " + students.getData8());
//        holder.bed.setText("Bed No - " + students.getData9());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentDetailsActivity.class);
                intent.putExtra("sid", students.getData0());
                intent.putExtra("sname", students.getData1());
                intent.putExtra("semail", students.getData2());
                intent.putExtra("scontact", students.getData3());
                intent.putExtra("sadd", students.getData4());
                intent.putExtra("secontact", students.getData5());
                intent.putExtra("rid", students.getData6());
                intent.putExtra("bid",students.getData8());
//                intent.putExtra("bid", students.getData7());
//                intent.putExtra("pass", students.getData8());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentlist.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView name, email, contact, room, bed;

        public myview(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentname_tv);
            email = itemView.findViewById(R.id.studentemail_tv);
            contact = itemView.findViewById(R.id.studentcontact_tv);
//            room = itemView.findViewById(R.id.studentroom_tv);
//            bed = itemView.findViewById(R.id.studentbed_tv);
        }
    }
}
