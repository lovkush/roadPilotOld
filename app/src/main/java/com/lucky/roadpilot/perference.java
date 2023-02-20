package com.lucky.roadpilot;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lucky.roadpilot.Adapter.JobAdapter;
import com.lucky.roadpilot.Models.JobModel;
import com.lucky.roadpilot.Registers.CarCerActivity;
import com.lucky.roadpilot.Registers.DrivingLBActivity;
import com.lucky.roadpilot.Registers.DrivingLFActivity;
import com.lucky.roadpilot.Registers.OwnerAdharBackActivity;
import com.lucky.roadpilot.Registers.OwnerAdharFrontActivity;
import com.lucky.roadpilot.Registers.OwnerFirmActivity;
import com.lucky.roadpilot.Registers.PanActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class perference extends AppCompatActivity {

    RecyclerView job_rec;
    TextView text_member, shop,apply,psInfo;
    RelativeLayout member;
    Switch sp_sw;
    String value;
    LinearLayout open;
    Context context;
    Resources resources;
    LinearLayout owner_proofs,driver_proof;
    TextView Owner_aadhar,Owner_Aadhar_front,Owner_Aadhar_back,Owner_Fram,Owner_Fram_proof;
    TextView car,Driver_car,pan,Driver,Driving_back,Driving_front,Driving_proof;
    private ArrayList<JobModel> models;
    JobAdapter jobAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perference);

        Intent intent = getIntent();
        value = intent.getStringExtra("cat");


        member = findViewById(R.id.member);
        driver_proof = findViewById(R.id.driver_proof);
        text_member = findViewById(R.id.text_member);
        job_rec = findViewById(R.id.job_rec);
        shop = findViewById(R.id.shop);
        sp_sw = findViewById(R.id.sp_sw);
        psInfo = findViewById(R.id.psInfo);
        owner_proofs = findViewById(R.id.owner_proofs);
        open = findViewById(R.id.open);
        Owner_aadhar = findViewById(R.id.Owner_aadhar);
        Owner_Aadhar_front = findViewById(R.id.Owner_Aadhar_front);
        Owner_Aadhar_back = findViewById(R.id.Owner_Aadhar_back);
        Owner_Fram = findViewById(R.id.Owner_Fram);
        Owner_Fram_proof = findViewById(R.id.Owner_Fram_proof);
        car = findViewById(R.id.car);
        Driver_car = findViewById(R.id.Driver_car);
        pan = findViewById(R.id.pan);
        Driver = findViewById(R.id.Driver);
        Driving_back = findViewById(R.id.Driving_back);
        Driving_front = findViewById(R.id.Driving_front);
        Driving_proof = findViewById(R.id.Driving_proof);

        TextView set = findViewById(R.id.set);
        TextView cur = findViewById(R.id.cur);
        apply = findViewById(R.id.apply);



        Owner_Fram_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(perference.this, OwnerFirmActivity.class);
                intent1.putExtra("cat",value);
                startActivity(intent1);
            }
        });

        Owner_Aadhar_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(perference.this, OwnerAdharFrontActivity.class);
                intent1.putExtra("cat",value);
                startActivity(intent1);
            }
        });
        Owner_Aadhar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(perference.this, OwnerAdharBackActivity.class);
                intent1.putExtra("cat",value);
                startActivity(intent1);
            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(perference.this, CarCerActivity.class);
                intent1.putExtra("cat",value);
                startActivity(intent1);
            }
        });

        pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(perference.this, PanActivity.class);
                intent1.putExtra("cat",value);
                startActivity(intent1);
            }
        });

        Driving_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(perference.this, DrivingLBActivity.class);
                intent1.putExtra("cat",value);
                startActivity(intent1);
            }
        });

        Driving_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(perference.this, DrivingLFActivity.class);
                intent1.putExtra("cat",value);
                startActivity(intent1);
            }
        });





        if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(perference.this,"en");
            resources =context.getResources();

//            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            set.setText(resources.getString(R.string.my_preferences));
            cur.setText(resources.getString(R.string.update_membership));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            shop.setText(resources.getString(R.string.shop));

            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));


            setTitle(resources.getString(R.string.app_name));

        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(perference.this,"hi");
            resources =context.getResources();
            set.setText(resources.getString(R.string.my_preferences));
            cur.setText(resources.getString(R.string.update_membership));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            shop.setText(resources.getString(R.string.shop));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            setTitle(resources.getString(R.string.app_name));

        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(perference.this,"kn");
            resources =context.getResources();
            set.setText(resources.getString(R.string.my_preferences));
            cur.setText(resources.getString(R.string.update_membership));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            shop.setText(resources.getString(R.string.shop));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            setTitle(resources.getString(R.string.app_name));

        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(perference.this, "te");
            resources = context.getResources();
            set.setText(resources.getString(R.string.my_preferences));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            cur.setText(resources.getString(R.string.update_membership));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            shop.setText(resources.getString(R.string.shop));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            setTitle(resources.getString(R.string.app_name));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(perference.this, "bn");
            resources = context.getResources();
            set.setText(resources.getString(R.string.my_preferences));
            shop.setText(resources.getString(R.string.shop));
            cur.setText(resources.getString(R.string.update_membership));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            setTitle(resources.getString(R.string.app_name));
        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(perference.this, "gu");
            resources = context.getResources();
            set.setText(resources.getString(R.string.my_preferences));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            cur.setText(resources.getString(R.string.update_membership));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            shop.setText(resources.getString(R.string.shop));
            Driver.setText(resources.getString(R.string.pan_proof));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            setTitle(resources.getString(R.string.app_name));
        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(perference.this, "ml");
            resources = context.getResources();
            set.setText(resources.getString(R.string.my_preferences));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            cur.setText(resources.getString(R.string.update_membership));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            shop.setText(resources.getString(R.string.shop));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            setTitle(resources.getString(R.string.app_name));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(perference.this, "mr");
            resources = context.getResources();
            set.setText(resources.getString(R.string.my_preferences));
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            cur.setText(resources.getString(R.string.update_membership));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            shop.setText(resources.getString(R.string.shop));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            setTitle(resources.getString(R.string.app_name));
        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("pa")) {
            context = LocaleHelper.setLocale(perference.this, "pa");
            resources = context.getResources();
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            set.setText(resources.getString(R.string.my_preferences));
            cur.setText(resources.getString(R.string.update_membership));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            shop.setText(resources.getString(R.string.shop));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            setTitle(resources.getString(R.string.app_name));
        }
        else if(LocaleHelper.getLanguage(perference.this).equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(perference.this, "ta");
            resources = context.getResources();
            psInfo.setText(resources.getString(R.string.let_s_get_some_proofs));
            set.setText(resources.getString(R.string.my_preferences));
            Owner_Fram.setText(resources.getString(R.string.firm_proof));
            shop.setText(resources.getString(R.string.shop));
            Owner_Fram_proof.setText(resources.getString(R.string.firm_proof));
            cur.setText(resources.getString(R.string.update_membership));
            Owner_aadhar.setText(resources.getString(R.string.aadhar_proof));
            Owner_Aadhar_front.setText(resources.getString(R.string.aadhar_front_side));
            car.setText(resources.getString(R.string.character_certificate));
            Driver_car.setText(resources.getString(R.string.character_certificate));
            pan.setText(resources.getString(R.string.pan_proof));
            Driver.setText(resources.getString(R.string.pan_proof));
            Driving_back.setText(resources.getString(R.string.driving_licence_back));
            Driving_front.setText(resources.getString(R.string.driving_licence_front));
            Driving_proof.setText(resources.getString(R.string.driving_license));
            Owner_Aadhar_back.setText(resources.getString(R.string.aadhar_back_side));
            setTitle(resources.getString(R.string.app_name));
        }





        sp_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sp_sw.isChecked()) {
                    HashMap<String, Object> Mech = new HashMap<>();
                    Mech.put("shop", "Open");



                    DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(mAuth.getCurrentUser().getUid());
                    Driver.updateChildren(Mech);
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                                                Long times = System.currentTimeMillis();
//
//                    Calendar cdate = Calendar.getInstance();
//
//                    SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");
//
//                    final String savedate = currentdates.format(cdate.getTime());
//
//                    Calendar ctime = Calendar.getInstance();
//                    SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
//
//                    final String savetime = currenttime.format(ctime.getTime());
//
//                    String time = savedate + ":" + savetime;
//
//                    HashMap<String, Object> hashMap1 = new HashMap<>();
//                    hashMap1.put("Id",String.valueOf(times));
//                    hashMap1.put("time",time);
//                    hashMap1.put("title","Shop Opened");
//
//                    DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
//                    references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
//                            .setValue(hashMap1);
//                        }
//                    });
//                    finish();
                } else if(!sp_sw.isChecked()){
                    HashMap<String, Object> Mech = new HashMap<>();
                    Mech.put("shop", "Closed");

                    DatabaseReference Driver1 = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(mAuth.getCurrentUser().getUid());
                    Driver1.updateChildren(Mech);
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Long times = System.currentTimeMillis();
//
//                            Calendar cdate = Calendar.getInstance();
//
//                            SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");
//
//                            final String savedate = currentdates.format(cdate.getTime());
//
//                            Calendar ctime = Calendar.getInstance();
//                            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
//
//                            final String savetime = currenttime.format(ctime.getTime());
//
//                            String time = savedate + ":" + savetime;
//
//
//                            HashMap<String, Object> hashMap1 = new HashMap<>();
//                            hashMap1.put("Id",String.valueOf(times));
//                            hashMap1.put("time",time);
//                            hashMap1.put("title","Shop Closed");
//
//                            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
//                            references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
//                                    .setValue(hashMap1);
//                        }
//                    });
//                    finish();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        switch (value) {
            case "Owner":
                job_rec.setVisibility(View.VISIBLE);
                owner_proofs.setVisibility(View.VISIBLE);
                open.setVisibility(View.GONE);
                driver_proof.setVisibility(View.GONE);
                apply.setText("Posted");
                loadOwmersData();


                break;
            case "Drivers":
                job_rec.setVisibility(View.VISIBLE);
                driver_proof.setVisibility(View.VISIBLE);
                open.setVisibility(View.GONE);
                owner_proofs.setVisibility(View.GONE);
                apply.setText("Job Applied");
                loadDriverData();

                break;
            case "Dhaba":
                job_rec.setVisibility(View.GONE);
                owner_proofs.setVisibility(View.GONE);
                open.setVisibility(View.VISIBLE);
                driver_proof.setVisibility(View.GONE);
                apply.setText("Shop");
                loadDhabaData();

                break;
            case "Mech":

                job_rec.setVisibility(View.GONE);
                owner_proofs.setVisibility(View.GONE);
                driver_proof.setVisibility(View.GONE);
                apply.setText("Shop");
                open.setVisibility(View.VISIBLE);
                loadMachData();

                break;
        }

    }


    @SuppressLint("ResourceAsColor")
    private void loadMachData() {


        member.setBackgroundColor(R.color.gold);
        text_member.setText("Posts + 24/7 Support");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    if ((""+snapshot.child("shop").getValue()).equals("Open")) {
                        sp_sw.setChecked(true);
                    } else {
                        sp_sw.setChecked(false);
                    }

                    if(!(""+snapshot.child("aadhar_b_Image").getValue()).equals("")){
                        Owner_Aadhar_back.setClickable(false);
                        Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.green));
                    }else {
                        Owner_Aadhar_back.setClickable(true);
                        Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.red));
                    }

                if(!(""+snapshot.child("aadhar_Img_f").getValue()).equals("")){
                    Owner_Aadhar_front.setClickable(false);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Aadhar_front.setClickable(true);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.red));
                }



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void loadDhabaData() {


        member.setBackgroundColor(R.color.gold);
        text_member.setText("MemberShip + 24/7 Support");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                    if ((""+snapshot.child("shop").getValue()).equals("Open")) {
                        sp_sw.setChecked(true);
                    } else {
                        sp_sw.setChecked(false);
                    }

                if(!(""+snapshot.child("aadhar_b_Image").getValue()).equals("")){
                    Owner_Aadhar_back.setClickable(false);
                    Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Aadhar_back.setClickable(true);
                    Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("aadhar_Img_f").getValue()).equals("")){
                    Owner_Aadhar_front.setClickable(false);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Aadhar_front.setClickable(true);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.red));
                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


//    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void loadDriverData() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("Applied").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

//                    if ((""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {

                        String id = ""+ds.child("id").getValue();


                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        models = new ArrayList<>();

         DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.keepSynced(true);
                        ref.child("Jobs").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                models.clear();
//                ("" + ds.child("Id").getValue()).equals(id)
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    if (ds.hasChild(mAuth.getCurrentUser().getUid())) {
                                        JobModel jobModel = ds.getValue(JobModel.class);
                                        models.add(jobModel);
                                    }
                                }

                                jobAdapter = new JobAdapter(perference.this, models);
                                job_rec.setAdapter(jobAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        reference1.keepSynced(true);
        reference1.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (("" + snapshot.child("MemberShip").getValue()).equals("Gold")) {
                        member.setBackgroundColor(Color.parseColor("#D4AF37"));
                        text_member.setText(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support);

                    }else   if (("" + snapshot.child("MemberShip").getValue()).equals("Silver")){
                        member.setBackgroundColor(Color.parseColor("#A8A9AD"));
                        text_member.setText(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support);
                    }else {
                        member.setBackgroundColor(Color.parseColor("#F3FF0D5E"));
                        text_member.setText("None");
                    }

                if(!(""+snapshot.child("aadhar_b_Image").getValue()).equals("")){
                    Owner_Aadhar_back.setClickable(false);
                    Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Aadhar_back.setClickable(true);
                    Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("aadhar_Img_f").getValue()).equals("")){
                    Owner_Aadhar_front.setClickable(false);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Aadhar_front.setClickable(true);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("PFan").getValue()).equals("")){
                    pan.setClickable(false);
                    pan.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    pan.setClickable(true);
                    pan.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("DrivingB_Image").getValue()).equals("")){
                    Driving_back.setClickable(false);
                    Driving_back.setBackgroundColor(resources.getColor(R.color.green));
                }else if((""+snapshot.child("DrivingB_Image").getValue()).equals("")){
                    Driving_back.setClickable(true);
                    Driving_back.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("DrivingF_Image").getValue()).equals("")){
                    Driving_front.setClickable(false);
                    Driving_front.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Driving_front.setClickable(true);
                    Driving_front.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("Pan_Image").getValue()).equals("")){
                    car.setClickable(false);
                    car.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    car.setClickable(true);
                    car.setBackgroundColor(resources.getColor(R.color.red));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void loadOwmersData() {


        member.setBackgroundColor(R.color.gold);
        text_member.setText("Enlist Trucks + Find Drivers + Dhaba's and Mechanics ");


        models = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Jobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (("" + ds.child("uid").getValue()).equals(mAuth.getCurrentUser().getUid())) {
                        JobModel jobModel = ds.getValue(JobModel.class);
                        models.add(jobModel);
                    }
                }

                jobAdapter = new JobAdapter(perference.this, models);
                job_rec.setAdapter(jobAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
        refs.keepSynced(true);
        refs.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if(!(""+snapshot.child("aadhar_b_Image").getValue()).equals("")){
                    Owner_Aadhar_back.setClickable(false);
                    Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Aadhar_back.setClickable(true);
                    Owner_Aadhar_back.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("aadhar_Img_f").getValue()).equals("")){
                    Owner_Aadhar_front.setClickable(false);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Aadhar_front.setClickable(true);
                    Owner_Aadhar_front.setBackgroundColor(resources.getColor(R.color.red));
                }

                if(!(""+snapshot.child("FPan").getValue()).equals("")){
                    Owner_Fram_proof.setClickable(false);
                    Owner_Fram_proof.setBackgroundColor(resources.getColor(R.color.green));
                }else {
                    Owner_Fram_proof.setClickable(true);
                    Owner_Fram_proof.setBackgroundColor(resources.getColor(R.color.red));
                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(perference.this,MainActivity.class));
        finish();
    }
}