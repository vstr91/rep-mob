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
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.listener.ModalEventoListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class ModalEventos extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button btnFechar;
    Button btnNovo;
    ListView listViewEventos;
    List<Evento> eventos;
    ModalEventoListener listener;
    EventoList adapterEventos;
    TextView textViewData;
    Calendar data;

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public ModalEventoListener getListener() {
        return listener;
    }

    public void setListener(ModalEventoListener listener) {
        this.listener = listener;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArtistaDBHelper artistaDBHelper;

        View view = inflater.inflate(R.layout.modal_eventos, container, false);

        view.setMinimumWidth(700);

        listViewEventos = (ListView) view.findViewById(R.id.listViewEventos);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnNovo = (Button) view.findViewById(R.id.btnNovo);
        textViewData = (TextView) view.findViewById(R.id.textViewData);

        btnFechar.setOnClickListener(this);
        btnNovo.setOnClickListener(this);

        textViewData.setText(DataUtils.toString(getData(), false));

        if(eventos != null){
            adapterEventos = new EventoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, eventos);
            listViewEventos.setAdapter(adapterEventos);
            listViewEventos.setOnItemClickListener(this);
        }

        setRetainInstance(true);

        return view;

    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
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
            case R.id.btnNovo:
                listener.onModalEventoDismissed(1);
                dismiss();
                break;
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Evento evento = adapterEventos.getItem(position);

        Intent intent = new Intent(getContext(), EventoDetalheActivity.class);
        intent.putExtra("evento", evento.getId());
        startActivity(intent);
        dismiss();
    }
}
