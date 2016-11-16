package com.unfairtools.campsites.presenters;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.gson.Gson;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.contracts.MapsContract;
import com.unfairtools.campsites.util.ApiService;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.MarkerInfoObject;
import com.unfairtools.campsites.util.SQLMethods;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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


    public MapsPresenter(MapsContract.View v, BaseApplication b){
        view = v;
        baseApp = b;
        b.getServicesComponent().inject(this);
        init();
        log("presenter created");
    }

    public void initZoom(){

    };


    private HashMap<MarkerOptions, Integer> markerOptionsHashMap;
    private HashMap<MarkerOptions, Integer> markerOptionsHashMapLocal;
    private HashMap<Marker, Integer> markerHashMap;

    public boolean onMarkerClick(Marker m){
        m.showInfoWindow();
        log("Marker clicked id "  +  m.getId() + ", id: " + markerHashMap.get(m));


        view.getMainActivity().presenter.animateToolbarMargin(0);
        view.getMainActivity().putMarkerInfoFragment(markerHashMap.get(m), m.getTitle());


        return true;

    }



    public void loadMarkersLocal(final LatLngBounds latLngBounds){

        ArrayList<SQLMethods.MarkerOptionsTuple> localSaves = SQLMethods.getMarkers(db,latLngBounds);

        //markerOptionsHashMapLocal.clear();

        Log.e("MapsPresenter", localSaves.size() + " localsaves");

        for(SQLMethods.MarkerOptionsTuple mo: localSaves){
            Log.e("MapsPresenter", "loading from local: " + mo.marker.getTitle());
            markerOptionsHashMapLocal.put(mo.marker,mo.id);
        }


    }


    public void loadMarkers(final LatLngBounds latLngBounds){


        placeMarkersOnMap(googleMap, null);


        new Thread(){
            public void run(){
                InfoObject inf = new InfoObject();
                inf.latNorth = latLngBounds.northeast.latitude;
                inf.longEast = latLngBounds.northeast.longitude;
                inf.latSouth = latLngBounds.southwest.latitude;
                inf.longWest = latLngBounds.southwest.longitude;
                Call<InfoObject> call = apiService.postBoundsForMarkers(inf);
                Log.e("MapsPresenter","Requesting markers");
                call.enqueue(new Callback<InfoObject>(){
                    @Override
                    public void onResponse(Call<InfoObject> call, retrofit2.Response<InfoObject> response) {
                        try {

                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println(json);

                            if(response.isSuccessful()) {
                                InfoObject inf = response.body();
                                System.out.println(inf.names);
                                if(response.body().names!=null) {
                                    placeMarkersOnMap(googleMap, response.body());
                                }
                            }
//                    readTSLInfo(new JSONObject(response.body().toJSon()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<InfoObject> call, Throwable t) {


                        Log.e("resp","failed " + t.toString() + " is executed: " + call.isExecuted());
                    }
                });
            }
        }.start();
    }


    public void placeMarkersOnMap(GoogleMap googleMap, InfoObject inf){

        //markerOptionsHashMap.clear();


        if(inf!=null) {
            for (int i = 0; i < inf.ids.length; i++) {
                MarkerOptions mo = new MarkerOptions()
                        .position(new LatLng(inf.latitudes[i], inf.longitudes[i]))
                        .title(inf.names[i]);

                if (!markerOptionsHashMapLocal.containsValue(inf.ids[i])) {
                    markerOptionsHashMap.put(mo, inf.ids[i]);
                    //Log.e("MapsPresenter", "Adding markeroptions from internet: " + mo.getTitle());
                } else {
                    //Log.e("MapsPresenter", "Already contained " + inf.ids[i] + " from local");
                }
            }
        }

        Set<MarkerOptions> localMarkers =  markerOptionsHashMapLocal.keySet();
        Set<MarkerOptions> iMarkers = markerOptionsHashMap.keySet();

        googleMap.clear();
        for(MarkerOptions m : localMarkers){
            markerHashMap.put(googleMap.addMarker(m),markerOptionsHashMapLocal.get(m));
        }

        for(MarkerOptions m: iMarkers){
            markerHashMap.put(googleMap.addMarker(m),markerOptionsHashMap.get(m));
        }
    }



    public void takeMap(GoogleMap gm){
        googleMap = gm;

        googleMap.setOnMarkerClickListener(this);

        if (ContextCompat.checkSelfPermission(this.view.getMainActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.view.getMainActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);

        } else {
            ActivityCompat.requestPermissions(this.view.getMainActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
        }




        view.getMainActivity().replaceSearchBarText();

        db.beginTransaction();
        SQLMethods.addLocation(db,0,48.0356029f,-123.424074f,"Heart O the Hills Campground",0);
        MarkerInfoObject obj = new MarkerInfoObject();
        obj.id_primary_key = 0; obj.description = "The beautiful Heart O' the Hills Campground";
        SQLMethods.addLocationInfo(db,obj);
        db.setTransactionSuccessful();
        db.endTransaction();
        LatLng lat = new LatLng(47.51f,-122.35f);

        LatLng lat2 = SQLMethods.getMapLocationLatLng(db);
        float zoom = SQLMethods.getMapLocationZoom(db);

        Log.e("MapsPresenter", "latLong: " + lat2.latitude + ", " + lat2.longitude + " :: zoom " + zoom);

        //6.833f = zoom default


        gm.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                SQLMethods.setMapPrefs(db,googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom);
                markerHashMap.clear();
                googleMap.clear();
                loadMarkersLocal(googleMap.getProjection().getVisibleRegion().latLngBounds);
                loadMarkers(googleMap.getProjection().getVisibleRegion().latLngBounds);
            }
        });

        gm.moveCamera(CameraUpdateFactory.newLatLngZoom(lat2,zoom));


        gm.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition location) {

//                SQLMethods.setMapPrefs(db,googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom);
//
//
//                //markerHashMap.clear();
//
//
//                googleMap.clear();
//
//
//
//                loadMarkersLocal(googleMap.getProjection().getVisibleRegion().latLngBounds);
//                loadMarkers(googleMap.getProjection().getVisibleRegion().latLngBounds);
            }
        });
    }



    public void initPoints(){};

    public void showInfo(SupportMapFragment mapFragment){
        //mapFragment.getActivity().makeDialog()
    };



    public void init(){

        markerHashMap = new HashMap<Marker,Integer>();
        markerOptionsHashMap = new HashMap<MarkerOptions, Integer>();
        markerOptionsHashMapLocal = new HashMap<MarkerOptions, Integer>();

    }




    public void log(String s){
        Log.e("presenter", s);
    }

}
