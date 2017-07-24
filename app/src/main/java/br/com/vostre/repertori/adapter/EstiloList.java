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
import br.com.vostre.repertori.model.Estilo;

/**
 * Created by Almir on 27/08/2015.
 */
public class EstiloList extends ArrayAdapter<Estilo> {

    private final Activity context;
    private final List<Estilo> estilos;


    public EstiloList(Activity context, int resource, List<Estilo> objects) {
        super(context, R.layout.listview_estilos, objects);
        this.context = context;
        this.estilos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_estilos, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewMusicas = (TextView) rowView.findViewById(R.id.textViewMusicas);

        Estilo estilo = estilos.get(position);

        textViewNome.setText(estilo.getNome());
        textViewMusicas.setText(estilo.contarMusicasAtivas(getContext())+" música(s)");

        return rowView;
    }

}
