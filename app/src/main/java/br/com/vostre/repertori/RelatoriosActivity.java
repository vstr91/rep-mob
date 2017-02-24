package br.com.vostre.repertori;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;

public class RelatoriosActivity extends AppCompatActivity {

    PieChart chartMusicas;
    MusicaDBHelper musicaDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);

        musicaDBHelper = new MusicaDBHelper(getApplicationContext());

        chartMusicas = (PieChart) findViewById(R.id.chartMusicas);

        HashMap<Integer, Artista> musicas = musicaDBHelper.contarTodosPorArtista(getApplicationContext());
        List<PieEntry> dados = new ArrayList<>();

        int total = musicas.size();

        for(Map.Entry<Integer, Artista> entry : musicas.entrySet()){
            dados.add(new PieEntry(entry.getKey(), entry.getValue().getNome()));
        }

        PieDataSet dataset = new PieDataSet(dados, "Musicas");

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: ColorTemplate.MATERIAL_COLORS) colors.add(c);

        dataset.setColors(colors);

        PieData pieData = new PieData(dataset);

        chartMusicas.setData(pieData);
        chartMusicas.invalidate();

    }
}
