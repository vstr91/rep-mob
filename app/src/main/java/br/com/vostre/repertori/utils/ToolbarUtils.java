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

    static TextView textViewBadgeMsg;
    static ImageButton imageButtonMsg;
    static View.OnClickListener mListener;

    public static void preparaMenu(Menu menu, Activity activity, View.OnClickListener listener){

        activity.getMenuInflater().inflate(R.menu.main, menu);

//        MenuItem itemMsg = menu.findItem(R.id.icon_msg);
//        MenuItemCompat.getActionView(itemMsg).setOnClickListener(listener);
//
//        mListener = listener;
//
//        int qtdMensagensNaoLidas = MessageUtils.getQuantidadeMensagensNaoLidas(activity);
//        textViewBadgeMsg = (TextView) MenuItemCompat.getActionView(itemMsg).findViewById(R.id.textViewBadgeMsg);
//        imageButtonMsg = (ImageButton) MenuItemCompat.getActionView(itemMsg).findViewById(R.id.imageButtonMsg);
//        imageButtonMsg.setOnClickListener(mListener);
//
//        atualizaBadge(qtdMensagensNaoLidas);

    }

    public static void preparaMenuMensagem(Menu menu, Activity activity, View.OnClickListener listener){

//        activity.getMenuInflater().inflate(R.menu.main, menu);
//
//        MenuItem itemMsg = menu.findItem(R.id.icon_msg);
//        MenuItemCompat.getActionView(itemMsg).setOnClickListener(listener);
//
//        mListener = listener;
//
//        int qtdMensagensNaoLidas = MessageUtils.getQuantidadeMensagensNaoLidas(activity);
//        textViewBadgeMsg = (TextView) MenuItemCompat.getActionView(itemMsg).findViewById(R.id.textViewBadgeMsg);
//        imageButtonMsg = (ImageButton) MenuItemCompat.getActionView(itemMsg).findViewById(R.id.imageButtonMsg);
//        imageButtonMsg.setOnClickListener(mListener);
//
//        atualizaBadge(qtdMensagensNaoLidas);

    }

    public static void onMenuItemClick(View v, Activity activity){
        switch(v.getId()){
//            case R.id.textViewBadgeMsg:
//            case R.id.imageButtonMsg:
//            case R.id.icon_msg:
//                Intent intent = new Intent(activity, Mensagens.class);
//                activity.startActivity(intent);
//                break;
        }
    }

}
