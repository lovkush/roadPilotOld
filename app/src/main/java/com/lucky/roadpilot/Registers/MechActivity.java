package com.lucky.roadpilot.Registers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lucky.roadpilot.MembershipActivity;
import com.lucky.roadpilot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class MechActivity extends AppCompatActivity {

    RadioButton electric,engine,suspensio,dandp;
    EditText brand;
    TextView submit;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        submit = findViewById(R.id.submit);
        electric = findViewById(R.id.electric);
        engine = findViewById(R.id.engine);
        suspensio = findViewById(R.id.suspensio);
        dandp = findViewById(R.id.dandp);
        brand = findViewById(R.id.brand);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(brand.getText().toString())){
                    saveDataq();
                }else {
                    Toast.makeText(MechActivity.this, "Please fill the Brand you are special at", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        suspensio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(suspensio.isChecked()){
//                    suspensio.setChecked(false);
//                }else {
//                    suspensio.setChecked(true);
//                }
//            }
//        });
//        dandp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(dandp.isChecked()){
//                    dandp.setChecked(false);
//                }else {
//                    dandp.setChecked(true);
//                }
//            }
//        });
//        engine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(engine.isChecked()){
//                    engine.setChecked(false);
//                }else {
//                    engine.setChecked(true);
//                }
//            }
//        });
//        electric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(electric.isChecked()){
//                    electric.setChecked(false);
//                }else {
//                    electric.setChecked(true);
//                }
//            }
//        });

    }

    private void saveDataq() {


        progressDialog.setMessage("Completing Registration.....");
        progressDialog.show();

        HashMap<String,Object> Map = new HashMap<>();

        Map.put("Brand", brand.getText().toString());
        if (electric.isChecked()) {
            Map.put("electric", "available");
        }else {
            Map.put("electric", "unavailable");
        }
        if (engine.isChecked()) {
            Map.put("engine", "available");
        }else {
            Map.put("engine", "unavailable");
        }
        if (suspensio.isChecked()) {
            Map.put("suspension", "available");
        }else {
            Map.put("suspension", "unavailable");
        }
        if (dandp.isChecked()) {
            Map.put("dent", "available");
        }else {
            Map.put("dent", "unavailable");
        }

        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Mech").child(Objects.requireNonNull(firebaseAuth.getUid()));
        Driver.updateChildren(Map);
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        DriverRef.updateChildren(Map).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Intent intent = new Intent(MechActivity.this, MembershipActivity.class);
            intent.putExtra("cat","Mech");
            startActivity(intent);
        });

    }
}