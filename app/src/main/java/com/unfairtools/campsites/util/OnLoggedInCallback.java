package com.unfairtools.campsites.util;

/**
 * Created by brianroberts on 11/18/16.
 */

public abstract class OnLoggedInCallback {
private InfoObject result;

    public final void setResult(InfoObject res){
        this.result = res;
    }
    public final InfoObject getResult(){
        return result;
    }
    public abstract void onFinish();
}
