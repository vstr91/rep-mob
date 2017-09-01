package br.com.vostre.repertori;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.zxing.qrcode.encoder.QRCode;

import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.vostre.repertori.adapter.ComentarioList;
import br.com.vostre.repertori.adapter.StableArrayAdapter;
import br.com.vostre.repertori.form.ModalAdicionaMusica;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalCronometro;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.DynamicListView;

public class EventoDetalhePdfActivity extends BaseActivity {

    TextView textViewNome;
    TextView textViewData;
    LinearLayout listViewMusicas;
    Evento evento;

    List<Musica> musicas;
    Map<String, Integer> musicasEstilos;

    MusicaEventoDBHelper musicaEventoDBHelper;
    ComentarioEventoDBHelper comentarioEventoDBHelper;
    EventoDBHelper eventoDBHelper;

//    PieChart pieChart;
    ImageView imageViewQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalhe_pdf);

        musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());
        comentarioEventoDBHelper = new ComentarioEventoDBHelper(getApplicationContext());
        eventoDBHelper = new EventoDBHelper(getApplicationContext());

        LinearLayout root = (LinearLayout) findViewById(R.id.root);
        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewData = (TextView) findViewById(R.id.textViewData);
        listViewMusicas = (LinearLayout) findViewById(R.id.listViewMusicas);
//        pieChart = (PieChart) findViewById(R.id.chartPizza);
        imageViewQr = (ImageView) findViewById(R.id.imageViewQr);

        evento = new Evento();
        evento.setId(getIntent().getStringExtra("evento"));

        root.getLayoutParams().width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        carregaInformacaoEvento();

        carregaListaMusicas();

//        gerarGrafico();

        Bitmap bitmap = net.glxn.qrgen.android.QRCode.from("draffonso://eventos/"+evento.getSlug()).withSize(imageViewQr.getWidth(), imageViewQr.getHeight()).bitmap();
        imageViewQr.setImageBitmap(bitmap);

        gerarPdf();

        listViewMusicas.setMinimumWidth(3000);

    }

//    private void gerarGrafico() {
//        musicasEstilos = musicaEventoDBHelper.contarTodosPorEventoEEstilo(getApplicationContext(), evento, 0);
//
//        final List<String> labels = new ArrayList<>();
//        final List<PieEntry> entries = new ArrayList<>();
//        Set keys = musicasEstilos.keySet();
//
//        for(Iterator i = keys.iterator(); i.hasNext();){
//            String estilo = (String) i.next();
//            PieEntry entry = new PieEntry(musicasEstilos.get(estilo), estilo);
//            entries.add(entry);
//            labels.add(estilo);
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, evento.getNome());
//
//        PieData data = new PieData(dataSet);
////        data.setValueFormatter(new IValueFormatter() {
////            @Override
////            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
////                return String.valueOf((int) value);
////            }
////        });
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.parseColor("#FFDD0000"));
//
//        pieChart.getLegend().setEnabled(false);
//        pieChart.getDescription().setEnabled(false);
//        pieChart.animateXY(
//                1400, 1400,
//                Easing.EasingOption.EaseInOutQuad,
//                Easing.EasingOption.EaseInOutQuad);
//        pieChart.setPadding(0,0,0,0);
//        pieChart.setRotationEnabled(false);
//
//        pieChart.setData(data);
//        pieChart.invalidate();
//
//    }

    private void carregaListaMusicas(){
        musicas = musicaEventoDBHelper.listarTodosPorEvento(getApplicationContext(), evento);

        int cont = 1;

        for(Musica m : musicas){
            View vi = getLayoutInflater().inflate(R.layout.listview_musicas_evento_pdf, null);

            TextView textViewCont = (TextView) vi.findViewById(R.id.textViewCont);
            TextView textViewNome = (TextView) vi.findViewById(R.id.textViewNome);
            TextView textViewArtista = (TextView) vi.findViewById(R.id.textViewArtista);
            TextView textViewTom = (TextView) vi.findViewById(R.id.textViewTom);
            ImageView imageViewQrMusica = (ImageView) vi.findViewById(R.id.imageViewQr);

            textViewCont.setText(String.valueOf(cont));
            textViewNome.setText(m.getNome());
            textViewArtista.setText(m.getArtista().getNome());
            textViewTom.setText(m.getTom().equals("null") || m.getTom().isEmpty() ? "-" : m.getTom());

            Bitmap bitmap = net.glxn.qrgen.android.QRCode.from("draffonso://musicas/"+m.getSlug()).withSize(imageViewQrMusica.getWidth(), imageViewQrMusica.getHeight()).bitmap();
            imageViewQrMusica.setImageBitmap(bitmap);

            cont++;
            listViewMusicas.addView(vi);
        }

    }

    private void carregaInformacaoEvento() {
        evento = eventoDBHelper.carregar(getApplicationContext(), evento);

        textViewNome.setText(evento.getNome());
        textViewData.setText(DataUtils.toString(evento.getData(), true));
    }

    private void gerarPdf(){

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4.asPortrait())
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(getBaseContext(), attributes);

        int tamanhoLista = musicas.size() * 450 + 200;
        int largura = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(largura, tamanhoLista, 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        //System.out.println("DWIDTH: "+document.getPageWidth()+" | DHEIGHT: "+tamanhoLista);

        getWindow().getDecorView().measure(largura, tamanhoLista);
        getWindow().getDecorView().layout(0, 0, tamanhoLista, largura);

        getWindow().getDecorView().draw(page.getCanvas());

        document.finishPage(page);

        File pdf = new File(getExternalFilesDir(null).getAbsolutePath()+evento.getSlug()+".pdf");
        //System.out.println(pdf.getAbsolutePath());

        try {
            OutputStream outputStream = new FileOutputStream(pdf);
            document.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();

            Uri path = Uri.fromFile(pdf);
            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfOpenintent.setDataAndType(path, "application/pdf");

            startActivity(pdfOpenintent);
            //finishActivity(0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        document.close();
    }
}
