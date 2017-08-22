package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.fragment.LetraFragment;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;

public class ModalLetra extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button btnFechar;
    Button btnEditar;
    TextView textViewLetra;
    TextView textViewMusica;
    Musica musica;

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_letra, container, false);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        view.setMinimumWidth(width);

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnEditar = (Button) view.findViewById(R.id.btnEditar);
        textViewMusica = (TextView) view.findViewById(R.id.textViewMusica);
        textViewLetra = (TextView) view.findViewById(R.id.textViewLetra);

        textViewMusica.setText(musica.getNome());

        if(!musica.getLetra().equals("null") && !musica.getLetra().isEmpty()){
            textViewLetra.setText(musica.getLetra());
        } else{
            textViewLetra.setText("Letra não cadastrada.");
        }

        btnFechar.setOnClickListener(this);
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
            case R.id.btnFechar:
                dismiss();
                break;
            case R.id.btnEditar:
                dismiss();
                ModalEditaLetra modalEditaLetra = new ModalEditaLetra();
                modalEditaLetra.setMusica(musica);

                modalEditaLetra.show(getFragmentManager(), "modalEditaLetra");
                break;
        }

    }
}
