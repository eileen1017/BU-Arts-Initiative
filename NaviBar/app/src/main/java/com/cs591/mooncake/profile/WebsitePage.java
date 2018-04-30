package com.cs591.mooncake.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.cs591.mooncake.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class WebsitePage extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_page);
    }
}