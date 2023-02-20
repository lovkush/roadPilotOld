package com.lucky.roadpilot;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements LocationListener {

    private static final int PICK_FILE = 1;
    private static final int PICK_DRIVER_AADHAR_FRONT = 2;
    private static final int PICK_DRIVER_AADHAR_BACK = 3;
    private static final int PICK_DRIVER_PAN_FRONT = 4;
    private static final int PICK_DRIVER_DRIVING_FRONT = 6;
    private static final int PICK_DRIVER_DRIVING_BACK = 7;
    private static final int PICK_DRIVER_CAR = 8;
    private static final int OPICK_FILE = 9;

    // Dhaba Elements
    private static final int OPICK_DRIVER_AADHAR_FRONT = 12;
    private static final int OPICK_DRIVER_AADHAR_BACK = 13;
    private static final int OPICK_DRIVER_PAN_FRONT = 41;
    private static final int DPICK_FILE = 29;

    // Mech Elements
    private static final int DPICK_DRIVER_AADHAR_FRONT = 212;
    private static final int DPICK_DRIVER_AADHAR_BACK = 213;
    private static final int MPICK_FILE = 239;
    private static final int MPICK_DRIVER_AADHAR_FRONT = 2312;
    private static final int MPICK_DRIVER_AADHAR_BACK = 2313;
    private static final int DPICK_DRIVER_PAN_FRONT = 2431;
    private static final int CPICK_FILE = 10;
    private static final int CPICK_DRIVER_AADHAR_FRONT = 20;
    RelativeLayout owner, Downer, Dhaba, Mname;
    LinearLayout login, Dlogin, Blogin, Mlogin;
    // Driver Elements
    ImageView Duser_image, Dfr_img, Dbk_img, Dfrp_img, dri_fr_img, dri_img, car_img;
    EditText IName_Dri, Dphone, Demail, Dpassword;
    TextView Daddress;
    // Owner Elements
    ImageView user_image, fr_img, bk_img, pan_front;
    EditText IName, CName, phone, email, password;
    TextView address;
    ImageView D_image, Dh_user_image, dfr_img, ba_img;
    EditText IName_Da, dphone, demail, dpassword, IName_Dha, High_way;
    TextView daddress;
    RadioButton parking, bath, sec, tv;
    ImageView M_image, Mfr_img, Mba_img;
    EditText IName_M, Mphone, Memail, Mpassword, brand;
    TextView Maddress;
    RadioButton electric, engine, suspensio, dandp;
    String[] cameraPermission;
    String[] storagePermission;
    TextView Driver_Submit, Owner_Submit, Dhaba_Submit, mech_Submit;
    UploadTask uploadTask1, uploadTask2, uploadTask3, uploadTask4, uploadTask6, uploadTask7, uploadTask8;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");
    MediaController mediaController;
    Uri DownloadImg, DowmloadAf, DownloadAb, DownloadPf, DownloadDf, DownloadDb, DownloadCat;
    String value, Driver_Name, Driver_Email, Driver_Phone, Driver_Address, Driver_pass;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList;
    Context context;
    Resources resources;
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t18, t17, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31, t32, t33, t34, t35, t36, t37, t38, t39, t40, t41, t42, t43, t45, t44, t46, t47;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri selectedUri, DriverAadharFront, DriverAadharBack, DriverPanFront, DriverDriFront, DriverDriBack, DriverCar;
    private Uri OwnerSelectedUri, OwnerAadharFront, OwnerAadharBack, OwnerPanFront;
    private Uri DhabaSelectedUri, DhabaAadharFront, DhabaAadharBack, DhabaPanFront;
    private Uri MechSelectedUri, MechAadharFront, MechAadharBack;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        value = intent.getStringExtra("cat");

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        mediaController = new MediaController(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        t6 = findViewById(R.id.t6);
        t7 = findViewById(R.id.t7);
        t8 = findViewById(R.id.t8);
        t9 = findViewById(R.id.t9);
        t10 = findViewById(R.id.t10);
        t11 = findViewById(R.id.t11);
        t12 = findViewById(R.id.t12);
        t13 = findViewById(R.id.t13);
        t14 = findViewById(R.id.t14);
        t15 = findViewById(R.id.t15);
        t16 = findViewById(R.id.t16);
        t17 = findViewById(R.id.t17);
        t18 = findViewById(R.id.t18);
        t19 = findViewById(R.id.t19);
        t20 = findViewById(R.id.t20);
        t21 = findViewById(R.id.t21);
        t22 = findViewById(R.id.t22);
        t23 = findViewById(R.id.t23);
        t24 = findViewById(R.id.t24);
        t25 = findViewById(R.id.t25);
        t26 = findViewById(R.id.t26);
        t27 = findViewById(R.id.t27);
        t28 = findViewById(R.id.t28);
        t29 = findViewById(R.id.t29);
        t30 = findViewById(R.id.t30);
        t31 = findViewById(R.id.t31);
        t32 = findViewById(R.id.t32);
        t33 = findViewById(R.id.t33);
        t34 = findViewById(R.id.t34);
        t35 = findViewById(R.id.t35);
        t36 = findViewById(R.id.t36);
        t37 = findViewById(R.id.t37);
        t38 = findViewById(R.id.t38);
        t39 = findViewById(R.id.t39);
        t40 = findViewById(R.id.t40);
        t41 = findViewById(R.id.t41);
        t42 = findViewById(R.id.t42);
        t43 = findViewById(R.id.t43);
        t44 = findViewById(R.id.t44);
        t45 = findViewById(R.id.t45);
        t46 = findViewById(R.id.t46);
        t47 = findViewById(R.id.t47);


        Duser_image = findViewById(R.id.Duser_image);
        Dfr_img = findViewById(R.id.Dfr_img);
        car_img = findViewById(R.id.car_img);
        Dbk_img = findViewById(R.id.Dbk_img);
        Dfrp_img = findViewById(R.id.Dfrp_img);
        dri_fr_img = findViewById(R.id.dri_fr_img);
        dri_img = findViewById(R.id.dri_img);
        IName_Dri = findViewById(R.id.IName_Dri);
        Daddress = findViewById(R.id.Daddress);
        Dphone = findViewById(R.id.Dphone);
        Demail = findViewById(R.id.Driver_email);
        Dpassword = findViewById(R.id.Dpassword);
        Driver_Submit = findViewById(R.id.Driver_Submit);
        Mname = findViewById(R.id.Mname);
        owner = findViewById(R.id.owner);
        Downer = findViewById(R.id.Downer);
        Dhaba = findViewById(R.id.Dhaba);
        Mlogin = findViewById(R.id.Mlogin);
        Blogin = findViewById(R.id.Blogin);
        Dlogin = findViewById(R.id.Dlogin);
        login = findViewById(R.id.login);
        user_image = findViewById(R.id.user_image);
        fr_img = findViewById(R.id.fr_img);
        bk_img = findViewById(R.id.bk_img);
        pan_front = findViewById(R.id.frp_img);
        IName = findViewById(R.id.IName);
        CName = findViewById(R.id.CName);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Owner_Submit = findViewById(R.id.Owner_Submit);
        D_image = findViewById(R.id.D_image);
        Dh_user_image = findViewById(R.id.Dh_user_image);
        dfr_img = findViewById(R.id.dfr_img);
        IName_Da = findViewById(R.id.IName_Da);
        daddress = findViewById(R.id.daddress);
        dphone = findViewById(R.id.dphone);
        demail = findViewById(R.id.demail);
        dpassword = findViewById(R.id.dpassword);
        IName_Dha = findViewById(R.id.IName_Dha);
        High_way = findViewById(R.id.High_way);
        Dhaba_Submit = findViewById(R.id.Dhaba_Submit);
        ba_img = findViewById(R.id.ba_img);
        parking = findViewById(R.id.parking);
        bath = findViewById(R.id.bath);
        sec = findViewById(R.id.sec);
        tv = findViewById(R.id.tv);
        M_image = findViewById(R.id.M_image);
        Mfr_img = findViewById(R.id.Mfr_img);
        Mba_img = findViewById(R.id.Mba_img);
        IName_M = findViewById(R.id.IName_M);
        Maddress = findViewById(R.id.Maddress);
        Mphone = findViewById(R.id.Mphone);
        Memail = findViewById(R.id.Memail);
        Mpassword = findViewById(R.id.Mpassword);
        brand = findViewById(R.id.brand);
        electric = findViewById(R.id.electric);
        engine = findViewById(R.id.engine);
        suspensio = findViewById(R.id.suspensio);
        dandp = findViewById(R.id.dandp);
        mech_Submit = findViewById(R.id.mech_Submit);

        switch (value) {
            case "owner":
                owner.setVisibility(View.VISIBLE);
                Mname.setVisibility(View.GONE);
                Downer.setVisibility(View.GONE);
                Dhaba.setVisibility(View.GONE);
                OwnerRegisterMethod();
                break;
            case "driver":
                owner.setVisibility(View.GONE);
                Mname.setVisibility(View.GONE);
                Downer.setVisibility(View.VISIBLE);
                Dhaba.setVisibility(View.GONE);

                Driver_Submit.setOnClickListener(view -> {
                    if (!TextUtils.isEmpty(Daddress.getText().toString()) || !TextUtils.isEmpty(IName_Dri.getText().toString()) || !TextUtils.isEmpty(Demail.getText().toString()) || !TextUtils.isEmpty(Dpassword.getText().toString())
                            || !TextUtils.isEmpty(Dphone.getText().toString()) || selectedUri != null || DriverAadharFront != null || DriverAadharBack != null || DriverPanFront != null
                            || DriverDriFront != null || DriverDriBack != null || DriverCar != null) {
                        CreateDriverAccount();
                    } else {
                        Toast.makeText(RegisterActivity.this, "All Details Required", Toast.LENGTH_SHORT).show();
                    }
//                        DriverLogin();
                });


                break;
            case "Dhaba":
                owner.setVisibility(View.GONE);
                Mname.setVisibility(View.GONE);
                Downer.setVisibility(View.GONE);
                Dhaba.setVisibility(View.VISIBLE);

                Dhaba_Submit.setOnClickListener(view -> {

                    if (!TextUtils.isEmpty(IName_Da.getText().toString()) || !TextUtils.isEmpty(daddress.getText().toString()) || !TextUtils.isEmpty(demail.getText().toString()) || !TextUtils.isEmpty(dpassword.getText().toString())
                            || !TextUtils.isEmpty(dphone.getText().toString()) || !TextUtils.isEmpty(IName_Dha.getText().toString()) || !TextUtils.isEmpty(High_way.getText().toString()) || DhabaSelectedUri != null || DhabaAadharFront != null || DhabaAadharBack != null || DhabaPanFront != null
                    ) {
                        CreateDhabaAccount();
                    } else {
                        Toast.makeText(RegisterActivity.this, "All Details Required", Toast.LENGTH_SHORT).show();
                    }

                });

                break;
            case "Mech":
                owner.setVisibility(View.GONE);
                Mname.setVisibility(View.VISIBLE);
                Downer.setVisibility(View.GONE);
                Dhaba.setVisibility(View.GONE);

                mech_Submit.setOnClickListener(view -> {

                    if (!TextUtils.isEmpty(IName_M.getText().toString()) || !TextUtils.isEmpty(Mphone.getText().toString()) || !TextUtils.isEmpty(Mpassword.getText().toString())
                            || !TextUtils.isEmpty(Maddress.getText().toString()) || !TextUtils.isEmpty(Memail.getText().toString()) || !TextUtils.isEmpty(brand.getText().toString())
                            || MechAadharBack != null || MechAadharFront != null || MechSelectedUri != null
                    ) {
                        CreateMechAccount();
                    } else {
                        Toast.makeText(RegisterActivity.this, "All Details Required", Toast.LENGTH_SHORT).show();
                    }


                });


                break;
        }

        login.setOnClickListener(v -> LoginMethod());
        Mlogin.setOnClickListener(v -> LoginMethod());
        Blogin.setOnClickListener(v -> LoginMethod());
        Dlogin.setOnClickListener(v -> LoginMethod());

        // ** Owner Registration ** //

        user_image.setOnClickListener(v -> chooseOwnerImage());
        fr_img.setOnClickListener(v -> chooseOwnerAadnarFImage());
        bk_img.setOnClickListener(v -> chooseOwnerAadharBImage());
        pan_front.setOnClickListener(v -> chooseOwnerPanImage());

        // ** Driver Registration ** //

        Duser_image.setOnClickListener(v -> chooseFiles());
        Dfr_img.setOnClickListener(v -> chooseDriverAadharFront());
        car_img.setOnClickListener(v -> chooseCar());
        Dbk_img.setOnClickListener(v -> chooseDriverAadharBack());
        Dfrp_img.setOnClickListener(v -> chooseDriverPan());
        dri_fr_img.setOnClickListener(v -> chooseDrivingFront());
        dri_img.setOnClickListener(v -> chooseDrivingBack());

        // ** Dhaba Registration ** //

        D_image.setOnClickListener(v -> chooseDhabaOwnerImage());
        Dh_user_image.setOnClickListener(v -> chooseDhabaImage());
        dfr_img.setOnClickListener(v -> chooseDhabaAadharImage());
        ba_img.setOnClickListener(v -> chooseDhabaAadhaImage());

        // ** Mech Registration ** //

        M_image.setOnClickListener(v -> choosemImage());
        Mfr_img.setOnClickListener(v -> choosemafImage());
        Mba_img.setOnClickListener(v -> choosemabImage());


        Driver_Address = Daddress.getText().toString();
        Driver_Name = IName_Dri.getText().toString();
        Driver_Email = Demail.getText().toString();
        Driver_pass = Dpassword.getText().toString();
        Driver_Phone = Dphone.getText().toString();


    }


    private void CreateMechAccount() {

        progressDialog.setMessage("Creating Account.....");
        progressDialog.show();

        Calendar cdate = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        firebaseAuth.createUserWithEmailAndPassword(Memail.getText().toString(), Mpassword.getText().toString())
                .addOnSuccessListener(this::onSuccess).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void OwnerRegisterMethod() {

        Owner_Submit.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(IName.getText().toString()) || !TextUtils.isEmpty(CName.getText().toString()) ||
                    !TextUtils.isEmpty(address.getText().toString()) || !TextUtils.isEmpty(phone.getText().toString())
                    || !TextUtils.isEmpty(email.getText().toString()) || !TextUtils.isEmpty(password.getText().toString())
                    || OwnerSelectedUri != null || OwnerAadharFront != null || OwnerAadharBack != null || OwnerPanFront != null
            ) {
                CreateOwnerAccount();
            } else {
                Toast.makeText(RegisterActivity.this, "All Details Required", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CreateOwnerAccount() {

        progressDialog.setMessage("Creating Account.....");
        progressDialog.show();

        Calendar cdate = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(authResult -> {

                    final StorageReference reference1 = storageReference.child("Owner").child(System.currentTimeMillis() + "User_image");
                    final StorageReference reference2 = storageReference.child("Owner").child(System.currentTimeMillis() + "FAadhar");
                    final StorageReference reference3 = storageReference.child("Owner").child(System.currentTimeMillis() + "BAAdhar");
                    final StorageReference reference4 = storageReference.child("Owner").child(System.currentTimeMillis() + "Pan");

                    uploadTask1 = reference1.putFile(OwnerSelectedUri);
                    uploadTask2 = reference2.putFile(OwnerAadharFront);
                    uploadTask3 = reference3.putFile(OwnerAadharBack);
                    uploadTask4 = reference4.putFile(OwnerPanFront);


                    uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return reference1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                DownloadImg = task.getResult();

                                Task<Uri> urlTask2 = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return reference2.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            DowmloadAf = task.getResult();

                                            Task<Uri> urlTask3 = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                @Override
                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                    if (!task.isSuccessful()) {
                                                        throw task.getException();
                                                    }
                                                    return reference3.getDownloadUrl();
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        DownloadAb = task.getResult();

                                                        Task<Uri> urlTask4 = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                            @Override
                                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                if (!task.isSuccessful()) {
                                                                    throw task.getException();
                                                                }
                                                                return reference4.getDownloadUrl();
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()) {
                                                                    DownloadPf = task.getResult();
                                                                    SaveOwnerData();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void CreateDriverAccount() {

        progressDialog.setMessage("Creating Account.....");
        progressDialog.show();

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        firebaseAuth.createUserWithEmailAndPassword(Demail.getText().toString(), Dpassword.getText().toString())
                .addOnSuccessListener(authResult -> {

                    final StorageReference reference1 = storageReference.child("Drivers").child(System.currentTimeMillis() + "User_image");
                    final StorageReference reference2 = storageReference.child("Drivers").child(System.currentTimeMillis() + "FAadhar");
                    final StorageReference reference3 = storageReference.child("Drivers").child(System.currentTimeMillis() + "BAAdhar");
                    final StorageReference reference4 = storageReference.child("Drivers").child(System.currentTimeMillis() + "Pan");
                    final StorageReference reference6 = storageReference.child("Drivers").child(System.currentTimeMillis() + "FDriving");
                    final StorageReference reference7 = storageReference.child("Drivers").child(System.currentTimeMillis() + "BDriving");
                    final StorageReference reference8 = storageReference.child("Drivers").child(System.currentTimeMillis() + "DriverCart");

                    uploadTask1 = reference1.putFile(selectedUri);
                    uploadTask2 = reference2.putFile(DriverAadharFront);
                    uploadTask3 = reference3.putFile(DriverAadharBack);
                    uploadTask4 = reference4.putFile(DriverPanFront);
                    uploadTask6 = reference6.putFile(DriverDriFront);
                    uploadTask7 = reference7.putFile(DriverDriBack);
                    uploadTask8 = reference8.putFile(DriverCar);

                    Task<Uri> urlTask1 = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return reference1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                DownloadImg = task.getResult();

                                Task<Uri> urlTask2 = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return reference2.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            DowmloadAf = task.getResult();

                                            Task<Uri> urlTask3 = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                @Override
                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                    if (!task.isSuccessful()) {
                                                        throw task.getException();
                                                    }
                                                    return reference3.getDownloadUrl();
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        DownloadAb = task.getResult();

                                                        Task<Uri> urlTask4 = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                            @Override
                                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                if (!task.isSuccessful()) {
                                                                    throw task.getException();
                                                                }
                                                                return reference4.getDownloadUrl();
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()) {
                                                                    DownloadPf = task.getResult();

                                                                    Task<Uri> urlTask6 = uploadTask6.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                        @Override
                                                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                            if (!task.isSuccessful()) {
                                                                                throw task.getException();
                                                                            }
                                                                            return reference6.getDownloadUrl();
                                                                        }
                                                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DownloadDf = task.getResult();

                                                                                Task<Uri> urlTask7 = uploadTask7.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                                    @Override
                                                                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                                        if (!task.isSuccessful()) {
                                                                                            throw task.getException();
                                                                                        }
                                                                                        return reference7.getDownloadUrl();
                                                                                    }
                                                                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            DownloadDb = task.getResult();

                                                                                            Task<Uri> urlTask8 = uploadTask8.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                                                @Override
                                                                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                                                    if (!task.isSuccessful()) {
                                                                                                        throw task.getException();
                                                                                                    }
                                                                                                    return reference8.getDownloadUrl();
                                                                                                }
                                                                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Uri> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        DownloadCat = task.getResult();
                                                                                                        SaveDriverData();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });

                }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void CreateDhabaAccount() {


        progressDialog.setMessage("Creating Account.....");
        progressDialog.show();

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        firebaseAuth.createUserWithEmailAndPassword(demail.getText().toString(), dpassword.getText().toString())
                .addOnSuccessListener(authResult -> {

                    final StorageReference reference1 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "User_image");
                    final StorageReference reference2 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "FAadhar");
                    final StorageReference reference3 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "BAAdhar");
                    final StorageReference reference4 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "Dhaba");

                    uploadTask1 = reference1.putFile(DhabaSelectedUri);
                    uploadTask2 = reference2.putFile(DhabaAadharFront);
                    uploadTask3 = reference3.putFile(DhabaAadharBack);
                    uploadTask4 = reference4.putFile(DhabaPanFront);

                    Task<Uri> urlTask1 = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return reference1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                DownloadImg = task.getResult();

                                Task<Uri> urlTask2 = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return reference2.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            DowmloadAf = task.getResult();

                                            Task<Uri> urlTask3 = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                @Override
                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                    if (!task.isSuccessful()) {
                                                        throw task.getException();
                                                    }
                                                    return reference3.getDownloadUrl();
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        DownloadAb = task.getResult();

                                                        Task<Uri> urlTask4 = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                            @Override
                                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                if (!task.isSuccessful()) {
                                                                    throw task.getException();
                                                                }
                                                                return reference4.getDownloadUrl();
                                                            }
                                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> task) {
                                                                if (task.isSuccessful()) {
                                                                    DownloadPf = task.getResult();
                                                                    SaveDhabaData();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }

                    });
                }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    private void SaveDhabaData() {

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

//        if(!addresses.isEmpty()){
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//
//            hashMap.put("uid", firebaseAuth.getUid());
//            hashMap.put("name", IName_Da.getText().toString());
//            hashMap.put("Company_Name", IName_Dha.getText().toString());
//            hashMap.put("Highway", High_way.getText().toString());
//            hashMap.put("phone", dphone.getText().toString());
//            hashMap.put("email", demail.getText().toString());
//            hashMap.put("Location", addresses.get(0).getLocality());
//            hashMap.put("lat", addresses.get(0).getLatitude());
//            hashMap.put("long", addresses.get(0).getLongitude());
//            hashMap.put("Cat", "Dhaba");
//            hashMap.put("Verified", "No");
//            hashMap.put("MemberShip", "0");
//            hashMap.put("shop", "Open");
//            hashMap.put("Insurance", "None");
//            hashMap.put("Image", DownloadImg.toString());
//            hashMap.put("FAadhar", DowmloadAf.toString());
//            hashMap.put("BAadhar", DownloadAb.toString());
//            hashMap.put("Dhaba", DownloadPf.toString());
//            if(tv.isChecked()){
//                hashMap.put("tv","available");
//            }
//            if(sec.isChecked()){
//                hashMap.put("Security", "available");
//            }
//            if(parking.isChecked()){
//                hashMap.put("parking", "available");
//            }
//            if(bath.isChecked()){
//                hashMap.put("bath", "available");
//            }
//
//
//            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Dhaba").child(firebaseAuth.getUid());
//            Driver.setValue(hashMap);
//            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
//            DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
//                progressDialog.dismiss();
//                Intent intent = new Intent(RegisterActivity.this, MembershipActivity.class);
//                intent.putExtra("cat","others");
//                startActivity(intent);
//            });
//
//        }else {


        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("uid", firebaseAuth.getUid());
        hashMap.put("name", IName_Da.getText().toString());
        hashMap.put("Company_Name", IName_Dha.getText().toString());
        hashMap.put("Highway", High_way.getText().toString());
        hashMap.put("phone", dphone.getText().toString());
        hashMap.put("Location", daddress.getText().toString());
        hashMap.put("email", demail.getText().toString());
        hashMap.put("Cat", "Dhaba");
        hashMap.put("Verified", "No");
        hashMap.put("MemberShip", "0");
        hashMap.put("shop", "Open");
        hashMap.put("Insurance", "None");
        hashMap.put("Image", DownloadImg.toString());
        hashMap.put("FAadhar", DowmloadAf.toString());
        hashMap.put("BAadhar", DownloadAb.toString());
        hashMap.put("Dhaba", DownloadPf.toString());
        if (tv.isChecked()) {
            hashMap.put("tv", "available");
        } else {
            hashMap.put("tv", "unavailable");
        }
        if (sec.isChecked()) {
            hashMap.put("Security", "available");
        } else {
            hashMap.put("Security", "unavailable");
        }
        if (parking.isChecked()) {
            hashMap.put("parking", "available");
        } else {
            hashMap.put("parking", "unavailable");
        }
        if (bath.isChecked()) {
            hashMap.put("bath", "available");
        } else {
            hashMap.put("bath", "unavailable");
        }
        if (!addressList.isEmpty()) {
            hashMap.put("lon", addressList.get(0).getLongitude());
            hashMap.put("lat", addressList.get(0).getLatitude());
        }


        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Dhaba").child(firebaseAuth.getUid());
        Driver.setValue(hashMap);
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Intent intent = new Intent(RegisterActivity.this, MembershipActivity.class);
            intent.putExtra("cat", "Dhaba");
            startActivity(intent);
        });

//        }


    }

    private void saveMechData() {

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;
//        if(!addresses.isEmpty()){
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//
//            hashMap.put("uid", firebaseAuth.getUid());
//            hashMap.put("name", IName_M.getText().toString());
//            hashMap.put("Brand", brand.getText().toString());
//            hashMap.put("Highway", High_way.getText().toString());
//            hashMap.put("Verified", "No");
//            hashMap.put("phone", Mphone.getText().toString());
//            hashMap.put("Location", addresses.get(0).getLocality());
//            hashMap.put("lat", addresses.get(0).getLatitude());
//            hashMap.put("long", addresses.get(0).getLongitude());
//            hashMap.put("email", Memail.getText().toString());
//            hashMap.put("Cat", "Mech");
//            hashMap.put("MemberShip", "0");
//            hashMap.put("Insurance", "None");
//            hashMap.put("shop", "Open");
//            hashMap.put("Image", DownloadImg.toString());
//            hashMap.put("FAadhar", DowmloadAf.toString());
//            hashMap.put("BAadhar", DownloadAb.toString());
//            if (electric.isChecked()) {
//                hashMap.put("electric", "available");
//            }
//            if (engine.isChecked()) {
//                hashMap.put("engine", "available");
//            }
//            if (suspensio.isChecked()) {
//                hashMap.put("suspension", "available");
//            }
//            if (dandp.isChecked()) {
//                hashMap.put("dent", "available");
//            }
//
//
//            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Mech").child(firebaseAuth.getUid());
//            Driver.setValue(hashMap);
//            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
//            DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
//                progressDialog.dismiss();
//                Intent intent = new Intent(RegisterActivity.this, MembershipActivity.class);
//                intent.putExtra("cat", "others");
//                startActivity(intent);
//            });
//
//        }else {

        HashMap<String, Object> Map = new HashMap<>();

        Map.put("uid", firebaseAuth.getUid());
        Map.put("name", IName_M.getText().toString());
        Map.put("Brand", brand.getText().toString());
        Map.put("Verified", "No");
        Map.put("phone", Mphone.getText().toString());
        Map.put("Location", Maddress.getText().toString());
        Map.put("email", Memail.getText().toString());
        Map.put("Cat", "Mech");
        Map.put("MemberShip", "0");
        Map.put("Insurance", "None");
        Map.put("shop", "Open");
        Map.put("Image", DownloadImg.toString());
        Map.put("FAadhar", DowmloadAf.toString());
        Map.put("BAadhar", DownloadAb.toString());
        if (electric.isChecked()) {
            Map.put("electric", "available");
        } else {
            Map.put("electric", "unavailable");
        }
        if (engine.isChecked()) {
            Map.put("engine", "available");
        } else {
            Map.put("engine", "unavailable");
        }
        if (suspensio.isChecked()) {
            Map.put("suspension", "available");
        } else {
            Map.put("suspension", "unavailable");
        }
        if (dandp.isChecked()) {
            Map.put("dent", "available");
        } else {
            Map.put("dent", "unavailable");
        }
        if (!addressList.isEmpty()) {
            Map.put("lon", addressList.get(0).getLongitude());
            Map.put("lat", addressList.get(0).getLatitude());
        }


        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Mech").child(Objects.requireNonNull(firebaseAuth.getUid()));
        Driver.setValue(Map);
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        DriverRef.setValue(Map).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Intent intent = new Intent(RegisterActivity.this, MembershipActivity.class);
            intent.putExtra("cat", "mech");
            startActivity(intent);
        });
//        }

    }


    private String getFileExt(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void SaveDriverData() {

//        if(!addresses.isEmpty()){
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//
//            hashMap.put("uid", firebaseAuth.getUid());
//            hashMap.put("name", IName_Dri.getText().toString());
//            hashMap.put("phone",Dphone.getText().toString());
//            hashMap.put("Location", addresses.get(0).getLocality());
//            hashMap.put("lat", addresses.get(0).getLatitude());
//            hashMap.put("long", addresses.get(0).getLongitude());
//            hashMap.put("email", Demail.getText().toString());
//            hashMap.put("Cat", "Driver");
//            hashMap.put("MemberShip", "0");
//            hashMap.put("Image",selectedUri.toString());
//            hashMap.put("FAadhar",DriverAadharFront.toString());
//            hashMap.put("BAadhar",DriverAadharBack.toString());
//            hashMap.put("FPan",DriverPanFront.toString());
//            hashMap.put("FDriving",DriverDriFront.toString());
//            hashMap.put("BDriving",DriverDriBack.toString());
//            hashMap.put("Cart",DriverCar.toString());
//
//            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
//            Driver.setValue(hashMap);
//            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users");
//            DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
//                progressDialog.dismiss();
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//            });
//
//        }else {

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("uid", firebaseAuth.getUid());
        hashMap.put("name", IName_Dri.getText().toString());
        hashMap.put("phone", Dphone.getText().toString());
        hashMap.put("Verified", "No");
        hashMap.put("email", Demail.getText().toString());
        hashMap.put("Cat", "Driver");
        hashMap.put("MemberShip", "None");
        hashMap.put("Insurance", "None");
        hashMap.put("Location", Daddress.getText().toString());
        hashMap.put("Image", DownloadImg.toString());
        hashMap.put("FAadhar", DowmloadAf.toString());
        hashMap.put("BAadhar", DownloadAb.toString());
        hashMap.put("FPan", DownloadPf.toString());
        hashMap.put("FDriving", DownloadDf.toString());
        hashMap.put("BDriving", DownloadDb.toString());
        hashMap.put("Cart", DownloadCat.toString());
        if (!addressList.isEmpty()) {
            hashMap.put("lon", addressList.get(0).getLongitude());
            hashMap.put("lat", addressList.get(0).getLatitude());
        }

        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Drivers").child(Objects.requireNonNull(firebaseAuth.getUid()));
        Driver.setValue(hashMap);
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Intent intent = new Intent(RegisterActivity.this, MembershipActivity.class);
            intent.putExtra("cat", "Driver");
            startActivity(intent);
        });
//        }

    }

    private void SaveOwnerData() {


//        if(!addresses.isEmpty()) {
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//
//            hashMap.put("uid", firebaseAuth.getUid());
//            hashMap.put("name", IName.getText().toString());
//            hashMap.put("Company_Name", CName.getText().toString());
//            hashMap.put("phone", phone.getText().toString());
//            hashMap.put("Location", addresses.get(0).getLocality());
//            hashMap.put("lat", addresses.get(0).getLatitude());
//            hashMap.put("long", addresses.get(0).getLongitude());
//            hashMap.put("email", email.getText().toString());
//            hashMap.put("Cat", "Owner");
//            hashMap.put("MemberShip", "0");
//            hashMap.put("Image", selectedUri.toString());
//            hashMap.put("FAadhar", DriverAadharFront.toString());
//            hashMap.put("BAadhar", DriverAadharBack.toString());
//            hashMap.put("FPan", DriverPanFront.toString());
//
//            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Users").child("Owner");
//            Driver.setValue(hashMap);
//            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users");
//            DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
//                progressDialog.dismiss();
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//            });
//        }else {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("uid", firebaseAuth.getUid());
        hashMap.put("name", IName.getText().toString());
        hashMap.put("Company_Name", CName.getText().toString());
        hashMap.put("phone", phone.getText().toString());
        hashMap.put("Verified", "No");
        hashMap.put("email", email.getText().toString());
        hashMap.put("Location", address.getText().toString());
        hashMap.put("Cat", "Owner");
        hashMap.put("MemberShip", "Gold");
        hashMap.put("Insurance", "None");
        hashMap.put("Image", DownloadImg.toString());
        hashMap.put("FAadhar", DowmloadAf.toString());
        hashMap.put("BAadhar", DownloadAb.toString());
        hashMap.put("FPan", DownloadPf.toString());
        if (!addressList.isEmpty()) {
            hashMap.put("lon", addressList.get(0).getLongitude());
            hashMap.put("lat", addressList.get(0).getLatitude());
        }

        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Owner").child(Objects.requireNonNull(firebaseAuth.getUid()));
        Driver.setValue(hashMap);
        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Intent intent = new Intent(RegisterActivity.this, MembershipActivity.class);
            intent.putExtra("cat", "owner");
            startActivity(intent);
        });
//        }


    }

    private void LoginMethod() {

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

    }

    // ** Camera  Methods ** //

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("IntentReset")
    private void chooseFiles() {

//        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,PICK_FILE);

        int pick = 0;
        if (pick == 0) {
            if (!checkCameraPermission()) {
                requestCameraPermission();
            } else {
                pickFromCamera();
            }
        } else if (pick == 1) {
            if (!checkStoragePermission()) {
                requestStoragePermission();

            } else {
//                PickFromStorage();
                pickFromCamera();
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void chooseDriverAadharFront() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, PICK_DRIVER_AADHAR_FRONT);

//        int pick = 0;
//        if(pick == 0){
//            if(!checkCameraPermission()){
//                requestCameraPermission1();
//            }else {
//                pickFromCamera();
//            }
//        }else if(pick == 1){
//            if (!checkStoragePermission()) {
//                requestStoragePermission1();
//
//            }else {
////                PickFromStorage();
//                pickFromCamera();
//            }
//        }

    }

    private void chooseCar() {

        Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent2.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent2, PICK_DRIVER_CAR);

    }

    private void chooseDriverAadharBack() {

        Intent intent3 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent3.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent3, PICK_DRIVER_AADHAR_BACK);

    }

    private void chooseDriverPan() {

        Intent intent4 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent4.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent4, PICK_DRIVER_PAN_FRONT);

    }


    private void chooseDrivingFront() {

        Intent intent6 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent6.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent6, PICK_DRIVER_DRIVING_FRONT);

    }

    private void chooseDrivingBack() {

        Intent intent7 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent7.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent7, PICK_DRIVER_DRIVING_BACK);

    }

    private void chooseOwnerAadharBImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, OPICK_DRIVER_AADHAR_BACK);
    }

    private void chooseOwnerAadnarFImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, OPICK_DRIVER_AADHAR_FRONT);
    }

    private void chooseOwnerImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, OPICK_FILE);

    }

    private void chooseOwnerPanImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, OPICK_DRIVER_PAN_FRONT);

    }

    private void chooseDhabaAadhaImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, DPICK_DRIVER_AADHAR_BACK);


    }

    private void chooseDhabaAadharImage() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, DPICK_DRIVER_AADHAR_FRONT);

    }

    private void chooseDhabaImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, DPICK_DRIVER_PAN_FRONT);
    }

    private void chooseDhabaOwnerImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, DPICK_FILE);
    }

    private void choosemabImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, MPICK_DRIVER_AADHAR_BACK);
    }

    private void choosemafImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, MPICK_DRIVER_AADHAR_FRONT);
    }

    private void choosemImage() {

        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, MPICK_FILE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, PICK_FILE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission1() {
        requestPermissions(storagePermission, PICK_DRIVER_AADHAR_FRONT);
    }

    private boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    private void pickFromCamera() {

        CropImage.activity().start(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {

        requestPermissions(cameraPermission, CPICK_FILE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission1() {

        requestPermissions(cameraPermission, CPICK_DRIVER_AADHAR_FRONT);

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == PICK_FILE || resultCode == RESULT_OK || data != null || data.getData() != null){
//            selectedUri = data.getData();
//            if(selectedUri.toString().contains("image")){
//                Picasso.get().load(selectedUri).into(Duser_image);
//            }else {
//                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//            }
//        }else
//            if(requestCode == PICK_DRIVER_AADHAR_FRONT || resultCode == RESULT_OK || data != null || data.getData() != null){
//            DriverAadharFront = data.getData();
//            if(DriverAadharFront.toString().contains("image")){
//                Picasso.get().load(DriverAadharFront).into(Dfr_img);
//            }else {
//                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//            }
//        }else
//
//        if(requestCode == PICK_DRIVER_AADHAR_BACK || resultCode == RESULT_OK || data != null || data.getData() != null){
//            DriverAadharBack = data.getData();
//            if(DriverAadharBack.toString().contains("image")){
//                Picasso.get().load(DriverAadharBack).into(Dbk_img);
//            }else {
//                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//            }
//        }else
//
//        if(requestCode == PICK_DRIVER_PAN_FRONT || resultCode == RESULT_OK || data != null || data.getData() != null){
//            DriverPanFront = data.getData();
//            if(DriverPanFront.toString().contains("image")){
//                Picasso.get().load(DriverPanFront).into(Dfrp_img);
//            }else {
//                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//            }
//        }else
//
//        if(requestCode == PICK_DRIVER_DRIVING_FRONT || resultCode == RESULT_OK || data != null || data.getData() != null){
//            DriverDriFront = data.getData();
//            if(DriverDriFront.toString().contains("image")){
//                Picasso.get().load(DriverDriFront).into(dri_fr_img);
//            }else {
//                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//            }
//        }else
//
//        if(requestCode == PICK_DRIVER_DRIVING_BACK || resultCode == RESULT_OK || data != null || data.getData() != null){
//            DriverDriBack = data.getData();
//            if(DriverDriBack.toString().contains("image")){
//                Picasso.get().load(DriverDriBack).into(dri_img);
//            }else {
//                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//            }
//        }else
//
//        if(requestCode == PICK_DRIVER_CAR || resultCode == RESULT_OK || data != null || data.getData() != null){
//            DriverCar = data.getData();
//            if(DriverCar.toString().contains("image")){
//                Picasso.get().load(DriverCar).into(car_img);
//            }else {
//                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//            }
//        }
//        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PICK_DRIVER_CAR:
                    assert data != null;
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    DriverCar = result.getUri();
                    if (DriverCar.toString().contains("image")) {
                        Picasso.get().load(DriverCar).into(car_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PICK_DRIVER_DRIVING_BACK:
                    DriverDriBack = data.getData();
                    if (DriverDriBack.toString().contains("image")) {
                        Picasso.get().load(DriverDriBack).into(dri_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PICK_DRIVER_DRIVING_FRONT:
                    DriverDriFront = data.getData();
                    if (DriverDriFront.toString().contains("image")) {
                        Picasso.get().load(DriverDriFront).into(dri_fr_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PICK_DRIVER_PAN_FRONT:
                    DriverPanFront = data.getData();
                    if (DriverPanFront.toString().contains("image")) {
                        Picasso.get().load(DriverPanFront).into(Dfrp_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PICK_DRIVER_AADHAR_BACK:
                    DriverAadharBack = data.getData();
                    if (DriverAadharBack.toString().contains("image")) {
                        Picasso.get().load(DriverAadharBack).into(Dbk_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PICK_DRIVER_AADHAR_FRONT:
                    DriverAadharFront = data.getData();
                    if (DriverAadharFront.toString().contains("image")) {
                        Picasso.get().load(DriverAadharFront).into(Dfr_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
//                  CropImage.ActivityResult result12 = CropImage.getActivityResult(data);
//
//                  DriverAadharFront = result12.getUri();
//                  Picasso.get().load(DriverAadharFront).into(Dfr_img);
//                  break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result1 = CropImage.getActivityResult(data);

                    selectedUri = result1.getUri();
                    Picasso.get().load(selectedUri).into(Duser_image);

                    break;

                case OPICK_FILE:
                    OwnerSelectedUri = data.getData();
                    if (OwnerSelectedUri.toString().contains("image")) {
                        Picasso.get().load(OwnerSelectedUri).into(user_image);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case OPICK_DRIVER_AADHAR_FRONT:
                    OwnerAadharFront = data.getData();
                    if (OwnerAadharFront.toString().contains("image")) {
                        Picasso.get().load(OwnerAadharFront).into(fr_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case OPICK_DRIVER_AADHAR_BACK:
                    OwnerAadharBack = data.getData();
                    if (OwnerAadharBack.toString().contains("image")) {
                        Picasso.get().load(OwnerAadharBack).into(bk_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case OPICK_DRIVER_PAN_FRONT:
                    OwnerPanFront = data.getData();
                    if (OwnerPanFront.toString().contains("image")) {
                        Picasso.get().load(OwnerPanFront).into(pan_front);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DPICK_FILE:
                    DhabaSelectedUri = data.getData();
                    if (DhabaSelectedUri.toString().contains("image")) {
                        Picasso.get().load(DhabaSelectedUri).into(D_image);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DPICK_DRIVER_PAN_FRONT:
                    DhabaPanFront = data.getData();
                    if (DhabaPanFront.toString().contains("image")) {
                        Picasso.get().load(DhabaPanFront).into(Dh_user_image);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DPICK_DRIVER_AADHAR_FRONT:
                    DhabaAadharFront = data.getData();
                    if (DhabaAadharFront.toString().contains("image")) {
                        Picasso.get().load(DhabaAadharFront).into(dfr_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DPICK_DRIVER_AADHAR_BACK:
                    DhabaAadharBack = data.getData();
                    if (DhabaAadharBack.toString().contains("image")) {
                        Picasso.get().load(DhabaAadharBack).into(ba_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MPICK_FILE:
                    MechSelectedUri = data.getData();
                    if (MechSelectedUri.toString().contains("image")) {
                        Picasso.get().load(MechSelectedUri).into(M_image);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MPICK_DRIVER_AADHAR_FRONT:
                    MechAadharFront = data.getData();
                    if (MechAadharFront.toString().contains("image")) {
                        Picasso.get().load(MechAadharFront).into(Mfr_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MPICK_DRIVER_AADHAR_BACK:
                    MechAadharBack = data.getData();
                    if (MechAadharBack.toString().contains("image")) {
                        Picasso.get().load(MechAadharBack).into(Mba_img);
                    } else {
                        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                    }

            }
        }

    }

// Location Method

    @Override
    protected void onStart() {
        super.onStart();

        CheckLanguage();

        Toast.makeText(RegisterActivity.this, "Please Make Sure to allow Location..", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getGoLocation();
        } else {
            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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


        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addressList = geocoder.getFromLocation(lat, lon, 1);
                address.setText(addressList.get(0).getLocality());
                Daddress.setText(addressList.get(0).getLocality());
                daddress.setText(addressList.get(0).getLocality());
                Maddress.setText(addressList.get(0).getLocality());
                Log.d("location", String.valueOf(lat));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private void CheckLanguage() {

        if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "en");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));


            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "hi");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));


            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("kn")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "kn");
            resources = context.getResources();
            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));


            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "te");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));

            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));

            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "bn");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));

            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "gu");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));

            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "ml");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));

            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "mr");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));

            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("pa")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "pa");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));


            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));

            setTitle(resources.getString(R.string.app_name));
        } else if (LocaleHelper.getLanguage(RegisterActivity.this).equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(RegisterActivity.this, "ta");
            resources = context.getResources();

            t1.setText(resources.getString(R.string.owners_registration));
            t2.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName.setHint(resources.getString(R.string.individual_name));
            CName.setHint(resources.getString(R.string.company_name));
            address.setHint(resources.getString(R.string.address));
            phone.setHint(resources.getString(R.string.contact_number));
            email.setHint(resources.getString(R.string.email_id));
            password.setHint(resources.getString(R.string.password));
            t3.setText(resources.getString(R.string.let_s_get_some_proofs));
            t5.setText(resources.getString(R.string.aadhar_proof));
            t4.setText(resources.getString(R.string.front_side));
            t6.setText(resources.getString(R.string.back_side));
            t7.setText(resources.getString(R.string.pan_proof));
            t8.setText(resources.getString(R.string.front_side));
            t9.setText(resources.getString(R.string.already_have_account));
            t10.setText(resources.getString(R.string.singin));
            t11.setText(resources.getString(R.string.driver_registration));
            t12.setText(resources.getString(R.string.let_s_get_some_personal_info));
            Daddress.setHint(resources.getString(R.string.address));
            Dphone.setHint(resources.getString(R.string.contact_number));
            Demail.setHint(resources.getString(R.string.email_id));
            Dpassword.setHint(resources.getString(R.string.password));
            t13.setText(resources.getString(R.string.let_s_get_some_proofs));
            t14.setText(resources.getString(R.string.aadhar_proof));
            t15.setText(resources.getString(R.string.front_side));
            t16.setText(resources.getString(R.string.back_side));
            t17.setText(resources.getString(R.string.pan_proof));
            t18.setText(resources.getString(R.string.front_side));
            t19.setText(resources.getString(R.string.driving_license));
            t20.setText(resources.getString(R.string.front_side));
            t21.setText(resources.getString(R.string.back_side));
            t22.setText(resources.getString(R.string.character_certificate));
            Driver_Submit.setText(resources.getString(R.string.submit));
            t23.setText(resources.getString(R.string.already_have_account));
            t24.setText(resources.getString(R.string.singin));
            t25.setText(resources.getString(R.string.dhaba_registration));
            t26.setText(resources.getString(R.string.dhaba_image));
            t27.setText(resources.getString(R.string.owner_image));
            t28.setText(resources.getString(R.string.let_s_get_some_proofs));
            IName_Da.setHint(resources.getString(R.string.owner_name));
            daddress.setHint(resources.getString(R.string.address));
            dphone.setHint(resources.getString(R.string.contact_number));
            demail.setHint(resources.getString(R.string.email_id));
            dpassword.setHint(resources.getString(R.string.password));
            IName_Dha.setHint(resources.getString(R.string.dhaba_name));
            High_way.setHint(resources.getString(R.string.highway));
            t30.setText(resources.getString(R.string.let_s_know_about_dhaba));
            t31.setText(resources.getString(R.string.facilities));
            parking.setText(resources.getString(R.string.parking));
            bath.setText(resources.getString(R.string.bath_area));
            sec.setText(resources.getString(R.string._24hr_security));
            tv.setText(resources.getString(R.string.tv));
            t32.setText(resources.getString(R.string.let_s_get_some_proofs));
            t33.setText(resources.getString(R.string.aadhar_proof));
            t34.setText(resources.getString(R.string.front_side));
            t35.setText(resources.getString(R.string.back_side));
            Dhaba_Submit.setText(resources.getString(R.string.submit));
            t36.setText(resources.getString(R.string.already_have_account));
            t37.setText(resources.getString(R.string.singin));
            t38.setText(resources.getString(R.string.mechanic_registration));
            t39.setText(resources.getString(R.string.let_s_get_some_personal_info));
            IName_M.setHint(resources.getString(R.string.name));
            Maddress.setHint(resources.getString(R.string.address));
            Mphone.setHint(resources.getString(R.string.contact_number));
            Memail.setHint(resources.getString(R.string.email_id));
            Mpassword.setHint(resources.getString(R.string.password));
            t29.setText(resources.getString(R.string.let_s_know_about_your_specifications));
            t40.setText(resources.getString(R.string.specialization));
            electric.setText(resources.getString(R.string.electric));
            engine.setText(resources.getString(R.string.engine));
            suspensio.setText(resources.getString(R.string.suspension));
            dandp.setText(resources.getString(R.string.denter));
            brand.setHint(resources.getString(R.string.brand));
            t41.setText(resources.getString(R.string.brand));
            t42.setText(resources.getString(R.string.let_s_get_some_proofs));
            t43.setText(resources.getString(R.string.aadhar_proof));
            t44.setText(resources.getString(R.string.front_side));
            t45.setText(resources.getString(R.string.back_side));
            t46.setText(resources.getString(R.string.already_have_account));
            t47.setText(resources.getString(R.string.singin));
            mech_Submit.setText(resources.getString(R.string.submit));

            IName_Dri.setHint(resources.getString(R.string.individual_name));
            t11.setText(resources.getString(R.string.let_s_get_some_personal_info));

            setTitle(resources.getString(R.string.app_name));
        }
    }

    private void onSuccess(AuthResult authResult) {

        final StorageReference reference1 = storageReference.child("Mech").child(System.currentTimeMillis() + "User_image");
        final StorageReference reference2 = storageReference.child("Mech").child(System.currentTimeMillis() + "FAadhar");
        final StorageReference reference3 = storageReference.child("Mech").child(System.currentTimeMillis() + "BAAdhar");

        uploadTask1 = reference1.putFile(MechSelectedUri);
        uploadTask2 = reference2.putFile(MechAadharFront);
        uploadTask4 = reference3.putFile(MechAadharBack);

        Task<Uri> urlTask1 = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return reference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    DownloadImg = task.getResult();

                    Task<Uri> urlTask2 = uploadTask2.continueWithTask(task1 -> {
                        if (!task1.isSuccessful()) {
                            throw task1.getException();
                        }
                        return reference2.getDownloadUrl();
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                DowmloadAf = task.getResult();

                                Task<Uri> urlTask3 = uploadTask4.continueWithTask(task12 -> {
                                    if (!task12.isSuccessful()) {
                                        throw task12.getException();
                                    }
                                    return reference3.getDownloadUrl();
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            DownloadAb = task.getResult();
                                            saveMechData();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }
}