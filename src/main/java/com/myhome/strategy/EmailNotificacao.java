package com.myhome.strategy;

import com.myhome.model.Usuario;

public class EmailNotificacao implements NotificacaoStrategy {

    @Override
    public void notificar(Usuario usuario, String mensagem) {
        System.out.println(
            "[EMAIL] " + usuario.getEmail() + " | " + mensagem
        );
    }
}