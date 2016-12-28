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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.gson.Gson;
import com.unfairtools.campsites.R;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.contracts.MapsContract;
import com.unfairtools.campsites.util.ApiService;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.MarkerInfoObject;
import com.unfairtools.campsites.util.SQLMethods;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    BitmapDescriptor icon;

    private volatile int showId = -1;

    public MapsPresenter(MapsContract.View v, BaseApplication b){
        view = v;
        baseApp = b;
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_marker);
        b.getServicesComponent().inject(this);
        init();
        log("presenter created");
    }

    public void sendMapTo(double lat, double longitude){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longitude), 9.0f));
    }

    public void initZoom(){

    };


    private HashMap<MarkerOptions, Integer> markerOptionsHashMap;
    private HashMap<MarkerOptions, Integer> markerOptionsHashMapLocal;
    private HashMap<Marker, Integer> markerHashMap;
    private HashMap<Integer, Marker> intToMarkerHashMap;


    public boolean showMarkerTag(int id){
        try {
            intToMarkerHashMap.get(id).showInfoWindow();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean onMarkerClick(Marker m){
        m.showInfoWindow();
        log("Marker clicked id "  +  m.getId() + ", id: " + markerHashMap.get(m));


        view.getMainActivity().presenter.animateToolbarMargin(0);
        view.getMainActivity().putMarkerInfoFragment(markerHashMap.get(m), m.getTitle(),0,
                m.getPosition().latitude,m.getPosition().longitude);


        return true;

    }



    public void loadMarkersLocal(final LatLngBounds latLngBounds){

        ArrayList<SQLMethods.MarkerOptionsTuple> localSaves = SQLMethods.getMarkers(db,latLngBounds);

        //markerOptionsHashMapLocal.clear();

        Log.e("MapsPresenter", localSaves.size() + " localsaves");



        for(SQLMethods.MarkerOptionsTuple mo: localSaves){
            mo.marker.icon(icon);
            //Log.e("MapsPresenter", "loading from local: " + mo.marker.getTitle());
            markerOptionsHashMapLocal.put(mo.marker,mo.id);
        }
        placeMarkersOnMap(googleMap, null);

    }


    public void loadMarkers(final LatLngBounds latLngBounds){


//        placeMarkersOnMap(googleMap, null);


        new Thread(){
            public void run(){
                InfoObject inf = new InfoObject();
                inf.latNorth = latLngBounds.northeast.latitude;
                inf.longEast = latLngBounds.northeast.longitude;
                inf.latSouth = latLngBounds.southwest.latitude;
                inf.longWest = latLngBounds.southwest.longitude;
                //Log.e("Json out",new Gson().toJson(inf));
                //Log.e("MapsPresenter", "apisvc: " + apiService.toString());
                Call<InfoObject> call = apiService.postBoundsForMarkersGet(
                        Double.toString(inf.latNorth),
                        Double.toString(inf.longEast),
                        Double.toString(inf.latSouth),
                        Double.toString(inf.longWest));
                Log.e("MapsPresenter","Requesting markers");
                call.enqueue(new Callback<InfoObject>(){
                    @Override
                    public void onResponse(Call<InfoObject> call, retrofit2.Response<InfoObject> response) {
                        try {
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            if(response.isSuccessful()) {
                                InfoObject inf = response.body();
                                System.out.println(inf.names);
                                placeMarkersOnMap(googleMap, response.body());
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
                }
            }
        }

        Set<MarkerOptions> localMarkers =  markerOptionsHashMapLocal.keySet();
        Set<MarkerOptions> iMarkers = markerOptionsHashMap.keySet();

        if(inf==null) {
            for (MarkerOptions m : localMarkers) {
                Marker a = googleMap.addMarker(m);
                markerHashMap.put(a, markerOptionsHashMapLocal.get(m));
                intToMarkerHashMap.put(markerOptionsHashMapLocal.get(m), a);
            }
        }

        if(inf!=null) {

            for (MarkerOptions m : iMarkers) {
                Marker a = googleMap.addMarker(m);
                markerHashMap.put(a, markerOptionsHashMap.get(m));
                intToMarkerHashMap.put(markerOptionsHashMap.get(m), a);
            }
        }

        Log.e("MapsPresenter", "Attempting to show tag " + showId);
        if(showId!=-1){
            //the hackiness is l33t!!!!
            if(((SQLMethods.getMarkerInfoLocal(showId,db)!=null) && inf==null) ||(SQLMethods.getMarkerInfoLocal(showId,db)==null && inf!=null)) {
                if (showMarkerTag(showId)) {
                    showId = -1;
                }
            }
        }
    }

    public void setShowTagId(int id){
        showId=id;
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

        InfoObject inf = new InfoObject();
        inf.name = "Heart O the Hills Campground";
        inf.ids = new int[]{0};
        inf.latPoint = 48.0356029d;
        inf.longPoint = -123.424074d;
        inf.types = new int[]{0};
        SQLMethods.addLocationLocal(db,inf);
        MarkerInfoObject obj = new MarkerInfoObject();
        obj.id_primary_key = 0; obj.description = "The beautiful Heart O' the Hills Campground";
        SQLMethods.addLocationInfoLocal(db,obj);
        LatLng lat = new LatLng(47.51f,-122.35f);

        LatLng lat2 = SQLMethods.getMapLocationLatLng(db);
        float zoom = SQLMethods.getMapLocationZoom(db);

        Log.e("MapsPresenter", "latLong: " + lat2.latitude + ", " + lat2.longitude + " :: zoom " + zoom);

        //6.833f = zoom default


        gm.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                SQLMethods.setMapPrefs(db,googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom);
                initPoints();
            }
        });

        gm.moveCamera(CameraUpdateFactory.newLatLngZoom(lat2,zoom));



    }


    public void refreshPoints(){
        googleMap.clear();
        markerOptionsHashMap.clear();
        markerOptionsHashMapLocal.clear();
        intToMarkerHashMap.clear();
        initPoints();
    }


    public void initPoints(){
        markerHashMap.clear();
        googleMap.clear();
        loadMarkersLocal(googleMap.getProjection().getVisibleRegion().latLngBounds);
        loadMarkers(googleMap.getProjection().getVisibleRegion().latLngBounds);
    };

    public void showInfo(SupportMapFragment mapFragment){
        //mapFragment.getActivity().makeDialog()
    };



    public void init(){

        markerHashMap = new HashMap<Marker,Integer>();
        markerOptionsHashMap = new HashMap<MarkerOptions, Integer>();
        markerOptionsHashMapLocal = new HashMap<MarkerOptions, Integer>();
        intToMarkerHashMap = new HashMap<Integer, Marker>();

    }




    public void log(String s){
        Log.e("presenter", s);
    }

}
