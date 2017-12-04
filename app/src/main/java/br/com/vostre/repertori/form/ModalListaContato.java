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
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ContatoCasaList;
import br.com.vostre.repertori.adapter.ContatoList;
import br.com.vostre.repertori.adapter.RepertorioList;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.ContatoCasa;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.dao.ContatoCasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;

public class ModalListaContato extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button btnFechar;
    ListView listViewContatos;
    List<Contato> contatos;
    ContatoCasaDBHelper contatoCasaDBHelper;
    Casa casa;

    ModalAdicionaListener listener;
    ContatoList adapter;

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contatoCasaDBHelper = new ContatoCasaDBHelper(getContext());

        View view = inflater.inflate(R.layout.modal_lista_contato, container, false);

        view.setMinimumWidth(700);

        listViewContatos = (ListView) view.findViewById(R.id.listViewContatos);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnFechar.setOnClickListener(this);

        contatos = contatoCasaDBHelper.listarTodosAusentesCasa(getContext(), casa);

        adapter = new ContatoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, contatos);
        listViewContatos.setAdapter(adapter);
        listViewContatos.setOnItemClickListener(this);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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
        Contato contato = contatos.get(position);
        ContatoCasa contatoCasa = new ContatoCasa();

        contatoCasa.setContato(contato);
        contatoCasa.setCasa(casa);
        contatoCasa.setStatus(0);
        contatoCasa.setEnviado(-1);
        contatoCasa.setUltimaAlteracao(Calendar.getInstance());
        contatoCasaDBHelper.salvarOuAtualizar(getContext(), contatoCasa);
        listener.onModalAdicionaDismissed(0);
        dismiss();
    }

}
