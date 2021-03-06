package com.example.virtuallibrary.models;

import androidx.annotation.Nullable;

import com.example.virtuallibrary.UserUtils;
import com.parse.ParseClassName;
import com.parse.ParseException;
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
    public static final String KEY_CHAT = "chat";
    public static final String KEY_INVITES = "invites";
    public static final String KEY_CHANNEL = "channel";
    public static final String KEY_SONGS = "songs";
    public static final String KEY_CURRENT_SONGS = "current_song";


    public ParseUser getCreator() {
        try {
            return fetch().getParseUser(KEY_CREATOR);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCreator(ParseUser creator) {
        put(KEY_CREATOR, creator);
    }

    public String getStatus() {
        try {
            return fetch().getString(KEY_STATUS);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setStatus(String status) {
        put(KEY_STATUS, status);
    }

    public List<ParseUser> getMates() {
        try {
            return (List<ParseUser>) fetch().get(KEY_MATES);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMates(List<ParseUser> mates) {
        put(KEY_MATES, mates);
    }

    public void addMate(ParseUser mate) {
        add(KEY_MATES, mate);
    }

    public int getSize() {
        try {
            return fetch().getInt(KEY_SIZE);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setSize(int size) { put(KEY_SIZE, size); }

    public String getTopic() {
        try {
            return fetch().getString(KEY_TOPIC);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setTopic(String topic) {
        put(KEY_TOPIC, topic);
    }

    public String getType() {
        try {
            return fetch().getString(KEY_TYPE);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setType(String type) {
        put(KEY_TYPE, type);
    }

    public boolean getVisiting() {
        try {
            return fetch().getBoolean(KEY_VISITING);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void setVisiting(boolean visiting) { put(KEY_VISITING, visiting); }

    public String getDescription() {
        try {
            return fetch().getString(KEY_DESCRIPTION);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public boolean getLocked() {
        try {
            return fetch().getBoolean(KEY_LOCKED);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void setLocked(boolean locked) { put(KEY_LOCKED, locked); }

    public List<Message> getChat() {
        try {
            return (List<Message>) fetch().get(KEY_CHAT);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setChat(List<Message> messages) {
        put(KEY_CHAT, messages);
    }

    public void addChat(Message message) {
        add(KEY_CHAT, message);
    }

    public List<Invite> getInvites() {
        try {
            return (List<Invite>) fetch().get(KEY_INVITES);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void setInvites(List<Invite> invites) {
        put(KEY_INVITES, invites);
    }

    public void addInvite(Invite invite) {
        add(KEY_INVITES, invite);
    }

    public void removeInvite(Invite invite) {
        List<Invite> currInvites = getInvites();
        List<Invite> remainingInvites = new ArrayList<>();
        for (Invite inv : currInvites) {
            if (UserUtils.equals(inv.getTo(), ParseUser.getCurrentUser())) {
                continue;
            }
            remainingInvites.add(inv);
        }
        setInvites(remainingInvites);
        saveInBackground();
    }

    public String getChannel() {
        try {
            return fetch().getString(KEY_CHANNEL);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setChannel(String channel) {
        put(KEY_CHANNEL, channel);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Table.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Table other = (Table) obj;
        if (!(this.getObjectId().equals(other.getObjectId()))) {
            return false;
        }
        return true;
    }

    public boolean containsUser(ParseUser user) {
        for (ParseUser mate : this.getMates()) {
            if (UserUtils.equals(user, mate)) {
                return true;
            }
        }
        return false;
    }

    public void setSongs(List<String> songs) {
        put(KEY_SONGS, songs);
    }

    public List<String> getSongs() {
        try {
            return (List<String>) fetch().get(KEY_SONGS);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addSong(String song) {
        add(KEY_SONGS, song);
    }

    public String getCurrentSong() {
        try {
            return fetch().getString(KEY_CURRENT_SONGS);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCurrentSong(String song) {
        put(KEY_CURRENT_SONGS, song);
    }
}