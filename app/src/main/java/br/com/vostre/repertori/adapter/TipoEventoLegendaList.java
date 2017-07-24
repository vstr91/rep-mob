package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.TipoEvento;

/**
 * Created by Almir on 27/08/2015.
 */
public class TipoEventoLegendaList extends ArrayAdapter<TipoEvento> {

    private final Activity context;
    private final List<TipoEvento> tiposEvento;


    public TipoEventoLegendaList(Activity context, int resource, List<TipoEvento> objects) {
        super(context, R.layout.listview_tipos_evento_legenda, objects);
        this.context = context;
        this.tiposEvento = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_tipos_evento_legenda, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        ImageView viewCor = (ImageView) rowView.findViewById(R.id.viewCor);

        TipoEvento tipoEvento = tiposEvento.get(position);

        textViewNome.setText(tipoEvento.getNome());
        viewCor.setBackgroundColor(Color.parseColor("#66"+tipoEvento.getCor().replace("#", "")));

        return rowView;
    }

}
