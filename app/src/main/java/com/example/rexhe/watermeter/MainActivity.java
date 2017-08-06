package com.example.rexhe.watermeter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rexhe.network.GetData;
import com.example.rexhe.utils.Preferences;
import com.github.glomadrian.velocimeterlibrary.VelocimeterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{


    private VelocimeterView velocimeter;
    private GetData getData;
    private LinearLayout mainLinearLayout;
    private SwipeRefreshLayout swipeContainer;
    private TextView dayDigit;
    private TextView monthDigit;
    private ActionBar actionBar;
    private HashMap<String, String> userInfo;
    private List<HashMap<String, String>> infoList;

    private double l_hour;
    private int currentFlowFreq=0;
    private float dailyConsumedWater=0;
    private float monthlyConsumedWater=0;
    private float bill=0;
    private int litre=0;
    private Bundle bundle;

    public static final String MY_PREFS_FILE = "loginInfo";
    public static final String MY_PREFS_COLOR = "mycolor";

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setBackgroundColor(Preferences.getInstance().getToolbarColor());


        initialize();
        getUpdatedData();
        //startCountAnimation(78.6f,999.7f);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpdatedData();

                //simulate doing something
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                        //Toast.makeText(getApplicationContext(), "Up-To-Date", Toast.LENGTH_LONG).show();
                    }

                }, 2000);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Preferences.getInstance().getToolbarColor());
    }

    public void initialize()
    {
        mainLinearLayout = (LinearLayout) findViewById(R.id.main_layout);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeContainer.setColorSchemeResources(
                R.color.green,
                R.color.orange
        );
        velocimeter = (VelocimeterView) findViewById(R.id.velocimeter);
        dayDigit= (TextView) findViewById(R.id.dayDigit);
        monthDigit= (TextView) findViewById(R.id.monthDigit);
        bundle= new Bundle();
        //velocimeter.setValue(50, true);

    }
    public void getUpdatedData()
    {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask asynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getData = new GetData(MainActivity.this, 1);
                            getData.execute();

                        }catch (Exception e)
                        {

                        }
                    }
                });
            }
        };timer.schedule(asynchronousTask, 0, 15000);
    }

            //Dynamic Number Showing
    private void startCountAnimation(int dayValue, int monthValue) {
        ValueAnimator animator1 = new ValueAnimator();
        ValueAnimator animator2 = new ValueAnimator();

        animator1.setObjectValues(0, dayValue);
        animator1.setDuration(3000);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dayDigit.setText("" + (int) animation.getAnimatedValue());
                //velocimeter.setValue((int) animation.getAnimatedValue(), true);
            }
        });

        animator2.setObjectValues(0, monthValue);
        animator2.setDuration(3000);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                monthDigit.setText("" + (int) animation.getAnimatedValue());
                //velocimeter.setValue((int) animation.getAnimatedValue(), true);
            }
        });
        animator1.start();
        animator2.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.logout)
        {

            SharedPreferences shared = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
            if (shared!=null) {
                String email = (shared.getString("email", ""));
                String password = (shared.getString("password",""));
                Toast.makeText(getApplicationContext(), "Logged-Out Successfully" , Toast.LENGTH_LONG).show();
            }

            //delete data
            Context context=MainActivity.this;
            SharedPreferences myPrefs = context.getSharedPreferences(MY_PREFS_FILE,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);

            return true;
        }
        if (id == R.id.settings)
        {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            finish();
            startActivity(intent);
            return true;
        }
        if (id ==R.id.payment)
        {
            bundle.putDouble("flow_rate",l_hour);
            bundle.putFloat("daily", dailyConsumedWater);
            bundle.putFloat("monthly", monthlyConsumedWater);
            bundle.putFloat("bill",bill);
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        if (id ==R.id.about)
        {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resultPackage(JSONArray jsonArray)
    {
        /*String userInfo1 = userInfo.get("user_info");
        String value = userInfo.get("value");
        velocimeter.setValue(Integer.valueOf(value), true);*/

        currentFlowFreq=0;
        dailyConsumedWater=0;
        monthlyConsumedWater=0;
        bill=0;
        litre=0;
        l_hour=0;

        Date date=new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate=dateFormat.format(date);


        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject resultObj = jsonArray.getJSONObject(i);

                monthlyConsumedWater = monthlyConsumedWater+Float.parseFloat(resultObj.getString("total_amount_water"));
                currentFlowFreq = resultObj.getInt("flow_freq");

                bill = bill+Float.parseFloat(resultObj.getString("bill"));

                if(formattedDate.equals(resultObj.getString("date")))
                {
                    dailyConsumedWater = dailyConsumedWater+Float.parseFloat(resultObj.getString("total_amount_water"));
                }
            }

            Log.v("Temp ", String.valueOf(bill));
            l_hour = (currentFlowFreq / 7.5); //Litre Per Second
            l_hour = l_hour/60;

            Log.v("Daily Consumed Water ", String.valueOf(dailyConsumedWater));
            Log.v("Monthly Consumed Water ", String.valueOf(monthlyConsumedWater));

            Log.v("l_hour ", String.valueOf(l_hour));

            startCountAnimation(Math.round(dailyConsumedWater), Math.round(monthlyConsumedWater));

        } catch (Exception e)
        {
            Log.v("Json Exception ", String.valueOf(e));
        }

        velocimeter.setValue((float) l_hour, true);
    }


    public void alertDialog(String message)
    {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        txtv.setTextColor(Color.CYAN);
    }

}
