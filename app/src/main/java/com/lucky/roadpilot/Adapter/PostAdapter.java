package com.lucky.roadpilot.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucky.roadpilot.CommentsActivity;
import com.lucky.roadpilot.Filters.PostFilter;
import com.lucky.roadpilot.Models.PostModel;
import com.lucky.roadpilot.ProfileViewActivity;
import com.lucky.roadpilot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> implements Filterable {

    Context context;
    public ArrayList<PostModel> arrayList, filterList;
    private PostFilter jobFilter;

    public PostAdapter(Context context, ArrayList<PostModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filterList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        PostModel model = arrayList.get(position);


        String uid = model.getUid();
        String time = model.getTime();
        String image = model.getPost_img();
        String title = model.getTitle();
        String Desc = model.getDescription();

        holder.post_desc.setText(Desc);
        holder.post_time.setText(time);
        holder.post_title.setText(title);

        try{
            Picasso.get().load(image).placeholder(R.drawable.post_add).into(holder.post_img);

        }catch (Exception e){
            holder.post_img.setImageResource(R.drawable.post_add);
        }


        Zoomy.Builder builder1 = new Zoomy.Builder((Activity) context)
                .target(holder.post_img).animateZooming(false).enableImmersiveMode(false).tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        Toast.makeText(context, "ZoomIn/Out", Toast.LENGTH_SHORT).show();
                    }
                });

        builder1.register();

        isLiked(model.getId(), holder.like);
        nrLikes(holder.like_count, model.getId());
        nrCom(holder.com_count, model.getId());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Posts").child(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if((""+snapshot.child("cat").getValue()).equals("News")){
                    holder.profile_name.setClickable(false);

                }else {
                    holder.profile_name.setClickable(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               holder.name = ""+snapshot.child("name").getValue();
               holder.imge = ""+snapshot.child("Image").getValue();
                holder.comp = ""+snapshot.child("Company_Name").getValue();
                holder.lat = ""+snapshot.child("lat").getValue();
                holder.lon = ""+snapshot.child("lon").getValue();
                holder.phone = ""+snapshot.child("phone").getValue();
                holder.loc = ""+snapshot.child("Location").getValue();
                holder.profile_name.setText(holder.name);

                try{
                    Picasso.get().load(holder.imge).placeholder(R.drawable.profile).into(holder.profile);

                }catch (Exception e){
                    holder.profile.setImageResource(R.drawable.profile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(model.getId())).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                    holder.like.setImageResource(R.drawable.like);
                    holder.like.setTag("liked");
//                    notification(model.getId());

//                        notify = true;
//                        final String msg = "Like on Your Post";
//
//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        reference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                String name = ""+snapshot.child("name").getValue();
//                                if(notify) {
//                                    sendNotification(uid, name, msg);
//                                }
//                                notify = false;
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(String.valueOf(model.getId())).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    holder.like.setImageResource(R.drawable.dislike);
                    holder.like.setTag("like");
                }
            }
        });



        holder.com_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("uid", model.getPost_img());
                intent.putExtra("title", title);
                intent.putExtra("desc", Desc);
                intent.putExtra("id", model.getId());
                intent.putExtra("name", holder.name);
                context.startActivity(intent);
            }
        });


        holder.profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProfileViewActivity.class);
                intent.putExtra("Id", uid);
                intent.putExtra("phone", holder.phone);
                intent.putExtra("name", holder.name);
                intent.putExtra("img", holder.imge);
                intent.putExtra("lat", holder.lat);
                intent.putExtra("lon", holder.lon);
                intent.putExtra("comp", holder.comp);
                intent.putExtra("location", holder.loc);
                intent.putExtra("images", "");
                intent.putExtra("cc", "");
                context.startActivity(intent);



            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(jobFilter == null){
            jobFilter =  new PostFilter(this, filterList);
        }

        return jobFilter;
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView post_img,profile,like,com;
        TextView post_time,profile_name,post_title,post_desc;
        String name,imge,lat,lon,comp,loc,phone;
        TextView like_count;
        TextView com_count;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            post_desc = itemView.findViewById(R.id.post_desc);
            post_img = itemView.findViewById(R.id.post_img);
            profile = itemView.findViewById(R.id.profile);
            post_time = itemView.findViewById(R.id.post_time);
            profile_name = itemView.findViewById(R.id.profile_name);
            post_title = itemView.findViewById(R.id.post_title);
            like = itemView.findViewById(R.id.like);
            com = itemView.findViewById(R.id.com);
            like_count = itemView.findViewById(R.id.like_count);
            com_count = itemView.findViewById(R.id.com_count);

        }
    }

    private void isLiked(String postId, final ImageView imageView) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Likes").child(String.valueOf(postId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assert user != null;
                if (snapshot.hasChild(user.getUid())) {
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("liked");
                } else if (!snapshot.hasChild(user.getUid())) {
                    imageView.setImageResource(R.drawable.dislike);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void nrLikes(TextView likes, String postId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Likes").child(String.valueOf(postId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount() + " Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nrCom(TextView com_count_text, String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Comments").child(String.valueOf(id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com_count_text.setText(snapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
