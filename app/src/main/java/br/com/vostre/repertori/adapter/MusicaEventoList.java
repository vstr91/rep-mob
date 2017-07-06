package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaEventoList extends ArrayAdapter<Musica> implements CompoundButton.OnCheckedChangeListener {

    private final Activity context;
    private final List<Musica> musicas;

    Musica musica = null;

    ImageButton btnExcluir;
    ButtonClickListener listener;


    public MusicaEventoList(Activity context, int resource, List<Musica> objects) {
        super(context, R.layout.listview_musicas_evento, objects);
        this.context = context;
        this.musicas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MusicaAdicionaList.ViewHolder viewHolder = null;

        if(convertView == null){
            musica = musicas.get(position);

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.listview_musicas_evento, null, true);

            TextView textViewNome = (TextView) convertView.findViewById(R.id.textViewNome);
            TextView textViewArtista = (TextView) convertView.findViewById(R.id.textViewArtista);
            TextView textViewTom = (TextView) convertView.findViewById(R.id.textViewTom);
//        btnExcluir = (ImageButton) rowView.findViewById(R.id.btnExcluir);

            String tom = musica.getTom().equals("null") || musica.getTom().isEmpty() ? "-" : musica.getTom();

            textViewNome.setText(musica.getNome());
            textViewArtista.setText(musica.getArtista().getNome());
            textViewTom.setText(tom);
        } else{
            viewHolder = (MusicaAdicionaList.ViewHolder) convertView.getTag();
        }

        viewHolder.checkbox.setTag(position);

        viewHolder.text.setText(musicas.get(position).getNome());
        viewHolder.artista.setText(musicas.get(position).getArtista().getNome());
        viewHolder.checkbox.setChecked(musicas.get(position).isChecked());

        return convertView;

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int getPosition = (Integer) compoundButton.getTag();
        musicas.get(getPosition).setChecked(compoundButton.isChecked());
    }

}
