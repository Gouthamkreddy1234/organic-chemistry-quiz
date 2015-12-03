package com.material.goutham.dquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import at.markushi.ui.CircleButton;


public class AboutDialog extends Activity {

    TextView tv,score;
    CircleButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setsingleline
        setContentView(R.layout.activity_about_dialog);
        button=(CircleButton)findViewById(R.id.but);
        String total = getIntent().getStringExtra("key");
        int tot= Integer.parseInt(total);

        tv=(TextView)findViewById(R.id.fulltv);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/hand2.ttf");
        tv.setTypeface(myTypeface);

        score=(TextView)findViewById(R.id.score);
       // tv.setText( R.string.fulltv );
        score.setText(total.toString());
        tv.append("  "+" \n ");

        if(tot<0)
        {
            tv.append("\n Better luck next time...!!! \n");
        }
        tv.append(" Click below to Proceed:");//inside and check

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//help-->latest changes110
                System.exit(0);//help-->latest changes110 code
                Intent intent = new Intent(getApplicationContext(),Listactivity.class);// inside!!
                startActivity(intent);
            }
        });
    }


}
