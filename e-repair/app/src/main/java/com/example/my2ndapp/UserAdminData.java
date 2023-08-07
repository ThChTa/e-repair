package com.example.my2ndapp;

public class UserAdminData {

    String fn,ln,email,type,isAdmin;


    public String getFn() {
        return fn;
    }

    public String getLn() {
        return ln;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getIsAdmin() {
        return isAdmin;
    }


    public UserAdminData(String fn, String ln, String email, String type, String isAdmin) {
        this.fn = fn;
        this.ln = ln;
        this.email = email;
        this.type = type;
        this.isAdmin = isAdmin;
    }


}
