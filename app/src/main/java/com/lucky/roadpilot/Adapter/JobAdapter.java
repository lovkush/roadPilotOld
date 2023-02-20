package com.lucky.roadpilot.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.roadpilot.Filters.JobFilter;
import com.lucky.roadpilot.Models.JobModel;
import com.lucky.roadpilot.R;
import com.lucky.roadpilot.UpdateJobDetails;
import com.lucky.roadpilot.jobDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobHolder> implements Filterable {

    Context context;
    public ArrayList<JobModel> arrayList, filterList;
    private JobFilter jobFilter;
    private FirebaseAuth mAuth;

    public JobAdapter(Context context, ArrayList<JobModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filterList = arrayList;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.job_row, parent, false);
        return new JobHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHolder holder, int position) {

        JobModel model = arrayList.get(position);

        String uid = model.getUid();
        String time = model.getTime();
        String driver = model.getDriver_data();
        String data = model.getVehicle_Data();
        String number = model.getNumber();
        String ids = model.getId();
        String dat = model.getNo_Wheels();
        holder.post_time.setText(time);
        holder.required.setText(number);
        holder.nature.setText(data);
        holder.wheels.setText(dat);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        ref1.child("Jobs").child(ids).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if((""+snapshot.child("Active").getValue()).equals("yes")){
                    holder.itemView.setClickable(true);
                    holder.job.setBackgroundColor(Color.parseColor("#26FF00"));
                }else if((""+snapshot.child("Active").getValue()).equals("no")){
                    holder.job.setBackgroundColor(Color.parseColor("#F40505"));
                    holder.itemView.setClickable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                holder.location.setText(""+snapshot.child("Location").getValue());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uid.equals(mAuth.getCurrentUser().getUid())){
                    Intent intent = new Intent(context, UpdateJobDetails.class);
                    intent.putExtra("uid",uid);
                    intent.putExtra("driver",driver);
                    intent.putExtra("data",data);
                    intent.putExtra("dat",dat);
                    intent.putExtra("id",ids);
                    intent.putExtra("num",number);
                    context.startActivity(intent);
                }else {

                    Intent intent = new Intent(context, jobDetailsActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("driver", driver);
                    intent.putExtra("data", data);
                    intent.putExtra("dat", dat);
                    intent.putExtra("id", ids);
                    intent.putExtra("num", number);
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {

        if(jobFilter == null){
            jobFilter =   new JobFilter(this, filterList);
        }

        return jobFilter;

    }


    public static class JobHolder extends RecyclerView.ViewHolder {

        RelativeLayout job;
        TextView location, required, post_time,wheels,nature;


        public JobHolder(@NonNull View itemView) {
            super(itemView);

            job = itemView.findViewById(R.id.job);
            location = itemView.findViewById(R.id.location);
            required = itemView.findViewById(R.id.required);
            post_time = itemView.findViewById(R.id.post_time);
            wheels = itemView.findViewById(R.id.wheels);
            nature = itemView.findViewById(R.id.nature);
        }
    }
}
