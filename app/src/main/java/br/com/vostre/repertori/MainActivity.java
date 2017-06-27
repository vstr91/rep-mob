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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import br.com.vostre.repertori.service.AtualizaDadosService;
import br.com.vostre.repertori.utils.ServiceUtils;
import br.com.vostre.repertori.utils.ToolbarUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRepertorio;
    Button btnEventos;
    Button btnArtistas;
    Button btnRelatorios;
    Menu menu;
    TextView textViewUsuario;

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
        btnArtistas = (Button) findViewById(R.id.btnArtistas);
        btnRelatorios = (Button) findViewById(R.id.btnRelatorios);
        textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);

        btnRepertorio.setOnClickListener(this);
        btnEventos.setOnClickListener(this);
        btnArtistas.setOnClickListener(this);
        btnRelatorios.setOnClickListener(this);

//        GoogleSignInAccount acc = (GoogleSignInAccount) getIntent().getExtras().get("usuario");
//        textViewUsuario.setText("Logado como "+acc.getDisplayName()+" ("+acc.getEmail()+")");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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
            case R.id.icon_sair:
                onBackPressed();
                break;
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
            case R.id.btnArtistas:
                intent = new Intent(getBaseContext(), ArtistasActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRelatorios:
                intent = new Intent(getBaseContext(), RelatoriosActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        //signOut();
        super.onBackPressed();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(App.getGoogleApiHelper().getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

}
