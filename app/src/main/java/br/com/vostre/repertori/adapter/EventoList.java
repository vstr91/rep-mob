package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/08/2015.
 */
public class EventoList extends ArrayAdapter<Evento> {

    private final Activity context;
    private final List<Evento> eventos;


    public EventoList(Activity context, int resource, List<Evento> objects) {
        super(context, R.layout.listview_eventos, objects);
        this.context = context;
        this.eventos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_eventos, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewData = (TextView) rowView.findViewById(R.id.textViewData);
        TextView textViewTipoEvento = (TextView) rowView.findViewById(R.id.textViewTipoEvento);

        Evento evento = eventos.get(position);

        textViewNome.setText(evento.getNome());
        textViewData.setText(DataUtils.toString(evento.getData(), true));
        textViewTipoEvento.setText(evento.getTipoEvento().getNome());

        return rowView;
    }

}
