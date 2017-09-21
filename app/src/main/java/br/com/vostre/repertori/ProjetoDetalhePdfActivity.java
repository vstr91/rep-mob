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
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class ProjetoDetalhePdfActivity extends BaseActivity {

    TextView textViewNome;
    TextView textViewObs;
    LinearLayout listViewMusicasEstilos;
    LinearLayout listViewMusicasTom;

    List<Musica> musicasTom;
    List<Musica> musicasEstilos;

    MusicaProjetoDBHelper musicaProjetoDBHelper;
    ProjetoDBHelper projetoDBHelper;

//    PieChart pieChart;
    ImageView imageViewQr;
    Projeto projeto;

    List<View> dadosEstilos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto_detalhe_pdf);

        musicaProjetoDBHelper = new MusicaProjetoDBHelper(getApplicationContext());
        projetoDBHelper = new ProjetoDBHelper(getApplicationContext());

        LinearLayout root = (LinearLayout) findViewById(R.id.root);
        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewObs = (TextView) findViewById(R.id.textViewObs);
        listViewMusicasEstilos = (LinearLayout) findViewById(R.id.listViewMusicasEstilos);
        listViewMusicasTom = (LinearLayout) findViewById(R.id.listViewMusicasTom);
//        pieChart = (PieChart) findViewById(R.id.chartPizza);
        imageViewQr = (ImageView) findViewById(R.id.imageViewQr);

        projeto = new Projeto();
        projeto.setId(getIntent().getStringExtra("projeto"));

        root.getLayoutParams().width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        carregaInformacaoProjeto();

        carregaListasMusicas();

//        gerarGrafico();

        Bitmap bitmap = net.glxn.qrgen.android.QRCode.from("draffonso://projetos/"+projeto.getSlug()).withSize(imageViewQr.getWidth(), imageViewQr.getHeight()).bitmap();
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
        musicasTom = musicaProjetoDBHelper.listarTodosPorProjetoETom(getApplicationContext(), projeto, 0);
        musicasEstilos = musicaProjetoDBHelper.listarTodosPorProjetoEEstilo(getApplicationContext(), projeto, 0);

        textViewObs.setText(musicasEstilos.size()+" música(s)");

        int cont = 1;
        String tom = "";

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
            listViewMusicasTom.addView(vi);
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

    }

    private void carregaInformacaoProjeto() {
        projeto = projetoDBHelper.carregar(getApplicationContext(), projeto);

        textViewNome.setText(projeto.getNome());
    }

    private void gerarPdf(){

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4.asPortrait())
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(getBaseContext(), attributes);

        int tamanhoLista = (musicasEstilos.size() * 230 + 200) + (musicasTom.size() * 230 + 200);
        int largura = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        int cont = 1;
        int contOld = 1;
        int tamanhoAcumuladoPagina = 0;
        int contTotal = 1;
        int tamanhoDados = dadosEstilos.size();

        LinearLayout linearLayout = new LinearLayout(getBaseContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(document.getPageWidth(), PrintAttributes.MediaSize.ISO_A4.getHeightMils()));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

//        linearLayout.measure(document.getPageWidth(), document.getPageHeight());
//        linearLayout.layout(0, 0, document.getPageWidth(), document.getPageHeight());

        linearLayout.setBackgroundColor(Color.BLUE);

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
                linearLayout.draw(page.getCanvas());
                document.finishPage(page);
                page = null;
                linearLayout.removeAllViews();
                tamanhoAcumuladoPagina = 0;
                cont++;
            }

            linearLayout.addView(view);

            if(contTotal >= tamanhoDados){
                linearLayout.draw(page.getCanvas());
                document.finishPage(page);
            }

            tamanhoAcumuladoPagina = tamanhoAcumuladoPagina + view.getMeasuredHeight();

            contTotal++;


            // ANTIGO ////////////////////////////////////////////////////////////
/*
            tamanhoAcumuladoPagina = tamanhoAcumuladoPagina + view.getMeasuredHeight();

            if(tamanhoAcumuladoPagina >= document.getPageHeight() || contTotal == 1 || contTotal >= tamanhoDados){
                System.out.println("CONT >>>>>>>>> "+cont);

                if(page != null){

                    linearLayout.draw(page.getCanvas());
                    document.finishPage(page);

                    int c = linearLayout.getChildCount();

                    System.out.println("PAGINA "+page.getInfo().getPageNumber());

                    for(int i = 0; i < c; i++){
                        TextView t = (TextView) linearLayout.getChildAt(i).findViewById(R.id.textViewNome);
                        System.out.println("   ===="+t.getText().toString());
                    }

                    page = null;
                    linearLayout.removeAllViews();
                }

                if(contTotal < tamanhoDados){
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                            .Builder(document.getPageWidth(), document.getPageHeight(), cont).create();


                    page = document.startPage(pageInfo);
                }

                cont++;

                if(contTotal > 1 || tamanhoAcumuladoPagina >= document.getPageHeight()){
                    tamanhoAcumuladoPagina = 0;
                    view.offsetTopAndBottom(tamanhoAcumuladoPagina);
                }

            }

            view.offsetTopAndBottom(tamanhoAcumuladoPagina);
            linearLayout.addView(view);

//            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
//                    .Builder(document.getPageWidth(), document.getPageHeight(), cont).create();
//
//
//            page = document.startPage(pageInfo);
//
//            linearLayout.draw(page.getCanvas());
//            document.finishPage(page);

            contTotal++;
*/
        }



//        getWindow().getDecorView().measure(largura, tamanhoLista);
//        getWindow().getDecorView().layout(0, 0, tamanhoLista, largura);

//        getWindow().getDecorView().measure(pageInfo.getPageWidth(), pageInfo.getPageHeight());
//        getWindow().getDecorView().layout(0, 0, pageInfo.getPageHeight(), pageInfo.getPageWidth());
//
//        getWindow().getDecorView().draw(page.getCanvas());

        File pdf = new File(getExternalFilesDir(null).getAbsolutePath()+projeto.getSlug()+".pdf");
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
