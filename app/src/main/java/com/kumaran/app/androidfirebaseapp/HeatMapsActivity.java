package com.kumaran.app.androidfirebaseapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import android.location.Address;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

public class HeatMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference dRef;
    int j =0;
    int[] colors = {
            Color.rgb(0, 191, 255),
            Color.rgb(255, 0, 0)
    };
    FireApp fireApp;
    String locationSelected;
    String eventAddressString;

    ArrayList<Double> intensityArray = new ArrayList<>();
    ArrayList<String> eventsSelected = new ArrayList<>();
    ArrayList<String> eventAddress = new ArrayList<>();
    ArrayList<String> eventCapacity = new ArrayList<>();
    ArrayList<String> eventTicketSold = new ArrayList<>();
    ArrayList<String> snippetMsgList = new ArrayList<>();
    String eventName;
    ArrayList<String> eventShortListed = new ArrayList<>();
    ArrayList<ArrayList<String>> event_collective_list = new ArrayList<>();
    ArrayList<String> event_indi_details = new ArrayList<>();


    String event_id;
    String capacity;
    String eventTicAvailable;
    String snippetMsg;
    double intensity;
    double capacity_event;
    double ticketSold_event;
//    ArrayList<Double> latArray = new ArrayList<>(Arrays.asList(-37.1886, -37.8361, -38.4034, -36.9672));
//    ArrayList<Double> lngArray = new ArrayList<>(Arrays.asList(145.708, 144.845, 144.192, 143.67));
    ArrayList<Double> latArray = new ArrayList<>();
    ArrayList<Double> lngArray = new ArrayList<>();


    ArrayList<String> MarkerTitleArray = new ArrayList<>();

    Double latLngArr[] = new Double[2];


    float[] startPoints = {
            0.2f, 1f
    };

    Gradient gradient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fireApp = (FireApp) getApplicationContext();
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Heat Map");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));
        locationSelected = fireApp.getGLocation();
        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/event");
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                j=0;
                eventsSelected = new ArrayList<String>();
                eventAddress = new ArrayList<String>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot child1 : child.getChildren()) {
                        if (child1.getKey().toString().equals("e_location")) {

                            if (child1.getValue().toString().equals(locationSelected)) {
                                eventsSelected.add(child.getKey().toString());
                            }
                        }
                    }
                }
                System.out.println("check:" + eventsSelected);
                eventAddress = new ArrayList<String>();
                eventTicketSold = new ArrayList<String>();
                eventCapacity = new ArrayList<String>();
                latArray = new ArrayList<Double>();
                lngArray = new ArrayList<Double>();
                intensityArray = new ArrayList<>();
                snippetMsgList = new ArrayList<String>();
                MarkerTitleArray = new ArrayList<String>();

                for (int i = 0; i < eventsSelected.size(); i++) {
                    MarkerTitleArray.add(eventsSelected.get(i));
                    System.out.println("MarkerTitleArray:"+MarkerTitleArray);
                    dRef.child(eventsSelected.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                if (child.getKey().toString().equals("e_address")) {

                                    eventAddressString = child.getValue().toString();
                                    System.out.println(eventAddressString);
                                    eventAddress.add(eventAddressString);
                                    String result = getLocationFromAddress(eventAddressString);
                                    System.out.println("result: "+result);
                                    String resultArr[] = result.split(",");
                                    System.out.println(resultArr[0] +"," +resultArr[1]);
                                    latLngArr[0] = Double.valueOf(resultArr[0]);
                                    latArray.add(latLngArr[0]);
                                    latLngArr[1] = Double.valueOf(resultArr[1]);
                                    lngArray.add(latLngArr[1]);



                                } else if (child.getKey().toString().equals("e_capacity")) {
                                    capacity = child.getValue().toString();
                                    capacity_event = Integer.valueOf(capacity);
                                    eventCapacity.add(child.getValue().toString());
                                } else if (child.getKey().toString().equals("e_ticket_sold")) {
                                    eventTicketSold.add(child.getValue().toString());
                                    String ticketSold = child.getValue().toString();
                                    ticketSold_event = Integer.valueOf(ticketSold);

                                    intensity = ((ticketSold_event/capacity_event)*100);
                                    System.out.println("ticketSold_event:"+ticketSold_event);
                                    System.out.println("capacity_event:"+capacity_event);
                                    System.out.println("intensity:"+intensity);
                                    if(intensity>=0 && intensity<50)
                                    {
                                        intensityArray.add(0.1);
                                    }
                                    else if(intensity>=50 && intensity<70)
                                    {
                                        intensityArray.add(0.5);
                                    }
                                    else if(intensity>=70 && intensity<=100)
                                    {
                                        intensityArray.add(1.0);
                                    }
                                    j++;
                                    System.out.println("j: "+j);

                                } else if (child.getKey().toString().equals("e_name")) {
                                    eventName = child.getValue().toString();
                                }
                                else if (child.getKey().toString().equals("e_ticket_available")) {
                                    eventTicAvailable = child.getValue().toString();
                                    snippetMsg = "Event Name: "+eventName + " | Tickets available: "+eventTicAvailable +"/"+ capacity;
                                    snippetMsgList.add(snippetMsg);
                                    System.out.println("snippetMsgList:"+snippetMsgList);

                                }

                                if (j == eventsSelected.size()) {
                                    System.out.println("MarkerTitleArray:"+MarkerTitleArray);
                                    System.out.println("snippetMsgList1:"+snippetMsgList);
                                    System.out.println("MarkerTitleArray1:"+MarkerTitleArray);
                                    System.out.println("intensity1:"+intensityArray);
                                    System.out.println("latArray1:"+latArray);
                                    System.out.println("lngArray1:"+lngArray);
                                    System.out.println("setUpMapIfNeeded");
                                    setUpMapIfNeeded();
                                    break;
                                }
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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("onMapReadycheck");
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        String cameraAnimate = getLocationFromAddress(locationSelected);
        String resultArr[] = cameraAnimate.split(",");
        latLngArr[0] = Double.valueOf(resultArr[0]);
        latLngArr[1] = Double.valueOf(resultArr[1]);
        LatLng chLocation = new LatLng(latLngArr[0], latLngArr[1]);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(chLocation, 11));
        gradient = new Gradient(colors, startPoints);
        List<WeightedLatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        list = readItems();
        System.out.println("List:" + list);
        System.out.println("MarkerTitleArray:"+MarkerTitleArray);
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder().weightedData(list).gradient(gradient).opacity(1).radius(50).build();
        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < latArray.size(); i++) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latArray.get(i), lngArray.get(i))).title(MarkerTitleArray.get(i)).snippet(snippetMsgList.get(i)));
            marker.showInfoWindow();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < MarkerTitleArray.size(); i++)
                {
                    if (marker.getTitle().equals(MarkerTitleArray.get(i)))
                    {
                        marker.showInfoWindow();
                    }
                }
/*                if (Integer.valueOf(marker.getTitle()) == 1) // if marker source is clicked
//                    Toast.makeText(getBaseContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    marker.showInfoWindow();

                else if (Integer.valueOf(marker.getTitle()) == 2) // if marker source is clicked
//                    Toast.makeText(getBaseContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    marker.showInfoWindow();
                else if (marker.getTitle().equals("C")) // if marker source is clicked
//                    Toast.makeText(getBaseContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    marker.showInfoWindow();
                else if (marker.getTitle().equals("D")) // if marker source is clicked
//                    Toast.makeText(getBaseContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    marker.showInfoWindow();*/
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                eventShortListed = new ArrayList<String>();
                event_indi_details = new ArrayList<String>();
                event_collective_list = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < MarkerTitleArray.size(); i++)
                {
                    if (marker.getTitle().equals(MarkerTitleArray.get(i)))
                    {

                        event_id = MarkerTitleArray.get(i);

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
                }
            }
        });

        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mOverlay.clearTileCache();
    }

    private void setUpMapIfNeeded() {
        System.out.println("setUpMapIfNeeded2");
        // Do a null check to confirm that we have not already instantiated the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public String getLocationFromAddress(String strAddress) {
        String latLng = "";
        Geocoder coder = new Geocoder(HeatMapsActivity.this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address != null && !address.isEmpty()) {
                Address location = address.get(0);
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                latLng = String.valueOf(lat) + "," + String.valueOf(lng);

//            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return latLng;
    }

    /**
     * Read the data (locations of police stations) from raw resources.
     */
    private ArrayList<WeightedLatLng> readItems() {
        ArrayList<WeightedLatLng> weightedLatLngs = new ArrayList<WeightedLatLng>();



        System.out.println("latArray:"+latArray);
        System.out.println("lngArray:"+lngArray);
        for (int i = 0; i < latArray.size(); i++) {
            double lat = latArray.get(i);
            double lng = lngArray.get(i);
            double intensity = intensityArray.get(i);
            WeightedLatLng data = new WeightedLatLng(new LatLng(lat, lng), intensity);
            weightedLatLngs.add(data);
        }

        return weightedLatLngs;
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
