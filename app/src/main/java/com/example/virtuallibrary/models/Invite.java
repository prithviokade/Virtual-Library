package com.example.virtuallibrary.models;

import androidx.annotation.Nullable;

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

    public static final String TAG = "INVITE";;
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_TABLE = "table";

    public ParseUser getFrom() {
        try {
            return fetch().getParseUser(KEY_FROM);
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
            return fetch().getParseUser(KEY_TO);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTo(ParseUser to) {
        put(KEY_TO, to);
    }

    public Table getTable() {
        try {
            return (Table) fetch().get(KEY_TABLE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTable(Table table) {
        put(KEY_TABLE, table);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Invite.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Invite other = (Invite) obj;
        if (!(this.getObjectId().equals(other.getObjectId()))) {
            return false;
        }
        return true;
    }
}
