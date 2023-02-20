package com.lucky.roadpilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class jobDetailsActivity extends AppCompatActivity {

    TextView owner_name, owner_phone, owner_location, Veh_des, driver_des, dialog_language, apply, vec, owner, text_lla, text_ll;

    String uid, driver, data, dat, call, image, name, location, comp, Uname, Uphone, Ulat, Ulog, Uloc, UImage, images, dl, cc, lat, lon, ids,num;
    RelativeLayout appy;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        driver_des = findViewById(R.id.driver_des);
        Veh_des = findViewById(R.id.Veh_des);
        apply = findViewById(R.id.apply);
        vec = findViewById(R.id.vec);
        owner = findViewById(R.id.owner);
        text_lla = findViewById(R.id.text_lla);
        text_ll = findViewById(R.id.text_ll);
        appy = findViewById(R.id.appy);
        owner_location = findViewById(R.id.owner_location);
        owner_phone = findViewById(R.id.owner_phone);
        owner_name = findViewById(R.id.owner_name);
        dialog_language = findViewById(R.id.dialog_language);

        owner_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(jobDetailsActivity.this, ProfileViewActivity.class);
                intent.putExtra("img", image);
                intent.putExtra("name", name);
                intent.putExtra("location", location);
                intent.putExtra("comp", comp);
                intent.putExtra("phone", call);
                intent.putExtra("Id", uid);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("images", images);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        driver = intent.getStringExtra("driver");
        data = intent.getStringExtra("data");
        dat = intent.getStringExtra("dat");
        ids = intent.getStringExtra("id");
        num = intent.getStringExtra("num");

        dialog_language.setText(dat);
        driver_des.setText(data);
        Veh_des.setText(driver);


        owner_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ((snapshot.child("MemberShip").getValue()).equals("None")) {


//                            if (MemberShip.equals("None")) {
//
                            Intent intent = new Intent(jobDetailsActivity.this, MembershipActivity.class);
                            intent.putExtra("cat", "Drivers");
                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
                        } else {
                            saveData();
                            apply.setText("Applied");
                            appy.setBackgroundColor(Color.parseColor("#26FF00"));
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + call));
                            startActivity(callIntent);
                        }
//
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

    });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if((snapshot.child("MemberShip").getValue()).equals("None")){


//                            if (MemberShip.equals("None")) {



                    //
                            Intent intent = new Intent(jobDetailsActivity.this,MembershipActivity.class);
                            intent.putExtra("cat","Drivers");
                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
                        }else {
                            saveData();
                            apply.setText("Applied");
                            appy.setBackgroundColor(Color.parseColor("#26FF00"));
                        }
//
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });


    }

    private void saveData() {

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference();


                        Long times = System.currentTimeMillis();



                        HashMap<String, Object> hashmap = new HashMap<>();

                        hashmap.put("number",call);
                        hashmap.put("time",time);
                        hashmap.put("Active","yes");
                        hashmap.put("id",ids);
                        hashmap.put("uid",mAuth.getCurrentUser().getUid());
                        hashmap.put("applied",uid);

                        DriverRef.child("Applied").child(String.valueOf(times)).setValue(hashmap);
                        apply.setClickable(false);

                        HashMap<String,Object> has1 = new HashMap<>();
                        has1.put("Applied","true");




                        DriverRef.child("Apply").child(ids).child(mAuth.getCurrentUser().getUid()).setValue(has1);
                        DriverRef.child("Jobs").child(ids).child(mAuth.getCurrentUser().getUid()).setValue(has1);

                        HashMap<String, Object> has = new HashMap<>();
                        has.put("uid", mAuth.getCurrentUser().getUid());
                        has.put("title", Uname + " applied for the Job");
                        has.put("time", time);
                        has.put("seen", "false");
                        has.put("id", String.valueOf(times));
                        has.put("Uname", Uname);
                        has.put("UImaage", UImage);
                        has.put("Uphone", Uphone);
                        has.put("Ulat", Ulat);
                        has.put("Ulog", Ulog);
                        has.put("location", Uloc);
                        has.put("dl", dl);
                        has.put("cc", cc);
                        has.put("key", "note_owner");

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("note_owner").child(uid).child(String.valueOf(times)).setValue(has).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                HashMap<String, Object> hashMap1 = new HashMap<>();
                                hashMap1.put("Id",String.valueOf(times));
                                hashMap1.put("time",time);
                                hashMap1.put("title","Applied For a Job  at" + Uloc);

                                DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                                references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                                        .setValue(hashMap1);
                            }
                        });

//                                HashMap<String, Object> has2 = new HashMap<>();
//                        has2.put("Active","yes");
//                        has2.put("Driver_data",driver);
//                        has2.put("Id",ids);
//                        has2.put("Location",location);
//                        has2.put("No_Wheels",dat);
//                        has2.put("Vehicle_Data",data);
//                        has2.put("number",num);
//                        has2.put("uid",uid);
//
//                        DriverRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Apply").child(ids).setValue(has2);


    }

    @Override
    protected void onStart() {
        super.onStart();

        LanguageChoose();

        driver_des.setText(data);
        Veh_des.setText(driver);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                call = ""+snapshot.child("phone").getValue();
                image = ""+snapshot.child("Image").getValue();
                name = ""+snapshot.child("name").getValue();
                location = ""+snapshot.child("Location").getValue();
                comp = ""+snapshot.child("Company_Name").getValue();
                images = ""+snapshot.child("FPan").getValue();
                lat = ""+snapshot.child("lat").getValue();
                lon = ""+snapshot.child("lon").getValue();


                owner_name.setText(""+snapshot.child("name").getValue());
                owner_location.setText(""+snapshot.child("Location").getValue());
//                if((snapshot.child("MemberShip").getValue()).equals("None")){
//
//
////                            if (MemberShip.equals("None")) {
////
////                    Intent intent = new Intent(jobDetailsActivity.this,MembershipActivity.class);
////                    intent.putExtra("cat","Drivers");
////                    startActivity(intent);
//                    owner_phone.setText("**********");
//////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
//                }else {
//                    owner_phone.setText(call);
//                }
//

//                owner_phone.setText(""+snapshot.child("phone").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
  DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        ref1.keepSynced(true);
        ref1.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Uname = ""+snapshot.child("name").getValue();
                UImage = ""+snapshot.child("Image").getValue();
                Uphone = ""+snapshot.child("phone").getValue();
                Ulat = ""+snapshot.child("lat").getValue();
                Ulog = ""+snapshot.child("lon").getValue();
                Uloc = ""+snapshot.child("Location").getValue();
                dl = ""+snapshot.child("DrivingF_Image").getValue();
                cc = ""+snapshot.child("Pan_Image").getValue();

                String Cat = ""+snapshot.child("Cat").getValue();

                if(Cat.equals("Owner")){
                    appy.setVisibility(View.INVISIBLE);
                    owner_phone.setVisibility(View.GONE);
                    owner_name.setClickable(false);
                }else {
                    appy.setVisibility(View.VISIBLE);
                    owner_phone.setVisibility(View.VISIBLE);
                    owner_name.setClickable(true);

                }


                if((snapshot.child("MemberShip").getValue()).equals("None")){


//                            if (MemberShip.equals("None")) {
//
//                    Intent intent = new Intent(jobDetailsActivity.this,MembershipActivity.class);
//                    intent.putExtra("cat","Drivers");
//                    startActivity(intent);
                    owner_phone.setText("**********");
////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
                }else {
                    owner_phone.setText(call);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Apply").child(ids).child(mAuth.getCurrentUser().getUid());
        DriverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds:snapshot.getChildren()){
                    if((""+snapshot.child("Applied").getValue()).equals("true")){

                        apply.setClickable(false);
                        apply.setText("Applied");
                        appy.setBackgroundColor(Color.parseColor("#26FF00"));
                    }else {
//                        Toast.makeText(jobDetailsActivity.this, "You have applied for the Job", Toast.LENGTH_SHORT).show();
                        apply.setClickable(true);
                        apply.setText("Apply");
                        appy.setBackgroundColor(Color.parseColor("#F40505"));
                    }

//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void LanguageChoose(){
        if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"en");
            resources =context.getResources();

            // Text In English

            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));



        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"hi");
            resources =context.getResources();
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));

        }
        else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"kn");
            resources =context.getResources();

            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"te");
            resources =context.getResources();
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));

            // Text in Telugu

        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"bn");
            resources =context.getResources();
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));

        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"gu");
            resources =context.getResources();
            text_ll.setText(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));



        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"ml");
            resources =context.getResources();

            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
//
        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"mr");
            resources =context.getResources();


            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));

        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"pa");
            resources =context.getResources();


            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));


        }else if(LocaleHelper.getLanguage(jobDetailsActivity.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(jobDetailsActivity.this,"ta");
            resources =context.getResources();

            // Text in Tamil

            text_ll.setHint(resources.getString(R.string.type_of_truck));
            vec.setHint(resources.getString(R.string.vehicle_information));
            owner.setHint(resources.getString(R.string.owner_information));
            text_lla.setHint(resources.getString(R.string.nature_of_job));

        }


    }

}