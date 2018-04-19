package com.cs591.mooncake.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Kao.db";

    private static final String EVENT_TABLE_NAME = "Event";
    private static final String COLUMN_EVENT_ID = "ID";
    private static final String COLUMN_EVENT_NAME = "NAME";
    private static final String COLUMN_EVENT_ADDRESS = "ADDRESS";
    private static final String COLUMN_EVENT_ARTIST = "Artist";
    private static final String COLUMN_EVENT_DATE = "Date";
    private static final String COLUMN_EVENT_START = "Start";
    private static final String COLUMN_EVENT_END = "End";
    private static final String COLUMN_EVENT_PIC = "Pic";
    private static final String COLUMN_EVENT_TYPE = "Type";

    private static final String ARTIST_TABLE_NAME = "Artist";
    private static final String COLUMN_ARTIST_ID = "ID";
    private static final String COLUMN_ARTIST_NAME = "Name";
    private static final String COLUMN_ARTIST_BIOS = "Bios";
    private static final String COLUMN_ARTIST_PIC = "Pic";
    private static final String COLUMN_ARTIST_WEBSITE = "Website";
    private static final String COLUMN_ARTIST_COUNTRY = "Country";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<Integer> getEventList() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + EVENT_TABLE_NAME, null);

        List<Integer> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(cursor.getInt(0));
        }







        return res;
    }

    public List<Integer> getArtistList() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + ARTIST_TABLE_NAME, null);

        List<Integer> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(cursor.getInt(0));
        }

        return res;
    }

    public SingleEvent getEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(EVENT_TABLE_NAME, new String[] { COLUMN_EVENT_ID, COLUMN_EVENT_NAME,
                COLUMN_EVENT_ADDRESS, COLUMN_EVENT_ARTIST, COLUMN_EVENT_PIC, COLUMN_EVENT_TYPE,
                COLUMN_EVENT_DATE, COLUMN_EVENT_START, COLUMN_EVENT_END}, COLUMN_EVENT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        SingleEvent singleEvent = new SingleEvent();

        singleEvent.setID(id);
        singleEvent.setName(cursor.getString(1));
        singleEvent.setAddress(cursor.getString(2));
        singleEvent.setArtist(cursor.getString(3));
        singleEvent.setType(cursor.getString(5));
        singleEvent.setDate(cursor.getInt(6));
        singleEvent.setStart(cursor.getString(7));
        singleEvent.setEnd(cursor.getString(8));

        byte[] image = cursor.getBlob(4);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        singleEvent.setPic(theImage);

        return singleEvent;
    }

    public SingleArtist getArtist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ARTIST_TABLE_NAME, new String[] { COLUMN_ARTIST_ID, COLUMN_ARTIST_NAME,
                COLUMN_ARTIST_PIC, COLUMN_ARTIST_COUNTRY, COLUMN_ARTIST_WEBSITE, COLUMN_ARTIST_BIOS},
                COLUMN_ARTIST_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        SingleArtist singleArtist = new SingleArtist();

        singleArtist.setId(id);
        singleArtist.setName(cursor.getString(1));
        singleArtist.setCountry(cursor.getString(3));
        singleArtist.setWebsite(cursor.getString(4));
        singleArtist.setBios(cursor.getString(5));

        byte[] image = cursor.getBlob(2);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        singleArtist.setPic(theImage);

        return singleArtist;
    }
}
