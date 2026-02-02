package com.myhome.state;

import com.myhome.model.Anuncio;

public class RascunhoState extends AnuncioState {

    public RascunhoState(Anuncio anuncio) {
        super(anuncio);
        this.nome = "Rascunho";
    }

    @Override
    public void publicar() {
        if (!anuncio.getImovel().validar()) {
            throw new IllegalStateException(
                "Imóvel inválido. Não é possível publicar o anúncio."
            );
        }
        anuncio.setState(new ModeracaoState(this.anuncio));
    }

    @Override
    public void aprovar() throws IllegalStateException {
        super.throwError("Rascunho");
    }

    @Override
    public void suspender() {
        System.out.println("Anúncio suspenso a partir do estado Rascunho.");
        anuncio.setState(new SuspensoState(this.anuncio));
    }

    @Override
    public void vender() throws IllegalStateException {
        super.throwError("Rascunho");
    }

    @Override
    public void revisar() throws IllegalStateException {
        super.throwError("Rascunho");
    }
        

}
