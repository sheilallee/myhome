package com.myhome;

import com.myhome.service.MyHomeService;

/**
 * CLASSE PRINCIPAL - APLICAÇÃO MYHOME
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * ✓ SRP: Main tem APENAS a responsabilidade de iniciar a aplicação
 * ✓ DIP: Depende de abstração (MyHomeService), não de implementações concretas
 * ✓ OCP: Fechado para modificação - novos recursos não alteram o Main
 * ✓ LSP: MyHomeService pode ser substituído por outras implementações
 * 
 * PADRÕES DEMONSTRADOS:
 * - RF07: Singleton + Facade (ConfigurationManager e ConfigFacade)
 * - RF01: Factory Method + Builder (ImovelFactory e AnuncioBuilder)
 * 
 * ARQUITETURA LIMPA:
 * O Main NÃO conhece detalhes de implementação.
 * Toda a complexidade está encapsulada na camada de SERVIÇOS.
 * 
 * @author MyHome Team
 * @version 1.0
 */
public class Main {
    
    public static void main(String[] args) {
        imprimirCabecalho();
        
        // Cliente usa APENAS uma interface de alto nível
        // Não conhece factories, builders, nem detalhes de implementação
        MyHomeService myHome = new MyHomeService();
        
        // Executar demonstrações dos padrões
        myHome.demonstrarSingletonFacade();
        myHome.demonstrarFactoryBuilder();
        myHome.demonstrarCasoDeUsoCompleto();
        
        imprimirRodape();
    }
    
    /**
     * Imprime cabeçalho da aplicação.
     */
    private static void imprimirCabecalho() {
        System.out.println("=".repeat(80));
        System.out.println("MYHOME - SISTEMA DE ANÚNCIOS DE IMÓVEIS");
        System.out.println("Demonstração dos Padrões de Projeto");
        System.out.println("=".repeat(80));
        System.out.println();
    }
    
    /**
     * Imprime rodapé da aplicação.
     */
    private static void imprimirRodape() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("✓ DEMONSTRAÇÃO CONCLUÍDA COM SUCESSO!");
        System.out.println("✓ Padrões implementados seguindo princípios SOLID");
        System.out.println("=".repeat(80));
    }
}
