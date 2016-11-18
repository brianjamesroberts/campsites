package com.unfairtools.campsites.dagger.module;


import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.util.LoginManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by brianroberts on 11/17/16.
 */

@Module
public class LoginManagerModule {

    private BaseApplication base;

    public LoginManagerModule(BaseApplication b){
        base = b;
    }

    @Provides
    @Singleton
    LoginManager provideLoginManager(){
        return new LoginManager(base);
    }

}


