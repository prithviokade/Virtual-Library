package com.example.virtuallibrary;

import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableUtils {

    public static final String TAG = "TABLE";

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
                String mateUsername = null;
                String userUsername = null;
                try {
                    mateUsername = mate.fetch().getUsername();
                    userUsername = user.fetch().getUsername();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!(mateUsername.equals(userUsername))) {
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
        table.saveInBackground();
        TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
        UserUtils.setCurrentTable(ParseUser.getCurrentUser(), table);
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

}
