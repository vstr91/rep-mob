package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.TempoBlocoRepertorio;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/08/2015.
 */
public class TempoBlocoList extends ArrayAdapter<TempoBlocoRepertorio> {

    private final Activity context;
    private final List<TempoBlocoRepertorio> tbrs;


    public TempoBlocoList(Activity context, int resource, List<TempoBlocoRepertorio> objects) {
        super(context, R.layout.listview_tempos, objects);
        this.context = context;
        this.tbrs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_tempos, null, true);
        TextView textViewTempo = (TextView) rowView.findViewById(R.id.textViewTempo);
        TextView textViewData = (TextView) rowView.findViewById(R.id.textViewData);
        ImageView imageViewPlay = (ImageView) rowView.findViewById(R.id.imageViewPlay);

        TempoBlocoRepertorio tbr = tbrs.get(position);

        textViewTempo.setText(DataUtils.toStringSomenteHoras(tbr.getTempo(), 1));
        textViewData.setText(DataUtils.toString(tbr.getDataCadastro(), false));

        if(tbr.getAudio().isEmpty() || tbr.getAudio().equals("null")){
            imageViewPlay.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }

}
