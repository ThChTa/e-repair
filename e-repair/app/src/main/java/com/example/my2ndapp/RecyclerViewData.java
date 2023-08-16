package com.example.my2ndapp;

public class RecyclerViewData {

   // private int id;
    private String location, name, type, description;

    RecyclerViewData(){

    }

    public RecyclerViewData(String name, String location, String type, String description) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }






}
