package br.com.vostre.repertori.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Almir on 10/04/2016.
 */
public class BroadcastUtils {

    public static void registraReceiver(Activity activity, BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, new IntentFilter("MensagensService"));
    }

    public static void removeRegistroReceiver(Activity activity, BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver);
    }

}
