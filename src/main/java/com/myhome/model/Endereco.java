package com.myhome.model;

public class Endereco {
    private String rua;
    private String numero;
    private String cidade;
    private String estado;

    public Endereco(String rua, String numero, String cidade, String estado) {
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Endereco clone() {
        Endereco enderecoClone = new Endereco(this.rua, this.numero, this.cidade, this.estado);
        return enderecoClone;
    }

    public String getRua() {
        return rua;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return String.format("%s, %s - %s/%s", rua, numero, cidade, estado);
    }
}
