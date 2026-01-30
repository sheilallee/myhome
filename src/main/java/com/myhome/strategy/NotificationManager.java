package com.myhome.strategy;

import com.myhome.model.Usuario;

public class NotificationManager {

    public void enviarNotificacao(Usuario usuario, String mensagem) {
        if (usuario.getCanalNotificacao() != null) {
            usuario.getCanalNotificacao().notificar(usuario, mensagem);
        }
    }
}