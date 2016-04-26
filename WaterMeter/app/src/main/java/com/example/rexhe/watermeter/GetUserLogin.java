package com.example.rexhe.watermeter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.rexhe.watermeter.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rexhe on 12/29/2015.
 */
public class GetUserLogin extends AsyncTask<String,String,JSONArray> {


    private JSONArray jsonArr;
    private int id = 0;
    private boolean internetConnection=true;
    private final String requestURL = "http://lab366.com/water_meter/water_meter_logininfo.php";
    private HashMap<String,String> postDataParams;
    LoginActivity loginActivity;
    ProgressDialog progressDialog;
    private String authentication="false";


    GetUserLogin(LoginActivity loginActivity, ProgressDialog progressDialog, HashMap<String,String> hashMapInfo) {
        this.loginActivity=loginActivity;
        this.progressDialog=progressDialog;

        this.postDataParams=hashMapInfo;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected JSONArray doInBackground(String... params) {

        String JsonString = getServiceData();
        if (JsonString != null) {
            try {
                jsonArr = new JSONArray(JsonString);
                //Log.v("Result : ", String.valueOf(jsonArr));
                id = 1;

            } catch (Exception e) {
                e.printStackTrace();
                internetConnection=false;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return jsonArr;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);

        if (progressDialog.isShowing())
            progressDialog.dismiss();
        if (id == -1 || id == 0 || internetConnection == false) {
            Log.v("Result ", "Failed To Connect");
            loginActivity.alertDialog("Please Check Your Internet Connection!");
        } else {
            result(jsonArray);
        }
    }


    private String getServiceData() {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();


    }


    private void result(JSONArray jArr)
    {
        try {
            for (int i = 0; i < jArr.length(); ++i)
            {
                JSONObject jo = (JSONObject) jArr.get(i);
                //Toast.makeText(loginActivity.getApplicationContext(),jo.getString("auth"),Toast.LENGTH_LONG).show();
                authentication = jo.getString("auth");

            }
            if (authentication.equals("true"))
            {
                loginActivity.saveInSharedPreference();
            }
            else
            {
                Toast.makeText(loginActivity.getApplicationContext(),"Info Doesn't Match",Toast.LENGTH_LONG).show();
            }

        }
        catch (JSONException jsonE)
        {
            loginActivity.alertDialog("Info Doesn't Match");
        }
        catch (Exception e)
        {

        }
    }
}
