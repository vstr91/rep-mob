package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.listener.ListviewComFiltroListener;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaList extends ArrayAdapter<Musica> {

    private final Activity context;
    private List<Musica> musicas;

    Musica musica = null;
    private List<Musica> dadosOriginais;
    ListviewComFiltroListener listener;

    public ListviewComFiltroListener getListener() {
        return listener;
    }

    public void setListener(ListviewComFiltroListener listener) {
        this.listener = listener;
    }

    public MusicaList(Activity context, int resource, List<Musica> objects) {
        super(context, R.layout.listview_musicas, objects);
        this.context = context;
        this.musicas = objects;
        dadosOriginais = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        musica = musicas.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_musicas, null, true);

        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        TextView textViewArtista = (TextView) rowView.findViewById(R.id.textViewArtista);
        TextView textViewTom = (TextView) rowView.findViewById(R.id.textViewTom);
        TextView textViewEstilo = (TextView) rowView.findViewById(R.id.textViewEstilo);

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());
        textViewTom.setText(musica.getTom().equals("null") || musica.getTom().isEmpty() ? "-" : musica.getTom());
        textViewEstilo.setText(musica.getEstilo().getNome());

        return rowView;
    }

    @Override
    public Filter getFilter() {
        return new ListviewFilter();
    }

    @Override
    public int getCount() {
        return musicas.size();
    }

    private class ListviewFilter extends Filter{

        @Override
        @SuppressWarnings("unchecked")
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            List<Musica> listaFiltrada = new ArrayList<>();

            if (constraint != null && musicas != null && constraint.length() > 0) {

                constraint = constraint.toString().toLowerCase();
                int length = musicas.size();
                int i = 0;

                while (i < length) {

                    Musica obj = musicas.get(i);
                    String data = obj.getNome()+" "+obj.getArtista().getNome();
                    if (data.toLowerCase().contains(constraint.toString())) {
                        listaFiltrada.add(obj);
                    }

                    i++;
                }

                filterResults.values = listaFiltrada;
                filterResults.count = listaFiltrada.size();

            } else{
                filterResults.values = dadosOriginais;
                filterResults.count = dadosOriginais.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //listaFiltrada = (ArrayList<Local>) results.values;
            if (results.count > 0) {

                musicas = (List<Musica>) results.values;

                notifyDataSetChanged();
                listener.onListviewComFiltroDismissed(musicas);
            } else {
                notifyDataSetInvalidated();
                listener.onListviewComFiltroDismissed(musicas);
            }
        }
    }

}
