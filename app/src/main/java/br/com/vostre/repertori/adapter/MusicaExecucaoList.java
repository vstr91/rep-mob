package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaExecucao;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaExecucaoList extends ArrayAdapter<MusicaExecucao> {

    private final Activity context;
    private final List<MusicaExecucao> musicas;

    Musica musica = null;
    Calendar execucao = null;


    public MusicaExecucaoList(Activity context, int resource, List<MusicaExecucao> objects) {
        super(context, R.layout.listview_musicas_execucoes, objects);
        this.context = context;
        this.musicas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        musica = musicas.get(position).getMusica();
        execucao = musicas.get(position).getExecucao();

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_musicas_execucoes, null, true);

        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewArtista = (TextView) rowView.findViewById(R.id.textViewArtista);
        TextView textViewExecucao = (TextView) rowView.findViewById(R.id.textViewExecucao);

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());

        if(execucao != null){
            textViewExecucao.setText(DataUtils.toString(execucao, true));
        } else{
            textViewExecucao.setText("-");
        }

        return rowView;
    }

}
