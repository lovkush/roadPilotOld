package com.lucky.roadpilot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.roadpilot.Models.CommentsModel;
import com.lucky.roadpilot.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.Holder>{

    Context context;
    ArrayList<CommentsModel> arrayList;

    public CommentsAdapter(Context context, ArrayList<CommentsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_row,parent,false);
        return  new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        CommentsModel model = arrayList.get(position);


        holder.user_name.setText(model.getName());
        holder.time.setText(model.getTime());
        holder.post_desc.setText(model.getComment());

        Picasso.get().load(model.getPic()).into(holder.profile);


//        holder.profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, InstDetatilsActivity.class);
//                intent.putExtra("value","view");
//                intent.putExtra("uid",model.getUid());
//                context.startActivity(intent);
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView user_name,post_desc,time;


        public Holder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            profile = itemView.findViewById(R.id.profile);
            user_name = itemView.findViewById(R.id.user_name);
            post_desc = itemView.findViewById(R.id.post_desc);
        }
    }
}