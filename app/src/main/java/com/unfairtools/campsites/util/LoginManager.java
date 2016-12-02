package com.unfairtools.campsites.util;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.util.callbacks.OnLoggedInCallback;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by brianroberts on 11/16/16.
 */

public class LoginManager {

    //private BaseApplication baseApplication;

    private boolean loggedIn = false;

    @Inject
    SQLiteDatabase db;

    @Inject
    ApiService apiService;


    private String authKey;
    private String username;
    private String password;

    public LoginManager(BaseApplication base){

        Log.e("LoginManager", "New login manager");

        //this.baseApplication = base;
        base.getServicesComponent().inject(this);

        printStoredAuth();

        if(!loggedIn())
            tryLogin();
//
//        newLogin("brian", "password", new OnLoggedInCallback() {
//
//            @Override
//            public void onFinish() {
//                String inf = LoginManager.this.authKey;
//                Log.e("LoginManager", "Login received " + authKey);
//                SQLMethods.putLastAuthKey(db,authKey);
//            }
//        });

//        if(loggedIn)
//            beginRefreshLogin();
    }


    public static class Constants{
        public final static int LOGIN_DENIED=0;
        public final static int LOGIN_SUCCESS=1;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void newLogin(final String username, final String password, final OnLoggedInCallback cb) {
        new Thread(){
            public void run(){
            Call<InfoObject> call = apiService.postLogin(username,password);
            Log.e("LoginManager","newLogin called");
            call.enqueue(new Callback<InfoObject>(){

                @Override
                public void onResponse(Call<InfoObject> call, retrofit2.Response<InfoObject> response){
                    InfoObject infResult = new InfoObject();
                    try {
                        infResult.ids = new int[]{Constants.LOGIN_DENIED};
                        infResult.name = "OnResponse: Success, but didn't parse json properly!";

                        Log.e("Incmoing json login: ", new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            InfoObject inf = response.body();
                            System.out.println(inf.names);
                            if (response.body().names != null) {
                                infResult.ids = new int[]{Constants.LOGIN_SUCCESS};
                                infResult.names = response.body().names;
                                infResult.authKey = response.body().authKey;
                                if (infResult.authKey != null && !infResult.authKey.equals("")) {
                                    LoginManager.this.username = username;
                                    LoginManager.this.password = password;
                                    LoginManager.this.loggedIn = true;
                                    LoginManager.this.authKey = infResult.authKey;
                                    SQLMethods.putLastAuthKey(db,infResult.authKey);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        infResult.ids = new int[]{Constants.LOGIN_DENIED};
                        infResult.name = e.toString();
                    }
                    cb.onFinish();
                }

            @Override
            public void onFailure(Call<InfoObject> call, Throwable t) {
                InfoObject infResult = new InfoObject();
                infResult.ids = new int[]{Constants.LOGIN_DENIED};
                infResult.name = "onFailure";
                cb.onFinish();
                Log.e("resp","failed " + t.toString() + " is executed: " + call.isExecuted());
            }
        });
            }
        }.start();

    }


    private void printStoredAuth(){
        authKey = SQLMethods.getLastAuthKey(db);
        Log.e("LoginManager","getAuth: " + authKey);
        //putLastAuthKey("55");
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
