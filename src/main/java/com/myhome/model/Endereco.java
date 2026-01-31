package com.myhome.model;

public class Endereco {
    private String rua;
    private String cidade;
    private String estado;
    private String cep;

    public Endereco(String rua, String cidade, String estado, String cep) {
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Endereco clone() {
        Endereco enderecoClone = new Endereco(this.rua, this.cidade, this.estado, this.cep);
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

    public String getCep() {
        return cep;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s - CEP: %s", rua, cidade, estado, cep);
    }
}
