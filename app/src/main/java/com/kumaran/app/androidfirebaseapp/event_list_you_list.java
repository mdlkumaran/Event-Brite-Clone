package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class event_list_you_list extends AppCompatActivity {
    FireApp fireApp;
    String location;
    String event_register_id;
    private ListView lvEventList;
    String event_address;
    String event_name;
    private event_single_adapter adapter;
    ArrayList<String> categorySelected = new ArrayList<>();
    ArrayList<String> eventShortListed = new ArrayList<>();
    private List<event_single_for_you_product> mEventList;
    DatabaseReference dRef;
    ArrayList<String> event_list_interm = new ArrayList<>();
    ArrayList<ArrayList<String>> event_collective_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_you_list);

        fireApp = (FireApp) getApplicationContext();
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Event List For You");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));


        location = fireApp.getGLocation();
        categorySelected = fireApp.getgPInterestList();
        lvEventList = (ListView) findViewById(R.id.event_list_you_list_view);

        event_collective_list = fireApp.getEvent_collective_list_for_you();
        System.out.println("event_collective_list:"+event_collective_list);

        mEventList = new ArrayList<>();





        for(int i=0; i<event_collective_list.size();i++)
        {
 //           System.out.println(event_collective_list.get(i));
            event_list_interm = event_collective_list.get(i);


            System.out.println(event_list_interm);
                        System.out.println(event_list_interm.get(0)+ event_list_interm.get(1) + event_list_interm.get(2)+ event_list_interm.get(3)+ event_list_interm.get(4)+event_list_interm.get(5)+event_list_interm.get(6));


                    mEventList.add(new event_single_for_you_product(event_list_interm.get(1), event_list_interm.get(4) , event_list_interm.get(7), event_list_interm.get(3), event_list_interm.get(0),event_list_interm.get(6),event_list_interm.get(5),event_list_interm.get(2)));
        }
//        mEventList.add(new event_single_for_you_product("1", "2", "3", "4", "5","11","13"));
//        mEventList.add(new event_single_for_you_product("6", "7", "8", "9", "10","12","14"));

        adapter = new event_single_adapter(getApplicationContext(), mEventList);
        lvEventList.setAdapter(adapter);
        lvEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "please come:" + view.getTag(), Toast.LENGTH_LONG).show();
                Intent intent;
                intent = new Intent(view.getContext(), event_description.class);

                event_register_id = view.getTag().toString();
                event_register_id = event_register_id.replaceAll("[a-zA-Z ]","").replace(":","");
                event_register_id = "event"+event_register_id;
                System.out.println("event_register_id: "+event_register_id);
                dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/"+event_register_id);
                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            if(child.getKey().toString().equals("e_address"))
                            {
                                event_address = child.getValue().toString();
                                System.out.println("event_addresslist: "+event_address);
                                String latLng = "";
                                Geocoder coder = new Geocoder(event_list_you_list.this);
                                List<Address> address;

                                try {
                                    address = coder.getFromLocationName(event_address, 5);
                                    if (address == null) {
                                        latLng = "";
                                    }
                                    if (address != null && !address.isEmpty()) {
                                        Address location = address.get(0);
                                        double lat = location.getLatitude();
                                        double lng = location.getLongitude();

                                        latLng = String.valueOf(lat) + ":" + String.valueOf(lng);
                                        System.out.println("latLng: "+latLng );
                                        System.out.println("event_register_id: "+event_register_id);
                                        fireApp.setEventFragment(latLng+":"+event_register_id+":"+event_address);
                                    }
                                } catch (Exception ex) {

                                    ex.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
                );
                fireApp.setEventFragment("event"+event_register_id);
                intent.putExtra("event_ID register",event_register_id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        menu.add(0, 0, 0, "Change Location");
        menu.add(0, 1, 0, "Personalize");
        menu.add(0, 2, 0, "DashBoard");
        menu.add(0, 3, 0, "User Profile");
        menu.add(0, 4, 0, "LogOut");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(getBaseContext(), location_selection.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 1:
                startActivity(new Intent(getBaseContext(), personalize_interest.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 2:
                startActivity(new Intent(getBaseContext(), EventRecyclerView.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 3:
                startActivity(new Intent(getBaseContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 4:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
