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
import br.com.vostre.repertori.model.Casa;

/**
 * Created by Almir on 27/08/2015.
 */
public class CasaList extends ArrayAdapter<Casa> {

    private final Activity context;
    private final List<Casa> casas;


    public CasaList(Activity context, int resource, List<Casa> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.casas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_casas, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);

        Casa casa = casas.get(position);

        textViewNome.setText(casa.getNome());

        return rowView;
    }

}
