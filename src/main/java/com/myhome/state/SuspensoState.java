package com.myhome.state;

import com.myhome.model.Anuncio;

public class SuspensoState extends AnuncioState {

    public SuspensoState(Anuncio anuncio) {
        super(anuncio);
        this.nome = "Suspenso";
    }

    @Override
    public void publicar() {
        super.throwError("Suspenso");
    }

    @Override
    public void aprovar() {
        super.throwError("Suspenso");
    }

    @Override
    public void suspender() {
        super.throwError("Suspenso");
    }

    @Override
    public void vender() {
        super.throwError("Suspenso");
    }

    @Override
    public void revisar() {
        System.out.println("Anúncio revisado e retornando ao estado Rascunho.");
        anuncio.mudarEstado(new RascunhoState(this.anuncio));
    }

}
