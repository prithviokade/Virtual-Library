package com.example.virtuallibrary;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Table;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;

public class TableUtils extends Application {

    public static final String TAG = "TABLE";
    public static final String TYPE_TAG = "TYPE";

    public static int getTableImage(int size) {
        if (size == 1) { return R.drawable.onetable; }
        if (size == 2) { return R.drawable.twotable; }
        if (size == 3) { return R.drawable.threetable; }
        if (size == 4) { return R.drawable.fourtable; }
        if (size == 5) { return R.drawable.fivetable; }
        if (size == 6) { return R.drawable.sixtable; }
        if (size == 7 || size == 8) { return R.drawable.eighttable; }
        if (size == 9 || size == 10) { return R.drawable.tentable; }
        return -1;
    }

    public static void removeFromPreviousTable(ParseUser user) {
        Table currentTable = (Table) user.get("current");
        if (currentTable != null) {
            List<ParseUser> newMates = new ArrayList<>();
            for (ParseUser mate : currentTable.getMates()) {
                if (!UserUtils.equals(mate, user)) {
                    newMates.add(mate);
                }
            }
            currentTable.setMates(newMates);
            currentTable.saveInBackground();
        }
    }

    public static Table saveNewTable(String topic, String description, int size, String type, Boolean locked, Boolean visitors) {
        Table table = new Table();
        table.setCreator(ParseUser.getCurrentUser());
        table.setMates(new ArrayList<ParseUser>());
        table.addMate(ParseUser.getCurrentUser());
        table.setStatus("working");
        table.setSize(size);
        table.setTopic(topic);
        table.setType(type);
        table.setVisiting(visitors);
        table.setDescription(description);
        table.setLocked(locked);
        table.setChannel(UUID.randomUUID().toString());
        table.saveInBackground();
        TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
        UserUtils.setCurrentTable(ParseUser.getCurrentUser(), table);
        UserUtils.addJoinedSize(ParseUser.getCurrentUser(), table.getSize());
        UserUtils.addJoinedType(ParseUser.getCurrentUser(), table.getType());
        ParseUser.getCurrentUser().saveInBackground();
        return table;
    }

    public static List<Integer> getTableMargins(int size) {
        if (size == 1) { return Arrays.asList(155, 50); }
        if (size == 2) { return Arrays.asList(40, 87, 300, 87); }
        if (size == 3) { return Arrays.asList(145, 25, 253, 87, 145, 155); }
        if (size == 4) { return Arrays.asList(120, 25, 230, 135, 230, 25, 125, 135); }
        if (size == 5) { return Arrays.asList(105, 72, 200, 15, 200, 130, 250, 130, 250, 15); }
        if (size == 6) { return Arrays.asList(80, 85, 145, 20, 205, 155, 270, 85, 205, 20, 145, 155); }
        if (size == 7 || size == 8) { return Arrays.asList(65, 90, 125, 25, 170, 150, 290, 90, 170, 25, 125, 150, 215, 25, 215, 150); }
        if (size == 9 || size == 10) { return Arrays.asList(175, 10, 175, 165, 125, 25, 225, 25, 100, 65, 255, 65, 100, 110, 255, 115, 125, 150, 225, 150); }
        return new ArrayList<>();
    }

    public static int getNumberFriends(Table table) {
        List<ParseUser> mates = table.getMates();
        int count = 0;
        for (ParseUser user : mates) {
            if (UserUtils.userContained(UserUtils.getFriends(ParseUser.getCurrentUser()), user)) {
                count++;
            }
        }
        return count;
    }

    public static int getTableScore(Table table) {
        int friendScore = getNumberFriends(table);

        List<Integer> joinedSizes = UserUtils.getJoinedSizes(ParseUser.getCurrentUser());
        int sum = 0;
        for (int size : joinedSizes) {
            sum += size;
        }
        int average = sum / joinedSizes.size();
        int sizeScore = 10 - abs(table.getSize() - average);

        List<String> joinedTypes = UserUtils.getJoinedTypes(ParseUser.getCurrentUser());
        int typeScore = 0;
        for (String type : joinedTypes) {
            if (type.equals(table.getType())) {
                typeScore += 1;
            }
        }

        return friendScore + sizeScore + typeScore;
    }

    public static void removeInvite(ParseUser to, ParseUser from, Table table, String type) {
        List<Invite> currInvites = table.getInvites();
        List<Invite> remainingInvites = new ArrayList<>();
        for (Invite invite : currInvites) {
            if (UserUtils.equals(invite.getTo(), to) && UserUtils.equals(invite.getFrom(), from) && invite.getType().equals(type)) {
                continue;
            }
            remainingInvites.add(invite);
        }
        table.setInvites(remainingInvites);
        table.saveInBackground();
    }

    public static void addInvite(ParseUser to, ParseUser from, Table table, String type) {
        List<Invite> currInvites = table.getInvites();
        for (Invite invite : currInvites) {
            if (UserUtils.equals(invite.getTo(), to) && UserUtils.equals(invite.getFrom(), from)) {
                return;
            }
        }
        Invite newInvite = new Invite();
        newInvite.setFrom(from);
        newInvite.setTo(to);
        newInvite.setTable(UserUtils.getCurrentTable(ParseUser.getCurrentUser()));
        newInvite.setType(type);
        newInvite.saveInBackground();
        table.addInvite(newInvite);
        table.saveInBackground();
    }

    public static List<Table> sortTable(List<Table> tables, String text, List<String> options) {
        Comparator<Table> compareByLastUpdated = new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                return o2.getUpdatedAt().compareTo(o1.getUpdatedAt());
            }
        };

        Comparator<Table> compareByCreatedAt = new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        };

        Comparator<Table> compareBySize = new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                return Integer.compare(o1.getSize(), o2.getSize());
            }
        };

        Comparator<Table> compareByFriends = new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                return Integer.compare(TableUtils.getNumberFriends(o2), TableUtils.getNumberFriends(o1));
            }
        };

        Comparator<Table> compareByScore = new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                return Integer.compare(TableUtils.getTableScore(o2), TableUtils.getTableScore(o1));
            }
        };

         if (text.equals(options.get(1))) {
             Collections.sort(tables, compareByLastUpdated);
        } else if (text.equals(options.get(2))) {
            Collections.sort(tables, compareByCreatedAt);
        } else if (text.equals(options.get(3))) {
             Collections.sort(tables, compareByFriends);
        } else if (text.equals(options.get(4))) {
             Collections.sort(tables, compareBySize);
        } else if (text.equals(options.get(5))) {
             Collections.sort(tables, Collections.reverseOrder(compareBySize));
        } else { // Recommended
             Collections.sort(tables, compareByScore);
        }
        return tables;
    }

}
