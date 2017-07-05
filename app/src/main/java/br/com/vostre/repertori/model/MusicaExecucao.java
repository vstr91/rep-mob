package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/06/2017.
 */

public class MusicaExecucao implements Comparable<MusicaExecucao> {

    private Musica musica;
    private Calendar execucao;

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public Calendar getExecucao() {
        return execucao;
    }

    public void setExecucao(Calendar execucao) {
        this.execucao = execucao;
    }

    @Override
    public int compareTo(@NonNull MusicaExecucao m) {
        Calendar date = m.getExecucao();
        Calendar exec = getExecucao();

        if(date == null){
            date = Calendar.getInstance();
            date.add(Calendar.DAY_OF_YEAR, -1000);
        }

        if(exec == null){
            exec = Calendar.getInstance();
            exec.add(Calendar.DAY_OF_YEAR, -1000);
        }

        return exec.compareTo(date);

    }
}
