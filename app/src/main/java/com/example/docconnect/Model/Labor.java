package com.example.docconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Labor implements Parcelable {
    private  String name, username, password, laborId;
    private  Long rating, ratingTimes;

    public Labor() {
    }

    protected Labor(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        laborId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
        if (in.readByte() == 0) {
            ratingTimes = null;
        } else {
            ratingTimes = in.readLong();
        }
    }

    public static final Creator<Labor> CREATOR = new Creator<Labor>() {
        @Override
        public Labor createFromParcel(Parcel in) {
            return new Labor(in);
        }

        @Override
        public Labor[] newArray(int size) {
            return new Labor[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLaborId() {
        return laborId;
    }

    public void setLaborId(String laborId) {
        this.laborId = laborId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(laborId);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rating);
        }
        if (ratingTimes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(ratingTimes);
        }
    }
}


