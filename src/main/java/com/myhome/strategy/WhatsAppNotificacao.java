package com.myhome.strategy;

import com.myhome.model.Usuario;
import com.myhome.service.WhatsAppService;

/**
 * Strategy de notificação por WhatsApp (RF05).
 */
public class WhatsAppNotificacao implements NotificacaoStrategy {

    private final WhatsAppService whatsAppService;

    public WhatsAppNotificacao(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @Override
    public void notificar(Usuario usuario, String mensagem) {
        whatsAppService.enviar(usuario.getTelefone(), mensagem);
    }
}
