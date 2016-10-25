package com.unfairtools.campsites.dagger.component;

import android.database.sqlite.SQLiteDatabase;

import com.unfairtools.campsites.dagger.module.SQLiteModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by newuser on 10/24/16.
 */



@Component(modules = {
        SQLiteModule.class
})

@Singleton
public interface ServicesComponent {

    SQLiteDatabase sqlDatabase();
}
