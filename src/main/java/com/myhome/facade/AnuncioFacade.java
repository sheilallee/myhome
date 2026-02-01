package com.myhome.facade;

import com.myhome.model.Anuncio;

/**
 * FACADE - GERENCIAMENTO DO CICLO DE VIDA DO ANÚNCIO
 */
public class AnuncioFacade {

    public void enviarParaModeracao(Anuncio anuncio) {
        anuncio.getEstado().publicar();
    }

    public void reativar(Anuncio anuncio) {
        anuncio.getEstado().revisar();
    }

    public void aprovar(Anuncio anuncio) {
        anuncio.getEstado().aprovar();
    }

    public void reprovar(Anuncio anuncio) {
        anuncio.getEstado().suspender();
    }

    public void vender(Anuncio anuncio) {
        anuncio.getEstado().vender();
    }

    public void suspender(Anuncio anuncio) {
        anuncio.getEstado().suspender();
    }
}