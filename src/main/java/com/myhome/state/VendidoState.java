package com.myhome.state;

import com.myhome.model.Anuncio;

public class VendidoState extends AnuncioState {

    public VendidoState(Anuncio anuncio) {
        super(anuncio);
        this.nome = "Vendido";
    }

    @Override
    public void publicar() {
        super.throwError("Vendido");
    }

    @Override
    public void aprovar() {
        super.throwError("Vendido");
    }

    @Override
    public void suspender() {
        super.throwError("Vendido");
    }

    @Override
    public void vender() {
        super.throwError("Vendido");
    }

    @Override
    public void revisar() {
        super.throwError("Vendido");
    }

    

}
