package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/06/2017.
 */

public class MusicaProjeto extends EntidadeBase {

    private Musica musica;
    private Projeto projeto;

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(context);
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
        ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            MusicaProjeto umMusicaProjeto = new MusicaProjeto();
            umMusicaProjeto.setId(object.getString("id"));

            Musica umaMusica = new Musica();
            umaMusica.setId(object.getString("musica"));
            umaMusica = musicaDBHelper.carregar(context, umaMusica);

            umMusicaProjeto.setMusica(umaMusica);

            Projeto umProjeto = new Projeto();
            umProjeto.setId(object.getString("projeto"));
            umProjeto = projetoDBHelper.carregar(context, umProjeto);

            umMusicaProjeto.setProjeto(umProjeto);

            umMusicaProjeto.setStatus(object.getInt("status"));
            umMusicaProjeto.setDataRecebimento(Calendar.getInstance());
            umMusicaProjeto.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umMusicaProjeto.setEnviado(0);

            musicaProjetoDBHelper.salvarOuAtualizar(context, umMusicaProjeto);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", "+
                "\"musica\": \""+this.getMusica().getId()+"\", \"projeto\": \""+this.getProjeto().getId()+"\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }
    
}
