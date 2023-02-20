package com.lucky.roadpilot.Registers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.lucky.roadpilot.R;
import com.lucky.roadpilot.perference;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class DrivingLBActivity extends AppCompatActivity {


    String value;
    ImageView fr_img;
    TextView submit;
    String cameraPermission[];
    String storagePermission[];
    Uri resultUri;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");
    MediaController mediaController;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_lbactivity);
        Intent intent = getIntent();
        value = intent.getStringExtra("cat");

        fr_img = findViewById(R.id.fr_img);
        submit = findViewById(R.id.submit);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        mediaController = new MediaController(this);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        fr_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View view) {
                startDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri != null){
                    saveID();
                }
            }
        });


    }

    private void saveID() {

        progressDialog.setMessage("Uploading.....");
        progressDialog.show();

        final StorageReference reference1 = storageReference.child(value).child(System.currentTimeMillis() + "drivingb");

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

                hashMap.put("DrivingB_Image", DownloadImg.toString());

                Calendar cdate = Calendar.getInstance();

                SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

                final String savedate = currentdates.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

                final String savetime = currenttime.format(ctime.getTime());

                String time = savedate + ":" + savetime;

                Long times = System.currentTimeMillis();

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("Id",String.valueOf(times));
                hashMap1.put("time",time);
                hashMap1.put("title","Updated Driving Licence Back");

                DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                        .setValue(hashMap1);

                DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child(value).child(Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid()));
                Driver.updateChildren(hashMap);
                DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
                DriverRef.updateChildren(hashMap).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();

                        Intent intent = new Intent(DrivingLBActivity.this, perference.class);
                        intent.putExtra("cat", value);
                        startActivity(intent);

                });

            }
        });


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
        boolean result1 = ContextCompat.checkSelfPermission(DrivingLBActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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
        boolean result = ContextCompat.checkSelfPermission(DrivingLBActivity.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(DrivingLBActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                resultUri = result.getUri();

                Picasso.get().load(resultUri).into(fr_img);

            }
        }
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
                    Toast.makeText(DrivingLBActivity.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case GALLERY_PICTURE:{
                if(grantResults.length>0){
                    boolean storage_accept = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if(storage_accept){
                        pickFromCamera();
                    }else {
                        Toast.makeText(DrivingLBActivity.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }


    }

}