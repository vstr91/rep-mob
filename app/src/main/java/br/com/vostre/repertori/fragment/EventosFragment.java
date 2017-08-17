package br.com.vostre.repertori.fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ProjetoList;
import br.com.vostre.repertori.adapter.TipoEventoLegendaList;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalEventos;
import br.com.vostre.repertori.form.ModalHora;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.ModalEventoListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.model.dao.TipoEventoDBHelper;

public class EventosFragment extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, ModalHoraListener, ModalEventoListener {

    List<Evento> eventos;
    EventoDBHelper eventoDBHelper;

    List<TipoEvento> tiposEventos;
    TipoEventoDBHelper tipoEventoDBHelper;

    Calendar dataEscolhida;
    CaldroidFragment caldroidFragment;

    ListView listViewTiposEvento;

    ModalEventos modalEventos;

    public EventosFragment() {
        // Required empty public constructor
    }


    public static EventosFragment newInstance() {
        EventosFragment fragment = new EventosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_eventos, container, false);

        eventoDBHelper = new EventoDBHelper(getContext());
        tipoEventoDBHelper = new TipoEventoDBHelper(getContext());

        listViewTiposEvento = (ListView) v.findViewById(R.id.listViewTiposEvento);

        tiposEventos = tipoEventoDBHelper.listarTodos(getContext());

        TipoEventoLegendaList legendaAdapter = new TipoEventoLegendaList(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, tiposEventos);
        listViewTiposEvento.setAdapter(legendaAdapter);

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                abrirModalData(date);

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }
        };

        caldroidFragment.setCaldroidListener(listener);

        atualizaCalendario();

        caldroidFragment.refreshView();

        if(savedInstanceState != null){

            if(savedInstanceState.getLong("data") > 0){
                Date date = new Date(savedInstanceState.getLong("data"));
                abrirModalData(date);
            }

            ModalCadastroEvento modalCadastroEvento = (ModalCadastroEvento) getFragmentManager()
                    .findFragmentByTag("modalEvento");

            if (modalCadastroEvento != null) {
                modalCadastroEvento.setListener(this);
            }

            ModalHora modalHora = (ModalHora) getFragmentManager()
                    .findFragmentByTag("modalHora");

            if (modalHora != null) {
                modalHora.setListener(this);
            }

            ModalEventos modalEventos = (ModalEventos) getFragmentManager()
                    .findFragmentByTag("modalEventos");

            if (modalEventos != null) {
                modalEventos.setListener(this);
            }

        }

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), "Clicou", Toast.LENGTH_LONG).show();
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

        modalCadastroEvento.show(getFragmentManager(), "modalEvento");
    }

    private void abrirModalHora(){
        ModalHora modalHora = new ModalHora();
        modalHora.setListener(this);
        modalHora.setData(dataEscolhida);

        modalHora.show(getFragmentManager(), "modalHora");
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

        eventos = eventoDBHelper.listarTodos(getContext());

        for(Evento evento : eventos){
            Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.fundo_evento, null);
            int cor = Color.parseColor("#66"+evento.getTipoEvento().getCor().replace("#", ""));
            dr.mutate().setColorFilter(cor, PorterDuff.Mode.MULTIPLY);

            caldroidFragment.setBackgroundDrawableForDate(dr, evento.getData().getTime());
        }

        caldroidFragment.refreshView();
    }

    private void abrirModalEventos(List<Evento> eventos){
        modalEventos = new ModalEventos();
        modalEventos.setEventos(eventos);
        modalEventos.setListener(this);
        modalEventos.setData(dataEscolhida);

        modalEventos.show(getFragmentManager(), "modalEventos");
    }

    private void abrirModalData(Date date){
        dataEscolhida = Calendar.getInstance();
        dataEscolhida.setTime(date);

        List<Evento> eventosData = eventoDBHelper.listarTodosPorData(getContext(), dataEscolhida);

        if(eventosData.size() > 0){
            abrirModalEventos(eventosData);
        } else{
            abrirModalHora();
        }
    }

    @Override
    public void onModalEventoDismissed(int resultado) {

        if(resultado > 0){
            abrirModalHora();
        }

    }

}
