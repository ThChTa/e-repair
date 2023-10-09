package com.example.my2ndapp;

public class Requests {

    long amount,pid,rid;
    String date_and_time,more_info,offer_countoffer,pfn,pln,rfn,rln;

    Requests(){

    }

    public Requests(long amount, String offer_countoffer, long pid, long rid, String date_and_time, String more_info, String pfn, String pln, String rfn, String rln) {
        this.amount = amount;
        this.offer_countoffer = offer_countoffer;
        this.pid = pid;
        this.rid = rid;
        this.date_and_time = date_and_time;
        this.more_info = more_info;
        this.pfn = pfn;
        this.pln = pln;
        this.rfn = rfn;
        this.rln = rln;
    }


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String  getOffer_countoffer() {
        return offer_countoffer;
    }

    public void setOffer_countoffer(String offer_countoffer) {
        this.offer_countoffer = offer_countoffer;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
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

    public String getPfn() {
        return pfn;
    }

    public void setPfn(String pfn) {
        this.pfn = pfn;
    }

    public String getPln() {
        return pln;
    }

    public void setPln(String pln) {
        this.pln = pln;
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
