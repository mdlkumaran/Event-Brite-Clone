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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserProfile extends AppCompatActivity {
    ListView ticket_Futurelist;
    ListView ticket_Pastlist;
    ListView ticket_todaylist;
    TextView usernameView;
    FireApp fireApp;
    String username;
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

    private ArrayList<String> future_ticket_list = new ArrayList<>();
    private ArrayList<String> today_ticket_list = new ArrayList<>();
    private ArrayList<String> past_ticket_list = new ArrayList<>();

    DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("User Profile");
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

        fireApp = (FireApp) getApplicationContext();
        username = fireApp.getUsername();
        usernameView = (TextView) findViewById(R.id.userprofile_name);
        usernameView.setText("Username: " + username);
        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username + "/tickets");
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot child1 : child.getChildren()) {

                        System.out.println("dataSnapshot child value to be noted:" + child1.getKey());
                        if(child1.getKey().toString().equals("e_date"))
                        {
                            try {
                                eventDate = child1.getValue().toString();
                                event_dateLong = parseDate(eventDate);
                                eventDateFormatted = DateFormat(event_dateLong);

                                System.out.println("event_date:"+eventDateFormatted);
                                System.out.println("todays_date:"+todayDateFormatted);
                                if(todayDateFormatted.compareTo(eventDateFormatted)>0)
                                {
                                    System.out.println("event is before current date");
                                    past_ticket_list.add(child.getKey());
                                }
                                else if(todayDateFormatted.compareTo(eventDateFormatted)==0)
                                {
                                    System.out.println("event is equal current date");
                                    today_ticket_list.add(child.getKey());
                                }
                                else if(todayDateFormatted.compareTo(eventDateFormatted)<0)
                                {
                                    System.out.println("event is after current date");
                                    future_ticket_list.add(child.getKey());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("dataSnapshot child value to be noted:" + child1.getValue());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ticket_Futurelist = (ListView) findViewById(R.id.tickets_future_list);
        ticket_Pastlist = (ListView) findViewById(R.id.tickets_past_list);
        ticket_todaylist = (ListView) findViewById(R.id.tickets_today_list);

        ArrayAdapter<String> futureListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, future_ticket_list);
        ticket_Futurelist.setAdapter(futureListAdapter);
        ticket_Futurelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) ticket_Futurelist.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ticket_future_details.class);
                intent.putExtra("value", value);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ArrayAdapter<String> pastListadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, past_ticket_list);
        ticket_Pastlist.setAdapter(pastListadapter);
        ticket_Pastlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) ticket_Pastlist.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ticket_past_details.class);
                intent.putExtra("value", value);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ArrayAdapter<String> todayListadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, today_ticket_list);
        ticket_todaylist.setAdapter(todayListadapter);
        ticket_todaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) ticket_todaylist.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ticket_today_details.class);
                intent.putExtra("value", value);
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
