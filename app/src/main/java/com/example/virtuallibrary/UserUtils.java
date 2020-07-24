package com.example.virtuallibrary;

import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseFile;
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
        List<ParseUser> friends = UserUtils.getFriends(user);
        List<ParseUser> remainingFriends = new ArrayList<>();
        for (ParseUser friend : friends) {
            if (UserUtils.equals(friend, user)) {
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
}
