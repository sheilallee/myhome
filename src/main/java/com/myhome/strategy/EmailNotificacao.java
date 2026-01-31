package com.myhome.strategy;

import com.myhome.model.Usuario;
import com.myhome.service.EmailService;

/**
 * Strategy de notificação por Email (RF05).
 */
public class EmailNotificacao implements NotificacaoStrategy {

    private final EmailService emailService;

    public EmailNotificacao(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notificar(Usuario usuario, String mensagem) {
        emailService.enviar(usuario.getEmail(), mensagem);
    }
}
