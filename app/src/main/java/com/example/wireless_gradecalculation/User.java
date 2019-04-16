package com.example.wireless_gradecalculation;


import android.graphics.Bitmap;
import android.net.Uri;

public class User {
    private String UID;
    private String firstname;
    private String lastname;
    private String picuri;
    public User(String UID,String firstname, String lastname) {
        this.UID=UID;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public User(String UID, String firstname, String lastname,String pic) {
        this.UID=UID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.picuri = pic;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPicuri() {
        return picuri;
    }

    public void setPicuri(String picuri) {
        this.picuri = picuri;
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
