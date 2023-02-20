package com.lucky.roadpilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class UpdateJobDetails extends AppCompatActivity {

    TextView Veh_des,dialog_language,noof,text_lla,text_ll,create,job_Submit,delete;
    EditText driver_des,no;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    String Location,Uname,Ulat,Ulon;

    Context context;
    Resources resources;

    String times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job_details);

        Veh_des = findViewById(R.id.Veh_des);
        dialog_language = findViewById(R.id.dialog_language);
        create = findViewById(R.id.create);
        text_ll = findViewById(R.id.text_ll);
        text_lla = findViewById(R.id.text_lla);
        noof = findViewById(R.id.noof);
        driver_des = findViewById(R.id.driver_des);
        no = findViewById(R.id.no);
        job_Submit = findViewById(R.id.job_Submit);
        delete = findViewById(R.id.delete);

        Intent intent = getIntent();
//        uid = intent.getStringExtra("uid");
         String driver = intent.getStringExtra("driver");
        String data = intent.getStringExtra("data");
        String dat = intent.getStringExtra("dat");
        times = intent.getStringExtra("id");
        String num = intent.getStringExtra("num");

        dialog_language.setText(dat);
        driver_des.setText(driver);
        Veh_des.setText(data);

        no.setText(num);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();

        job_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(Veh_des.getText().toString()) || !TextUtils.isEmpty(driver_des.getText().toString()) ||!TextUtils.isEmpty(dialog_language.getText().toString()) || !TextUtils.isEmpty(no.getText().toString()) ){
                    saveJob();
                }else {
                    Toast.makeText(UpdateJobDetails.this, "Please fill all the Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Veh_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBox();
            }
        });

        dialog_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBox2();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> has = new HashMap<>();
                has.put("Active","no");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Jobs").child(times).updateChildren(has);
                onBackPressed();

                Toast.makeText(UpdateJobDetails.this, "Closed", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void showBox2() {
        final String[] Language = {"6 Wheeler","10 Wheeler"," 12 Wheeler","14 Wheeler","16 Wheeler","18 Wheeler","22 Wheeler"};
        final int checkItems = 0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateJobDetails.this);
        builder.setTitle("Number Of Wheeler")
                .setItems(Language, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Language[i].equals("6 Wheeler")){
                            dialog_language.setText("6 Wheeler");
                        }else if(Language[i].equals("10 Wheeler")){
                            dialog_language.setText("10 Wheeler");
                        }else if(Language[i].equals("12 Wheeler")){
                            dialog_language.setText("12 Wheeler");
                        }else if(Language[i].equals("14 Wheeler")){
                            dialog_language.setText("14 Wheeler");
                        }else if(Language[i].equals("16 Wheeler")){
                            dialog_language.setText("16 Wheeler");
                        }else if(Language[i].equals("18 Wheeler")){
                            dialog_language.setText("18 Wheeler");
                        }else if(Language[i].equals("22 Wheeler")){
                            dialog_language.setText("22 Wheeler");
                        }
                    }
                });
        builder.create().show();
    }

    private void showBox() {
        final String[] Language = {"MONTHLY","CONTRACTUAL","TRIP WISE"};
        final int checkItems = 0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateJobDetails.this);
        builder.setTitle("Nature Of Job")
                .setItems(Language, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Language[i].equals("MONTHLY")){
                            Veh_des.setText("Monthly");
                        }else if(Language[i].equals("CONTRACTUAL")){
                            Veh_des.setText("CONTRACTUAL");
                        }else if(Language[i].equals("TRIP WISE")){
                            Veh_des.setText("TRIP WISE");
                        }
                    }
                });

        builder.create().show();

    }

    private void saveJob() {

        progressDialog.setMessage("Please wait while Job has been Update");

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;
//
//        Long times = System.currentTimeMillis();

        HashMap<String, Object> hashmap = new HashMap<>();

        hashmap.put("Vehicle_Data",Veh_des.getText().toString());
        hashmap.put("Driver_data",driver_des.getText().toString());
        hashmap.put("No_Wheels",dialog_language.getText().toString());
        hashmap.put("number",no.getText().toString());
        hashmap.put("time",time);
        hashmap.put("Id",String.valueOf(times));
        hashmap.put("Active","yes");
        hashmap.put("Location", Location);
        hashmap.put("uid",mAuth.getCurrentUser().getUid());

//        HashMap<String, String> has = new HashMap<>();
//        has.put("Location", Location);
//        has.put("uid",mAuth.getCurrentUser().getUid());
//        has.put("time",time);
//        has.put("title",dialog_language.getText().toString());
//        DatabaseReference note = FirebaseDatabase.getInstance().getReference();
//        note.child("Note").child(time).setValue(has);

        HashMap<String, Object> has = new HashMap<>();
        has.put("uid", mAuth.getCurrentUser().getUid());
        has.put("title", Uname + " posted a job");
        has.put("time", time);
        has.put("seen", "false");
        has.put("id", String.valueOf(times));
        has.put("Uname", Uname);
        has.put("Ulat",Veh_des.getText().toString());
        has.put("Ulog",driver_des.getText().toString());
        has.put("cc",dialog_language.getText().toString());
        has.put("location", Location);
        has.put("key", "note_driver");




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    if((""+ds.child("Cat").getValue()).equals("Drivers")){
                        if((""+ds.child("Location").getValue()).equals(Location)) {


                            String Uid = "" + ds.child("uid").getValue();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("note_driver").child(Uid).child(String.valueOf(times)).updateChildren(has);

                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference();

//        HashMap<String, Object> has1 = new HashMap<>();
//        has1.put("uid",mAuth.getCurrentUser().getUid());
//        DriverRef.child(String.valueOf(times)).setValue(has1);

        DriverRef.child("Jobs").child(String.valueOf(times)).updateChildren(hashmap).addOnSuccessListener(unused -> {
            Long times = System.currentTimeMillis();

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("Id",String.valueOf(times));
            hashMap1.put("time",time);
            hashMap1.put("title","Updated Jod Details");

            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
            references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                    .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    no.setText("");
                    Veh_des.setText("");
                    driver_des.setText("");

                    finish();
                    onBackPressed();
                    startActivity(new Intent(UpdateJobDetails.this, MainActivity.class));
                }
            });

            Toast.makeText(UpdateJobDetails.this, "Post has been Updated", Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Location = ""+snapshot.child("Location").getValue();
                Uname = ""+snapshot.child("Company_Name").getValue();
                Ulat = ""+snapshot.child("lat").getValue();
                Ulon = ""+snapshot.child("lon").getValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LanguageChoose();
    }


    private void LanguageChoose(){
        if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"en");
            resources =context.getResources();

            // Text In English

            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            delete.setHint(resources.getString(R.string.delete));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));


        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"hi");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            delete.setHint(resources.getString(R.string.delete));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
        }
        else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"kn");
            resources =context.getResources();

            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            delete.setHint(resources.getString(R.string.delete));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"te");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
            // Text in Telugu

            delete.setHint(resources.getString(R.string.delete));
        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"bn");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            delete.setHint(resources.getString(R.string.delete));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"gu");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            delete.setHint(resources.getString(R.string.delete));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));


        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"ml");
            resources =context.getResources();


            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            delete.setHint(resources.getString(R.string.delete));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));//
        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"mr");
            resources =context.getResources();


            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            delete.setHint(resources.getString(R.string.delete));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));

        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"pa");
            resources =context.getResources();


            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
            delete.setHint(resources.getString(R.string.delete));

        }else if(LocaleHelper.getLanguage(UpdateJobDetails.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(UpdateJobDetails.this,"ta");
            resources =context.getResources();

            // Text in Tamil

            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            delete.setHint(resources.getString(R.string.delete));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.update));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));

        }


    }
}