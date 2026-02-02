package com.myhome.service;

import java.util.Scanner;

import com.myhome.model.Usuario;
import com.myhome.strategy.EmailNotificacao;
import com.myhome.strategy.NotificationManager;
import com.myhome.strategy.SMSNotificacao;
import com.myhome.strategy.WhatsAppNotificacao;

/**
 * RF05 - SERVICE: Gerencia configuração de canais de notificação
 * 
 * RESPONSABILIDADES:
 * - Exibir menu de seleção de canais
 * - Configurar canal de notificação do usuário
 * - Testar canal de notificação
 * 
 * BENEFÍCIOS:
 * - Encapsula lógica de configuração
 * - Facilita testes unitários
 * - Desacoplamento da Facade
 * 
 * PADRÃO UTILIZADO:
 * - Strategy Pattern: Diferentes estratégias de notificação (Email, SMS, WhatsApp)
 */
public class NotificationConfigService {
    
    private EmailService emailService;
    private SMSService smsService;
    private WhatsAppService whatsAppService;
    
    /**
     * Construtor com injeção de dependências dos serviços de notificação
     */
    public NotificationConfigService(
            EmailService emailService,
            SMSService smsService,
            WhatsAppService whatsAppService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.whatsAppService = whatsAppService;
    }
    
    /**
     * Permite ao usuário configurar seu canal de notificação preferido
     * Implementa Strategy Pattern: permite trocar o algoritmo de notificação
     * 
     * @param scanner Para entrada de dados do usuário
     * @param usuarioAtual Usuário que está configurando a notificação
     */
    public void configurarCanalNotificacao(Scanner scanner, Usuario usuarioAtual) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ RF05 - STRATEGY (Canal de Notificação)  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println("📢 Escolha o canal de notificação preferido:\n");
        System.out.println("[1] Email 📧");
        System.out.println("    → Notificações por email (mais detalhado)");
        System.out.println("[2] SMS 📱");
        System.out.println("    → Notificações por SMS (mais rápido)");
        System.out.println("[3] WhatsApp 💬");
        System.out.println("    → Notificações por WhatsApp");
        System.out.println("[0] Cancelar");
        
        try {
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            
            switch (opcao) {
                case 1:
                    configurarEmail(usuarioAtual);
                    break;
                    
                case 2:
                    configurarSMS(usuarioAtual);
                    break;
                    
                case 3:
                    configurarWhatsApp(usuarioAtual);
                    break;
                    
                case 0:
                    System.out.println("❌ Operação cancelada.");
                    break;
                    
                default:
                    System.out.println("❌ Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Opção inválida! Digite um número.");
        }
    }
    
    /**
     * Configura Email como canal de notificação
     */
    private void configurarEmail(Usuario usuarioAtual) {
        usuarioAtual.setCanalNotificacao(
            new EmailNotificacao(emailService)
        );
        System.out.println("\n✅ Canal alterado para EMAIL");
        System.out.println("   Você receberá notificações por: " + usuarioAtual.getEmail());
        testarNotificacao(usuarioAtual, "📧 Email: Bem-vindo! Você está recebendo notificações por email.");
    }
    
    /**
     * Configura SMS como canal de notificação
     */
    private void configurarSMS(Usuario usuarioAtual) {
        usuarioAtual.setCanalNotificacao(
            new SMSNotificacao(smsService)
        );
        System.out.println("\n✅ Canal alterado para SMS");
        System.out.println("   Você receberá notificações por: " + usuarioAtual.getTelefone());
        testarNotificacao(usuarioAtual, "📱 SMS: Bem-vindo! Você está recebendo notificações por SMS.");
    }
    
    /**
     * Configura WhatsApp como canal de notificação
     */
    private void configurarWhatsApp(Usuario usuarioAtual) {
        usuarioAtual.setCanalNotificacao(
            new WhatsAppNotificacao(whatsAppService)
        );
        System.out.println("\n✅ Canal alterado para WHATSAPP");
        System.out.println("   Você receberá notificações por: " + usuarioAtual.getTelefone());
        testarNotificacao(usuarioAtual, "💬 WhatsApp: Bem-vindo! Você está recebendo notificações por WhatsApp.");
    }
    
    /**
     * Testa o canal de notificação configurado
     * 
     * @param usuarioAtual Usuário para enviar a notificação
     * @param mensagem Mensagem de teste
     */
    public void testarNotificacao(Usuario usuarioAtual, String mensagem) {
        System.out.println("\n📤 Enviando notificação de teste...");
        NotificationManager manager = new NotificationManager();
        manager.enviarNotificacao(usuarioAtual, mensagem);
        System.out.println("✅ Notificação enviada com sucesso!");
    }
}
