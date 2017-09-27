package br.com.vostre.repertori.listener;

import java.util.Map;

/**
 * Created by Almir on 04/01/2015.
 */
public interface TarefaAssincronaListener {

    public void onTarefaAssincronaResultSucceeded(Map<String, Object> result, int acao);

}
