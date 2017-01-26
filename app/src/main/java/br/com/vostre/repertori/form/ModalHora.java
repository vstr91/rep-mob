package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalHora extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button btnSalvar;
    Button btnFechar;
    TimePicker timePicker;

    ModalHoraListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArtistaDBHelper artistaDBHelper;

        View view = inflater.inflate(R.layout.modal_hora, container, false);

        view.setMinimumWidth(700);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);

        timePicker.setIs24HourView(true);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public ModalHoraListener getListener() {
        return listener;
    }

    public void setListener(ModalHoraListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnSalvar:

                String hora = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hora = timePicker.getHour()+":"+timePicker.getMinute();
                } else{
                    hora = timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute();
                }

                listener.onModalHoraDismissed(hora);
                dismiss();
                break;
            case R.id.btnFechar:
                listener.onModalHoraDismissed(null);
                dismiss();
                break;
        }

    }
}
