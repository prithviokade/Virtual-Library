package com.example.virtuallibrary;

import android.util.Log;

import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class TableUtils {

    public static int getTableImage(int size) {
        if (size == 1) { return R.drawable.onetable; }
        if (size == 2) { return R.drawable.twotable; }
        if (size == 3) { return R.drawable.threetable; }
        if (size == 4) { return R.drawable.fourtable; }
        if (size == 5) { return R.drawable.fivetable; }
        if (size == 6) { return R.drawable.sixtable; }
        if (size == 8) { return R.drawable.eighttable; }
        if (size == 10) { return R.drawable.tentable; }
        return -1;
    }

    public static void removeFromPreviousTable(ParseUser user) {
        Table currentTable = (Table) user.get("current");
        if (currentTable != null) {
            List<ParseUser> newMates = new ArrayList<>();
            for (ParseUser mate : currentTable.getMates()) {
                if (!(mate.getUsername().equals(user.getUsername()))) {
                    newMates.add(mate);
                }
            }
            currentTable.setMates(newMates);
            saveTable(currentTable);
        }
    }

    public static void saveTable(Table table) {
        table.saveInBackground();
    }

}
