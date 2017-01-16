package br.com.vostre.repertori.model;

/**
 * Created by Almir on 15/01/2017.
 */

public class StatusMusica {

    private int id;
    private String texto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.getTexto();
    }
}
