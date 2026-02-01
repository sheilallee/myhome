package com.myhome.service;

/**
 * Serviço de envio de emails (mock simples).
 */
public class EmailService {

    /**
     * Envia um email para o endereço especificado com a mensagem fornecida.
     *
     * @param destinatario o endereço de email do destinatário
     * @param mensagem a mensagem a ser enviada
     */
    public void enviar(String destinatario, String mensagem) {
        System.out.println("[EMAIL] Enviando email para: " + destinatario);
        System.out.println("[EMAIL] Mensagem: " + mensagem);
        System.out.println("[EMAIL] Email enviado com sucesso!");
    }
}
