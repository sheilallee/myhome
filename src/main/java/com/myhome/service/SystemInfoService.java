package com.myhome.service;

import com.myhome.controller.UIController;
import com.myhome.model.Usuario;
import com.myhome.singleton.ConfigurationManager;

/**
 * Serviço responsável por exibir informações do sistema (RF07 - Singleton)
 * 
 * RESPONSABILIDADES:
 * - Exibir informações de configuração
 * - Exibir dados do usuário atual
 * - Demonstrar o padrão Singleton
 */
public class SystemInfoService {
    
    private final UIController uiController;
    
    public SystemInfoService(UIController uiController) {
        this.uiController = uiController;
    }
    
    /**
     * Exibe informações completas do sistema (RF07)
     */
    public void exibirInformacoes(Usuario usuarioAtual) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   RF07 - SINGLETON (Configurações)     ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        exibirConfiguracoesDoSistema(config);
        exibirDadosDoUsuario(usuarioAtual);
        exibirInfoSingleton(config);
    }
    
    /**
     * Exibe configurações do sistema
     */
    private void exibirConfiguracoesDoSistema(ConfigurationManager config) {
        System.out.println("📋 Configurações do Sistema:");
        System.out.println("─".repeat(40));
        System.out.println("Nome: " + config.getProperty("app.name", "MyHome"));
        System.out.println("Versão: " + config.getProperty("app.version", "2.0"));
        System.out.println("Cidade: João Pessoa - Paraíba");
        System.out.println("─".repeat(40));
    }
    
    /**
     * Exibe dados do usuário atual
     */
    private void exibirDadosDoUsuario(Usuario usuarioAtual) {
        System.out.println("\n👤 Dados do Usuário Atual:");
        System.out.println("─".repeat(40));
        System.out.println("Nome: " + usuarioAtual.getNome());
        System.out.println("Email: " + usuarioAtual.getEmail());
        System.out.println("Telefone: " + usuarioAtual.getTelefone());
        System.out.println("Canal de Notificação: " + 
            (usuarioAtual.getCanalNotificacao() != null 
                ? usuarioAtual.getCanalNotificacao().getClass().getSimpleName().replace("Notificacao", "")
                : "Não configurado"));
        System.out.println("─".repeat(40));
    }
    
    /**
     * Exibe informações sobre o padrão Singleton
     */
    private void exibirInfoSingleton(ConfigurationManager config) {
        System.out.println("\n💡 ConfigurationManager é um Singleton!");
        System.out.println("   Sempre a mesma instância: " + config.hashCode());
    }
}
