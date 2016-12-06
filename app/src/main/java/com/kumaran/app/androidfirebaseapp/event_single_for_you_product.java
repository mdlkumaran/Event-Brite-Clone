package com.kumaran.app.androidfirebaseapp;

/**
 * Created by kumaran on 02-11-2016.
 */

public class event_single_for_you_product {
    String image_url;
    String date;
    String title;
    String Event_ID;
    String City;
    String CategoryName;
    String ticket_available;
    String ticket_cost;
    public event_single_for_you_product(String date, String title, String event_ID, String city, String categoryName, String ticket_available, String ticket_cost,String image_url) {
        this.image_url = image_url;
        this.date = date;
        this.title = title;
        Event_ID = event_ID;
        City = city;
        CategoryName = categoryName;
        this.ticket_available = ticket_available;
        this.ticket_cost = ticket_cost;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(String event_ID) {
        Event_ID = event_ID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getTicket_available() {
        return ticket_available;
    }

    public void setTicket_available(String ticket_available) {
        this.ticket_available = ticket_available;
    }

    public String getTicket_cost() {
        return ticket_cost;
    }

    public void setTicket_cost(String ticket_cost) {
        this.ticket_cost = ticket_cost;
    }
}
