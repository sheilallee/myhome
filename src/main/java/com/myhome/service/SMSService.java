package com.myhome.service;

/**
 * Serviço de envio de SMS (mock simples).
 */
public class SMSService {

    /**
     * Envia um SMS para o telefone especificado com a mensagem fornecida.
     *
     * @param telefone o número de telefone do destinatário
     * @param mensagem a mensagem a ser enviada
     */
    public void enviar(String telefone, String mensagem) {
        System.out.println("[SMS] Enviando SMS para: " + telefone);
        System.out.println("[SMS] Mensagem: " + mensagem);
        System.out.println("[SMS] SMS enviado com sucesso!");
    }
}
