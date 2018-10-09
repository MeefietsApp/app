package nl.hypothermic.meefietsen.core;

import android.content.Context;
import android.content.SharedPreferences;

import nl.hypothermic.meefietsen.SplashActivity;

public class ConfigurationLoader {

    private static SharedPreferences prefs = SplashActivity.act.getPreferences(Context.MODE_PRIVATE);

    public static String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
