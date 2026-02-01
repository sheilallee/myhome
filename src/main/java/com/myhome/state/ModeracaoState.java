package com.myhome.state;

import com.myhome.chain.ModeradorBase;
import com.myhome.chain.ValidadorPalavras;
import com.myhome.chain.ValidadorPreco;
import com.myhome.model.Anuncio;

public class ModeracaoState extends AnuncioState {

    public ModeracaoState(Anuncio anuncio) {
        super(anuncio);
        this.nome = "Moderação";
    }

    @Override
    public void aprovar() {
        ModeradorBase validador = new ValidadorPalavras();
        validador.setNext(new ValidadorPreco());

        if (validador.handle(anuncio)) {
            System.out.println("Anúncio aprovado na moderação. Movendo para estado Ativo.");
            anuncio.mudarEstado(new AtivoState(anuncio));
        } else {
            System.out.println("Anúncio reprovado na moderação. Movendo para estado Suspenso.");
            anuncio.mudarEstado(new SuspensoState(anuncio));
        }
    }

    @Override
    public void suspender() {
        System.out.println("Anúncio suspenso a partir do estado Moderação.");
        anuncio.mudarEstado(new SuspensoState(this.anuncio));
    }

    @Override
    public void publicar() throws IllegalStateException {
        super.throwError("Moderação");
    }

    @Override
    public void vender() throws IllegalStateException {
        super.throwError("Moderação");
    }

    @Override
    public void revisar() throws IllegalStateException {
        super.throwError("Moderação");
    }
}
