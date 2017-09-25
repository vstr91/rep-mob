package br.com.vostre.repertori.utils;

import android.content.Context;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 25/09/2017.
 */

public class CsvUtils {

    public static String dadoParaCsv(Context context, String dir, List<Musica> dados){

        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(dir)));

            for(Musica m : dados){
                csvWriter.writeNext(m.toCsv(context).split(";"));
            }

            csvWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
