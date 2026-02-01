package com.myhome.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Serviço de infraestrutura responsável por registrar logs.
 * RF04 - Observer Pattern: registra eventos do sistema
 * Logs são exibidos no console E salvos em arquivo logs/sistema.log
 */
public class LoggerService {

    private static final String LOG_FILE = "logs/sistema.log";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void info(String mensagem) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = "[" + timestamp + "] " + mensagem;
        
        // Exibir no console
        System.out.println("📝 " + logMessage);
        
        // Salvar em arquivo
        salvarEmArquivo(logMessage);
    }

    private void salvarEmArquivo(String mensagem) {
        try {
            // Criar diretório logs se não existir
            java.io.File logDir = new java.io.File("logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            // Append ao arquivo de log
            try (FileWriter fw = new FileWriter(LOG_FILE, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println(mensagem);
            }
        } catch (IOException e) {
            System.err.println("⚠️  Erro ao salvar log em arquivo: " + e.getMessage());
        }
    }
}
