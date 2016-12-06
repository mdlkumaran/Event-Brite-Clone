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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class messagetofriendeventlist extends AppCompatActivity {
    ListView events_join_me_list_view;
    ArrayList<String> messageBody = new ArrayList<>();
    String msgBuild;
    ArrayList<String> eventDateList = new ArrayList<>();
    ArrayList<String> eventLocationList = new ArrayList<>();
    ArrayList<String> eventNameList = new ArrayList<>();
    ArrayList<String> eventTimeList = new ArrayList<>();
    ArrayList<String> eventVenueList = new ArrayList<>();
    ArrayList<String> eventIDList = new ArrayList<>();

    ArrayList<String> eventListSelected = new ArrayList<>();
    ArrayAdapter adapter;
    String timeStamp;
    String event_date;

    String event_id_string;
    String friendEmail;
    DatabaseReference dRef;
    int i=0;
    int j=0;
    String initial = "Ask your friend to join this event with you??";
    String friendInitial = " Your friend has asked you to join this event." + "\n" + "Click here to view & join this event!!" + "\n";
    FireApp fireApp;
    String username;
    String usernameFriend;
    DateFormat dateFormat;
    String today_date;
    Date todays_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagetofriendeventlist);
        events_join_me_list_view = (ListView) findViewById(R.id.friends_event_join_ticket_list_view);
        fireApp = (FireApp) getApplicationContext();

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Send Message");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        username = fireApp.getUsername();
        usernameFriend = fireApp.getUsername();
        friendEmail = getIntent().getStringExtra("friendName");
        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todays_date = new Date();
        today_date = dateFormat.format(todays_date);

        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username + "/tickets");
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                j =0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot child1 : child.getChildren()) {

                        System.out.println("dataSnapshot key value to be noted:" + child1.getKey());


                        if (child1.getKey().toString().equals("e_date")) {
                            try {
                                System.out.println("dataSnapshot child value to be noted:" + child1.getValue().toString());
                                Date event_date = dateFormat.parse(child1.getValue().toString());
                                System.out.println("event_date:" + event_date);
                                System.out.println("todays_date:" + todays_date);
                                if (todays_date.compareTo(event_date) < 0) {
                                    System.out.println("event is before current date");
                                    if (!eventListSelected.contains(child.getKey()))
                                        eventListSelected.add(child.getKey());
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                System.out.println("eventListSelected:" + eventListSelected);

                for (i = 0; i < eventListSelected.size(); i++) {
                    dRef.child(eventListSelected.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            msgBuild = initial + "\n";
                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                if (child.getKey().equals("e_name")) {
                                    eventNameList.add(child.getValue().toString());

                                    msgBuild = msgBuild + "Event Name: " + child.getValue().toString() + "\n";
                                } else if (child.getKey().equals("e_location")) {
                                    eventLocationList.add(child.getValue().toString());
                                    msgBuild = msgBuild + "Event Location: " + child.getValue().toString() + "\n";
                                } else if (child.getKey().equals("e_date")) {
                                    eventDateList.add(child.getValue().toString());
                                    msgBuild = msgBuild + "Event Date: " + child.getValue().toString() + "\n";
                                } else if (child.getKey().equals("e_time")) {
                                    eventTimeList.add(child.getValue().toString());
                                    msgBuild = msgBuild + "Event Time: " + child.getValue().toString() + "\n";
                                } else if (child.getKey().equals("e_venue")) {
                                    eventVenueList.add(child.getValue().toString());
                                    msgBuild = msgBuild + "Event Venue: " + child.getValue().toString() + "\n";
                                } else if (child.getKey().equals("eventID")) {
                                    eventIDList.add(child.getValue().toString());
                                    msgBuild = msgBuild + "Event ID: " + child.getValue().toString() + "\n";
                                    messageBody.add(msgBuild);

                                    j++;
                                }

                                if (j == eventListSelected.size()) {
                                    System.out.println("messageBody:" + messageBody);
                                    adapter =new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1,messageBody);
                                    events_join_me_list_view.setAdapter(adapter);
                                    events_join_me_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            event_id_string = (String)parent.getItemAtPosition(position);
                                            Format formatter = new SimpleDateFormat("MM/dd/yy hh:mm a");
                                            timeStamp = formatter.format(new Date());
//                                            int index =  event_id_string.lastIndexOf(" ");
//                                            event_id_string = event_id_string.substring(index+1);
                                            System.out.println("event_id_string:"+event_id_string);
                                            int index = event_id_string.indexOf("\n");
                                            System.out.println("index:"+index);
                                            event_id_string = event_id_string.substring(index+1);
                                            event_id_string = friendInitial + event_id_string;
                                            int index_2 = event_id_string.indexOf("\n", event_id_string.indexOf("\n") + 1);
                                            System.out.println("index_2:"+index_2);
                                            index_2 = event_id_string.indexOf("\n", index_2+1);
                                            event_date = event_id_string.substring(index+1,index_2);
                                            System.out.println("event_date:"+event_date);
                                            index = event_date.indexOf(":")+1;
                                            event_date = event_date.substring(index+1);
                                            index = friendEmail.indexOf("@");
                                            username = friendEmail.substring(0,index);

                                            dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username );
                                            String key = dRef.child("Messages").push().getKey();
                                            dRef.child("Messages").child(key).child("Friend_username").setValue(usernameFriend);
                                            dRef.child("Messages").child(key).child("sent_at").setValue(timeStamp);
                                            dRef.child("Messages").child(key).child("msg").setValue(event_id_string);
                                            dRef.child("Messages").child(key).child("event_date").setValue(event_date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(), "Message Sent Successfully", Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                        }
                                    });
                                }


                            }
                            System.out.println("eventNameList.get(i):" + eventNameList);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            }

            );
//
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
