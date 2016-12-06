package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class location_selection extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    DatabaseReference dRef;
    FireApp fireApp;
    String location;
    ArrayList<String> categorySelected = new ArrayList<>();
    ArrayList<String> eventShortListed = new ArrayList<>();
    ArrayList<String> event_indi_details = new ArrayList<>();
    ArrayList<ArrayList<String>> event_collective_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        fireApp = (FireApp)getApplicationContext();
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Enter your location");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));
        final TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        ListView lv = (ListView)findViewById(R.id.listViewUSStates);

        ArrayList<String> aLUSStates = new ArrayList<>();
        aLUSStates.addAll(Arrays.asList(getResources().getStringArray(R.array.usStatesArr)));
        adapter = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1,aLUSStates);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("Checking to be done");
                        event_indi_details = new ArrayList<String>();
                        event_collective_list = new ArrayList<ArrayList<String>>();
                        eventShortListed = new ArrayList<String>();
                        String usStateSelected =String.valueOf(parent.getItemAtPosition(position));

                        //                Toast.makeText(getBaseContext(),usStates,Toast.LENGTH_LONG).show();
                        fireApp.setGLocation(usStateSelected);

                        location = fireApp.getGLocation();
                        categorySelected = fireApp.getgPInterestList();

                        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event");
                        System.out.println("categorySelected.size():"+categorySelected.size());

                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    for(int i=0;i<categorySelected.size();i++) {
                                        if (child.getValue().toString().contains("e_location=" + location) && child.getValue().toString().contains("e_category=" + categorySelected.get(i))) {
                                            if(!eventShortListed.contains(child.getKey()))
                                            eventShortListed.add(child.getKey());
                                            System.out.println(eventShortListed);
                                        }
                                    }
                                }

                                fireApp.setEventShortListedForYou(eventShortListed);
                                event_collective_list = new ArrayList<ArrayList<String>>();
                                for(int i=0; i<fireApp.getEventShortListedForYou().size();i++)
                                {
                                    event_indi_details = new ArrayList<String>();
                                    dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/"+eventShortListed.get(i));
                                    dRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            event_indi_details = new ArrayList<String>();
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                if (child.getKey().toString().equals("e_image")) {
                                                    event_indi_details.add(child.getValue().toString());
                                                }
                                                else if (child.getKey().toString().equals("e_date")) {
                                                    event_indi_details.add("Event Date:"+child.getValue().toString());

                                                }
                                                else if(child.getKey().toString().equals("e_name")){
                                                    event_indi_details.add("Event Title:"+child.getValue().toString());
                                                }
                                                else if(child.getKey().toString().equals("eventID")){
                                                    event_indi_details.add("Event ID:" +child.getValue().toString());
                                                }
                                                else if(child.getKey().toString().equals("e_location")){
                                                    event_indi_details.add("Event Location: "+child.getValue().toString());
                                                }
                                                else if(child.getKey().toString().equals("e_category")){
                                                    event_indi_details.add("Event Category: "+child.getValue().toString());
                                                }
                                                else if(child.getKey().toString().equals("e_ticket_available")){
                                                    event_indi_details.add("Event Ticket Available: "+child.getValue().toString());
                                                }
                                                else if(child.getKey().toString().equals("e_price")){
                                                    event_indi_details.add("Event Price: "+child.getValue().toString());
                                                }
                                            }
                                            System.out.println("event_indi_list()"+event_indi_details);
                                                event_collective_list.add(event_indi_details);
                                            System.out.println("event_collective_list()"+event_collective_list);
                                            }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                                fireApp.setEvent_collective_list_for_you(event_collective_list);
                           }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        System.out.println("eventShortListed:"+eventShortListed);



                                Intent intent = new Intent(getBaseContext(), EventRecyclerView.class);
                        intent.putExtra("usStatesSelected",usStateSelected);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
        );
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_place_search,menu);
        MenuItem item =menu.findItem(R.id.menu_place_search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override

            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
