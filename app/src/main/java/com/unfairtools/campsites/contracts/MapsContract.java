package com.unfairtools.campsites.contracts;



import android.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.unfairtools.campsites.ui.MainActivity;
import com.unfairtools.campsites.base.BasePresenter;
import com.unfairtools.campsites.base.BaseView;

import java.util.List;

/**
 * Created by brianroberts on 10/26/16.
 */

public interface MapsContract {

    interface View extends BaseView<Presenter> {

        void placePoints(List<Integer> points);

        void zoomLocation(int x, int y, int zoom);

        MainActivity getMainActivity();

        FragmentManager getFragmentManager2();

    }

    interface MarkerInfoView extends BaseView<MarkerInfoPresenter> {
        MainActivity getMainActivity();
    }

    interface MarkerInfoPresenter extends BasePresenter {
    }

    interface Presenter extends BasePresenter {


        void sendMapTo(double lat, double longitude);
        void takeMap(GoogleMap gm);
        void initZoom();
        void initPoints();
        void showInfo(SupportMapFragment mapFragment);
        boolean showMarkerTag(int id);
        void setShowTagId(int id);


//        void addNewCard(ScannedCode code);

  //      void deleteCard(ScannedCode code);

    }
}
