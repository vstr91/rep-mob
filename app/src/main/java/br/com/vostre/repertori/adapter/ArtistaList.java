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
 * Created by Almir on 27/08/2015.
 */
public class ArtistaList extends ArrayAdapter<Artista> {

    private final Activity context;
    private final List<Artista> artistas;
    private final boolean completo;


    public ArtistaList(Activity context, int resource, List<Artista> objects, boolean completo) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.artistas = objects;
        this.completo = completo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_artistas, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewMusicas = (TextView) rowView.findViewById(R.id.textViewMusicas);

        Artista artista = artistas.get(position);

        textViewNome.setText(artista.getNome());

        if(completo){
            textViewMusicas.setText(artista.contarMusicasAtivas(getContext())+" música(s)");
        } else{
            textViewMusicas.setVisibility(View.GONE);
        }



        return rowView;
    }

}
