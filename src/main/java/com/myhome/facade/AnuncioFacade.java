package com.myhome.facade;

import com.myhome.model.Anuncio;

/**
 * FACADE - GERENCIAMENTO DO CICLO DE VIDA DO ANÚNCIO
 */
public class AnuncioFacade {

    public void enviarParaModeracao(Anuncio anuncio) {
        anuncio.getEstado().enviarParaModeracao(anuncio);
    }

    public void aprovar(Anuncio anuncio) {
        anuncio.getEstado().aprovar(anuncio);
    }

    public void reprovar(Anuncio anuncio) {
        anuncio.getEstado().reprovar(anuncio);
    }

    public void vender(Anuncio anuncio) {
        anuncio.getEstado().vender(anuncio);
    }

    public void suspender(Anuncio anuncio) {
        anuncio.getEstado().suspender(anuncio);
    }
}