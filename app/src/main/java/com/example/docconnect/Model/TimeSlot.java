package com.example.docconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TimeSlot implements Parcelable {
    private Long slot;
    private List<String> timeSlot;

    public TimeSlot() {
    }

    protected TimeSlot(Parcel in) {
        if (in.readByte() == 0) {
            slot = null;
        } else {
            slot = in.readLong();
        }
        timeSlot = in.createStringArrayList();
    }

    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public List<String> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<String> timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (slot == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(slot);
        }
        dest.writeStringList(timeSlot);
    }
}
