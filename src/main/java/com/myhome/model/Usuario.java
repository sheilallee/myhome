package com.myhome.model;

import com.myhome.strategy.NotificacaoStrategy;

/**
 * RF01 - ENTIDADE DE DOMÍNIO
 * 
 * Representa um Usuário no sistema MyHome.
 * 
 * RESPONSABILIDADES:
 * - Armazenar informações do usuário
 * - Definir preferências de notificação (Strategy - RF05)
 * 
 * TIPOS DE USUÁRIO:
 * - PROPRIETARIO: Dono de imóveis
 * - CORRETOR: Corretor de imóveis
 * - IMOBILIARIA: Imobiliária
 * - COMPRADOR: Interessado em comprar/alugar
 * - ADMINISTRADOR: Gerencia a plataforma
 */
public class Usuario {

    // ========================================
    // ATRIBUTOS
    // ========================================

    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String senha; // Em produção, deve ser hash

    /**
     * Tipo de usuário no sistema
     */
    private TipoUsuario tipo;

    /**
     * Canal preferido de notificação
     * Strategy Pattern - RF05
     */
    private NotificacaoStrategy canalNotificacao;

    // ========================================
    // ENUM - TIPO DE USUÁRIO
    // ========================================

    public enum TipoUsuario {
        PROPRIETARIO,
        CORRETOR,
        IMOBILIARIA,
        COMPRADOR,
        ADMINISTRADOR
    }

    // ========================================
    // CONSTRUTORES
    // ========================================

    public Usuario() {
        this.tipo = TipoUsuario.COMPRADOR;
    }

    public Usuario(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipo = TipoUsuario.COMPRADOR;
    }

    // ========================================
    // MÉTODOS DE NEGÓCIO
    // ========================================

    /**
     * Valida se o usuário possui informações mínimas válidas.
     */
    public boolean validar() {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        if (email == null || !email.contains("@")) {
            return false;
        }

        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Verifica se o usuário é administrador.
     */
    public boolean isAdministrador() {
        return this.tipo == TipoUsuario.ADMINISTRADOR;
    }

    /**
     * Verifica se o usuário pode criar anúncios.
     */
    public boolean podeAnunciar() {
        return this.tipo == TipoUsuario.PROPRIETARIO
            || this.tipo == TipoUsuario.CORRETOR
            || this.tipo == TipoUsuario.IMOBILIARIA;
    }

    /**
     * Define o canal preferido de notificação do usuário.
     * 
     * PADRÃO STRATEGY (RF05)
     */
    public void setCanalNotificacao(NotificacaoStrategy canal) {
        this.canalNotificacao = canal;
    }

    /**
     * Retorna o canal de notificação configurado.
     */
    public NotificacaoStrategy getCanalNotificacao() {
        return canalNotificacao;
    }

    // ========================================
    // GETTERS E SETTERS
    // ========================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================

    @Override
    public String toString() {
        return String.format(
            "Usuario{nome='%s', email='%s', tipo=%s}",
            nome, email, tipo
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;
        return id != null && id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}