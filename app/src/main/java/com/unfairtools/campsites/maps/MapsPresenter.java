package com.unfairtools.campsites.maps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.util.ApiService;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.SQLMethods;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by brianroberts on 10/26/16.
 */

public class MapsPresenter implements MapsContract.Presenter, GoogleMap.OnMarkerClickListener {



    @Inject
    SQLiteDatabase db;

    @Inject
    ApiService apiService;

    BaseApplication baseApp;

    MapsContract.View view;

    private GoogleMap googleMap;

    public void initZoom(){

    };

    public boolean onMarkerClick(Marker m){

        log("Marker clicked id "  +  m.getId() + ", snippet: " + m.getSnippet());
        return true;

    }


    public void loadMarkers(final LatLngBounds latLngBounds){
        new Thread(){
            public void run(){
                InfoObject inf = new InfoObject();
                inf.latNorth = latLngBounds.northeast.latitude;
                inf.longEast = latLngBounds.northeast.longitude;
                inf.latSouth = latLngBounds.southwest.latitude;
                inf.longWest = latLngBounds.southwest.longitude;
                Call<InfoObject> call = apiService.postBoundsForMarkers(inf);
                call.enqueue(new Callback<InfoObject>(){
                    @Override
                    public void onResponse(Call<InfoObject> call, retrofit2.Response<InfoObject> response) {
                        try {
                            if(response.isSuccessful()) {
                                placeMarkersOnMap(googleMap, response.body());
                            }
//                    readTSLInfo(new JSONObject(response.body().toJSon()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<InfoObject> call, Throwable t) {
                        Log.e("resp","failed " + t.toString());
                    }
                });
            }
        }.start();
    }


    public static void placeMarkersOnMap(GoogleMap googleMap, InfoObject inf){
        for(int i = 0; i < inf.ids.length; i++) {
            googleMap.addMarker(new MarkerOptions()
                    .snippet(inf.ids[i])
            .position(new LatLng(inf.latitudes[i],inf.longitudes[i]))
            .title(inf.names[i]));
        }
    }

    public void takeMap(GoogleMap gm){
        googleMap = gm;

        googleMap.setOnMarkerClickListener(this);

        db.beginTransaction();
        SQLMethods.addLocation(db,0,48.0356029f,-123.424074f,"Heart O' the Hills Campground",0);
        SQLMethods.addLocationInfo(db,0,"The beautiful Heart O' the Hills Campground","",4.5f,"I hate this place\n");
        db.setTransactionSuccessful();
        db.endTransaction();
        LatLng lat = new LatLng(47.51f,-122.35f);
        gm.moveCamera(CameraUpdateFactory.newLatLngZoom(lat,6.833f));
        gm.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition location) {

                db.beginTransaction();
                ArrayList<MarkerOptions> markers = SQLMethods.getMarkers(db, googleMap.getProjection().getVisibleRegion().latLngBounds);
                db.setTransactionSuccessful();
                db.endTransaction();
                googleMap.clear();
                Log.e("Presenter","MarkerOptions is size " + markers.size());
                for(MarkerOptions marker: markers){
                    googleMap.addMarker(marker);
                }
                loadMarkers(googleMap.getProjection().getVisibleRegion().latLngBounds);

                //log(location.zoom+ ": lat; " + location.target.latitude + " long; " + location.target.longitude);
            }
        });
    }



    public void initPoints(){};

    public void showInfo(SupportMapFragment mapFragment){
        //mapFragment.getActivity().makeDialog()
    };

    public MapsPresenter(MapsContract.View v, BaseApplication b){
        view = v;
        baseApp = b;
        b.getServicesComponent().inject(this);
        init();
        log("presenter created");
    }

    public void init(){

        db.beginTransaction();
        db.endTransaction();

    }




    public void log(String s){
        Log.e("presenter", s);
    }

}
