package com.hllbr.instagramclonewithparse;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterActivity extends Application {//ParseStarterClass


    @Override
    public void onCreate() {
        super.onCreate();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        //initialize = başlangıç durumuna getirmek
        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("BiFEFEPOL8RcFkVK4xzhcNGNd3OAyCw93YpXnEkU")
        .clientKey("tS8GwdTcb7im5nO4SVOA1eZi0oL7OsHj1X7LOS3Q")
        .server("https://parseapi.back4app.com/")
        .build());
    }
}
