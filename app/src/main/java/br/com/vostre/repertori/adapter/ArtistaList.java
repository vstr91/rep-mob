package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Cefet on 27/08/2015.
 */
public class ArtistaList extends ArrayAdapter<Artista> {

    private final Activity context;
    private final List<Artista> artistas;


    public ArtistaList(Activity context, int resource, List<Artista> objects) {
        super(context, R.layout.listview_artistas, objects);
        this.context = context;
        this.artistas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_artistas, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);

        Artista artista = artistas.get(position);

        textViewNome.setText(artista.getNome());

        return rowView;
    }

}
