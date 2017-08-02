package br.com.vostre.repertori.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.vostre.repertori.model.dao.ParametroDBHelper;

/**
 * Created by Almir on 02/08/2017.
 */

public class ParametrosUtils {

    public static String getDataUltimoAcesso(Context context){
        DateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
        DateFormat dateFormatWeb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);

        String ultimaData = parametroDBHelper.carregarUltimoAcesso(context);

        try {

            if(!ultimaData.equals("-")){
                data = dateFormat.parse(ultimaData.replace(",", "").replace("%20", " "));
            }

        } catch(ParseException ex){
            ex.printStackTrace();
        }

        if(null != data){
            return dateFormatWeb.format(data).replace(" ", "%20");
        } else{
            return "-";
        }

    }

    public static void setDataUltimoAcesso(Context context, String dataUltimoAcesso){

        if(dataUltimoAcesso != null){
            ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);
            parametroDBHelper.gravarUltimoAcesso(context, dataUltimoAcesso);
        }

    }

}
