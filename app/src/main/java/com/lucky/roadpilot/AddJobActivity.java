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

public class AddJobActivity extends AppCompatActivity {

    TextView Veh_des,dialog_language,noof,text_lla,text_ll,create,job_Submit;
    EditText driver_des,no;
    FirebaseAuth  mAuth;
    private ProgressDialog progressDialog;
    String Location,Uname,Ulat,Ulon;

    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        Veh_des = findViewById(R.id.Veh_des);
        dialog_language = findViewById(R.id.dialog_language);
        create = findViewById(R.id.create);
        text_ll = findViewById(R.id.text_ll);
        text_lla = findViewById(R.id.text_lla);
        noof = findViewById(R.id.noof);
        driver_des = findViewById(R.id.driver_des);
        no = findViewById(R.id.no);
        job_Submit = findViewById(R.id.job_Submit);

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
                    Toast.makeText(AddJobActivity.this, "Please fill all the Details", Toast.LENGTH_SHORT).show();
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

    }

    private void showBox2() {
        final String[] Language = {"6 Wheeler","10 Wheeler"," 12 Wheeler","14 Wheeler","16 Wheeler","18 Wheeler","22 Wheeler"};
        final int checkItems = 0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddJobActivity.this);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddJobActivity.this);
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

        progressDialog.setMessage("Please wait while Job has been posting");

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();

        HashMap<String, String> hashmap = new HashMap<>();

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
                            reference.child("note_driver").child(Uid).child(String.valueOf(times)).setValue(has);

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

        DriverRef.child("Jobs").child(String.valueOf(times)).setValue(hashmap).addOnSuccessListener(unused -> {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Id",String.valueOf(times));
            hashMap.put("time",time);
            hashMap.put("title","Posted a Job for "+ dialog_language.getText().toString());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                    .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    no.setText("");
                    Veh_des.setText("");
                    driver_des.setText("");
                    finish();
                    onBackPressed();
                    startActivity(new Intent(AddJobActivity.this, MainActivity.class));
                }
            });



            Toast.makeText(AddJobActivity.this, "Post has been Submitted", Toast.LENGTH_SHORT).show();

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
        if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(AddJobActivity.this,"en");
            resources =context.getResources();

            // Text In English

            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));


        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"hi");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
        }
        else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"kn");
            resources =context.getResources();

            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"te");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
            // Text in Telugu

        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"bn");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));
        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"gu");
            resources =context.getResources();
            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));


        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"ml");
            resources =context.getResources();


            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));//
        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"mr");
            resources =context.getResources();


            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));

        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"pa");
            resources =context.getResources();


            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));

        }else if(LocaleHelper.getLanguage(AddJobActivity.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(AddJobActivity.this,"ta");
            resources =context.getResources();

            // Text in Tamil

            noof.setHint(resources.getString(R.string.no_of_drivers_need));
            create.setHint(resources.getString(R.string.create_job_vacancy));
            text_ll.setHint(resources.getString(R.string.type_of_truck));
            text_lla.setHint(resources.getString(R.string.nature_of_job));
            dialog_language.setHint(resources.getString(R.string.no_of_wheels));
            Veh_des.setHint(resources.getString(R.string.monthly));
            driver_des.setHint(resources.getString(R.string.route_of_vehicles));
            job_Submit.setHint(resources.getString(R.string.submit));
            no.setHint(resources.getString(R.string.number_of_drivers_you_need));

        }


    }
}