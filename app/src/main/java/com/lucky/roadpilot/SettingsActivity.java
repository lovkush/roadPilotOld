package com.lucky.roadpilot;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Context;
import android.content.res.Resources;
import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    TextView logout;
    FirebaseAuth mAut;
    Switch ins_sw,age_sw;
    TextView ins,terms,privacy,feedback,delete,text_in,support,users,text_job;
    private View i,vi;
    TextView dialog_language;
    int lang_selected;
    LinearLayout insP,job_lay;
    RelativeLayout show_lan_dialog;

    CheckBox job;

    Context context;
    Resources resources;

    String inst,ins_doc,Customer,id,job_;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logout = findViewById(R.id.logout);
        text_job = findViewById(R.id.text_job);
        job_lay = findViewById(R.id.job_lay);
        job = findViewById(R.id.job);
        ins_sw = findViewById(R.id.ins_sw);
        users = findViewById(R.id.users);
        ins = findViewById(R.id.ins);
        vi = findViewById(R.id.vi);
//        v = findViewById(R.id.v);
//        rela = findViewById(R.id.rela);
        i = findViewById(R.id.i);
//        range = findViewById(R.id.range);
//        range_tv = findViewById(R.id.range_tv);
        support = findViewById(R.id.support);
        terms = findViewById(R.id.terms);
        privacy = findViewById(R.id.policy);
        age_sw = findViewById(R.id.age_sw);
        feedback = findViewById(R.id.feed);
        insP = findViewById(R.id.insP);
        delete = findViewById(R.id.delete);
        mAut = FirebaseAuth.getInstance();

        TextView set = findViewById(R.id.set);
        TextView text_n = findViewById(R.id.text_n);
        TextView text_nt = findViewById(R.id.text_nt);
        TextView text = findViewById(R.id.text);
        TextView text_l = findViewById(R.id.text_l);
        TextView text_lt = findViewById(R.id.text_lt);
        TextView text_ll = findViewById(R.id.text_ll);
        text_in = findViewById(R.id.text_in);
        TextView text_int = findViewById(R.id.text_int);
        TextView leg = findViewById(R.id.leg);

        dialog_language = (TextView)findViewById(R.id.dialog_language);
        show_lan_dialog = (RelativeLayout)findViewById(R.id.showlangdialog);

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Customer));
                startActivity(callIntent);
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AllUsersActivity.class);
                startActivity(intent);
            }
        });


        if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("en"))
        {
            context = LocaleHelper.setLocale(SettingsActivity.this,"en");
            resources =context.getResources();
            dialog_language.setText("ENGLISH");

            // Text In English

            set.setText(resources.getString(R.string.settings));
            ins.setText(resources.getString(R.string.insurance));
            leg.setText(resources.getString(R.string.legal));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_in.setText(resources.getString(R.string.insurance));
            text_n.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
            text.setText(resources.getString(R.string.notifications));
            text_l.setText(resources.getString(R.string.languages));
            text_ll.setText(resources.getString(R.string.languages));
            terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account));
            support.setText(resources.getString(R.string.support));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));

//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));


            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));


            setTitle(resources.getString(R.string.app_name));
            lang_selected = 0;
        }else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("hi")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"hi");
            resources =context.getResources();
            dialog_language.setText("हिन्दी");

            text_n.setText(resources.getString(R.string.notifications));
            text_l.setText(resources.getString(R.string.languages));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
            terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account)); text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            leg.setText(resources.getString(R.string.legal));
            text.setText(resources.getString(R.string.notifications));
            support.setText(resources.getString(R.string.support));
            text_in.setText(resources.getString(R.string.insurance));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            set.setText(resources.getString(R.string.settings));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            ins.setText(resources.getString(R.string.insurance));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));


            setTitle(resources.getString(R.string.app_name));
            lang_selected =1;
        }
        else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("kn")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"kn");
            resources =context.getResources();
            dialog_language.setText("ಕನ್ನಡ");

            set.setText(resources.getString(R.string.settings));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
            text_ll.setText(resources.getString(R.string.languages));
            leg.setText(resources.getString(R.string.legal));
            text_l.setText(resources.getString(R.string.languages)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            support.setText(resources.getString(R.string.support));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            ins.setText(resources.getString(R.string.insurance));
            text_in.setText(resources.getString(R.string.insurance));
            text.setText(resources.getString(R.string.notifications));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            text_n.setText(resources.getString(R.string.notifications));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =2;
        }
        else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("te")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"te");
            resources =context.getResources();
            dialog_language.setText("తెలుగు");

            set.setText(resources.getString(R.string.settings));
            support.setText(resources.getString(R.string.support));
            text_n.setText(resources.getString(R.string.notifications));
            ins.setText(resources.getString(R.string.insurance));
            text_in.setText(resources.getString(R.string.insurance));
            leg.setText(resources.getString(R.string.legal)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account));
            text_l.setText(resources.getString(R.string.languages));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));

            setTitle(resources.getString(R.string.app_name));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            lang_selected =3;
        }
        else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("bn")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"bn");
            resources =context.getResources();
            dialog_language.setText("বাংলা");

            set.setText(resources.getString(R.string.settings));
            text_n.setText(resources.getString(R.string.notifications));
            ins.setText(resources.getString(R.string.insurance));
            support.setText(resources.getString(R.string.support));
            text_in.setText(resources.getString(R.string.insurance));
            leg.setText(resources.getString(R.string.legal)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            feedback.setText(resources.getString(R.string.feedback));
            logout.setText(resources.getString(R.string.logout));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            delete.setText(resources.getString(R.string.delete_account));
            text_l.setText(resources.getString(R.string.languages));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =4;
        }else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("gu")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"gu");
            resources =context.getResources();
            dialog_language.setText("ગુજરાતી");

            set.setText(resources.getString(R.string.settings));
            text_n.setText(resources.getString(R.string.notifications));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            ins.setText(resources.getString(R.string.insurance));
            text_in.setText(resources.getString(R.string.insurance));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            support.setText(resources.getString(R.string.support));
            leg.setText(resources.getString(R.string.legal)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account));
            text_l.setText(resources.getString(R.string.languages));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =5;
        }else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("ml")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"ml");
            resources =context.getResources();
            dialog_language.setText("മലയാളം");

            set.setText(resources.getString(R.string.settings));
            text_n.setText(resources.getString(R.string.notifications));
            ins.setText(resources.getString(R.string.insurance));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            text_in.setText(resources.getString(R.string.insurance));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            support.setText(resources.getString(R.string.support));
            leg.setText(resources.getString(R.string.legal)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account));
            text_l.setText(resources.getString(R.string.languages));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =6;
        }else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("mr")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"mr");
            resources =context.getResources();
            dialog_language.setText("मराठी");

            set.setText(resources.getString(R.string.settings));
            text_n.setText(resources.getString(R.string.notifications));
            ins.setText(resources.getString(R.string.insurance));
            text_in.setText(resources.getString(R.string.insurance));
            leg.setText(resources.getString(R.string.legal)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            feedback.setText(resources.getString(R.string.feedback));
            support.setText(resources.getString(R.string.support));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account));
            text_l.setText(resources.getString(R.string.languages));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =7;
        }else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("pa")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"pa");
            resources =context.getResources();
            dialog_language.setText("ਪੰਜਾਬੀ");

            set.setText(resources.getString(R.string.settings));
            text_n.setText(resources.getString(R.string.notifications));
            ins.setText(resources.getString(R.string.insurance));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            text_in.setText(resources.getString(R.string.insurance));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            leg.setText(resources.getString(R.string.legal)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            logout.setText(resources.getString(R.string.logout));
            support.setText(resources.getString(R.string.support));
            delete.setText(resources.getString(R.string.delete_account));
            text_l.setText(resources.getString(R.string.languages));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =8;
        }else
            if(LocaleHelper.getLanguage(SettingsActivity.this).equalsIgnoreCase("ta")){
            context = LocaleHelper.setLocale(SettingsActivity.this,"ta");
            resources =context.getResources();
            dialog_language.setText("தமிழ்");

            set.setText(resources.getString(R.string.settings));
//            range.setText(resources.getString(R.string.km_range));
//            range_tv.setText(resources.getString(R.string.range_to_search));
            text_n.setText(resources.getString(R.string.notifications));
            ins.setText(resources.getString(R.string.insurance));
            text_job.setText(resources.getString(R.string.do_you_in_job));
            job.setText(resources.getString(R.string.do_you_in_job));
            support.setText(resources.getString(R.string.support));
            text_in.setText(resources.getString(R.string.insurance));
            leg.setText(resources.getString(R.string.legal)); terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
            privacy.setText(resources.getString(R.string.privacy_policy));
            feedback.setText(resources.getString(R.string.feedback));
            logout.setText(resources.getString(R.string.logout));
            delete.setText(resources.getString(R.string.delete_account));
            text_l.setText(resources.getString(R.string.languages));
            text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
            text_ll.setText(resources.getString(R.string.languages));
            text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
            text.setText(resources.getString(R.string.notifications));
            text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));

            setTitle(resources.getString(R.string.app_name));
            lang_selected =9;
        }
        show_lan_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] Language = {"ENGLISH","हिन्दी","ಕನ್ನಡ","తెలుగు","বাংলা","ગુજરાતી","മലയാളം","मराठी","தமிழ்"};
                final int checkItem;
                Log.d("Clicked","Clicked");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
                dialogBuilder.setTitle("Select a Language")
                        .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog_language.setText(Language[i]);
                                if(Language[i].equals("ENGLISH")){
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"en");
                                    resources =context.getResources();
                                    lang_selected = 0;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    logout.setText(resources.getString(R.string.logout));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
                                    delete.setText(resources.getString(R.string.delete_account));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    leg.setText(resources.getString(R.string.legal));
                                    support.setText(resources.getString(R.string.support));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    set.setText(resources.getString(R.string.settings));
                                    ins.setText(resources.getString(R.string.insurance));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text.setText(resources.getString(R.string.notifications));


                                    setTitle(resources.getString(R.string.app_name));
                                }
                                if(Language[i].equals("हिन्दी"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"hi");
                                    resources =context.getResources();
                                    lang_selected = 1;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    support.setText(resources.getString(R.string.support));
                                    logout.setText(resources.getString(R.string.logout));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    ins.setText(resources.getString(R.string.insurance));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text.setText(resources.getString(R.string.notifications));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    set.setText(resources.getString(R.string.settings));
                                    text_l.setText(resources.getString(R.string.languages));


                                    setTitle(resources.getString(R.string.app_name));
                                }
                                if(Language[i].equals("ಕನ್ನಡ"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"kn");
                                    resources =context.getResources();
                                    lang_selected = 2;
                                    support.setText(resources.getString(R.string.support));
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    logout.setText(resources.getString(R.string.logout));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    text_l.setText(resources.getString(R.string.languages));
                                    ins.setText(resources.getString(R.string.insurance));
                                    set.setText(resources.getString(R.string.settings));
                                    text.setText(resources.getString(R.string.notifications));
                                    text_in.setText(resources.getString(R.string.insurance));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("తెలుగు"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"te");
                                    resources =context.getResources();
                                    lang_selected = 3;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    logout.setText(resources.getString(R.string.logout));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    set.setText(resources.getString(R.string.settings));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    support.setText(resources.getString(R.string.support));
                                    ins.setText(resources.getString(R.string.insurance));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_l.setText(resources.getString(R.string.languages));
                                    text.setText(resources.getString(R.string.notifications));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("বাংলা"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"bn");
                                    resources =context.getResources();
                                    lang_selected = 4;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    logout.setText(resources.getString(R.string.logout));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    set.setText(resources.getString(R.string.settings));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    ins.setText(resources.getString(R.string.insurance));
                                    support.setText(resources.getString(R.string.support));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_l.setText(resources.getString(R.string.languages));
                                    text.setText(resources.getString(R.string.notifications));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("ગુજરાતી"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"gu");
                                    resources =context.getResources();
                                    lang_selected = 5;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    logout.setText(resources.getString(R.string.logout));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    set.setText(resources.getString(R.string.settings));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    ins.setText(resources.getString(R.string.insurance));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    support.setText(resources.getString(R.string.support));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_l.setText(resources.getString(R.string.languages));
                                    text.setText(resources.getString(R.string.notifications));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("മലയാളം"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"ml");
                                    resources =context.getResources();
                                    lang_selected = 6;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    logout.setText(resources.getString(R.string.logout));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    set.setText(resources.getString(R.string.settings));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    ins.setText(resources.getString(R.string.insurance));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    support.setText(resources.getString(R.string.support));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_l.setText(resources.getString(R.string.languages));
                                    text.setText(resources.getString(R.string.notifications));

                                    setTitle(resources.getString(R.string.app_name));
                                }if(Language[i].equals("मराठी"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"mr");
                                    resources =context.getResources();
                                    lang_selected = 7;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    logout.setText(resources.getString(R.string.logout));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    set.setText(resources.getString(R.string.settings));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    ins.setText(resources.getString(R.string.insurance));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    support.setText(resources.getString(R.string.support));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_l.setText(resources.getString(R.string.languages));
                                    text.setText(resources.getString(R.string.notifications));

                                    setTitle(resources.getString(R.string.app_name));
                                }
//                                if(Language[i].equals("ਪੰਜਾਬੀ"))
//                                {
//                                    context = LocaleHelper.setLocale(SettingsActivity.this,"pa");
//                                    resources =context.getResources();
//                                    lang_selected = 8;
//                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
//                                    privacy.setText(resources.getString(R.string.privacy_policy));
//                                    feedback.setText(resources.getString(R.string.feedback));
//                                    logout.setText(resources.getString(R.string.logout));
//                                    delete.setText(resources.getString(R.string.delete_account));
//                                    set.setText(resources.getString(R.string.settings));
//                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
//                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
//                                    ins.setText(resources.getString(R.string.insurance));
//                                    text_ll.setText(resources.getString(R.string.languages));
//                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
//                                    text_in.setText(resources.getString(R.string.insurance));
//                                    text_n.setText(resources.getString(R.string.notifications));
//                                    leg.setText(resources.getString(R.string.legal));
//                                    text_l.setText(resources.getString(R.string.languages));
//                                    text.setText(resources.getString(R.string.notifications));
//
//                                    setTitle(resources.getString(R.string.app_name));
//                                }
                                if(Language[i].equals("தமிழ்"))
                                {
                                    context = LocaleHelper.setLocale(SettingsActivity.this,"ta");
                                    resources =context.getResources();
                                    lang_selected = 8;
                                    terms.setText(resources.getString(R.string.terms_and_conditions_of_use));
                                    privacy.setText(resources.getString(R.string.privacy_policy));
                                    feedback.setText(resources.getString(R.string.feedback));
                                    logout.setText(resources.getString(R.string.logout));
                                    text_job.setText(resources.getString(R.string.do_you_in_job));
                                    job.setText(resources.getString(R.string.do_you_in_job));
//                                    range.setText(resources.getString(R.string.km_range));
//                                    range_tv.setText(resources.getString(R.string.range_to_search));
                                    delete.setText(resources.getString(R.string.delete_account));
                                    set.setText(resources.getString(R.string.settings));
                                    text_nt.setText(resources.getString(R.string.get_notified_every_time_you_receive_a_message_when_someone_reacts_to_your_profile_you_can_also_get_a_daily_recap_of_your_popularity_on_thriving_so_you_never_miss_a_thing));
                                    text_lt.setText(resources.getString(R.string.change_your_language_here_to_understand_the_app_better));
                                    ins.setText(resources.getString(R.string.insurance));
                                    text_ll.setText(resources.getString(R.string.languages));
                                    support.setText(resources.getString(R.string.support));
                                    text_int.setText(resources.getString(R.string.we_provide_the_insurance_in_case_of_any_trouble_for_both_vehicle_and_life));
                                    text_in.setText(resources.getString(R.string.insurance));
                                    text_n.setText(resources.getString(R.string.notifications));
                                    leg.setText(resources.getString(R.string.legal));
                                    text_l.setText(resources.getString(R.string.languages));
                                    text.setText(resources.getString(R.string.notifications));

                                    setTitle(resources.getString(R.string.app_name));
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                dialogBuilder.create().show();
            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure to logout")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAut.signOut();
                                startActivity(new Intent(SettingsActivity.this,SplashActivity.class));
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingsActivity.this, "No", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.create();
                builder.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Delete")
                        .setMessage("Are you sure to Delete Account? Our CustomerCare will Contact You soon..")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                HashMap<String, String> hashMap1 = new HashMap<>();

                                hashMap1.put("Delete", "Pending");
                                hashMap1.put("uid", mAut.getCurrentUser().getUid());

                                DatabaseReference apply = FirebaseDatabase.getInstance().getReference().child("Delete_Request").child(mAut.getCurrentUser().getUid());
                                apply.setValue(hashMap1);

                                mAut.signOut();

                                startActivity(new Intent(SettingsActivity.this,SplashActivity.class));
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingsActivity.this, "No", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.create();
                builder.show();
            }
        });

           terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DriverRegisterActivity.class);
                intent.putExtra("key", "terms");
                v.getContext().startActivity(intent);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DriverRegisterActivity.class);
                intent.putExtra("key", "privacy");
                v.getContext().startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto: contact@roadpilot.co.in"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
//                intent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                startActivity(intent);
            }
        });


  DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                    reference1.child("Users").child(mAut.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                         inst = ""+snapshot.child("Insurance").getValue();
                         ins_doc = ""+snapshot.child("Insurance_Doc").getValue();
                         id = ""+ snapshot.child("Id").getValue();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inst.equals("Completed")) {

                    Intent intent = new Intent(SettingsActivity.this, InsuranceActivity.class);
                    intent.putExtra("Doc", ins_doc);
                    startActivity(intent);
                }
            }
        });

        ins_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ins_sw.isChecked()) {

                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("Insurance", "Pending");
                    hashMap.put("Insurance_Doc", "Link");

                    DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Users").child(mAut.getCurrentUser().getUid());
                    Driver.updateChildren(hashMap);

                    HashMap<String, Object> hashMap1 = new HashMap<>();

                    hashMap1.put("Insurance", "Pending");
                    hashMap1.put("Insurance_Doc", "Link");
                    hashMap1.put("Id", id);
                    hashMap1.put("uid", mAut.getCurrentUser().getUid());

                    DatabaseReference apply = FirebaseDatabase.getInstance().getReference().child("Ins_Apply").child(mAut.getCurrentUser().getUid());
                    apply.setValue(hashMap1);

                    ins.setTextColor(R.color.appcolor);
                }else  {
                    Toast.makeText(SettingsActivity.this, "You have Insurances already ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        age_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(age_sw.isChecked()){
                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("Notification", "yes");

                    DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Users").child(mAut.getCurrentUser().getUid());
                    Driver.updateChildren(hashMap);

                }else if(!age_sw.isChecked()){
                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("Notification", "no");

                    DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Users").child(mAut.getCurrentUser().getUid());
                    Driver.updateChildren(hashMap);

                }
            }
        });

        job.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

//                    if(job.isChecked()){
//                        hashMap.put("job","None");
//                    }else {
//                        hashMap.put("job","Required");
//                    }


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("job","None");


                    DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Drivers").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    Driver.updateChildren(hashMap);
                    DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    DriverRef.updateChildren(hashMap);

                }

                else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("job","Required");


                    DatabaseReference Driver = FirebaseDatabase.getInstance().getReference().child("Drivers").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    Driver.updateChildren(hashMap);
                    DatabaseReference DriverRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    DriverRef.updateChildren(hashMap);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Dynamic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Customer = ""+snapshot.child("Customer").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        reference1.keepSynced(true);
        reference1.child("Users").child(Objects.requireNonNull(mAut.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if((""+snapshot.child("Cat").getValue()).equals("Drivers")){

                    job_ = ""+snapshot.child("job").getValue();

                    if(job_.equals("None")){
                        job.setChecked(true);
                    }else if(job_.equals("Required")) {
                        job.setChecked(false);
                    }

                }
                if (("" + snapshot.child("Insurance").getValue()).equals("Completed")) {

                    ins_sw.setClickable(false);
                    ins_sw.setVisibility(View.INVISIBLE);
                    ins.setTextColor(resources.getColor(R.color.green));

                }else   if (("" + snapshot.child("Insurance").getValue()).equals("Pending")){
                    ins_sw.setClickable(false);
                    ins_sw.setVisibility(View.INVISIBLE);
                    ins.setTextColor(resources.getColor(R.color.gold));


                }else {
                    ins_sw.setChecked(false);
                    ins_sw.setVisibility(View.VISIBLE);
                    ins.setTextColor(resources.getColor(R.color.red));
                }

                if (("" + snapshot.child("Notification").getValue()).equals("yes")) {

                    age_sw.setChecked(true);

                }else if (("" + snapshot.child("Notification").getValue()).equals("no")) {

                    age_sw.setChecked(false);

                }





                if((""+ snapshot.child("Cat").getValue()).equals("Drivers")){
                    insP.setVisibility(View.VISIBLE);
                    vi.setVisibility(View.VISIBLE);
                    text_in.setVisibility(View.VISIBLE);
                    text_job.setVisibility(View.VISIBLE);
                    job_lay.setVisibility(View.VISIBLE);
//                    range_tv.setVisibility(View.VISIBLE);
//                    rela.setVisibility(View.VISIBLE);
                    i.setVisibility(View.VISIBLE);
//                    range_num.setText(""+snapshot.child("range").getValue());
                }else {
                    insP.setVisibility(View.GONE);
                    vi.setVisibility(View.GONE);
                    text_in.setVisibility(View.GONE);
                    text_job.setVisibility(View.GONE);
                    job_lay.setVisibility(View.GONE);
//                    range_num.setVisibility(View.GONE);
//                    range.setVisibility(View.GONE);
//                    range_tv.setVisibility(View.GONE);
                    i.setVisibility(View.GONE);
//                    rela.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        HashMap<String,Object> has = new HashMap<>();
//        has.put("range",range_num.getText().toString());
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("Users").child(Objects.requireNonNull(mAut.getCurrentUser()).getUid()).updateChildren(has);
//
//    }
}