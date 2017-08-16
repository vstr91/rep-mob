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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ProjetoList;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaProjeto;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalMusicaProjeto extends android.support.v4.app.DialogFragment implements View.OnClickListener, ModalCadastroListener, AdapterView.OnItemSelectedListener {

    Button btnFechar;
    Button btnSalvar;
    Spinner spinnerProjetos;
    Musica musica;
    Projeto projeto = null;
    List<Projeto> projetos;

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_adicionar_projeto, container, false);

        view.setMinimumWidth(700);

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        spinnerProjetos = (Spinner) view.findViewById(R.id.spinnerProjetos);

        MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());

        projetos = musicaProjetoDBHelper.listarTodosDisponiveisMusica(getContext(), musica);
        ProjetoList adapter = new ProjetoList(getActivity(), R.layout.listview_estilos, projetos, false);

        spinnerProjetos.setAdapter(adapter);
        spinnerProjetos.setOnItemSelectedListener(this);

        btnFechar.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnSalvar:

                if(projeto != null){
                    MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());
                    MusicaProjeto musicaProjeto = new MusicaProjeto();
                    musicaProjeto.setMusica(musica);
                    musicaProjeto.setProjeto(projeto);
                    musicaProjeto = musicaProjetoDBHelper.carregarPorMusicaEProjeto(getContext(), musicaProjeto);

                    if(musicaProjeto == null){
                        musicaProjeto = new MusicaProjeto();
                        musicaProjeto.setMusica(musica);
                        musicaProjeto.setProjeto(projeto);
                        musicaProjeto.setStatus(0);
                        musicaProjeto.setEnviado(-1);
                        musicaProjeto.setDataCadastro(Calendar.getInstance());
                        musicaProjeto.setUltimaAlteracao(Calendar.getInstance());
                    } else{
                        musicaProjeto.setStatus(0);
                        musicaProjeto.setEnviado(-1);
                        musicaProjeto.setUltimaAlteracao(Calendar.getInstance());
                    }

                    musicaProjetoDBHelper.salvarOuAtualizar(getContext(), musicaProjeto);
                    SnackbarHelper.notifica(getView(), "Cadastrado com Sucesso!", Snackbar.LENGTH_LONG);
                    dismiss();

                } else{
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getSelectedItem() instanceof Projeto){
            projeto = projetos.get(position);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {

    }
}
