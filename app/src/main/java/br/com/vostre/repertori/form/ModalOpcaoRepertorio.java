package br.com.vostre.repertori.form;

import android.app.Activity;
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
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.ModalEventoListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class ModalOpcaoRepertorio extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button btnFechar;
    Button btnNovoRepertorio;
    Button btnNovaMusica;
    ModalAdicionaListener listenerAdiciona;
    ModalCadastroListener listenerRepertorio;

    Projeto projeto;

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public ModalAdicionaListener getListenerAdiciona() {
        return listenerAdiciona;
    }

    public void setListenerAdiciona(ModalAdicionaListener listenerAdiciona) {
        this.listenerAdiciona = listenerAdiciona;
    }

    public ModalCadastroListener getListenerRepertorio() {
        return listenerRepertorio;
    }

    public void setListenerRepertorio(ModalCadastroListener listenerRepertorio) {
        this.listenerRepertorio = listenerRepertorio;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArtistaDBHelper artistaDBHelper;

        View view = inflater.inflate(R.layout.modal_opcao_repertorio, container, false);

        view.setMinimumWidth(700);

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnNovaMusica = (Button) view.findViewById(R.id.btnNovaMusica);
        btnNovoRepertorio = (Button) view.findViewById(R.id.btnNovoRepertorio);

        btnFechar.setOnClickListener(this);
        btnNovaMusica.setOnClickListener(this);
        btnNovoRepertorio.setOnClickListener(this);

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
            case R.id.btnNovoRepertorio:
                ModalCadastroRepertorio modalCadastroRepertorio = new ModalCadastroRepertorio();
                modalCadastroRepertorio.setListener(listenerRepertorio);
                modalCadastroRepertorio.setProjeto(projeto);

                modalCadastroRepertorio.show(getFragmentManager(), "modalMusicaProjeto");
                dismiss();
                break;
            case R.id.btnNovaMusica:
                ModalAdicionaMusicaProjeto modalAdicionaoMusicaProjeto = new ModalAdicionaMusicaProjeto();
                modalAdicionaoMusicaProjeto.setListener(listenerAdiciona);
                modalAdicionaoMusicaProjeto.setProjeto(projeto);

                modalAdicionaoMusicaProjeto.show(getFragmentManager(), "modalMusicaProjeto");
                dismiss();
                break;
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }

}
