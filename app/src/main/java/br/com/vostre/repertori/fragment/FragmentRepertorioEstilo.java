package br.com.vostre.repertori.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaEstiloList;
import br.com.vostre.repertori.adapter.MusicaExecucaoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalCadastroMusicaProjeto;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaExecucao;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentRepertorioEstilo extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener {

    //private ListView lista;
    private ListView listaExecucoes;

    MusicaEstiloList adapterMusicasEstilos;
    MusicaExecucaoList adapterMusicasExecucoes;

    int situacao = -1;
    String idProjeto;

    Map<String, Integer> musicas;
    List<MusicaExecucao> execucoes;

    Projeto projeto;

    RadarChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_listview_musicas_estilos, container, false);

        //lista = (ListView) rootView.findViewById(R.id.listViewMusicasEstilos);
        listaExecucoes = (ListView) rootView.findViewById(R.id.listViewMusicasExecucoes);
        chart = (RadarChart) rootView.findViewById(R.id.chart);

        idProjeto = getArguments().getString("projeto");
        musicas = carregaMusicas();

        final List<String> labels = new ArrayList<>();
        final List<RadarEntry> entries = new ArrayList<>();
        Set keys = musicas.keySet();

        for(Iterator i = keys.iterator(); i.hasNext();){
            String estilo = (String) i.next();
            RadarEntry entry = new RadarEntry(musicas.get(estilo), estilo);
            entries.add(entry);
            labels.add(estilo);
        }

        XAxis xAxis = chart.getXAxis();
        YAxis yAxis = chart.getYAxis();

        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        xAxis.setTextSize(8f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        });

        yAxis.setSpaceMin(20f);
        yAxis.setXOffset(10f);
        yAxis.setYOffset(10f);
        yAxis.setTextSize(10f);
        yAxis.setAxisMinimum(0f);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        RadarDataSet dataSet = new RadarDataSet(entries, projeto.getNome());
        dataSet.setDrawFilled(true);

        RadarData data = new RadarData(dataSet);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.parseColor("#FFDD0000"));

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);
        chart.setPadding(0,0,0,0);
        chart.setRotationEnabled(false);

        chart.setData(data);

//        adapterMusicasEstilos =
//                new MusicaEstiloList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);

//        adapterMusicasEstilos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

//        lista.setAdapter(adapterMusicasEstilos);
//        lista.setOnItemClickListener(this);
//        lista.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        // EXECUCOES
        execucoes = carregaUltimaExecucao();
        Collections.sort(execucoes);

        adapterMusicasExecucoes =
                new MusicaExecucaoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, execucoes);

        adapterMusicasExecucoes.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listaExecucoes.setAdapter(adapterMusicasExecucoes);
        listaExecucoes.setOnItemClickListener(this);
        listaExecucoes.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        listaExecucoes.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Musica musica = null;

        switch(parent.getId()){
//            case R.id.listViewMusicasEstilos:
//                musica = adapterMusicasEstilos.getItem(position);
//                break;
            case R.id.listViewMusicasExecucoes:
                musica = adapterMusicasExecucoes.getItem(position).getMusica();
                break;
        }

        Intent intent = new Intent(getContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);
    }

    private Map<String, Integer> carregaMusicas(){

        if(idProjeto != null){
            MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
            projeto = new Projeto();
            projeto.setId(idProjeto);
            projeto = projetoDBHelper.carregar(getContext(), projeto);

             musicas = musicaProjetoDBHelper.contarTodosPorProjetoEEstilo(getContext(), projeto, 0);

            return musicas;
        } else{
            return null;
        }

    }

    public void atualizaLista(){
        musicas = carregaMusicas();
//        adapterMusicasEstilos =
//                new MusicaEstiloList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);

//        if(lista != null){
//            lista.setAdapter(adapterMusicasEstilos);
//            adapterMusicasEstilos.notifyDataSetChanged();
//        }

    }

    public void atualizaExecucoes(){
        musicas = carregaMusicas();
//        adapterMusicasExecucoes =
//                new MusicaExecucaoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, execucoes);
//
//        if(listaExecucoes != null){
//            listaExecucoes.setAdapter(adapterMusicasExecucoes);
//            adapterMusicasExecucoes.notifyDataSetChanged();
//        }

    }

    private List<MusicaExecucao> carregaUltimaExecucao(){

        List<MusicaExecucao> musicas = null;

        if(idProjeto != null){
            MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
            projeto = new Projeto();
            projeto.setId(idProjeto);
            projeto = projetoDBHelper.carregar(getContext(), projeto);

            musicas = musicaProjetoDBHelper.ultimaExecucaoPorProjeto(getContext(), projeto);

            return musicas;
        } else{
            return null;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

}
