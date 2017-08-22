package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;

public class ModalCifra extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button btnFechar;
    Button btnEditar;
    TextView textViewCifra;
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

        View view = inflater.inflate(R.layout.modal_cifra, container, false);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        view.setMinimumWidth(width);

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnEditar = (Button) view.findViewById(R.id.btnEditar);
        textViewMusica = (TextView) view.findViewById(R.id.textViewMusica);
        textViewCifra = (TextView) view.findViewById(R.id.textViewCifra);

        textViewMusica.setText(musica.getNome());

        if(!musica.getCifra().equals("null") && !musica.getCifra().isEmpty()){
            textViewCifra.setText(musica.getCifra());
        } else{
            textViewCifra.setText("Cifra não cadastrada.");
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
                ModalEditaCifra modalEditaCifra = new ModalEditaCifra();
                modalEditaCifra.setMusica(musica);

                modalEditaCifra.show(getFragmentManager(), "modalEditaCifra");
                break;
        }

    }
}
