package com.myhome.observer;

import com.myhome.model.Anuncio;
import com.myhome.state.AnuncioState;

public interface AnuncioObserver {

    void onEstadoAlterado(
            Anuncio anuncio,
            AnuncioState estadoAntigo,
            AnuncioState estadoNovo
    );
}