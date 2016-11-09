package com.unfairtools.campsites.maps;

import com.unfairtools.campsites.base.BasePresenter;
import com.unfairtools.campsites.base.BaseView;
import com.unfairtools.campsites.util.InfoObject;

/**
 * Created by brianroberts on 11/8/16.
 */

public interface MarkerInfoContract {

    interface View extends BaseView<MarkerInfoContract.Presenter> {

        void takeInfo(InfoObject info);
        void takePrelimInfo(InfoObject info);

    }

    interface Presenter extends BasePresenter {

        InfoObject getMarkerInfo(int id);

        boolean acceptRating(int rating, int id);

        public void setMarkerIdAndName(int idd, String name);


    }
}
