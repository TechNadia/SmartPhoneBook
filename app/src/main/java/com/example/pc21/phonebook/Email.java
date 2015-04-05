package com.example.pc21.phonebook;

/**
 * Created by Nadia Akter on 3/31/2015.
 */
public class Email {
    private int id;
    private String subject;
    private String date;
    private String receivedFrom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    @Override
    public String toString() {
        return receivedFrom;
    }
}
