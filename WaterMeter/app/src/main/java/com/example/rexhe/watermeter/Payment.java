package com.example.rexhe.watermeter;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Payment extends AppCompatActivity {


    TextView flowRate;
    TextView daily;
    TextView monthly;
    TextView bill;

    Double flowRateValue;
    float dailyValue;
    float monthlyValue;
    float billValue;


    private ActionBar actionBar;
    public static final String MY_PREFS_COLOR = "mycolor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initialize();


        //Changing ActionBar Color
        actionBar=getSupportActionBar();
        SharedPreferences shared = getSharedPreferences(MY_PREFS_COLOR, MODE_PRIVATE);
        if (shared!=null) {
            int getColor = (shared.getInt("position", 0));
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor));
        }
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            flowRateValue = bundle.getDouble("flow_rate");
            dailyValue = bundle.getFloat("daily");
            monthlyValue = bundle.getFloat("monthly");
            billValue = bundle.getFloat("bill");
        }

        float abc =  Math.round(flowRateValue);
        DecimalFormat form = new DecimalFormat("0.00");
        flowRate.setText(String.valueOf(abc));
        daily.setText(form.format(dailyValue));
        monthly.setText(form.format(monthlyValue));
        bill.setText(form.format(billValue));

    }

    public void initialize()
    {
        flowRate= (TextView) findViewById(R.id.flowRate);
        daily= (TextView) findViewById(R.id.daily);
        monthly= (TextView) findViewById(R.id.monthly);
        bill= (TextView) findViewById(R.id.bill);
    }
}
