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

import br.com.vostre.repertori.BlocoRepertorioDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.BlocoRepertorioAdapter;
import br.com.vostre.repertori.adapter.RepertorioList;
import br.com.vostre.repertori.listener.FiltroMusicaListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaBlocoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.DialogUtils;

public class ModalListaBloco extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button btnFechar;
    ListView listViewBlocos;
    List<BlocoRepertorio> blocos;
    BlocoRepertorioDBHelper blocoRepertorioDBHelper;

    Evento evento;
    Repertorio repertorio;

    ModalAdicionaListener listener;
    BlocoRepertorioAdapter adapterBloco;

    MusicaEventoDBHelper musicaEventoDBHelper;
    Dialog dialogLoad;

    public Repertorio getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Repertorio repertorio) {
        this.repertorio = repertorio;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(getContext());

        View view = inflater.inflate(R.layout.modal_lista_bloco, container, false);

        view.setMinimumWidth(700);

        listViewBlocos = (ListView) view.findViewById(R.id.listViewBlocos);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnFechar.setOnClickListener(this);

        blocos = blocoRepertorioDBHelper.listarTodosPorRepertorio(getContext(), repertorio, 0);

        adapterBloco = new BlocoRepertorioAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, blocos);
        listViewBlocos.setAdapter(adapterBloco);
        listViewBlocos.setOnItemClickListener(this);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
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
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CadastraMusica cadastraMusica = new CadastraMusica();
        cadastraMusica.setPosicao(position);
        cadastraMusica.execute();
    }

    private int corrigeOrdemMusicas(){

        List<MusicaEvento> musicas = musicaEventoDBHelper.corrigirOrdemPorEvento(getContext(), evento);

        int qtdMusicas = musicas.size();

        for(int i = 0; i < qtdMusicas; i++){
            MusicaEvento musicaEvento = musicas.get(i);
            musicaEvento.setOrdem(i+1);
            musicaEvento.setEnviado(-1);
            musicaEvento.setUltimaAlteracao(Calendar.getInstance());

            musicaEventoDBHelper.salvarOuAtualizar(getContext(), musicaEvento);

        }

        return qtdMusicas+1;

    }

    private class CadastraMusica extends AsyncTask<Void, String, String> {

        int posicao = -1;

        public int getPosicao() {
            return posicao;
        }

        public void setPosicao(int posicao) {
            this.posicao = posicao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(getContext(), "Salvando alterações", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            BlocoRepertorio blocoRepertorio = blocos.get(getPosicao());
            //Toast.makeText(getContext(), "Adicionando "+blocoRepertorio.getNome()+" ao evento "+evento.getNome(), Toast.LENGTH_LONG).show();

            MusicaBlocoDBHelper musicaBlocoDBHelper = new MusicaBlocoDBHelper(getContext());
            musicaEventoDBHelper = new MusicaEventoDBHelper(getContext());

            List<Musica> musicas = musicaBlocoDBHelper.listarTodosPorBloco(getContext(), blocoRepertorio, 0);

            for(Musica m : musicas){

                MusicaEvento musicaEvento = new MusicaEvento();
                musicaEvento.setMusica(m);
                musicaEvento.setEvento(evento);

                musicaEvento = musicaEventoDBHelper.carregarPorMusicaEEvento(getContext(), musicaEvento);

                if(musicaEvento == null){
                    musicaEvento = new MusicaEvento();
                    musicaEvento.setMusica(m);
                    musicaEvento.setEvento(evento);
                }

                musicaEvento.setStatus(0);
                musicaEvento.setUltimaAlteracao(Calendar.getInstance());
                musicaEvento.setDataCadastro(Calendar.getInstance());
                musicaEvento.setEnviado(-1);

                int ordem = corrigeOrdemMusicas();

                musicaEvento.setOrdem(ordem);

                musicaEventoDBHelper.salvarOuAtualizar(getContext(), musicaEvento);

            }

            return "";

        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            dialogLoad.dismiss();
            listener.onModalAdicionaDismissed(0);
            dismiss();
        }
    }

}
