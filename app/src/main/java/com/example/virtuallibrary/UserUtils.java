package com.example.virtuallibrary;

import android.util.Log;

import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Message;
import com.example.virtuallibrary.models.Table;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    public static final String TAG = "USER";

    public static ParseFile getProfilePicture(ParseUser user) {
        try {
            return user.fetch().getParseFile("picture");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addGoal(ParseUser user, Goal goal) {
        user.add("goals", goal);
    }

    public static void setCurrentTable(ParseUser user, Table table) {
        user.put("current", table);
    }

    public static Table getCurrentTable(ParseUser user) {
        try {
            return (Table) user.fetch().get("current");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void removeCurrentTable(ParseUser user) {
        user.remove("current");
    }

    public static String getUsername(ParseUser user) {
        try {
            return user.fetch().getUsername();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getName(ParseUser user) {
        try {
            return user.fetch().getString("name");
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getBio(ParseUser user) {
        try {
            return user.fetch().getString("bio");
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<Goal> getGoals(ParseUser user) {
        try {
            return (List<Goal>) user.fetch().get("goals");
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean equals(ParseUser self, ParseUser other) {
        return self.getObjectId().equals(other.getObjectId());
    }

    public static void addFriend(ParseUser self, ParseUser friend) {
        self.add("friends", friend);
    }

    public static void setFriends(ParseUser self, List<ParseUser> friends) {
        self.put("friends", friends);
    }

    public static void removeFriend(ParseUser self, ParseUser user) {
        List<ParseUser> friends = UserUtils.getFriends(self);
        List<ParseUser> remainingFriends = new ArrayList<>();
        for (ParseUser friend : friends) {
            Log.d(TAG, UserUtils.getUsername(friend));
            if (UserUtils.equals(friend, user)) {
                Log.d(TAG, UserUtils.getUsername(friend));
                continue;
            }
            remainingFriends.add(friend);
        }
        UserUtils.setFriends(self, remainingFriends);
    }

    public static List<ParseUser> getFriends(ParseUser user) {
        return (List<ParseUser>) user.get("friends");
    }

    public static boolean userContained(List<ParseUser> friends, ParseUser user) {
        for (ParseUser friend : friends) {
            if (UserUtils.equals(friend, user)) {
                return true;
            }
        }
        return false;
    }

    public static List<Invite> queryInvites(List<Table> tables, ParseUser user) {
        List<Invite> invites = new ArrayList<>();
        for (Table table : tables) {
            List<Invite> tableInvites = table.getInvites();
            for (Invite invite : tableInvites) {
                if (UserUtils.equals(invite.getTo(), user)) {
                    invites.add(invite);
                }
            }
        }
        return invites;
    }

    public static boolean userInviteContained(List<Invite> invites, ParseUser to, ParseUser from) {
        for (Invite invite : invites) {
            if (UserUtils.equals(invite.getTo(), to)) {
                return true;
            }
        }
        return false;
    }
}
