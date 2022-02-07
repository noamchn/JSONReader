package com.noam.jsonreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView jsonTV = findViewById(R.id.json_tv);
        try {
            jsonTV.setText(loadJSONFromAsset(this).toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject loadJSONFromAsset(Context context) {
        JSONObject json = null;
        try {
            InputStream is = context.getAssets().open("my_json.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new JSONObject(new String(buffer, "UTF-8"));


        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}