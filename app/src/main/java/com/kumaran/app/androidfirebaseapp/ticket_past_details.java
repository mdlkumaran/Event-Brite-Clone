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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ticket_past_details extends AppCompatActivity {

    String ticket_name;
    DatabaseReference dRef;
    private String username;
    Button btn_event_takepicture;
    Button review_post_button;
    FireApp fireApp;
    String event_id;
    Button rate_review_button;
    EditText review;
    private ListView tic_past_ind_list;
    private RatingBar ratingBar;
    int i=0;
    ArrayList<String> combEventDetailNames = new ArrayList<String>(Arrays.asList("Ticket Purchased: ", "Ticket Purchased Date: ", "Event Date: ", "Event Location: ", "Event Name: ","Event Time: ", "Event Venue: ","Event ID: "));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_past_details);

        fireApp = (FireApp) getApplicationContext();

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Tickets for Past Event");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        rate_review_button = (Button) findViewById(R.id.rate_review_button);
        username = fireApp.getUsername();
        tic_past_ind_list = (ListView) findViewById(R.id.ticket_past_indi_list);
        ticket_name = getIntent().getStringExtra("value");
        review = (EditText)findViewById(R.id.review_label_text);
        btn_event_takepicture = (Button) findViewById(R.id.camera_button);
        review_post_button = (Button)findViewById(R.id.submit_review_button);
        ratingBar=(RatingBar) findViewById(R.id.ratingBar_event);
        System.out.println(ticket_name);

        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username + "/tickets/" + ticket_name);

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println(child.getKey());
                    if (child.getKey().equals("eventID")) {
                        System.out.println("going inside");
                        event_id = ((String) child.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/event"+event_id);
                dRef.child("Rating").child(username).setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getBaseContext(),"Event Rated successfully!!Thanks!!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                this,
                String.class, android.R.layout.simple_list_item_1,
                dRef
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                if(i==8)
                {
                    i=0;
                }
                System.out.println("combEventDetailNames:"+combEventDetailNames.get(i));
                model = combEventDetailNames.get(i) + model;
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
                i++;
            }
        };

        tic_past_ind_list.setAdapter(firebaseListAdapter);


        rate_review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), rate_review_event.class);
                intent.putExtra("event_id", event_id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }

        });

        review_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/event"+event_id);
                dRef.child("Review").child(username).setValue(review.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        review.setText("");
                        Toast.makeText(getBaseContext(),"Review posted successfully!!Thanks!!",Toast.LENGTH_LONG).show();
                    }
                });


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
