package com.lucky.roadpilot;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView nav_bot;
    FirebaseAuth mAuth;
    String Cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        nav_bot =findViewById(R.id.nav_bot);
        mAuth = FirebaseAuth.getInstance();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new ProfileFragment()).commit();
        nav_bot.setSelectedItemId(R.id.home);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Cat = ""+ snapshot.child("Cat").getValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        nav_bot.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;

                switch (item.getItemId()){
                    case  R.id.home:
                        selected = new ProfileFragment();
                        break;
                    case R.id.searsh:
                        selected = new DabaFragment();


                        break;
                    case R.id.swipe:
                        if(Cat.equals("Drivers") || Cat.equals("Owner")){

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
//                            Long times = System.currentTimeMillis();
//
//                            HashMap<String, Object> hashMap1 = new HashMap<>();
//                            hashMap1.put("Id",String.valueOf(times));
//                            hashMap1.put("time",time);
//                            hashMap1.put("title","Searched For Jobs");
//
//                            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
//                            references1.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Activities").child(String.valueOf(times))
//                                    .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                }
//                            });
                            item.setCheckable(true);
                            selected = new JobFragment();
                        }else {
                            selected = new ProfileFragment();
                            item.setCheckable(false);
                            Toast.makeText(MainActivity.this, "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.news:
                        selected = new NewsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, selected).commit();



                return true;
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot ds: snapshot.getChildren()){
//                    if((""+ds.child("uid").getValue()).equals(mAuth.getCurrentUser().getUid())){
//                        String
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
//    }


//    @Override
//    public void onBackPressed() {
//
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//
//    }

    @Override
    protected void onStart() {
        super.onStart();

//     ** verification ** //



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                String cat = ""+snapshot.child("Cat").getValue();
                String DrivingB_Image = ""+snapshot.child("DrivingB_Image").getValue();
                String DrivingF_Image = ""+snapshot.child("DrivingF_Image").getValue();
                String FPan = ""+snapshot.child("FPan").getValue();
                String MemberShip = ""+snapshot.child("MemberShip").getValue();
                String Pan_Image = ""+snapshot.child("Pan_Image").getValue();
                String aadhar_Img_f = ""+snapshot.child("aadhar_Img_f").getValue();
                String aadhar_b_Image = ""+snapshot.child("aadhar_b_Image").getValue();
                String Company_Name = ""+snapshot.child("Company_Name").getValue();
                String Brand = ""+snapshot.child("Brand").getValue();


                switch (cat) {
//                    case "Owner":
////                        if (aadhar_Img_f.equals("")) {
////                            Intent intent = new Intent(MainActivity.this,OwnerAdharFrontActivity.class);
////                            intent.putExtra("cat","Owner");
////                        startActivity(intent);
////                    }
////                        if (aadhar_b_Image.equals("")) {
////                            Intent intent = new Intent(MainActivity.this,OwnerAdharBackActivity.class);
////                            intent.putExtra("cat","Owner");
////                            startActivity(intent);
//////                            startActivity(new Intent(MainActivity.this, OwnerAdharBackActivity.class));
////                        }
////                        if (FPan.equals("")) {
////                            Intent intent = new Intent(MainActivity.this,OwnerFirmActivity.class);
////                            intent.putExtra("cat","Owner");
////                            startActivity(intent);
//////                            startActivity(new Intent(MainActivity.this, OwnerFirmActivity.class));
////                        }
//                        if (MemberShip.equals("None")) {
//
//                            Intent intent = new Intent(MainActivity.this,MembershipActivity.class);
//                            intent.putExtra("cat","Owner");
//                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
//                        }
//
//                        break;
//                    case "Drivers":
////                        if (aadhar_Img_f.equals("")) {
////                            Intent intent = new Intent(MainActivity.this, OwnerAdharFrontActivity.class);
////                            intent.putExtra("cat","Drivers");
////                            startActivity(intent);
//////                            startActivity(new Intent(MainActivity.this, OwnerAdharFrontActivity.class));
////                        }
////                        if (aadhar_b_Image.equals("")) {
////                            Intent intent = new Intent(MainActivity.this, OwnerAdharBackActivity.class);
////                            intent.putExtra("cat","Drivers");
////                            startActivity(intent);
//////                            startActivity(new Intent(MainActivity.this, OwnerAdharBackActivity.class));
////                        }
////                        if (DrivingF_Image.equals("")) {
////                            Intent intent = new Intent(MainActivity.this, DrivingLFActivity.class);
////                            intent.putExtra("cat","Drivers");
////                            startActivity(intent);
//////                            startActivity(new Intent(MainActivity.this, DrivingLFActivity.class));
////                        }
////                        if (FPan.equals("None")) {
////                            Intent intent = new Intent(MainActivity.this, PanActivity.class);
////                            intent.putExtra("cat","Drivers");
////                            startActivity(intent);
//////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
////                        }
////                        if (DrivingB_Image.equals("")) {
////                            Intent intent = new Intent(MainActivity.this, DrivingLBActivity.class);
////                            intent.putExtra("cat","Drivers");
////                            startActivity(intent);
//////                            startActivity(new Intent(MainActivity.this, DrivingLBActivity.class));
////                        }
////                        if (Pan_Image.equals("")) {
////                            Intent intent = new Intent(MainActivity.this, PanActivity.class);
////                            intent.putExtra("cat","Drivers");
////                            startActivity(intent);
////                        }
//                        if (MemberShip.equals("None")) {
//                            Intent intent = new Intent(MainActivity.this, MembershipActivity.class);
//                            intent.putExtra("cat","Drivers");
//                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
//                        }
//                        break;
                    case "Dhaba":
//                        if (aadhar_Img_f.equals("")) {
//                            Intent intent = new Intent(MainActivity.this,OwnerAdharFrontActivity.class);
//                            intent.putExtra("cat","Dhaba");
//                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, OwnerAdharFrontActivity.class));
//                        }
//                        if (aadhar_b_Image.equals("")) {
//                            Intent intent = new Intent(MainActivity.this,OwnerAdharBackActivity.class);
//                            intent.putExtra("cat","Dhaba");
//                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, OwnerAdharBackActivity.class));
//                        }
//                        if (Company_Name.equals("")) {
//                            Intent intent = new Intent(MainActivity.this,DhabaActivity.class);
//                            intent.putExtra("cat","Dhaba");
//                            startActivity(intent);
//                        }
                        if (MemberShip.equals("None")) {
                            Intent intent = new Intent(MainActivity.this,MembershipActivity.class);
                            intent.putExtra("cat","Dhaba");
                            startActivity(intent);
//                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
                        }
                        break;
                    case "Mech":


//                        if (aadhar_Img_f.equals("")) {
//                            Intent intent = new Intent(MainActivity.this,OwnerAdharFrontActivity.class);
//                            intent.putExtra("cat","Mech");
//                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, OwnerAdharFrontActivity.class));
//                        }
//                        if (aadhar_b_Image.equals("")) {
//                            Intent intent = new Intent(MainActivity.this,OwnerAdharBackActivity.class);
//                            intent.putExtra("cat","Mech");
//                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, OwnerAdharBackActivity.class));
//                        }
//                        if (Brand.equals("")) {
//                            Intent intent = new Intent(MainActivity.this,MechActivity.class);
//                            intent.putExtra("cat","Mech");
//                            startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, MechActivity.class));
//                        }
                        if (MemberShip.equals("None")) {
                            Intent intent = new Intent(MainActivity.this,MembershipActivity.class);
                            intent.putExtra("cat","Mech");
                            startActivity(intent);
                        }
                        break;
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
//
//    @Override public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.body_container);
//        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
//            super.onBackPressed();
//        }
//
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }

    @Override
    public void onBackPressed() {

//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
        Fragment selected = new ProfileFragment();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.body_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }else if(getSupportFragmentManager().beginTransaction().equals(selected)){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

    }


}