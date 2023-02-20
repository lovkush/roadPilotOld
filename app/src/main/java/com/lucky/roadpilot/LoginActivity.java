package com.lucky.roadpilot;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;

    private static final String TAG="MAIN_TAG";

    private FirebaseAuth firebaseAuth;

    private ProgressDialog pd;

    private LinearLayout phoneLl, codeLl;
    private EditText phoneEt,codeEt;
    Button phoneContinueBtn, codeSubmitBtn;
    private TextView codeSentDescription,resendCodeTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        phoneLl = findViewById(R.id.phoneLl);
        codeLl = findViewById(R.id.codeLl);
        phoneEt = findViewById(R.id.phoneEt);
        codeEt = findViewById(R.id.codeEt);
        phoneContinueBtn = findViewById(R.id.phoneContinueBtn);
        codeSentDescription = findViewById(R.id.codeSentDescription);
        codeSubmitBtn = findViewById(R.id.codeSubmitBtn);
        resendCodeTv = findViewById(R.id.resendCodeTv);


        phoneLl.setVisibility(View.VISIBLE);
        codeLl.setVisibility(View.GONE);

        firebaseAuth=FirebaseAuth.getInstance();



        pd=new ProgressDialog(this);
        pd.setTitle("Please Wait....");
        pd.setCanceledOnTouchOutside(false);




        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this ,""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String VerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(VerificationId, forceResendingToken);
                Log.e(TAG, "onCodeSent: "+VerificationId);
                mVerificationId=VerificationId;
                PhoneAuthProvider.ForceResendingToken token = null;
                pd.dismiss();

                phoneLl.setVisibility(View.GONE);
                codeLl.setVisibility(View.VISIBLE);

                Toast.makeText(LoginActivity.this, "Verification code sent..", Toast.LENGTH_SHORT).show();
                codeSentDescription.setText("Please type Verification code we sent \n "+phoneEt.getText().toString().trim());
            }
        };


        phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone="+91"+phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds: snapshot.getChildren()) {

//                                boolean p = (""+ds.child("phone").getValue()).matches(phoneEt.getText().toString());

                                if (("" + ds.child("phone").getValue()).equals(phoneEt.getText().toString()) ) {
                                    startPhoneNumberVerification(phone);
                                }else {
//                                    Toast.makeText(LoginActivity.this, "Please Register yourSelf at RoadPilot", Toast.LENGTH_SHORT).show();
                                }
//                                if (("" + ds.child("phone").getValue()).matches(phoneEt.getText().toString())) {
//                                    if (ds.exists()) {
//                                        startPhoneNumberVerification(phone);
//
//
//                                    } else {
//                                        Toast.makeText(LoginActivity.this, "Please Register YourSelf in RoadPilot", Toast.LENGTH_SHORT).show();
//
//                                    }
//
//                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Please Register YourSelf in RoadPilot", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });

        resendCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone="+91"+phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    resendVerificationCode(phone,forceResendingToken);
                }
            }
        });
        codeSubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String code= codeEt.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(LoginActivity.this, "please enter verification code", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyPhoneNumberwithCode(mVerificationId,code);
                }
            }
        });


    }

    private void verifyPhoneNumberwithCode(String VerificationId, String code) {
        pd.setMessage("Verifying code");
        pd.show();

        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VerificationId, code) ;
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage("Logging In");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(LoginActivity.this, "Logged In as"+phone , Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
//                        Toast.makeText(LoginActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        pd.setMessage("resending code");
        pd.show();
        PhoneAuthProvider.ForceResendingToken token = null;
        assert token != null;
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60l, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phone) {
        pd.setMessage("verifying phone number");
        pd.show();
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60l, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }


}