package com.unfairtools.campsites.presenters;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.contracts.MarkerInfoContract;
import com.unfairtools.campsites.util.ApiService;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.MarkerInfoObject;
import com.unfairtools.campsites.util.SQLMethods;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by brianroberts on 11/8/16.
 */

public class MarkerInfoFragmentPresenter implements MarkerInfoContract.Presenter {


    @Inject
    ApiService apiService;

    @Inject
    SQLiteDatabase db;


    MarkerInfoContract.View view;
    BaseApplication baseApplication;

    InfoObject myInfo;


    public MarkerInfoFragmentPresenter(MarkerInfoContract.View v, BaseApplication b){
        this.view = v;
        this.baseApplication = b;
        this.baseApplication.getServicesComponent().inject(this);
    }

    public void setMarkerIdAndName(InfoObject inf){
        this.myInfo = new InfoObject();
        this.myInfo.ids = new int[]{inf.ids[0]};
        this.myInfo.name = inf.name;
        this.myInfo.latPoint = inf.latPoint;
        this.myInfo.longPoint = inf.longPoint;
        this.myInfo.types = inf.types;

        this.view.takePrelimInfo(inf);

        makeItRainOnDemBitches();


    }

    public void saveMarkerInfoLocal(){

    }

    public void makeItRainOnDemBitches(){

        MarkerInfoObject markerInfoObject = SQLMethods.getMarkerInfoLocal(MarkerInfoFragmentPresenter.this.myInfo.ids[0], db);

        if(markerInfoObject==null) {
            Log.e("MarkerInfFragPres","markerInfoObject: null");
            new Thread() {
                public void run() {
                    InfoObject inf = new InfoObject();
                    inf.ids = new int[]{MarkerInfoFragmentPresenter.this.myInfo.ids[0]};
                    Call<MarkerInfoObject> call = apiService.postIdForMarkerInfo(inf);
                    call.enqueue(new Callback<MarkerInfoObject>() {
                        @Override
                        public void onResponse(Call<MarkerInfoObject> call, retrofit2.Response<MarkerInfoObject> response) {
                            try {
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body());
                                System.out.println("received json: " + json);
                                if (response.isSuccessful()) {
                                    MarkerInfoObject inf = response.body();
                                    System.out.println("descrption: " + inf.description);
                                    if (response.body().description != null) {
                                        System.out.println(response.body().description);
                                        view.takeInfo(response.body(),myInfo);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<MarkerInfoObject> call, Throwable t) {
                            Log.e("resp", "failed " + t.toString() + " is executed: " + call.isExecuted());
                        }
                    });
                }
            }.start();
        }else{
            Log.e("MarkerInfFragPres",markerInfoObject.toString());
            view.takeInfo(markerInfoObject,myInfo);
        }
    }

    public boolean acceptRating(int a, int b){
      return true;
    };

    public InfoObject getMarkerInfo(int id){
        return new InfoObject();
    }



}
