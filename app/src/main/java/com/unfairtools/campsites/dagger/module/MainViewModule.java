package com.unfairtools.campsites.dagger.module;

import android.database.sqlite.SQLiteDatabase;

import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.presenters.MainActivityPresenter;
import com.unfairtools.campsites.contracts.MainContract;
import com.unfairtools.campsites.util.OpenHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by brianroberts on 11/15/16.
 */

@Module
public class MainViewModule {

    private MainContract.View view;
    private BaseApplication base;

    public MainViewModule(MainContract.View v, BaseApplication b) {
        view = v;
        base = b;
    }

    @Provides
    MainActivityPresenter providePresenter() {
        return new MainActivityPresenter(view,base);
    }

    @Provides
    SQLiteDatabase provideDB(){
        return OpenHelper.getInstance(base.getApplicationContext()).getWritableDatabase();
    }
}