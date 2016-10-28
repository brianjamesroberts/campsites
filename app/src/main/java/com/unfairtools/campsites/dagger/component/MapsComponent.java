package com.unfairtools.campsites.dagger.component;

import com.unfairtools.campsites.dagger.module.MapsModule;
import com.unfairtools.campsites.dagger.module.RealmModule;
import com.unfairtools.campsites.dagger.module.SQLiteModule;
import com.unfairtools.campsites.ui.MapFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by newuser on 10/27/16.
 */


@Component(modules = {
        MapsModule.class
})

@Singleton
public interface MapsComponent {
        void inject(MapFragment fragment);

}
