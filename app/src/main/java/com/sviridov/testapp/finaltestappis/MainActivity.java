package com.sviridov.testapp.finaltestappis;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.sviridov.testapp.finaltestappis.NotificationUtils.ANDROID_CHANNEL_ID;


public class MainActivity extends AppCompatActivity {
    //public static final String NOTIFICATION_CHANNEL_ID = "4565";

    ArrayList<Item> arrayList;
    ListView lv;
    NotificationUtils mNotificationUtils;
    int notificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        notificationCount = 1;
        mNotificationUtils = new NotificationUtils(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://jsonplaceholder.typicode.com/photos");
            }
        });

        CustomListAdapter adapter = new CustomListAdapter(
                getApplicationContext(), R.layout.custom_list_layout, arrayList
        );
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification("Уведомление", "By N.S.");
                mNotificationUtils.getManager().notify(101, nb.build());
                if (notificationCount % 2 == 0) {
                    mNotificationUtils.getManager().cancel(101);
                }
                notificationCount++;
            }
        });
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                //JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = new JSONArray(content);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    arrayList.add(new Item(
                            productObject.getString("title"),
                            productObject.getString("id"),
                            productObject.getString("url")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomListAdapter adapter = new CustomListAdapter(
                    getApplicationContext(), R.layout.custom_list_layout, arrayList
            );
            lv.setAdapter(adapter);

        }
    }


    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
