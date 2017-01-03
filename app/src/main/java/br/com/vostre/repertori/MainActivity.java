package br.com.vostre.repertori;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.vostre.repertori.service.AtualizaDadosService;
import br.com.vostre.repertori.utils.ServiceUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
