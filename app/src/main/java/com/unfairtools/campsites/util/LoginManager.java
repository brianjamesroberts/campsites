package com.unfairtools.campsites.util;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.unfairtools.campsites.base.BaseApplication;

import javax.inject.Inject;

/**
 * Created by brianroberts on 11/16/16.
 */

public class LoginManager {

    BaseApplication baseApplication;

    private boolean loggedIn = false;

    @Inject
    SQLiteDatabase db;

    @Inject
    ApiService apiService;


    private String authKey;

    public LoginManager(BaseApplication base){

        Log.e("LoginManager", "New login manager");

        this.baseApplication = base;
        base.getServicesComponent().inject(this);

        this.authKey = SQLMethods.getLastAuthKey(db);
        Log.e("LoginManager", "Last AuthKey: " + this.authKey);

        if(!loggedIn())
            tryLogin();

    }

    private void putLastAuthKey(String key){
        SQLMethods.putLastAuthKey(db,key);
    }

    private boolean loggedIn(){
        //get restufl, does my key work?
        return loggedIn;
    }

    public void tryLogin(){

        InfoObject inf = new InfoObject();
        inf.name = authKey;
        //apiService.postLoginAuth(inf);
    }

}
