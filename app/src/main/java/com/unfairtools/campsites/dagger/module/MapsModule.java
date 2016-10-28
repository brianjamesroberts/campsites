package com.unfairtools.campsites.dagger.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.maps.MapsContract;
import com.unfairtools.campsites.maps.MapsPresenter;
import com.unfairtools.campsites.util.OpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by newuser on 10/27/16.
 */


@Module
public class MapsModule {

    private MapsContract.View view;
    private Context context;
    private BaseApplication base;

    public MapsModule(MapsContract.View v, BaseApplication b) {
        view = v;
        base = b;
    }

    @Provides
    MapsPresenter providePresenter() {
        return new MapsPresenter(view,base);
    }

    @Provides
    SQLiteDatabase provideDB(){
        return OpenHelper.getInstance(base.getApplicationContext()).getWritableDatabase();
    }
}