package com.unfairtools.campsites.maps;

import android.database.sqlite.SQLiteDatabase;

import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.util.ApiService;
import com.unfairtools.campsites.util.InfoObject;

import javax.inject.Inject;

/**
 * Created by brianroberts on 11/8/16.
 */

public class MarkerInfoDialogFragmentPresenter implements MarkerInfoContract.Presenter {


    @Inject
    ApiService apiService;

    @Inject
    SQLiteDatabase db;

    MarkerInfoContract.View view;
    BaseApplication baseApplication;

    private int id;
    private String name;


    public MarkerInfoDialogFragmentPresenter(MarkerInfoContract.View v, BaseApplication b){
        view = v;
        baseApplication = b;
        b.getServicesComponent().inject(this);
    }

    public void setMarkerIdAndName(int idd, String namee){
        this.id = idd;
        this.name = namee;
        InfoObject inf = new InfoObject();
        inf.name = this.name;
        inf.ids = new int[]{this.id};
        view.takePrelimInfo(inf);
    }

    public boolean acceptRating(int a, int b){
      return true;
    };

    public InfoObject getMarkerInfo(int id){
        return new InfoObject();
    }



}
