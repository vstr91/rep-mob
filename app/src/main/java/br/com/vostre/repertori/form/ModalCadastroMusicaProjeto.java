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

import org.w3c.dom.Text;

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
import br.com.vostre.repertori.model.MusicaProjeto;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalCadastroMusicaProjeto extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    TextView textViewNome;
    Spinner spinnerStatus;
    Button btnSalvar;
    Button btnFechar;

    ModalCadastroListener listener;

    int status;
    Musica musica;
    Projeto projeto;
    List<StatusMusica> statusList;
    MusicaProjeto musicaProjeto;

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

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MusicaDBHelper musicaDBHelper;
        ArtistaDBHelper artistaDBHelper;
        EstiloDBHelper estiloDBHelper;

        View view = inflater.inflate(R.layout.modal_cadastro_musica_projeto, container, false);

        view.setMinimumWidth(700);

        textViewNome = (TextView) view.findViewById(R.id.textViewNome);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        statusList = new ArrayList<>();

        StatusMusica ativo = new StatusMusica();
        ativo.setId(0);
        ativo.setTexto("Ativo");

        StatusMusica emEspera = new StatusMusica();
        emEspera.setId(1);
        emEspera.setTexto("Fila");

        StatusMusica sugestao = new StatusMusica();
        sugestao.setId(3);
        sugestao.setTexto("Sugestão");

        StatusMusica inativo = new StatusMusica();
        inativo.setId(2);
        inativo.setTexto("Removida");

        statusList.add(ativo);
        statusList.add(emEspera);
        statusList.add(sugestao);
        statusList.add(inativo);

        SpinnerAdapter statusAdapter = new ArrayAdapter<StatusMusica>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, statusList);

        spinnerStatus.setAdapter(statusAdapter);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);

        if(getMusica() != null){
            textViewNome.setText(getMusica().getNome());

            MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());
            musicaProjeto = new MusicaProjeto();
            musicaProjeto.setProjeto(projeto);
            musicaProjeto.setMusica(musica);

            musicaProjeto = musicaProjetoDBHelper.carregarPorMusicaEProjeto(getContext(), musicaProjeto);

            switch(musicaProjeto.getStatus()){
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

                StatusMusica status = (StatusMusica) spinnerStatus.getSelectedItem();

                if(musica != null && status == null){
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                } else{

                    MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());

                    if(musicaProjeto != null){
                        musicaProjeto.setUltimaAlteracao(Calendar.getInstance());
                        musicaProjeto.setEnviado(-1);

                        musicaProjeto.setStatus(status.getId());
                        
                    }

                    musicaProjetoDBHelper.salvarOuAtualizar(getContext(), musicaProjeto);
                    SnackbarHelper.notifica(getView(), "Cadastrado com Sucesso!", Snackbar.LENGTH_LONG);
                    listener.onModalCadastroDismissed(0);
                    dismiss();

                }

                break;
            case R.id.btnFechar:
                listener.onModalCadastroDismissed(0);
                dismiss();
                break;
        }

    }

}
