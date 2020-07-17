package com.example.virtuallibrary.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze={Table.class})
@ParseClassName("Table")
public class Table extends ParseObject {

    // empty constructor needed by the Parceler library
    public Table() { }

    public static final String KEY_CREATOR = "creator";
    public static final String KEY_MATES = "mates";
    public static final String KEY_STATUS = "status";
    public static final String KEY_SIZE = "size";
    public static final String KEY_TOPIC = "topic";
    public static final String KEY_TYPE = "type";
    public static final String KEY_VISITING = "visiting";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCKED = "locked";

    public ParseUser getCreator() {
        return getParseUser(KEY_CREATOR);
    }

    public void setCreator(ParseUser creator) {
        put(KEY_CREATOR, creator);
    }

    public String getStatus() {
        return getString(KEY_STATUS);
    }

    public void setStatus(String status) {
        put(KEY_STATUS, status);
    }

    public List<ParseUser> getMates() {
        return (List<ParseUser>) get(KEY_MATES);
    }

    public void setMates(ArrayList<ParseUser> mates) {
        put(KEY_MATES, mates);
    }

    public void addMate(ParseUser mate) {
        add(KEY_MATES, mate);
    }

    public int getSize() { return getInt(KEY_SIZE); }

    public void setSize(int size) { put(KEY_SIZE, size); }

    public String getTopic() {
        return getString(KEY_TOPIC);
    }

    public void setTopic(String topic) {
        put(KEY_TOPIC, topic);
    }

    public String getType() {
        return getString(KEY_TYPE);
    }

    public void setType(String type) {
        put(KEY_TYPE, type);
    }

    public boolean getVisiting() { return getBoolean(KEY_VISITING); }

    public void setVisiting(boolean visiting) { put(KEY_VISITING, visiting); }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public boolean getLocked() { return getBoolean(KEY_LOCKED); }

    public void setLocked(boolean locked) { put(KEY_LOCKED, locked); }

}