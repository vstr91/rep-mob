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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.ArtistasActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroArtista extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    EditText editTextNome;
    Spinner spinnerStatus;
    Button btnSalvar;
    Button btnFechar;

    ModalCadastroListener listener;

    int status;
    Artista artista;
    List<Artista> artistas;
    List<StatusMusica> statusList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArtistaDBHelper artistaDBHelper;

        View view = inflater.inflate(R.layout.modal_cadastro_artista, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
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

        if(getArtista() != null){
            editTextNome.setText(getArtista().getNome());

            switch(artista.getStatus()){
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

                if(nome.isEmpty() || (artista != null && status == null)){
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                } else{

                    ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(getContext());

                    if(getArtista() != null){
                        artista.setNome(nome);
                        artista.setUltimaAlteracao(Calendar.getInstance());
                        artista.setEnviado(-1);

                        artista.setStatus(status.getId());
                        
                    } else{
                        artista = new Artista();
                        artista.setNome(nome);
                        artista.setDataCadastro(Calendar.getInstance());
                        artista.setUltimaAlteracao(Calendar.getInstance());
                        artista.setEnviado(-1);
                        artista.setStatus(status.getId());
                    }

                    if((artista.getId() != null && getStatus() > 0) && artistaDBHelper.jaExiste(getContext(), artista)){
                        Toast.makeText(getContext(), "O registro informado já existe!", Toast.LENGTH_SHORT).show();
                    } else{
                        artistaDBHelper.salvarOuAtualizar(getContext(), artista);
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
