package br.com.vostre.repertori;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.vostre.repertori.service.AtualizaDadosService;
import br.com.vostre.repertori.utils.ServiceUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRepertorio;
    Button btnEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaServicoAtualizacao();

        btnRepertorio = (Button) findViewById(R.id.btnRepertorio);
        btnEventos = (Button) findViewById(R.id.btnEventos);

        btnRepertorio.setOnClickListener(this);
        btnEventos.setOnClickListener(this);

    }

    private void iniciaServicoAtualizacao(){
        final ServiceUtils serviceUtils = new ServiceUtils();
        Intent serviceIntent = new Intent(getBaseContext(), AtualizaDadosService.class);

        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if(!serviceUtils.isMyServiceRunning(AtualizaDadosService.class, manager)){
            stopService(serviceIntent);
            startService(serviceIntent);
            //Toast.makeText(this, "Iniciando serviço...", Toast.LENGTH_LONG).show();
        } else{
            //Toast.makeText(this, "Serviço já rodando...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRepertorio:
                Toast.makeText(getApplicationContext(), "Repertório", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnEventos:
                Toast.makeText(getApplicationContext(), "Eventos", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
