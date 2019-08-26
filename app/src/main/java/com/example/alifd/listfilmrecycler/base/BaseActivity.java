package com.example.alifd.listfilmrecycler.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.alifd.listfilmrecycler.helper.SessionManager;

public class BaseActivity extends AppCompatActivity {
    protected SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sessionManager == null) {
            sessionManager = new SessionManager(this);
            sessionManager.setLanguage("en-US");
            sessionManager.setImgBaseUrl("https://image.tmdb.org/t/p/w185");
        }
    }
}
