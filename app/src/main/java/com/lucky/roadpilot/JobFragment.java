package com.lucky.roadpilot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucky.roadpilot.Adapter.AutoFillAdapter;
import com.lucky.roadpilot.Adapter.JobAdapter;
import com.lucky.roadpilot.Models.JobModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends Fragment implements IOnBackPressed {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobFragment newInstance(String param1, String param2) {
        JobFragment fragment = new JobFragment();
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

    RecyclerView job_rec;
    AutoCompleteTextView search;
    FirebaseAuth mAuth;
    FloatingActionButton fab;
    private TextView all,cities,ranges;
    private LinearLayout error;
    ArrayList<com.lucky.roadpilot.Models.Location> location;
    Context context;
    Resources resources;
    private ArrayList<JobModel> models;
    JobAdapter jobAdapter;

    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addressList;

    String Locate, Job_Location;
    String uid;
    double range;
    double lat1,lat2,lat3,lon1,lon2,lon3,distance;

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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job, container, false);

        job_rec = view.findViewById(R.id.job_rec);
        search = view.findViewById(R.id.search);
        fab = view.findViewById(R.id.addDhada);
        all = view.findViewById(R.id.all);
        cities = view.findViewById(R.id.cities);
        error = view.findViewById(R.id.error);
        ranges = view.findViewById(R.id.range);

        location = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                location.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    com.lucky.roadpilot.Models.Location model = ds.getValue(com.lucky.roadpilot.Models.Location.class);
                    location.add(model);
                }
                AutoFillAdapter adapter = new AutoFillAdapter(getActivity(),location);
                search.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                jobAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                jobAdapter.getFilter().filter(charSequence);
                AutoFillAdapter adapter = new AutoFillAdapter(getActivity(),location);
                search.setAdapter(adapter);

                FilterCity(search.getText().toString());


//                if(models.isEmpty()){
//                    error.setVisibility(View.VISIBLE);
//                }else {
//                    error.setVisibility(View.GONE);
//                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FilterCity(search.getText().toString());

            }
        });



        models = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                all.setBackgroundColor(Color.parseColor("#26FF00"));

                models = new ArrayList<>();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.keepSynced(true);
                ref.child("Jobs").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        models.clear();

                        for(DataSnapshot ds:snapshot.getChildren()){


                                JobModel jobModel = ds.getValue(JobModel.class);
                                models.add(jobModel);

                            if(models.isEmpty()){
                                error.setVisibility(View.VISIBLE);
                            }else {
                                error.setVisibility(View.GONE);
                            }
//                    else {
//                        JobModel jobModel = ds.getValue(JobModel.class);
//                        models.add(jobModel);
//                    }

                        }

                        jobAdapter = new JobAdapter(getActivity(),models);
                        job_rec.setAdapter(jobAdapter);
                        search.setText("");
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowCities();

            }
        });

        ranges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetUserLocation();
                GetJobLocations();
                ShowRangeDilog();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("MemberShip").getValue().equals("None")){
                            Intent intent = new Intent(getActivity(), MembershipActivity.class);
                            intent.putExtra("cat", "Owner");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getActivity(),AddJobActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });



        mAuth = FirebaseAuth.getInstance();


        return view;
    }

    private void ShowCities() {

        final String[] cities = {"Andhra pradesh","Adoni","Amaravati","Anantapur", "Chandragiri","Chittoor","Dowlaiswaram","Eluru","Guntur","Kadapa","Kakinada","Kurnool","Machilipatnam","Nagarjunakoṇḍa","Rajahmundry","Srikakulam","Tirupati","Vijayawada","Visakhapatnam","Vizianagaram","Yemmiganur",
                "Arunachal Pradesh","Itanagar",
                "Assam","Dhuburi","Dibrugarh","Dispur","Guwahati","Jorhat","Nagaon","Sibsagar","Silchar","Tezpur","Tinsukia",
                "Bihar","Ara","Baruni","Begusarai","Bettiah","Bhagalpur","Bihar","Sharif","Bodh","Gaya","Buxar","Chapra","Darbhanga","Dehri","Dinapur","Nizamat","Gaya","Hajipur","Jamalpur","Katihar","Madhubani","Motihari","Munger","Muzaffarpur","Patna","Purnia","Pusa","Saharsa","Samastipur","Sasaram","Sitamarhi","Siwan",
                "Chandigarh(Union Territory)","Chandigarh",
                "Chhattisgarh","Ambikapur","Bhilai","Bilaspur","Dhamtari","Durg","Jagdalpur","Raipur","Rajnandgaon",
                "Dadra and Nagar Haveli (union territory)","Silvassa",
                "Daman and Diu (union territory)","Daman","Dia",
                "Delhi(national capital territory)","Delhi","New Delhi",
                "Goa","Madgaon","Panaji",
                "Gujarat","Ahmadabad","Amreli","Bharuch","Bhavnagar","Bhuj","Dwarka","Gandhinagar","Godhra","Jamnagar","Junagadh","Kandla","Khambhat","Kheda","Mahesana","Morvi","Nadiad","Navsari","Okha","Palanpur","Patan","Porbandar","Rajkot","Surat","Surendranagar","Valsad","Veraval",
                "Haryana","Ambala","Bhiwani","Chandigarh","Faridabad","Firozpur","Jhirka","Gurgaon","Hansi","Hisar","Jind","Kaithal","Karnal","Kurukshetra","Panipat","Pehowa","Rewari","Rohtak","Sirsa","Sonipat",
                "Himachal Pradesh","Bilaspur","Chamba","Dalhousie","Dharmshala","Hamirpur","Kangra","Kullu","Mandi","Nahan","Shimla","Una",
                "Jammu and Kashmir (union territory)","Anantnag","Baramula","Doda","Gulmarg","Jammu","Kathua","Punch","Rajauri","Srinagar","Udhampur",
                "Jharkhand","Bokaro","Chaibasa","Deoghar","Dhanbad","Dumka","Giridih","Hazaribag","Jamshedpur","Jharia","Rajmahal","Ranchi","Saraikela",
                "Karnataka","Badami","Ballari","Bangalore","Belgavi","Bhadravati","Bidar","Chikkamagaluru","Chitradurga","Davangere","Halebid","Hassan","Hubballi-Dharwad","Kalaburagi","Kolar","Madikeri","Mandya","Mangaluru","Mysuru","Raichur","Shivamogga","Shravanabelagola","Shrirangapattana","Tumkuru",
                "Kerala","Alappuzha","Badagara","Idukki","Kannur","Kochi","Kollam","Kottayam","Kozhikode","Mattancheri","Palakkad","Thalassery","Thiruvananthapuram","Thrissur",
                "Ladakh (union territory)","Kargil","Leh",
                "Madhya Pradesh","Balaghat","Barwani","Betul","Bharhut","Bhind","Bhojpur","Bhopal","Burhanpur","Chhatarpur","Chhindwara","Damoh","Datia","Dewas","Dhar","Guna","Gwalior","Hoshangabad","Indore","Itarsi","Jabalpur","Jhabua","Khajuraho","Khandwa","Khargon","Maheshwar","Mandla","Mandsaur","Mhow","Morena","Murwara","Narsimhapur","Narsinghgarh","Narwar","Neemuch","Nowgong","Orchha","Panna","Raisen","Rajgarh","Ratlam","Rewa","Sagar","Sarangpur","Satna","Sehore","Seoni","Shahdol","Shajapur","Sheopur","Shivpuri","Ujjain","Vidisha",
                "Maharashtra","Ahmadnagar","Akola","Amravati","Aurangabad","Bhandara","Bhusawal","Bid","Buldana","Chandrapur","Daulatabad","Dhule","Jalgaon","Kalyan","Karli","Kolhapur","Mahabaleshwar","Malegaon","Matheran","Mumbai","Nagpur","Nanded","Nashik","Osmanabad","Pandharpur","Parbhani","Pune","Ratnagiri","Sangli","Satara","Sevagram","Solapur","Thane","Ulhasnagar","Vasai-Virar","Wardha","Yavatmal",
                "Manipur","Imphal",
                "Meghalaya","Cherrapunji","Shillong",
                "Mizoram","Aizawl","Lunglei",
                "Nagaland","Kohima","Mon","Phek","Wokha","Zunheboto",
                "Odisha","Balangir","Baleshwar","Baripada","Bhubaneshwar","Brahmapur","Cuttack","Dhenkanal","Keonjhar","Konark","Koraput","Paradip","Phulabani","Puri","Sambalpur","Udayagiri",
                "Puducherry (union territory)","Karaikal","Mahe","Puducherry","Yanam",
                "Punjab","Amritsar","Batala","Chandigarh","Faridkot","Firozpur","Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Nabha","Patiala","Rupnagar","Sangrur",
                "Rajasthan","Abu","Ajmer","Alwar","Amer","Barmer","Beawar","Bharatpur","Bhilwara","Bikaner","Bundi","Chittaurgarh","Churu","Dhaulpur","Dungarpur","Ganganagar","Hanumangarh","Jaipur","Jaisalmer","Jalor","Jhalawar","Jhunjhunu","Jodhpur","Kishangarh","Kota","Merta","Nagaur","Nathdwara","Pali","Phalodi","Pushkar","Sawai Madhopur","Shahpura","Sikar","Sirohi","Tonk","Udaipur",
                "Sikkim","Gangtok","Gyalsing","Lachung","Mangan",
                "Tamil Nadu","Arcot","Chengalpattu","Chennai","Chidambaram","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Kanchipuram","Kanniyakumari","Kodaikanal","Kumbakonam","Madurai","Mamallapuram","Nagappattinam","Nagercoil","Palayankottai","Pudukkottai","Rajapalaiyam","Ramanathapuram","Salem","Thanjavur","Tiruchchirappalli","Tirunelveli","Tiruppur","Tuticorin","Udhagamandalam","Vellore",
                "Telangana","Hyderabad","Karimnagar","Khammam","Mahbubnagar","Nizamabad","Sangareddi","Warangal",
                "Tripura","Agartala",
                "Uttar Pradesh","Agra","Aligarh","Amroha","Ayodhya","Azamgarh","Bahraich","Ballia","Banda","Bara Banki","Bareilly","Basti","Bijnor","Bithur","Budaun","Bulandshahr","Deoria","Etah","Etawah","Faizabad","Farrukhabad-cum-Fatehgarh","Fatehpur","Fatehpur Sikri","Ghaziabad","Ghazipur","Gonda","Gorakhpur","Hamirpur","Hardoi","Hathras","Jalaun","Jaunpur","Jhansi","Kannauj","Kanpur","Lakhimpur","Lalitpur","Lucknow","Mainpuri","Mathura","Meerut","Mirzapur-Vindhyachal","Moradabad","Muzaffarnagar","Partapgarh","Pilibhit","Prayagraj","Rae Bareli","Rampur","Saharanpur","Sambhal","Shahjahanpur","Sitapur","Sultanpur","Tehri","Varanasi",
                "Uttarakhand","Almora","Dehra Dun","Haridwar","Mussoorie","Nainital","Pithoragarh",
                "West Bengal","Alipore","Alipur","Duar","Asansol","Baharampur","Bally","Balurghat","Bankura","Baranagar","Barasat","Barrackpore","Basirhat","Bhatpara","Bishnupur","Budge","Budge","Burdwan","Chandernagore","Darjiling","Diamond","Harbour","Dum Dum","Durgapur","Halisahar","Haora","Hugli","Ingraj Bazar","Jalpaiguri","Kalimpong","Kamarhati","Kanchrapara","Kharagpur","Koch Bihar","Kolkata","Krishnanagar","Malda","Midnapore","Murshidabad","Navadwip","Palashi","Panihati","Purulia","Raiganj","Santipur","Shantiniketan","Shrirampur","Siliguri","Siuri","Tamluk","Titagarh",
        };

        final int checkItem = 0;

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Select Place")
                .setSingleChoiceItems(cities, checkItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        search.setText(cities[i]);

//                        ranges = Rang[i];
                        FilterCity(cities[i]);

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                dialogInterface.dismiss();
            }
        });
        dialogBuilder.create().show();


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
//        hashMap.put("title","Searched for Job" + "At Location " + city);

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Activities").child(String.valueOf(times))
//                .setValue(hashMap);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Jobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();

                for(DataSnapshot ds:snapshot.getChildren()){


//                    uid = ""+ds.child("uid").getValue();

//                    if(!ds.exists()){
//                    }else {
//                    }

                    if((""+ds.child("Location").getValue()).equals(city)) {

                        JobModel jobModel = ds.getValue(JobModel.class);
                        models.add(jobModel);

                    }

                    if(models.isEmpty()){
                        error.setVisibility(View.VISIBLE);
                    }else {
                        error.setVisibility(View.GONE);
                    }


                }
                jobAdapter = new JobAdapter(getActivity(),models);
                job_rec.setAdapter(jobAdapter);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError e) {

//                error.setVisibility(View.VISIBLE);


            }
        });



    }

    int i;
    private void GetJobLocations() {
        final String[] ids = new String[i++];
        i++;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Jobs").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for(DataSnapshot ds: snapshot.getChildren()){

                    Job_Location = ""+ds.child("Location").getValue();
                    ids[i] = ""+ds.child("uid").getValue();

                    Toast.makeText(getActivity(), ids[i], Toast.LENGTH_SHORT).show();

//                    DatabaseReference user = FirebaseDatabase.getInstance().getReference();
//                    user.child("Owner").child(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            String latitude = ""+snapshot.child("lat").getValue();
//                            String longitude = ""+snapshot.child("lon").getValue();
//
//                            lat2 = Double.parseDouble(latitude);
//                            lon2 = Double.parseDouble(longitude);
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//


//                    Toast.makeText(getActivity(), Job_Location, Toast.LENGTH_SHORT).show();
//                    FindLatLon(Job_Location);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void FindLatLon(String job_location) {

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.getAddress("Delhi",getActivity(),new GeoHandler());

    }


    private void GetUserLocation() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Locate = ""+snapshot.child("Location").getValue();

                String latitude = ""+snapshot.child("lat").getValue();
                String longitude = ""+snapshot.child("lon").getValue();

                lat1 = Double.parseDouble(latitude);
                lon1 = Double.parseDouble(longitude);



//                search.setText(Locate);
//                Toast.makeText(getActivity(), Locate, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void ShowRangeDilog() {

        final String[] Rang = {"50","100","200","300","400","500","600","700","800","900","1000",
        "1100","1200","1300","1400","1500","1600","1700","1800","1900","2000"};

        final int checkItem = 0;

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Select Range in Km")
                .setSingleChoiceItems(Rang, checkItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ranges.setText(Rang[i]);

//                        ranges = Rang[i];

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                FilterAdapter();

                dialogInterface.dismiss();
            }
        });
        dialogBuilder.create().show();

    }

    private void FilterAdapter() {

        if(search.getText().toString().equals("")){
            SearchFromUserLocation();
        }else {
            SearchFromLocation();
        }

    }

    private void SearchFromLocation() {

        Toast.makeText(getActivity(), search.getText().toString(), Toast.LENGTH_SHORT).show();


    }

    private void SearchFromUserLocation() {
        distance(lat1,lon1,lat2,lon2);
    }


    private void distance(double lat1, double lon1, double lat2, double lon2) {

        double longDiff = lon1 - lon2;

        distance = Math.sin(deg2red(lat1))
                *Math.sin(deg2red(lat2))
                +Math.cos(deg2red(lat1))
                *Math.cos(deg2red(lat2))
                *Math.cos(deg2red(longDiff));
        distance = Math.acos(distance);
        // Convert distance radian to degree
        distance = red2deg(distance);
        // Distance in miles
        distance = distance * 60*1.1515;
        // Distance in km
        distance = distance * 1.609344;

        LoadDataFromLocation(distance);


    }

    private double red2deg(double distance) {
        return (distance*180.0/Math.PI);
    }

    private double deg2red(double lat1) {
        return (lat1*Math.PI/180.0);
    }


    private void LoadDataFromLocation(double distance){

        Toast.makeText(getActivity(), String.valueOf(distance), Toast.LENGTH_SHORT).show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Jobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();

                for(DataSnapshot ds:snapshot.getChildren()){

//                    uid = ""+ds.child("uid").getValue();

//                    if(!ds.exists()){
//                    }else {
//                    }
                    int i;
                    i = Integer.parseInt(ranges.getText().toString());

                    if(i >= distance) {

                        JobModel jobModel = ds.getValue(JobModel.class);
                        models.add(jobModel);

                    }

                    if(models.isEmpty()){
                        error.setVisibility(View.VISIBLE);
                    }else {
                        error.setVisibility(View.GONE);
                    }


                }
                jobAdapter = new JobAdapter(getActivity(),models);
                job_rec.setAdapter(jobAdapter);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError e) {

//                error.setVisibility(View.VISIBLE);


            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

//        LanguageChoose();

//        if(ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            getGoLocation();
//        }else {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//        }


//        if(search.getText().toString().isEmpty()){
//            Toast.makeText(getActivity(), "Please select the place you want to search Jobs", Toast.LENGTH_SHORT).show();
//        }else {
//            FilterCity(search.getText().toString());
//        }

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        ref2.keepSynced(true);
        ref2.child("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String ID = mAuth.getCurrentUser().getUid();

                Locate = ""+snapshot.child("Location").getValue();



                if(("" + snapshot.child("Cat").getValue()).equals("Owner")){
                    fab.setVisibility(View.VISIBLE);
                }
                else {
                    fab.setVisibility(View.INVISIBLE);
                }
//                if(("" + snapshot.child("Cat").getValue()).equals("Drivers")){
//                    range = Double.parseDouble(""+snapshot.child("range").getValue());
//                }else {
//                    range = 100;
//                }

//                String latitude = ""+snapshot.child("lat").getValue();
//                String longitude = ""+snapshot.child("lon").getValue();
//
//                lat1 = Double.parseDouble(latitude);
//                lon1 = Double.parseDouble(longitude);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
//        ref1.keepSynced(true);
//        ref1.child("Jobs").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()){
//                    uid = ""+ds.child("uid").getValue();
//
//                    DatabaseReference user = FirebaseDatabase.getInstance().getReference();
//                    user.child("Owner").child(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            String latitude = ""+snapshot.child("lat").getValue();
//                            String longitude = ""+snapshot.child("lon").getValue();
//
//                            lat2 = Double.parseDouble(latitude);
//                            lon2 = Double.parseDouble(longitude);
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        distance(lat1,lon1,lat2,lon2);
        loadJobs();
    }



    private void loadJobs() {



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);
        ref.child("Jobs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();

                for(DataSnapshot ds:snapshot.getChildren()){


//                    uid = ""+ds.child("uid").getValue();

//                    if(!ds.exists()){
//                    }else {
//                    }

                    if((""+ds.child("Location").getValue()).equals(Locate)) {

                        JobModel jobModel = ds.getValue(JobModel.class);
                        models.add(jobModel);

                    }

                    if(models.isEmpty()){
                        error.setVisibility(View.VISIBLE);
                    }else {
                        error.setVisibility(View.GONE);
                    }


                }
                jobAdapter = new JobAdapter(getActivity(),models);
                job_rec.setAdapter(jobAdapter);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError e) {

//                error.setVisibility(View.VISIBLE);


            }
        });

    }



//    private void getGoLocation() {
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            // ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            // int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
////        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
////            @Override
////            public void onComplete(@NonNull @NotNull Task<Location> task) {
////
////                Location location = task.getResult();
////                if(location != null){
////
////                    try {
////                        Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
////                        addresses = geocoder.getFromLocation(
////                                location.getLatitude(), location.getLongitude(),1
////                        );
//////                        shopcountryedt.setText(addresses.get(0).getCountryName());
//////                        shopstateedt.setText(addresses.get(0).getAdminArea());
//////                        shopcityedt.setText(addresses.get(0).getLocality());
//////                        shopaddresstotaledt.setText(addresses.get(0).getAddressLine(0));
////
////                        address.setText(addresses.get(0).getLocality() + "," +  addresses.get(0).getAdminArea() + "," +  addresses.get(0).getCountryName() );
////
////                        Daddress.setText(addresses.get(0).getLocality() + "," +  addresses.get(0).getAdminArea() + "," +  addresses.get(0).getCountryName() );
////
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        });
//
//
//        LocationManager manager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
//
//        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,5, (LocationListener) getActivity());
//
//        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        if(location != null){
//            double lat = location.getLatitude();
//            double lon = location.getLongitude();
//
//            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//
//            try {
//                addressList  =geocoder.getFromLocation(lat, lon, 1);
//                Log.d("location", String.valueOf(lat));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    @Override
    public boolean onBackPressed() {

            return true;

    }


    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String address;
            String address1;
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    address1 = bundle.getString("addresses");

                    break;
                default:
                    address = null;
                    address1 = null;
            }

//            lat2 = Double.parseDouble(address);
//            lon2 = Double.parseDouble(address1);

            Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();
        }
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