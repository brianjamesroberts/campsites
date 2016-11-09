package com.unfairtools.campsites.dagger.module;

import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.maps.MarkerInfoContract;
import com.unfairtools.campsites.maps.MarkerInfoDialogFragmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by newuser on 11/8/16.
 */

@Module
public class MarkerInfoFragmentModule {


    MarkerInfoContract.View view;
    BaseApplication base;


    public MarkerInfoFragmentModule(MarkerInfoContract.View v, BaseApplication b) {
        view = v;
        base = b;
    }


    @Provides
    MarkerInfoDialogFragmentPresenter providePresenter() {
        return new MarkerInfoDialogFragmentPresenter(view,base);
    }
}
