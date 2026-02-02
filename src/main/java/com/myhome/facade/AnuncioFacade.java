package com.myhome.facade;

import com.myhome.model.Anuncio;

/**
 * FACADE - GERENCIAMENTO DO CICLO DE VIDA DO ANÚNCIO
 */
public class AnuncioFacade {

    public void enviarParaModeracao(Anuncio anuncio) {
        anuncio.getState().publicar();
    }

    public void reativar(Anuncio anuncio) {
        anuncio.getState().revisar();
    }

    public void aprovar(Anuncio anuncio) {
        anuncio.getState().aprovar();
    }

    public void reprovar(Anuncio anuncio) {
        anuncio.getState().suspender();
    }

    public void vender(Anuncio anuncio) {
        anuncio.getState().vender();
    }

    public void suspender(Anuncio anuncio) {
        anuncio.getState().suspender();
    }
}