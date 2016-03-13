package com.material.goutham.dquiz;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.markushi.ui.CircleButton;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private String url ="http://iitjeeorganic.com/halfwaydown/android/api/freequiz.php?id=4";//url
    private String TAG ="Quiz JSON";
    private TextView question,timer;
    ProgressBar progressBar;
    private RadioButton t1,t2,t3,t4;
    private ImageView v1,v2,v3,v4,questionimage,questionimage2,questionimage3,questionimage4;

    private  ImageView[] qiarr;
  int flag=0,y=0;
   // private Button next;
    private int c=0,t=0,n=0;
    //private JSONObject jsonObject;
    protected  static int timeout  = 5;
    private JSONArray jsonArray;
    //private JSONArray jsonObject;
    private int uniqueid=0;
    private String result,r2,srcq;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final Pattern TAG_REGEX = Pattern.compile("<(.+?)>");

    private String res_a,a2,srcq_A;
    private String res_b,b2,srcq_B;
    private String res_c,c2,srcq_C;
    private String res_d,d2,srcq_D;
    int i=61,k=0,correct,total;
    int length;
    private CircleButton button;
    private Toolbar toolbar;
    private ImageLoader imageLoader;
    private FloatingActionButton next;
    public  MyCountDownTimer countDownTimer=new MyCountDownTimer(61000,1000);//one object only;

    private TextView cor,inc,tot;
    String totalq,quizname;
    LinearLayout ll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       k=0;
        setContentView(R.layout.activity_main);
        progressBar=(ProgressBar)findViewById(R.id.progresbar);
        progressBar.setVisibility(View.VISIBLE);
        //-----------------------------------------
        //nothing!!
        ObservableScrollView scrollview = (ObservableScrollView) findViewById(R.id.scrollview);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.next);
        fab.attachToScrollView(scrollview);
        ll= (LinearLayout)findViewById(R.id.ll2);


       quizname= getIntent().getStringExtra("quizname");
        url = getIntent().getStringExtra("URI");
        totalq = getIntent().getStringExtra("totalq");

        //------------------------------------------

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal)));
        actionBar.setTitle("ORGANIC CHEMISTRY ");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//PB in Gap-by default given//do something

            }
        });
        //--------------------------------------------

      /*  ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));*/


        //-------------------------------------------------------------


        imageLoader = AppController.getInstance().getImageLoader();


        timer = (TextView) findViewById(R.id.timer);
        cor = (TextView) findViewById(R.id.tcorrect);
        inc = (TextView) findViewById(R.id.incorrect);
        tot = (TextView) findViewById(R.id.tot);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/pie.ttf");
       /* cor.setTypeface(myTypeface);
        inc.setTypeface(myTypeface);
        tot.setTypeface(myTypeface);*/

        t1 = (RadioButton) findViewById(R.id.t1);
        t2 = (RadioButton) findViewById(R.id.t2);
        t3 = (RadioButton) findViewById(R.id.t3);
        t4 = (RadioButton) findViewById(R.id.t4);
        question = (TextView) findViewById(R.id.question);

        v1 = (ImageView) findViewById(R.id.v1);
        v2 = (ImageView) findViewById(R.id.v2);
        v3 = (ImageView) findViewById(R.id.v3);
        v4 = (ImageView) findViewById(R.id.v4);
        questionimage = (ImageView) findViewById(R.id.questionimage);
        questionimage2 = (ImageView) findViewById(R.id.questionimage2);
        questionimage3 = (ImageView) findViewById(R.id.questionimage3);
        questionimage4 = (ImageView) findViewById(R.id.questionimage4);

        qiarr = new ImageView[]{questionimage, questionimage2, questionimage3, questionimage4};


        next = (FloatingActionButton) findViewById(R.id.next);
        next.setOnClickListener(this);

        t1.setOnCheckedChangeListener(this);
        t2.setOnCheckedChangeListener(this);
        t3.setOnCheckedChangeListener(this);
        t4.setOnCheckedChangeListener(this);


        AlertDialog.Builder builder1;

        //AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder1 = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);//theme problem
        } else {
            builder1 = new AlertDialog.Builder(this);
        }
        builder1.setTitle("Test Information");
        builder1.setMessage("This test would have " + totalq + " questions and you would get 1 minute for each question. All the best! ;)");
        builder1.setCancelable(true);//change
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        getdata();
                    }
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    getdata();
                    Log.d("ondismiss","dismissed");
                }
            });
        }
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }




    public void getdata()
    {
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        jsonArray=response;
                        displayData(k);
                        length=jsonArray.length();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (AppStatus.getInstance(getBaseContext()).isOnline()) {

                    //Toast t = Toast.makeText(getBaseContext(),"You are online!!!!",8000).show();
                    //while im connected to the internet but my app isn't responding

                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                            --timeout;
                            Log.d("timeout_count",Integer.toString(timeout));
                            if(timeout <=0)
                            {
                                getdata();
                            }
                            else
                            {
                                Snackbar.make(ll, "Connection too slow!", Snackbar.LENGTH_LONG)
                                        .setAction("Refreshed", null)
                                        .setActionTextColor(getResources().getColor(R.color.lightblue))//imp
                                        .setDuration(3000).show();
                            }

                        }
                    });



                } else {

                    Snackbar.make(ll, "Please check your internet connection!", Snackbar.LENGTH_LONG)
                            .setAction("Refreshed", null)
                            .setActionTextColor(getResources().getColor(R.color.lightblue))//imp
                            .setDuration(3000).show();
                    Log.v("Home", "############################You are not online!!!!");
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });


        //--------------------------------------------------------------------
        AppController.getInstance().addToRequestQueue(req, "feeds");


    }

    public void displayData(int id)
    {

        resetimageviews();
        JSONObject temp= null;
        try {
             temp = (JSONObject)jsonArray.get(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // response will be a json object
        try {
            String a = temp.getString("optiona");
            String b = temp.getString("optionb");
            //JSONObject phone = jsonObject.getJSONObject("phone");
            String c = temp.getString("optionc");
            String d = temp.getString("optiond");
             correct  = temp.getInt("right");

            String q =temp.getString("question");

            Log.d("Question",q);
            Log.d("optiona",a);
            Log.d("optionb",b);
            Log.d("optionc",c);
            Log.d("optiond",d);

            q=filter(q);
           // q=q.replaceAll("\\s+","");
            q=q.trim();

            //-------question----------------------//remove this before submitting the code
            if(q.contains("<img"))//question can have two images
            {

                /* String[] res = pattern.split(st);
                Log.d("regex",Arrays.toString(res));*/
                 String str = q;

                List<String> stockList = new ArrayList<String>();
                stockList=getTagValues(str);
                String [] stockArr = stockList.toArray(new String[stockList.size()]);

                r2=q;
                for(int t=0;t<stockArr.length;t++) {
                    Log.d("LINKS", stockArr[t]);
                    stockArr[t]="<"+stockArr[t]+">";
                    r2=r2.replace(stockArr[t], "");
                    srcq = stockArr[t].substring(stockArr[t].indexOf("src") + 5, stockArr[t].indexOf("png") +3);
                    srcq="http://iitjeeorganic.com/halfwaydown/android/api/"+srcq;
                    imageLoader.get(srcq, ImageLoader.getImageListener(
                            qiarr[t], R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha));
                }

            }
            else
            {
                r2=q;
            }

            //-----------------------------------------------

            a=filter(a);
            a=a.trim();

            if(a.contains("<img"))
            {
                res_a = a.substring(a.indexOf("<img") , a.indexOf("png")+5);
                a2=a.replace(res_a, "");
                srcq_A = a.substring(a.indexOf("src") + 5, a.indexOf("png") +3);//3 is perfect
                //cuz the last one isnt taken thats the logic
                Log.d("res_a",res_a);
                Log.d("src_a",srcq_A);
                srcq_A="http://iitjeeorganic.com/halfwaydown/android/api/"+srcq_A;
                Log.d("srcq_a",srcq_A);

                // Loading image with placeholder and error image
                imageLoader.get(srcq_A, ImageLoader.getImageListener(
                        v1, R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha));

            }
            else
            {
                a2=a;
            }

            //-----------------------------------------------


            //-----------------------------------------------
            b=filter(b);
            b=b.trim();

            if(b.contains("<img"))
            {
                res_b = b.substring(b.indexOf("<img") , b.indexOf("png")+5);
                b2=b.replace(res_b, "  ");
                srcq_B = b.substring(b.indexOf("src") + 5, b.indexOf("png") +3);
                Log.d("res_b",res_b);
                Log.d("src_b",srcq_B);
                srcq_B="http://iitjeeorganic.com/halfwaydown/android/api/"+srcq_B;
                Log.d("srcq_b",srcq_B);

                // Loading image with placeholder and error image
                imageLoader.get(srcq_B, ImageLoader.getImageListener(
                        v2, R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha));

            }
            else
            {
                b2=b;
            }

            //-----------------------------------------------

            //-----------------------------------------------
               c=filter(c);
            c=c.trim();

            if(c.contains("<img"))
            {
                res_c = c.substring(c.indexOf("<img") , c.indexOf("png")+5);
                c2=c.replace(res_c, "");
                srcq_C = c.substring(c.indexOf("src") + 5, c.indexOf("png") +3);
                 Log.d("res_c",res_c);
                Log.d("src_c",srcq_C);
                srcq_C="http://iitjeeorganic.com/halfwaydown/android/api/"+srcq_C;
                Log.d("srcq_c",srcq_C);

                // Loading image with placeholder and error image
                imageLoader.get(srcq_C, ImageLoader.getImageListener(
                        v3, R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha));

            }
            else
            {
                c2=c;
            }

            //-----------------------------------------------

            //-----------------------------------------------

            d=filter(d);
            d=d.trim();

            if(d.contains("<img"))
            {
                res_d = d.substring(d.indexOf("<img") , d.indexOf("png")+5);
                d2=d.replace(res_d, "");
                srcq_D = d.substring(d.indexOf("src") + 5, d.indexOf("png") +3 );
                Log.d("res_d",res_d);
                Log.d("src_d",srcq_D);
                srcq_D="http://iitjeeorganic.com/halfwaydown/android/api/"+srcq_D;
                Log.d("srcq_d",srcq_D);

                // Loading image with placeholder and error image
                imageLoader.get(srcq_D, ImageLoader.getImageListener(
                        v4, R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha));

            }
            else
            {
                d2=d;
            }

            //-----------------------------------------------
                  //final
            Log.d("a2",a2);

            //if()
            t1.setText(a2);
            t2.setText(b2);
            t3.setText(c2);
            t4.setText(d2);
            question.setText(r2);
            progressBar.setVisibility(View.INVISIBLE);
            countDownTimer.start();//start it here


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private String filter(String t) {

        if(t.contains("</br>"))
        {
            t=t.replace("</br>","");//back in q
        }

        if(t.contains("<br>"))
        {
            t=t.replace("<br>","");//back in q
        }

        if(t.contains("<sup>"))
        {
            t=t.replace("<sup>","");//back in q
        }
        if(t.contains("</sup>"))
        {
            t=t.replace("</sup>","");//back in q
        }
        if(t.contains("<sub>"))
        {
            t=t.replace("<sub>","");//back in q
        }
        if(t.contains("</sub>"))
        {
            t=t.replace("</sub>","");//back in q
        }
        return t;
    }

    private static List<String> getTagValues(final String str) {//imp
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = TAG_REGEX.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues;
    }

    public void  resetimageviews()
    {
        v1.setImageBitmap(null);
        v2.setImageBitmap(null);
        v3.setImageBitmap(null);
        v4.setImageBitmap(null);
        questionimage.setImageBitmap(null);
        questionimage2.setImageBitmap(null);
        questionimage3.setImageBitmap(null);
        t1.setChecked(false);
        t2.setChecked(false);
        t3.setChecked(false);
        t4.setChecked(false);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if(id==R.id.aboutus)
        {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Test Information");
            builder1.setMessage("Organic Chemistry quiz is an Interactive Quiz App for Testing your Knowledge in Organic chemistry. There are so many categories and each category will have a bunch of exciting questions .Each Quiz will have variable number questions and you will have only 60 seconds to answer each question. Each correct answer carries 4 points and a wrong answer will have negative marking of 1 point.Click on the OK button to continue to the Quiz!!");
            builder1.setCancelable(true);
            builder1.setNeutralButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()) {
                case R.id.next:

                    countDownTimer.cancel();
                   //no choice
                    i = 61;

                    if (t1.isChecked() && correct == 1) {
                        total++;
                        cor.setText("Correct:" + Integer.toString(++c));
                        t = t + 4;//t will be incr only is something is checked
                        tot.setText("Total:" + Integer.toString(t));
                    } else if (t2.isChecked() && correct == 2) {
                        total++;
                        cor.setText("Correct:" + Integer.toString(++c));
                        t = t + 4;
                        tot.setText("Total:" + Integer.toString(t));
                    } else if (t3.isChecked() && correct == 3) {
                        total++;
                        cor.setText("Correct:" + Integer.toString(++c));
                        t = t + 4;
                        tot.setText("Total:" + Integer.toString(t));
                    } else if (t4.isChecked() && correct == 4) {
                        total++;
                        cor.setText("Correct:" + Integer.toString(++c));
                        t = t + 4;
                        tot.setText("Total:" + Integer.toString(t));
                    }
                    else if(t1.isChecked()||t2.isChecked()||t3.isChecked()||t4.isChecked())
                    {
                        total++;
                        inc.setText("Incorrect:" + Integer.toString(++n));
                        t = t - 1;
                        tot.setText("Total:" + Integer.toString(t));
                    }
                    if (k != (length -1)) {
                        k++;
                    } else {

                        finish();//works!!//perfect!-current activity
                        Intent intent = new Intent(getApplicationContext(), AboutDialog.class);
                        intent.putExtra("quizname",quizname);
                        intent.putExtra("key",Integer.toString(t));
                        startActivity(intent);
                    }


                    Log.d("total", Integer.toString(total));
                    displayData(k);
                    break;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("ALERT!!");
            builder1.setMessage("Do you want to abort the quiz?.");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            finish();
                            Intent intent = new Intent(getApplicationContext(),Listactivity.class);// inside!!
                            startActivity(intent);


                        }
                    });
            builder1.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                           // onResume();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch(buttonView.getId())
        {
            case R.id.t1:
                if(isChecked)
                {
                    t2.setChecked(false);
                    t3.setChecked(false);
                    t4.setChecked(false);
                }
                break;

            case R.id.t2:
                if(isChecked)
                {
                    t4.setChecked(false);
                    t3.setChecked(false);
                    t1.setChecked(false);
                }
                break;

            case R.id.t3:
                if(isChecked)
                {
                    t2.setChecked(false);
                    t1.setChecked(false);
                    t4.setChecked(false);
                }
                break;

            case R.id.t4:
                if(isChecked)
                {
                    t2.setChecked(false);
                    t3.setChecked(false);
                    t1.setChecked(false);
                }
                break;


        }

    }
    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            Log.e("Times up", "Times up");

            countDownTimer.cancel();
            i=61;
            if (t1.isChecked() && correct == 1) {
                total++;
                cor.setText("Correct : " + Integer.toString(++c));
                t = t + 4;
                tot.setText("Total : " + Integer.toString(t));
            } else if (t2.isChecked() && correct == 2) {
                total++;
                cor.setText("Correct : " + Integer.toString(++c));
                t = t + 4;
                tot.setText("Total : " + Integer.toString(t));
            } else if (t3.isChecked() && correct == 3) {
                total++;
                cor.setText("Correct : " + Integer.toString(++c));
                t = t + 4;
                tot.setText("Total : " + Integer.toString(t));
            } else if (t4.isChecked() && correct == 4) {
                total++;
                cor.setText("Correct : " + Integer.toString(++c));
                t = t + 4;
                tot.setText("Total : " + Integer.toString(t));
            }
            else if(t1.isChecked()||t2.isChecked()||t3.isChecked()||t4.isChecked())
            {
                total++;
                inc.setText("Incorrect : " + Integer.toString(++n));
                t = t - 1;
                tot.setText("Total : " + Integer.toString(t));
            }


            if (k != (length -1)) {
                k++;
                Log.d("total", Integer.toString(total));
                displayData(k);
            }


        }
        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText(Integer.toString(--i));

        }
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        Log.d("back button", "back button pressed");
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        builder1.setTitle("ALERT!!");
        builder1.setMessage("Do you want to abort the quiz?.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                          finish();
                        Intent intent = new Intent(getApplicationContext(),Listactivity.class);// inside!!
                        startActivity(intent);


                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // onResume();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }




}
//Learn how to use styles.xml
//learn fragments -- >before 9 o' clock
//handlers and threads


