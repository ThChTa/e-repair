package com.example.my2ndapp;

public class RecyclerViewData {


    private String location, name, lastName, type, description;
    private Long publicationId, requests;

    RecyclerViewData(){

    }

    public RecyclerViewData(String location, String name,String lastName, String type, String description, Long publicationId, Long requests) {
        this.location = location;
        this.name = name;
        this.lastName= lastName;
        this.type = type;
        this.description = description;
        this.publicationId = publicationId;
        this.requests = requests;
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

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setRequests(Long requests) {
        this.requests = requests;
    }

    public Long getRequests() {
        return requests;
    }










    }
