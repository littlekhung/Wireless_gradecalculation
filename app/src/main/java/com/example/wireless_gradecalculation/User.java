package com.example.wireless_gradecalculation;



public class User {
    private String UID;
    private String firstname;
    private String lastname;
    private String picurl;
    public User(String UID,String firstname, String lastname) {
        this.UID=UID;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public User(String UID, String firstname, String lastname,String url) {
        this.UID=UID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.picurl=url;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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
