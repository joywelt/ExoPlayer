package com.google.android.exoplayer.demo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import java.io.File;

/**
 * Created by li on 4/11/16.
 */
public class AddPreference extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        loadUserSettings();
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void loadUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //if there exists SDP files or Config file for Unicast, we should load the value
        PlayerActivity newActivity = new PlayerActivity();
        File f = new File(Environment.getExternalStorageDirectory().getPath()+"/htdocs/Route_Receiver/bin/SDP1.sdp");
        if(f.exists() && !f.isDirectory()) {
            try {
                String ip = newActivity.new MyAsyncTask().execute("http://localhost:8080/Route_Receiver/Receiver_App/loadIP.php", "", "").get();
                editor.putString("senderIP", ip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        f = new File(Environment.getExternalStorageDirectory().getPath()+"/htdocs/Route_Receiver/Receiver/RcvConfig.txt");
        if(f.exists() && !f.isDirectory()) {
            try {
                String cast = newActivity.new MyAsyncTask().execute("http://localhost:8080/Route_Receiver/Receiver_App/loadRcvConfig.php", "", "").get();
                editor.putBoolean("unicastEnabled",cast.equals("1"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        editor.apply();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //write the settings to the configuration files
        PlayerActivity newActivity = new PlayerActivity();
        if (key.equals("senderIP")) {
            try {
                newActivity.new MyAsyncTask().execute("http://localhost:8080/Route_Receiver/Receiver_App/saveIP.php", "ip", sharedPreferences.getString(key, "")).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(key.equals("unicastEnabled")) {
            try {
                newActivity.new MyAsyncTask().execute("http://localhost:8080/Route_Receiver/Receiver_App/saveRcvConfig.php", "cast", sharedPreferences.getBoolean(key,false)?"1":"2").get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}