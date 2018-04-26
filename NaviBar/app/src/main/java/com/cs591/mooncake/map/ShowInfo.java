package com.cs591.mooncake.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.style.BulletSpan;
import android.util.Log;
import android.widget.ImageView;

import com.cs591.mooncake.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yangzhuoshu on 4/12/18.
 */



public class ShowInfo extends AppCompatActivity{
    public final String TAG = "SUP";
    ImageView mImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String result = extras.getString("location");
        Log.w(TAG,result);

        if (result.equals("BU_GSU")){
            setContentView(R.layout.fragment_map_gsu_2ndfloor);

            //zoom in and out feature
            mImageView = (ImageView) findViewById(R.id.gsu_2nfloor);
            PhotoViewAttacher photoView = new PhotoViewAttacher(mImageView);
            photoView.update();

        } else if (result.equals("BU_Tsai")){
            setContentView(R.layout.fragment_map_tsai);

            //zoom in and out feature
            mImageView = (ImageView) findViewById(R.id.tsai);
            PhotoViewAttacher photoView = new PhotoViewAttacher(mImageView);
            photoView.update();
        }




//        String locaTitle;
//
//        if (savedInstanceState == null){
//
//            Bundle extras = getIntent().getExtras();
//            if (extras == null){
//                locaTitle = null;
//            } else {
//                locaTitle = extras.getString("location");
//            }
//        } else {
//            locaTitle = (String) savedInstanceState.getSerializable("location");
//
//            if (locaTitle.equals("BU_GSU")){
//                setContentView(R.layout.fragment_map_gsu_2ndfloor);
//            } else if (locaTitle.equals("BU_Tsai")){
//                setContentView(R.layout.fragment_map_tsai);
//            }
//        }
//
    }

}
