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

public class AllRoomAdapter extends RecyclerView.Adapter<AllRoomAdapter.myview> {
    Context context;
    List<AllRooms> roomsList = new ArrayList<>();

    public AllRoomAdapter(Context context, List<AllRooms> roomsList) {
        this.context = context;
        this.roomsList = roomsList;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_allrooms, parent, false);
        // set the view's size, margins, paddings and layout parameters
        myview vh = new myview(v); // pass the view to View Holder
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        AllRooms rooms = roomsList.get(position);
        holder.roomtype.setText("Type - " + rooms.getData1());
        holder.roomno.setText("Room No - " + rooms.getData2());
        holder.roomname.setText(rooms.getData3());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Intent intent = new Intent(context.getApplicationContext(), RoomDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("roomname", rooms.getData3());
                intent.putExtra("roomno", rooms.getData2());
                intent.putExtra("roomtype", rooms.getData1());
                intent.putExtra("roomid", rooms.getData0());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView roomname, roomno, roomtype;

        public myview(@NonNull View itemView) {
            super(itemView);
            roomname = itemView.findViewById(R.id.roomname_tv);
            roomno = itemView.findViewById(R.id.roomno_tv);
            roomtype = itemView.findViewById(R.id.roomtype_tv);
        }
    }
}
