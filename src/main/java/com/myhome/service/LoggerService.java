package com.myhome.service;

/**
 * Serviço de infraestrutura responsável por registrar logs.
 * Centraliza o uso de System.out.println (E2).
 */
public class LoggerService {

    public void info(String mensagem) {
        System.out.println(mensagem);
    }
}
