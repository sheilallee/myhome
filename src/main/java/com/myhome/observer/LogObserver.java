package com.myhome.observer;

import com.myhome.model.Anuncio;
import com.myhome.state.AnuncioState;
import com.myhome.service.LoggerService;

/**
 * Observer responsável por registrar log
 * sempre que o estado do anúncio muda.
 */
public class LogObserver implements AnuncioObserver {

    private final LoggerService logger;

    public LogObserver(LoggerService logger) {
        this.logger = logger;
    }

    @Override
    public void onEstadoAlterado(
            Anuncio anuncio,
            AnuncioState antigo,
            AnuncioState novo) {

        String tituloAnuncio = anuncio.getTitulo() != null ? 
            anuncio.getTitulo() : "Anúncio";
        
        logger.info(
            "[LOG] Anúncio '" + tituloAnuncio + "'" +
            " mudou de " + antigo.getNome() +
            " para " + novo.getNome()
        );
    }
}
