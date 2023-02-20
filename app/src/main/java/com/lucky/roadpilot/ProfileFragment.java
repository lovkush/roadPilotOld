package com.lucky.roadpilot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    private CardView cardView1,performance,cardView3,help,setting,need;
    private ImageView use_img;
    private TextView user_name,user_location,user_id,help1,helps;
    private String verified, cat, uid;
    private FirebaseAuth mAuth;
    private CardView count_lay;
    private TextView count;
    private ImageView note;
    long cou;
    String id,url;

    Context context;
    Resources resources;
    AlertDialog dialog;
    ImageView tube;

    private TextView find,search,set,lan,member,account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        setting = view.findViewById(R.id.setting);
        help = view.findViewById(R.id.help);
        helps = view.findViewById(R.id.helps);
        help1 = view.findViewById(R.id.help1);
        tube = view.findViewById(R.id.tube);
        user_id = view.findViewById(R.id.user_id);
        cardView1 = view.findViewById(R.id.cardView1);
        count_lay = view.findViewById(R.id.count_lay);
        count = view.findViewById(R.id.count);
        note = view.findViewById(R.id.note);
        performance = view.findViewById(R.id.performance);
        cardView3 = view.findViewById(R.id.cardView3);
        need = view.findViewById(R.id.need);
        use_img = view.findViewById(R.id.use_img);
        user_name = view.findViewById(R.id.user_name);
        user_location = view.findViewById(R.id.user_location);

        find = view.findViewById(R.id.find);
        search = view.findViewById(R.id.search);
        set = view.findViewById(R.id.set);
        lan = view.findViewById(R.id.lan);
        member = view.findViewById(R.id.member);
        account = view.findViewById(R.id.account);

        mAuth = FirebaseAuth.getInstance();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Dynamic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                url = ""+snapshot.child("url").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long times = System.currentTimeMillis();

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("Id",String.valueOf(times));
                hashMap1.put("time",time);
                hashMap1.put("title","Opened YouTube for Help");

                DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                        .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                    }
                });



            }
        });

        tube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long times = System.currentTimeMillis();

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("Id",String.valueOf(times));
                hashMap1.put("time",time);
                hashMap1.put("title","Opened YouTube for Help");

                DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                        .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                    }
                });


            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NotificationActivity.class);
                switch (cat) {
                    case "Owner":
                        // owner Profile
//                    Intent intent = new Intent(getActivity(), perference.class);
//                    intent.putExtra("cat", "owner");
//                    startActivity(intent);
//                        ShowBox();
                        intent.putExtra("note","note_owner");
                        startActivity(intent);
                        break;
                    case "Drivers":
                        // Driver Profile
//                    Intent intent1 = new Intent(getActivity(), perference.class);
//                    intent1.putExtra("cat", "driver");
//                    startActivity(intent1);
//                        ShowBox();
                        intent.putExtra("note","note_driver");
                        startActivity(intent);
                        break;
                    case "Mech":
                        // Mech Profile
//                    Intent intent2 = new Intent(getActivity(), perference.class);
//                    intent2.putExtra("cat", "mech");
//                    startActivity(intent2);
//                        Toast.makeText(getActivity(), "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();
                        intent.putExtra("note","note_mech");
                        startActivity(intent);
                        break;
                    case "Dhaba":
                        // Dhaba Profile
//                    Intent intent3 = new Intent(getActivity(), perference.class);
//                    intent3.putExtra("cat", "dhaba");
//                    startActivity(intent3);
//                        Toast.makeText(getActivity(), "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();
                        intent.putExtra("note","note_dhaba");
                        startActivity(intent);
                        break;
                }

            }
        });


        need.setOnClickListener(v -> {
//           startActivity(new Intent(getActivity(), NeedActivity.class))

            switch (cat) {
                case "Owner":
                    // owner Profile
//                    Intent intent = new Intent(getActivity(), perference.class);
//                    intent.putExtra("cat", "owner");
//                    startActivity(intent);
                    ShowBox();
                    break;
                case "Drivers":
                    // Driver Profile
//                    Intent intent1 = new Intent(getActivity(), perference.class);
//                    intent1.putExtra("cat", "driver");
//                    startActivity(intent1);
                    ShowBox();
                    break;
                case "Mech":
                    // Mech Profile
//                    Intent intent2 = new Intent(getActivity(), perference.class);
//                    intent2.putExtra("cat", "mech");
//                    startActivity(intent2);
                    Toast.makeText(getActivity(), "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();
                    break;
                case "Dhaba":
                    // Dhaba Profile
//                    Intent intent3 = new Intent(getActivity(), perference.class);
//                    intent3.putExtra("cat", "dhaba");
//                    startActivity(intent3);
                    Toast.makeText(getActivity(), "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();

                    break;
            }

        });

        setting.setOnClickListener(v -> startActivity(new Intent(getActivity(),SettingsActivity.class)));

        performance.setOnClickListener(v -> {

            switch (cat) {
                case "Owner":
                    // owner Profile
                    Intent intent = new Intent(getActivity(), perference.class);
                    intent.putExtra("cat", "Owner");
                    startActivity(intent);
                    break;
                case "Drivers":
                    // Driver Profile
                    Intent intent1 = new Intent(getActivity(), perference.class);
                    intent1.putExtra("cat", "Drivers");
                    startActivity(intent1);
                    break;
                case "Mech":
                    // Mech Profile
                    Intent intent2 = new Intent(getActivity(), perference.class);
                    intent2.putExtra("cat", "Mech");
                    startActivity(intent2);
                    break;
                case "Dhaba":
                    // Dhaba Profile
                    Intent intent3 = new Intent(getActivity(), perference.class);
                    intent3.putExtra("cat", "Dhaba");
                    startActivity(intent3);
                    break;
            }


                });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (cat) {
                    case "Owner":
                        // owner Profile
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);

                        intent.putExtra("cat", "Owner");
                        startActivity(intent);
                        break;
                    case "Drivers":
                        // Driver Profile
                        Intent intent1 = new Intent(getActivity(), EditProfileActivity.class);
                        intent1.putExtra("cat", "Drivers");
                        startActivity(intent1);
                        break;
                    case "Mech":
                        // Mech Profile
                        Intent intent2 = new Intent(getActivity(), EditProfileActivity.class);
                        intent2.putExtra("cat", "Mech");
                        startActivity(intent2);
                        break;
                    case "Dhaba":
                        // Dhaba Profile
                        Intent intent3 = new Intent(getActivity(), EditProfileActivity.class);
                        intent3.putExtra("cat", "Dhaba");
                        startActivity(intent3);
                        break;
                }
            }
        });


        return view;
    }

    private void ShowBox() {

        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);

        }else {
            alert = new AlertDialog.Builder(getActivity());
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.apply_lay,null);

        TextView owner = view.findViewById(R.id.owner);
        TextView Driver = view.findViewById(R.id.Driver);
        TextView Dhaba = view.findViewById(R.id.Dhaba);
        TextView mech = view.findViewById(R.id.mech);
        TextView t1 = view.findViewById(R.id.t1);
        LinearLayout vie = view.findViewById(R.id.vie);

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;





        owner.setVisibility(View.GONE);
        if ("Drivers".equals(cat)) {
            vie.setVisibility(View.GONE);
        }


        if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(getActivity(),"en");
            resources =context.getResources();

            // Text In English

//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
//            account.setText(resources.getString(R.string.my_preferences));
            Driver.setText(resources.getString(R.string.drivers));
            t1.setText(resources.getString(R.string.choose));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
//            member.setText(resources.getString(R.string.membership_plan_others));



        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(getActivity(),"hi");
            resources =context.getResources();

            // Text in Hindi

//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
//            account.setText(resources.getString(R.string.my_preferences));
            t1.setText(resources.getString(R.string.choose));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
//            member.setText(resources.getString(R.string.membership_plan_others));


        }
        else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(getActivity(),"kn");
            resources =context.getResources();

            t1.setText(resources.getString(R.string.choose));
            // Text in Kannada

//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
//            account.setText(resources.getString(R.string.my_preferences));
//            member.setText(resources.getString(R.string.membership_plan_others));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(getActivity(),"te");
            resources =context.getResources();

            // Text in Telugu

//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
//            account.setText(resources.getString(R.string.my_preferences));
            t1.setText(resources.getString(R.string.choose));
//            member.setText(resources.getString(R.string.membership_plan_others));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(getActivity(),"bn");
            resources =context.getResources();


//
//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
            t1.setText(resources.getString(R.string.choose));
//            account.setText(resources.getString(R.string.my_preferences));
//            member.setText(resources.getString(R.string.membership_plan_others));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(getActivity(),"gu");
            resources =context.getResources();


//
//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
//            account.setText(resources.getString(R.string.my_preferences));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
//            member.setText(resources.getString(R.string.membership_plan_others));
            t1.setText(resources.getString(R.string.choose));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(getActivity(),"ml");
            resources =context.getResources();


//
//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
            t1.setText(resources.getString(R.string.choose));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
//            account.setText(resources.getString(R.string.my_preferences));
//            member.setText(resources.getString(R.string.membership_plan_others));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(getActivity(),"mr");
            resources =context.getResources();



//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            t1.setText(resources.getString(R.string.choose));
//            set.setText(resources.getString(R.string.settings));
//            account.setText(resources.getString(R.string.my_preferences));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(getActivity(),"pa");
            resources =context.getResources();



//            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
//            search.setText(resources.getString(R.string.search));
//            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
//            set.setText(resources.getString(R.string.settings));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));
            t1.setText(resources.getString(R.string.choose));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(getActivity(),"ta");
            resources =context.getResources();

            // Text in Tamil

            t1.setText(resources.getString(R.string.choose));
            Driver.setText(resources.getString(R.string.drivers));
            Dhaba.setText(resources.getString(R.string.dhabas));
            mech.setText(resources.getString(R.string.mechanics));


        }


        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (cat){
                    case "Owner":
                        Toast.makeText(getActivity(), "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case "Drivers":
                        Intent intent = new Intent(getActivity(),NeedActivity.class);
                        intent.putExtra("cat","owner");
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                }


            }
        });

        Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NeedActivity.class);
                intent.putExtra("cat","Driver");

                Long times = System.currentTimeMillis();

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("Id",String.valueOf(times));
                hashMap1.put("time",time);
                hashMap1.put("title","Searched For Drivers");

                DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                        .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        startActivity(intent);
                        dialog.dismiss();
                    }
                });




            }
        });
        Dhaba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),NeedActivity.class);
                intent.putExtra("cat","Dhaba");

                Long times = System.currentTimeMillis();

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("Id",String.valueOf(times));
                hashMap1.put("time",time);
                hashMap1.put("title","Searched For Dhaba");

                DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                        .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        startActivity(intent);
                        dialog.dismiss();
                    }
                });


            }
        });
        mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),NeedActivity.class);
                intent.putExtra("cat","Mech");

                Long times = System.currentTimeMillis();

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("Id",String.valueOf(times));
                hashMap1.put("time",time);
                hashMap1.put("title","Searched For Mechanics");

                DatabaseReference references1 = FirebaseDatabase.getInstance().getReference();
                references1.child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
                        .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        startActivity(intent);
                        dialog.dismiss();
                    }
                });


            }
        });



        alert.setView(view);

        alert.setCancelable(true);

        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Users").child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cat = ""+snapshot.child("Cat").getValue();
                verified = ""+snapshot.child("Verified").getValue();
                uid = ""+snapshot.child("uid").getValue();
                String address = ""+snapshot.child("Location").getValue();
                String name = ""+snapshot.child("name").getValue();
                String image = ""+snapshot.child("Image").getValue();
                id = ""+snapshot.child("Id").getValue();

                user_id.setText(id);



                user_location.setText(address);
                user_name.setText(name);

                try{
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(use_img);

                }catch (Exception e){
                    use_img.setImageResource(R.drawable.profile);
                }

                if(verified.equals("yes")){
                    cardView1.setVisibility(View.VISIBLE);
                }else {
                    cardView1.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(getActivity(),"en");
            resources =context.getResources();

            // Text In English

            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
            account.setText(resources.getString(R.string.my_preferences));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));



        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(getActivity(),"hi");
            resources =context.getResources();

            // Text in Hindi

            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
            account.setText(resources.getString(R.string.my_preferences));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }
        else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(getActivity(),"kn");
            resources =context.getResources();

            // Text in Kannada

            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            account.setText(resources.getString(R.string.my_preferences));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(getActivity(),"te");
            resources =context.getResources();

            // Text in Telugu

            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            account.setText(resources.getString(R.string.my_preferences));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(getActivity(),"bn");
            resources =context.getResources();



            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            account.setText(resources.getString(R.string.my_preferences));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(getActivity(),"gu");
            resources =context.getResources();



            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
            account.setText(resources.getString(R.string.my_preferences));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));
            member.setText(resources.getString(R.string.membership_plan_others));


        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(getActivity(),"ml");
            resources =context.getResources();



            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            account.setText(resources.getString(R.string.my_preferences));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(getActivity(),"mr");
            resources =context.getResources();



            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
            account.setText(resources.getString(R.string.my_preferences));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(getActivity(),"pa");
            resources =context.getResources();



            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            account.setText(resources.getString(R.string.my_preferences));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }else if(LocaleHelper.getLanguage(getActivity()).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(getActivity(),"ta");
            resources =context.getResources();

            // Text in Tamil

            find.setText(resources.getString(R.string.find_dhaba_and_job_owners));
            search.setText(resources.getString(R.string.search));
            lan.setText(resources.getString(R.string.language_terms_amp_conditions));
            set.setText(resources.getString(R.string.settings));
//            Driver.setText(resources.getString(R.string.drivers));
//            Dhaba.setText(resources.getString(R.string.dhabas));
//            mech.setText(resources.getString(R.string.mechanics));
            account.setText(resources.getString(R.string.my_preferences));
            member.setText(resources.getString(R.string.membership_plan_others));
            help1.setText(resources.getString(R.string.in_case_of_any_trouble_in_using_app));
            helps.setText(resources.getString(R.string.help));

        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String cate = ""+snapshot.child("Cat").getValue();


        switch (cate) {
            case "Owner":
                // owner Profile
//                    Intent intent = new Intent(getActivity(), perference.class);
//                    intent.putExtra("cat", "owner");
//                    startActivity(intent);
//                        ShowBox();
                DatabaseReference owner = FirebaseDatabase.getInstance().getReference();
                owner.child("note_owner").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if((""+ds.child("seen").getValue()).equals("false")){
//                                cou =  ds.getChildrenCount();
//                                count.setText((int) cou);
                                    count_lay.setVisibility(View.VISIBLE);
//                                    count.setText(arrayList.size());

                            }else if((""+ds.child("seen").getValue()).equals("true")){
                                count_lay.setVisibility(View.INVISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Drivers":
                // Driver Profile
//                    Intent intent1 = new Intent(getActivity(), perference.class);
//                    intent1.putExtra("cat", "driver");
//                    startActivity(intent1);
//                        ShowBox();
                DatabaseReference driver = FirebaseDatabase.getInstance().getReference();
                driver.child("note_driver").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if((""+ds.child("seen").getValue()).equals("false")){
//                                cou = (int) ds.getChildrenCount();
//                                count.setText(cou);
                                count_lay.setVisibility(View.VISIBLE);
//                                    count.setText(arrayList.size());

                            }else if((""+ds.child("seen").getValue()).equals("true")){
                                count_lay.setVisibility(View.INVISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Mech":
                // Mech Profile
//                    Intent intent2 = new Intent(getActivity(), perference.class);
//                    intent2.putExtra("cat", "mech");
//                    startActivity(intent2);
//                        Toast.makeText(getActivity(), "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();
                DatabaseReference mech = FirebaseDatabase.getInstance().getReference();
                mech.child("note_mech").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if((""+ds.child("seen").getValue()).equals("false")){
//                                cou = (int) ds.getChildrenCount();
//                                count.setText(cou);
                                count_lay.setVisibility(View.VISIBLE);
//                                    count.setText(arrayList.size());

                            }else if((""+ds.child("seen").getValue()).equals("true")){
                                count_lay.setVisibility(View.INVISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Dhaba":
                // Dhaba Profile
//                    Intent intent3 = new Intent(getActivity(), perference.class);
//                    intent3.putExtra("cat", "dhaba");
//                    startActivity(intent3);
//                        Toast.makeText(getActivity(), "You are not authorized for this! Sorry for the inconvenience", Toast.LENGTH_SHORT).show();
                DatabaseReference dhaba = FirebaseDatabase.getInstance().getReference();
                dhaba.child("note_dhaba").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if((""+ds.child("seen").getValue()).equals("false")){
//                                cou = (int) ds.getChildrenCount();
//                                count.setText(cou);
                               count_lay.setVisibility(View.VISIBLE);

                            }else if((""+ds.child("seen").getValue()).equals("true")){
                                count_lay.setVisibility(View.INVISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}