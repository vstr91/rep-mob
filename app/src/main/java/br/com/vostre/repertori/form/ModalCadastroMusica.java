package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
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
import java.util.Map;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroMusica extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editTextNome;
    EditText editTextTom;
    Spinner spinnerArtista;
    Spinner spinnerStatus;
    Button btnSalvar;
    Button btnFechar;

    ModalCadastroListener listener;

    int status;
    Artista artista;
    List<Artista> artistas;
    Musica musica;
    List<StatusMusica> statusList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MusicaDBHelper musicaDBHelper;
        ArtistaDBHelper artistaDBHelper;

        View view = inflater.inflate(R.layout.modal_cadastro_musica, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        editTextTom = (EditText) view.findViewById(R.id.editTextTom);
        spinnerArtista = (Spinner) view.findViewById(R.id.spinnerArtista);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        artistaDBHelper = new ArtistaDBHelper(getContext());
        artistas = artistaDBHelper.listarTodos(getContext());
        ArtistaList adapterArtista = new ArtistaList(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, artistas);

        statusList = new ArrayList<>();

        StatusMusica ativo = new StatusMusica();
        ativo.setId(0);
        ativo.setTexto("Ativo");

        StatusMusica emEspera = new StatusMusica();
        emEspera.setId(1);
        emEspera.setTexto("Em Espera");

        StatusMusica inativo = new StatusMusica();
        inativo.setId(2);
        inativo.setTexto("Inativo");

        StatusMusica sugestao = new StatusMusica();
        sugestao.setId(3);
        sugestao.setTexto("Sugestão");

        statusList.add(ativo);
        statusList.add(emEspera);
        statusList.add(inativo);
        statusList.add(sugestao);

        SpinnerAdapter statusAdapter = new ArrayAdapter<StatusMusica>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, statusList);

        spinnerArtista.setAdapter(adapterArtista);
        spinnerArtista.setOnItemSelectedListener(this);

        spinnerStatus.setAdapter(statusAdapter);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);

        if(getMusica() != null){
            editTextNome.setText(getMusica().getNome());
            editTextTom.setText(getMusica().getTom());
            Artista artista = getMusica().getArtista();
            int index = adapterArtista.getPosition(artista);
            spinnerArtista.setSelection(index);

            switch(musica.getStatus()){
                case 0:
                    spinnerStatus.setSelection(statusList.indexOf(ativo));
                    break;
                case 1:
                    spinnerStatus.setSelection(statusList.indexOf(emEspera));
                    break;
                case 2:
                    spinnerStatus.setSelection(statusList.indexOf(inativo));
                    break;
                case 3:
                    spinnerStatus.setSelection(statusList.indexOf(sugestao));
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
                String tom = editTextTom.getText().toString();
                StatusMusica status = (StatusMusica) spinnerStatus.getSelectedItem();

                if(nome.isEmpty() || tom.isEmpty() || artista == null || (musica != null && status == null)){
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                } else{

                    MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getContext());

                    if(getMusica() != null){
                        musica.setNome(nome);
                        musica.setTom(tom);
                        musica.setArtista(artista);
                        musica.setUltimaAlteracao(Calendar.getInstance());
                        musica.setEnviado(-1);

                        musica.setStatus(status.getId());
                        
                    } else{
                        musica = new Musica();
                        musica.setNome(nome);
                        musica.setTom(tom);
                        musica.setArtista(artista);
                        musica.setDataCadastro(Calendar.getInstance());
                        musica.setUltimaAlteracao(Calendar.getInstance());
                        musica.setEnviado(-1);

                        switch(getStatus()){
                            case 0:
                                musica.setStatus(0);
                                break;
                            case 1:
                                musica.setStatus(1);
                                break;
                            case 2:
                                musica.setStatus(3);
                                break;
                        }
                    }

                    if((musica.getId() != null && getStatus() > 0) && musicaDBHelper.jaExiste(getContext(), musica)){
                        Toast.makeText(getContext(), "O registro informado já existe!", Toast.LENGTH_SHORT).show();
                    } else{
                        musicaDBHelper.salvarOuAtualizar(getContext(), musica);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        artista = artistas.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
