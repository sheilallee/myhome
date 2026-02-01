package com.myhome.state;

import com.myhome.model.Anuncio;

public class AtivoState extends AnuncioState {

    public AtivoState(Anuncio anuncio) {
        super(anuncio);
        this.nome = "Ativo";
    }

    @Override
    public void publicar() {
        super.throwError("Ativo");
    }

    @Override
    public void aprovar() {
        super.throwError("Ativo");
    }

    @Override
    public void suspender() {
        System.out.println("Anúncio suspenso no estado Ativo.");
        anuncio.mudarEstado(new SuspensoState(this.anuncio));
    }

    @Override
    public void vender() {
        System.out.println("Anúncio vendido.");
        anuncio.mudarEstado(new VendidoState(this.anuncio));
    }

    @Override
    public void revisar() {
        super.throwError("Ativo");
    }

}
