package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaEventoList extends ArrayAdapter<Musica> {

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

        musica = musicas.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.listview_musicas_evento, null, true);

        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewArtista = (TextView) rowView.findViewById(R.id.textViewArtista);
        TextView textViewTom = (TextView) rowView.findViewById(R.id.textViewTom);
//        btnExcluir = (ImageButton) rowView.findViewById(R.id.btnExcluir);

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());
        textViewTom.setText(musica.getTom());

//        btnExcluir.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                listener.onButtonClicked(musica);
//            }
//        });

        return rowView;
    }

}
