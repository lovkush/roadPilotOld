package com.lucky.roadpilot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.lucky.roadpilot.Adapter.PostAdapter;
import com.lucky.roadpilot.Models.PostModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DabaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DabaFragment extends Fragment implements IOnBackPressed{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DabaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DabaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DabaFragment newInstance(String param1, String param2) {
        DabaFragment fragment = new DabaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseAuth mAuth;
    FloatingActionButton fab;
    RecyclerView news_rec;
    EditText search;
    Context context;
    Resources resources;
    PostAdapter jobAdapter;
    ArrayList<PostModel> models;
    LottieAnimationView loading;

    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daba, container, false);

        fab = view.findViewById(R.id.addDhada);
        news_rec = view.findViewById(R.id.user_rec);
        search = view.findViewById(R.id.search);
        loading = view.findViewById(R.id.loading);

       // getLocation();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                intent.putExtra("add", "dhaba");
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                jobAdapter.getFilter().filter(charSequence);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAuth = FirebaseAuth.getInstance();


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

//        LanguageChoose();
        loading.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (("" + snapshot.child("Cat").getValue()).equals("Dhaba")) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadPosts();
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getGoLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    private void loadPosts() {

        models = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    if (("" + ds.child("approved").getValue()).equals("Yes") && (""+ds.child("Cat").getValue()).equals("Post")) {
                        PostModel postModel = ds.getValue(PostModel.class);
                        models.add(postModel);
                        loading.setVisibility(View.GONE);

                    }
                }

                jobAdapter = new PostAdapter(getActivity(), models);
                news_rec.setAdapter(jobAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getGoLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Location> task) {
//
//                Location location = task.getResult();
//                if(location != null){
//
//                    try {
//                        Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
//                        addresses = geocoder.getFromLocation(
//                                location.getLatitude(), location.getLongitude(),1
//                        );
////                        shopcountryedt.setText(addresses.get(0).getCountryName());
////                        shopstateedt.setText(addresses.get(0).getAdminArea());
////                        shopcityedt.setText(addresses.get(0).getLocality());
////                        shopaddresstotaledt.setText(addresses.get(0).getAddressLine(0));
//
//                        address.setText(addresses.get(0).getLocality() + "," +  addresses.get(0).getAdminArea() + "," +  addresses.get(0).getCountryName() );
//
//                        Daddress.setText(addresses.get(0).getLocality() + "," +  addresses.get(0).getAdminArea() + "," +  addresses.get(0).getCountryName() );
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });


        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

//        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,5, (LocationListener) getActivity());

//        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        try {
                            addressList = geocoder.getFromLocation(lat, lon, 1);
                            Log.d("location", String.valueOf(lat));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {

                                Location location1 = locationResult.getLastLocation();


                            }
                        };
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }


                }
            });

        }else {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }


//        if(location != null){
//            double lat = location.getLatitude();
//            double lon = location.getLongitude();
//
//            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//
//            try {
//                addressList  =geocoder.getFromLocation(lat, lon, 1);
//                Log.d("location", String.valueOf(lat));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
    }


    @Override
    public boolean onBackPressed() {

        return true;

    }

    private void LanguageChoose(){
        if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(getActivity(),"en");
            resources =context.getResources();

            // Text In English

            search.setText(resources.getString(R.string.search));



        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(getActivity(),"hi");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));

        }
        else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(getActivity(),"kn");
            resources =context.getResources();

            search.setText(resources.getString(R.string.search));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(getActivity(),"te");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));

            // Text in Telugu

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(getActivity(),"bn");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(getActivity(),"gu");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));



        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(getActivity(),"ml");
            resources =context.getResources();


            search.setText(resources.getString(R.string.search));
//
        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(getActivity(),"mr");
            resources =context.getResources();


            search.setText(resources.getString(R.string.search));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(getActivity(),"pa");
            resources =context.getResources();


            search.setText(resources.getString(R.string.search));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(getActivity(),"ta");
            resources =context.getResources();

            // Text in Tamil

            search.setText(resources.getString(R.string.search));

        }


    }


}