package com.lucky.roadpilot;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lucky.roadpilot.Adapter.AutoFillAdapter;
import com.lucky.roadpilot.Adapter.UserAdapter;
import com.lucky.roadpilot.Models.UserModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NeedActivity extends AppCompatActivity implements LocationListener {


    RecyclerView user_rec;
    AutoCompleteTextView Search;
    ArrayList<com.lucky.roadpilot.Models.Location> location;
    UserAdapter jobAdapter;
    ArrayList<UserModel> arrayList;

    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String value;
    String locations;
    Context context;
    Resources resources;
    private LinearLayout error;

    private final String[] SEARCHES = {"Adoni","Amaravati","Anantapur", "Chandragiri","Chittoor","Dowlaiswaram","Eluru","Guntur","Kadapa","Kakinada","Kurnool","Machilipatnam","Nagarjunakoṇḍa","Rajahmundry","Srikakulam","Tirupati","Vijayawada","Visakhapatnam","Vizianagaram","Yemmiganur",
            "Itanagar",
            "Dhuburi","Dibrugarh","Dispur","Guwahati","Jorhat","Nagaon","Sibsagar","Silchar","Tezpur","Tinsukia",
            "Ara","Baruni","Begusarai","Bettiah","Bhagalpur","Bihar","Sharif","Bodh","Gaya","Buxar","Chapra","Darbhanga","Dehri","Dinapur","Nizamat","Gaya","Hajipur","Jamalpur","Katihar","Madhubani","Motihari","Munger","Muzaffarpur","Patna","Purnia","Pusa","Saharsa","Samastipur","Sasaram","Sitamarhi","Siwan", "Chandigarh",
            "Ambikapur","Bhilai","Bilaspur","Dhamtari","Durg","Jagdalpur","Raipur","Rajnandgaon",
            "Silvassa",
            "Daman","Dia",
            "Delhi","New Delhi",
            "Madgaon","Panaji",
            "Ahmadabad","Amreli","Bharuch","Bhavnagar","Bhuj","Dwarka","Gandhinagar","Godhra","Jamnagar","Junagadh","Kandla","Khambhat","Kheda","Mahesana","Morvi","Nadiad","Navsari","Okha","Palanpur","Patan","Porbandar","Rajkot","Surat","Surendranagar","Valsad","Veraval",
            "Ambala","Bhiwani","Chandigarh","Faridabad","Firozpur","Jhirka","Gurgaon","Hansi","Hisar","Jind","Kaithal","Karnal","Kurukshetra","Panipat","Pehowa","Rewari","Rohtak","Sirsa","Sonipat",
            "Bilaspur","Chamba","Dalhousie","Dharmshala","Hamirpur","Kangra","Kullu","Mandi","Nahan","Shimla","Una",
            "Anantnag","Baramula","Doda","Gulmarg","Jammu","Kathua","Punch","Rajauri","Srinagar","Udhampur",
            "Bokaro","Chaibasa","Deoghar","Dhanbad","Dumka","Giridih","Hazaribag","Jamshedpur","Jharia","Rajmahal","Ranchi","Saraikela",
            "Badami","Ballari","Bangalore","Belgavi","Bhadravati","Bidar","Chikkamagaluru","Chitradurga","Davangere","Halebid","Hassan","Hubballi-Dharwad","Kalaburagi","Kolar","Madikeri","Mandya","Mangaluru","Mysuru","Raichur","Shivamogga","Shravanabelagola","Shrirangapattana","Tumkuru",
            "Alappuzha","Badagara","Idukki","Kannur","Kochi","Kollam","Kottayam","Kozhikode","Mattancheri","Palakkad","Thalassery","Thiruvananthapuram","Thrissur",
            "Leh",
            "Balaghat","Barwani","Betul","Bharhut","Bhind","Bhojpur","Bhopal","Burhanpur","Chhatarpur","Chhindwara","Damoh","Datia","Dewas","Dhar","Guna","Gwalior","Hoshangabad","Indore","Itarsi","Jabalpur","Jhabua","Khajuraho","Khandwa","Khargon","Maheshwar","Mandla","Mandsaur","Mhow","Morena","Murwara","Narsimhapur","Narsinghgarh","Narwar","Neemuch","Nowgong","Orchha","Panna","Raisen","Rajgarh","Ratlam","Rewa","Sagar","Sarangpur","Satna","Sehore","Seoni","Shahdol","Shajapur","Sheopur","Shivpuri","Ujjain","Vidisha",
            "Ahmadnagar","Akola","Amravati","Aurangabad","Bhandara","Bhusawal","Bid","Buldana","Chandrapur","Daulatabad","Dhule","Jalgaon","Kalyan","Karli","Kolhapur","Mahabaleshwar","Malegaon","Matheran","Mumbai","Nagpur","Nanded","Nashik","Osmanabad","Pandharpur","Parbhani","Pune","Ratnagiri","Sangli","Satara","Sevagram","Solapur","Thane","Ulhasnagar","Vasai-Virar","Wardha","Yavatmal",
            "Imphal",
            "Cherrapunji","Shillong",
            "Aizawl","Lunglei",
            "Kohima","Mon","Phek","Wokha","Zunheboto",
            "Balangir","Baleshwar","Baripada","Bhubaneshwar","Brahmapur","Cuttack","Dhenkanal","Keonjhar","Konark","Koraput","Paradip","Phulabani","Puri","Sambalpur","Udayagiri",
            "Karaikal","Mahe","Puducherry","Yanam",
            "Punjab","Amritsar","Batala","Chandigarh","Faridkot","Firozpur","Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Nabha","Patiala","Rupnagar","Sangrur",
            "Abu","Ajmer","Alwar","Amer","Barmer","Beawar","Bharatpur","Bhilwara","Bikaner","Bundi","Chittaurgarh","Churu","Dhaulpur","Dungarpur","Ganganagar","Hanumangarh","Jaipur","Jaisalmer","Jalor","Jhalawar","Jhunjhunu","Jodhpur","Kishangarh","Kota","Merta","Nagaur","Nathdwara","Pali","Phalodi","Pushkar","Sawai Madhopur","Shahpura","Sikar","Sirohi","Tonk","Udaipur",
            "Gangtok","Gyalsing","Lachung","Mangan",
            "Padur","Arcot","Chengalpattu","Chennai","Chidambaram","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Kanchipuram","Kanniyakumari","Kodaikanal","Kumbakonam","Madurai","Mamallapuram","Nagappattinam","Nagercoil","Palayankottai","Pudukkottai","Rajapalaiyam","Ramanathapuram","Salem","Thanjavur","Tiruchchirappalli","Tirunelveli","Tiruppur","Tuticorin","Udhagamandalam","Vellore",
            "Hyderabad","Karimnagar","Khammam","Mahbubnagar","Nizamabad","Sangareddi","Warangal",
            "Tripura","Agartala",
            "Agra","Aligarh","Amroha","Ayodhya","Azamgarh","Bahraich","Ballia","Banda","Bara Banki","Bareilly","Basti","Bijnor","Bithur","Budaun","Bulandshahr","Deoria","Etah","Etawah","Faizabad","Farrukhabad-cum-Fatehgarh","Fatehpur","Fatehpur Sikri","Ghaziabad","Ghazipur","Gonda","Gorakhpur","Hamirpur","Hardoi","Hathras","Jalaun","Jaunpur","Jhansi","Kannauj","Kanpur","Lakhimpur","Lalitpur","Lucknow","Mainpuri","Mathura","Meerut","Mirzapur-Vindhyachal","Moradabad","Muzaffarnagar","Partapgarh","Pilibhit","Prayagraj","Rae Bareli","Rampur","Saharanpur","Sambhal","Shahjahanpur","Sitapur","Sultanpur","Tehri","Varanasi",
            "Almora","Dehra Dun","Haridwar","Mussoorie","Nainital","Pithoragarh",
            "Alipore","Alipur","Duar","Asansol","Baharampur","Bally","Balurghat","Bankura","Baranagar","Barasat","Barrackpore","Basirhat","Bhatpara","Bishnupur","Budge","Budge","Burdwan","Chandernagore","Darjiling","Diamond","Harbour","Dum Dum","Durgapur","Halisahar","Haora","Hugli","Ingraj Bazar","Jalpaiguri","Kalimpong","Kamarhati","Kanchrapara","Kharagpur","Koch Bihar","Kolkata","Krishnanagar","Malda","Midnapore","Murshidabad","Navadwip","Palashi","Panihati","Purulia","Raiganj","Santipur","Shantiniketan","Shrirampur","Siliguri","Siuri","Tamluk","Titagarh",
    };



    private TextView all,cities,ranges;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Search = findViewById(R.id.search);
        user_rec = findViewById(R.id.user_rec);
        all = findViewById(R.id.all);
        cities = findViewById(R.id.cities);
        ranges = findViewById(R.id.range);
        error = findViewById(R.id.error);
        arrayList = new ArrayList<>();

        location = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        value = intent.getStringExtra("cat");

        final String[] Location = new String[1];

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Location[0] = ""+ds.child("Location").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                location.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    com.lucky.roadpilot.Models.Location model = ds.getValue(com.lucky.roadpilot.Models.Location.class);
                    location.add(model);
                }
                AutoFillAdapter adapter = new AutoFillAdapter(NeedActivity.this,location);
                Search.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                jobAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                jobAdapter.getFilter().filter(charSequence);

                AutoFillAdapter adapter = new AutoFillAdapter(NeedActivity.this,location);
                Search.setAdapter(adapter);

                FilterCity(Search.getText().toString());





//                if(models.isEmpty()){
//                    error.setVisibility(View.VISIBLE);
//                }else {
//                    error.setVisibility(View.GONE);
//                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FilterCity(Search.getText().toString());

            }
        });



    }

    private void FilterCity(String city) {

//        Calendar cdate = Calendar.getInstance();
//
//        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");
//
//        final String savedate = currentdates.format(cdate.getTime());
//
//        Calendar ctime = Calendar.getInstance();
//        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
//
//        final String savetime = currenttime.format(ctime.getTime());
//
//        String time = savedate + ":" + savetime;
//
//        Long times = System.currentTimeMillis();
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("Id",String.valueOf(times));
//        hashMap.put("time",time);
//        hashMap.put("title","Searched for "+ value + "At Location " + city);
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Activities").child(String.valueOf(times))
//                .setValue(hashMap);

        switch (value){
            case "owner":
                DatabaseReference owners = FirebaseDatabase.getInstance().getReference();
                owners.keepSynced(true);
                owners.child("Owner").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser().getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(city)){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }

                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }

                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Driver":
                DatabaseReference Driver = FirebaseDatabase.getInstance().getReference();
                Driver.keepSynced(true);
                Driver.child("Drivers").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(city) && (""+ds.child("job").getValue()).equals("Required")){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }

                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }

                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Dhaba":
                DatabaseReference Dhaba = FirebaseDatabase.getInstance().getReference();
                Dhaba.keepSynced(true);
                Dhaba.child("Dhaba").addValueEventListener(new ValueEventListener() {
//                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(city)){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }
                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }


                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
//                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Mech":
                DatabaseReference mech = FirebaseDatabase.getInstance().getReference();
                mech.keepSynced(true);
                mech.child("Mech").addValueEventListener(new ValueEventListener() {
//                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser().getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(city)){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }
                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }

                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
//                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Search.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();

        getLocation();
//        LanguageChoose();

//        Calendar cdate = Calendar.getInstance();
//
//        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");
//
//        final String savedate = currentdates.format(cdate.getTime());
//
//        Calendar ctime = Calendar.getInstance();
//        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
//
//        final String savetime = currenttime.format(ctime.getTime());
//
//        String time = savedate + ":" + savetime;
//
//        Long times = System.currentTimeMillis();
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("Id",String.valueOf(times));
//        hashMap.put("time",time);
//        hashMap.put("title","Searched for "+ value);
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child("Users").child("Activities").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times))
//                .setValue(hashMap);



        DatabaseReference user = FirebaseDatabase.getInstance().getReference();
        user.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                locations = ""+snapshot.child("Location").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        switch (value){
            case "owner":
                DatabaseReference owners = FirebaseDatabase.getInstance().getReference();
                owners.keepSynced(true);
                owners.child("Owner").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser().getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(locations)){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }

                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }

                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Driver":
                DatabaseReference Driver = FirebaseDatabase.getInstance().getReference();
                Driver.keepSynced(true);
                Driver.child("Drivers").addValueEventListener(new ValueEventListener() {
//                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser().getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(locations)){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }

                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }

                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
//                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Dhaba":
                DatabaseReference Dhaba = FirebaseDatabase.getInstance().getReference();
                Dhaba.keepSynced(true);
                Dhaba.child("Dhaba").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        arrayList.clear();

                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser().getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(locations)){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }
                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }


                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
//                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "Mech":
                DatabaseReference mech = FirebaseDatabase.getInstance().getReference();
                mech.keepSynced(true);
                mech.child("Mech").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser().getUid())) &&
                                    (""+ds.child("Location").getValue()).equals(locations)){
                                UserModel postModel = ds.getValue(UserModel.class);
                                arrayList.add(postModel);
                            }
                            if(arrayList.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }

                        }
                        jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
                        user_rec.setAdapter(jobAdapter);
                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref.keepSynced(true);
//        ref.child("Users").addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                arrayList.clear();
//
//                for(DataSnapshot ds:snapshot.getChildren()) {
//
//                    switch (value){
//                        case "owner":
//
//                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()) ||
//                                    (""+ds.child("Cat").getValue()).equals("Owner") ||
//                                    (""+ds.child("Location").getValue()).equals(addressList.get(0).getLocality())) {
//                                UserModel postModel = ds.getValue(UserModel.class);
//                                arrayList.add(postModel);
//                            }
//                            break;
//                        case "Driver":
//
//                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()) ||
//                                    (""+ds.child("Cat").getValue()).equals("Driver")||
//                                    (""+ds.child("Location").getValue()).equals(addressList.get(0).getLocality())) {
//                                UserModel postModel = ds.getValue(UserModel.class);
//                                arrayList.add(postModel);
//                            }
//                            break;
//                        case "Dhaba":
//
//                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()) ||
//                                    (""+ds.child("Cat").getValue()).equals("Dhaba")||
//                                    (""+ds.child("Location").getValue()).equals(addressList.get(0).getLocality())) {
//                                UserModel postModel = ds.getValue(UserModel.class);
//                                arrayList.add(postModel);
//                            }
//                            break;
//                        case "mech":
//
//                            if(!(""+ds.child("uid").getValue()).equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()) ||
//                                    (""+ds.child("Cat").getValue()).equals("Mech")||
//                                    (""+ds.child("Location").getValue()).equals(addressList.get(0).getLocality())) {
//                                UserModel postModel = ds.getValue(UserModel.class);
//                                arrayList.add(postModel);
//                            }
//                            break;
//                    }
//
//                }
//
//                jobAdapter = new UserAdapter(NeedActivity.this,arrayList);
//                user_rec.setAdapter(jobAdapter);
//                jobAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void getLocation() {

        if(ActivityCompat.checkSelfPermission(NeedActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getGoLocation();
        }else {
            ActivityCompat.requestPermissions(NeedActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    private void getGoLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Location> task) {
//
//                Location location = task.getResult();
//                if(location != null){
//
//                    try {
//                        Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
//                        addresses = geocoder.getFromLocation(
//                                location.getLatitude(), location.getLongitude(),1
//                        );
////                        shopcountryedt.setText(addresses.get(0).getCountryName());
////                        shopstateedt.setText(addresses.get(0).getAdminArea());
////                        shopcityedt.setText(addresses.get(0).getLocality());
////                        shopaddresstotaledt.setText(addresses.get(0).getAddressLine(0));
//
//                        address.setText(addresses.get(0).getLocality() + "," +  addresses.get(0).getAdminArea() + "," +  addresses.get(0).getCountryName() );
//
//                        Daddress.setText(addresses.get(0).getLocality() + "," +  addresses.get(0).getAdminArea() + "," +  addresses.get(0).getCountryName() );
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });


        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,5,this);

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null){
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addressList  =geocoder.getFromLocation(lat, lon, 1);
                Log.d("location", String.valueOf(lat));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
    
//    private void LanguageChoose(){
//        if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("en"))
//        {
//            context = LocaleHelper.setLocale(NeedActivity.this,"en");
//            resources =context.getResources();
//
//            // Text In English
//
//            Search.setText(resources.getString(R.string.search));
//
//
//
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("hi")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"hi");
//            resources =context.getResources();
//            Search.setText(resources.getString(R.string.search));
//
//        }
//        else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("kn")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"kn");
//            resources =context.getResources();
//
//            Search.setText(resources.getString(R.string.search));
//
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("te")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"te");
//            resources =context.getResources();
//            Search.setText(resources.getString(R.string.search));
//
//            // Text in Telugu
//
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("bn")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"bn");
//            resources =context.getResources();
//            Search.setText(resources.getString(R.string.search));
//
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("gu")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"gu");
//            resources =context.getResources();
//            Search.setText(resources.getString(R.string.search));
//
//
//
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("ml")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"ml");
//            resources =context.getResources();
//
//
//            Search.setText(resources.getString(R.string.search));
////
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("mr")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"mr");
//            resources =context.getResources();
//
//
//            Search.setText(resources.getString(R.string.search));
//
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("pa")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"pa");
//            resources =context.getResources();
//
//
//            Search.setText(resources.getString(R.string.search));
//
//
//        }else if(LocaleHelper.getLanguage(NeedActivity.this).equalsIgnoreCase("ta")){
//            context = LocaleHelper.setLocale(NeedActivity.this,"ta");
//            resources =context.getResources();
//
//            // Text in Tamil
//
//            Search.setText(resources.getString(R.string.search));
//
//        }
//
//
//    }


}