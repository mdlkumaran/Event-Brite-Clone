package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class personalize_interest extends AppCompatActivity {

    FireApp fireApp;
    ArrayList<String> pInterest = new ArrayList<>();
    ArrayList<String> pInterestSelectedList = new ArrayList<>();
    ArrayList<Boolean> status = new ArrayList<>();
    String childPath;

    DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(500);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        setContentView(R.layout.activity_personalize_interest);
        fireApp = (FireApp)getApplicationContext();
        pInterest.addAll(Arrays.asList( "Festival","Halloween","Music", "Party","Science" ));
        status.addAll(Arrays.asList(false, false, false, false, false));
        System.out.println("status1"+status);
        childPath = fireApp.getUsername();
        //mRef = new Firebase("https://androidfirebaseapp-51e0b.firebaseio.com/Users/"+childPath+"/emailID");
        //mRef = new Firebase("https://androidfirebaseapp-51e0b.firebaseio.com/Users/");
        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users");
        dRef = dRef.child(childPath).child("category");


        dRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("every day will be the best");
                System.out.println("dataSnapshot value take care:" + dataSnapshot.getValue());
                status.clear();
                fireApp.setgPInterestList(new ArrayList<String>());
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    System.out.println("dataSnapshot value to be noted:" + child.getValue());
                    status.add((Boolean) child.getValue());
                }
                System.out.println("status3"+status);

                FireApp fireApp = (FireApp)getApplicationContext();
                fireApp.setGCategoryList(status);
                pInterestSelectedList = new ArrayList<>();
                for(int i =0;i<status.size();i++)
                {
                    if(status.get(i) == true)
                    {
                        pInterestSelectedList.add(pInterest.get(i));
                    }
                }


                fireApp.setgPInterestList(pInterestSelectedList);
                System.out.println("pInterestSelectedcheck" + pInterestSelectedList);
                Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
                setSupportActionBar(my_toolbar);
                getSupportActionBar().setTitle("");
                final TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
                toolbarText.setText("Personalize");
                my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

                ListView pInterestsLv = (ListView) findViewById(R.id.lv_pInterests);


                List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();

                OnItemClickListener itemClickListener = new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> lv, View item, int position, long id) {
                        ListView lView = (ListView) lv;
                        SimpleAdapter adapter = (SimpleAdapter) lView.getAdapter();
                        HashMap<String, Object> hm = (HashMap) adapter.getItem(position);
                        RelativeLayout rLayout = (RelativeLayout) item;
                        ToggleButton tg1 = (ToggleButton) rLayout.getChildAt(1);

                        String strStatus = "";
                        if (tg1.isChecked()) {
                            tg1.setChecked(false);
                            strStatus = "Off";
                            status.set(position, false);
                        } else {
                            tg1.setChecked(true);
                            strStatus = "On";
                            status.set(position, true);
                        }
                        String categoryKey = (String) hm.get("txt");
                        boolean categoryValue = status.get(position);
                        System.out.println("categoryKey:" + categoryKey);
                        System.out.println("categoryValue:" + categoryValue);
                        Toast.makeText(getBaseContext(), (String) hm.get("txt") + " : " + strStatus, Toast.LENGTH_SHORT).show();
                        dRef.child(categoryKey).setValue(categoryValue);
                    }
                };
                System.out.println("status6"+status);
                pInterestsLv.setOnItemClickListener(itemClickListener);

                for (int i = 0; i < pInterest.size(); i++) {
                    HashMap<String, Object> hm = new HashMap<String, Object>();
                    hm.put("txt", pInterest.get(i));
                    hm.put("stat", status.get(i));
                    aList.add(hm);
                }

                String[] from = {"txt", "stat"};
                int[] to = {R.id.pInterest_item, R.id.pInterestTgl_status};
                SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.activity_personalize_imagebutton_list, from, to);
                pInterestsLv.setAdapter(adapter);
                System.out.println("status7"+status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean[] statusArr = toPrimitiveArray(status);
        outState.putBooleanArray("status", statusArr);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigate_next:
                startActivity(new Intent(this, location_selection.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }

}
