package com.example.docconnect.Model;

import com.google.firebase.Timestamp;

public class BookingInformation {
    private String  customerName, customerPhone, time, laborID, laborName, premiseID, premiseName, premiseAddress;
    private Long slot;
    private Timestamp timestamp;
    private boolean done;

    public BookingInformation() {
    }

    public BookingInformation(String customerName, String customerPhone, String time, String laborID, String laborName, String premiseID, String premiseName, String premiseAddress, Long slot, Timestamp timestamp, boolean done) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.time = time;
        this.laborID = laborID;
        this.laborName = laborName;
        this.premiseID = premiseID;
        this.premiseName = premiseName;
        this.premiseAddress = premiseAddress;
        this.slot = slot;
        this.timestamp = timestamp;
        this.done = done;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLaborID() {
        return laborID;
    }

    public void setLaborID(String laborID) {
        this.laborID = laborID;
    }

    public String getLaborName() {
        return laborName;
    }

    public void setLaborName(String laborName) {
        this.laborName = laborName;
    }

    public String getPremiseID() {
        return premiseID;
    }

    public void setPremiseID(String premiseID) {
        this.premiseID = premiseID;
    }

    public String getPremiseName() {
        return premiseName;
    }

    public void setPremiseName(String premiseName) {
        this.premiseName = premiseName;
    }

    public String getPremiseAddress() {
        return premiseAddress;
    }

    public void setPremiseAddress(String premiseAddress) {
        this.premiseAddress = premiseAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
