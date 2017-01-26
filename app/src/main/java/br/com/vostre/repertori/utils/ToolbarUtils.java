package br.com.vostre.repertori.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.vostre.repertori.R;

/**
 * Created by Almir on 16/12/2015.
 */
public class ToolbarUtils {

    static View.OnClickListener mListener;

    public static void preparaMenu(Menu menu, Activity activity, View.OnClickListener listener){

//        activity.getMenuInflater().inflate(R.menu.main, menu);
//
//        MenuItem itemEdit = menu.findItem(R.id.icon_edit);
//        MenuItemCompat.getActionView(itemEdit).setOnClickListener(listener);
//
//        mListener = listener;

    }

    public static void preparaMenuEvento(Menu menu, Activity activity, View.OnClickListener listener){

        activity.getMenuInflater().inflate(R.menu.evento_detalhe, menu);

        MenuItem itemEdit = menu.findItem(R.id.icon_edit);
        View a = MenuItemCompat.getActionView(itemEdit);
        a.setOnClickListener(listener);

        mListener = listener;

    }

    public static void onMenuItemClick(View v, Activity activity){
//        switch(v.getId()){
//            case R.id.icon_edit:
//                Intent intent = new Intent(activity, Mensagens.class);
//                activity.startActivity(intent);
//                break;
//        }
    }

}
