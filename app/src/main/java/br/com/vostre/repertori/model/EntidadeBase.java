package br.com.vostre.repertori.model;

import java.util.Calendar;

/**
 * Created by Almir on 30/12/2016.
 */

public class EntidadeBase {

    private String id;
    private Integer status;
    private Integer enviado;
    private Calendar dataCadastro;
    private Calendar dataRecebimento;
    private Calendar ultimaAlteracao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Calendar getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Calendar dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Calendar getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Calendar dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public Calendar getUltimaAlteracao() {
        return ultimaAlteracao;
    }

    public void setUltimaAlteracao(Calendar ultimaAlteracao) {
        this.ultimaAlteracao = ultimaAlteracao;
    }

    public Integer getEnviado() {
        return enviado;
    }

    public void setEnviado(Integer enviado) {
        this.enviado = enviado;
    }

}
