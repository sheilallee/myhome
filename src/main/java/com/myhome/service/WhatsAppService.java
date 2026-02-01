package com.myhome.service;

/**
 * Serviço de envio de WhatsApp (mock simples).
 */
public class WhatsAppService {

    /**
     * Envia uma mensagem pelo WhatsApp para o telefone especificado.
     *
     * @param telefone o número de telefone/WhatsApp do destinatário
     * @param mensagem a mensagem a ser enviada
     */
    public void enviar(String telefone, String mensagem) {
        System.out.println("[WhatsApp] Enviando mensagem para: " + telefone);
        System.out.println("[WhatsApp] Mensagem: " + mensagem);
        System.out.println("[WhatsApp] Mensagem enviada com sucesso!");
    }
}
