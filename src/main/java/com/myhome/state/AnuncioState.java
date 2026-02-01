package com.myhome.state;

import com.myhome.model.Anuncio;

public abstract class AnuncioState {
    protected Anuncio anuncio;

    protected String nome;

    public AnuncioState(Anuncio anuncio) {
        this.anuncio = anuncio;
    }
    
    public abstract void publicar();
    public abstract void aprovar();
    public abstract void vender();
    public abstract void revisar();
    public abstract void suspender();

    protected void throwError(String estado) {
        throw new IllegalStateException(
            "Operação inválida no estado " + estado + "."
        );
    }

    public String getNome() {
        return nome;
    }

}
