package com.example.virtuallibrary.models;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze={Goal.class})
@ParseClassName("Goal")
public class Goal extends ParseObject {

    // empty constructor needed by the Parceler library
    public Goal() { }

    public static final String KEY_GOAL = "goal";
    public static final String KEY_STATUS = "status";
    public static final String KEY_USER = "user";

    public String getGoal() {
        try {
            return fetchIfNeeded().getString(KEY_GOAL);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setGoal(String goal) {
        put(KEY_GOAL, goal);
    }

    public String getStatus() {
        try {
            return fetchIfNeeded().getString(KEY_STATUS);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setStatus(String status) {
        put(KEY_STATUS, status);
    }

    public ParseUser getUser() {
        try {
            return fetchIfNeeded().getParseUser(KEY_USER);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}
