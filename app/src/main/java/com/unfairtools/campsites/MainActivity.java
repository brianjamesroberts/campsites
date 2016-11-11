package com.unfairtools.campsites;


import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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

import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.ui.LocationDetailsFragment;
import com.unfairtools.campsites.ui.MapFragment;
import com.unfairtools.campsites.util.SQLMethods;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapFragment.OnFragmentInteractionListener,
         LocationDetailsFragment.OnFragmentInteractionListener
        {

            @Inject
            SQLiteDatabase db;

            private Toolbar toolbar;
            private ActionBarDrawerToggle toggle;
            private DrawerLayout drawer;
            private String searchBarText = new String();


            public void setToggleHamburgerVisibility(boolean visible){

                if(visible) {
                    toggle.setDrawerArrowDrawable(toggle.getDrawerArrowDrawable());
                }

            }



    public void onFragmentInteraction(Uri uri){

    }




            public void putMarkerInfoFragment(int id, String name){
                Log.e("MainActivity", "Replacing map fragment with markerInfoFragment");
                FragmentManager fm = getSupportFragmentManager();
                Fragment locationDetailsFragment = fm.findFragmentByTag("marker_info_fragment");
                if (locationDetailsFragment == null) {
                    locationDetailsFragment = LocationDetailsFragment.newInstance(id);
                }


                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                ft.setCustomAnimations(R.anim.slide_up,R.anim.slide_up,R.anim.slide_up,R.anim.slide_down);


//                fm.beginTransaction().replace(R.id.map_cointainer,locationDetailsFragment,"marker_info_fragment")
//                        .addToBackStack("map_container")
//                        .commit();

                ft.add(R.id.map_cointainer, locationDetailsFragment, "marker_info_fragment").addToBackStack("map_container");
                //ft.replace(R.id.map_cointainer, locationDetailsFragment, "marker_info_fragment").addToBackStack("map_container");
                ft.commit();

                searchBarText = ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).getText().toString();
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setText(name);
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setEnabled(false);




                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);

                //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);




            }
            //called by mappresenter
            public void replaceSearchBarText(){
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setEnabled(true);
                ((AutoCompleteTextView)findViewById(R.id.main_search_bar)).setText(searchBarText);
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

        ((BaseApplication)getApplication()).getServicesComponent().inject(this);

        SQLMethods.initDatabaseCheck(db);

        putMapFragment();

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);

        LayoutInflater mInflater= LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_searchfield, null);

        toolbar.addView(mCustomView);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        //ImageButton hamburgerButton = (ImageButton)findViewById(R.id.hamburger_button);

//DrawerArrowDrawable dad = new DrawerArrowDrawable(this);
  //     dad.
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
//                dad, R.string.drawer_open,
//                R.string.drawer_close);


//        e(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout,
//                DrawerArrowDrawable slider, @StringRes int openDrawerContentDescRes,
//        @StringRes int closeDrawerContentDescRes)

        //ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,null,drawer,dad,R.string.drawer_open,R.string.drawer_close);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
//                null, R.string.drawer_open, R.string.drawer_close) {
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                Log.e("Drawer","Drawer closed");
//                //getActionBar().setTitle();
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                Log.e("Drawer","Drawer opened");
//                //getActionBar().setTitle(mDrawerTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//        };

        // Set the drawer toggle as the DrawerListener
       // drawer.setDrawerListener(mDrawerToggle);



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
