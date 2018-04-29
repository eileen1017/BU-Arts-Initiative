package com.cs591.mooncake.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private static final String COLUMN_EVENT_VENUE = "Venue";
    private static final String COLUMN_EVENT_BUILDING = "Building";
    private static final String COLUMN_EVENT_LEVEL = "Level";

    private static final String ARTIST_TABLE_NAME = "Artist";
    private static final String COLUMN_ARTIST_ID = "ID";
    private static final String COLUMN_ARTIST_NAME = "Name";
    private static final String COLUMN_ARTIST_BIOS = "Bios";
    private static final String COLUMN_ARTIST_PIC = "Pic";
    private static final String COLUMN_ARTIST_WEBSITE = "Website";
    private static final String COLUMN_ARTIST_COUNTRY = "Country";

    private static final String PROFILE_TABLE_NAME = "Profile";
    private static final String COLUMN_PROFILE_ID = "ID";
    private static final String COLUMN_PROFILE_USERNAME = "Username";
    private static final String COLUMN_PROFILE_UID = "UID";
    private static final String COLUMN_PROFILE_SCHEDULED = "Scheduled";
    private static final String COLUMN_PROFILE_LIKED = "Liked";
    private static final String COLUMN_PROFILE_PIC = "Pic";
    private static final String COLUMN_PROFILE_PIC_URI = "PicURI";


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

        Cursor cursor = db.rawQuery("select * from " + EVENT_TABLE_NAME , null);

        List<Integer> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(cursor.getInt(0));
        }

        cursor.close();
        return res;
    }

    public List<Integer> getArtistList() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + ARTIST_TABLE_NAME, null);

        List<Integer> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(cursor.getInt(0));
        }

        cursor.close();
        return res;
    }

    public SingleEvent getEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(EVENT_TABLE_NAME, new String[] { COLUMN_EVENT_ID, COLUMN_EVENT_NAME,
                        COLUMN_EVENT_ADDRESS, COLUMN_EVENT_ARTIST, COLUMN_EVENT_PIC, COLUMN_EVENT_TYPE,
                        COLUMN_EVENT_DATE, COLUMN_EVENT_START, COLUMN_EVENT_END, COLUMN_EVENT_BUILDING,
                        COLUMN_EVENT_VENUE, COLUMN_EVENT_LEVEL}, COLUMN_EVENT_ID + "=?",
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
        singleEvent.setBuilding(cursor.getString(9));
        singleEvent.setVenue(cursor.getString(10));
        singleEvent.setLevel(cursor.getString(11));


        byte[] image = cursor.getBlob(4);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        singleEvent.setPic(theImage);

        cursor.close();
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

        cursor.close();
        return singleArtist;
    }

    public void addProfile(SingleUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROFILE_USERNAME, user.getUserName());
        cv.put(COLUMN_PROFILE_UID, user.getUID());
        cv.put(COLUMN_PROFILE_ID, 0);
        if (user.getPic() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            user.getPic().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            cv.put(COLUMN_PROFILE_PIC, byteArray);
        }
        cv.put(COLUMN_PROFILE_LIKED, user.getLikedString());
        cv.put(COLUMN_PROFILE_SCHEDULED, user.getScheduledString());
        if (user.getPicUrl() != null)
            cv.put(COLUMN_PROFILE_PIC_URI, user.getPicUrl().toString());

        db.update(PROFILE_TABLE_NAME, cv, COLUMN_PROFILE_ID+"="+0, null);
    }

    public void initProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.putNull(COLUMN_PROFILE_USERNAME);
        cv.putNull(COLUMN_PROFILE_UID);
        cv.putNull(COLUMN_PROFILE_SCHEDULED);
        cv.putNull(COLUMN_PROFILE_LIKED);
        cv.putNull(COLUMN_PROFILE_PIC);
        cv.putNull(COLUMN_PROFILE_PIC_URI);
        cv.put(COLUMN_PROFILE_ID, 0);

        db.update(PROFILE_TABLE_NAME, cv, COLUMN_PROFILE_ID+"="+0, null);
    }

    public SingleUser getProfile() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where" +
                " tbl_name = '"+PROFILE_TABLE_NAME+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
            } else {
                db.execSQL(" CREATE TABLE " + PROFILE_TABLE_NAME + " (" +
                                COLUMN_PROFILE_ID + " INTEGER, " +
                                COLUMN_PROFILE_UID + " TEXT, " +
                                COLUMN_PROFILE_USERNAME + " TEXT, " +
                                COLUMN_PROFILE_PIC + " BOLB, " +
                                COLUMN_PROFILE_SCHEDULED + " TEXT, " +
                                COLUMN_PROFILE_PIC_URI + " TEXT, " +
                                COLUMN_PROFILE_LIKED + " TEXT);");
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.putNull(COLUMN_PROFILE_USERNAME);
                cv.putNull(COLUMN_PROFILE_UID);
                cv.putNull(COLUMN_PROFILE_SCHEDULED);
                cv.putNull(COLUMN_PROFILE_LIKED);
                cv.putNull(COLUMN_PROFILE_PIC);
                cv.putNull(COLUMN_PROFILE_PIC_URI);
                cv.put(COLUMN_PROFILE_ID, 0);
                db.insert(PROFILE_TABLE_NAME, null, cv);
            }
        }



        cursor = db.query(PROFILE_TABLE_NAME, new String[] { COLUMN_PROFILE_UID, COLUMN_PROFILE_USERNAME,
                        COLUMN_PROFILE_PIC, COLUMN_PROFILE_LIKED, COLUMN_PROFILE_SCHEDULED, COLUMN_PROFILE_PIC_URI},
                COLUMN_PROFILE_ID + "=?",
                new String[] { String.valueOf(0) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        SingleUser singleUser = new SingleUser();

        singleUser.setUID(cursor.getString(0));
        singleUser.setUserName(cursor.getString(1));
        if (cursor.getBlob(2) != null) {
            byte[] image = cursor.getBlob(2);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            singleUser.setPic(theImage);
        }

        if (cursor.getString(3) != null) {
            singleUser.setLikedByString(cursor.getString(3));
        }

        if (cursor.getString(4) != null ) {
            singleUser.setScheduledByString(cursor.getString(4));
        }

        if (cursor.getString(5) != null) {
            singleUser.setPicUrl(Uri.parse(cursor.getString(5)));
        }

        cursor.close();
        return singleUser;
    }
}
