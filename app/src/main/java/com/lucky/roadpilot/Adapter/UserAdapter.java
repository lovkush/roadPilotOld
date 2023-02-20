package com.lucky.roadpilot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.roadpilot.Filters.UserFilter;
import com.lucky.roadpilot.Models.UserModel;
import com.lucky.roadpilot.ProfileViewActivity;
import com.lucky.roadpilot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> implements Filterable {

    Context context;
   public  ArrayList<UserModel> models, filterList;
    private UserFilter jobFilter;
    private FirebaseAuth mAuth;
    private String ima;

    public UserAdapter(Context context, ArrayList<UserModel> models) {
        this.context = context;
        this.models = models;
        this.filterList = models;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        UserModel model = models.get(position);

        String names = model.getName();
        String cats = model.getCat();
        String img = model.getImage();
        String phones = model.getPhone();
        String Id = model.getUid();
        String location = model.getLocation();
        double  lon = model.getLon();
        double  lat = model.getLat();
        String comp = model.getCompany_Name();
        String cc = model.getPan_Image();
        String dl = model.getDrivingF_Image();

        String latitude = String.valueOf(lat);
        String longitude = String.valueOf(lon);

        holder.cat.setText(cats);
        holder.name.setText(names);

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel: " + phones));
                context.startActivity(callIntent);
            }
        });

        try{
            Picasso.get().load(img).placeholder(R.drawable.profile).into(holder.Image);

        }catch (Exception e){
            holder.Image.setImageResource(R.drawable.profile);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                for(DataSnapshot ds:snapshot.getChildren()) {
//                    if (("" + ds.child("Cat").getValue()).equals("Mech") || ("" + ds.child("shop").getValue()).equals("Open")) {
//                        holder.cat.setTextColor(Color.parseColor("#26FF00"));
//                    } else if (("" + ds.child("Cat").getValue()).equals("Mech") || ("" + ds.child("shop").getValue()).equals("Close")) {
//                        holder.cat.setTextColor(Color.parseColor("#F40505"));
//
//                    }
                    if (("" + snapshot.child("shop").getValue()).equals("Closed")) {
                        holder.cat.setTextColor(Color.parseColor("#F40505"));
                        holder.itemView.setClickable(false);

                    } else if (("" + snapshot.child("shop").getValue()).equals("Open")) {
                        holder.cat.setTextColor(Color.parseColor("#26FF00"));
                        holder.itemView.setClickable(true);


                    } else {
                        holder.itemView.setClickable(true);
                        holder.cat.setTextColor(Color.parseColor("#717070"));
                    }

                    if((""+snapshot.child("Cat").getValue()).equals("Owner")){
                        ima = model.getFPan();
                    }else if((""+snapshot.child("Cat").getValue()).equals("Drivers")){
                        ima = model.getDrivingF_Image();
                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileViewActivity.class);
                intent.putExtra("Id",Id);
                intent.putExtra("phone",phones);
                intent.putExtra("name",names);
                intent.putExtra("img",img);
                intent.putExtra("lat",latitude);
                intent.putExtra("lon",longitude);
                intent.putExtra("comp",comp);
                intent.putExtra("location",location);
                intent.putExtra("images",dl);
                intent.putExtra("cc",cc);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public Filter getFilter() {
        if(jobFilter == null){
            jobFilter =  new UserFilter(this, filterList);
        }

        return jobFilter;
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView Image,phone;
        TextView name,cat;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            Image = itemView.findViewById(R.id.Image);
            phone = itemView.findViewById(R.id.phone);
            cat = itemView.findViewById(R.id.cat);

        }
    }
}
