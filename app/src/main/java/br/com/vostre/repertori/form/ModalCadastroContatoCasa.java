package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.ContatoCasa;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ContatoCasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroContatoCasa extends android.support.v4.app.DialogFragment implements View.OnClickListener, ModalAdicionaListener {

    EditText editTextNome;
    EditText editTextTelefone;
    EditText editTextEmail;
    EditText editTextObservacoes;
    EditText editTextCargo;
    Spinner spinnerStatus;
    Button btnSalvar;
    Button btnFechar;
    Button btnContatoExistente;

    ModalCadastroListener listener;

    int status;
    ContatoCasa contatoCasa;
    List<StatusMusica> statusList;

    Casa casa;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContatoCasa getContatoCasa() {
        return contatoCasa;
    }

    public void setContatoCasa(ContatoCasa contatoCasa) {
        this.contatoCasa = contatoCasa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_cadastro_contato_casa, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        editTextTelefone = (EditText) view.findViewById(R.id.editTextTelefone);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextObservacoes = (EditText) view.findViewById(R.id.editTextObservacao);
        editTextCargo = (EditText) view.findViewById(R.id.editTextCargo);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnContatoExistente = (Button) view.findViewById(R.id.btnContatoExistente);

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

        spinnerStatus.setAdapter(statusAdapter);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);
        btnContatoExistente.setOnClickListener(this);

        if(getContatoCasa().getContato() != null){
            btnContatoExistente.setVisibility(View.GONE);

            editTextNome.setText(getContatoCasa().getContato().getNome());
            editTextTelefone.setText(getContatoCasa().getContato().getTelefone());
            editTextEmail.setText(getContatoCasa().getContato().getEmail());
            editTextCargo.setText(getContatoCasa().getCargo());
            editTextObservacoes.setText(getContatoCasa().getContato().getObservacao());

            switch(contatoCasa.getStatus()){
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
                String telefone = editTextTelefone.getText().toString();
                String email = editTextEmail.getText().toString();
                String observacoes = editTextObservacoes.getText().toString();
                StatusMusica status = (StatusMusica) spinnerStatus.getSelectedItem();

                if((nome.isEmpty() || telefone.isEmpty()) || (contatoCasa != null && status == null)){
                    Toast.makeText(getContext(), "Por favor informe ao menos nome e telefone", Toast.LENGTH_SHORT).show();
                } else{

                    ContatoDBHelper contatoDBHelper = new ContatoDBHelper(getContext());

                    Contato umContato;

                    if(getContatoCasa().getContato() != null){

                        umContato = getContatoCasa().getContato();

                        umContato.setNome(nome);
                        umContato.setTelefone(telefone);
                        umContato.setEmail(email);
                        umContato.setObservacao(observacoes);
                        umContato.setUltimaAlteracao(Calendar.getInstance());
                        umContato.setEnviado(-1);

                        umContato.setStatus(status.getId());
                        
                    } else{
                        umContato = new Contato();
                        umContato.setNome(nome);
                        umContato.setTelefone(telefone);
                        umContato.setEmail(email);
                        umContato.setObservacao(observacoes);
                        umContato.setDataCadastro(Calendar.getInstance());
                        umContato.setUltimaAlteracao(Calendar.getInstance());
                        umContato.setEnviado(-1);
                        umContato.setStatus(status.getId());
                    }

                    if(contatoDBHelper.jaExiste(getContext(), umContato)){
                        Toast.makeText(getContext(), "O telefone informado já existe!", Toast.LENGTH_SHORT).show();
                    } else{
                        String id = contatoDBHelper.salvarOuAtualizar(getContext(), umContato);
                        ContatoCasaDBHelper contatoCasaDBHelper = new ContatoCasaDBHelper(getContext());

                        if(!id.equals("")){
                            Contato contato = new Contato();
                            contato.setId(id);

                            contato = contatoDBHelper.carregar(getContext(), contato);
                            contatoCasa.setContato(contato);
                            contatoCasa.setCargo(editTextCargo.getText().toString());
                            contatoCasa.setObservacao(editTextObservacoes.getText().toString());
                            contatoCasa.setStatus(0);
                            contatoCasa.setEnviado(-1);
                            contatoCasa.setUltimaAlteracao(Calendar.getInstance());
                            contatoCasaDBHelper.salvarOuAtualizar(getContext(), contatoCasa);
                        } else{
                            contatoCasa.setCargo(editTextCargo.getText().toString());
                            contatoCasa.setObservacao(editTextObservacoes.getText().toString());
                            contatoCasa.setStatus(0);
                            contatoCasa.setEnviado(-1);
                            contatoCasa.setUltimaAlteracao(Calendar.getInstance());
                            contatoCasaDBHelper.salvarOuAtualizar(getContext(), contatoCasa);
                        }

                        SnackbarHelper.notifica(getView(), "Cadastrado com Sucesso!", Snackbar.LENGTH_LONG);
                        listener.onModalCadastroDismissed(0);
                        dismiss();
                    }

                }

                break;
            case R.id.btnFechar:
                listener.onModalCadastroDismissed(0);
                dismiss();
                break;
            case R.id.btnContatoExistente:
                ModalListaContato modalListaContato = new ModalListaContato();
                modalListaContato.setListener(this);
                modalListaContato.setCasa(contatoCasa.getCasa());

                modalListaContato.show(this.getFragmentManager(), "modalListaContato");
                break;
        }

    }

    @Override
    public void onModalAdicionaDismissed(int resultado) {
        listener.onModalCadastroDismissed(0);
        dismiss();
    }
}
