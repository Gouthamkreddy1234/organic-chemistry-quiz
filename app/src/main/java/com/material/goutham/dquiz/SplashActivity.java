package com.material.goutham.dquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Gautam on 6/10/2015.
 */
public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/agb.ttf");
        TextView myTextView = (TextView)findViewById(R.id.tv);
        myTextView.setTypeface(myTypeface);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, Listactivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
