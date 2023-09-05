package com.example.my2ndapp;

public class RecyclerViewData {

    // private int id;
    private String location, name,lastName, type, description;
    private int publicationId;

    RecyclerViewData(){

    }

    public RecyclerViewData(String location, String name,String lastName, String type, String description, int publicationId) {
        this.location = location;
        this.name = name;
        this.lastName= lastName;
        this.type = type;
        this.description = description;
        this.publicationId = publicationId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {this.lastName = lastName; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }










}
