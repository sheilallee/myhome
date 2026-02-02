package com.myhome.state;

import com.myhome.model.Anuncio;
import com.myhome.service.ChainValidationService;

public class ModeracaoState extends AnuncioState {

    public ModeracaoState(Anuncio anuncio) {
        super(anuncio);
        this.nome = "Moderação";
    }

    @Override
    public void aprovar() {
        // Usar ChainValidationService para validar (Chain of Responsibility Pattern)
        ChainValidationService validationService = new ChainValidationService();
        
        if (validationService.validarAnuncio(this.anuncio)) {
            System.out.println("Anúncio aprovado na moderação. Movendo para estado Ativo.");
            this.anuncio.setState(new AtivoState(this.anuncio));
        } else {
            System.out.println("Anúncio reprovado na moderação. Movendo para estado Suspenso.");
            this.anuncio.setState(new SuspensoState(this.anuncio));
        }
    }

    @Override
    public void suspender() {
        System.out.println("Anúncio suspenso a partir do estado Moderação.");
        this.anuncio.setState(new SuspensoState(this.anuncio));
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
