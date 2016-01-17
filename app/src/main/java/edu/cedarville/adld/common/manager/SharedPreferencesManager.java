package edu.cedarville.adld.common.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    /** Static singleton instance */
    private static SharedPreferencesManager INSTANCE;

    /** Gets access to singleton instance */
    public static SharedPreferencesManager instance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesManager(context);
        }
        return INSTANCE;
    }

    /* Preference Keys */
    private static final String PREF_TITLE = "edu.cedarville.adld";
    private static final String PREF_RUNNING_AVG = "PREF_RUNNING_AVG";
    private static final String PREF_DISPLAY_HEX = "PREF_DISPLAY_HEX";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_TITLE, Context.MODE_PRIVATE);
    }

    public int getRunningAverage() {
        return sharedPreferences.getInt(PREF_RUNNING_AVG, 1);
    }

    public boolean getDisplayHex() {
        return sharedPreferences.getBoolean(PREF_DISPLAY_HEX, false);
    }

    public void setRunningAverage(int runningAverage) {
        sharedPreferences.edit().putInt(PREF_RUNNING_AVG, runningAverage).apply();
    }

    public void setDisplayHex(boolean displayHex) {
        sharedPreferences.edit().putBoolean(PREF_DISPLAY_HEX, displayHex).apply();
    }
}
