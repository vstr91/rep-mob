package br.com.vostre.repertori.utils;

import android.app.ActivityManager;

/**
 * Created by Almir on 11/03/2015.
 */
public class ServiceUtils {

    public boolean isMyServiceRunning(Class<?> serviceClass, ActivityManager manager) {
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
