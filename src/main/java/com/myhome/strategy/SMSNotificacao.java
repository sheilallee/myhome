package com.myhome.strategy;

import com.myhome.model.Usuario;
import com.myhome.service.SMSService;

/**
 * Strategy de notificação por SMS (RF05).
 */
public class SMSNotificacao implements NotificacaoStrategy {

    private final SMSService smsService;

    public SMSNotificacao(SMSService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void notificar(Usuario usuario, String mensagem) {
        smsService.enviar(usuario.getTelefone(), mensagem);
    }
}
