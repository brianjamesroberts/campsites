package com.unfairtools.campsites.util.callbacks;

import android.util.Log;

import com.unfairtools.campsites.Adapters.MarkerInfoCardAdapter;
import com.unfairtools.campsites.util.InfoObject;

/**
 * Created by brianroberts on 11/18/16.
 */

public interface OnLoggedInCallback {
    InfoObject result = null;
   void onFinish();
}
