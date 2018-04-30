package com.cs591.mooncake.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs591.mooncake.R;
import com.cs591.mooncake.SQLite.MySQLiteHelper;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yangzhuoshu on 4/12/18.
 */



public class ShowInfo extends AppCompatActivity{
    public final String TAG = "SUP";
    ImageView mImageView;
    public Button btnBase;
    public Button btn2ndFloor;
    MySQLiteHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String result = extras.getString("location");
        Log.w(TAG,result);

        if (result.equals("BU_GSU")){
            setContentView(R.layout.fragment_map_gsu_2ndfloor);

            btnBase = (Button) findViewById(R.id.btnGSUBase);
            btn2ndFloor = (Button)findViewById(R.id.btn2ndFloor);

            //zoom in and out feature
            mImageView = (ImageView) findViewById(R.id.gsu_2nfloor);
            PhotoViewAttacher photoView = new PhotoViewAttacher(mImageView);
            photoView.update();

            btn2ndFloor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageView.setImageResource(R.drawable.map_gsu_2ndfloor);
                }
            });

            btnBase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageView.setImageResource(R.drawable.map_gsu_basement);
                }
            });

        } else if (result.equals("BU_Tsai")){
            setContentView(R.layout.fragment_map_tsai);

            //zoom in and out feature
            mImageView = (ImageView) findViewById(R.id.tsai);
            PhotoViewAttacher photoView = new PhotoViewAttacher(mImageView);
            photoView.update();
        }


    }

}
