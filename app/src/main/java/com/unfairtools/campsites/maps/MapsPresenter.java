package com.unfairtools.campsites.maps;

/**
 * Created by brianroberts on 10/26/16.
 */

public class MapsPresenter implements MapsContract.Presenter {

    MapsContract.View view;

    public void loadCards(){

    }

    public MapsPresenter(MapsContract.View v){
        view = v;
    }

}
