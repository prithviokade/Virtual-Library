package com.example.virtuallibrary;

import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    public static final String TAG = "USER";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_GOALS = "goals";
    public static final String KEY_CURRENT_TABLE = "current";
    public static final String KEY_NAME = "name";
    public static final String KEY_BIO = "bio";
    public static final String KEY_FRIENDS = "friends";
    public static final String KEY_JOINED_SIZES = "joined_sizes";
    public static final String KEY_JOINED_TYPES = "joined_types";
    public static final String KEY_VISITING_TABLE = "visiting";


    public static ParseFile getProfilePicture(ParseUser user) {
        try {
            return user.fetch().getParseFile(KEY_PICTURE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addGoal(ParseUser user, Goal goal) {
        user.add(KEY_GOALS, goal);
    }

    public static void setCurrentTable(ParseUser user, Table table) {
        user.put(KEY_CURRENT_TABLE, table);
    }

    public static Table getCurrentTable(ParseUser user) {
        try {
            return (Table) user.fetch().get(KEY_CURRENT_TABLE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void removeCurrentTable(ParseUser user) {
        user.remove(KEY_CURRENT_TABLE);
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
            return user.fetch().getString(KEY_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getBio(ParseUser user) {
        try {
            return user.fetch().getString(KEY_BIO);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<Goal> getGoals(ParseUser user) {
        try {
            return (List<Goal>) user.fetch().get(KEY_GOALS);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Integer> getJoinedSizes(ParseUser user) {
        try {
            return (List<Integer>) user.fetch().get(KEY_JOINED_SIZES);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<String> getJoinedTypes(ParseUser user) {
        try {
            return (List<String>) user.fetch().get(KEY_JOINED_TYPES);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void addJoinedSize(ParseUser user, Integer size) {
        user.add(KEY_JOINED_SIZES, size);
    }

    public static void addJoinedType(ParseUser user, String type) {
         user.add(KEY_JOINED_TYPES, type);
    }

    public static boolean equals(ParseUser self, ParseUser other) {
        return self.getObjectId().equals(other.getObjectId());
    }

    public static void addFriend(ParseUser self, ParseUser friend) {
        self.add(KEY_FRIENDS, friend);
    }

    public static void setFriends(ParseUser self, List<ParseUser> friends) {
        self.put(KEY_FRIENDS, friends);
    }

    public static void removeFriend(ParseUser self, ParseUser user) {
        List<ParseUser> friends = UserUtils.getFriends(self);
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
        return (List<ParseUser>) user.get(KEY_FRIENDS);
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

    public static Table getVisiting(ParseUser currentUser) {
        try {
            return (Table) currentUser.fetch().get(KEY_VISITING_TABLE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isVisiting(ParseUser user, Table table) {
        if (table.equals(getVisiting(user)))  {
            return true;
        }
        return false;
    }

    public static void removeVisitingTable(ParseUser user, Table table) {
        if (isVisiting(user, table)) {
            user.remove(KEY_VISITING_TABLE);
        }
    }

    public static void setVisiting(ParseUser user, Table table) {
        user.put(KEY_VISITING_TABLE, table);
    }
}
