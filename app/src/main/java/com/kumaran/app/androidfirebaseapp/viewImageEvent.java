package com.kumaran.app.androidfirebaseapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class viewImageEvent extends AppCompatActivity {

    private ArrayList<EventImageListModel> eventsImageListdata = new ArrayList<>();
    ArrayList<String> eventImageListString;
    DatabaseReference dRef;
    FireApp fireApp;
    String event_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_event);

        fireApp = (FireApp) getApplicationContext();
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("View Images of the Event");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        event_id = fireApp.getEventIDForImage();
        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event_photos/" + event_id);

        ListView eventImageLv = (ListView)findViewById(R.id.imageEventListView);
        generateEventsImageListData();
        eventImageLv.setAdapter(new EventImageListAdapter(this,eventsImageListdata));

    }

    private void generateEventsImageListData() {
        eventsImageListdata = new ArrayList<>();
        eventImageListString = new ArrayList<>();

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    eventImageListString.add(child.getValue().toString());
                }
                System.out.println("eventImageListdata:"+eventImageListString);
                for(int i=0; i<eventImageListString.size();i++)
                    eventsImageListdata.add(new EventImageListModel(eventImageListString.get(i)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class EventImageListModel{
        private String eventImageString;

        public EventImageListModel(String eventImageString) {
            this.eventImageString = eventImageString;
        }

        public String getEventImageString() {
            return eventImageString;
        }

        public void setEventImageString(String eventImageString) {
            this.eventImageString = eventImageString;
        }
    }

    private class EventImageListAdapter extends ArrayAdapter<EventImageListModel> {
        public EventImageListAdapter(Context context, ArrayList<EventImageListModel> eventImageLists) {
            super(context, R.layout.vieweventimageunit, eventImageLists);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EventImageListModel eventImageListsView = getItem(position);
            ViewHolderForEventImageList viewHolder;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.vieweventimageunit,parent,false);
                viewHolder = new ViewHolderForEventImageList();
                viewHolder.viewImage = (ImageView) convertView.findViewById(R.id.event_ImageView);

                convertView.setTag(viewHolder);
            }

            else{
                viewHolder = (ViewHolderForEventImageList) convertView.getTag();

            }

            Picasso.with(convertView.getContext()).load(eventImageListsView.getEventImageString()).fit().centerCrop().into(viewHolder.viewImage);
            return convertView;
        }
    }

    public  class ViewHolderForEventImageList {
        ImageView viewImage;
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
