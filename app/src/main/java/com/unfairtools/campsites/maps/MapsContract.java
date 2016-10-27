package com.unfairtools.campsites.maps;

import com.unfairtools.campsites.base.BasePresenter;
import com.unfairtools.campsites.base.BaseView;

import java.util.List;

/**
 * Created by brianroberts on 10/26/16.
 */

public interface MapsContract {

    interface View extends BaseView<Presenter> {

        //void showCards(List<ScannedCode> codes);

    }

    interface Presenter extends BasePresenter {

        void loadCards();

//        void addNewCard(ScannedCode code);

  //      void deleteCard(ScannedCode code);

    }
}
