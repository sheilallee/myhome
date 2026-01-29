package com.myhome.model;

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
     * Canal preferido de notificação (Strategy Pattern - RF05)
     * Será do tipo NotificationStrategy quando implementado
     */
    // private NotificationStrategy canalNotificacao; // Será implementado em RF05
    
    // ========================================
    // ENUM - TIPO DE USUÁRIO
    // ========================================
    
    /**
     * Tipos possíveis de usuário no sistema.
     */
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
        this.tipo = TipoUsuario.COMPRADOR; // Tipo padrão
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
     * 
     * REGRA DE NEGÓCIO:
     * - Nome não pode ser vazio
     * - Email deve ser válido (formato básico)
     * - Telefone não pode ser vazio
     * 
     * @return true se válido
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
     * Verifica se o usuário é um administrador.
     * 
     * @return true se for administrador
     */
    public boolean isAdministrador() {
        return this.tipo == TipoUsuario.ADMINISTRADOR;
    }
    
    /**
     * Verifica se o usuário pode criar anúncios.
     * 
     * REGRA DE NEGÓCIO:
     * Apenas proprietários, corretores e imobiliárias podem criar anúncios.
     * 
     * @return true se pode anunciar
     */
    public boolean podeAnunciar() {
        return this.tipo == TipoUsuario.PROPRIETARIO || 
               this.tipo == TipoUsuario.CORRETOR || 
               this.tipo == TipoUsuario.IMOBILIARIA;
    }
    
    // Método será implementado em RF05 (Strategy)
    /**
     * Define o canal preferido de notificação do usuário.
     * 
     * PADRÃO STRATEGY (RF05):
     * Permite trocar dinamicamente a estratégia de notificação.
     * 
     * @param canal Estratégia de notificação
     */
    /*
    public void setCanalNotificacao(NotificationStrategy canal) {
        this.canalNotificacao = canal;
    }
    */
    
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
        return String.format("Usuario{nome='%s', email='%s', tipo=%s}", 
            nome, email, tipo);
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
