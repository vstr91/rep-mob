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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;

public class ModalEditaLetra extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Button btnSalvar;
    Button btnFechar;
    EditText editTextLetra;
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

        View view = inflater.inflate(R.layout.modal_edita_letra, container, false);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        view.setMinimumWidth(width);

        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        textViewMusica = (TextView) view.findViewById(R.id.textViewMusica);
        editTextLetra = (EditText) view.findViewById(R.id.editTextLetra);

        textViewMusica.setText(musica.getNome());
        editTextLetra.setText(musica.getLetra());

        btnFechar.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);

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
            case R.id.btnSalvar:
                musica.setLetra(editTextLetra.getText().toString());
                musica.setEnviado(-1);
                MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getContext());
                musicaDBHelper.salvarOuAtualizar(getContext(), musica);
                Toast.makeText(getContext(), "Letra Cadastrada!", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }
}
