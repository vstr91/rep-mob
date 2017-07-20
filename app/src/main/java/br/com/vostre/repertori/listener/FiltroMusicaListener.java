package br.com.vostre.repertori.listener;

import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 30/11/2015.
 */
public interface FiltroMusicaListener {

    public void onFiltroMusicaDismissed(Estilo estilo, Artista artista);

}
