package com.material.goutham.dquiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import at.markushi.ui.CircleButton;


public class AboutDialog extends Activity {

    TextView score,max,avg,totalpart,share,rank;
    CircleButton button;
    ProgressDialog progress;
    String link ="https://goo.gl/FVBBni";
    String url ="http://iitjeeorganic.com/QuizApi.php?quizName=X&marks=Y";
    String quizname,total;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dialog);

        ll= (LinearLayout)findViewById(R.id.ll3);

       //get intent data via bundle
        quizname= getIntent().getStringExtra("quizname");
        total = getIntent().getStringExtra("key");
        //int tot= Integer.parseInt(total);

        //set dialog
        setdialog();
         progress.show();

        //get data is done below--done
        button=(CircleButton)findViewById(R.id.but);
        max=(TextView)findViewById(R.id.max);
        avg=(TextView)findViewById(R.id.avg);
        totalpart=(TextView)findViewById(R.id.totalpart);
        rank=(TextView)findViewById(R.id.rank);
        share=(TextView)findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Hey,I just gave a quiz on "+quizname+" on IITJEEORGANIC QUIZ app.Download it here \n"+link);//add score also here
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "shared successfully!!", Toast.LENGTH_LONG);
                }


            }
        });


        SpannableString content = new SpannableString("share quiz on whatsapp");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        share.setText(content);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/hand2.ttf");
        share.setTypeface(myTypeface);
        share.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        score=(TextView)findViewById(R.id.score);
       // tv.setText( R.string.fulltv );
        score.setText(total.toString());
        share.append("  "+" \n ");

       /* if(tot<0)
        {
            tv.append("\n Better luck next time...!!! \n");
        }
        tv.append(" Click below to Proceed:");//inside and check*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//help-->latest changes110
                System.exit(0);//help-->latest changes110 code
                Intent intent = new Intent(getApplicationContext(),Listactivity.class);// inside!!
                startActivity(intent);
            }
        });



       url= url.replace("X",quizname);
       url= url.replace("Y",total);
        url=url.replace(" ","%20");
        Log.d("url",url);

        getData(url);




    }

    private void getData(String url2) {
        //make request here//i can even do that easily

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                url2, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("response2", response.toString());

                progress.dismiss();
                setData(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error!", "Error: " + error.getMessage());
                Snackbar.make(ll, "Please check your internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Refreshed", null)
                        .setActionTextColor(getResources().getColor(R.color.lightblue))//imp
                        .setDuration(3000).show();
                progress.dismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                5,
                2));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "aboutdialog");


    }

    private void setData(JSONObject object)
    {
        //first parse  the object and append the text views
        //set the average text and max text
        try {
            int Rank = object.getInt("rank");
            String averageMarks = object.getString("averageMarks");
            int totalParticipants = object.getInt("totalParticipants");
            String maxMarks = object.getString("maxMarks");

            avg.append(averageMarks);
            max.append(maxMarks);
            totalpart.append(Integer.toString(totalParticipants));
            rank.append(Integer.toString(Rank));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setdialog() {

        progress=new ProgressDialog(this);
        progress.setMessage("Fetching data...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);


    }


}
