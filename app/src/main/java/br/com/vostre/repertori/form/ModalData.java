package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.listener.ModalDataListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;

public class ModalData extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button btnSalvar;
    Button btnFechar;
    DatePicker datePicker;

    ModalDataListener listener;
    Calendar dataSelecionada = Calendar.getInstance();
    Calendar data;

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_data, container, false);

        view.setMinimumWidth(700);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();

        if(data != null){
            calendar = data;
        }

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                dataSelecionada.set(Calendar.YEAR, year);
                dataSelecionada.set(Calendar.MONTH, month);
                dataSelecionada.set(Calendar.DAY_OF_MONTH, day);
            }
        });

        setRetainInstance(true);

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
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public ModalDataListener getListener() {
        return listener;
    }

    public void setListener(ModalDataListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnSalvar:

                Calendar calendar = Calendar.getInstance();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    calendar.setTime(new Date(datePicker.getCalendarView().getDate()));
                } else {
                    calendar = dataSelecionada;
                }

                if(listener != null){
                    listener.onModalDataDismissed(calendar);
                }

                dismiss();
                break;
            case R.id.btnFechar:

                //if(listener != null){
                    listener.onModalDataDismissed(null);
                //}

                dismiss();
                break;
        }

    }
}
