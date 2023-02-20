package com.lucky.roadpilot;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {

    private ImageView img_logo;
    private EditText post_title,post_description;
    private TextView Submit;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    String selectedImagePath, Location, HayWay;
    String cameraPermission[];
    String storagePermission[];
    Uri resultUri;
    String value,note;
    Context context;
    Resources resources;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");
    MediaController mediaController;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Intent intent = getIntent();
        value = intent.getStringExtra("add");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        mediaController = new MediaController(this);

        Submit = findViewById(R.id.Submit);
        img_logo = findViewById(R.id.post_img);
        post_title = findViewById(R.id.post_title);
        post_description = findViewById(R.id.post_description);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(post_description.getText().toString()) &&
                        !TextUtils.isEmpty(post_title.getText().toString()) &&
                        img_logo.getDrawable() != null){

                    PostToServer();

                }else {
                    Toast.makeText(AddPostActivity.this, "PLease Fill the All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_logo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                startDialog();
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
        boolean result1 = ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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
        boolean result = ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                resultUri = result.getUri();

                Picasso.get().load(resultUri).into(img_logo);

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
                    Toast.makeText(AddPostActivity.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case GALLERY_PICTURE:{
                if(grantResults.length>0){
                    boolean storage_accept = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if(storage_accept){
                        pickFromCamera();
                    }else {
                        Toast.makeText(AddPostActivity.this, "No File is Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }


    }

    public void PostToServer(){

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;


        final StorageReference reference = storageReference.child("Posts").child(System.currentTimeMillis() + "post_images");

       UploadTask uploadTask = reference.putFile(resultUri);

        Task<Uri> url = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Uri DownloadImg = task.getResult();

                Long times = System.currentTimeMillis();

                switch (value) {
                    case "news":
                        HashMap<String, Object> news = new HashMap<>();
                        news.put("Cat", "News");
                        news.put("post_img", DownloadImg.toString());
                        news.put("uid", firebaseAuth.getCurrentUser().getUid());
                        news.put("time", time);
                        news.put("approved", "No");
                        news.put("Location", Location);
                        news.put("title", post_title.getText().toString());
                        news.put("Description", post_description.getText().toString());
                        news.put("id", String.valueOf(times));


                        HashMap<String, String> has = new HashMap<>();
                        has.put("Location", Location);
                        has.put("uid",firebaseAuth.getCurrentUser().getUid());
                        has.put("time",time);
                        has.put("title", post_title.getText().toString() + " submitted for approval");
                        has.put("seen", "false");
                        has.put("id", String.valueOf(times));
                        has.put("key", note);




                        DatabaseReference notes = FirebaseDatabase.getInstance().getReference();
                        notes.child(note).child(firebaseAuth.getCurrentUser().getUid()).child(String.valueOf(times)).setValue(has);

                        DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(String.valueOf(times));
                        DriverRef.setValue(news).addOnSuccessListener(unused -> {


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Id",String.valueOf(times));
                            hashMap.put("time",time);
                            hashMap.put("title","Posted a Post ");

                            DatabaseReference references = FirebaseDatabase.getInstance().getReference();
                            references.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                                    .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Picasso.get().load(R.drawable.add_img).into(img_logo);
                                    post_title.setText("");
                                    post_description.setText("");
                                    onBackPressed();
                                }
                            });

                            Toast.makeText(AddPostActivity.this, "Your Post have been submitted for approval", Toast.LENGTH_SHORT).show();

                        });
                        break;
                    case "dhaba":
                        HashMap<String, Object> post = new HashMap<>();
                        post.put("Cat", "Post");
                        post.put("post_img", DownloadImg.toString());
                        post.put("uid", firebaseAuth.getCurrentUser().getUid());
                        post.put("time", String.valueOf(time));
                        post.put("Location", HayWay);
                        post.put("approved", "No");
                        post.put("title", post_title.getText().toString());
                        post.put("Description", post_description.getText().toString());
                        post.put("id", String.valueOf(times));


                        HashMap<String, String> has1 = new HashMap<>();
                        has1.put("Location", Location);
                        has1.put("uid",firebaseAuth.getCurrentUser().getUid());
                        has1.put("time",time);
                        has1.put("title", post_title.getText().toString() + " submitted for approval");
                        has1.put("seen", "false");
                        has1.put("id", String.valueOf(times));
                        has1.put("key", "note_dhaba");

                        DatabaseReference note1 = FirebaseDatabase.getInstance().getReference();
                        note1.child("note_dhaba").child(firebaseAuth.getCurrentUser().getUid()).child(String.valueOf(times)).setValue(has1);


                        DatabaseReference PostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(String.valueOf(times));
                        PostRef.setValue(post).addOnSuccessListener(unused -> {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Id",String.valueOf(times));
                            hashMap.put("time",time);
                            hashMap.put("title","Posted a Post");

                            DatabaseReference references = FirebaseDatabase.getInstance().getReference();
                            references.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                                    .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Picasso.get().load(R.drawable.add_img).into(img_logo);
                                    post_title.setText("");
                                    post_description.setText("");
                                    onBackPressed();
                                }
                            });
                            Toast.makeText(AddPostActivity.this, "Your Post have been submitted for approval", Toast.LENGTH_SHORT).show();

                        });
                        break;

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Location = ""+snapshot.child("Location").getValue();

                if((snapshot.child("Cat").getValue()).equals("Dhaba")){
                    HayWay = ""+snapshot.child("Highway").getValue();
                    note = "note_dhaba";
                }else if((snapshot.child("Cat").getValue()).equals("Owner")){
//                    HayWay = ""+snapshot.child("Highway").getValue();
                    note = "note_owner";
                }else if((snapshot.child("Cat").getValue()).equals("Drivers")){
//                    HayWay = ""+snapshot.child("Highway").getValue();
                    note = "note_driver";
                }else if((snapshot.child("Cat").getValue()).equals("Mech")){
//                    HayWay = ""+snapshot.child("Highway").getValue();
                    note = "note_mech";
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LanguageChoose();
    }

        private void LanguageChoose(){
        if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(AddPostActivity.this,"en");
            resources =context.getResources();

            // Text In English

            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));



        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"hi");
            resources =context.getResources();
            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));

        }
        else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"kn");
            resources =context.getResources();

            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));

        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"te");
            resources =context.getResources();
            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));
            // Text in Telugu

        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"bn");
            resources =context.getResources();
            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));
        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"gu");
            resources =context.getResources();
            post_title.setText(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));



        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"ml");
            resources =context.getResources();


            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));
//
        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"mr");
            resources =context.getResources();


            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));

        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"pa");
            resources =context.getResources();


            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));


        }else if(LocaleHelper.getLanguage(AddPostActivity.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(AddPostActivity.this,"ta");
            resources =context.getResources();

            // Text in Tamil

            post_title.setHint(resources.getString(R.string.title));
            post_description.setHint(resources.getString(R.string.description));
            Submit.setHint(resources.getString(R.string.submit));

        }


    }
    

}