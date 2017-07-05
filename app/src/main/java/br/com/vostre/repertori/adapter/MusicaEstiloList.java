package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaEstiloList extends ArrayAdapter<Musica> {

    private final Activity context;
    private final List<Musica> musicas;
    Estilo estilo;

    Musica musica = null;


    public MusicaEstiloList(Activity context, int resource, List<Musica> objects) {
        super(context, R.layout.listview_musicas_estilos, objects);
        this.context = context;
        this.musicas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_musicas_estilos, null, true);

        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewArtista = (TextView) rowView.findViewById(R.id.textViewArtista);
        TextView textViewTom = (TextView) rowView.findViewById(R.id.textViewTom);
        TextView textViewEstilo = (TextView) rowView.findViewById(R.id.textViewEstilo);

        musica = musicas.get(position);

        if(!musica.getEstilo().equals(estilo)){
            estilo = musica.getEstilo();
            textViewEstilo.setText(musica.getEstilo().getNome());
            textViewEstilo.setVisibility(View.VISIBLE);
        } else{
            textViewEstilo.setVisibility(View.GONE);
        }

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());
        textViewTom.setText(musica.getTom().equals("null") || musica.getTom().isEmpty() ? "-" : musica.getTom());

        return rowView;
    }

}
