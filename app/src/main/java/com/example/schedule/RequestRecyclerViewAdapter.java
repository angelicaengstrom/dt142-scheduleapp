package com.example.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.MyViewHolder>{
    Context context;
    List<Pair<String,Shift>> shifts;
    public RequestRecyclerViewAdapter(Context context, List<Pair<String,Shift>> shifts){
        this.context = context;
        this.shifts = shifts;
    }

    @NonNull
    @Override
    public RequestRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_request, parent, false);
        return new RequestRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestRecyclerViewAdapter.MyViewHolder holder, int position) {
        String hoursStr = shifts.get(position).second.getStartTime() + "\n" + shifts.get(position).second.getStopTime();
        holder.hours.setText(hoursStr);
        String shiftStr = shifts.get(position).second.getShift();
        holder.shift.setText(shiftStr);
        String dateStr = shifts.get(position).second.getDateString();
        holder.date.setText(dateStr);
        holder.name.setText(shifts.get(position).first);

        holder.accept.setId(shifts.get(position).second.getId());
        holder.decline.setId(shifts.get(position).second.getId());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Du vill ha");

                String userID = ((Activity) context).getIntent().getStringExtra("id");
                if(userID != null){
                    builder.setMessage("POST HTTP REQUEST: ShiftID: " + view.getId() + " to userID: " + userID);
                    AlertDialog ad = builder.create();
                    ad.show();
                }
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Du vill INTE ha");
                String userID = ((Activity) context).getIntent().getStringExtra("id");
                if(userID != null){
                    builder.setMessage("DELETE HTTP REQUEST: ShiftID: " + view.getId() + " from userID: " + userID);
                    AlertDialog ad = builder.create();
                    ad.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hours, name, shift, date;
        ImageButton accept, decline;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hours = itemView.findViewById(R.id.txtRequestShiftHours);
            date = itemView.findViewById(R.id.txtRequestShiftDate);
            name = itemView.findViewById(R.id.txtRequestFrom);
            shift = itemView.findViewById(R.id.txtRequestShift);
            accept = itemView.findViewById(R.id.btnAcceptRequest);
            decline = itemView.findViewById(R.id.btnDeclineRequest);
        }
    }
}