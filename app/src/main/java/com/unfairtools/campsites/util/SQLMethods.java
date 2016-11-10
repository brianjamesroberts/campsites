package com.unfairtools.campsites.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class SQLMethods {


    public static class MarkerOptionsSpec{
        public MarkerOptions marker;
        public Integer id;

    }

    public static void setMapPrefs(SQLiteDatabase db,LatLng target, float zoom){
        db.beginTransaction();
        try{

            //if(!SQLMethods.existsRecord(Constants.LOCATIONS_INFO_TABLE_NAME,Constants.LocationsInfoTable.id_primary_key,id +"",db)) {
                ContentValues args = new ContentValues();
                args.put(Constants.MapPreferencesTable.id_primary_key, 0);
                args.put(Constants.MapPreferencesTable.latitude, target.latitude);
                args.put(Constants.MapPreferencesTable.longitude, target.longitude);
                args.put(Constants.MapPreferencesTable.zoom, zoom);
                db.update(Constants.MAP_PREFERENCES_TABLE_NAME, args, Constants.MapPreferencesTable.id_primary_key +" = 0",null);


            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
    }

    public static float getMapLocationZoom(SQLiteDatabase db){
        float retFloat = 0.0f;

        Cursor resultSet = null;

        db.beginTransaction();
        try{

            resultSet = db.rawQuery("Select * from " + Constants.MAP_PREFERENCES_TABLE_NAME+ " WHERE " +
                    Constants.MapPreferencesTable.id_primary_key + " = '0';", null);
            if(resultSet==null || resultSet.getCount()<1) {
                Log.e("SQLMethods", "zoom was empty");
            return retFloat;
            }

            resultSet.moveToFirst();

            retFloat = resultSet.getFloat(3);
            resultSet.close();
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
            return retFloat;
        }

    }

    public static LatLng getMapLocationLatLng(SQLiteDatabase db){
        LatLng retLatLng = new LatLng(0.0f,0.0f);
        Cursor resultSet = null;
        db.beginTransaction();
        try{

            resultSet = db.rawQuery("Select * from " + Constants.MAP_PREFERENCES_TABLE_NAME + " WHERE " +
                    Constants.MapPreferencesTable.id_primary_key + " = 0;", null);
            if(resultSet==null)
                return retLatLng;
            if(resultSet.getCount()<1)
                return retLatLng;
            resultSet.moveToFirst();
            retLatLng = new LatLng(resultSet.getDouble(1),resultSet.getDouble(2));
            resultSet.close();
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();

        }finally{
            db.endTransaction();
            return retLatLng;
        }

    }

    public static ArrayList<SQLMethods.MarkerOptionsSpec>  getMarkers(SQLiteDatabase db, LatLngBounds latLngBounds){
        ArrayList<SQLMethods.MarkerOptionsSpec> returnMarkers = new ArrayList<SQLMethods.MarkerOptionsSpec>();

        Log.e("SQLMETHODS", "latLngBounds.southewest: " + latLngBounds.southwest.toString());

        Cursor resultSet = db.rawQuery("Select * from " + Constants.LOCATIONS_TABLE_NAME//,null);

                + " WHERE " + Constants.LocationsTable.latitude + " > " + latLngBounds.southwest.latitude
                + " AND " + Constants.LocationsTable.longitude + " > " + latLngBounds.southwest.longitude
                + " AND " + Constants.LocationsTable.latitude + " < " + latLngBounds.northeast.latitude
                + " AND " + Constants.LocationsTable.longitude + " < " + latLngBounds.northeast.longitude
                + ";", null);
        Integer count = 0;
        if(resultSet==null)
            return returnMarkers;

        Log.e("SQLMethods", "resultset size: " + resultSet.getCount());

        for (int i = 0; i < (resultSet.getCount()) && !resultSet.isAfterLast(); i++) {
            if (i == 0)
                resultSet.moveToFirst();

            Integer mInt = resultSet.getInt(0);
            LatLng latLng = new LatLng(resultSet.getFloat(1), resultSet.getFloat(2));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(resultSet.getString(3));
            MarkerOptionsSpec mSpec = new MarkerOptionsSpec();
            mSpec.marker = markerOptions;
            mSpec.id = mInt;
            returnMarkers.add(mSpec);
            resultSet.moveToNext();
        }

        if(resultSet!=null)
            resultSet.close();

        return returnMarkers;

    }

    public static void addLocationInfo(SQLiteDatabase db, int id, String description, String imagesurl,
                                       float cachedRating, String cachedComments){
        if(!SQLMethods.existsRecord(Constants.LOCATIONS_INFO_TABLE_NAME,Constants.LocationsInfoTable.id_primary_key,id +"",db)) {
            ContentValues args = new ContentValues();
            args.put(Constants.LocationsInfoTable.id_primary_key, id);
            args.put(Constants.LocationsInfoTable.description, description);
            args.put(Constants.LocationsInfoTable.imagesurl, imagesurl);
            args.put(Constants.LocationsInfoTable.cached_rating, cachedRating);
            args.put(Constants.LocationsInfoTable.cached_comments, cachedComments);
            db.insert(Constants.LOCATIONS_INFO_TABLE_NAME,null, args);
        }
    }

    public static void addLocation(SQLiteDatabase db, int id, float lat, float longitude, String name, int type){
        if(!SQLMethods.existsRecord(Constants.LOCATIONS_TABLE_NAME,Constants.LocationsTable.id_primary_key,id +"",db)) {
            ContentValues args = new ContentValues();
            args.put(Constants.LocationsTable.id_primary_key, id);
            args.put(Constants.LocationsTable.latitude, lat);
            args.put(Constants.LocationsTable.longitude, longitude);
            args.put(Constants.LocationsTable.name, name);
            args.put(Constants.LocationsTable.type, type);
            db.insert(Constants.LOCATIONS_TABLE_NAME, null, args);//, Constants.LocationsTable.id_primary_key + " = '" + id + "'", null);
        }
    }

    public class Constants{
        public final static String LOCATIONS_TABLE_NAME = "LOCATIONS_TABLE";
        public class LocationsTable{
            public final static String id_primary_key = "id";
            public final static String latitude = "latitude";
            public final static String longitude = "longitude";
            public final static String name = "name";
            public final static String type = "type";

        }

        public final static String MAP_PREFERENCES_TABLE_NAME = "MAP_PREFERENCES";
        public class MapPreferencesTable{
            public final static String id_primary_key = "id";
            public final static String longitude = "longitude";
            public final static String latitude = "latitude";
            public final static String zoom = "zoom";
        }

        public final static String LOCATIONS_INFO_TABLE_NAME = "LOCATIONS_INFO";
        public class LocationsInfoTable{
            public final static String id_primary_key = "id";
            public final static String description = "description";
            public final static String imagesurl = "images_url";
            public final static String cached_rating = "cached_rating";
            public final static String cached_comments = "cached_comments";
        }

    }


    //working
    public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = null;
        try {
            db.beginTransaction();

            cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    db.setTransactionSuccessful();
                    return true;
                }
            }
            db.setTransactionSuccessful();
            return false;
        }finally{
            if(cursor != null)
                cursor.close();
            db.endTransaction();
        }
    }

    //working
    public static Boolean existsRecord(String TableName,
                                       String dbfield, String fieldValue, SQLiteDatabase db) {

        String Query = "Select * from " + TableName + " where " + dbfield + "="
                + fieldValue;

        Cursor cursor = db.rawQuery(Query, null);
        if(cursor==null)
            return false;
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }


}
