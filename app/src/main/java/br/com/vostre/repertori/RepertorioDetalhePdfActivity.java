package br.com.vostre.repertori;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class RepertorioDetalhePdfActivity extends BaseActivity {

    TextView textViewNome;
    TextView textViewObs;
    LinearLayout listViewMusicasEstilos;
    LinearLayout listViewMusicasTom;

    List<Musica> musicasTom;
    List<Musica> musicasEstilos;
    List<Musica> musicas;

    MusicaRepertorioDBHelper musicaRepertorioDBHelper;
    RepertorioDBHelper repertorioDBHelper;

//    PieChart pieChart;
    ImageView imageViewQr;
    Repertorio repertorio;

    List<View> dadosEstilos;
    List<View> dadosTom;
    List<View> dadosMusicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto_detalhe_pdf);

        musicaRepertorioDBHelper = new MusicaRepertorioDBHelper(getApplicationContext());
        repertorioDBHelper = new RepertorioDBHelper(getApplicationContext());

        LinearLayout root = (LinearLayout) findViewById(R.id.root);
        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewObs = (TextView) findViewById(R.id.textViewObs);
        listViewMusicasEstilos = (LinearLayout) findViewById(R.id.listViewMusicasEstilos);
        listViewMusicasTom = (LinearLayout) findViewById(R.id.listViewMusicasTom);
//        pieChart = (PieChart) findViewById(R.id.chartPizza);
        imageViewQr = (ImageView) findViewById(R.id.imageViewQr);

        repertorio = new Repertorio();
        repertorio.setId(getIntent().getStringExtra("repertorio"));

        root.getLayoutParams().width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        carregaInformacaoProjeto();

        carregaListasMusicas();

//        gerarGrafico();

        Bitmap bitmap = net.glxn.qrgen.android.QRCode.from("draffonso://repertorios/"+repertorio.getSlug()).withSize(imageViewQr.getWidth(), imageViewQr.getHeight()).bitmap();
        imageViewQr.setImageBitmap(bitmap);

        gerarPdf();

        listViewMusicasEstilos.setMinimumWidth(3000);
        listViewMusicasTom.setMinimumWidth(3000);

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

    private void carregaListasMusicas(){
        musicasTom = musicaRepertorioDBHelper.listarTodosPorRepertorioETom(getApplicationContext(), repertorio, 0);
        musicasEstilos = musicaRepertorioDBHelper.listarTodosPorRepertorioEEstilo(getApplicationContext(), repertorio, 0);
        musicas = musicaRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);

        textViewObs.setText(musicasEstilos.size()+" música(s)");

        int cont = 1;
        String tom = "";
        dadosTom = new ArrayList<>();

        for(Musica m : musicasTom){
            View vi = getLayoutInflater().inflate(R.layout.listview_musicas_tom_pdf, null);

            TextView textViewCont = (TextView) vi.findViewById(R.id.textViewCont);
            TextView textViewNome = (TextView) vi.findViewById(R.id.textViewNome);
            TextView textViewArtista = (TextView) vi.findViewById(R.id.textViewArtista);
            TextView textViewTom = (TextView) vi.findViewById(R.id.textViewTom);
            TextView textViewTempoMedio = (TextView) vi.findViewById(R.id.textViewTempoMedio);
            TextView textViewEstilo = (TextView) vi.findViewById(R.id.textViewEstilo);

            if(!tom.equals(m.getTom().equals("null") || m.getTom().isEmpty() ? "-" : m.getTom())){
                tom = m.getTom().equals("null") || m.getTom().isEmpty() ? "-" : m.getTom();
                textViewTom.setText(tom);
                cont = 1;
            } else{
                textViewTom.setVisibility(View.GONE);
            }

            textViewCont.setText(String.valueOf(cont));
            textViewNome.setText(m.getNome());
            textViewArtista.setText(m.getArtista().getNome());

            Calendar calendar = m.calcularMedia(getBaseContext());

            if(calendar != null){
                textViewTempoMedio.setText(DataUtils.toStringSomenteHoras(calendar, 1));
            } else{
                textViewTempoMedio.setText("-");
            }

            textViewEstilo.setText(m.getEstilo().getNome());

            cont++;
            dadosTom.add(vi);
//            listViewMusicasTom.addView(vi);
        }

        // ESTILOS

        String estilo = "";
        cont = 1;
        dadosEstilos = new ArrayList<>();

        for(Musica m : musicasEstilos){
            View vi = getLayoutInflater().inflate(R.layout.listview_musicas_estilo_pdf, null);

            TextView textViewCont = (TextView) vi.findViewById(R.id.textViewCont);
            TextView textViewNome = (TextView) vi.findViewById(R.id.textViewNome);
            TextView textViewArtista = (TextView) vi.findViewById(R.id.textViewArtista);
            TextView textViewTom = (TextView) vi.findViewById(R.id.textViewTom);
            TextView textViewTempoMedio = (TextView) vi.findViewById(R.id.textViewTempoMedio);
            TextView textViewEstilo = (TextView) vi.findViewById(R.id.textViewEstilo);

            if(!estilo.equals(m.getEstilo().getNome())){
                estilo = m.getEstilo().getNome();
                textViewEstilo.setText(estilo);
                cont = 1;
            } else{
                textViewEstilo.setVisibility(View.GONE);
            }

            textViewCont.setText(String.valueOf(cont));
            textViewNome.setText(m.getNome());
            textViewArtista.setText(m.getArtista().getNome());
            textViewTom.setText(m.getTom().equals("null") || m.getTom().isEmpty() ? "-" : m.getTom());

            Calendar calendar = m.calcularMedia(getBaseContext());

            if(calendar != null){
                textViewTempoMedio.setText(DataUtils.toStringSomenteHoras(calendar, 1));
            } else{
                textViewTempoMedio.setText("-");
            }

            cont++;
            dadosEstilos.add(vi);
            //listViewMusicasEstilos.addView(vi);
        }

        // LISTA ALFABETICA

        cont = 1;
        dadosMusicas = new ArrayList<>();

        for(Musica m : musicas){
            View vi = getLayoutInflater().inflate(R.layout.listview_musicas_pdf, null);

            TextView textViewCont = (TextView) vi.findViewById(R.id.textViewCont);
            TextView textViewNome = (TextView) vi.findViewById(R.id.textViewNome);
            TextView textViewArtista = (TextView) vi.findViewById(R.id.textViewArtista);
            TextView textViewTempoMedio = (TextView) vi.findViewById(R.id.textViewTempoMedio);
            TextView textViewEstilo = (TextView) vi.findViewById(R.id.textViewEstilo);
            Space space = (Space) vi.findViewById(R.id.space);

            if(cont > 1){
                space.setVisibility(View.GONE);
            }

            estilo = m.getEstilo().getNome();
            textViewEstilo.setText(estilo);

            textViewCont.setText(String.valueOf(cont));
            textViewNome.setText(m.getNome());
            textViewArtista.setText(m.getArtista().getNome());

            Calendar calendar = m.calcularMedia(getBaseContext());

            if(calendar != null){
                textViewTempoMedio.setText(DataUtils.toStringSomenteHoras(calendar, 1));
            } else{
                textViewTempoMedio.setText("-");
            }

            cont++;
            dadosMusicas.add(vi);
            //listViewMusicasEstilos.addView(vi);
        }

    }

    private void carregaInformacaoProjeto() {
        repertorio = repertorioDBHelper.carregar(getApplicationContext(), repertorio);

        textViewNome.setText(repertorio.getNome());
    }

    private void gerarPdf(){

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4.asPortrait())
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(getBaseContext(), attributes);

        int cont = 1;
        int contOld = 1;
        int tamanhoAcumuladoPagina = 0;
        int contTotal = 1;
        int tamanhoDadosEstilos = dadosEstilos.size();

        LinearLayout linearLayout = new LinearLayout(getBaseContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(document.getPageWidth(), PrintAttributes.MediaSize.ISO_A4.getHeightMils()));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        PdfDocument.Page page = null;

        for(View view : dadosEstilos){

            view.measure(document.getPageWidth(), 200);
            view.layout(0, 0, document.getPageWidth(), 200);

            TextView t1 = (TextView) view.findViewById(R.id.textViewNome);

            System.out.println("OFFSET >>>>>>>>> "+tamanhoAcumuladoPagina+" | "+t1.getText());

            if(tamanhoAcumuladoPagina == 0 || cont != contOld){
                contOld = cont;
                System.out.println("NOVA PAGINA: "+cont);

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                        .Builder(document.getPageWidth(), document.getPageHeight(), cont).create();
                page = document.startPage(pageInfo);
            }

            if(tamanhoAcumuladoPagina + view.getMeasuredHeight() <= document.getPageHeight()){
                view.offsetTopAndBottom(tamanhoAcumuladoPagina);
            } else{
                // PAGINA
                geraPaginacao(document, linearLayout, page);
                // FIM PAGINA

                if(page.getInfo().getPageNumber() == 1){
                    geraTitulo(document, linearLayout, "Músicas por Estilo");
                }

                linearLayout.draw(page.getCanvas());
                document.finishPage(page);
                page = null;
                linearLayout.removeAllViews();
                tamanhoAcumuladoPagina = 0;
                cont++;
            }

            linearLayout.addView(view);

            if(contTotal >= tamanhoDadosEstilos){

                // PAGINA
                geraPaginacao(document, linearLayout, page);
                // FIM PAGINA

                linearLayout.draw(page.getCanvas());
                document.finishPage(page);
            }

            tamanhoAcumuladoPagina = tamanhoAcumuladoPagina + view.getMeasuredHeight();

            contTotal++;

        }

        // TOM
        cont = 1;
        contOld = 1;
        tamanhoAcumuladoPagina = 0;
        contTotal = 1;
        int tamanhoDadosTom = dadosTom.size();

        for(View view : dadosTom){

            view.measure(document.getPageWidth(), 200);
            view.layout(0, 0, document.getPageWidth(), 200);

            TextView t1 = (TextView) view.findViewById(R.id.textViewNome);

            System.out.println("OFFSET >>>>>>>>> "+tamanhoAcumuladoPagina+" | "+t1.getText());

            if(tamanhoAcumuladoPagina == 0 || cont != contOld){
                contOld = cont;
                System.out.println("NOVA PAGINA: "+cont);

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                        .Builder(document.getPageWidth(), document.getPageHeight(), cont).create();
                page = document.startPage(pageInfo);
            }

            if(tamanhoAcumuladoPagina + view.getMeasuredHeight() <= document.getPageHeight()){
                view.offsetTopAndBottom(tamanhoAcumuladoPagina);
            } else{

                // PAGINA
                geraPaginacao(document, linearLayout, page);
                // FIM PAGINA

                if(page.getInfo().getPageNumber() == 1){
                    geraTitulo(document, linearLayout, "Músicas por Tom");
                }

                linearLayout.draw(page.getCanvas());
                document.finishPage(page);
                page = null;
                linearLayout.removeAllViews();
                tamanhoAcumuladoPagina = 0;
                cont++;
            }

            linearLayout.addView(view);

            if(contTotal >= tamanhoDadosTom){

                if(page == null){
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                            .Builder(document.getPageWidth(), document.getPageHeight(), cont).create();
                    page = document.startPage(pageInfo);
                }

                // PAGINA
                geraPaginacao(document, linearLayout, page);
                // FIM PAGINA

                //if(page != null){
                    linearLayout.draw(page.getCanvas());
                    document.finishPage(page);
                //}


            }

            tamanhoAcumuladoPagina = tamanhoAcumuladoPagina + view.getMeasuredHeight();

            contTotal++;

        }

        // MUSICAS
        cont = 1;
        contOld = 1;
        tamanhoAcumuladoPagina = 0;
        contTotal = 1;
        int tamanhoDadosMusicas = dadosMusicas.size();

        for(View view : dadosMusicas){

            view.measure(document.getPageWidth(), 200);
            view.layout(0, 0, document.getPageWidth(), 200);

            TextView t1 = (TextView) view.findViewById(R.id.textViewNome);

            System.out.println("OFFSET >>>>>>>>> "+tamanhoAcumuladoPagina+" | "+t1.getText());

            if(tamanhoAcumuladoPagina == 0 || cont != contOld){
                contOld = cont;
                System.out.println("NOVA PAGINA: "+cont);

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                        .Builder(document.getPageWidth(), document.getPageHeight(), cont).create();
                page = document.startPage(pageInfo);
            }

            if(tamanhoAcumuladoPagina + view.getMeasuredHeight() <= document.getPageHeight()){
                view.offsetTopAndBottom(tamanhoAcumuladoPagina);
            } else{

                // PAGINA
                geraPaginacao(document, linearLayout, page);
                // FIM PAGINA

                if(page.getInfo().getPageNumber() == 1){
                    geraTitulo(document, linearLayout, "Todas as Músicas");
                }

                linearLayout.draw(page.getCanvas());
                document.finishPage(page);
                page = null;
                linearLayout.removeAllViews();
                tamanhoAcumuladoPagina = 0;
                cont++;
            }

            linearLayout.addView(view);

            if(contTotal >= tamanhoDadosMusicas){

                // PAGINA
                geraPaginacao(document, linearLayout, page);
                // FIM PAGINA

                linearLayout.draw(page.getCanvas());
                document.finishPage(page);
            }

            tamanhoAcumuladoPagina = tamanhoAcumuladoPagina + view.getMeasuredHeight();

            contTotal++;

        }



//        getWindow().getDecorView().measure(largura, tamanhoLista);
//        getWindow().getDecorView().layout(0, 0, tamanhoLista, largura);

//        getWindow().getDecorView().measure(pageInfo.getPageWidth(), pageInfo.getPageHeight());
//        getWindow().getDecorView().layout(0, 0, pageInfo.getPageHeight(), pageInfo.getPageWidth());
//
//        getWindow().getDecorView().draw(page.getCanvas());

        File pdf = new File(getExternalFilesDir(null).getAbsolutePath()+File.separator+"resumo-"+repertorio.getSlug()+".pdf");
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

    private void geraPaginacao(PrintedPdfDocument document, LinearLayout linearLayout, PdfDocument.Page page) {
        TextView tv = new TextView(getBaseContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
        tv.setLayoutParams(layoutParams);
        tv.setVisibility(View.VISIBLE);

        tv.measure(document.getPageWidth(), document.getPageHeight());
        tv.layout(20, document.getPageHeight() - 40, document.getPageWidth(), document.getPageHeight());
        tv.setTextColor(Color.GRAY);
        tv.setTextSize(4);

        if(page != null){
            tv.setText(String.valueOf(page.getInfo().getPageNumber()));
        } else{
            tv.setText("-");
        }


        linearLayout.addView(tv);
    }

    private void geraTitulo(PrintedPdfDocument document, LinearLayout linearLayout, String titulo) {
        TextView tv = new TextView(getBaseContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
        tv.setLayoutParams(layoutParams);
        tv.setVisibility(View.VISIBLE);
        tv.setGravity(Gravity.CENTER);

        tv.measure(document.getPageWidth(), document.getPageHeight());
        tv.layout(0, 0, document.getPageWidth(), document.getPageHeight());
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(5);

        tv.setText(titulo);
        linearLayout.addView(tv);
    }

}
