package com.lucky.roadpilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucky.roadpilot.Adapter.AutoFillAdapter;
import com.lucky.roadpilot.Adapter.UserAdapter;
import com.lucky.roadpilot.Models.Location;
import com.lucky.roadpilot.Models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllUsersActivity extends AppCompatActivity {


    RecyclerView user_rec;
    AutoCompleteTextView Search;
    ArrayList<Location> location;
    UserAdapter jobAdapter;
    ArrayList<UserModel> arrayList;

    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String locations;
    Context context;
    Resources resources;
    private LinearLayout error;

    private TextView all, cities, ranges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Search = findViewById(R.id.search);
        user_rec = findViewById(R.id.user_rec);
        all = findViewById(R.id.all);
        cities = findViewById(R.id.cities);
        ranges = findViewById(R.id.range);
        error = findViewById(R.id.error);
        arrayList = new ArrayList<>();

        location = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        final String[] Location = new String[1];

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Location[0] = "" + ds.child("Location").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                location.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    com.lucky.roadpilot.Models.Location model = ds.getValue(com.lucky.roadpilot.Models.Location.class);
                    location.add(model);
                }
                AutoFillAdapter adapter = new AutoFillAdapter(AllUsersActivity.this ,  location);
                Search.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                jobAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                jobAdapter.getFilter().filter(charSequence);

                AutoFillAdapter adapter = new AutoFillAdapter(AllUsersActivity.this,  location);
                Search.setAdapter(adapter);

                FilterCity(Search.getText().toString());


//                if(models.isEmpty()){
//                    error.setVisibility(View.VISIBLE);
//                }else {
//                    error.setVisibility(View.GONE);
//                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FilterCity(Search.getText().toString());

            }
        });


    }

    private void FilterCity(String toString) {


        DatabaseReference owners = FirebaseDatabase.getInstance().getReference();
        owners.keepSynced(true);
        owners.child("Owner").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                        UserModel postModel = ds.getValue(UserModel.class);
                        arrayList.add(postModel);


                    if (arrayList.isEmpty()) {
                        error.setVisibility(View.VISIBLE);
                    } else {
                        error.setVisibility(View.GONE);
                    }

                }
                jobAdapter = new UserAdapter(AllUsersActivity.this, arrayList);
                user_rec.setAdapter(jobAdapter);
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Search.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();

//        getLocation();
//        LanguageChoose();

        DatabaseReference owners = FirebaseDatabase.getInstance().getReference();
        owners.keepSynced(true);
        owners.child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                        UserModel postModel = ds.getValue(UserModel.class);
                        arrayList.add(postModel);


                    if(arrayList.isEmpty()){
                        error.setVisibility(View.VISIBLE);
                    }else {
                        error.setVisibility(View.GONE);
                    }

                }
                jobAdapter = new UserAdapter(AllUsersActivity.this,arrayList);
                user_rec.setAdapter(jobAdapter);
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference user = FirebaseDatabase.getInstance().getReference();
        user.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                locations = "" + snapshot.child("Location").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}