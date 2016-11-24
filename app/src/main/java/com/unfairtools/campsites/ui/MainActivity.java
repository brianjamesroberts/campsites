package com.unfairtools.campsites.ui;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import com.unfairtools.campsites.R;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.contracts.MapsContract;
import com.unfairtools.campsites.dagger.component.DaggerMainViewComponent;
import com.unfairtools.campsites.dagger.module.MainViewModule;
import com.unfairtools.campsites.presenters.MainActivityPresenter;
import com.unfairtools.campsites.contracts.MainContract;

import com.unfairtools.campsites.presenters.MapsPresenter;
import com.unfairtools.campsites.util.SQLMethods;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnFragmentInteractionListener,
         LocationDetailsFragment.OnFragmentInteractionListener, MainContract.View {

            public static int ToolbarMargin = 30;
            public static int ToolbarAnimationTimeMS = 250;

            @Inject
            public MainActivityPresenter presenter;

            @Inject
            SQLiteDatabase db;

            private Toolbar toolbar;
            private ActionBarDrawerToggle toggle;
            private DrawerLayout drawer;
            private String searchBarText = new String();


            public AutoCompleteTextView getToolbarEditText(){
                return (AutoCompleteTextView)toolbar.findViewById(R.id.main_search_bar);
            }

            public FrameLayout getToolbarFrameLayout(){
                return (FrameLayout) findViewById(R.id.searchbar_framelayout);
            }

            public void setToggleHamburgerVisibility(boolean visible){
                if(visible) {
                    toggle.setDrawerArrowDrawable(toggle.getDrawerArrowDrawable());
                }
            }


    public void onFragmentInteraction(Uri uri){

    }




            public void putMarkerInfoFragment(int id, String name, int type, double latPoint, double longPoint ){
                Log.e("MainActivity", "Replacing map fragment with markerInfoFragment");
                FragmentManager fm = getSupportFragmentManager();
                Fragment locationDetailsFragment = fm.findFragmentByTag("marker_info_fragment");
                if (locationDetailsFragment == null) {
                    locationDetailsFragment = LocationDetailsFragment.newInstance(id,name,type,latPoint,longPoint);
                }


                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_up,R.anim.slide_up,R.anim.slide_up,R.anim.slide_down);


                ft.add(R.id.map_cointainer, locationDetailsFragment, "marker_info_fragment").addToBackStack("map_container");
                ft.commit();

                searchBarText = ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).getText().toString();
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setText(name);
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setEnabled(false);




                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);



            }
            //called by mappresenter
            public void replaceSearchBarText(){
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setEnabled(true);
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setText(searchBarText);
            }

    public MapsContract.Presenter getMapsPresenter(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment mapFragment = fm.findFragmentByTag("map_container");
        MapsPresenter pres = ((MapFragment)mapFragment).presenter;
        return pres;
    }

    public void putMapFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment mapFragment = fm.findFragmentByTag("map_container");
        if(mapFragment == null) {
            mapFragment = new MapFragment();
            Log.e("gg","Map fragment was null");
            Bundle bundle = new Bundle();
            mapFragment.setArguments(bundle);
        }

        fm.beginTransaction()
                .replace(R.id.map_cointainer,mapFragment,"map_container")
                .addToBackStack("map_fragment").commit();
    }


    public void refreshMap(){
        ((MapFragment)getSupportFragmentManager().findFragmentByTag("map_container")).presenter.refreshPoints();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        for(int i = 0; i < menu.size(); i++){
            menu.getItem(i).setVisible(false);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//      ((BaseApplication)getApplication()).getServicesComponent().inject(this);

        DaggerMainViewComponent.builder()
                .mainViewModule(new MainViewModule(this, (BaseApplication)getApplication()))
                .build()
                .inject(this);




        SQLMethods.initDatabaseCheck(db);

        putMapFragment();

        setContentView(R.layout.activity_main);


        ((FloatingActionButton)findViewById(R.id.fab)).setOnClickListener(new FloatingActionButton.OnClickListener(){
            public void onClick(View v){

                Log.e("MainActivity", "CLICK");

            }
        });

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);

        LayoutInflater mInflater= LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_searchfield, null);

        toolbar.addView(mCustomView);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    onBackPressed();
                }
                else{

                    if (!drawer.isDrawerOpen(GravityCompat.START)) {
                        Log.e("MainActivity","opening drawer");
                        drawer.openDrawer(GravityCompat.START);
                    } else {
                        //nBackPressed();
                    }
                }

                Log.e("MainActivity","Navigation clicked");
            }
        });



        //toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        presenter.notifyViewIsReady();
    }

public Context getViewContext(){
    return this;
}

            @Override
            public boolean onSupportNavigateUp(){
                Log.e("MainActivity","Up clicked");
                return true;
            }



            @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.e("MainActivity","closing drawer from back button");
            drawer.closeDrawer(GravityCompat.START);
        } else if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            presenter.animateToolbarMargin(MainActivity.ToolbarMargin);
                super.onBackPressed();
        }else {
                Log.e("Main Activity","Back pressed");
                this.finish();
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

        Log.e("MainActivity","Id is " + id + "  (menuItem id)");

        if(id == android.R.id.home){
            Log.e("MainActivity","up pressed!");
        }

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
