package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaAdicionaList;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.MusicaProjeto;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;

public class ModalAdicionaMusicaProjeto extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ModalCadastroListener {

    Button btnSalvar;
    Button btnFechar;
    Button btnCadastrarNovaMusica;
    ListView listViewMusicas;
    List<Musica> musicas;
    MusicaProjetoDBHelper musicaProjetoDBHelper;

    Projeto projeto;

    ModalAdicionaListener listener;
    MusicaAdicionaList adapterMusica;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());

        View view = inflater.inflate(R.layout.modal_adiciona_musica_projeto, container, false);

        view.setMinimumWidth(700);

        listViewMusicas = (ListView) view.findViewById(R.id.listViewMusicas);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnCadastrarNovaMusica = (Button) view.findViewById(R.id.btnCadastrarNovaMusica);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);
        btnCadastrarNovaMusica.setOnClickListener(this);

        musicas = musicaProjetoDBHelper.listarTodosAusentesProjeto(getContext(), projeto);

        adapterMusica = new MusicaAdicionaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);
        listViewMusicas.setAdapter(adapterMusica);
        listViewMusicas.setOnItemClickListener(this);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public ModalAdicionaListener getListener() {
        return listener;
    }

    public void setListener(ModalAdicionaListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnSalvar:

                int tamanhoLista = musicas.size();

                musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());

                for(int i = 0; i < tamanhoLista; i++){

                    Musica musica = musicas.get(i);

                    if(musica.isChecked()){
                        MusicaProjeto musicaProjeto = new MusicaProjeto();
                        musicaProjeto.setMusica(musicas.get(i));
                        musicaProjeto.setProjeto(projeto);

                        musicaProjeto = musicaProjetoDBHelper.carregarPorMusicaEProjeto(getContext(), musicaProjeto);

                        if(musicaProjeto == null){
                            musicaProjeto = new MusicaProjeto();
                            musicaProjeto.setMusica(musicas.get(i));
                            musicaProjeto.setProjeto(projeto);
                        }

                        musicaProjeto.setStatus(0);
                        musicaProjeto.setUltimaAlteracao(Calendar.getInstance());
                        musicaProjeto.setDataCadastro(Calendar.getInstance());
                        musicaProjeto.setEnviado(-1);

                        musicaProjetoDBHelper.salvarOuAtualizar(getContext(), musicaProjeto);

                    }

                }

                listener.onModalAdicionaDismissed(0);

                dismiss();
                break;
            case R.id.btnFechar:
                dismiss();
                break;
            case R.id.btnCadastrarNovaMusica:
                ModalCadastroMusica modalCadastroMusica = new ModalCadastroMusica();
                modalCadastroMusica.setListener(this);
                modalCadastroMusica.setStatus(0);

                modalCadastroMusica.show(getFragmentManager(), "modalMusica");
                break;
        }

    }

    public void atualizaLista(){
        musicas = musicaProjetoDBHelper.listarTodosAusentesProjeto(getContext(), projeto);

        adapterMusica = new MusicaAdicionaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);
        listViewMusicas.setAdapter(adapterMusica);
        adapterMusica.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxAdicionar);
//        checkBox.setChecked(!checkBox.isChecked());

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }



}
