package com.example.hmsadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.myview> {
    List<Attendance> attendanceList = new ArrayList<>();
    Context context;

    public AttendanceAdapter(List<Attendance> attendanceList, Context context) {
        this.attendanceList = attendanceList;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_attendance, parent, false);
        // set the view's size, margins, paddings and layout parameters
        myview vh = new myview(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.name.setText(attendance.getData2());
        holder.type.setText("Type : " + attendance.getData7());
        holder.time.setText("Time : " + attendance.getData6());
        holder.rromno.setText("Room no : " + attendance.getData1());

    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView name, rromno, time, type;

        public myview(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Astudentname_tv);
            rromno = itemView.findViewById(R.id.Aroomno_tv);
            time = itemView.findViewById(R.id.Atime_tv);
            type = itemView.findViewById(R.id.Atype_tv);
        }
    }
}
