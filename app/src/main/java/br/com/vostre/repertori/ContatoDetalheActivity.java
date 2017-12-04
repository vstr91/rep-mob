package br.com.vostre.repertori;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;

public class ContatoDetalheActivity extends BaseActivity {

    TextView textViewNome;
    TextView textViewTelefone;
    TextView textViewEmail;
    TextView textViewObservacao;
    Button btnLigar;

    Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_detalhe);

        ContatoDBHelper contatoDBHelper = new ContatoDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewTelefone = (TextView) findViewById(R.id.textViewTelefone);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewObservacao = (TextView) findViewById(R.id.textViewObservacao);
        btnLigar = (Button) findViewById(R.id.btnLigar);

        contato = new Contato();
        contato.setId(getIntent().getStringExtra("contato"));
        contato = contatoDBHelper.carregar(getApplicationContext(), contato);

        textViewNome.setText(contato.getNome());
        textViewTelefone.setText(contato.getTelefone());
        textViewEmail.setText(contato.getEmail());
        textViewObservacao.setText(contato.getObservacao());

        btnLigar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLigar:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", textViewTelefone.getText().toString(), null));
                startActivity(intent);
                break;
        }
    }
}
