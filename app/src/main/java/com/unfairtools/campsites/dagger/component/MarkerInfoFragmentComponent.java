package com.unfairtools.campsites.dagger.component;

import com.unfairtools.campsites.dagger.module.MarkerInfoFragmentModule;
import com.unfairtools.campsites.ui.LocationDetailsFragment;

import dagger.Component;

/**
 * Created by newuser on 11/8/16.
 */


@Component(modules = {
        MarkerInfoFragmentModule.class
})


public interface MarkerInfoFragmentComponent {
    void inject(LocationDetailsFragment fragment);
}

