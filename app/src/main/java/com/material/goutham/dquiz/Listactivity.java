package com.material.goutham.dquiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Listactivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    String[] mobileArray = {"Quiz  1","Quiz  2","Quiz  3","Quiz  4","Quiz  5","Quiz  6","Quiz  7","Quiz  8","Quiz  9","Quiz  10","Quiz  11"};
   private ArrayList<String> names = new ArrayList<>();
    private Toolbar toolbar;
    // json array response url
    private String urlJsonArry = "http://iitjeeorganic.com/halfwaydown/android/api/get_free_list.php";//two main things
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private JSONArray jsonResponse;
   public  Toast toast;
     int[] ids = new int[1000];
     int[] totalqs = new int[1000];
    private ProgressBar progressBar;
    boolean flag=false;
    protected  int timeout  = 5;
    //String uri="http://edufb.esy.es/q/new.php?id=X";
    String uri="http://iitjeeorganic.com/halfwaydown/android/api/freequiz.php?id=X";
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout ll,linearLayout;
    Button retry;

    private static String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listactivity);
       ll = (LinearLayout)findViewById(R.id.ll);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        linearLayout = (LinearLayout)findViewById(R.id.error);
        retry = (Button)findViewById(R.id.retry);

        retry.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout.setVisibility(View.GONE);
                makeJsonArrayRequest();

            }
        });



        Snackbar.make(ll, "Fetching Quizes from Server....", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.lightblue))//imp
                .setDuration(3000).show();

        //------------------------------------------

        toolbar=(Toolbar)findViewById(R.id.app_bar2);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal)));
        actionBar.setTitle("Select Quiz -");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
            }
        });//th
        listView = (ListView) findViewById(R.id.mobile_list);
        //--------------------------------------------

        makeJsonArrayRequest();

        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //---------------------swipe to refresh----------------------------------

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override public void onRefresh() {

                refreshItems();
            }


        });




    }

    private void settoast() {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toastinternet,
                (ViewGroup) findViewById(R.id.relativeLayout1));

         toast = new Toast(this);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
       //toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_listactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeJsonArrayRequest() {
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                             flag= true;
                            jsonResponse = response;
                            progressBar.setVisibility(View.INVISIBLE);
                           setdata(jsonResponse);

                            //-----------------------toast---------------------
                            Snackbar.make(ll, "Refreshed", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(R.color.lightblue))//imp
                                    .setDuration(3000).show();

                            /*View vieew = toast.getView();
                            //  vieew.setBackgroundColor(Color.parseColor("#BD8BDC"));
                            vieew.setBackgroundResource(R.drawable.textinputborder);
                            toast.setView(vieew);
                            toast.show(); //This displays the toast for the specified lenght.
                              //-------------------custom toast------------------------------
                             */


                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(ll, "Error", Snackbar.LENGTH_LONG)
                                    .setAction("Refreshed", null)
                                    .setActionTextColor(getResources().getColor(R.color.lightblue))//imp
                                    .setDuration(3000).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Snackbar.make(ll, "Please check your internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Refreshed", null)
                        .setActionTextColor(getResources().getColor(R.color.lightblue))//imp
                        .setDuration(3000).show();
                progressBar.setVisibility(View.INVISIBLE);
                if(!flag)
                linearLayout.setVisibility(View.VISIBLE);
            }

        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                5,
                2));

        AppController.getInstance().addToRequestQueue(req);
    }

    public void setdata(JSONArray response)
    {
        names.clear();
        String name;
        int id ,totalq;//these arrays are local
        for (int i = 0; i < response.length(); i++) {//i is local , therefore,it will be rewritten
            //since i again starts from zero

            JSONObject person = null;
            try {
                person = (JSONObject) response .get(i);
                Log.d("obj",person.toString());

             name = person.getString("name");
             id  = person.getInt("id");
                totalq=person.getInt("totalq");

                names.add(name);
                Log.d("name",names.get(i).toString());
                ids[i]=id;
                totalqs[i]=totalq;
                Log.d("name",Integer.toString(ids[i]));


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }


        adapter = new ArrayAdapter<>(this, R.layout.activity_listview, names);
        listView.setAdapter(adapter);//names-->listview thats it
        listView.setOnItemClickListener(this);

    }

    void refreshItems() {
        // Load items
        // ...
        linearLayout.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);//this should be false for roatation
            }
        }, 5000);

        swipeRefreshLayout.setEnabled(true);//enabled here
        makeJsonArrayRequest();//updates the adapter in the end this is the last thing that is does

        // Load complete

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...just finish the work
     try {
         adapter.notifyDataSetChanged();//notify data set changed changed done X1000
     }
     catch (Exception e)
     {
         if(names!=null) {

         }
         else
         {
             Toast.makeText(getApplicationContext(),
                     " Server down!! ",
                     Toast.LENGTH_LONG).show();
         }
     }
        // Stop refresh animation
        finally {
         swipeRefreshLayout.setRefreshing(true);//this should be true for rotation to stop
     }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,MainActivity.class);
        Bundle b;
        //int quizid = position+1;//general
        uri=uri.replace("X",Integer.toString(ids[position]));//ids and totalqs are auto refreshed when a new request is made
        intent.putExtra("totalq",Integer.toString(totalqs[position]));
        intent.putExtra( "URI",uri);
        intent.putExtra("quizname",names.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {


            finish();
            moveTaskToBack(true);
            return true;//i1
        }
        return super.onKeyDown(keyCode, event);
    }

}
//he just needs to change the premiumquizlistDB