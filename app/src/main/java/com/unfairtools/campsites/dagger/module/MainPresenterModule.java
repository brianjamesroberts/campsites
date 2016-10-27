package com.unfairtools.campsites.dagger.module;

import com.google.android.gms.maps.SupportMapFragment;
import com.unfairtools.campsites.maps.MapsContract;
import com.unfairtools.campsites.maps.MapsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by brianroberts on 10/26/16.
 */

@Module
public class MainPresenterModule {

    MapsContract.View view;

    public MainPresenterModule(MapsContract.View v) {
        view = v;
    }

    @Provides
    MapsContract.Presenter providePresenter() {
        return new MapsPresenter(view);
    }

}
