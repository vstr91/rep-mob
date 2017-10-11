package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaAdicionaList;
import br.com.vostre.repertori.listener.FiltroMusicaListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaBloco;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.dao.MusicaBlocoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.utils.DialogUtils;

public class ModalAdicionaMusicaBlocoRepertorio extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, FiltroMusicaListener {

    Button btnSalvar;
    Button btnFechar;
    ListView listViewMusicas;
    List<Musica> musicas;
    MusicaBlocoDBHelper musicaBlocoDBHelper;
    Button btnFiltros;

    BlocoRepertorio blocoRepertorio;

    ModalAdicionaListener listener;
    MusicaAdicionaList adapterMusica;

    Estilo estiloFiltro;
    Artista artistaFiltro;

    Dialog dialog;
    Dialog dialogLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        musicaBlocoDBHelper = new MusicaBlocoDBHelper(getContext());

        View view = inflater.inflate(R.layout.modal_adiciona_musica, container, false);

        view.setMinimumWidth(700);

        listViewMusicas = (ListView) view.findViewById(R.id.listViewMusicas);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnFiltros = (Button) view.findViewById(R.id.btnFiltros);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);
        btnFiltros.setOnClickListener(this);

        musicas = musicaBlocoDBHelper.listarTodosAusentesBloco(getContext(), blocoRepertorio);

        adapterMusica = new MusicaAdicionaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);
        listViewMusicas.setAdapter(adapterMusica);
        listViewMusicas.setOnItemClickListener(this);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public BlocoRepertorio getBlocoRepertorio() {
        return blocoRepertorio;
    }

    public void setBlocoRepertorio(BlocoRepertorio blocoRepertorio) {
        this.blocoRepertorio = blocoRepertorio;
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

                SalvarEntidade salvarEntidade = new SalvarEntidade();
                salvarEntidade.execute();
                break;
            case R.id.btnFechar:
                dismiss();
                break;
            case R.id.btnFiltros:
                ModalFiltros modalFiltros = new ModalFiltros();
                modalFiltros.setListener(this);
                modalFiltros.setArtista(artistaFiltro);
                modalFiltros.setEstilo(estiloFiltro);

                modalFiltros.show(getFragmentManager(), "modalFiltros");
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxAdicionar);
//        checkBox.setChecked(!checkBox.isChecked());

    }

    private int corrigeOrdemMusicas(){

        List<MusicaBloco> musicas = musicaBlocoDBHelper.corrigirOrdemPorBloco(getContext(), blocoRepertorio);

        int qtdMusicas = musicas.size();

        for(int i = 0; i < qtdMusicas; i++){
            MusicaBloco musicaBloco = musicas.get(i);
            musicaBloco.setOrdem(i+1);
            musicaBloco.setEnviado(-1);
            musicaBloco.setUltimaAlteracao(Calendar.getInstance());

            musicaBlocoDBHelper.salvarOuAtualizar(getContext(), musicaBloco);

        }

        return qtdMusicas+1;

    }

    private void atualizaLista(){
        adapterMusica = new MusicaAdicionaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);
        listViewMusicas.setAdapter(adapterMusica);
    }

    @Override
    public void onFiltroMusicaDismissed(Estilo estilo, Artista artista) {

        artistaFiltro = artista;
        estiloFiltro = estilo;

        if(estilo.getSlug() != null || artista.getSlug() != null){
            musicas = musicaBlocoDBHelper.listarTodosAusentesBloco(getContext(), blocoRepertorio, estilo, artista);
        } else{
            musicas = musicaBlocoDBHelper.listarTodosAusentesBloco(getContext(), blocoRepertorio);
        }

        atualizaLista();
    }

    private class SalvarEntidade extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(getContext(), "Salvando alterações", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            int tamanhoLista = musicas.size();

            for(int i = 0; i < tamanhoLista; i++){
                Musica musica = musicas.get(i);

                if(musica.isChecked()){
                    MusicaBloco musicaBloco = new MusicaBloco();
                    musicaBloco.setMusica(musicas.get(i));
                    musicaBloco.setBlocoRepertorio(blocoRepertorio);

                    musicaBloco = musicaBlocoDBHelper.carregarPorMusicaEBloco(getContext(), musicaBloco);

                    if(musicaBloco == null){
                        musicaBloco = new MusicaBloco();
                        musicaBloco.setMusica(musicas.get(i));
                        musicaBloco.setBlocoRepertorio(blocoRepertorio);
                    }

                    musicaBloco.setStatus(0);
                    musicaBloco.setUltimaAlteracao(Calendar.getInstance());
                    musicaBloco.setDataCadastro(Calendar.getInstance());
                    musicaBloco.setEnviado(-1);

                    int ordem = corrigeOrdemMusicas();

                    musicaBloco.setOrdem(ordem);

                    musicaBlocoDBHelper.salvarOuAtualizar(getContext(), musicaBloco);

                }

            }

            return "";

        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            Toast.makeText(dialog.getContext(), "Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
            listener.onModalAdicionaDismissed(0);
            dialog.dismiss();
            dialogLoad.dismiss();

        }
    }
}
