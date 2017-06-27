package br.com.vostre.repertori.utils;

/**
 * Created by Almir on 03/03/2016.
 */
public class Constants {

    public static final String IP = "172.16.0.37";
//    public static final String IP = "10.0.0.101";
//    public static final String SERVIDOR = "http://192.168.42.178/ktl/web/app_dev.php/";
//    public static final String SERVIDOR_IMAGEM = "http://192.168.42.178/ktl/web/";
    public static final String SERVIDOR = "http://"+IP+"/rep2/web/app_dev.php";
    public static final String URLSERVIDOR = SERVIDOR+"/api/recebe-dados/";
    public static final String URLSERVIDORENVIO = SERVIDOR+"/api/envia-dados/";
    public static final String SERVIDOR_TESTE = "vostre.com.br";
    public static final int PORTA_SERVIDOR = 80;
    public static final String URLTOKEN = SERVIDOR+"/api/token";
    public static final String URLTOKENGOOGLE = SERVIDOR+"/api/valida-token";

    public static final int TEMPO_ATUALIZACAO = 5;

}
