package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.TempoBlocoRepertorio;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class TempoBlocoRepertorioDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public TempoBlocoRepertorioDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(TempoBlocoRepertorio tbr){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(tbr.getId() != null){
            cv.put("_id", tbr.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("tempo", DataUtils.dataParaBanco(tbr.getTempo()));
        cv.put("id_bloco_repertorio", tbr.getBlocoRepertorio().getId());
        cv.put("status", tbr.getStatus());
        cv.put("enviado", tbr.getEnviado());
        cv.put("audio", tbr.getAudio());
        cv.put("audio_enviado", tbr.getAudioEnviado());
        cv.put("audio_recebido", tbr.getAudioRecebido());

        if(tbr.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(tbr.getDataCadastro()));
        }

        if(tbr.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(tbr.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(tbr.getUltimaAlteracao()));

        if(database.update("tempo_bloco_repertorio", cv, "_id = '"+tbr.getId()+"'", null) < 1){
            retorno = database.insert("tempo_bloco_repertorio", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("tempo_bloco_repertorio", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<TempoBlocoRepertorio> listarTodosPorBlocoRepertorio(BlocoRepertorio blocoRepertorio, int limite){
        Cursor cursor = database.rawQuery("SELECT tbr._id, tbr.tempo, tbr.id_bloco_repertorio, tbr.status, tbr.data_cadastro, tbr.data_recebimento, " +
                "tbr.ultima_alteracao, tbr.audio, tbr.audio_enviado, tbr.audio_recebido FROM tempo_bloco_repertorio tbr INNER JOIN bloco_repertorio br ON br._id = tbr.id_bloco_repertorio " +
                "WHERE br._id = ? AND tbr.status = 0 " +
                "ORDER BY tbr.ultima_alteracao LIMIT "+limite, new String[]{blocoRepertorio.getId()});
        List<TempoBlocoRepertorio> tbrs = new ArrayList<TempoBlocoRepertorio>();

        if(cursor.moveToFirst()){

            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                TempoBlocoRepertorio tbr = new TempoBlocoRepertorio();
                tbr.setId(cursor.getString(0));

                tbr.setTempo(DataUtils.bancoParaData(cursor.getString(1)));

                BlocoRepertorio umBlocoRepertorio = new BlocoRepertorio();
                umBlocoRepertorio.setId(cursor.getString(2));
                umBlocoRepertorio = blocoRepertorioDBHelper.carregar(context, umBlocoRepertorio);
                tbr.setBlocoRepertorio(umBlocoRepertorio);

                tbr.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    tbr.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    tbr.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    tbr.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                }

                tbr.setAudio(cursor.getString(7));

                tbr.setAudioEnviado(cursor.getInt(8));
                tbr.setAudioRecebido(cursor.getInt(9));

                tbrs.add(tbr);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tbrs;
    }

    public List<TempoBlocoRepertorio> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, tempo, id_bloco_repertorio, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, audio FROM tempo_bloco_repertorio WHERE enviado = -1 ORDER BY data_cadastro DESC", null);
        List<TempoBlocoRepertorio> tbrs = new ArrayList<TempoBlocoRepertorio>();

        if(cursor.moveToFirst()){

            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                TempoBlocoRepertorio tbr = new TempoBlocoRepertorio();
                tbr.setId(cursor.getString(0));

                tbr.setTempo(DataUtils.bancoParaData(cursor.getString(1)));

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(2));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                tbr.setBlocoRepertorio(blocoRepertorio);

                tbr.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    tbr.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    tbr.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    tbr.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                }

                tbr.setAudio(cursor.getString(7));

                tbrs.add(tbr);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tbrs;
    }

    public List<TempoBlocoRepertorio> listarTodosAEnviarAudio(){
        Cursor cursor = database.rawQuery("SELECT _id, tempo, id_bloco_repertorio, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, audio FROM tempo_bloco_repertorio WHERE audio_enviado = -1 AND status = 0 ORDER BY data_cadastro DESC", null);
        List<TempoBlocoRepertorio> tbrs = new ArrayList<TempoBlocoRepertorio>();

        if(cursor.moveToFirst()){

            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                TempoBlocoRepertorio tbr = new TempoBlocoRepertorio();
                tbr.setId(cursor.getString(0));

                tbr.setTempo(DataUtils.bancoParaData(cursor.getString(1)));

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(2));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                tbr.setBlocoRepertorio(blocoRepertorio);

                tbr.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    tbr.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    tbr.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    tbr.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                }

                tbr.setAudio(cursor.getString(7));

                tbrs.add(tbr);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tbrs;
    }

    public List<TempoBlocoRepertorio> listarTodosAReceberAudio(){
        Cursor cursor = database.rawQuery("SELECT _id, tempo, id_bloco_repertorio, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, audio FROM tempo_bloco_repertorio WHERE audio_recebido = -1 ORDER BY data_cadastro DESC", null);
        List<TempoBlocoRepertorio> tbrs = new ArrayList<TempoBlocoRepertorio>();

        if(cursor.moveToFirst()){

            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                TempoBlocoRepertorio tbr = new TempoBlocoRepertorio();
                tbr.setId(cursor.getString(0));

                tbr.setTempo(DataUtils.bancoParaData(cursor.getString(1)));

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(2));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                tbr.setBlocoRepertorio(blocoRepertorio);

                tbr.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    tbr.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    tbr.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    tbr.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                }

                tbr.setAudio(cursor.getString(7));

                tbrs.add(tbr);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tbrs;
    }

    public int sinalizaEnvioAudio(String audio){
        ContentValues cv = new ContentValues();
        cv.put("audio_enviado", 0);

        int retorno = database.update("tempo_bloco_repertorio", cv, "audio = \""+audio+"\"", null);
        database.close();
        return retorno;
    }

    public int sinalizaRecebimentoAudio(String audio){
        ContentValues cv = new ContentValues();
        cv.put("audio_recebido", 0);

        int retorno = database.update("tempo_bloco_repertorio", cv, "audio = \""+audio+"\"", null);
        database.close();
        return retorno;
    }

    public TempoBlocoRepertorio carregarPorAudio(String audio){
        Cursor cursor = database.rawQuery("SELECT tbr._id, tbr.tempo, tbr.id_bloco_repertorio, tbr.status, tbr.data_cadastro, tbr.data_recebimento, " +
                "tbr.ultima_alteracao, tbr.audio FROM tempo_bloco_repertorio tbr INNER JOIN bloco_repertorio br ON br._id = tbr.id_bloco_repertorio WHERE tbr.audio = ? " +
                "ORDER BY tbr.ultima_alteracao ", new String[]{audio});

        TempoBlocoRepertorio tbr = null;

        if(cursor.moveToFirst()){

            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                tbr = new TempoBlocoRepertorio();
                tbr.setId(cursor.getString(0));

                tbr.setTempo(DataUtils.bancoParaData(cursor.getString(1)));

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(2));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                tbr.setBlocoRepertorio(blocoRepertorio);

                tbr.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    tbr.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    tbr.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    tbr.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                }

                tbr.setAudio(cursor.getString(7));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tbr;
    }

}
