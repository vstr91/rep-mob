package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/08/2015.
 */
public class RepertorioList extends ArrayAdapter<Repertorio> {

    private final Activity context;
    private final List<Repertorio> repertorios;


    public RepertorioList(Activity context, int resource, List<Repertorio> objects) {
        super(context, R.layout.listview_repertorios, objects);
        this.context = context;
        this.repertorios = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_repertorios, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);

        Repertorio repertorio = repertorios.get(position);

        textViewNome.setText(repertorio.getNome());

        return rowView;
    }

}
