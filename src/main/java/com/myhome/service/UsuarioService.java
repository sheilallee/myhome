package com.myhome.service;

import com.myhome.model.Usuario;

/**
 * SERVIÇO DE GERENCIAMENTO DE USUÁRIOS
 * 
 * RESPONSABILIDADE:
 * - Encapsular a lógica de criação e gerenciamento de usuários
 * - Fornecer interface de alto nível para operações com usuários
 * - Validar regras de negócio relacionadas a usuários
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por gerenciar usuários
 * - OCP: Pode ser estendido com novos tipos de usuários
 */
public class UsuarioService {
    
    /**
     * Cria um usuário proprietário.
     */
    public Usuario criarProprietario(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.PROPRIETARIO);
        return usuario;
    }
    
    /**
     * Cria um usuário corretor.
     */
    public Usuario criarCorretor(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.CORRETOR);
        return usuario;
    }
    
    /**
     * Cria um usuário imobiliária.
     */
    public Usuario criarImobiliaria(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.IMOBILIARIA);
        return usuario;
    }
    
    /**
     * Cria um usuário comprador.
     */
    public Usuario criarComprador(String nome, String email, String telefone) {
        Usuario usuario = new Usuario(nome, email, telefone);
        usuario.setTipo(Usuario.TipoUsuario.COMPRADOR);
        return usuario;
    }
    
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
}
