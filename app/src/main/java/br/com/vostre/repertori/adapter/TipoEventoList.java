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
import br.com.vostre.repertori.model.TipoEvento;

/**
 * Created by Cefet on 27/08/2015.
 */
public class TipoEventoList extends ArrayAdapter<TipoEvento> {

    private final Activity context;
    private final List<TipoEvento> tiposEvento;


    public TipoEventoList(Activity context, int resource, List<TipoEvento> objects) {
        super(context, R.layout.listview_tipos_evento, objects);
        this.context = context;
        this.tiposEvento = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_tipos_evento, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);

        TipoEvento tipoEvento = tiposEvento.get(position);

        textViewNome.setText(tipoEvento.getNome());

        return rowView;
    }

}
