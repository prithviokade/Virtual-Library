package com.example.virtuallibrary;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.virtuallibrary.fragments.table_fragments.Table10Fragment;
import com.example.virtuallibrary.fragments.table_fragments.Table1Fragment;
import com.example.virtuallibrary.fragments.table_fragments.Table2Fragment;
import com.example.virtuallibrary.fragments.table_fragments.Table3Fragment;
import com.example.virtuallibrary.fragments.table_fragments.Table4Fragment;
import com.example.virtuallibrary.fragments.table_fragments.Table5Fragment;
import com.example.virtuallibrary.fragments.table_fragments.Table6Fragment;
import com.example.virtuallibrary.fragments.table_fragments.Table8Fragment;
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
            saveTable(currentTable);
        }
    }

    public static void saveTable(Table table) {
        table.saveInBackground();
    }

    public static Fragment getTableFragment(int size) {
        if (size == 1) { return new Table1Fragment(); }
        if (size == 2) { return new Table2Fragment();}
        if (size == 3) { return new Table3Fragment(); }
        if (size == 4) { return new Table4Fragment(); }
        if (size == 5) { return new Table5Fragment(); }
        if (size == 6) { return new Table6Fragment();}
        if (size == 7 || size == 8) { return new Table8Fragment(); }
        if (size == 9 || size == 10) { return new Table10Fragment();}
        return new Table6Fragment();
    }
}
