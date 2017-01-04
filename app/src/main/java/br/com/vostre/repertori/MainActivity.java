package br.com.vostre.repertori;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.vostre.repertori.service.AtualizaDadosService;
import br.com.vostre.repertori.utils.ServiceUtils;
import br.com.vostre.repertori.utils.ToolbarUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRepertorio;
    Button btnEventos;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        iniciaServicoAtualizacao();

        btnRepertorio = (Button) findViewById(R.id.btnRepertorio);
        btnEventos = (Button) findViewById(R.id.btnEventos);

        btnRepertorio.setOnClickListener(this);
        btnEventos.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        /*if(iniciaModoCamera){
            getMenuInflater().inflate(R.menu.realidade_aumentada, menu);
        } else{
            getMenuInflater().inflate(R.menu.main, menu);
        }*/

        this.menu = menu;

        ToolbarUtils.preparaMenu(menu, this, this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;

        switch (id){
            /*case R.id.icon_config:
                intent = new Intent(this, Parametros.class);
                startActivity(intent);
                break;*/
//            case R.id.icon_msg:
//                intent = new Intent(this, Mensagens.class);
//                startActivity(intent);
//                break;
            case android.R.id.home:
                return true;
        }


        return super.onOptionsItemSelected(item);
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

        Intent intent;

        switch (v.getId()){
            case R.id.btnRepertorio:
                intent = new Intent(getBaseContext(), RepertorioActivity.class);
                startActivity(intent);
                break;
            case R.id.btnEventos:
                intent = new Intent(getBaseContext(), EventosActivity.class);
                startActivity(intent);
                break;
        }

    }
}
