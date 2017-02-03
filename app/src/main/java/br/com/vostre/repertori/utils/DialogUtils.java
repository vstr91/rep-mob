package br.com.vostre.repertori.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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

}
