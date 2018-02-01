package com.simx.imagepicker;

import android.app.Application;
import android.content.Context;

/**
 * Created by simxd on 1/31/2018.
 */

public class Apps extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
