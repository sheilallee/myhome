package com.myhome.observer;

import com.myhome.model.Anuncio;
import com.myhome.state.AnuncioState;
import com.myhome.strategy.NotificationManager;

/**
 * Observer responsável por notificar o anunciante
 * sempre que o estado do anúncio muda.
 */
public class NotificationObserver implements AnuncioObserver {

    private final NotificationManager manager;

    public NotificationObserver(NotificationManager manager) {
        this.manager = manager;
    }

    @Override
    public void onEstadoAlterado(
            Anuncio anuncio,
            AnuncioState antigo,
            AnuncioState novo) {

        if (anuncio.getAnunciante() == null) {
            return;
        }

        String msg = "Seu anúncio mudou de " +
                antigo.getNome() + " para " +
                novo.getNome();

        manager.enviarNotificacao(anuncio.getAnunciante(), msg);
    }
}
