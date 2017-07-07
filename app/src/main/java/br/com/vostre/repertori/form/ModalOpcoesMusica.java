package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.ModalEventoListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class ModalOpcoesMusica extends android.support.v4.app.DialogFragment implements View.OnClickListener, ModalCadastroListener {

    Button btnFechar;
    Button btnEditar;
    Button btnAdicionarProjeto;
    Musica musica;

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_opcoes_musica, container, false);

        view.setMinimumWidth(700);

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnEditar = (Button) view.findViewById(R.id.btnEditar);
        btnAdicionarProjeto = (Button) view.findViewById(R.id.btnAdicionarProjeto);

        btnFechar.setOnClickListener(this);
        btnAdicionarProjeto.setOnClickListener(this);
        btnEditar.setOnClickListener(this);

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
            case R.id.btnEditar:
                ModalCadastroMusica modalCadastroMusica = new ModalCadastroMusica();
                modalCadastroMusica.setListener(this);
                modalCadastroMusica.setMusica(musica);

                modalCadastroMusica.show(this.getFragmentManager(), "modalMusica");
                dismiss();
                break;
            case R.id.btnAdicionarProjeto:
                ModalMusicaProjeto modalMusicaProjeto = new ModalMusicaProjeto();
                modalMusicaProjeto.setMusica(musica);

                modalMusicaProjeto.show(this.getFragmentManager(), "modalMusicaProjeto");
                dismiss();
                break;
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {

    }
}
