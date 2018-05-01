package com.cs591.mooncake.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.cs591.mooncake.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class FeedbackPage extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  Set content view as layout activity_feedback_page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);
    }
}
