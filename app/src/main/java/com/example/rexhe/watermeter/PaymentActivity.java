package com.example.rexhe.watermeter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.rexhe.utils.Preferences;

import java.text.DecimalFormat;

public class PaymentActivity extends AppCompatActivity {


    private TextView flowRate;
    private TextView daily;
    private TextView monthly;
    private TextView bill;

    private Double flowRateValue;
    private float dailyValue;
    private float monthlyValue;
    private float billValue;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initialize();


        //Changing ActionBar Color

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            flowRateValue = bundle.getDouble("flow_rate");
            dailyValue = bundle.getFloat("daily");
            monthlyValue = bundle.getFloat("monthly");
            billValue = bundle.getFloat("bill");
        }

        float abc =  Math.round(flowRateValue);
        DecimalFormat form = new DecimalFormat("0.00");
        flowRate.setText(String.format( "%.2f", abc ));
        daily.setText(form.format(dailyValue));
        monthly.setText(form.format(monthlyValue));
        bill.setText(form.format(billValue));

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Preferences.getInstance().getToolbarColor());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    public void initialize()
    {
        flowRate= (TextView) findViewById(R.id.flowRate);
        daily= (TextView) findViewById(R.id.daily);
        monthly= (TextView) findViewById(R.id.monthly);
        bill= (TextView) findViewById(R.id.bill);
    }
}
