package com.otemainc.mlipa.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static String PREFERENCE_NAME = "GasAp";
    private static SharedPref sharedPref;
    private SharedPreferences preferences;

    private SharedPref(Context context) {
        PREFERENCE_NAME = PREFERENCE_NAME + context.getPackageName();
        this.preferences = context.getSharedPreferences(PREFERENCE_NAME, context.MODE_PRIVATE);
    }
    //get the instance of the shared preference
    public static SharedPref getInstance(){
        if(sharedPref == null) {
            sharedPref = new SharedPref(Mlipa.getContext());
        }
        return sharedPref;
    }
    //save the shared string
    public void saveString(String key, String val){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,val);
        editor.apply();
    }
    public String getString(String key, String defVal){
        return preferences.getString(key,defVal);
    }
    public String getString(String key){
        return preferences.getString(key,"");
    }
}
