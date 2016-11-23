package com.unfairtools.campsites.util;

import android.util.Log;

import com.unfairtools.campsites.Adapters.MarkerInfoCardAdapter;

/**
 * Created by brianroberts on 11/18/16.
 */

public interface OnLoggedInCallback {
    InfoObject result = null;
    //MarkerInfoCardAdapter markerInfoCardAdapter;

    //public OnLoggedInCallback(MarkerInfoCardAdapter adapter);//;
    //{
        //this.markerInfoCardAdapter = adapter;
    //}

//
//    public final void setResult(InfoObject res);//{
//        //this.result = res;
////        if(markerInfoCardAdapter!=null) {
////            Log.e("Callback", "NotifyDataSetChanged called");
////            markerInfoCardAdapter.notifyDataSetChanged();
////        }
//    }
//    public final InfoObject getResult(){
//        return result;
//    }
   void onFinish();
}
