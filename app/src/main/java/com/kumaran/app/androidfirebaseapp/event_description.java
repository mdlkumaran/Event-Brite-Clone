package com.kumaran.app.androidfirebaseapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class event_description extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener{

    DatabaseReference dRef;
    ArrayList<Object> eventlist_values = new ArrayList<>();
    ArrayList<String> event_key_name;
    ImageView event_image_desc;
    TextView event_date_desc;
    TextView event_name_desc;
    TextView event_description_desc;
    TextView event_host;
    String eventID_use;
    int ticket_available;
    String event_date;
    Button btn_register;

    String today_date;
    String eventDate;
    Date todays_date;
    long today_dateLong;
    long event_dateLong;
    DateFormat dateFormat;
    String dayDate1String;
    String monthDate1String;
    String yearDate1String;
    Date todayDateFormatted;
    Date eventDateFormatted;
    boolean event_allow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);


        final FireApp fireApp = (FireApp)getApplicationContext();
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Event Description");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        todays_date = new Date();
        today_date = dateFormat.format(todays_date);
        try {
            today_dateLong = parseDate(today_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("today_dateLong:"+today_dateLong);
        todayDateFormatted = DateFormat(today_dateLong);
        System.out.println("todays date:"+today_date);

        event_key_name = fireApp.getEvent_key_name();
        event_image_desc =(ImageView)findViewById(R.id.event_image_desc);
        event_date_desc = (TextView)findViewById(R.id.event_date_desc);
        event_name_desc = (TextView)findViewById(R.id.event_name_desc);
        event_description_desc = (TextView)findViewById(R.id.eventDescription_desc);
        event_host = (TextView)findViewById(R.id.event_host);
        btn_register =(Button)findViewById(R.id.button_register_desc);
        String event_ID = getIntent().getStringExtra("event_ID register");
        System.out.println("eventID_use"+event_ID);
        eventID_use = event_ID.replaceAll("[a-zA-Z ]","").replace(":","");

        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event/event"+eventID_use);
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    eventlist_values.add(child.getValue());
                }

                for (int i=0;i<eventlist_values.size();i++)
                {
                    if(event_key_name.get(i).equals("e_date"))
                    {
                        System.out.println("event_key_name"+event_key_name);
                        System.out.println("eventlist"+eventlist_values);
                        System.out.println("eventlist_values.get(i)"+eventlist_values.get(i));
                        event_date_desc.setText("Date: "+(String)eventlist_values.get(i));
                        try {
                            eventDate = eventlist_values.get(i).toString();
                            event_dateLong = parseDate(eventDate);
                            eventDateFormatted = DateFormat(event_dateLong);

                            System.out.println("event_date:"+eventDateFormatted);
                            System.out.println("todays_date:"+todayDateFormatted);
                            if(todayDateFormatted.compareTo(eventDateFormatted)>0)
                            {
                                System.out.println("event is before current date");
                                event_allow = false;

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    else if(event_key_name.get(i).equals("e_image"))
                    {
                        Picasso.with(getBaseContext()).load(eventlist_values.get(i).toString()).fit().centerCrop().into(event_image_desc);
                    }
                    else if(event_key_name.get(i).equals("e_name"))
                    {
                        event_name_desc.setText("Title: "+(String)eventlist_values.get(i));
                    }
                    else if(event_key_name.get(i).equals("e_description"))
                    {
                        event_description_desc.setText((String)eventlist_values.get(i));
                    }
                    else if(event_key_name.get(i).equals("e_host"))
                    {
                        event_host.setText("Event Host: "+(String)eventlist_values.get(i));
                    }
                    else if(event_key_name.get(i).equals("e_ticket_available"))
                    {
                        ticket_available = Integer.valueOf(eventlist_values.get(i).toString());
                        System.out.println("ticket_available:"+ticket_available);
                    }

                }
                fireApp.setEvent_details(eventlist_values);
                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(event_allow == false)
                        {
                            Toast.makeText(getApplicationContext(), "Cannot book tickets for Past events.", Toast.LENGTH_LONG).show();
                        }
                        else if(ticket_available<=0)
                        {
                            Toast.makeText(getApplicationContext(), "Tickets are sold out. Cannot book tickets", Toast.LENGTH_LONG).show();
                        }
                        else if(ticket_available>0) {
                            Intent intent = new Intent(getBaseContext(), event_registration.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                });


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        menu.add(0, 0, 0, "Change Location");
        menu.add(0, 1, 0, "Personalize");
        menu.add(0, 2, 0, "DashBoard");
        menu.add(0, 3, 0, "Event List For You");
        menu.add(0, 4, 0, "User Profile");
        menu.add(0, 5, 0, "LogOut");
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
                startActivity(new Intent(getBaseContext(), event_list_you_list.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 4:
                startActivity(new Intent(getBaseContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 5:
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

    public Date DateFormat(Long today_dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar date1 = Calendar.getInstance();
        date1.setTimeInMillis(today_dateLong);
        int dayDate1 = date1.get(Calendar.DAY_OF_MONTH);
        int monthDate1 = date1.get(Calendar.MONTH);
        int yearDate1 = date1.get(Calendar.YEAR);
        dayDate1String = String.valueOf(dayDate1);
        monthDate1String = String.valueOf(monthDate1);
        yearDate1String = String.valueOf(yearDate1);
        String dateFormatted = yearDate1String + "-" +monthDate1String + "-" +dayDate1String;
        Date date =null;
        try {
            date = sdf.parse(dateFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    private long parseDate(String text)
            throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy",
                Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.parse(text).getTime();
    }


}
