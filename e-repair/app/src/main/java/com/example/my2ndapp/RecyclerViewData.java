package com.example.my2ndapp;

public class RecyclerViewData {

    private int id;
    private String name, place, type;

    RecyclerViewData(){

    }

    public RecyclerViewData(int id, String name, String place, String type) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
