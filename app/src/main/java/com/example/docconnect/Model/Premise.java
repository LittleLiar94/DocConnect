package com.example.docconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Premise implements Parcelable {
    private  String name, description, location, phone, image, premiseId;
    private  List<Boolean> openingDayOfWeek = new ArrayList<>();
    private  List<String> timeSlot = new ArrayList<>();
    private  Long rating, ratingTimes;

    public Premise() {
    }

    protected Premise(Parcel in) {
        name = in.readString();
        description = in.readString();
        location = in.readString();
        phone = in.readString();
        image = in.readString();
        premiseId = in.readString();
        rating = in.readLong();
        ratingTimes = in.readLong();

        this.openingDayOfWeek = new ArrayList<>();
        in.readList(openingDayOfWeek,getClass().getClassLoader());
        this.timeSlot = new ArrayList<>();
        in.readList(timeSlot,getClass().getClassLoader());
    }

    public static final Creator<Premise> CREATOR = new Creator<Premise>() {
        @Override
        public Premise createFromParcel(Parcel in) {
            return new Premise(in);
        }

        @Override
        public Premise[] newArray(int size) {
            return new Premise[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPremiseId() {
        return premiseId;
    }

    public void setPremiseId(String premiseId) {
        this.premiseId = premiseId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Boolean> getOpeningDayOfWeek() {
        return openingDayOfWeek;
    }

    public void setOpeningDayOfWeek(List<Boolean> openingDayOfWeek) {
        this.openingDayOfWeek = openingDayOfWeek;
    }

    public List<String> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<String> timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(phone);
        dest.writeString(image);
        dest.writeString(premiseId);
        dest.writeLong(rating);
        dest.writeList(openingDayOfWeek);
        dest.writeList(timeSlot);
    }
}
