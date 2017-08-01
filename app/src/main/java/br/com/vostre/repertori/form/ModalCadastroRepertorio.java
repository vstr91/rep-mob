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
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ProjetoList;
import br.com.vostre.repertori.adapter.TipoEventoList;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.model.dao.TipoEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroRepertorio extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editTextNome;
    Spinner spinnerProjeto;
    Spinner spinnerStatus;
    Button btnSalvar;
    Button btnFechar;

    ModalCadastroListener listener;

    int status;
    Repertorio repertorio;
    Projeto projeto;
    List<Projeto> projetos;
    List<StatusMusica> statusList;

    Calendar data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Repertorio getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Repertorio repertorio) {
        this.repertorio = repertorio;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ProjetoDBHelper projetoDBHelper;

        View view = inflater.inflate(R.layout.modal_cadastro_repertorio, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        spinnerProjeto = (Spinner) view.findViewById(R.id.spinnerProjeto);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        projetoDBHelper = new ProjetoDBHelper(getContext());
        projetos = projetoDBHelper.listarTodosAtivos(getContext());
        ProjetoList adapterProjeto = new ProjetoList(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, projetos);

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

        spinnerProjeto.setAdapter(adapterProjeto);
        spinnerProjeto.setOnItemSelectedListener(this);

        spinnerStatus.setAdapter(statusAdapter);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);

        if(getRepertorio() != null){
            editTextNome.setText(getRepertorio().getNome());

            Projeto projeto = getRepertorio().getProjeto();
            int indexProj = projetos.indexOf(projeto);
            spinnerProjeto.setSelection(indexProj);

            switch(repertorio.getStatus()){
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

                if(nome.isEmpty()){
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                } else{

                    RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(getContext());

                    if(getRepertorio() != null){
                        repertorio.setNome(nome);
                        repertorio.setProjeto(projeto);
                        repertorio.setUltimaAlteracao(Calendar.getInstance());
                        repertorio.setEnviado(-1);

                        repertorio.setStatus(status.getId());
                        
                    } else{
                        repertorio = new Repertorio();
                        repertorio.setNome(nome);
                        repertorio.setProjeto(projeto);
                        repertorio.setDataCadastro(Calendar.getInstance());
                        repertorio.setUltimaAlteracao(Calendar.getInstance());
                        repertorio.setEnviado(-1);

                        repertorio.setStatus(status.getId());

                    }

                    repertorioDBHelper.salvarOuAtualizar(getContext(), repertorio);
                    SnackbarHelper.notifica(getView(), "Cadastrado com Sucesso!", Snackbar.LENGTH_LONG);
                    listener.onModalCadastroDismissed(0);
                    dismiss();

                }

                break;
            case R.id.btnFechar:
                listener.onModalCadastroDismissed(-1);
                dismiss();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        projeto = projetos.get(position);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
