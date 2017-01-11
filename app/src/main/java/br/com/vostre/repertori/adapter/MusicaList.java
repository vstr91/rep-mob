package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Cefet on 27/08/2015.
 */
public class MusicaList extends ArrayAdapter<Musica> {

    private final Activity context;
    private final List<Musica> musicas;


    public MusicaList(Activity context, int resource, List<Musica> objects) {
        super(context, R.layout.listview_musicas, objects);
        this.context = context;
        this.musicas = objects;
    }

    @Override
    public void add(Musica row) {
        super.add(row);
        musicas.add(row);
    }

    @Override
    public void insert(Musica row, int position) {
        super.insert(row, position);
        musicas.add(position, row);
    }

    @Override
    public void remove(Musica row) {
        super.remove(row);
        musicas.remove(row);
    }

    @Override
    public void clear() {
        super.clear();
        musicas.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_musicas, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewArtista = (TextView) rowView.findViewById(R.id.textViewArtista);
        TextView textViewTom = (TextView) rowView.findViewById(R.id.textViewTom);

        Musica musica = musicas.get(position);

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());
        textViewTom.setText(musica.getTom());

        return rowView;
    }

}
