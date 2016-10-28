package com.unfairtools.campsites.dagger.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.util.OpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by newuser on 10/24/16.
 */

@Module
public class SQLiteModule {

        private Context context;


        public SQLiteModule(Context c) {
            context = c;
        }

    @Provides
    @Singleton
    SQLiteDatabase provideDatabase() {
        return OpenHelper.getInstance(context).getWritableDatabase();
    }
}

