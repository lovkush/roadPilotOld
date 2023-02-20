package com.lucky.roadpilot.Registers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.lucky.roadpilot.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DhabaRegister extends AppCompatActivity implements LocationListener{

    ImageView user_image;
    EditText IName,phone, verify,Caddress,address;
    TextView submit,reset;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    AlertDialog dialog;

    private LocationRequest locationRequest;
    double latitude,longitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList;
    String cameraPermission[];
    String storagePermission[];
    Uri resultUri;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");
    MediaController mediaController;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    private FaceDetector faceDetector;
    private LottieAnimationView image;
    private CardView cardView5;
    private  int count = 0000;
    String pass,mail,emailAddress;

    private static final String TAG="MAIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaba_register);


        image = findViewById(R.id.image);
        cardView5 = findViewById(R.id.cardView5);
        verify = findViewById(R.id.Dpassword);
        user_image = findViewById(R.id.Duser_image);
        IName = findViewById(R.id.IName_Dri);
        phone = findViewById(R.id.Dphone);
        Caddress = findViewById(R.id.Caddress);
        address = findViewById(R.id.Daddress);
        submit = findViewById(R.id.submit);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String phone1=phone.getText().toString().trim();
//                if(TextUtils.isEmpty(phone1)) {
//                    Toast.makeText(DhabaRegister.this, "Please Enter Phone number", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    resendVerificationCode(phone1,forceResendingToken);
//                }
//            }
//        });
        Intent intent = getIntent();
        pass = intent.getStringExtra("pass");
        mail = intent.getStringExtra("mail");

        if(mail.equals("")){
            emailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }else{
            emailAddress = mail;
        }


        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(DhabaRegister.this ,""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String VerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(VerificationId, forceResendingToken);
                Log.e(TAG, "onCodeSent: "+VerificationId);
                mVerificationId=VerificationId;
                PhoneAuthProvider.ForceResendingToken token = null;
                progressDialog.dismiss();


                Toast.makeText(DhabaRegister.this, "Verification code sent..", Toast.LENGTH_SHORT).show();
                //  codeSentDescription.setText("Please type Verification code we sent \n "+phoneEt.getText().toString().trim());
                Toast.makeText(DhabaRegister.this, "Please type Verification code we sent \n "+phone.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        };



        mediaController = new MediaController(this);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View view) {
                startDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(phone.getText().toString().length() < 10 || phone.getText().toString().startsWith("+91")){
                     phone.setError("Please fill the correct phone number");
                } else if(!TextUtils.isEmpty(IName.getText().toString().trim())
                        && !TextUtils.isEmpty(address.getText().toString().trim()) && !TextUtils.isEmpty(phone.getText().toString().trim())
                        && resultUri != null && faceDetector.isOperational() && user_image.getDrawable() != null || !TextUtils.isEmpty(Caddress.getText().toString().trim())){

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists()){
                                StartRegister();
                            }else {
                                Toast.makeText(DhabaRegister.this, "Already Exist this Account", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else if(phone.getText().toString().length() > 10){
                    Toast.makeText(DhabaRegister.this, "Please enter the valid phone Number", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DhabaRegister.this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



    }

    private void DilagBox() {

        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        }else {
            alert = new AlertDialog.Builder(this);
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.verfication_code,null);

        EditText verify_code = view.findViewById(R.id.verify_code);
        TextView submit = view.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = verify_code.getText().toString().trim();

                verifyPhoneNumberwithCode(mVerificationId,code);
            }
        });

        reset = view.findViewById(R.id.resend);


        alert.setView(view);

        alert.setCancelable(true);





        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void resendVerificationCode(String phone1, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

        progressDialog.setMessage("resending code");
        progressDialog.show();
        PhoneAuthProvider.ForceResendingToken token = null;
        assert token != null;
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone1)
                        .setTimeout(60l, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phone) {
        progressDialog.setMessage("verifying phone number");
        progressDialog.show();
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60l, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startDialog() {

        int pick = 0;
        if(pick == 0){
            if(!checkCameraPermission()){
                requestCameraPermission();
            }else {
                pickFromCamera();
            }
        }else if(pick == 1){
            if (!checkStoragePermission()) {
                requestStoragePermission();

            }else {
//                PickFromStorage();
                pickFromCamera();
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission,GALLERY_PICTURE);
    }

    private boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(DhabaRegister.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    private void pickFromCamera() {

        CropImage.activity().start(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {

        requestPermissions(cameraPermission,CAMERA_REQUEST);

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(DhabaRegister.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(DhabaRegister.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                resultUri = result.getUri();

                // ** Reuse this ** //
//                Picasso.get().load(resultUri).into(user_image);

                try {
                    getBitmapFromUri(resultUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
        
    }

    private void getCurrentLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(DhabaRegister.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(DhabaRegister.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(DhabaRegister.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();

//                                        AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);

                                    }

                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    }

    private void getBitmapFromUri(Uri resultUri) throws FileNotFoundException {

        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(resultUri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        showImage(fileDescriptor);

    }

    private void showImage(FileDescriptor fileDescriptor) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;

        //create paint object to draw square
        Bitmap myBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);

        //create canvas to draw on
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        //create face detector
        faceDetector = new
                FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                .build();
        if(!faceDetector.isOperational()){
            new AlertDialog.Builder(getApplicationContext()).setMessage("Could not set up the face detector!").show();
            Toast.makeText(DhabaRegister.this, "Could not detect Face Load another Image", Toast.LENGTH_SHORT).show();
            return;
        }

        //detect faces
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        for (int i = 0; i < faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            user_image.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
            cardView5.setVisibility(View.GONE);
        }

        user_image.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        cardView5.setVisibility(View.GONE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST:{
                boolean camera_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                boolean storage_accepted = grantResults[1] == (PackageManager.PERMISSION_GRANTED);
                if(camera_accepted && storage_accepted){
                    pickFromCamera();
                }else {
                    Toast.makeText(DhabaRegister.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case GALLERY_PICTURE:{
                if(grantResults.length>0){
                    boolean storage_accept = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if(storage_accept){
                        pickFromCamera();
                    }else {
                        Toast.makeText(DhabaRegister.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }



    }

    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(DhabaRegister.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(DhabaRegister.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {

        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }




    @Override
    protected void onStart() {
        super.onStart();
        loadnumber();
        if(ActivityCompat.checkSelfPermission(DhabaRegister.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getGoLocation();
        }else {
            ActivityCompat.requestPermissions(DhabaRegister.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }

    private void loadnumber() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Dhaba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                count = (int) (snapshot.getChildrenCount() + 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                address.setText(addressList.get(0).getLocality());
                Caddress.setText(addressList.get(0).getAddressLine(0));
                Log.d("location", String.valueOf(lat));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void verifyPhoneNumberwithCode(String VerificationId, String code) {
        progressDialog.setMessage("Verifying code");
        progressDialog.show();

        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VerificationId, code) ;
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setMessage("Logging In");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
//                        Toast.makeText(DhabaRegister.this, "Logged In as"+phone , Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(MainActivity.this,ProfileActivity.class));
//                        Toast.makeText(DhabaRegister.this, "Sucess", Toast.LENGTH_SHORT).show();
//                        StartRegister();


                        final StorageReference reference1 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "User_image");

                        UploadTask uploadTask = reference1.putFile(resultUri);

                        Task<Uri> url = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }
                                return reference1.getDownloadUrl();
                            }
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri DownloadImg = task.getResult();

                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("uid", firebaseAuth.getUid());
                                hashMap.put("name", IName.getText().toString());
                                hashMap.put("Company_Name", "");
                                assert phone != null;
                                hashMap.put("phone", phone.replace("+91",""));
                                hashMap.put("Verified", "No");
                                hashMap.put("Location", address.getText().toString());
                                hashMap.put("Cat", "Dhaba");
                                hashMap.put("MemberShip", "None");
                                hashMap.put("Insurance", "None");
                                hashMap.put("aadhar_Img_f", "");
                                hashMap.put("aadhar_b_Image", "");
                                hashMap.put("shop", "Open");
                                hashMap.put("Notification", "yes");
                                hashMap.put("Id", "DHA" + String.format("%04d", count));
                                hashMap.put("Image", DownloadImg.toString());
                                if(!addressList.isEmpty()){
                                    hashMap.put("lon", addressList.get(0).getLongitude());
                                    hashMap.put("lat", addressList.get(0).getLatitude());
                                }
                                Calendar cdate = Calendar.getInstance();

                                SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

                                final String savedate = currentdates.format(cdate.getTime());

                                Calendar ctime = Calendar.getInstance();
                                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

                                final String savetime = currenttime.format(ctime.getTime());

                                String time = savedate + ":" + savetime;

                                HashMap<String, Object> has = new HashMap<>();
                                has.put("uid", firebaseAuth.getUid());
                                has.put("name", IName.getText().toString());
                                has.put("title", "Welcome to RoadPilot");
                                has.put("time", time);
                                has.put("seen", "false");
                                has.put("id", "1");
                                has.put("key", "note_dhaba");

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                ref.child("note_dhaba").child(Objects.requireNonNull(firebaseAuth.getUid())).child("1").setValue(has);

                                HashMap<String, Object> hashMap1 = new HashMap<>();
                                hashMap1.put("user",phone.replace("+91",""));
                                hashMap1.put("logged","true");
                                hashMap1.put("pass",pass);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("Logins").child(phone.replace("+91","")).setValue(hashMap1);

                                DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Dhaba").child(Objects.requireNonNull(firebaseAuth.getUid()));
                                Driver.setValue(hashMap);
                                DatabaseReference Drivers = FirebaseDatabase.getInstance().getReference().child("CustomerCare").child(phone.replace("+91",""));
                                Drivers.setValue(hashMap);
                                DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
                                DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(DhabaRegister.this, DhabaActivity.class);
                                    intent.putExtra("cat","Dhaba");
                                    startActivity(intent);
                                });

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DhabaRegister.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void StartRegister() {


        progressDialog.setMessage("Creating Account.....");
        progressDialog.show();

        // if verification success



        final StorageReference reference1 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "User_image");

        UploadTask uploadTask = reference1.putFile(resultUri);

        Task<Uri> url = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return reference1.getDownloadUrl();
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri DownloadImg = task.getResult();

                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("uid", firebaseAuth.getCurrentUser().getUid());
                hashMap.put("name", IName.getText().toString());
                hashMap.put("Company_Name", "");
                hashMap.put("phone", phone.getText().toString());
                hashMap.put("Verified", "No");
                hashMap.put("Location", address.getText().toString());
                hashMap.put("address", Caddress.getText().toString().replace(","," "));
                hashMap.put("Cat", "Dhaba");
                hashMap.put("MemberShip", "None");
                hashMap.put("Insurance", "None");
                hashMap.put("aadhar_Img_f", "");
                hashMap.put("aadhar_b_Image", "");
                hashMap.put("shop", "Open");
                hashMap.put("Notification", "yes");
                hashMap.put("Id", "DHA" + String.format("%04d", count));
                hashMap.put("Image", DownloadImg.toString());
                    hashMap.put("lon", longitude);
                    hashMap.put("lat", latitude);


                if(verify.getText().toString().isEmpty()){
                    hashMap.put("ref","Self");
                }else {
                    hashMap.put("ref",verify.getText().toString());
                }

                Calendar cdate = Calendar.getInstance();

                SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

                final String savedate = currentdates.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

                final String savetime = currenttime.format(ctime.getTime());

                String time = savedate + ":" + savetime;

                HashMap<String, Object> has = new HashMap<>();
                has.put("uid", firebaseAuth.getCurrentUser().getUid());
                has.put("name", IName.getText().toString());
                has.put("title", "Welcome to RoadPilot");
                has.put("time", time);
                has.put("seen", "false");
                has.put("id", "1");
                has.put("key", "note_dhaba");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("note_dhaba").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid())).child("1").setValue(has);

                HashMap<String, Object> objectHashMap = new HashMap<>();
                objectHashMap.put("Location",address.getText().toString().replace(" ",""));
                ref.child("Locations").child(address.getText().toString()).updateChildren(objectHashMap);

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("log",emailAddress);
                hashMap1.put("pass",pass);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Login").child(firebaseAuth.getCurrentUser().getUid()).setValue(hashMap1);

                DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Dhaba").child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid()));
                Driver.setValue(hashMap);
                DatabaseReference Drivers = FirebaseDatabase.getInstance().getReference().child("CustomerCare").child(phone.getText().toString());
                Drivers.setValue(hashMap);
                DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
                DriverRef.setValue(hashMap).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Intent intent = new Intent(DhabaRegister.this, DhabaActivity.class);
                    intent.putExtra("cat","Dhaba");
                    startActivity(intent);
                });

            }
        });


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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}