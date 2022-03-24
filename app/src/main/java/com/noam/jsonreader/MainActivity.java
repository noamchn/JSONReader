package com.noam.jsonreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.noam.jsonreader.Utils.TAG;

public class MainActivity extends AppCompatActivity {
    private GetJsonTask asyncTask;
    private TextView jsonTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init json TextView
        jsonTV = findViewById(R.id.json_tv);

        // Get Json from server
        asyncTask = new GetJsonTask(this);
        asyncTask.execute();

    }

    public JSONObject loadJSONFromAsset(Context context) {
        JSONObject json = null;
        try {
            InputStream is = context.getAssets().open("my_json.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new JSONObject(new String(buffer, StandardCharsets.UTF_8));


        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void onJsonTaskResult(JSONObject jsonObject) {
        if(jsonObject != null) {
            try {
                jsonTV.setText(jsonObject.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.v(TAG, "onJsonTaskResult: json is null");
        }
    }
}