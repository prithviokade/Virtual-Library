package com.example.virtuallibrary.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze={Post.class})
@ParseClassName("Post")
public class Post extends ParseObject {

    // empty constructor needed by the Parceler library
    public Post() { }

    public static final String KEY_CAPTION = "caption";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_LINK = "link";
    public static final String KEY_FILE = "file";
    public static final String KEY_FILEPATH = "filepath";
    public static final String KEY_FILENAME = "filename";

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getSubject()  {
        return getString(KEY_SUBJECT);
    }

    public void setSubject(String subject)  {
        put(KEY_SUBJECT, subject);
    }

    public String getLink()  {
        return getString(KEY_LINK);
    }

    public void setLink(String link)  {
        put(KEY_LINK, link);
    }

    public ParseFile getFile()  {
        return getParseFile(KEY_FILE);
    }

    public void setFile(ParseFile file) {
        put(KEY_FILE, file);
    }

    public String getFileName() {
        return getString(KEY_FILENAME);
    }

    public void setFileName(String filepath) {
        put(KEY_FILENAME, filepath);
    }

    public String getFilePath() {
        return getString(KEY_FILEPATH);
    }

    public void setFilePath(String filepath) {
        put(KEY_FILEPATH, filepath);
    }

}