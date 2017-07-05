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
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.adapter.EstiloList;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroEstilo extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editTextNome;
    Button btnSalvar;
    Button btnFechar;

    ModalCadastroListener listener;

    Estilo estilo;

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        EstiloDBHelper estiloDBHelper;

        View view = inflater.inflate(R.layout.modal_cadastro_estilo, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);

        if(getEstilo() != null){
            editTextNome.setText(getEstilo().getNome());

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

                if(nome.isEmpty()){
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                } else{

                    EstiloDBHelper estiloDBHelper = new EstiloDBHelper(getContext());

                    if(getEstilo() != null){
                        estilo.setNome(nome);
                        estilo.setUltimaAlteracao(Calendar.getInstance());
                        estilo.setEnviado(-1);

                        estilo.setStatus(0);
                        
                    } else{
                        estilo = new Estilo();
                        estilo.setNome(nome);
                        estilo.setDataCadastro(Calendar.getInstance());
                        estilo.setUltimaAlteracao(Calendar.getInstance());
                        estilo.setEnviado(-1);
                        estilo.setStatus(0);
                    }

                    if(estiloDBHelper.jaExiste(getContext(), estilo)){
                        Toast.makeText(getContext(), "O registro informado já existe!", Toast.LENGTH_SHORT).show();
                    } else{
                        estiloDBHelper.salvarOuAtualizar(getContext(), estilo);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
