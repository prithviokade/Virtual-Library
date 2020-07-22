package com.example.virtuallibrary;

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


}
