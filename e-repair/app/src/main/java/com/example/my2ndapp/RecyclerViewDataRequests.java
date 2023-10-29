package com.example.my2ndapp;

public class RecyclerViewDataRequests {

    String amount, date_and_time, more_info, rfn, rln;



    Long pId;


    public RecyclerViewDataRequests(String amount, String date_and_time, String more_info, Long pId, String rfn, String rln) {
        this.amount = amount;
        this.date_and_time = date_and_time;
        this.more_info = more_info;
        this.pId = pId;
        this.rfn = rfn;
        this.rln = rln;
    }

    RecyclerViewDataRequests(){

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }

    public String getMore_info() {
        return more_info;
    }

    public void setMore_info(String more_info) {
        this.more_info = more_info;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getRfn() {
        return rfn;
    }

    public void setRfn(String rfn) {
        this.rfn = rfn;
    }

    public String getRln() {
        return rln;
    }

    public void setRln(String rln) {
        this.rln = rln;
    }

}
