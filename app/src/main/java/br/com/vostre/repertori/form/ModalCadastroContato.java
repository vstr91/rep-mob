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
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.ContatoCasa;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ContatoCasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroContato extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    EditText editTextNome;
    EditText editTextTelefone;
    EditText editTextEmail;
    EditText editTextObservacoes;
    Spinner spinnerStatus;
    Button btnSalvar;
    Button btnFechar;

    ModalCadastroListener listener;

    int status;
    Contato contato;
    List<StatusMusica> statusList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_cadastro_contato, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        editTextTelefone = (EditText) view.findViewById(R.id.editTextTelefone);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextObservacoes = (EditText) view.findViewById(R.id.editTextObservacao);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

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

        if(getContato() != null){
            editTextNome.setText(getContato().getNome());
            editTextTelefone.setText(getContato().getTelefone());
            editTextEmail.setText(getContato().getEmail());
            editTextObservacoes.setText(getContato().getObservacao());

            switch(contato.getStatus()){
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

                if((nome.isEmpty() || telefone.isEmpty()) || (contato != null && status == null)){
                    Toast.makeText(getContext(), "Por favor informe ao menos nome e telefone", Toast.LENGTH_SHORT).show();
                } else{

                    ContatoDBHelper contatoDBHelper = new ContatoDBHelper(getContext());

                    if(getContato() != null){
                        contato.setNome(nome);
                        contato.setTelefone(telefone);
                        contato.setEmail(email);
                        contato.setObservacao(observacoes);
                        contato.setUltimaAlteracao(Calendar.getInstance());
                        contato.setEnviado(-1);

                        contato.setStatus(status.getId());
                        
                    } else{
                        contato = new Contato();
                        contato.setNome(nome);
                        contato.setTelefone(telefone);
                        contato.setEmail(email);
                        contato.setObservacao(observacoes);
                        contato.setDataCadastro(Calendar.getInstance());
                        contato.setUltimaAlteracao(Calendar.getInstance());
                        contato.setEnviado(-1);
                        contato.setStatus(status.getId());
                    }

                    if(contatoDBHelper.jaExiste(getContext(), contato)){
                        Toast.makeText(getContext(), "O telefone informado já existe!", Toast.LENGTH_SHORT).show();
                    } else{
                        contatoDBHelper.salvarOuAtualizar(getContext(), contato);

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
        }

    }
}
