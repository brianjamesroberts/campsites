package com.unfairtools.campsites.dagger.component;

import android.database.sqlite.SQLiteDatabase;

import com.unfairtools.campsites.MainActivity;
import com.unfairtools.campsites.dagger.module.SQLiteModule;

import dagger.Component;


import javax.inject.Singleton;

@Component(modules = {
        SQLiteModule.class
})

@Singleton
public interface ServicesComponent {
    void inject(MainActivity fragment);
}
