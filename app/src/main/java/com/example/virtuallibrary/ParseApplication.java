package com.example.virtuallibrary;

import android.app.Application;

import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Message;
import com.example.virtuallibrary.models.Post;
import com.example.virtuallibrary.models.Table;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Table.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Goal.class);
        ParseObject.registerSubclass(Message.class);


        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("prithvi-virtual-library") // should correspond to APP_ID env variable
                .clientKey("PrithviParseVirtualLibraryFour")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://prithvi-virtual-library.herokuapp.com/parse").build());
    }
}