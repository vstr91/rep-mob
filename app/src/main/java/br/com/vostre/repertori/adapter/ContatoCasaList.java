package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.ContatoCasa;

/**
 * Created by Almir on 27/08/2015.
 */
public class ContatoCasaList extends ArrayAdapter<ContatoCasa> {

    private final Activity context;
    private final List<ContatoCasa> contatos;


    public ContatoCasaList(Activity context, int resource, List<ContatoCasa> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.contatos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_contatos_casas, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewCargo = (TextView) rowView.findViewById(R.id.textViewCargo);

        ContatoCasa contatoCasa = contatos.get(position);

        textViewNome.setText(contatoCasa.getContato().getNome());
        textViewCargo.setText(contatoCasa.getCargo());

        return rowView;
    }

}
