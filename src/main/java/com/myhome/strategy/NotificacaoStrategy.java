package com.myhome.strategy;

import com.myhome.model.Usuario;

public interface NotificacaoStrategy {

    void notificar(Usuario usuario, String mensagem);
}