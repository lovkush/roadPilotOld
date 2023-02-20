package com.lucky.roadpilot;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProfileViewActivity extends AppCompatActivity implements LocationListener {

    private TextView Tv_one;
    private TextView Tv_two;
    private TextView tv_three;
    private TextView tv_four,user_phone;
    private CardView cardView1;
    private RelativeLayout one,two,three,four;
    private LinearLayout lay;
    String id;
    String url,phone,cat,name;
    WebView map;
    ImageView image,loc,car;
    double longitude,latitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList;

    SupportMapFragment supportMapFragment;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        String img = intent.getStringExtra("img");
        name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        id = intent.getStringExtra("Id");
        String comp = intent.getStringExtra("comp");
        phone = intent.getStringExtra("phone");
        String images = intent.getStringExtra("images");
        String lat = intent.getStringExtra("lat");
        String log = intent.getStringExtra("lon");
        String cc = intent.getStringExtra("cc");

//        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                LatLng latLng;
//                latLng = new LatLng(latitude,longitude);
//
//                MarkerOptions options = new MarkerOptions().position(latLng).title(name);
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//                googleMap.addMarker(options);
//            }
//        });



        tv_four = findViewById(R.id.tv_four);
        image = findViewById(R.id.image);
        ImageView use_img = findViewById(R.id.use_img);
        lay = findViewById(R.id.lay);
        map = findViewById(R.id.map);
        loc = findViewById(R.id.loc);
        car = findViewById(R.id.car);
        TextView user_name = findViewById(R.id.user_name);
         user_phone = findViewById(R.id.user_phone);
        TextView user_location = findViewById(R.id.user_location);
        Tv_one = findViewById(R.id.Tv_one);
        tv_three = findViewById(R.id.tv_three);
        Tv_two = findViewById(R.id.Tv_two);
        cardView1 = findViewById(R.id.cardView1);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);

//        url = "https://www.google.com/maps/dir/?api=1&destination="+lat+","+log+"&travelmode=driving";
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

//        map.loadUrl(String.valueOf(Uri.parse(url)));

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + log;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });


        try{
            Picasso.get().load(img).placeholder(R.drawable.profile).into(use_img);

        }catch (Exception e){
            use_img.setImageResource(R.drawable.profile);
        }

        user_name.setText(name + "," + comp);

        try{
            Picasso.get().load(images).placeholder(R.drawable.test).into(image);

        }catch (Exception e){
            image.setImageResource(R.drawable.test);
        }
        try{
            Picasso.get().load(cc).placeholder(R.drawable.test).into(car);

        }catch (Exception e){
            car.setImageResource(R.drawable.test);
        }


        Zoomy.Builder builder = new Zoomy.Builder(this)
                .target(image).animateZooming(false).enableImmersiveMode(false).tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        Toast.makeText(ProfileViewActivity.this, "ZoomIn/Out", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.register();

        Zoomy.Builder builder1 = new Zoomy.Builder(this)
                .target(car).animateZooming(false).enableImmersiveMode(false).tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        Toast.makeText(ProfileViewActivity.this, "ZoomIn/Out", Toast.LENGTH_SHORT).show();
                    }
                });

        builder1.register();

        user_location.setText(address);

//        user_phone.setText(phone);

//        if(comp.equals("")){
//            user_name.setText(name);
//        }else {
//            user_name.setText(name + "," + comp);
//        }

        user_phone.setOnClickListener(view -> {


            Calendar cdate = Calendar.getInstance();

            SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

            final String savedate = currentdates.format(cdate.getTime());

            Calendar ctime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

            final String savetime = currenttime.format(ctime.getTime());

            String time = savedate + ":" + savetime;

            Long times = System.currentTimeMillis();

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("Id", String.valueOf(times));
            hashMap1.put("time", time);
            hashMap1.put("title", "Called to" + name + " at" + phone);

            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
            references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                    .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                    ref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            String Cat = "" + snapshot.child("Cat").getValue();
                            if ((snapshot.child("MemberShip").getValue()).equals("None")) {


//                            if (MemberShip.equals("None")) {
//                user_phone.setText("**********");
//
                                Intent intent = new Intent(ProfileViewActivity.this, MembershipActivity.class);
                                intent.putExtra("cat", Cat);
                                startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
                            } else {


                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel: " + phone));
                                startActivity(callIntent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();

        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("Id", String.valueOf(times));
        hashMap1.put("time", time);
        hashMap1.put("title", "Called to" + name);

        DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
        references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                .setValue(hashMap1);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cat = ""+snapshot.child("Cat").getValue();

                if((""+snapshot.child("Cat").getValue()).equals("Mech")){

                    lay.setVisibility(View.VISIBLE);
                    car.setVisibility(View.GONE);
                    tv_four.setText(R.string.dandp);
                    Tv_one.setText(R.string.electric);
                    Tv_two.setText(R.string.engine);
                    tv_three.setText(R.string.suspension);

                    user_phone.setText(phone);

                    if((""+snapshot.child("electric").getValue()).equals("available")){
                        one.setBackgroundColor(Color.parseColor("#26FF00"));
                    }
                    else if((""+snapshot.child("electric").getValue()).equals("unavailable")){
                        one.setBackgroundColor(Color.parseColor("#F40505"));
                    }
                    if((""+snapshot.child("suspension").getValue()).equals("available")){
                        three.setBackgroundColor(Color.parseColor("#26FF00"));
                    }
                    else if((""+snapshot.child("suspension").getValue()).equals("unavailable")){
                        three.setBackgroundColor(Color.parseColor("#F40505"));
                    }
                     if((""+snapshot.child("engine").getValue()).equals("available")){
                         two.setBackgroundColor(Color.parseColor("#26FF00"));
                    }
                    else if((""+snapshot.child("engine").getValue()).equals("unavailable")){
                        two.setBackgroundColor(Color.parseColor("#F40505"));
                    }
                    if((""+snapshot.child("dent").getValue()).equals("available")){
                        four.setBackgroundColor(Color.parseColor("#26FF00"));
                    }
                    else if((""+snapshot.child("dent").getValue()).equals("unavailable")){
                        four.setBackgroundColor(Color.parseColor("#F40505"));
                    }
                }
                else if((""+snapshot.child("Cat").getValue()).equals("Dhaba")){

                    lay.setVisibility(View.VISIBLE);
                    car.setVisibility(View.GONE);
                    tv_four.setText(R.string.bath_area);
                    Tv_one.setText(R.string._24hr_security);
                    Tv_two.setText(R.string.tv);
                    tv_three.setText(R.string.parking);
                    user_phone.setText(phone);

                    if((""+snapshot.child("Security").getValue()).equals("available")){
                        one.setBackgroundColor(Color.parseColor("#26FF00"));
                    }else if((""+snapshot.child("Security").getValue()).equals("unavailable")){
                        one.setBackgroundColor(Color.parseColor("#F40505"));
                    }
                    if((""+snapshot.child("parking").getValue()).equals("available")){
                        three.setBackgroundColor(Color.parseColor("#26FF00"));
                    }else if((""+snapshot.child("parking").getValue()).equals("unavailable")){
                        three.setBackgroundColor(Color.parseColor("#F40505"));
                    }
                    if((""+snapshot.child("tv").getValue()).equals("available")){
                        two.setBackgroundColor(Color.parseColor("#26FF00"));
                    }
                    else if((""+snapshot.child("tv").getValue()).equals("unavailable")){
                        two.setBackgroundColor(Color.parseColor("#F40505"));
                    }
                    if((""+snapshot.child("bath").getValue()).equals("available")){
                        four.setBackgroundColor(Color.parseColor("#26FF00"));
                    }else if((""+snapshot.child("bath").getValue()).equals("unavailable")){
                        four.setBackgroundColor(Color.parseColor("#F40505"));
                    }

                    String im = ""+snapshot.child("dhaba_Image").getValue();

                    Picasso.get().load(im).placeholder(R.drawable.test).into(image);


                }else if((""+snapshot.child("Cat").getValue()).equals("Owner")){
                    lay.setVisibility(View.GONE);
                    car.setVisibility(View.GONE);

//                        user_phone.setText(phone);


//

                }else {
                    lay.setVisibility(View.GONE);


//                        user_phone.setText(phone);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users");
        ref1.child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if((""+snapshot.child("Verified").getValue()).equals("Yes")) {
                    cardView1.setVisibility(View.VISIBLE);
                }else {
                    cardView1.setVisibility(View.INVISIBLE);
                }




//                longitude = Double.parseDouble(""+snapshot.child("log").getValue());
//                 latitude = Double.parseDouble(""+snapshot.child("lat").getValue());
//                 img = ""+snapshot.child("Image").getValue();
                 address = ""+snapshot.child("address").getValue();
//                 phone = ""+snapshot.child("phone").getValue();
//                 loc = ""+snapshot.child("Location").getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref1.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {





//                            if (MemberShip.equals("None")) {
                    if(cat.equals("Owner") && (snapshot.child("MemberShip").getValue()).equals("None")){
                        user_phone.setText("**********");

                    }else if(cat.equals("Drivers") && (snapshot.child("MemberShip").getValue()).equals("None")){
                        user_phone.setText("**********");
                    }else {
                        user_phone.setText(phone);
                    }
////
//                        Intent intent = new Intent(ProfileViewActivity.this,MembershipActivity.class);
//                        intent.putExtra("cat","Owner");
//                        startActivity(intent);
////                            startActivity(new Intent(MainActivity.this, MembershipActivity.class));
//
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(ActivityCompat.checkSelfPermission(ProfileViewActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getGoLocation();
        }else {
            ActivityCompat.requestPermissions(ProfileViewActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }

    private void getGoLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }



        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,5, this);

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null){
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addressList  =geocoder.getFromLocation(lat, lon, 1);
                Log.d("location", String.valueOf(lat));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        
    }
}