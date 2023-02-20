package com.lucky.roadpilot;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.lucky.roadpilot.Adapter.PostAdapter;
import com.lucky.roadpilot.Models.PostModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements IOnBackPressed {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseAuth mAuth;
    RecyclerView news_rec;
    EditText search;
    PostAdapter jobAdapter;
    ArrayList<PostModel> models;
    LottieAnimationView loading;

    Context context;
    Resources resources;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);

        FloatingActionButton fab = view.findViewById(R.id.addDhada);
        news_rec = view.findViewById(R.id.news_rec);
        search = view.findViewById(R.id.search);
        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddPostActivity.class);
                intent.putExtra("add","news");
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                jobAdapter.getFilter().filter(charSequence);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();

                for(DataSnapshot ds:snapshot.getChildren()) {

                    if ((""+ds.child("approved").getValue()).equals("Yes")&& (""+ds.child("Cat").getValue()).equals("News")) {
                        PostModel postModel = ds.getValue(PostModel.class);
                        models.add(postModel);
                        loading.setVisibility(View.GONE);

                    }
                }

                jobAdapter = new PostAdapter(getActivity(),models);
                news_rec.setAdapter(jobAdapter);

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

//        LanguageChoose();

        models = new ArrayList<>();



    }


    @Override
    public boolean onBackPressed() {

        return true;

    }

    private void LanguageChoose(){
        if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(getActivity(),"en");
            resources =context.getResources();

            // Text In English

            search.setText(resources.getString(R.string.search));



        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(getActivity(),"hi");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));

        }
        else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(getActivity(),"kn");
            resources =context.getResources();

            search.setText(resources.getString(R.string.search));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(getActivity(),"te");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));

            // Text in Telugu

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(getActivity(),"bn");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(getActivity(),"gu");
            resources =context.getResources();
            search.setText(resources.getString(R.string.search));



        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(getActivity(),"ml");
            resources =context.getResources();


            search.setText(resources.getString(R.string.search));
//
        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(getActivity(),"mr");
            resources =context.getResources();


            search.setText(resources.getString(R.string.search));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(getActivity(),"pa");
            resources =context.getResources();


            search.setText(resources.getString(R.string.search));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(getActivity(),"ta");
            resources =context.getResources();

            // Text in Tamil

            search.setText(resources.getString(R.string.search));

        }


    }





}