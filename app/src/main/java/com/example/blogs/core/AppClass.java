package com.example.blogs.core;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;

public class AppClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(this, "ar");

        Lingver.getInstance().setLocale(this , "en");



    }
}
