package com.example.virtuallibrary.models;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze={Invite.class})
@ParseClassName("Invite")
public class Invite extends ParseObject {

    // empty constructor needed by the Parceler library
    public Invite() { }

    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_TABLE = "table";

    public ParseUser getFrom() {
        try {
            return fetchIfNeeded().getParseUser(KEY_FROM);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFrom(ParseUser from) {
        put(KEY_FROM, from);
    }

    public ParseUser getTo() {
        try {
            return fetchIfNeeded().getParseUser(KEY_TO);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTo(ParseUser to) {
        put(KEY_TO, to);
    }

    public ParseUser getTable() {
        try {
            return fetchIfNeeded().getParseUser(KEY_TABLE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTable(Table table) {
        put(KEY_TABLE, table);
    }

}
