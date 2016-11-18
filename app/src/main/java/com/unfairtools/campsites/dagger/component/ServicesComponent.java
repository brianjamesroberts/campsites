package com.unfairtools.campsites.dagger.component;

import com.unfairtools.campsites.Adapters.MarkerInfoCardAdapter;
import com.unfairtools.campsites.dagger.module.LoginManagerModule;
import com.unfairtools.campsites.dagger.module.RealmModule;
import com.unfairtools.campsites.dagger.module.SQLiteModule;
import com.unfairtools.campsites.presenters.MapsPresenter;
import com.unfairtools.campsites.presenters.MarkerInfoFragmentPresenter;
import com.unfairtools.campsites.util.LoginManager;

import dagger.Component;


import javax.inject.Singleton;

@Component(modules = {
        SQLiteModule.class,
        RealmModule.class,
        LoginManagerModule.class
})

@Singleton
public interface ServicesComponent {
    void inject(MapsPresenter presenter);
    void inject(MarkerInfoFragmentPresenter presenter);
    void inject(MarkerInfoCardAdapter adapter);
    void inject(LoginManager loginManager);

}
