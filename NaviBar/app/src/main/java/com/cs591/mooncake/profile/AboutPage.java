package com.cs591.mooncake.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.cs591.mooncake.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AboutPage extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  Set content view as layout activity_about_page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
    }

}
