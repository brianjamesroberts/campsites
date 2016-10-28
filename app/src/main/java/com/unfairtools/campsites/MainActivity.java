package com.unfairtools.campsites;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.ui.MapFragment;
import com.unfairtools.campsites.util.SQLMethods;

import java.net.URI;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnFragmentInteractionListener
        {


    public void onFragmentInteraction(Uri uri){

    }

    @Inject
    SQLiteDatabase db;


    public void putMapFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment mapFragment = fm.findFragmentByTag("map_container");
        if(mapFragment == null)
            mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        mapFragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.map_cointainer,mapFragment,"invites_fragment")
                .addToBackStack("game_fragment").commit();
    }

    public void initDatabaseCheck(){
        if(!SQLMethods.doesTableExist(db,SQLMethods.Constants.LOCATIONS_TABLE_NAME)){
            Log.e("MainActivity","Creating table " + SQLMethods.Constants.LOCATIONS_TABLE_NAME);
            db.beginTransaction();
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                    SQLMethods.Constants.LOCATIONS_TABLE_NAME +
                    "("
                            +  SQLMethods.Constants.LocationsTable.id_primary_key + " INTEGER PRIMARY KEY,"
                            +  SQLMethods.Constants.LocationsTable.latitude +" REAL,"
                            +  SQLMethods.Constants.LocationsTable.longitude +" REAL,"
                            +  SQLMethods.Constants.LocationsTable.name + " VARCHAR,"
                            +  SQLMethods.Constants.LocationsTable.type+" INTEGER"
                            +");");
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        if(!SQLMethods.doesTableExist(db,SQLMethods.Constants.MAP_PREFERENCES_TABLE_NAME)){
            Log.e("MainActivity", "Creating table " + SQLMethods.Constants.MAP_PREFERENCES_TABLE_NAME);
            db.beginTransaction();
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                    SQLMethods.Constants.MAP_PREFERENCES_TABLE_NAME +
                    "("
                            + SQLMethods.Constants.MapPreferencesTable.latitude + " REAL,"
                            + SQLMethods.Constants.MapPreferencesTable.longitude + " REAL,"
                            +SQLMethods.Constants.MapPreferencesTable.zoom + " REAL"
                            + ");");
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        if(!SQLMethods.doesTableExist(db,SQLMethods.Constants.LOCATIONS_INFO_TABLE_NAME)){
            Log.e("MainActivity", "Creating table " + SQLMethods.Constants.LOCATIONS_INFO_TABLE_NAME);
            db.beginTransaction();
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            SQLMethods.Constants.LOCATIONS_INFO_TABLE_NAME +
                            "("
                    + SQLMethods.Constants.LocationsInfoTable.id_primary_key + " INTEGER PRIMARY KEY,"
                    + SQLMethods.Constants.LocationsInfoTable.description + " VARCHAR,"
                    + SQLMethods.Constants.LocationsInfoTable.imagesurl + " VARCHAR,"
                    + SQLMethods.Constants.LocationsInfoTable.cached_rating + " REAL,"
                    + SQLMethods.Constants.LocationsInfoTable.cached_comments + " VARCHAR"
                    +");"
            );
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseApplication)getApplication()).getServicesComponent().inject(this);

        initDatabaseCheck();

        putMapFragment();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
