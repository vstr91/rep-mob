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
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 27/08/2015.
 */
public class MusicaList extends ArrayAdapter<Musica> {

    private final Activity context;
    private final List<Musica> musicas;

    Musica musica = null;


    public MusicaList(Activity context, int resource, List<Musica> objects) {
        super(context, R.layout.listview_musicas, objects);
        this.context = context;
        this.musicas = objects;
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

//    @Override
//    public Filter getFilter() {
//        return new ListviewFilter();
//    }
//
//    @Override
//    public int getCount() {
//        return musicas.size();
//    }
//
//    private class ListviewFilter extends Filter{
//
//        @Override
//        @SuppressWarnings("unchecked")
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults filterResults = new FilterResults();
//
//            switch (getTipoObjeto()){
//                case "local":
//                    listaFiltradaLocal = new ArrayList<Local>();
//                    break;
//                case "partida":
//                    listaFiltradaBairro = new ArrayList<Bairro>();
//                    break;
//                case "destino":
//                    listaFiltradaBairro = new ArrayList<Bairro>();
//                    break;
//            }
//
//            if (constraint != null && dados != null && constraint.length() > 0) {
//
//                constraint = constraint.toString().toLowerCase();
//                int length = dados.size();
//                int i = 0;
//
//                switch (tipoObjeto){
//                    case "local":
//                        while (i < length) {
//
//                            Local obj = (Local) dados.get(i);
//                            String data = obj.getNome()+" "+obj.getEstado().getNome();
//                            if (data.toLowerCase().contains(constraint.toString())) {
//                                listaFiltradaLocal.add(obj);
//                            }
//
//                            i++;
//                        }
//
//                        filterResults.values = listaFiltradaLocal;
//                        filterResults.count = listaFiltradaLocal.size();
//
//                        break;
//                    case "partida":
//                        while (i < length) {
//
//                            Bairro obj = (Bairro) dados.get(i);
//                            String data = obj.getNome()+" "+obj.getLocal().getNome();
//                            if (data.toLowerCase().contains(constraint.toString())) {
//                                listaFiltradaBairro.add(obj);
//                            }
//
//                            i++;
//                        }
//
//                        filterResults.values = listaFiltradaBairro;
//                        filterResults.count = listaFiltradaBairro.size();
//
//                        break;
//                    case "destino":
//                        while (i < length) {
//
//                            Bairro obj = (Bairro) dados.get(i);
//                            String data = obj.getNome()+" "+obj.getLocal().getNome();
//                            if (data.toLowerCase().contains(constraint.toString())) {
//                                listaFiltradaBairro.add(obj);
//                            }
//
//                            i++;
//                        }
//
//                        filterResults.values = listaFiltradaBairro;
//                        filterResults.count = listaFiltradaBairro.size();
//                        break;
//                }
//
//            } else{
//                filterResults.values = dadosOriginais;
//                filterResults.count = dadosOriginais.size();
//            }
//            return filterResults;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            //listaFiltrada = (ArrayList<Local>) results.values;
//            if (results.count > 0) {
//
//                switch (getTipoObjeto()){
//                    case "local":
//                        dados = (List<Local>) results.values;
//                        break;
//                    case "partida":
//                        dados = (List<Bairro>) results.values;
//                        break;
//                    case "destino":
//                        dados = (List<Bairro>) results.values;
//                        break;
//                }
//
//
//                notifyDataSetChanged();
//            } else {
//                notifyDataSetInvalidated();
//            }
//        }
//    }

}
