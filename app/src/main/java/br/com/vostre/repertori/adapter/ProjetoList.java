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
import br.com.vostre.repertori.model.Projeto;

/**
 * Created by Almir on 27/08/2015.
 */
public class ProjetoList extends ArrayAdapter<Projeto> {

    private final Activity context;
    private final List<Projeto> projetos;
    private final boolean completo;

    public ProjetoList(Activity context, int resource, List<Projeto> objects, boolean completo) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.projetos = objects;
        this.completo = completo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_projetos, null, true);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewMusicas = (TextView) rowView.findViewById(R.id.textViewMusicas);

        Projeto projeto = projetos.get(position);

        textViewNome.setText(projeto.getNome());

        if(completo){
            textViewMusicas.setText(projeto.contarMusicasAtivas(getContext())+" música(s)");
        } else{
            textViewMusicas.setVisibility(View.GONE);
        }

        return rowView;
    }

}
