package com.kumaran.app.androidfirebaseapp;

import android.app.Application;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by kumaran on 29-10-2016.
 */

public class FireApp extends Application {
    private String gLocation;
    private String eventFragment;
    private ArrayList<Boolean> gCategoryList;
    private ArrayList<String> gPInterestList;
    private ArrayList<Object> event_details;
    private String username;
    private String eventIDForImage;
    private String UserEmail;
    private ArrayList<ArrayList<String>> event_collective_list_for_you;


    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getEventIDForImage() {
        return eventIDForImage;
    }

    public void setEventIDForImage(String eventIDForImage) {
        this.eventIDForImage = eventIDForImage;
    }

    public String getEventFragment() {
        return eventFragment;
    }

    public void setEventFragment(String eventFragment) {
        this.eventFragment = eventFragment;
    }

    public ArrayList<ArrayList<String>> getEvent_collective_list_for_you() {
        return event_collective_list_for_you;
    }

    public void setEvent_collective_list_for_you(ArrayList<ArrayList<String>> event_collective_list_for_you) {
        this.event_collective_list_for_you = event_collective_list_for_you;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getEvent_key_name() {
        return event_key_name;
    }

    public ArrayList<Object> getEvent_details() {
        return event_details;
    }

    public void setEvent_details(ArrayList<Object> event_details) {
        this.event_details = new ArrayList<>(event_details);
    }

    private ArrayList<String> event_key_name = new ArrayList<>(Arrays.asList("Rating","Review","e_address", "e_capacity", "e_category", "e_date", "e_description","e_host","e_image", "e_location", "e_name", "e_price", "e_rate", "e_ticket_available", "e_ticket_sold", "e_time", "e_venue", "eventID"));;
    private ArrayList<String> eventShortListedForYou;
    HashMap<String,ArrayList<Object>> eventValuesHmForYou;;

    public ArrayList<String> getEventShortListedForYou() {
        return eventShortListedForYou;
    }

    public void setEventShortListedForYou(ArrayList<String> eventShortListedForYouSize) {
        this.eventShortListedForYou = new ArrayList<>(eventShortListedForYouSize);
    }



    public HashMap<String, ArrayList<Object>> getEventValuesHmForYou() {
        return eventValuesHmForYou;
    }

    public void setEventValuesHmForYou(HashMap<String, ArrayList<Object>> eventValuesHmForYou) {
        this.eventValuesHmForYou = new HashMap<>(eventValuesHmForYou);
    }





    public ArrayList<String> getgPInterestList() {
        return gPInterestList;
    }

    public void setgPInterestList(ArrayList<String> gPInterestList) {
        this.gPInterestList = new ArrayList<>(gPInterestList);
    }

    public String getGLocation() {
        return gLocation;
    }

    public void setGLocation(String str) {
        gLocation = str;
    }

    public ArrayList<Boolean> getGCategoryList() {
        return gCategoryList;
    }

    public void setGCategoryList(ArrayList<Boolean> gCategoryList) {
        this.gCategoryList = new ArrayList<>(gCategoryList);
    }

    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        }
}
