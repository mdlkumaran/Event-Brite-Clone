package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class rate_review_event extends AppCompatActivity {

    String event_id;
    double ratingCount;
    double ratingSum;
    double ratingAvg;
    TextView rating_label;
    String username;
    String comments;
    ListView commentsList;
    ArrayList<String> reviews = new ArrayList<>();
    DatabaseReference dRef;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_review_event);

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("View Events Rating & Review");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));
        commentsList = (ListView)findViewById(R.id.review_event_list_view);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_event);
        rating_label = (TextView) findViewById(R.id.rating_event_label);
        event_id = getIntent().getStringExtra("event_id");

        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/event" + event_id);
        dRef.child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingCount = dataSnapshot.getChildrenCount();
                rating_label.setText("Rating" + "(" + (int) ratingCount + ")");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ratingSum = ratingSum + Double.valueOf(child.getValue().toString());
                }
                ratingAvg = ratingSum / ratingCount;
                System.out.println("ratingAvg:" + ratingAvg);

                ratingBar.setRating((float) ratingAvg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dRef.child("Review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    username = child.getKey().toString();
                    comments = child.getValue().toString();
                    reviews.add("Username: "+ username + "\n" + "Comment: "+comments);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,reviews);
        commentsList.setAdapter(adapter);
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

