package br.com.vostre.repertori;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.concurrent.TimeUnit;

import br.com.vostre.repertori.fragment.ArtistasFragment;
import br.com.vostre.repertori.fragment.EstilosFragment;
import br.com.vostre.repertori.fragment.EventosFragment;
import br.com.vostre.repertori.fragment.FragmentRepertorio;
import br.com.vostre.repertori.fragment.InfoFragment;
import br.com.vostre.repertori.fragment.MusicasFragment;
import br.com.vostre.repertori.fragment.ProjetosFragment;
import br.com.vostre.repertori.listener.LoadListener;
import br.com.vostre.repertori.service.AtualizaDadosService;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.ServiceUtils;
import br.com.vostre.repertori.utils.ToolbarUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener, LoadListener {

    private DrawerLayout drawer;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout conteudo;
    Class fragmentClass = null;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    Dialog dialogLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.container);
        navView = (NavigationView) findViewById(R.id.nav);
        conteudo = (FrameLayout) findViewById(R.id.conteudo);

        navView.setNavigationItemSelectedListener(this);

        fragmentClass = EventosFragment.class;
        fragmentManager = getSupportFragmentManager();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.conteudo, fragment).commit();
        setTitle("Eventos");

        // --------------------------------------------------------

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0){

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                drawerToggle.syncState();

                try {

                    switch(fragmentClass.getCanonicalName()){
                        case "br.com.vostre.repertori.fragment.MusicasFragment":
                            fragment = (MusicasFragment) fragmentClass.newInstance();
                            MusicasFragment f = (MusicasFragment) fragment;
                            f.setListener(MainActivity.this);
                            insereFragmento(f, true);
                            break;
                        case "br.com.vostre.repertori.fragment.ProjetosFragment":
                            fragment = (ProjetosFragment) fragmentClass.newInstance();
                            ProjetosFragment f2 = (ProjetosFragment) fragment;
                            f2.setListener(MainActivity.this);
                            insereFragmento(f2, false);
                            break;
                        default:
                            fragment = (Fragment) fragmentClass.newInstance();
                            insereFragmento(fragment, false);
                            break;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }

            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                drawerToggle.syncState();
            }

        };

        // --------------------------------------------------------

        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        iniciaServicoAtualizacao();

//        GoogleSignInAccount acc = (GoogleSignInAccount) getIntent().getExtras().get("usuario");
//        textViewUsuario.setText("Logado como "+acc.getDisplayName()+" ("+acc.getEmail()+")");

    }

    private void insereFragmento(Fragment f, boolean modal){
        if(f != null){
            fragmentManager.beginTransaction().replace(R.id.conteudo, f).commit();
            fragmentManager.executePendingTransactions();

            if(modal){
                dialogLoad = DialogUtils.criarAlertaCarregando(MainActivity.this, "Carregando dados", "Por favor aguarde...");
                dialogLoad.show();
            }

        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void iniciaServicoAtualizacao(){

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent serviceIntent = new Intent(this, AtualizaDadosService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, serviceIntent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TimeUnit.MINUTES.toMillis(Constants.TEMPO_ATUALIZACAO), pi);

        //

//        final ServiceUtils serviceUtils = new ServiceUtils();
//        Intent serviceIntent = new Intent(getBaseContext(), AtualizaDadosService.class);
//
//        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//
//        if(!serviceUtils.isMyServiceRunning(AtualizaDadosService.class, manager)){
//            stopService(serviceIntent);
//            startService(serviceIntent);
//            //Toast.makeText(this, "Iniciando serviço...", Toast.LENGTH_LONG).show();
//        } else{
//            //Toast.makeText(this, "Serviço já rodando...", Toast.LENGTH_LONG).show();
//        }

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){
//            case R.id.btnRepertorio:
//                intent = new Intent(getBaseContext(), RepertorioActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.btnEventos:
//                intent = new Intent(getBaseContext(), EventosActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.btnArtistas:
//                intent = new Intent(getBaseContext(), ArtistasActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.btnRelatorios:
//                intent = new Intent(getBaseContext(), RelatoriosActivity.class);
//                startActivity(intent);
//                break;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.projetos:
                fragmentClass = ProjetosFragment.class;
                break;
            case R.id.eventos:
                fragmentClass = EventosFragment.class;
                break;
            case R.id.musicas:
                fragmentClass = MusicasFragment.class;
                break;
            case R.id.estilos:
                fragmentClass = EstilosFragment.class;
                break;
            case R.id.artistas:
                fragmentClass = ArtistasFragment.class;
                break;
            case R.id.info:
                fragmentClass = InfoFragment.class;
                break;
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();

        return true;
    }

    @Override
    public void onLoadFinished() {

        if(dialogLoad != null){
            dialogLoad.dismiss();
        }

    }
}
