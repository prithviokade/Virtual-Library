package com.example.virtuallibrary.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
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
}