package com.unfairtools.campsites.contracts;

import android.widget.FrameLayout;

import com.unfairtools.campsites.base.BasePresenter;
import com.unfairtools.campsites.base.BaseView;

/**
 * Created by brianroberts on 11/15/16.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {

        public FrameLayout getToolbarFrameLayout();



    }

    interface Presenter extends BasePresenter {

        public void animateToolbarMargin(int margin);

    }
}
