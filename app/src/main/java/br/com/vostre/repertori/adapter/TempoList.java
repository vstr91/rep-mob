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
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/08/2015.
 */
public class TempoList extends ArrayAdapter<TempoMusicaEvento> {

    private final Activity context;
    private final List<TempoMusicaEvento> tmes;


    public TempoList(Activity context, int resource, List<TempoMusicaEvento> objects) {
        super(context, R.layout.listview_tempos, objects);
        this.context = context;
        this.tmes = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_tempos, null, true);
        TextView textViewTempo = (TextView) rowView.findViewById(R.id.textViewTempo);
        TextView textViewData = (TextView) rowView.findViewById(R.id.textViewData);

        TempoMusicaEvento tme = tmes.get(position);

        textViewTempo.setText(DataUtils.toStringSomenteHoras(tme.getTempo(), 1));
        textViewData.setText(DataUtils.toString(tme.getUltimaAlteracao(), false));

        return rowView;
    }

}
