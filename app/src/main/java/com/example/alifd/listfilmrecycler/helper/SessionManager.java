package com.example.alifd.listfilmrecycler.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.alifd.listfilmrecycler.BuildConfig;

public class SessionManager {

    private final SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String KEY = BuildConfig.API_KEY;
    private static final String LANGUAGE = "language";
    private static final String IMG_BASE_URL = "img_base_url";


    public SessionManager(Context context) {
        pref = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }

    public void setKey(String value) {
        editor.putString(KEY, value);
        editor.commit();
    }

    public void setLanguage(String value) {
        editor.putString(LANGUAGE, value);
        editor.commit();
    }

    public void setImgBaseUrl(String value) {
        editor.putString(IMG_BASE_URL, value);
        editor.commit();
    }

    public String getKey() {
        return pref.getString(KEY, "");
    }

    public String getLanguage() {
        return pref.getString(LANGUAGE, "");
    }

    public String getImgBaseUrl() {
        return pref.getString(IMG_BASE_URL, "");
    }

}
