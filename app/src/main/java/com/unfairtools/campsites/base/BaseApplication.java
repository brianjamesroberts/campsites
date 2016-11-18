package com.unfairtools.campsites.base;

import android.app.Application;

import com.unfairtools.campsites.dagger.component.DaggerServicesComponent;
import com.unfairtools.campsites.dagger.component.ServicesComponent;
import com.unfairtools.campsites.dagger.module.LoginManagerModule;
import com.unfairtools.campsites.dagger.module.RealmModule;
import com.unfairtools.campsites.dagger.module.SQLiteModule;
import com.unfairtools.campsites.util.LoginManager;


/**
 * Created by brianroberts on 10/25/16.
 */



public class BaseApplication extends Application {

    private ServicesComponent servicesComponent;



    public void onCreate(){


        servicesComponent =
                DaggerServicesComponent.builder()
                .sQLiteModule(new SQLiteModule(this))
                .realmModule(new RealmModule(this))
                .loginManagerModule(new LoginManagerModule(this))
                .build();
    }

    public ServicesComponent getServicesComponent(){
        return servicesComponent;
    }


}
