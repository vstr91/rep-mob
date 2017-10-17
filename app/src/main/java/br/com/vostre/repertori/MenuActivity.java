package br.com.vostre.repertori;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.ServiceUtils;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnProjetos;
    Button btnEventos;
    Button btnMusicas;
    Button btnArtistas;
    Button btnEstilos;
    Button btnQR;
    Button btnInfos;
    Button btnAtualizar;

    Tracker mTracker;
    static int RC_BARCODE_CAPTURE = 1;
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnProjetos = (Button) findViewById(R.id.btnProjetos);
        btnEventos = (Button) findViewById(R.id.btnEventos);
        btnMusicas = (Button) findViewById(R.id.btnMusicas);
        btnArtistas = (Button) findViewById(R.id.btnArtistas);
        btnEstilos = (Button) findViewById(R.id.btnEstilos);
        btnQR = (Button) findViewById(R.id.btnQR);
        btnInfos = (Button) findViewById(R.id.btnInfos);
        btnAtualizar = (Button) findViewById(R.id.btnAtualizar);

        btnProjetos.setOnClickListener(this);
        btnEventos.setOnClickListener(this);
        btnMusicas.setOnClickListener(this);
        btnArtistas.setOnClickListener(this);
        btnEstilos.setOnClickListener(this);
        btnQR.setOnClickListener(this);
        btnInfos.setOnClickListener(this);
        btnAtualizar.setOnClickListener(this);

        App application = (App) getApplication();
        mTracker = application.getDefaultTracker();

        mTracker.setScreenName("Menu Principal");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        ServiceUtils.iniciaServicoAtualizacao(true, getBaseContext());

    }

    @Override
    public void onClick(View view) {

        Intent i;

        switch(view.getId()){
            case R.id.btnProjetos:
                i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("fragmento", R.id.projetos);
                startActivity(i);
                break;
            case R.id.btnEventos:
                i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("fragmento", R.id.eventos);
                startActivity(i);
                break;
            case R.id.btnMusicas:
                i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("fragmento", R.id.musicas);
                startActivity(i);
                break;
            case R.id.btnArtistas:
                i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("fragmento", R.id.artistas);
                startActivity(i);
                break;
            case R.id.btnEstilos:
                i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("fragmento", R.id.estilos);
                startActivity(i);
                break;
            case R.id.btnQR:
                i = new Intent(this, BarcodeCaptureActivity.class);
                i.putExtra(BarcodeCaptureActivity.AutoFocus,true);
                i.putExtra(BarcodeCaptureActivity.UseFlash, false);

                startActivityForResult(i, RC_BARCODE_CAPTURE);
                break;
            case R.id.btnInfos:
                i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("fragmento", R.id.info);
                startActivity(i);
                break;
            case R.id.btnAtualizar:
                ServiceUtils.iniciaServicoAtualizacao(false, getBaseContext());
                btnAtualizar.setEnabled(false);
                btnAtualizar.setText("Atualizando dados...");
                btnAtualizar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    String[] valor = barcode.rawValue.split("/");
                    String dominio = valor[0];

                    if(dominio.equals("draffonso:")){
                        String tipo = valor[2];

                        String slug = valor[3];

                        Intent intent;

                        switch(tipo){
                            case "musicas":
                                MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getBaseContext());
                                Musica musica = new Musica();
                                musica.setSlug(slug);
                                musica = musicaDBHelper.carregarPorSlug(getBaseContext(), musica);

                                if(musica != null){
                                    intent = new Intent(getBaseContext(), MusicaDetalheActivity.class);
                                    intent.putExtra("musica", musica.getId());
                                    intent.putExtra("qr", true);
                                    startActivity(intent);
                                } else{
                                    Toast.makeText(getBaseContext(), "Música não encontrada. Registro pode ter sido removido ou alterado.", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            case "eventos":
                                EventoDBHelper eventoDBHelper = new EventoDBHelper(getBaseContext());
                                Evento evento = new Evento();
                                evento.setSlug(slug);
                                evento = eventoDBHelper.carregarPorSlug(getBaseContext(), evento);

                                if(evento != null){
                                    intent = new Intent(getBaseContext(), EventoDetalheActivity.class);
                                    intent.putExtra("evento", evento.getId());
                                    intent.putExtra("qr", true);
                                    startActivity(intent);
                                } else{
                                    Toast.makeText(getBaseContext(), "Evento não encontrado. Registro pode ter sido removido ou alterado.", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            default:
                                Toast.makeText(getBaseContext(), "Erro ao ler QR Code. QR Code não relativo ao aplicativo.", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    } else{
                        Toast.makeText(getBaseContext(), "Erro ao ler QR Code. QR Code não relativo ao aplicativo.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getBaseContext(), "Erro ao ler QR Code. Resultado vazio.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "Erro ao ler QR Code", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                btnAtualizar.setEnabled(true);
                btnAtualizar.setBackgroundColor(getResources().getColor(R.color.white));
                btnAtualizar.setText("SINCRONIZAR DADOS");
            }
        };

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(br, new IntentFilter("br.com.vostre.repertori.AtualizaDadosService"));

        mTracker.setScreenName("Tela Eventos");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        super.onResume();

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(br);
        super.onPause();
    }

}
