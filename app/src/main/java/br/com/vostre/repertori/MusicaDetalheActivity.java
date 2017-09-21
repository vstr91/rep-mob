package br.com.vostre.repertori;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.adapter.TempoList;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.form.ModalCifra;
import br.com.vostre.repertori.form.ModalEditaLetra;
import br.com.vostre.repertori.form.ModalLetra;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class MusicaDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener, ModalCadastroListener {

    TextView textViewNome;
    TextView textViewArtista;
    TextView textViewTom;
    ListView listViewExecucoes;
    ListView listViewMesmoTom;
    EventoList adapterEventos;
    MusicaList adapterMusicas;
    Button btnBuscaVideo;
    TextView textViewMedia;
    Button btnLetra;
    Button btnCifra;

    LineChart chart;
    TextView textViewLabelObservacoes;
    TextView textViewObservacoes;

    Musica musica;
    boolean qrCode = false;

    MusicaDBHelper musicaDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_detalhe);

        List<Musica> musicasMesmoTom;
        List<Evento> eventos;
        MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());

        final List<TempoMusicaEvento> tmes;
        TempoMusicaEventoDBHelper tempoMusicaEventoDBHelper = new TempoMusicaEventoDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        musicaDBHelper = new MusicaDBHelper(getApplicationContext());

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewArtista = (TextView) findViewById(R.id.textViewArtista);
        textViewTom = (TextView) findViewById(R.id.textViewTom);
        listViewExecucoes = (ListView) findViewById(R.id.listViewExecucoes);
        listViewMesmoTom = (ListView) findViewById(R.id.listViewMesmoTom);
        btnBuscaVideo = (Button) findViewById(R.id.btnBuscaVideo);
        textViewMedia = (TextView) findViewById(R.id.textViewMedia);
        btnLetra = (Button) findViewById(R.id.btnLetra);
        btnCifra = (Button) findViewById(R.id.btnCifra);
        chart = (LineChart) findViewById(R.id.chart);
        textViewObservacoes = (TextView) findViewById(R.id.textViewObservacoes);
        textViewLabelObservacoes = (TextView) findViewById(R.id.textViewLabelObservacoes);

        btnBuscaVideo.setOnClickListener(this);
        btnLetra.setOnClickListener(this);
        btnCifra.setOnClickListener(this);

        musica = new Musica();

        Uri data = getIntent().getData();

        if(data != null){
            List<String> params = data.getPathSegments();
            String slug = params.get(0);
            musica.setSlug(slug);
            musica = musicaDBHelper.carregarPorSlug(getApplicationContext(), musica);
            qrCode = true;
        } else{
            musica.setId(getIntent().getStringExtra("musica"));
            musica = musicaDBHelper.carregar(getApplicationContext(), musica);
        }

        if(musica != null){
            String qr = getIntent().getStringExtra("qr");

            if(qr != null){
                qrCode = true;
            }

            carregarDadosMusica();

            eventos = musicaEventoDBHelper.listarTodosPorMusica(getApplicationContext(), musica);

            adapterEventos =
                    new EventoList(this, android.R.layout.simple_spinner_dropdown_item, eventos);

            adapterEventos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

            listViewExecucoes.setAdapter(adapterEventos);
            listViewExecucoes.setOnItemClickListener(this);
            listViewExecucoes.setEmptyView(findViewById(R.id.textViewListaVazia));

            listViewExecucoes.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            musicasMesmoTom = musicaDBHelper.listarRelacionadasPorTom(getApplicationContext(), musica);

            adapterMusicas =
                    new MusicaList(this, android.R.layout.simple_spinner_dropdown_item, musicasMesmoTom);

            adapterMusicas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

            listViewMesmoTom.setAdapter(adapterMusicas);
            listViewMesmoTom.setOnItemClickListener(this);
            listViewMesmoTom.setEmptyView(findViewById(R.id.textViewListaTomVazia));

            listViewMesmoTom.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            tmes = tempoMusicaEventoDBHelper.listarTodosPorMusica(getApplicationContext(), musica, 10);

            chart.setExtraOffsets(10f, 10f, 10f, 10f);
            chart.getDescription().setEnabled(false);
            chart.setNoDataText("Nenhum registro encontrado.");
            chart.setNoDataTextColor(R.color.colorAccent);

            if(tmes.size() > 0){
                int cont = 0;
                List<Entry> dados = new ArrayList<>();

                //long ultimoStamp = tmes.get(0).getMusicaEvento().getEvento().getData().getTimeInMillis();

                for(TempoMusicaEvento tme : tmes){

                    String tempo = DataUtils.toStringSomenteHoras(tme.getTempo(), 1);
                    //int dateInt =  (int) ultimoStamp - date;



                    dados.add(new Entry(cont, DataUtils.tempoParaSegundos(tempo)));
                    cont++;
                }

                LineDataSet dataSet = new LineDataSet(dados, "Tempos");
                dataSet.setDrawFilled(true);
                dataSet.setFillColor(R.color.colorAccent);

                dataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return DataUtils.segundosParaTempo((long) value);
                    }
                });

                dataSet.setColor(Color.RED);
                dataSet.setValueTextSize(12f);
                dataSet.setValueTextColor(Color.RED);

                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);

                XAxis xAxis = chart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        TempoMusicaEvento tme = null;

                        try{
                            tme = tmes.get((int) value);
                        }catch(IndexOutOfBoundsException e){
                            return "";
                        }


                        if(tme != null){
                            return DataUtils.toString(tme.getUltimaAlteracao(), false);
                        } else{
                            return String.valueOf(value);
                        }


                    }
                });

                IAxisValueFormatter formatterY  = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return DataUtils.segundosParaTempo((long) value);
                    }
                };

                YAxis yAxis = chart.getAxisLeft();
                yAxis.setValueFormatter(formatterY);
                yAxis.setGranularity(10f);

                YAxis yAxisRight = chart.getAxisRight();
                yAxisRight.setValueFormatter(formatterY);
                yAxisRight.setGranularity(10f);

                chart.animateXY(
                        400, 1000,
                        Easing.EasingOption.EaseInSine,
                        Easing.EasingOption.EaseInSine);

                chart.invalidate();

            }

//        adapterExecucoes =
//                new TempoList(this, android.R.layout.simple_spinner_dropdown_item, tmes);
//
//        adapterEventos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
//
//        listViewExecucoesCronometradas.setAdapter(adapterExecucoes);
//        listViewExecucoesCronometradas.setEmptyView(findViewById(R.id.textViewListaVaziaCronometro));

            Calendar c = musica.calcularMedia(getBaseContext());

            if(c != null){
                textViewMedia.setText(DataUtils.toStringSomenteHoras(c, 1));
            } else{
                textViewMedia.setText("N/D");
            }

            //btnLetra.setEnabled(!musica.getLetra().isEmpty());
        } else{
            Toast.makeText(getBaseContext(), "Música não encontrada. Registro pode ter sido removido ou alterado.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.musica_detalhe, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.icon_edit:

                ModalCadastroMusica modalCadastroMusica = new ModalCadastroMusica();
                modalCadastroMusica.setListener(this);
                modalCadastroMusica.setMusica(musica);

                modalCadastroMusica.show(getSupportFragmentManager(), "modalMusica");

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent;

        switch(parent.getId()){
            case R.id.listViewMesmoTom:
                Musica musica = adapterMusicas.getItem(position);

                intent = new Intent(getBaseContext(), MusicaDetalheActivity.class);
                intent.putExtra("musica", musica.getId());
                startActivity(intent);
                break;
            case R.id.listViewExecucoes:
                Evento evento = adapterEventos.getItem(position);

                intent = new Intent(getBaseContext(), EventoDetalheActivity.class);
                intent.putExtra("evento", evento.getId());
                startActivity(intent);
                break;
        }



    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch(v.getId()){
            case R.id.btnBuscaVideo:

                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", musica.getNome()+" "+musica.getArtista().getNome());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.btnLetra:
//                ModalLetra modalLetra = new ModalLetra();
//                modalLetra.setMusica(musica);
//
//                modalLetra.show(getSupportFragmentManager(), "modalLetra");
                Intent i = new Intent(getBaseContext(), LetraActivity.class);
                i.putExtra("musica", musica.getId());
                startActivity(i);
                break;
            case R.id.btnCifra:
//                ModalCifra modalCifra = new ModalCifra();
//                modalCifra.setMusica(musica);
//
//                modalCifra.show(getSupportFragmentManager(), "modalCifra");
                Intent cifra = new Intent(getBaseContext(), CifraActivity.class);
                cifra.putExtra("musica", musica.getId());
                startActivity(cifra);
                break;
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if(qrCode){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        musica = musicaDBHelper.carregar(getBaseContext(), musica);
        carregarDadosMusica();
    }

    private void carregarDadosMusica(){
        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());

        String tom = musica.getTom().equals("null") ? "-" : musica.getTom();

        textViewTom.setText(tom);

        if(!musica.getObservacoes().equals("null") && !musica.getObservacoes().isEmpty()){
            textViewObservacoes.setText(musica.getObservacoes());
        } else{
            textViewLabelObservacoes.setVisibility(View.GONE);
            textViewObservacoes.setVisibility(View.GONE);
        }

    }
}
