package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaList extends ArrayAdapter<Musica> {

    private final Activity context;
    private final List<Musica> musicas;

    Musica musica = null;


    public MusicaList(Activity context, int resource, List<Musica> objects) {
        super(context, R.layout.listview_musicas, objects);
        this.context = context;
        this.musicas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        musica = musicas.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_musicas, null, true);

        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewArtista = (TextView) rowView.findViewById(R.id.textViewArtista);
        TextView textViewTom = (TextView) rowView.findViewById(R.id.textViewTom);
        TextView textViewEstilo = (TextView) rowView.findViewById(R.id.textViewEstilo);

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());
        textViewTom.setText(musica.getTom().equals("null") || musica.getTom().isEmpty() ? "-" : musica.getTom());
        textViewEstilo.setText(musica.getEstilo().getNome());

        return rowView;
    }

}
