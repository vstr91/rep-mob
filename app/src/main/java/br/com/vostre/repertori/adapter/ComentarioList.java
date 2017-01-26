package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Cefet on 27/08/2015.
 */
public class ComentarioList extends ArrayAdapter<ComentarioEvento> {

    private final Activity context;
    private final List<ComentarioEvento> comentarios;


    public ComentarioList(Activity context, int resource, List<ComentarioEvento> objects) {
        super(context, R.layout.listview_comentarios, objects);
        this.context = context;
        this.comentarios = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_comentarios, null, true);
        TextView textViewTexto = (TextView) rowView.findViewById(R.id.textViewTexto);
        TextView textViewData = (TextView) rowView.findViewById(R.id.textViewData);

        ComentarioEvento comentario = comentarios.get(position);

        textViewTexto.setText(comentario.getTexto());
        textViewData.setText(DataUtils.toString(comentario.getUltimaAlteracao(), true));

        return rowView;
    }

}
