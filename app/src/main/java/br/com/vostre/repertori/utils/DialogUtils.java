package br.com.vostre.repertori.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import br.com.vostre.repertori.R;

/**
 * Created by Almir on 03/02/2017.
 */

public class DialogUtils {

    public static android.app.AlertDialog criarAlerta(Context context, String titulo, String mensagem){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensagem)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static android.app.AlertDialog criarAlertaConfirmacao(Context context, String titulo, String mensagem, DialogInterface.OnClickListener listener){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("Sim", listener)
                .setNegativeButton("Não", listener);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static android.app.AlertDialog criarAlertaCarregando(Context context, String titulo, String mensagem){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false);
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
