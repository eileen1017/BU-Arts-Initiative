package com.cs591.mooncake.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.cs591.mooncake.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataBaseUtil {

    private Context context;
    public static String dbName = "Kao.db";
    private static String DATABASE_PATH;

    public DataBaseUtil(Context context) {
        this.context = context;
        String packageName = context.getPackageName();
        DATABASE_PATH="/data/data/"+packageName+"/databases/";
    }

    /**
     * See if database exists
     *
     * @return false or true
     */
    public boolean checkDataBase() {
        SQLiteDatabase db = null;
        try {
            String databaseFilename = DATABASE_PATH + dbName;
            db = SQLiteDatabase.openDatabase(databaseFilename, null,SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        if (db != null) {
            db.close();
        }
        return db != null;
    }

    /**
     * copy database to phone
     *
     * @throws IOException
     */
    public void copyDataBase() throws IOException {
        String databaseFilenames = DATABASE_PATH + dbName;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists())
            dir.mkdir();
        FileOutputStream os = new FileOutputStream(databaseFilenames);
        InputStream is = context.getResources().openRawResource(R.raw.db01);
        byte[] buffer = new byte[8192];
        int count;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
            os.flush();
        }
        is.close();
        os.close();
    }
}
