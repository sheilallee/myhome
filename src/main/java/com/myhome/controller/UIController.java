package com.myhome.controller;

import com.myhome.model.*;
import com.myhome.service.MenuService;
import com.myhome.service.ValidadorService;
import java.util.List;
import java.util.Scanner;

/**
 * CONTROLADOR DE INTERFACE COM USUÁRIO
 * 
 * RESPONSABILIDADE:
 * - Centralizar toda interação com usuário (entrada/saída)
 * - Coletar dados específicos de domínio (Imovel, Anuncio, Filtros)
 * - Exibir resultados formatados
 * - Gerenciar Scanner único da aplicação
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por UI/interação com usuário
 * - Composição: Compõe MenuService para reutilizar primitivas genéricas
 * - DIP: Não depende de lógica de negócio (services)
 * 
 * DESIGN: Keep it Simple!
 * - Reutiliza MenuService para métodos genéricos (lerTexto, lerOpcao, etc)
 * - Adiciona apenas métodos específicos de domínio
 * - Scanner único gerenciado via MenuService
 * 
 * @author MyHome Team
 */
public class UIController {
    
    private final MenuService menuService;
    private final ValidadorService validadorService;
    
    public UIController(MenuService menuService, ValidadorService validadorService) {
        this.menuService = menuService;
        this.validadorService = validadorService;
    }
    
    // ================================================================
    // DELEGAÇÃO PARA MenuService (Reutilização)
    // ================================================================
    
    public int lerOpcao(String prompt) {
        return menuService.lerOpcao(prompt);
    }
    
    public String lerTexto(String prompt) {
        return menuService.lerTexto(prompt);
    }
    
    public double lerDecimal(String prompt) {
        return menuService.lerDecimal(prompt);
    }
    
    public boolean lerConfirmacao(String prompt) {
        return menuService.lerConfirmacao(prompt);
    }
    
    public void exibirSucesso(String mensagem) {
        menuService.exibirSucesso(mensagem);
    }
    
    public void exibirErro(String mensagem) {
        menuService.exibirErro(mensagem);
    }
    
    public void exibirInfo(String mensagem) {
        menuService.exibirInfo(mensagem);
    }
    
    public void exibirCabecalho(String titulo) {
        menuService.exibirCabecalho(titulo);
    }
    
    public void exibirPasso(String titulo) {
        menuService.exibirPasso(titulo);
    }
    
    public void pausar() {
        menuService.pausar();
    }
    
    public Scanner getScanner() {
        return menuService.getScanner();
    }
    
    public void fechar() {
        menuService.fechar();
    }
    
    // ================================================================
    // MÉTODOS ESPECÍFICOS DE DOMÍNIO - COLETA DE DADOS
    // ================================================================
    
    /**
     * Coleta dados do usuário anunciante.
     */
    public Usuario coletarDadosUsuario() {
        String nome = lerTexto("\n👤 Seu nome: ");
        
        String email;
        while (true) {
            email = lerTexto("📧 Seu email: ");
            if (validadorService.validarEmail(email)) {
                break;
            }
            exibirErro("Email inválido! Use o formato: exemplo@dominio.com");
        }
        
        String telefone;
        while (true) {
            String input = lerTexto("📱 Seu telefone (apenas números): ");
            telefone = validadorService.formatarTelefone(input);
            if (telefone != null) {
                break;
            }
            exibirErro("Telefone inválido! Digite 10 ou 11 dígitos (ex: 83988881111)");
        }
        
        return new Usuario(nome, email, telefone);
    }
    
    /**
     * Coleta filtros de busca e retorna como String[] para ser processado pelo Facade.
     * Retorna: [precoMin, precoMax, cidade, estado, tipo]
     */
    public String[] coletarFiltrosBusca() {
        String precoMin = "";
        String precoMax = "";
        String cidade = "";
        String estado = "";
        String tipo = "";
        
        exibirPasso("FILTROS DE BUSCA");
        exibirInfo("(Pressione Enter para pular um filtro)\n");
        
        // Filtro 1: Preço
        if (lerConfirmacao("💰 Filtrar por preço? (S/N): ")) {
            try {
                double min = lerDecimal("   Preço mínimo (R$): ");
                double max = lerDecimal("   Preço máximo (R$): ");
                if (min > 0 && max > 0 && min <= max) {
                    precoMin = String.valueOf(min);
                    precoMax = String.valueOf(max);
                }
            } catch (Exception e) {
                exibirErro("Preços inválidos, pulando filtro.");
            }
        }
        
        // Filtro 2: Localização
        if (lerConfirmacao("\n🏠 Filtrar por localização? (S/N): ")) {
            cidade = lerTexto("   Cidade: ").trim();
            estado = lerTexto("   Estado (ex: PB): ").trim().toUpperCase();
        }
        
        // Filtro 3: Tipo de Imóvel
        if (lerConfirmacao("\n🏘️  Filtrar por tipo de imóvel? (S/N): ")) {
            exibirInfo("Tipos: Casa, Apartamento, Terreno, Sala Comercial");
            tipo = lerTexto("   Tipo: ").trim();
        }
        
        return new String[]{precoMin, precoMax, cidade, estado, tipo};
    }
    
    // ================================================================
    // MÉTODOS ESPECÍFICOS DE DOMÍNIO - EXIBIÇÃO DE RESULTADOS
    // ================================================================
    
    /**
     * Exibe um anúncio formatado.
     */
    public void exibirAnuncio(Anuncio anuncio, int numero) {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│ [" + numero + "] " + anuncio.getTitulo());
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 💰 Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
        
        Imovel imovel = anuncio.getImovel();
        System.out.println("│ 📍 Local: " + imovel.getEndereco().getCidade() + 
                         " - " + imovel.getEndereco().getEstado());
        System.out.println("│ 🏠 Tipo: " + imovel.getTipo());
        System.out.println("│ 📏 Área: " + imovel.getArea() + " m²");
        System.out.println("│ 📊 Estado: " + anuncio.getState().getNome().toUpperCase());
        System.out.println("└────────────────────────────────────────┘\n");
    }
    
    /**
     * Exibe uma lista de anúncios.
     */
    public void exibirListaAnuncios(List<Anuncio> anuncios, String titulo) {
        exibirCabecalho(titulo);
        
        if (anuncios.isEmpty()) {
            exibirErro("Nenhum anúncio encontrado.");
            pausar();
            return;
        }
        
        System.out.println("✅ " + anuncios.size() + " anúncio(s) encontrado(s):\n");
        
        for (int i = 0; i < anuncios.size(); i++) {
            exibirAnuncio(anuncios.get(i), i + 1);
        }
        
        pausar();
    }
    
    /**
     * Exibe resultado detalhado de um anúncio criado.
     */
    public void exibirResultadoAnuncioCriado(Anuncio anuncio, int numero) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     ✅ ANÚNCIO CRIADO COM SUCESSO!     ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        Imovel imovel = anuncio.getImovel();
        Usuario anunciante = anuncio.getAnunciante();
        
        System.out.println("\n📋 ANÚNCIO #" + numero);
        System.out.println("─".repeat(40));
        System.out.println("🏷️  Título: " + anuncio.getTitulo());
        System.out.println("💰 Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
        System.out.println("📄 Descrição: " + anuncio.getDescricao());
        
        System.out.println("\n🏘️  IMÓVEL:");
        System.out.println("   Tipo: " + imovel.getTipo());
        System.out.println("   Endereço: " + imovel.getEndereco());
        System.out.println("   Área: " + imovel.getArea() + "m²");
        
        System.out.println("\n👤 ANUNCIANTE:");
        System.out.println("   Nome: " + anunciante.getNome());
        System.out.println("   Email: " + anunciante.getEmail());
        System.out.println("   Telefone: " + anunciante.getTelefone());
        
        System.out.println("\n💡 Status: RASCUNHO (pronto para publicação)");
        System.out.println("═".repeat(40) + "\n");
    }
    
    /**
     * Exibe resultado de busca avançada com filtros.
     */
    public void exibirResultadoBusca(List<Anuncio> resultados) {
        exibirListaAnuncios(resultados, "RESULTADOS DA BUSCA");
    }
    
    /**
     * Exibe lista de "Anúncios".
     */
    public void exibirMeusAnuncios(List<Anuncio> anuncios) {
        exibirCabecalho("ANÚNCIOS");
        
        if (anuncios.isEmpty()) {
            exibirErro("Você ainda não criou nenhum anúncio.");
            pausar();
            return;
        }
        
        System.out.println(">> Total de anúncios: " + anuncios.size() + "\n");
        
        for (int i = 0; i < anuncios.size(); i++) {
            Anuncio anuncio = anuncios.get(i);
            System.out.println("[" + (i + 1) + "] " + anuncio.getTitulo());
            System.out.println("    💰 Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
            System.out.println("    📊 Estado: " + anuncio.getImovel().getEndereco().getEstado().toUpperCase());
            System.out.println("    🏘️  Tipo: " + anuncio.getImovel().getTipo() + "\n");
        }
        
        pausar();
    }
    
    /**
     * Exibe menus principais.
     */
    public void exibirMenuPrincipal() {
        menuService.exibirMenuPrincipal();
    }
    
    public void exibirSubmenuCriarAnuncio() {
        menuService.exibirSubmenuCriarAnuncio();
    }
    
    public void exibirMensagemDespedida() {
        menuService.exibirMensagemDespedida();
    }
}
