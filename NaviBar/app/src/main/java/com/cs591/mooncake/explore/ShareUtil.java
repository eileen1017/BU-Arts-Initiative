package com.cs591.mooncake.explore;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanmingyang on 4/24/18.
 */

public class ShareUtil {

    private Context mContext;

    private String url;



    ShareUtil(Context mContext){
        this.mContext = mContext;
    }

    public void share() {
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
            // Check all the app's packageName to select from just twitter and facebook.
            for(ResolveInfo resInfo : resInfos){
                String pacName=resInfo.activityInfo.packageName;
                if(pacName.contains("com.twitter.android") || pacName.contains("com.facebook")
                        || pacName.contains("com.google.android.gm") || pacName.contains("messaging")) {
                    url = "https://www.bu.edu";
                    Intent intent = new Intent();
                    // Explicity set the component to handle the intent.
                    intent.setComponent(new ComponentName(pacName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    // Send the BU website for research; the url will be a specific event's url.
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.setType("text/plain");
                    // Set the app package name that limits the components this intent will resolve to.
                    intent.setPackage(pacName);
                    shareList.add(intent);
                }
            }
            if(!shareList.isEmpty()){
                // Create the chooser every time the user clicks the button.
                Intent chooserIntent=Intent.createChooser(shareList.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareList.toArray(new Parcelable[]{}));
                mContext.startActivity(chooserIntent);
            }
        }
    }




}
