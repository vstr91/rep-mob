package br.com.vostre.repertori.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Almir on 24/11/2015.
 */
public class SnackbarHelper {

    public static void notifica(View rootView, CharSequence texto, int duracao){
        Snackbar.make(rootView, texto, duracao).show();
    }

}
