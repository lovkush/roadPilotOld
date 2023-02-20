package com.lucky.roadpilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucky.roadpilot.Adapter.CommentsAdapter;
import com.lucky.roadpilot.Models.CommentsModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    ImageView profile,sendBtn,back,img;
    TextView user_name,post_title,post_desc;
    EditText messageBox;

    String Uname,UPic,UCat,id;

    RecyclerView recyclerView;

    CommentsAdapter adapter;
    ArrayList<CommentsModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        sendBtn = findViewById(R.id.sendBtn);
        back = findViewById(R.id.back);
        profile = findViewById(R.id.profile);
        user_name = findViewById(R.id.user_name);
        post_title = findViewById(R.id.post_title);
        post_desc = findViewById(R.id.post_desc);
        messageBox = findViewById(R.id.messageBox);
        recyclerView = findViewById(R.id.recyclerView);
        img = findViewById(R.id.img);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        Zoomy.Builder builder1 = new Zoomy.Builder((Activity) CommentsActivity.this)
                .target(img).animateZooming(false).enableImmersiveMode(false).tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        Toast.makeText(CommentsActivity.this, "ZoomIn/Out", Toast.LENGTH_SHORT).show();
                    }
                });

        builder1.register();

        Intent intent = getIntent();
        String pic = intent.getStringExtra("uid");
        String name = intent.getStringExtra("name");
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        id = intent.getStringExtra("id");

        Picasso.get().load(pic).placeholder(R.drawable.image_placeholder).into(img);
        user_name.setText(name);
        post_title.setText(title);
        post_desc.setText(desc);

        Date date = new Date();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cdate = Calendar.getInstance();

                SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

                final String savedate = currentdates.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

                final String savetime = currenttime.format(ctime.getTime());

                String time = savedate + ":" + savetime;

                Long times = System.currentTimeMillis();

                if(!TextUtils.isEmpty(messageBox.getText().toString())){

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name",Uname);
                    hashMap.put("pic",UPic);
                    hashMap.put("Category",UCat);
                    hashMap.put("time",time);
                    hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("Comment",messageBox.getText().toString());
                    hashMap.put("id",date.getTime());

                    DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();
                    reference.child("Comments").child(id).child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            messageBox.setText("");
                        }
                    });

                }else {
                    messageBox.setError("No Comment");
                }
            }
        });

        LoadComments();

    }

    private void LoadComments() {

        arrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("Comments").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    CommentsModel model = ds.getValue(CommentsModel.class);
                    arrayList.add(model);
                }

                adapter = new CommentsAdapter(CommentsActivity.this,arrayList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadComments();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Uname = ""+snapshot.child("name").getValue();
                UPic = ""+snapshot.child("pic").getValue();
                UCat = ""+snapshot.child("Category").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}