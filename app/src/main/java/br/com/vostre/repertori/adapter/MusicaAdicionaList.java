package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaAdicionaList extends ArrayAdapter<Musica> implements CompoundButton.OnCheckedChangeListener {

    private final Activity context;
    private final List<Musica> musicas;

    Musica musica = null;


    public MusicaAdicionaList(Activity context, int resource, List<Musica> objects) {
        super(context, R.layout.listview_adiciona_musicas, objects);
        this.context = context;
        this.musicas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(convertView == null){
            musica = musicas.get(position);

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_adiciona_musicas, null, true);

            viewHolder = new ViewHolder();

            viewHolder.text = (TextView) convertView.findViewById(R.id.textViewNome);
            viewHolder.artista = (TextView) convertView.findViewById(R.id.textViewArtista);
            viewHolder.tom = (TextView) convertView.findViewById(R.id.textViewTom);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBoxAdicionar);
            viewHolder.checkbox.setOnCheckedChangeListener(this);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.textViewNome, viewHolder.text);
            convertView.setTag(R.id.textViewArtista, viewHolder.artista);
            convertView.setTag(R.id.textViewTom, viewHolder.tom);
            convertView.setTag(R.id.checkBoxAdicionar, viewHolder.checkbox);

            TextView textViewNome = (TextView) convertView.findViewById(R.id.textViewNome);
            TextView textViewArtista = (TextView) convertView.findViewById(R.id.textViewArtista);
            TextView textViewTom = (TextView) convertView.findViewById(R.id.textViewTom);
            CheckBox checkBoxAdicionar = (CheckBox) convertView.findViewById(R.id.checkBoxAdicionar);

            textViewNome.setText(musica.getNome());
            textViewArtista.setText(musica.getArtista().getNome());
            textViewTom.setText(musica.getTom().equals("null") || musica.getTom().isEmpty() ? "-" : musica.getTom());
            checkBoxAdicionar.setOnCheckedChangeListener(this);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkbox.setTag(position); // This line is important.

        viewHolder.text.setText(musicas.get(position).getNome());
        viewHolder.artista.setText(musicas.get(position).getArtista().getNome());
        viewHolder.tom.setText(musicas.get(position).getTom().equals("null") || musicas.get(position).getTom().isEmpty() ? "-" : musicas.get(position).getTom());
        viewHolder.checkbox.setChecked(musicas.get(position).isChecked());

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int getPosition = (Integer) compoundButton.getTag();
        musicas.get(getPosition).setChecked(compoundButton.isChecked());
    }

    static class ViewHolder {
        protected TextView text;
        protected TextView artista;
        protected TextView tom;
        protected CheckBox checkbox;
    }

}
