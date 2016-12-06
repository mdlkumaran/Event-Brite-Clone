package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class customsearch_eventsList extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    String category_name;
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
        setContentView(R.layout.activity_customsearch_events_list);
        radioGroup = (RadioGroup)findViewById(R.id.category_customRadioGroup);

    }

    public  void radioButtonClick(View v) {
        System.out.println("trigger");
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        fireApp = (FireApp) getApplicationContext();

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Custom Search(Free or Paid)");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        radioButton = (RadioButton) (findViewById(radioButtonId));
        category_name = radioButton.getText().toString();
        Toast.makeText(getBaseContext(), category_name, Toast.LENGTH_LONG).show();
        if (category_name.equals("Free Events")) {
            System.out.println("Checking to be done");
            event_indi_details = new ArrayList<String>();
            event_collective_list = new ArrayList<ArrayList<String>>();
            eventShortListed = new ArrayList<String>();

            //                Toast.makeText(getBaseContext(),usStates,Toast.LENGTH_LONG).show();

            location = fireApp.getGLocation();
            categorySelected = fireApp.getgPInterestList();

            dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event");
            System.out.println("categorySelected.size():" + categorySelected.size());

            dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    eventShortListed = new ArrayList<String>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        System.out.println("child key"+child.getKey());
                        System.out.println("child key"+child.getValue());
                        for (int i = 0; i < categorySelected.size(); i++) {
                            if (child.getValue().toString().contains("e_location=" + location) && child.getValue().toString().contains("e_category=" + categorySelected.get(i)) && child.getValue().toString().contains("e_price=Free")){
                                if(!eventShortListed.contains(child.getKey()))
                                eventShortListed.add(child.getKey());
                                System.out.println(eventShortListed);
                            }
                        }
                    }
                    fireApp.setEventShortListedForYou(eventShortListed);
                    event_collective_list = new ArrayList<ArrayList<String>>();
                    for (int i = 0; i < fireApp.getEventShortListedForYou().size(); i++) {
                        event_indi_details = new ArrayList<String>();
                        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/" + eventShortListed.get(i));
                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                event_indi_details = new ArrayList<String>();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.getKey().toString().equals("e_image")) {
                                        event_indi_details.add(child.getValue().toString());
                                    }
                                    else if (child.getKey().toString().equals("e_date")) {
                                        event_indi_details.add("Event Date:" + child.getValue().toString());

                                    } else if (child.getKey().toString().equals("e_name")) {
                                        event_indi_details.add("Event Title:" + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("eventID")) {
                                        event_indi_details.add("Event ID:" + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_location")) {
                                        event_indi_details.add("Event Location: " + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_category")) {
                                        event_indi_details.add("Event Category: " + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_ticket_available")) {
                                        event_indi_details.add("Event Ticket Available: " + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_price")) {
                                        event_indi_details.add("Event Price: " + child.getValue().toString());
                                    }
                                }
                                System.out.println("event_indi_list()" + event_indi_details);
                                event_collective_list.add(event_indi_details);
                                System.out.println("event_collective_list()" + event_collective_list);
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
            Intent intent;
            intent = new Intent(v.getContext(), event_list_you_list.class);
            v.getContext().startActivity(intent);
        }
        else if (category_name.equals("Paid Events")) {
            System.out.println("Checking to be done");
            event_indi_details = new ArrayList<String>();
            event_collective_list = new ArrayList<ArrayList<String>>();
            eventShortListed = new ArrayList<String>();

            //                Toast.makeText(getBaseContext(),usStates,Toast.LENGTH_LONG).show();

            location = fireApp.getGLocation();
            categorySelected = fireApp.getgPInterestList();

            dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event");
            System.out.println("categorySelected.size():" + categorySelected.size());

            dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    eventShortListed = new ArrayList<String>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        for (int i = 0; i < categorySelected.size(); i++) {
                            if (child.getValue().toString().contains("e_location=" + location) && child.getValue().toString().contains("e_category=" + categorySelected.get(i)) && !child.getValue().toString().contains("e_price=Free")) {
                                if(!eventShortListed.contains(child.getKey()))
                                eventShortListed.add(child.getKey());
                                System.out.println(eventShortListed);
                            }
                        }
                    }
                    fireApp.setEventShortListedForYou(eventShortListed);
                    event_collective_list = new ArrayList<ArrayList<String>>();
                    for (int i = 0; i < fireApp.getEventShortListedForYou().size(); i++) {
                        event_indi_details = new ArrayList<String>();
                        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/" + eventShortListed.get(i));
                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                event_indi_details = new ArrayList<String>();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.getKey().toString().equals("e_image")) {
                                        event_indi_details.add(child.getValue().toString());
                                    }
                                    else if (child.getKey().toString().equals("e_date")) {
                                        event_indi_details.add("Event Date:" + child.getValue().toString());

                                    } else if (child.getKey().toString().equals("e_name")) {
                                        event_indi_details.add("Event Title:" + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("eventID")) {
                                        event_indi_details.add("Event ID:" + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_location")) {
                                        event_indi_details.add("Event Location: " + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_category")) {
                                        event_indi_details.add("Event Category: " + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_ticket_available")) {
                                        event_indi_details.add("Event Ticket Available: " + child.getValue().toString());
                                    } else if (child.getKey().toString().equals("e_price")) {
                                        event_indi_details.add("Event Price: " + child.getValue().toString());
                                    }
                                }
                                System.out.println("event_indi_list()" + event_indi_details);
                                event_collective_list.add(event_indi_details);
                                System.out.println("event_collective_list()" + event_collective_list);
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
            Intent intent;
            intent = new Intent(v.getContext(), event_list_you_list.class);
            v.getContext().startActivity(intent);
        }
    }

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
