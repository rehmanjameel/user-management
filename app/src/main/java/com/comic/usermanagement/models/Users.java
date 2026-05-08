package com.comic.usermanagement.models;

import java.io.Serializable;

public class Users implements Serializable {
    public String id;
    public String username;
    public String dob;
    public String address;
    public String contact;
    public String email;
    public String gender;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String id, String username, String dob, String address, String contact, String email, String gender) {
        this.id = id;
        this.username = username;
        this.dob = dob;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.gender = gender;
    }
}
