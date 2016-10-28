package com.unfairtools.campsites.dagger.component;

import com.unfairtools.campsites.MainActivity;
import com.unfairtools.campsites.dagger.module.SQLiteModule;
import com.unfairtools.campsites.maps.MapsContract;
import com.unfairtools.campsites.maps.MapsPresenter;

import dagger.Component;


import javax.inject.Singleton;

@Component(modules = {
        SQLiteModule.class

})

@Singleton
public interface ServicesComponent {
    void inject(MainActivity fragment);
    void inject(MapsPresenter presenter);
}
