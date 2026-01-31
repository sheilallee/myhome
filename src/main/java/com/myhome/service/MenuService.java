package com.myhome.service;

import java.util.Scanner;

/**
 * SERVIÇO DE INTERFACE COM USUÁRIO (UI)
 * 
 * RESPONSABILIDADE:
 * - Exibir menus e mensagens formatadas
 * - Capturar entrada do usuário
 * - Gerenciar fluxo de navegação
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por UI/interação
 * - DIP: Não depende de lógica de negócio
 * 
 * PADRÃO: Template Method (estrutura de menus)
 * 
 * @author MyHome Team - João Pessoa/PB
 */
public class MenuService {
    
    private final Scanner scanner;
    
    public MenuService() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Exibe o menu principal do sistema.
     */
    public void exibirMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      MYHOME - CLASSIFICADOS          ║");
        System.out.println("║        IMOBILIÁRIOS                   ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Criar novo anúncio                ║");
        System.out.println("║  2. Buscar imóveis                    ║");
        System.out.println("║  3. Meus anúncios                     ║");
        System.out.println("║  4. Configurações                     ║");
        System.out.println("║  0. Sair                              ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    /**
     * Exibe submenu de criação de anúncio.
     */
    public void exibirSubmenuCriarAnuncio() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       CRIAR NOVO ANÚNCIO              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Usar modelo padrão (Prototype)    ║");
        System.out.println("║  2. Criar do zero (Builder)           ║");
        System.out.println("║  0. Voltar                            ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    /**
     * Exibe mensagem de despedida.
     */
    public void exibirMensagemDespedida() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        👋 ATÉ LOGO! 👋                ║");
        System.out.println("║   Obrigado por usar o MyHome!         ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Exibe cabeçalho de seção.
     */
    public void exibirCabecalho(String titulo) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  " + centralizarTexto(titulo, 38) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Exibe título de passo.
     */
    public void exibirPasso(String titulo) {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│  " + titulo + repetirEspacos(38 - titulo.length()) + "│");
        System.out.println("└────────────────────────────────────────┘\n");
    }
    
    /**
     * Lê uma opção numérica do usuário.
     */
    public int lerOpcao(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Opção inválida
        }
    }
    
    /**
     * Lê um texto do usuário.
     */
    public String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Lê um número decimal do usuário.
     */
    public double lerDecimal(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Valor inválido
        }
    }
    
    /**
     * Lê uma confirmação sim/não.
     */
    public boolean lerConfirmacao(String prompt) {
        System.out.print(prompt);
        String resposta = scanner.nextLine().trim().toLowerCase();
        return resposta.equals("s") || resposta.equals("sim");
    }
    
    /**
     * Pausa e aguarda ENTER.
     */
    public void pausar() {
        System.out.println("\n⏸️  Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Exibe mensagem de sucesso.
     */
    public void exibirSucesso(String mensagem) {
        System.out.println("✅ " + mensagem);
    }
    
    /**
     * Exibe mensagem de erro.
     */
    public void exibirErro(String mensagem) {
        System.out.println("❌ " + mensagem);
    }
    
    /**
     * Exibe mensagem de informação.
     */
    public void exibirInfo(String mensagem) {
        System.out.println("ℹ️  " + mensagem);
    }
    
    /**
     * Centraliza texto em um tamanho específico.
     */
    private String centralizarTexto(String texto, int tamanho) {
        int espacos = (tamanho - texto.length()) / 2;
        return repetirEspacos(espacos) + texto + repetirEspacos(tamanho - texto.length() - espacos);
    }
    
    /**
     * Repete espaços.
     */
    private String repetirEspacos(int quantidade) {
        return " ".repeat(Math.max(0, quantidade));
    }
    
    /**
     * Fecha o scanner ao finalizar.
     */
    public void fechar() {
        scanner.close();
    }
    
    /**
     * Retorna o scanner (para casos especiais).
     */
    public Scanner getScanner() {
        return scanner;
    }
}
