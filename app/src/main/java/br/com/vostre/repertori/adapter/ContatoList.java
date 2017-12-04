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

/**
 * Created by Almir on 27/08/2015.
 */
public class ContatoList extends ArrayAdapter<Contato> {

    private final Activity context;
    private final List<Contato> contatos;


    public ContatoList(Activity context, int resource, List<Contato> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.contatos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_contatos, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);

        Contato contato = contatos.get(position);

        textViewNome.setText(contato.getNome());

        return rowView;
    }

}
