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

public class view_msg_list extends AppCompatActivity {

    ListView msgListView;
    DatabaseReference dRef;
    FireApp fireApp;
    ArrayAdapter adapter;
    String username;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date todays_date = new Date();
    String event_id;
    String today_date;
    String msgBuild;
    String event_id_string;
    int j = 0;
    ArrayList<String> msgListSelected = new ArrayList<>();
    ArrayList<String> friend_nameList = new ArrayList<>();
    ArrayList<String> msgList = new ArrayList<>();
    ArrayList<String> sent_atList = new ArrayList<>();
    ArrayList<String> messageBody = new ArrayList<>();
    ArrayList<String> eventShortListed = new ArrayList<>();
    ArrayList<ArrayList<String>> event_collective_list = new ArrayList<>();
    ArrayList<String> event_indi_details = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_msg_list);


        fireApp = (FireApp) getApplicationContext();

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("View Messages");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        username = fireApp.getUsername();
        msgListView = (ListView) findViewById(R.id.view_msg_list_view);
        todays_date = new Date();
        today_date = dateFormat.format(todays_date);
        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username);
        dRef.child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot child1 : child.getChildren()) {

                        System.out.println("dataSnapshot key value to be noted:" + child1.getKey());


                        if (child1.getKey().toString().equals("event_date")) {
                            try {
                                System.out.println("dataSnapshot child value to be noted:" + child1.getValue().toString());
                                Date event_date = dateFormat.parse(child1.getValue().toString());
                                System.out.println("event_date:" + event_date);
                                System.out.println("todays_date:" + todays_date);
                                if (todays_date.compareTo(event_date) < 0) {
                                    System.out.println("event is before current date");
                                    if (!msgListSelected.contains(child.getKey()))
                                        msgListSelected.add(child.getKey());
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                System.out.println("msgListSelected:" + msgListSelected);

                for (int i = 0; i < msgListSelected.size(); i++) {
                    dRef.child("Messages").child(msgListSelected.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            msgBuild = "";
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                System.out.println("key:" + child.getKey());
                                System.out.println("value:" + child.getValue());
                                if (child.getKey().equals("Friend_username")) {
                                    friend_nameList.add(child.getValue().toString());
                                    msgBuild = "Message From: " + child.getValue().toString() + "\n";
                                } else if (child.getKey().equals("msg")) {
                                    msgList.add(child.getValue().toString());
                                    msgBuild = msgBuild + "Message: " + child.getValue().toString() + "\n";
                                } else if (child.getKey().equals("sent_at")) {
                                    sent_atList.add(child.getValue().toString());
                                    msgBuild = msgBuild + "Sent At: " + child.getValue().toString() + "\n";
                                    messageBody.add(msgBuild);
                                    j++;
                                }


                            }
                            if (j == friend_nameList.size()) {
                                System.out.println("messageBody:" + messageBody);
                                adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, messageBody);
                                msgListView.setAdapter(adapter);
                                msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        event_id_string = (String) parent.getItemAtPosition(position);
//                                            int index =  event_id_string.lastIndexOf(" ");
//                                            event_id_string = event_id_string.substring(index+1);

                                        int index = event_id_string.indexOf("Event ID");
                                        event_id = event_id_string.substring(index, event_id_string.length() - 1);
                                        System.out.println("event_id_stringcheck:" + event_id);
                                        index = event_id.indexOf(":");
                                        int index2 = event_id.indexOf("\n");
                                        event_id = "event" + event_id.substring(index + 2,index2);

                                        eventShortListed.add(event_id);
                                        System.out.println("event_id_string:" + event_id);
                                        fireApp.setEventShortListedForYou(eventShortListed);

                                        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/" + eventShortListed.get(0));
                                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                eventShortListed = new ArrayList<String>();
                                                event_indi_details = new ArrayList<String>();
                                                event_collective_list = new ArrayList<ArrayList<String>>();
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
                                                fireApp.setEvent_collective_list_for_you(event_collective_list);
                                                Intent intent = new Intent(getBaseContext(), event_list_you_list.class);
                                                startActivity(intent);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                    }

                                });


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        msgListView = (ListView) findViewById(R.id.view_msg_list_view);


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
