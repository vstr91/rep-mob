package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.ProjetoList;
import br.com.vostre.repertori.adapter.TipoEventoList;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.ModalDataListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.model.dao.TipoEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroEvento extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, ModalHoraListener, ModalDataListener {

    EditText editTextNome;
    TextView textViewData;
    Spinner spinnerTipoEvento;
    Spinner spinnerProjeto;
    Spinner spinnerStatus;
    Button btnSalvar;
    Button btnFechar;
    Button btnData;

    TextView textViewLabelStatus;

    ModalCadastroListener listener;

    int status;
    Evento evento;
    TipoEvento tipoEvento;
    Projeto projeto;
    List<TipoEvento> tiposEvento;
    List<Projeto> projetos;
    List<StatusMusica> statusList;

    Calendar data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TipoEventoDBHelper tipoEventoDBHelper;
        ProjetoDBHelper projetoDBHelper;

        View view = inflater.inflate(R.layout.modal_cadastro_evento, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        textViewData = (TextView) view.findViewById(R.id.textViewData);
        spinnerTipoEvento = (Spinner) view.findViewById(R.id.spinnerTipoEvento);
        spinnerProjeto = (Spinner) view.findViewById(R.id.spinnerProjeto);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnData = (Button) view.findViewById(R.id.btnData);

        tipoEventoDBHelper = new TipoEventoDBHelper(getContext());
        tiposEvento = tipoEventoDBHelper.listarTodos(getContext());
        TipoEventoList adapterTipoEvento = new TipoEventoList(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, tiposEvento);

        projetoDBHelper = new ProjetoDBHelper(getContext());
        projetos = projetoDBHelper.listarTodosAtivos(getContext());
        ProjetoList adapterProjeto = new ProjetoList(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, projetos, false);

        statusList = new ArrayList<>();

        StatusMusica ativo = new StatusMusica();
        ativo.setId(0);
        ativo.setTexto("Ativo");

        StatusMusica inativo = new StatusMusica();
        inativo.setId(2);
        inativo.setTexto("Inativo");

        statusList.add(ativo);
        statusList.add(inativo);

        SpinnerAdapter statusAdapter = new ArrayAdapter<StatusMusica>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, statusList);

        spinnerTipoEvento.setAdapter(adapterTipoEvento);
        spinnerTipoEvento.setOnItemSelectedListener(this);

        spinnerProjeto.setAdapter(adapterProjeto);
        spinnerProjeto.setOnItemSelectedListener(this);

        spinnerStatus.setAdapter(statusAdapter);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);
        btnData.setOnClickListener(this);

        if(data != null){
            textViewData.setText(DataUtils.toString(data, true));
        }

        if(getEvento() != null){
            editTextNome.setText(getEvento().getNome());

            TipoEvento tipoEvento = getEvento().getTipoEvento();
            int index = tiposEvento.indexOf(tipoEvento);
            spinnerTipoEvento.setSelection(index);

            Projeto projeto = getEvento().getProjeto();
            int indexProj = projetos.indexOf(projeto);
            spinnerProjeto.setSelection(indexProj);

            switch(evento.getStatus()){
                case 0:
                    spinnerStatus.setSelection(statusList.indexOf(ativo));
                    break;
                case 2:
                    spinnerStatus.setSelection(statusList.indexOf(inativo));
                    break;
            }

        }

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public ModalCadastroListener getListener() {
        return listener;
    }

    public void setListener(ModalCadastroListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnSalvar:

                String nome = editTextNome.getText().toString();
                StatusMusica status = (StatusMusica) spinnerStatus.getSelectedItem();

                if(nome.isEmpty() || tiposEvento == null){
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                } else{

                    EventoDBHelper eventoDBHelper = new EventoDBHelper(getContext());

                    if(getEvento() != null){
                        evento.setNome(nome);
                        evento.setData(data);
                        evento.setTipoEvento(tipoEvento);
                        evento.setProjeto(projeto);
                        evento.setUltimaAlteracao(Calendar.getInstance());
                        evento.setEnviado(-1);

                        evento.setStatus(status.getId());
                        
                    } else{
                        evento = new Evento();
                        evento.setNome(nome);
                        evento.setData(data);
                        evento.setTipoEvento(tipoEvento);
                        evento.setProjeto(projeto);
                        evento.setDataCadastro(Calendar.getInstance());
                        evento.setUltimaAlteracao(Calendar.getInstance());
                        evento.setEnviado(-1);

                        evento.setStatus(status.getId());

                    }

                    if(evento.getId() != null && eventoDBHelper.jaExiste(getContext(), evento)){
                        Toast.makeText(getContext(), "O registro informado já existe!", Toast.LENGTH_SHORT).show();
                    } else{
                        eventoDBHelper.salvarOuAtualizar(getContext(), evento);
                        SnackbarHelper.notifica(getView(), "Cadastrado com Sucesso!", Snackbar.LENGTH_LONG);
                        listener.onModalCadastroDismissed(0);
                        dismiss();
                    }

                }

                break;
            case R.id.btnFechar:
                listener.onModalCadastroDismissed(-1);
                dismiss();
                break;
            case R.id.btnData:
                ModalData modalData = new ModalData();
                modalData.setListener(this);
                modalData.setData(data);

                modalData.show(getFragmentManager(), "modalData");
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getSelectedItem() instanceof TipoEvento){
            tipoEvento = tiposEvento.get(position);
        } else{
            projeto = projetos.get(position);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onModalHoraDismissed(Calendar hora) {

        if(hora != null){
            this.data = hora;

            textViewData.setText(DataUtils.toString(data, true));
        }

    }

    @Override
    public void onModalDataDismissed(Calendar data) {

        if(data != null){
            this.data.set(Calendar.DAY_OF_MONTH, data.get(Calendar.DAY_OF_MONTH));
            this.data.set(Calendar.MONTH, data.get(Calendar.MONTH));
            this.data.set(Calendar.YEAR, data.get(Calendar.YEAR));

            ModalHora modalHora = new ModalHora();
            modalHora.setListener(this);
            modalHora.setData(this.data);
            modalHora.setEvento(evento);

            modalHora.show(getFragmentManager(), "modalHora");
        }

    }
}
