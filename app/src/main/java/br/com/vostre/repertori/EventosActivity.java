package br.com.vostre.repertori;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.adapter.TipoEventoLegendaList;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalEventos;
import br.com.vostre.repertori.form.ModalHora;
import br.com.vostre.repertori.fragment.FragmentEvento;
import br.com.vostre.repertori.fragment.FragmentRepertorio;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.ModalEventoListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.TipoEventoDBHelper;

public class EventosActivity extends BaseActivity implements View.OnClickListener, ModalCadastroListener, ModalHoraListener, ModalEventoListener {

    private ViewPager pager;
    private ScreenPagerAdapter pagerAdapter;

    List<Evento> eventos;
    EventoDBHelper eventoDBHelper;

    Calendar dataEscolhida;
    CaldroidFragment caldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventoDBHelper = new EventoDBHelper(getApplicationContext());

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                dataEscolhida = Calendar.getInstance();
                dataEscolhida.setTime(date);

                List<Evento> eventosData = eventoDBHelper.listarTodosPorData(getApplicationContext(), dataEscolhida);

                if(eventosData.size() > 0){
                    abrirModalEventos(eventosData);
                } else{
                    abrirModalHora();
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }
        };

        caldroidFragment.setCaldroidListener(listener);

        atualizaCalendario();

        caldroidFragment.refreshView();

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {

        if(resultado > -1){
            atualizaCalendario();
        }

    }

    private void abrirModalCadastro(Evento evento, Calendar data){
        ModalCadastroEvento modalCadastroEvento = new ModalCadastroEvento();
        modalCadastroEvento.setListener(this);

        if(evento != null){
            modalCadastroEvento.setEvento(evento);
        }

        if(data != null){
            modalCadastroEvento.setData(data);
        }

        modalCadastroEvento.show(getSupportFragmentManager(), "modalEvento");
    }

    private void abrirModalHora(){
        ModalHora modalHora = new ModalHora();
        modalHora.setListener(this);

        modalHora.show(getSupportFragmentManager(), "modalHora");
    }

    @Override
    public void onModalHoraDismissed(Calendar hora) {

        if(hora != null){
            dataEscolhida.set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY));
            dataEscolhida.set(Calendar.MINUTE, hora.get(Calendar.MINUTE));
            abrirModalCadastro(null, dataEscolhida);
        }

    }

    private void atualizaCalendario(){

        eventos = eventoDBHelper.listarTodos(getApplicationContext());

        for(Evento evento : eventos){
            Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.fundo_evento, null);
            int cor = Color.parseColor("#66"+evento.getTipoEvento().getCor().replace("#", ""));
            dr.mutate().setColorFilter(cor, PorterDuff.Mode.MULTIPLY);

            caldroidFragment.setBackgroundDrawableForDate(dr, evento.getData().getTime());
        }

        caldroidFragment.refreshView();
    }

    private void abrirModalEventos(List<Evento> eventos){
        ModalEventos modalEventos = new ModalEventos();
        modalEventos.setEventos(eventos);
        modalEventos.setListener(this);
        modalEventos.setData(dataEscolhida);

        modalEventos.show(getSupportFragmentManager(), "modalEventos");
    }

    @Override
    public void onModalEventoDismissed(int resultado) {

        if(resultado > 0){
            abrirModalHora();
        }

    }
}
