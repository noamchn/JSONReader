package com.noam.jsonreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.io.BufferedReader;

import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.json.JSONArray;

import info.guardianproject.netcipher.NetCipher;
import info.guardianproject.netcipher.client.TlsOnlySocketFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.noam.jsonreader.Utils.TAG;
import static android.content.Context.TELEPHONY_SERVICE;

public class GetJsonTask extends AsyncTask<Void, Void, JSONObject> {

    private final MainActivity parent;

    public GetJsonTask(MainActivity mainActivity) {
        this.parent = mainActivity;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        String responseObj = getJsonObject();
        try {
            return new JSONObject(responseObj);
        } catch (Exception e) {
            Log.v(TAG, "[GetJsonTask] doInBackground: cannot get json");
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        parent.onJsonTaskResult(response);
    }


    private String getJsonObject() {
        URL url;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL("https://api.stackexchange.com/2.2/search?order=desc&sort=activity&intitle=perl&site=stackoverflow");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url");
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            // TODO if there are params {
//            Uri.Builder builder = new Uri.Builder()
//                    .appendQueryParameter("name", Build.PRODUCT)
//            String query = builder.build().getEncodedQuery();
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
//            bufferedWriter.write(query);
//            bufferedWriter.flush();
//            bufferedWriter.close();
//            os.close();
            // TODO } else {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            // TODO }

            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (conn != null) {
            conn.disconnect();
        }

        Log.v(TAG, "json:\n" + response.toString());
        return response.toString();

    }

    @SuppressLint("MissingPermission")
    private String getImei() {
        String imei = null;
        TelephonyManager telephonyManager = (TelephonyManager) parent.getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            try {
                imei = telephonyManager.getDeviceId();
                Log.v(TAG, "device imei is " + imei);
            } catch (Exception e) {
                Log.v(TAG, "could not read imei");
            }
        }

        return imei;

    }

}

