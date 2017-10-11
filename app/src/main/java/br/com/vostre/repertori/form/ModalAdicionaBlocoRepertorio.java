package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaAdicionaList;
import br.com.vostre.repertori.listener.FiltroMusicaListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class ModalAdicionaBlocoRepertorio extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button btnSalvar;
    Button btnFechar;
    EditText editTextNome;
    BlocoRepertorioDBHelper blocoRepertorioDBHelper;

    Repertorio repertorio;

    ModalAdicionaListener listener;

    Dialog dialog;

    BlocoRepertorio blocoRepertorio;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(getContext());

        View view = inflater.inflate(R.layout.modal_cadastro_bloco_repertorio, container, false);

        view.setMinimumWidth(700);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);

        if(getBlocoRepertorio() != null){
            editTextNome.setText(getBlocoRepertorio().getNome());
        }

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public Repertorio getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Repertorio repertorio) {
        this.repertorio = repertorio;
    }

    public ModalAdicionaListener getListener() {
        return listener;
    }

    public void setListener(ModalAdicionaListener listener) {
        this.listener = listener;
    }

    public BlocoRepertorio getBlocoRepertorio() {
        return blocoRepertorio;
    }

    public void setBlocoRepertorio(BlocoRepertorio blocoRepertorio) {
        this.blocoRepertorio = blocoRepertorio;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnSalvar:

                String nome = editTextNome.getText().toString();

                if(nome.isEmpty()){
                    Toast.makeText(getContext(), "Por favor informe todos os dados", Toast.LENGTH_SHORT).show();
                } else{

                    if(blocoRepertorio != null){
                        blocoRepertorio.setNome(nome);
                        blocoRepertorio.setUltimaAlteracao(Calendar.getInstance());
                        blocoRepertorio.setEnviado(-1);
                        blocoRepertorio.setRepertorio(repertorio);
                        blocoRepertorio.setStatus(0);

                    } else{
                        blocoRepertorio = new BlocoRepertorio();
                        blocoRepertorio.setNome(nome);
                        blocoRepertorio.setRepertorio(repertorio);
                        blocoRepertorio.setDataCadastro(Calendar.getInstance());
                        blocoRepertorio.setUltimaAlteracao(Calendar.getInstance());
                        blocoRepertorio.setEnviado(-1);
                        blocoRepertorio.setStatus(0);
                    }

                    int ordem = corrigeOrdemBlocos();

                    blocoRepertorio.setOrdem(ordem);

                    if(blocoRepertorioDBHelper.jaExiste(getContext(), blocoRepertorio)){
                        Toast.makeText(getContext(), "O registro informado já existe!", Toast.LENGTH_SHORT).show();
                    } else{
                        blocoRepertorioDBHelper.salvarOuAtualizar(getContext(), blocoRepertorio);
                        SnackbarHelper.notifica(getView(), "Cadastrado com Sucesso!", Snackbar.LENGTH_LONG);
                        listener.onModalAdicionaDismissed(2);
                        dismiss();
                    }

                }

                break;
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxAdicionar);
//        checkBox.setChecked(!checkBox.isChecked());

    }

    private int corrigeOrdemBlocos(){

        List<BlocoRepertorio> blocosRepertorios = blocoRepertorioDBHelper.corrigirOrdemPorRepertorio(getContext(), repertorio);

        int qtdBlocos = blocosRepertorios.size();

        for(int i = 0; i < qtdBlocos; i++){
            BlocoRepertorio blocoRepertorio = blocosRepertorios.get(i);
            blocoRepertorio.setOrdem(i+1);
            blocoRepertorio.setEnviado(-1);
            blocoRepertorio.setUltimaAlteracao(Calendar.getInstance());

            blocoRepertorioDBHelper.salvarOuAtualizar(getContext(), blocoRepertorio);

        }

        return qtdBlocos+1;

    }
}
