package com.example.pc21.phonebook;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PC21 on 21/3/2015.
 */
public class Contact implements Parcelable {
    private int id;
    private String name;
    private String phoneNo;
    private String email;
    private String address;
    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getId());
        out.writeString(getName());
        out.writeString(getPhoneNo());
        out.writeString(getEmail());
        out.writeString(getAddress());

    }

    public static final Creator<Contact> CREATOR
            = new Parcelable.Creator<Contact>(){
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    private Contact(Parcel in){
        id = in.readInt();
        name = in.readString();
        phoneNo = in.readString();
        email = in.readString();
        address = in.readString();
    }
    public Contact(){}

    @Override
    public String toString() {
        return name;
    }
}
