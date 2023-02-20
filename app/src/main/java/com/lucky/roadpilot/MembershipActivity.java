package com.lucky.roadpilot;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class MembershipActivity extends AppCompatActivity implements PaymentResultListener{

    // implements PaymentResultListener

    private CardView others_membership,goldMembership,silverMembership;
    String value;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int amount;
    String sAmount;
    Context context;
    Resources resources;
    TextView text31,t1,member,t2,t3,gold,text,silver,text3,t4,amounts;
    String phone,name,razorpay;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    public final static int GOOGLE_PAY_REQUEST_CODE = 100;
    public final static int GOOGLE_PAY_REQUEST_CODE_GOLD = 2950;
    public final static int GOOGLE_PAY_REQUEST_CODE_OTHERS = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        Intent intent = getIntent();
        value = intent.getStringExtra("cat");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        others_membership = findViewById(R.id.others_membership);
        silverMembership = findViewById(R.id.silverMembership);
        goldMembership = findViewById(R.id.goldMembership);
         text31 = findViewById(R.id.text31);
        t1 = findViewById(R.id.t1);
        member = findViewById(R.id.member);
        amounts = findViewById(R.id.amount);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        gold = findViewById(R.id.gold);
        text = findViewById(R.id.text);
        silver = findViewById(R.id.silver);
        text3 = findViewById(R.id.text3);
        t4 = findViewById(R.id.t4);

        LinearLayout DriverMemberShip = findViewById(R.id.DriverMemberShip);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        switch (value){
            case "Mech":
                DriverMemberShip.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                text31.setText("Posts + 24/7 Support + Get Clients");
                amounts.setText("500");

                break;
                case "Dhaba":
                DriverMemberShip.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                text31.setText("MemberShip + 24/7 Support");

                    amounts.setText("1100");

                break;
                case "Owner":
                DriverMemberShip.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                text31.setText("Enlist Trucks + Find Drivers + Dhaba's and Mechanics + 24/7 Support");

                    amounts.setText("1100");
                break;
            case "Drivers":
                DriverMemberShip.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                break;

        }

        others_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OtherMemberShip();

//                startActivity(new Intent(MembershipActivity.this,MainActivity.class));
            }
        });
        goldMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoldenMemberShip();

//                startActivity(new Intent(MembershipActivity.this,MainActivity.class));
            }
        });
        silverMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SilverMemberShip();

//                startActivity(new Intent(MembershipActivity.this,MainActivity.class));
            }
        });




    }

    private void SilverMemberShip() {

//        RazorPay PaymentGateway

        sAmount = "100";

        amount = Math.round(Float.parseFloat(sAmount) * 250);

        Checkout checkout = new Checkout();

        checkout.setKeyID(razorpay);

        checkout.setImage(R.mipmap.logo);

        JSONObject object = new JSONObject();

        try {
            // Put Name
            object.put("name", "RoadPilot");
            // Put description
            object.put("description","MemberShip Payment");
            // Put theme color
            object.put("theme.color","#0093DD");
            //Put current unit
            object.put("currency","INR");
            //Put amount
            object.put("amount",amount);
            //Put mobile number
            object.put("prefill.contact",phone);
            // Put email
            object.put("prefill.email","info@roadpilot.co.in");
            //Open razorpay checkout activity
            checkout.open(MembershipActivity.this,object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
////        google pay payment Gateway
//
//
//        Uri uri =
//                new Uri.Builder()
//                        .scheme("upi")
//                        .authority("pay")
//                        .appendQueryParameter("pa", "nitsharma07@okaxis")
//                        .appendQueryParameter("pn", "Lokesh Reddy")
//                        .appendQueryParameter("mc", " ")
//                        .appendQueryParameter("tr", "2345")
//                        .appendQueryParameter("tn", "Payment")
//                        .appendQueryParameter("am", "100")
//                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
//                        .build();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        Activity activity;
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);


    }



    private void GoldenMemberShip() {

        //        RazorPay PaymentGateway
        sAmount = "100";

        amount = Math.round(Float.parseFloat(sAmount) * 2950);

        Checkout checkout = new Checkout();

        checkout.setKeyID(razorpay);

        checkout.setImage(R.mipmap.logo);

        JSONObject object = new JSONObject();

        try {
            // Put Name
            object.put("name", "RoadPilot");
            // Put description
            object.put("description","MemberShip Payment");
            // Put theme color
            object.put("theme.color","#0093DD");
            //Put current unit
            object.put("currency","INR");
            //Put amount
            object.put("amount",amount);
            //Put mobile number
            object.put("prefill.contact",phone);
            // Put email
            object.put("prefill.email","info@roadpilot.co.in");
            //Open razorpay checkout activity
            checkout.open(MembershipActivity.this,object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //        google pay payment Gateway




//        Uri uri =
//                new Uri.Builder()
//                        .scheme("upi")
//                        .authority("pay")
//                        .appendQueryParameter("pa", "nitsharma07@okaxis")
//                        .appendQueryParameter("pn", "Lokesh Reddy")
//                        .appendQueryParameter("mc", " ")
//                        .appendQueryParameter("tr", "2345")
//                        .appendQueryParameter("tn", "Payment")
//                        .appendQueryParameter("am", "100")
//                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
//                        .build();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        Activity activity;
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE_GOLD);


    }

    private void OtherMemberShip() {

        //        RazorPay PaymentGateway

        if (amounts.getText().toString().equals("1100")) {
            sAmount = "100";

            amount = Math.round(Float.parseFloat(sAmount) * 1100);

            Checkout checkout = new Checkout();

            checkout.setKeyID(razorpay);

            checkout.setImage(R.mipmap.logo);

            JSONObject object = new JSONObject();

            try {
                // Put Name
                object.put("name", "RoadPilot");
                // Put description
                object.put("description", "MemberShip Payment");
                // Put theme color
                object.put("theme.color", "#0093DD");
                //Put current unit
                object.put("currency", "INR");
                //Put amount
                object.put("amount", amount);
                //Put mobile number
                object.put("prefill.contact", phone);
                // Put email
                object.put("prefill.email", "info@roadpilot.co.in");
                //Open razorpay checkout activity
                checkout.open(MembershipActivity.this, object);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
//
            sAmount = "100";

            amount = Math.round(Float.parseFloat(sAmount) * 500);

            Checkout checkout = new Checkout();

            checkout.setKeyID(razorpay);

            checkout.setImage(R.mipmap.logo);

            JSONObject object = new JSONObject();

            try {
                // Put Name
                object.put("name", "RoadPilot");
                // Put description
                object.put("description", "MemberShip Payment");
                // Put theme color
                object.put("theme.color", "#0093DD");
                //Put current unit
                object.put("currency", "INR");
                //Put amount
                object.put("amount", amount);
                //Put mobile number
                object.put("prefill.contact", phone);
                // Put email
                object.put("prefill.email", "info@roadpilot.co.in");
                //Open razorpay checkout activity
                checkout.open(MembershipActivity.this, object);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        google pay payment Gateway

//        Uri uri =
//                new Uri.Builder()
//                        .scheme("upi")
//                        .authority("pay")
//                        .appendQueryParameter("pa", "nitsharma07@okaxis")
//                        .appendQueryParameter("pn", "Lokesh Reddy")
//                        .appendQueryParameter("mc", " ")
//                        .appendQueryParameter("tr", "2345")
//                        .appendQueryParameter("tn", "Payment")
//                        .appendQueryParameter("am", "100")
//                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
//                        .build();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        Activity activity;
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE_OTHERS);

    }

    //        RazorPay PaymentGateway

    @Override
    public void onPaymentSuccess(String s) {


        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;


//        if(amount == Math.round(Float.parseFloat(sAmount) * 500)){
        if(amount == Math.round(Float.parseFloat(sAmount) * 500)){

            HashMap<String, Object> map = new HashMap<>();

            map.put("MemberShip","Gold");
            map.put("time",time);

            Long times = System.currentTimeMillis();

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("Id",String.valueOf(times));
            hashMap1.put("time",time);
            hashMap1.put("title","Updated Membership");

            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
            references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                    .setValue(hashMap1);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).updateChildren(map);
            reference.child("CustomerCare").child(phone).updateChildren(map);
//            reference.child(value).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).updateChildren(map);

//            Toast.makeText(MembershipActivity.this, "500", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MembershipActivity.this,MainActivity.class));


        }
        else if(amount == Math.round(Float.parseFloat(sAmount) * 1100)){

            HashMap<String, Object> map = new HashMap<>();

            map.put("MemberShip","Gold");
            map.put("time",time);

            Long times = System.currentTimeMillis();

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("Id",String.valueOf(times));
            hashMap1.put("time",time);
            hashMap1.put("title","Updated Membership");

            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
            references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                    .setValue(hashMap1);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).updateChildren(map);
            reference.child("CustomerCare").child(phone).updateChildren(map);
//            reference.child(value).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).updateChildren(map);

//            Toast.makeText(MembershipActivity.this, "500", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MembershipActivity.this,MainActivity.class));


        }
        else if(amount == Math.round(Float.parseFloat(sAmount) * 2950)){

            HashMap<String, Object> map = new HashMap<>();

            map.put("time",time);
            map.put("MemberShip","Gold");

            Long times = System.currentTimeMillis();

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("Id",String.valueOf(times));
            hashMap1.put("time",time);
            hashMap1.put("title","Updated Membership");

            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
            references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                    .setValue(hashMap1);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//            reference.child(value).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).updateChildren(map);
            reference.child("CustomerCare").child(phone).updateChildren(map);
            reference.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map);
            onBackPressed();
//            startActivity(new Intent(MembershipActivity.this,MainActivity.class));
//            Toast.makeText(MembershipActivity.this, "2950", Toast.LENGTH_SHORT).show();

        }else
            if(amount == Math.round(Float.parseFloat(sAmount) * 250)){

            HashMap<String, Object> map = new HashMap<>();

                map.put("time",time);
                map.put("MemberShip","Silver");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                reference.child(value).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).updateChildren(map);
                reference.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map);
                reference.child("CustomerCare").child(phone).updateChildren(map);
                onBackPressed();
//                startActivity(new Intent(MembershipActivity.this,MainActivity.class));
//                Toast.makeText(MembershipActivity.this, "100", Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK && data.getData()!=null ){
//            switch (requestCode){
//                case GOOGLE_PAY_REQUEST_CODE:
//                    HashMap<String, Object> map = new HashMap<>();
//
//            map.put("MemberShip","Silver");
//
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//            reference.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map);
//
//            startActivity(new Intent(MembershipActivity.this,MainActivity.class));
//
//                    break;
//                    case GOOGLE_PAY_REQUEST_CODE_GOLD:
//                        HashMap<String, Object> map1 = new HashMap<>();
//
//            map1.put("MemberShip","Gold");
//
//            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
//            reference1.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map1);
//                        startActivity(new Intent(MembershipActivity.this,MainActivity.class));
//
//                    break;
//                    case GOOGLE_PAY_REQUEST_CODE_OTHERS:
//                        HashMap<String, Object> map2 = new HashMap<>();
//
//            map2.put("MemberShip","gold");
//
//            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
//            reference2.child("Users").child(mAuth.getCurrentUser().getUid()).updateChildren(map2);
//                        startActivity(new Intent(MembershipActivity.this,MainActivity.class));
//
//                    break;
//
//            }
//        }else{
//            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
//        }



//    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Dynamic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                razorpay = ""+snapshot.child("razorpay").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                phone = ""+snapshot.child("phone").getValue();
                name = ""+snapshot.child("name").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(MembershipActivity.this,"en");
            resources =context.getResources();

            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));


            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"hi");
            resources =context.getResources();

            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }
        else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"kn");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"te");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"bn");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"gu");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"ml");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"mr");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"pa");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(MembershipActivity.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(MembershipActivity.this,"ta");
            resources =context.getResources();
            t4.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            text3.setText(resources.getString(R.string.get_the_access_to_apply_job_n_get_24_7_costumercare_support));
            silver.setText(resources.getString(R.string.silver_membership));
            text.setText(resources.getString(R.string.insurance_n_discount_on_food_at_dhaba_n_get_24_7_costumercare_support));
            gold.setText(resources.getString(R.string.gold_membership));
            t3.setText(resources.getString(R.string.please_select_the_plan_please));
            t2.setText(resources.getString(R.string.have_a_great_time_with_roadpilot));
            member.setText(resources.getString(R.string.gold_membership));
            t1.setText(resources.getString(R.string.please_select_the_plan_please));

            setTitle(resources.getString(R.string.app_name));

        }

    }
}