package com.lucky.roadpilot;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucky.roadpilot.Registers.DhabaRegister;
import com.lucky.roadpilot.Registers.DriverRegister;
import com.lucky.roadpilot.Registers.MechRegister;
import com.lucky.roadpilot.Registers.OwnerRegister;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class HomeActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    private static final int RC_SIGN_IN_R = 102;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addresses;
    AlertDialog dialog;
    private TextView text_l,text_lt,text_ll,dialog_language;
    private RelativeLayout show_lan_dialog;
    Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;

    int lang_selected;
    Resources resources;

//    private TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;

    TextView choose,login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        choose = findViewById(R.id.choose);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Google Sign In..... ");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
//        t1 = findViewById(R.id.t1);
//        t2 = findViewById(R.id.t2);
//        t3 = findViewById(R.id.t3);
//        t4 = findViewById(R.id.t4);
//        t5 = findViewById(R.id.t5);
//        t6 = findViewById(R.id.t6);
//        t7 = findViewById(R.id.t7);
//        t8 = findViewById(R.id.t8);
//        t9 = findViewById(R.id.t9);

//        CardView Owner = findViewById(R.id.owner);
//
//        Owner.setOnClickListener(v -> {
//            Intent owner = new Intent(HomeActivity.this,RegisterActivity.class);
//            owner.putExtra("cat","owner");
//            startActivity(owner);
//        });
//
//        CardView Driver = findViewById(R.id.driver);
//
//        Driver.setOnClickListener(v -> {
//            Intent driver = new Intent(HomeActivity.this,RegisterActivity.class);
//            driver.putExtra("cat","driver");
//            startActivity(driver);
//        });
//
//        CardView daba = findViewById(R.id.daba);
//
//        daba.setOnClickListener(v -> {
//            Intent Dhaba = new Intent(HomeActivity.this,RegisterActivity.class);
//            Dhaba.putExtra("cat","Dhaba");
//            startActivity(Dhaba);
//        });
//
//        CardView mech = findViewById(R.id.mech);
//
//        mech.setOnClickListener(v -> {
//            Intent Mech = new Intent(HomeActivity.this,RegisterActivity.class);
//            Mech.putExtra("cat","Mech");
//            startActivity(Mech);
//        });



        // Drop Down Menu of Truck Owner, Truck Driver, Dhaba Owner, Truck Mech ....
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(choose.getText().toString())){
//                    startActivity(new Intent(HomeActivity.this, OwnerRegister.class));
//                }else if(choose.getText().toString().equals(resources.getString(R.string.drivers))){
//                    startActivity(new Intent(HomeActivity.this, DriverRegister.class));
//                }else if(choose.getText().toString().equals(resources.getString(R.string.dhabas))){
//                    startActivity(new Intent(HomeActivity.this, DhabaRegister.class));
//                }else if(choose.getText().toString().equals(resources.getString(R.string.mechanics))){
//                    startActivity(new Intent(HomeActivity.this, MechRegister.class))
                    GoogleSignIN();

                }else {
                    Toast.makeText(HomeActivity.this, "Please Select the Category", Toast.LENGTH_SHORT).show();
                }
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBox();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(choose.getText().toString())){
//                    startActivity(new Intent(HomeActivity.this, OwnerRegister.class));
//                }else if(choose.getText().toString().equals(resources.getString(R.string.drivers))){
//                    startActivity(new Intent(HomeActivity.this, DriverRegister.class));
//                }else if(choose.getText().toString().equals(resources.getString(R.string.dhabas))){
//                    startActivity(new Intent(HomeActivity.this, DhabaRegister.class));
//                }else if(choose.getText().toString().equals(resources.getString(R.string.mechanics))){
//                    startActivity(new Intent(HomeActivity.this, MechRegister.class));
                    GoogleSignINR();
                }else {
                    Toast.makeText(HomeActivity.this, "Please Select the Category", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void GoogleSignINR() {
        progressDialog.show();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_R);

//        ShowLoginDialogBox("register");
    }


    private void showBox() {
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        }else {
            alert = new AlertDialog.Builder(this);
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.apply_lay,null);

        TextView owner = view.findViewById(R.id.owner);
        TextView Driver = view.findViewById(R.id.Driver);
        TextView Dhaba = view.findViewById(R.id.Dhaba);
        TextView mech = view.findViewById(R.id.mech);

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose.setText(resources.getString(R.string.owners));
                dialog.dismiss();
            }
        });

        Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose.setText(resources.getString(R.string.drivers));
                dialog.dismiss();
            }
        });

        Dhaba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose.setText(resources.getString(R.string.dhabas));
                dialog.dismiss();
            }
        });

        mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose.setText(resources.getString(R.string.mechanics));
                dialog.dismiss();
            }
        });

        alert.setView(view);

        alert.setCancelable(true);





        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


    }

    private void GoogleSignIN(){
        progressDialog.show();
        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
                progressDialog.dismiss();
                Toast.makeText(this, "Google sign in failed"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                ShowLoginDialogBox("login");

            }
        }else  if (requestCode == RC_SIGN_IN_R) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogler(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
                progressDialog.dismiss();
                Toast.makeText(this, "Google sign in failed"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                ShowLoginDialogBox("register");
            }
        }
    }

    private void ShowLoginDialogBox(String login) {

        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        }else {
            alert = new AlertDialog.Builder(this);
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_email_login,null);


        TextView text1 = view.findViewById(R.id.text1);
        EditText mail = view.findViewById(R.id.mail);
        EditText password = view.findViewById(R.id.password);
        TextView sign = view.findViewById(R.id.sign);

        if(login.equals("register")){
            text1.setText("Sorry for the inconvenience Please Register Here");
            sign.setText(R.string.singup_here);

            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();


                    if(!TextUtils.isEmpty(mail.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){

                        mAuth.createUserWithEmailAndPassword(mail.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();

                                    sendmail();

                                    if(choose.getText().toString().equals(resources.getString(R.string.owners))){
                                        Intent intent = new Intent(getApplication(),OwnerRegister.class);
                                        intent.putExtra("pass",password.getText().toString());
                                        intent.putExtra("mail",mail.getText().toString());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }else if(choose.getText().toString().equals(resources.getString(R.string.drivers))){
                                        Intent intent = new Intent(getApplication(),DriverRegister.class);
                                        intent.putExtra("pass",password.getText().toString());
                                        intent.putExtra("mail",mail.getText().toString());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }else if(choose.getText().toString().equals(resources.getString(R.string.mechanics))){
                                        Intent intent = new Intent(getApplication(),MechRegister.class);
                                        intent.putExtra("pass",password.getText().toString());
                                        intent.putExtra("mail",mail.getText().toString());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }else if(choose.getText().toString().equals(resources.getString(R.string.dhabas))){
                                        Intent intent = new Intent(getApplication(),DhabaRegister.class);
                                        intent.putExtra("pass",password.getText().toString());
                                        intent.putExtra("mail",mail.getText().toString());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }





                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(HomeActivity.this, "Email Already Exists!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }
            });

        }
        else if(login.equals("login")){
            text1.setText("Sorry for the inconvenience Please Login Here");
            sign.setText(R.string.singin);

            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    if(!TextUtils.isEmpty(mail.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){

                        mAuth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(HomeActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(HomeActivity.this, "Please Fill the Login Details", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }


        alert.setView(view);

        alert.setCancelable(true);

        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        
    }

    private void sendmail() {

        final String username = "Roadkapilot@gmail.com";
        final String password = "Nits@7007";
        String message = "प्रिय ड्राइवर और मालिक,\n" +
                "\n" +
                "भारत के पहले डिजिटल प्लेटफॉर्म रोड पायलट को डाउनलोड करने के लिए धन्यवाद। यह संदेश आपको यह सूचित करने के लिए भेजा गया है कि आपने एप्लिकेशन में अपना व्यक्तिगत नाम और मोबाइल नंबर और स्थान अपडेट नहीं किया है। इसलिए आपसे अनुरोध है कि आप सभी सुविधाओं का लाभ उठाएं। रोड पायलट, कृपया अपने आवेदन में आपसे संबंधित सभी जानकारी जल्द से जल्द प्रदान करें ताकि हम आपको सेवाओं का बेहतर लाभ प्रदान कर सकें। कृपया ध्यान न दें यदि आपने विवरण अपडेट किया है\n" +
                "धन्यवाद\n" +
                "टीम रोड पायलट";
        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username, password);
                    }

                });

        try{
            Message message1 = new MimeMessage(session);
            message1.setFrom(new InternetAddress(username));
            message1.setRecipients(Message.RecipientType.TO,InternetAddress.parse(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
            message1.setSubject("Complete Registration at Road Pilot");
            message1.setText(message);
            Transport.send(message1);
            Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(HomeActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                            Toast.makeText(HomeActivity.this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show();



                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
//        Toast.makeText(this, "SuccessFull", Toast.LENGTH_SHORT).show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Login").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((""+snapshot.child("log").getValue()).equals(mAuth.getCurrentUser().getEmail())){
                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        progressDialog.dismiss();
                }else if(!snapshot.exists()){
                    Toast.makeText(HomeActivity.this, "Please Register Your Self in Road Pilot", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        Intent intent = new Intent(this,HomeScreen.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        progressDialog.dismiss();

        // info save

    }



    private void firebaseAuthWithGoogler(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIr(user,choose.getText().toString());
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            updateUIr(null,choose.getText().toString());
                            Toast.makeText(HomeActivity.this, "Please Try again.....", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUIr(FirebaseUser user, String cat) {

        if(choose.getText().toString().equals(resources.getString(R.string.owners))){
            Intent intent = new Intent(this,OwnerRegister.class);
            intent.putExtra("pass","");
            intent.putExtra("mail","");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            progressDialog.dismiss();
        }else if(choose.getText().toString().equals(resources.getString(R.string.drivers))){
            Intent intent = new Intent(this,DriverRegister.class);
            intent.putExtra("pass","");
            intent.putExtra("mail","");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            progressDialog.dismiss();
        }else if(choose.getText().toString().equals(resources.getString(R.string.mechanics))){
            Intent intent = new Intent(this,MechRegister.class);
            intent.putExtra("pass","");
            intent.putExtra("mail","");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            progressDialog.dismiss();
        }else if(choose.getText().toString().equals(resources.getString(R.string.dhabas))){
            Intent intent = new Intent(this,DhabaRegister.class);
            intent.putExtra("pass","");
            intent.putExtra("mail","");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            progressDialog.dismiss();
        }

    }






    // Location Method

    @Override
    protected void onStart() {
        super.onStart();

        showDialogBox();

        if(ActivityCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getGoLocation();
        }else {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }

    private void showDialogBox() {

        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        }else {
            alert = new AlertDialog.Builder(this);
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.languages_row,null);

        dialog_language = view.findViewById(R.id.dialog_language);
        text_l = view.findViewById(R.id.text_l);
        text_lt = view.findViewById(R.id.text_lt);
        text_ll = view.findViewById(R.id.text_ll);
        show_lan_dialog = view.findViewById(R.id.showlangdialog);

        if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(HomeActivity.this,"en");
            resources =context.getResources();
            dialog_language.setText("ENGLISH");

            // Text In English
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));

//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));



            setTitle(resources.getString(R.string.app_name));
            lang_selected = 0;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(HomeActivity.this,"hi");
            resources =context.getResources();
            dialog_language.setText("हिन्दी");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));
//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));



            setTitle(resources.getString(R.string.app_name));
            lang_selected =1;
        }
        else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(HomeActivity.this,"kn");
            resources =context.getResources();
            dialog_language.setText("ಕನ್ನಡ");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));
//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));


            setTitle(resources.getString(R.string.app_name));
            lang_selected =2;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(HomeActivity.this,"te");
            resources =context.getResources();
            dialog_language.setText("తెలుగు");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));
//
//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =3;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(HomeActivity.this,"bn");
            resources =context.getResources();
            dialog_language.setText("বাংলা");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));

//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =4;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(HomeActivity.this,"gu");
            resources =context.getResources();
            dialog_language.setText("ગુજરાતી");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));

//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =5;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(HomeActivity.this,"ml");
            resources =context.getResources();
            dialog_language.setText("മലയാളം");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));

//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =6;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(HomeActivity.this,"mr");
            resources =context.getResources();
            dialog_language.setText("मराठी");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));

//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =7;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(HomeActivity.this,"pa");
            resources =context.getResources();
            dialog_language.setText("ਪੰਜਾਬੀ");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));

//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =8;
        }else if(LocaleHelper.getLanguage(HomeActivity.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(HomeActivity.this,"ta");
            resources =context.getResources();
            dialog_language.setText("தமிழ்");
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_l.setText(resources.getString(R.string.languages));

//            t1.setText(resources.getString(R.string.i_am_a));
//            t3.setText(resources.getString(R.string.i_am_a));
//            t5.setText(resources.getString(R.string.i_am_a));
//            t7.setText(resources.getString(R.string.i_am_a));
//            t2.setText(resources.getString(R.string.owners));
//            t4.setText(resources.getString(R.string.drivers));
//            t6.setText(resources.getString(R.string.dhabas));
//            t8.setText(resources.getString(R.string.mechanics));
//            t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =9;
        }
        show_lan_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] Language = {"ENGLISH","हिन्दी","ಕನ್ನಡ","తెలుగు","বাংলা","ગુજરાતી","മലയാളം","मराठी","தமிழ்"};
                final int checkItem;
                Log.d("Clicked","Clicked");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                dialogBuilder.setTitle("Select a Language")
                        .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog_language.setText(Language[i]);
                                if(Language[i].equals("ENGLISH")){
                                    context = LocaleHelper.setLocale(HomeActivity.this,"en");
                                    resources =context.getResources();
                                    lang_selected = 0;
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));

//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }
                                if(Language[i].equals("हिन्दी"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"hi");
                                    resources =context.getResources();
                                    lang_selected = 1;
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));


                                    setTitle(resources.getString(R.string.app_name));
                                }
                                if(Language[i].equals("ಕನ್ನಡ"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"kn");
                                    resources =context.getResources();
                                    lang_selected = 2;
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("తెలుగు"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"te");
                                    resources =context.getResources();
                                    lang_selected = 3;

                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("বাংলা"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"bn");
                                    resources =context.getResources();
                                    lang_selected = 4;

                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("ગુજરાતી"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"gu");
                                    resources =context.getResources();
                                    lang_selected = 5;

                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("മലയാളം"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"ml");
                                    resources =context.getResources();
                                    lang_selected = 6;

                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("मराठी"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"mr");
                                    resources =context.getResources();
                                    lang_selected = 7;

                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }
//                                if(Language[i].equals("ਪੰਜਾਬੀ"))
//                                {
//                                    context = LocaleHelper.setLocale(HomeActivity.this,"pa");
//                                    resources =context.getResources();
//                                    lang_selected = 8;
//
//                                    text_ll.setText(resources.getString(R.string.languages));
//                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
//                                    text_l.setText(resources.getString(R.string.languages));
////                                    t1.setText(resources.getString(R.string.i_am_a));
////                                    t3.setText(resources.getString(R.string.i_am_a));
////                                    t5.setText(resources.getString(R.string.i_am_a));
////                                    t7.setText(resources.getString(R.string.i_am_a));
////                                    t2.setText(resources.getString(R.string.owners));
////                                    t4.setText(resources.getString(R.string.drivers));
////                                    t6.setText(resources.getString(R.string.dhabas));
////                                    t8.setText(resources.getString(R.string.mechanics));
////                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));
//
//                                    setTitle(resources.getString(R.string.app_name));
//                                }
                                if(Language[i].equals("தமிழ்"))
                                {
                                    context = LocaleHelper.setLocale(HomeActivity.this,"ta");
                                    resources =context.getResources();
                                    lang_selected = 8;

                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
//                                    t1.setText(resources.getString(R.string.i_am_a));
//                                    t3.setText(resources.getString(R.string.i_am_a));
//                                    t5.setText(resources.getString(R.string.i_am_a));
//                                    t7.setText(resources.getString(R.string.i_am_a));
//                                    t2.setText(resources.getString(R.string.owners));
//                                    t4.setText(resources.getString(R.string.drivers));
//                                    t6.setText(resources.getString(R.string.dhabas));
//                                    t8.setText(resources.getString(R.string.mechanics));
//                                    t9.setText(resources.getString(R.string.thank_you_choosing_the_roadpilot_n_please_select_the_role_and_continue_the_process));

                                    setTitle(resources.getString(R.string.app_name));
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                dialog.dismiss();
                            }
                        });
                dialogBuilder.create().show();
            }
        });





        alert.setView(view);

        alert.setCancelable(true);



        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {

            Location location = task.getResult();
            if(location != null){

                try {
                    Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(),1
                    );
//                        shopcountryedt.setText(addresses.get(0).getCountryName());
//                        shopstateedt.setText(addresses.get(0).getAdminArea());
//                        shopcityedt.setText(addresses.get(0).getLocality());
//                        shopaddresstotaledt.setText(addresses.get(0).getAddressLine(0));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}