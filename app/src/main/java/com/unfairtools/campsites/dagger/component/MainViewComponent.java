package com.unfairtools.campsites.dagger.component;

import com.unfairtools.campsites.ui.MainActivity;
import com.unfairtools.campsites.dagger.module.MainViewModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by brianroberts on 11/15/16.
 */

@Component(modules = {
        MainViewModule.class
})

@Singleton
public interface MainViewComponent {
    void inject(MainActivity view);
}
