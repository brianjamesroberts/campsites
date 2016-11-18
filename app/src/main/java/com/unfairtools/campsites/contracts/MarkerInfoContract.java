package com.unfairtools.campsites.contracts;

import com.unfairtools.campsites.base.BasePresenter;
import com.unfairtools.campsites.base.BaseView;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.MarkerInfoObject;

/**
 * Created by brianroberts on 11/8/16.
 */

public interface MarkerInfoContract {

    interface View extends BaseView<MarkerInfoContract.Presenter> {

        void takeInfo(MarkerInfoObject info, InfoObject inf);
        void takePrelimInfo(InfoObject info);

    }

    interface Presenter extends BasePresenter {

        InfoObject getMarkerInfo(int id);

        boolean acceptRating(int rating, int id);

        public void setMarkerIdAndName(InfoObject inf);


    }
}
