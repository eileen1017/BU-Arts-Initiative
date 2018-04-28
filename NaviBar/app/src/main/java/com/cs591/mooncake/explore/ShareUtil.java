package com.cs591.mooncake.explore;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;

import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;
import com.cs591.mooncake.SQLite.SingleEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanmingyang on 4/24/18.
 */

public class ShareUtil {

    private Context mContext;
    private String message;
    private String appUrl;


    public ShareUtil(Context mContext){
        this.mContext = mContext;
    }


    // method for Invite Friend in Profile page to share the url of our app to other apps
    public void inviteFriend() {
    //Send the Google Play Store url, it can make to our app's url after shipped on Google Play Store.
        appUrl = mContext.getString(R.string.appUrl);
        helper(appUrl);
    }


    // method for share button in each performance and workshop to share text with a hashtag to other apps
    public void share(int eventID) {
        // get SQLite DB
        final MySQLiteHelper mydb = new MySQLiteHelper(mContext);
        // get event ID
        final SingleEvent singleEvent = mydb.getEvent(eventID);
        message = mContext.getString(R.string.shareSms) + singleEvent.getName() + mContext.getString(R.string.hashTag);
        helper(message);
    }


    // helper function for sending message/url to other app, e.g. twitter, facebook, gmail, and sms.
    private void helper(String text) {

        // Create an array to store the apps (twitter and facebook) that the chooser will have.
        List<Intent> shareList = new ArrayList<Intent>();
        // The intent that the user will be selecting an app to perform.
        Intent sendIntent = new Intent();

        // ACTION_SEND: Deliver text content to other apps.
        sendIntent.setAction(Intent.ACTION_SEND);
        // Set the type to be plain text.
        sendIntent.setType("text/plain");

        // Get all the app infos.
        List<ResolveInfo> resInfos = mContext.getPackageManager().queryIntentActivities(sendIntent, 0);
        if(!resInfos.isEmpty()){
            // Check all the app's packageName to select from just twitter, facebook, gmail, and sms.
            for(ResolveInfo resInfo : resInfos){
                String pacName=resInfo.activityInfo.packageName;
                if(pacName.contains("com.twitter.android") || pacName.contains("com.facebook")
                        || pacName.contains("com.google.android.gm") || pacName.contains("messaging")) {

                    Intent intent = new Intent();
                    // Explicitly set the component to handle the intent.
                    intent.setComponent(new ComponentName(pacName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, text);

                    intent.setType("text/plain");
                    // Set the app package name that limits the components this intent will resolve to.
                    intent.setPackage(pacName);
                    shareList.add(intent);
                }
            }
            if(!shareList.isEmpty()){
                // Create the chooser every time the user clicks the button.
                Intent chooserIntent=Intent.createChooser(shareList.remove(0), mContext.getString(R.string.chooser));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareList.toArray(new Parcelable[]{}));
                mContext.startActivity(chooserIntent);
            }
        }
    }




}
