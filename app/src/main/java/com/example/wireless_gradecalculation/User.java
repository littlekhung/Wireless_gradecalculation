package com.example.wireless_gradecalculation;

import android.net.Uri;
public class User {
    private String firstname;
    private String lastname;
    private String picurl;
    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public User(String firstname, String lastname,String url) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.picurl=url;
    }
    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
