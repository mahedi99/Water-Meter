package com.example.rexhe.network;

/**
 * Created by rexhe on 3/8/2016.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.rexhe.watermeter.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by hemel007 on 9/10/2015.
 */
public class GetData extends AsyncTask<String, String, JSONArray> {


    JSONArray jsonArr;
    String rawUrlData;
    MainActivity mainActivity;
    int id=0;
    private String finalURL="", url1, url2;
    String url;
    private int userId=0;


    private HashMap<String, String> userInfo;

    private static final String testUrl="http://192.168.0.102/multiple_user.php?";
    private static final String TAG_URL = "http://lab366.com/water_meter/water_meter_deviceinfo.php?";
    private static final String TAG_USER_ID = "userid=";

    private boolean internetConnection = true;
    private boolean jsonException = false;

    public GetData(MainActivity mainActivity, int userId)
    {

        this.mainActivity = mainActivity;
        this.userId = userId;

        finalURL = finalURL.concat(TAG_URL);
        finalURL = finalURL.concat(TAG_USER_ID);
        finalURL = finalURL.concat(String.valueOf(10));

        Log.v("URL-----> ", finalURL);

    }

    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected JSONArray doInBackground(String... params) {

        String JsonString = getServiceData(finalURL);
        Log.v("Result-1 : ", String.valueOf(JsonString));

        if (JsonString != null) {
            try {
                jsonArr = new JSONArray(JsonString);
                Log.v("Result-2 : ", String.valueOf(jsonArr));
                id=1;

            } catch (JSONException e){
                jsonException = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return jsonArr;
    }



    protected void onPostExecute(JSONArray result) {

        if ( internetConnection==false)
        {
            Log.v("Result ", "Failed To Connect");
            mainActivity.alertDialog("No internet!");
        }
        else if (jsonException == true){
            mainActivity.alertDialog("Data not found!");
        }
        else if (id==-1 || id==0){
            mainActivity.alertDialog("Unknown error!");
        }
        else {

            //result(result);
            mainActivity.resultPackage(result);
        }

    }

    public String getServiceData(String urlLink){


        StringBuffer BufferData = new StringBuffer("");

        try {
            URL url = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null){
                BufferData.append(line);
            }

            rawUrlData = BufferData.toString();

        } catch (FileNotFoundException e) {
            mainActivity.alertDialog("Server Not found");
        } catch (Exception e) {
            internetConnection = false;
        }
        return  rawUrlData;
    }

    public void result(JSONArray jsonArray) {
        /*userInfo = new HashMap<String, String>();

        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject resultObj = jsonArray.getJSONObject(0);
                userInfo.put("user_id", resultObj.getString("uniq_id"));
                userInfo.put("total_amount_water", resultObj.getString("total_amount_water"));
                userInfo.put("bill", resultObj.getString("bill"));
            }


        } catch (Exception e)
        {
            Log.v("Json Exception ",String.valueOf(e));
        }*/
    }

}

