package com.lucky.roadpilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.lucky.roadpilot.Adapter.NotifyAdapter;
import com.lucky.roadpilot.Models.NoteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView note_rec;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String note;
    NotifyAdapter adapter;
    ArrayList<NoteModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        note_rec = findViewById(R.id.note_rec);

        Intent intent = getIntent();
        note = intent.getStringExtra("note");

    }

    @Override
    protected void onStart() {
        super.onStart();

        arrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child(note).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    NoteModel model = ds.getValue(NoteModel.class);

                    arrayList.add(model);



                }
                adapter = new NotifyAdapter(NotificationActivity.this,arrayList);
                note_rec.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        HashMap<String ,Object> has = new HashMap<>();
//        has.put("seen", "true");
////        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
//        ref.child(note).child(mAuth.getCurrentUser().getUid()).updateChildren(has);
    }
}