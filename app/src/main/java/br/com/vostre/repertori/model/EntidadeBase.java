package br.com.vostre.repertori.model;

import java.util.Calendar;

/**
 * Created by Almir on 30/12/2016.
 */

public class EntidadeBase {

    private Integer id;
    private Integer idRemoto;
    private Integer status;
    private Calendar dataCadastro;
    private Calendar dataRecebimento;
    private Calendar ultimaAlteracao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getIdRemoto() {
        return idRemoto;
    }

    public void setIdRemoto(Integer idRemoto) {
        this.idRemoto = idRemoto;
    }
}
