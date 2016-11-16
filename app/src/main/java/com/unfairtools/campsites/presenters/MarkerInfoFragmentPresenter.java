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

    private int id;
    private String name;


    public MarkerInfoFragmentPresenter(MarkerInfoContract.View v, BaseApplication b){
        view = v;
        baseApplication = b;
        b.getServicesComponent().inject(this);
    }

    public void setMarkerIdAndName(int idd){
        this.id = idd;
        //this.name = namee;
        InfoObject inf = new InfoObject();
        //inf.name = this.name;
        inf.ids = new int[]{this.id};
        view.takePrelimInfo(inf);


        makeItRainOnDemBitches();



    }

    public void saveMarkerInfoLocal(){

    }

    public void makeItRainOnDemBitches(){

        MarkerInfoObject markerInfoObject = SQLMethods.getMarkerInfoLocal(MarkerInfoFragmentPresenter.this.id, db);

        if(markerInfoObject==null) {
            Log.e("MarkerInfFragPres","markerInfoObject: null");
            new Thread() {
                public void run() {
                    InfoObject inf = new InfoObject();
                    inf.ids = new int[]{MarkerInfoFragmentPresenter.this.id};
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
                                        view.takeInfo(response.body());
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
            view.takeInfo(markerInfoObject);
        }
    }

    public boolean acceptRating(int a, int b){
      return true;
    };

    public InfoObject getMarkerInfo(int id){
        return new InfoObject();
    }



}
