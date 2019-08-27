package com.example.docconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {
    private String name, description, serviceId;
    private Long price;

    public Service() {
    }

    protected Service(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readLong();
        serviceId = in.readString();
    }

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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getServiceId(String id) {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(price);
        dest.writeString(serviceId);
    }
}
