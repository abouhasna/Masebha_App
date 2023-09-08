package com.bestandroidaboudemaybas.masebha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private static SQLiteDatabase myDB;

    public static SQLiteDatabase getDatabase(Context context) {
        if (myDB == null || !myDB.isOpen()) {
            myDB = context.openOrCreateDatabase("masebha.db", Context.MODE_PRIVATE, null);
        }
        return myDB;
    }

    public static void closeDatabase() {
        if (myDB != null && myDB.isOpen()) {
            myDB.close();
        }
    }
}
