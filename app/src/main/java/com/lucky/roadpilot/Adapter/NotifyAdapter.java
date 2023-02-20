package com.lucky.roadpilot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.roadpilot.Models.NoteModel;
import com.lucky.roadpilot.ProfileViewActivity;
import com.lucky.roadpilot.R;
import com.lucky.roadpilot.jobDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyHolder> {

    Context context;
    ArrayList<NoteModel> arrayList;
    private String Data;


    public NotifyAdapter(Context context, ArrayList<NoteModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notify,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        NoteModel model = arrayList.get(position);

        String uid = model.getUid();
        String time = model.getTime();
//        String location = model.getLocation();
        String title = model.getTitle();

        holder.time.setText(time);
        holder.notify_mgs.setText(title);

//        final String[] cat = new String[1];
//        final String[] name = new String[1];
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds: snapshot.getChildren()){
//                    if ((""+ds.child("uid")).equals(uid)){
//                         cat[0] = ""+ds.child("Cat").getValue();
//                         name[0] = ""+ds.child("name").getValue();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
//        ref1.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds: snapshot.getChildren()){
//                    if ((""+ds.child("uid")).equals(uid)){
//                         String ver = ""+ds.child("Verified");
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//

//        if(cat.equals("Owner")){
//            holder.notify_mgs.setText("Congratulations, Your Post has been posted successfully");
//        }else if(cat.equals("Drivers")){
//            holder.notify_mgs.setText(name + "");
//
//        }
        String location = model.getLocation();
        String phones = model.getUphone();
        String names = model.getUname();
        String img = model.getUImaage();
        String lat = model.getUlat();
        String lon = model.getUlog();
        String dl = model.getDl();
        String id = model.getId();
        String key = model.getKey();
        String cc = model.getCc();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(holder.auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if((""+snapshot.child("Cat").getValue()).equals("Owner")){
                    Data = "note_owner";
                }else if((""+snapshot.child("Cat").getValue()).equals("Drivers")){
                    Data = "note_driver";
                }else if((""+snapshot.child("Cat").getValue()).equals("Dhaba")){
                    Data = "note_dhaba";
                }else if((""+snapshot.child("Cat").getValue()).equals("Mech")){
                    Data = "note_mech";
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(key).child(holder.auth.getCurrentUser().getUid()).child(id)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((""+snapshot.child("seen").getValue()).equals("false")){
                    holder.note_lay.setBackgroundColor(Color.parseColor("#26FF00"));
                }else {
                    holder.note_lay.setBackgroundColor(Color.parseColor("#FFFFFF"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.contains("applied for the Job")){
                    Intent intent = new Intent(context, ProfileViewActivity.class);
                    intent.putExtra("Id",id);
                    intent.putExtra("phone",phones);
                    intent.putExtra("name",names);
                    intent.putExtra("img",img);
                    intent.putExtra("lat",lat);
                    intent.putExtra("lon",lon);
                    intent.putExtra("comp","");
                    intent.putExtra("location",location);
                    intent.putExtra("images",dl);
                    intent.putExtra("cc",cc);
                    context.startActivity(intent);
                }else
                    if(title.contains("posted a job")){
                    Intent intent = new Intent(context, jobDetailsActivity.class);

                    intent.putExtra("uid",uid);
                    intent.putExtra("driver",lon);
                    intent.putExtra("data",lat);
                    intent.putExtra("dat",cc);
                    intent.putExtra("id",id);
                    context.startActivity(intent);


                }


                HashMap<String,Object> has = new HashMap<>();
                has.put("seen","true");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child(key).child(holder.auth.getCurrentUser().getUid()).child(id).updateChildren(has);


            }
        });



    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView time,notify_mgs;
        private CardView note_lay;
        FirebaseAuth auth;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            notify_mgs = itemView.findViewById(R.id.notify_mgs);
            note_lay = itemView.findViewById(R.id.note_lay);
            auth = FirebaseAuth.getInstance();


        }
    }

}
