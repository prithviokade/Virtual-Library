package com.example.virtuallibrary.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;


@Parcel(analyze={Message.class})
@ParseClassName("Message")
public class Message extends ParseObject {

    // empty constructor needed by the Parceler library
    public Message() { }

    public static final String KEY_TEXT = "text";
    public static final String KEY_SENDER = "sender";

    public String getText() {
        try {
            return fetchIfNeeded().getString(KEY_TEXT);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setText(String text) {
        put(KEY_TEXT, text);
    }

    public ParseUser getSender() {
        try {
            return fetchIfNeeded().getParseUser(KEY_SENDER);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setSender(ParseUser sender) {
        put(KEY_SENDER, sender);
    }



}



