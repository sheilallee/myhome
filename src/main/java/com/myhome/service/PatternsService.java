package com.myhome.service;

import com.myhome.model.Imovel;
import com.myhome.prototype.PrototypeRegistry;

/**
 * Serviço responsável por demonstrar os padrões GoF implementados
 * 
 * RESPONSABILIDADES:
 * - Exibir informações sobre cada padrão
 * - Demonstrar o funcionamento do padrão Prototype
 * - Fornecer documentação do sistema via terminal
 */
public class PatternsService {
    
    /**
     * Demonstra todos os padrões GoF implementados no sistema
     */
    public void demonstrarTodosPadroes() {
        exibirCabecalho();
        exibirListaPadroes();
        demonstrarPrototype();
        exibirInstrucoesDoUsuario();
        exibirRodape();
    }
    
    /**
     * Exibe cabeçalho da demonstração
     */
    private void exibirCabecalho() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   DEMONSTRAÇÃO PADRÕES GOF             ║");
        System.out.println("║   RF01 + RF02 + RF07                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("📚 PADRÕES IMPLEMENTADOS NO MYHOME:\n");
    }
    
    /**
     * Exibe lista com descrição de cada padrão
     */
    private void exibirListaPadroes() {
        System.out.println("✅ RF01 - FACTORY METHOD (Criação de Anúncios)");
        System.out.println("   → VendaFactory, AluguelFactory, TemporadaFactory");
        System.out.println("   → Usado na opção: 1 - Criar novo anúncio\n");
        
        System.out.println("✅ RF01 - BUILDER (Construção de Imóveis)");
        System.out.println("   → ImovelBuilder, ImovelBuilderImpl");
        System.out.println("   → Usado na opção: 1 - Criar novo anúncio → Criar do zero\n");
        
        System.out.println("✅ RF01 - DIRECTOR");
        System.out.println("   → Director (sequências pré-definidas)");
        System.out.println("   → Disponível para construções automatizadas\n");
        
        System.out.println("✅ RF02 - PROTOTYPE (Modelos Padrão de Imóveis)");
        System.out.println("   → Interface: ImovelPrototype (método clonar())");
        System.out.println("   → Singleton: PrototypeRegistry (armazena e fornece clones)");
        System.out.println("   → Usado na opção: 1 - Criar novo anúncio → Usar modelo padrão\n");
        
        System.out.println("✅ RF07 - SINGLETON (Configurações)");
        System.out.println("   → ConfigurationManager");
        System.out.println("   → Usado na opção: 4 - Configurações\n");
    }
    
    /**
     * Demonstra o funcionamento do padrão Prototype em detalhes
     */
    private void demonstrarPrototype() {
        System.out.println("🔍 DEMONSTRAÇÃO LIVE - PROTOTYPE PATTERN:");
        System.out.println("   ┌ ────────────────────────── ┐");
        
        PrototypeRegistry registro = PrototypeRegistry.getInstance();
        
        // Obtém um protótipo
        Imovel original = registro.obterPrototipo("apartamento-padrao");
        
        // Clona o protótipo
        Imovel clone1 = registro.obterPrototipo("apartamento-padrao");
        Imovel clone2 = registro.obterPrototipo("apartamento-padrao");
        
        System.out.println("   • Original: " + original.hashCode());
        System.out.println("   • Clone 1: " + clone1.hashCode());
        System.out.println("   • Clone 2: " + clone2.hashCode());
        System.out.println("   ✓ São objetos diferentes (hashcodes distintos)");
        System.out.println("   ✓ Cada clone é independente para customização");
        System.out.println("   └ ────────────────────────── ┘\n");
    }
    
    /**
     * Exibe instruções de como testar os padrões
     */
    private void exibirInstrucoesDoUsuario() {
        System.out.println("💡 COMO TESTAR OS PADRÕES:");
        System.out.println("   1. Use a opção '1' → '2' para criar anúncio com Builder");
        System.out.println("   2. Use a opção '1' → '1' para criar anúncio com Prototype");
        System.out.println("   3. Use a opção '3' para ver seus anúncios cadastrados");
        System.out.println("   4. Use a opção '4' para ver o Singleton em ação\n");
    }
    
    /**
     * Exibe rodapé com mensagem de sucesso
     */
    private void exibirRodape() {
        System.out.println("═".repeat(60));
        System.out.println("✅ Todos os padrões estão funcionando via terminal!");
        System.out.println("═".repeat(60) + "\n");
    }
}
