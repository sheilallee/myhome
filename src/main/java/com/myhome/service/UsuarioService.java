package com.myhome.service;

import com.myhome.model.Usuario;
import com.myhome.strategy.EmailNotificacao;
import com.myhome.strategy.SMSNotificacao;
import com.myhome.strategy.WhatsAppNotificacao;

/**
 * SERVIÇO DE GERENCIAMENTO DE USUÁRIOS
 * 
 * RESPONSABILIDADE:
 * - Encapsular a lógica de criação e gerenciamento de usuários
 * - Fornecer interface de alto nível para operações com usuários
 * - Definir preferências de notificação (RF05 - Strategy)
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por gerenciar usuários
 * - OCP: Pode ser estendido com novos tipos de usuários
 */
public class UsuarioService {
    
    // ========================================
    // CRIAÇÃO DE USUÁRIOS
    // ========================================

    /**
     * Cria um usuário proprietário.
     * RF05 - Configura canal padrão de notificação (Email)
     */
    public Usuario criarProprietario(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.PROPRIETARIO);
        // RF05 - Configurar canal padrão de email para notificações
        usuario.setCanalNotificacao(new EmailNotificacao(new EmailService()));
        return usuario;
    }
    
    /**
     * Cria um usuário corretor.
     * RF05 - Configura canal padrão de notificação (Email)
     */
    public Usuario criarCorretor(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.CORRETOR);
        // RF05 - Configurar canal padrão de email para notificações
        usuario.setCanalNotificacao(new EmailNotificacao(new EmailService()));
        return usuario;
    }
    
    /**
     * Cria um usuário imobiliária.
     * RF05 - Configura canal padrão de notificação (Email)
     */
    public Usuario criarImobiliaria(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.IMOBILIARIA);
        // RF05 - Configurar canal padrão de email para notificações
        usuario.setCanalNotificacao(new EmailNotificacao(new EmailService()));
        return usuario;
    }
    
    /**
     * Cria um usuário comprador.
     * RF05 - Configura canal padrão de notificação (Email)
     */
    public Usuario criarComprador(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.COMPRADOR);
        // RF05 - Configurar canal padrão de email para notificações
        usuario.setCanalNotificacao(new EmailNotificacao(new EmailService()));
        return usuario;
    }

    // ========================================
    // REGRAS DE NEGÓCIO
    // ========================================

    /**
     * Verifica se um usuário pode criar anúncios.
     */
    public boolean podeAnunciar(Usuario usuario) {
        return usuario.podeAnunciar();
    }
    
    /**
     * Verifica se um usuário é administrador.
     */
    public boolean isAdministrador(Usuario usuario) {
        return usuario.isAdministrador();
    }

    // ========================================
    // CONFIGURAÇÃO DE NOTIFICAÇÃO (RF05)
    // ========================================

    /**
     * Define o canal de notificação do usuário como Email.
     * (Strategy Pattern - RF05)
     */
    public void configurarCanalEmail(Usuario usuario) {
        usuario.setCanalNotificacao(
            new EmailNotificacao(new EmailService())
        );
    }

    /**
     * Define o canal de notificação do usuário como SMS.
     */
    public void configurarCanalSMS(Usuario usuario) {
        usuario.setCanalNotificacao(
            new SMSNotificacao(new SMSService())
        );
    }

    /**
     * Define o canal de notificação do usuário como WhatsApp.
     */
    public void configurarCanalWhatsApp(Usuario usuario) {
        usuario.setCanalNotificacao(
            new WhatsAppNotificacao(new WhatsAppService())
        );
    }
}
