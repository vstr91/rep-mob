package br.com.vostre.repertori.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Almir on 30/12/2016.
 */

public class DataUtils {

    public static String dataParaBanco(Calendar data){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return df.format(data.getTime());
    }

    public static Calendar bancoParaData(String data){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }

    public static Calendar apiParaData(String data){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }

    public static String toString(Calendar data){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return df.format(data.getTime());
    }

}
