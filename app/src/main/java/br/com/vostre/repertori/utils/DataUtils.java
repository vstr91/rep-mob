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

    public static String dataParaBancoSemHora(Calendar data){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(data.getTime());
    }

    public static String toString(Calendar data, boolean horas){

        DateFormat df;

        if(horas){
            df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        } else{
            df = new SimpleDateFormat("dd/MM/yyyy");
        }

        return df.format(data.getTime());
    }

    public static String toStringSomenteHoras(Calendar data, int tipo){

        DateFormat df = null;

        switch(tipo){
            case 0:
                df = new SimpleDateFormat("HH:mm:ss");
                break;
            case 1:
                df = new SimpleDateFormat("mm:ss");
                break;
            case 2:
                df = new SimpleDateFormat("HH:mm");
                break;
        }

        return df.format(data.getTime());
    }

    public static Calendar horaParaData(String data){
        DateFormat df = new SimpleDateFormat("mm:ss");
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }

}
