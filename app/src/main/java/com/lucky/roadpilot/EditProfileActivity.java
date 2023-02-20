package com.lucky.roadpilot;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

//    RelativeLayout owner, Downer, Dhaba, Mname;
    String value;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");
    MediaController mediaController;
    private ProgressDialog progressDialog;

    // Owner Elements


    Context context;
    Resources resources;

    // Driver Elements

    ImageView Duser_image;
    EditText IName_Dri,Dphone;
    TextView Driver_Submit,Daddress;
    private Uri resultUri;
    private static final int DRIVER_IMAGE = 12;
    String cameraPermission[];
    String storagePermission[];
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    // Dhaba Elements
    private FaceDetector faceDetector;

    // Mech Elements

    private TextView update,update1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        mediaController = new MediaController(this);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

//        Dh_user_image = findViewById(R.id.Dh_user_image);
//        IName_Da = findViewById(R.id.IName_Da);
//        dphone = findViewById(R.id.dphone);
//        M_image = findViewById(R.id.M_image);
//        IName_M = findViewById(R.id.IName_M);
//        Mphone = findViewById(R.id.Mphone);
//        mech_Submit = findViewById(R.id.mech_Submit);
//        Mname = findViewById(R.id.Mname);
//        owner = findViewById(R.id.owner);
//        Downer = findViewById(R.id.Downer);
//        Dhaba = findViewById(R.id.Dhaba);
//        user_image = findViewById(R.id.user_image);
//        CName = findViewById(R.id.CName);
//        IName = findViewById(R.id.IName);
//        phone = findViewById(R.id.phone);
//        Owner_Submit = findViewById(R.id.Owner_Submit);
        update = findViewById(R.id.update);
        update1 = findViewById(R.id.updadte1);
        Duser_image = findViewById(R.id.Duser_image);
        IName_Dri = findViewById(R.id.IName_Dri);
        Daddress = findViewById(R.id.Daddress);
        Dphone = findViewById(R.id.Dphone);
        Driver_Submit = findViewById(R.id.Driver_Submit);

        Intent intent = getIntent();
        value = intent.getStringExtra("cat");


        switch (value) {
            case "Owner":
//                owner.setVisibility(View.VISIBLE);
//                Mname.setVisibility(View.GONE);
//                Downer.setVisibility(View.GONE);
//                Dhaba.setVisibility(View.GONE);
                // ** load Data ** //

                LoadOwnerData();


            case "Drivers":
//                owner.setVisibility(View.GONE);
//                Mname.setVisibility(View.GONE);
//                Downer.setVisibility(View.VISIBLE);
//                Dhaba.setVisibility(View.GONE);

                // ** Driver Data ** //
                LoadDriverData();
                break;
            case "Dhaba":
//                owner.setVisibility(View.GONE);
//                Mname.setVisibility(View.GONE);
//                Downer.setVisibility(View.GONE);
//                Dhaba.setVisibility(View.VISIBLE);

                // ** Dhaba Data ** //
                LoadDhabaData();

                break;
            case "Mech":
//                owner.setVisibility(View.GONE);
//                Mname.setVisibility(View.VISIBLE);
//                Downer.setVisibility(View.GONE);
//                Dhaba.setVisibility(View.GONE);
                // ** Mech Data ** //
                LoadMechData();

                break;
        }

//        Owner_Submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UpdateOwnerData();
//            }
//        });
//
//        user_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent1.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent1,OWNER_IMAGE);
//
//            }
//        });

        Driver_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDriverData();
            }
        });

        Duser_image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
//                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent1.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent1,DRIVER_IMAGE);

                startDialog();
            }
        });


//        Dhada_Submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UpdateDhabaData();
//            }
//        });
//
//        Dh_user_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent1.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent1,DHABA_IMAGE);
//
//            }
//        });
//
//        mech_Submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UpdateMechData();
//            }
//        });
//
//        M_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent1.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent1,MECH_IMAGE);
//
//            }
//        });

//        switch (value) {
//            case "owner":
//                owner.setVisibility(View.VISIBLE);
//                Mname.setVisibility(View.GONE);
//                Downer.setVisibility(View.GONE);
//                Dhaba.setVisibility(View.GONE);
//
//
//
//                break;
//            case "driver":
//                owner.setVisibility(View.GONE);
//                Mname.setVisibility(View.GONE);
//                Downer.setVisibility(View.VISIBLE);
//                Dhaba.setVisibility(View.GONE);
//
//
//
//                break;
//            case "dhaba":
//                owner.setVisibility(View.GONE);
//                Mname.setVisibility(View.GONE);
//                Downer.setVisibility(View.GONE);
//                Dhaba.setVisibility(View.VISIBLE);
//
//
//                break;
//            case "mech":
//                owner.setVisibility(View.GONE);
//                Mname.setVisibility(View.VISIBLE);
//                Downer.setVisibility(View.GONE);
//                Dhaba.setVisibility(View.GONE);
//
//
//
//                break;
//        }

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
        boolean result1 = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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
        boolean result = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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
            Toast.makeText(EditProfileActivity.this, "Could not detect Face Load another Image", Toast.LENGTH_SHORT).show();
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
            Duser_image.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
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
                    Toast.makeText(EditProfileActivity.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case GALLERY_PICTURE:{
                if(grantResults.length>0){
                    boolean storage_accept = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if(storage_accept){
                        pickFromCamera();
                    }else {
                        Toast.makeText(EditProfileActivity.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }


    }

//    private void UpdateMechData() {
//
//        progressDialog.setMessage("Updating Account.....");
//        progressDialog.show();
//
//        if(MechImage !=null){
//
//            final StorageReference reference1 = storageReference.child("Mech").child(System.currentTimeMillis() + "User_image");
//
//            UploadTask uploadTask = reference1.putFile(MechImage);
//
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if(!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return reference1.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if(task.isSuccessful()){
//                        Uri DownloadMechImg = task.getResult();
//
//                        UpdatMedh(DownloadMechImg);
//
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(EditProfileActivity.this, "Something went Wrong! Please Update again", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//        }else {
//
//            HashMap<String, Object> Mech = new HashMap<>();
//
//            Mech.put("name",IName_M.getText().toString());
//            Mech.put("phone", Mphone.getText().toString());
//            Mech.put("Location", Maddress.getText().toString());
//
//
//
//            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Mech").child(mAuth.getUid());
//            Driver.updateChildren(Mech);
//            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
//            DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
//                progressDialog.dismiss();
//
//            });
//
//
//        }
//
//
//
//
//    }
//
//    private void UpdatMedh(Uri downloadMechImg) {
//
//
//        HashMap<String, Object> Mech = new HashMap<>();
//
//        Mech.put("name",IName_M.getText().toString());
//        Mech.put("phone", Mphone.getText().toString());
//        Mech.put("Location", Maddress.getText().toString());
//        Mech.put("Image", downloadMechImg.toString());
//
//
//
//        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Mech").child(mAuth.getUid());
//        Driver.updateChildren(Mech);
//        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
//        DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
//            progressDialog.dismiss();
//
//        });
//
//    }
//
//    private void UpdateDhabaData() {
//
//        progressDialog.setMessage("Updating Account.....");
//        progressDialog.show();
//
//        if(DhabaImage !=null){
//
//            final StorageReference reference1 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "User_image");
//
//            UploadTask uploadTask = reference1.putFile(DhabaImage);
//
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if(!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return reference1.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if(task.isSuccessful()){
//                        Uri DownloadMechImg = task.getResult();
//
//                        HashMap<String, Object> Mech = new HashMap<>();
//
//                        Mech.put("name",IName_Da.getText().toString());
//                        Mech.put("phone", dphone.getText().toString());
//                        Mech.put("Location", daddress.getText().toString());
//                        Mech.put("Image", DownloadMechImg.toString());
//
//
//
//                        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Dhaba").child(mAuth.getUid());
//                        Driver.updateChildren(Mech);
//                        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
//                        DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
//                            progressDialog.dismiss();
//
//                        });
//
//
//
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(EditProfileActivity.this, "Something went Wrong! Please Update again", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//        }else {
//
//            HashMap<String, Object> Mech = new HashMap<>();
//
//            Mech.put("name",IName_Da.getText().toString());
//            Mech.put("phone", dphone.getText().toString());
//            Mech.put("Location", daddress.getText().toString());
//
//
//
//            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Dhaba").child(mAuth.getUid());
//            Driver.updateChildren(Mech);
//            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
//            DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
//                progressDialog.dismiss();
//
//            });
//
//
//        }
//
//
//    }

    private void UpdateDriverData() {

        progressDialog.setMessage("Updating Account.....");
        progressDialog.show();

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();

        if(resultUri !=null){

            final StorageReference reference1 = storageReference.child(value).child(System.currentTimeMillis() + "User_image");

            UploadTask uploadTask = reference1.putFile(resultUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri DownloadMechImg = task.getResult();

                        HashMap<String, Object> Mech = new HashMap<>();

                        Mech.put("name",IName_Dri.getText().toString());
                        Mech.put("phone", Dphone.getText().toString());
                        Mech.put("Location", Daddress.getText().toString());
                        Mech.put("Image", DownloadMechImg.toString());

                        HashMap<String, Object> hashMap1 = new HashMap<>();
                        hashMap1.put("Id",String.valueOf(times));
                        hashMap1.put("time",time);
                        hashMap1.put("title","Updated Profile");

                        DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                        references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                                .setValue(hashMap1);



                        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child(value).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                        Driver.updateChildren(Mech);
                        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                        DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
                            progressDialog.dismiss();
                            startActivity(new Intent(EditProfileActivity.this,MainActivity.class));

                        });



                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Something went Wrong! Please Update again", Toast.LENGTH_SHORT).show();
                }
            });


        }
        else {

            HashMap<String, Object> Mech = new HashMap<>();

            Mech.put("name",IName_Dri.getText().toString());
            Mech.put("phone", Dphone.getText().toString());
            Mech.put("address", Daddress.getText().toString());

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("Id",String.valueOf(times));
            hashMap1.put("time",time);
            hashMap1.put("title","Updated Profile");

            DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
            references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(String.valueOf(times))
                    .setValue(hashMap1);



            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child(value).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            Driver.updateChildren(Mech);
            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
                progressDialog.dismiss();
                startActivity(new Intent(EditProfileActivity.this,MainActivity.class));

            });


        }



    }

//    private void UpdateOwnerData() {
//
//        progressDialog.setMessage("Updating Account.....");
//        progressDialog.show();
//
//        if(OwnerImage !=null){
//
//            final StorageReference reference1 = storageReference.child("Dhaba").child(System.currentTimeMillis() + "User_image");
//
//            UploadTask uploadTask = reference1.putFile(OwnerImage);
//
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if(!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return reference1.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if(task.isSuccessful()){
//                        Uri DownloadMechImg = task.getResult();
//
//                        HashMap<String, Object> Mech = new HashMap<>();
//
//                        Mech.put("name",IName.getText().toString());
//                        Mech.put("phone", phone.getText().toString());
//                        Mech.put("Location", address.getText().toString());
//                        Mech.put("Image", DownloadMechImg.toString());
//
//
//
//                        DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Owner").child(mAuth.getUid());
//                        Driver.updateChildren(Mech);
//                        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
//                        DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
//                            progressDialog.dismiss();
//
//                        });
//
//
//
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(EditProfileActivity.this, "Something went Wrong! Please Update again", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//        }else {
//
//            HashMap<String, Object> Mech = new HashMap<>();
//
//            Mech.put("name",IName.getText().toString());
//            Mech.put("phone", phone.getText().toString());
//            Mech.put("Location", address.getText().toString());
//
//
//
//            DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Owner").child(mAuth.getUid());
//            Driver.updateChildren(Mech);
//            DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
//            DriverRef.updateChildren(Mech).addOnSuccessListener(unused -> {
//                progressDialog.dismiss();
//
//            });
//
//
//        }
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == RESULT_OK){
//
//            switch (requestCode){
//                case OWNER_IMAGE:
//                    OwnerImage = data.getData();
//                    if(OwnerImage.toString().contains("image")){
//                        Picasso.get().load(OwnerImage).into(user_image);
//                    }else {
//                        Toast.makeText(EditProfileActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case DRIVER_IMAGE:
//                    DriverImage = data.getData();
//                    if(DriverImage.toString().contains("image")){
//                        Picasso.get().load(DriverImage).into(Duser_image);
//                    }else {
//                        Toast.makeText(EditProfileActivity.this, "No file is Selected", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case DHABA_IMAGE:
//                    DhabaImage = data.getData();
//                    if(DhabaImage.toString().contains("image")){
//                        Picasso.get().load(DhabaImage).into(Dh_user_image);
//                    }else {
//                        Toast.makeText(EditProfileActivity.this, "No file is Selected", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case MECH_IMAGE:
//                    MechImage = data.getData();
//                    if(MechImage.toString().contains("image")){
//                        Picasso.get().load(MechImage).into(M_image);
//                    }else {
//                        Toast.makeText(EditProfileActivity.this, "No file is Selected", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//            }
//
//        }
//
//
//    }


    private void LoadMechData() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String addresses = ""+snapshot.child("Location").getValue();
                String name = ""+snapshot.child("name").getValue();
                String image = ""+snapshot.child("Image").getValue();
                String phoneNo = ""+snapshot.child("phone").getValue();


                Daddress.setText(addresses);
                IName_Dri.setText(name);
                Dphone.setText(phoneNo);

                try{
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(Duser_image);

                }catch (Exception e){
                    Duser_image.setImageResource(R.drawable.profile);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "UnAble to load data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadDhabaData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String addresses = ""+snapshot.child("Location").getValue();
                String name = ""+snapshot.child("name").getValue();
                String image = ""+snapshot.child("Image").getValue();
                String phoneNo = ""+snapshot.child("phone").getValue();


                Daddress.setText(addresses);
                IName_Dri.setText(name);
                Dphone.setText(phoneNo);

                try{
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(Duser_image);

                }catch (Exception e){
                    Duser_image.setImageResource(R.drawable.profile);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "UnAble to load data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadDriverData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String addresses = ""+snapshot.child("Location").getValue();
                String name = ""+snapshot.child("name").getValue();
                String image = ""+snapshot.child("Image").getValue();
                String phoneNo = ""+snapshot.child("phone").getValue();


                Daddress.setText(addresses);
                IName_Dri.setText(name);
                Dphone.setText(phoneNo);

                try{
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(Duser_image);

                }catch (Exception e){
                    Duser_image.setImageResource(R.drawable.profile);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "UnAble to load data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadOwnerData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String addresses = ""+snapshot.child("Location").getValue();
                String name = ""+snapshot.child("name").getValue();
                String image = ""+snapshot.child("Image").getValue();
                String CNam = ""+snapshot.child("Company_Name").getValue();
                String phoneNo = ""+snapshot.child("phone").getValue();


                Daddress.setText(addresses);
                IName_Dri.setText(name);
//                CName.setText(CNam);
                Dphone.setText(phoneNo);

                try{
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(Duser_image);

                }catch (Exception e){
                    Duser_image.setImageResource(R.drawable.profile);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "UnAble to load data", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(EditProfileActivity.this,"en");
            resources =context.getResources();

            // Text In English


            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));


        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"hi");
            resources =context.getResources();

            // Text in Hindi

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));

        }
        else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"kn");
            resources =context.getResources();

            // Text in Kannada

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));


        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"te");
            resources =context.getResources();

            // Text in Telugu


            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));

        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"bn");
            resources =context.getResources();

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));

         


        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"gu");
            resources =context.getResources();

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));

         

        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"ml");
            resources =context.getResources();

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));

       
        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"mr");
            resources =context.getResources();

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));

           

        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"pa");
            resources =context.getResources();

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));


        }else if(LocaleHelper.getLanguage(EditProfileActivity.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(EditProfileActivity.this,"ta");
            resources =context.getResources();

            // Text in Tamil

            update.setText(resources.getString(R.string.update_profile));
            update1.setText(resources.getString(R.string.update_your_info));
            Driver_Submit.setText(resources.getString(R.string.submit));
            setTitle(resources.getString(R.string.app_name));

        }
    }
}