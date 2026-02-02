package com.myhome.service;

import java.util.List;
import java.util.Scanner;

import com.myhome.controller.UIController;
import com.myhome.facade.AnuncioFacade;
import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;

/**
 * RF04 - SERVICE: Gerencia ciclo de vida de anúncios individuais
 * 
 * RESPONSABILIDADES:
 * - Exibir menu de gerenciamento de anúncio específico
 * - Executar ações de mudança de estado
 * - Validar transições de estado
 * - Coordenar persistência
 * - Enviar notificações
 * 
 * BENEFÍCIOS:
 * - Encapsula lógica complexa de gerenciamento de anúncio
 * - Facilita testes unitários
 * - Desacoplamento da Facade
 * - Reutilização de lógica
 * 
 * PADRÕES UTILIZADOS:
 * - State Pattern: Gerencia transições de estado
 * - Strategy Pattern: Notificações através de diferentes canais
 * - Facade Pattern: AnuncioFacade coordena mudanças de estado
 */
public class AnuncioManagementService {
    
    private PersistenciaService persistenciaService;
    private UIController uiController;
    private List<Anuncio> meusAnuncios;
    
    /**
     * Construtor com injeção de dependências
     */
    public AnuncioManagementService(
            PersistenciaService persistenciaService,
            UIController uiController,
            List<Anuncio> meusAnuncios) {
        this.persistenciaService = persistenciaService;
        this.uiController = uiController;
        this.meusAnuncios = meusAnuncios;
    }
    
    /**
     * Gerencia um anúncio específico com menu interativo
     * Permite transições de estado baseado na situação atual
     * 
     * @param scanner Para entrada de dados do usuário
     * @param anuncio O anúncio a ser gerenciado
     */
    public void gerenciarAnuncioEspecifico(Scanner scanner, Anuncio anuncio) {
        AnuncioFacade facade = new AnuncioFacade();
        
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GERENCIAR ANÚNCIO                ║");
            System.out.println("╚════════════════════════════════════════╝");
            
            System.out.println("\n📄 " + anuncio.getTitulo());
            System.out.println("💰 R$ " + String.format("%,.2f", anuncio.getPreco()));
            System.out.println("📊 Estado atual: " + anuncio.getState().getNome().toUpperCase());
            
            System.out.println("\n┌────────────────────────────────────────┐");
            System.out.println("│ AÇÕES DISPONÍVEIS:                     │");
            System.out.println("├────────────────────────────────────────┤");
            
            String estadoNome = anuncio.getState().getNome();
            exibirAcoesDisponiveis(estadoNome);
            
            System.out.print("\n➤ Escolha uma ação: ");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                
                if (opcao == 0) {
                    return;
                }
                
                boolean sucesso = executarAcaoAnuncio(facade, anuncio, opcao, estadoNome);
                
                if (sucesso) {
                    // Salvar mudanças após transição bem-sucedida
                    persistenciaService.salvarAnuncios(meusAnuncios);
                    System.out.println("\nAlteração no anúncio registrada com sucesso!");
                    pausar(scanner);
                } else {
                    pausar(scanner);
                }
                
            } catch (NumberFormatException e) {
                System.out.println("\n❌ Digite um número válido!");
                pausar(scanner);
            } catch (IllegalStateException e) {
                System.out.println("\n⚠️  Erro: " + e.getMessage());
                pausar(scanner);
            }
        }
    }
    
    /**
     * Exibe as ações disponíveis baseado no estado atual
     */
    private void exibirAcoesDisponiveis(String estadoNome) {
        if (estadoNome.equals("Rascunho")) {
            System.out.println("│ [1] Enviar para Moderação              │");
            System.out.println("│ [2] Suspender Anúncio                  │");
        } else if (estadoNome.equals("Moderação")) {
            System.out.println("│ [1] Aprovar Anúncio                    │");
            System.out.println("│ [2] Reprovar Anúncio                   │");
            System.out.println("│ [3] Suspender Anúncio                  │");
        } else if (estadoNome.equals("Ativo")) {
            System.out.println("│ [1] Marcar como Vendido                │");
            System.out.println("│ [2] Suspender Anúncio                  │");
        } else if (estadoNome.equals("Suspenso")) {
            System.out.println("│ [1] Reativar (enviar para Moderação)   │");
        } else if (estadoNome.equals("Vendido")) {
            System.out.println("│ (Nenhuma ação disponível)              │");
        }
        
        System.out.println("│ [0] Voltar                             │");
        System.out.println("└────────────────────────────────────────┘");
    }
    
    /**
     * Executa ação baseada no estado atual e opção escolhida
     * Valida mudanças de estado e coordena notificações
     * 
     * @return true se a ação foi executada com sucesso
     */
    private boolean executarAcaoAnuncio(AnuncioFacade facade, Anuncio anuncio, int opcao, String estadoNome) {
        try {
            System.out.println("\n" + "═".repeat(42));
            
            // Executar transição conforme estado
            return executarTransicaoEstado(facade, anuncio, opcao, estadoNome);
            
        } catch (IllegalStateException e) {
            System.out.println("❌ Erro: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Executa a transição de estado conforme opção selecionada
     */
    private boolean executarTransicaoEstado(AnuncioFacade facade, Anuncio anuncio, int opcao, String estadoNome) {
        if (estadoNome.equals("Rascunho")) {
            return executarTransicaoRascunho(facade, anuncio, opcao);
        } else if (estadoNome.equals("Moderação")) {
            return executarTransicaoModeracao(facade, anuncio, opcao);
        } else if (estadoNome.equals("Ativo")) {
            return executarTransicaoAtivo(facade, anuncio, opcao);
        } else if (estadoNome.equals("Suspenso")) {
            return executarTransicaoSuspenso(facade, anuncio, opcao);
        } else if (estadoNome.equals("Vendido")) {
            System.out.println("❌ Opção inválida para o estado atual!");
            return false;
        }
        
        return false;
    }
    
    private boolean executarTransicaoRascunho(AnuncioFacade facade, Anuncio anuncio, int opcao) {
        if (opcao == 1) {
            System.out.println("📤 Enviando anúncio para moderação...\n");
            facade.enviarParaModeracao(anuncio);
            System.out.println("✅ Anúncio enviado para MODERAÇÃO");
            System.out.println("   📝 Observer registrando mudança em logs/sistema.log...");
            System.out.println("   Próxima etapa: Validação (Chain of Responsibility)");
            return true;
        } else if (opcao == 2) {
            System.out.println("⏸️  Suspendendo anúncio...\n");
            facade.suspender(anuncio);
            System.out.println("✅ Anúncio movido para SUSPENSO");
            return true;
        }
        return false;
    }
    
    private boolean executarTransicaoModeracao(AnuncioFacade facade, Anuncio anuncio, int opcao) {
        if (opcao == 1) {
            System.out.println("✅ Aprovando anúncio...\n");
            System.out.println("Executando Chain of Responsibility:");
            facade.aprovar(anuncio);
            System.out.println("   📝 Observer registrando mudança em logs/sistema.log...");
            return true;
        } else if (opcao == 2) {
            System.out.println("❌ Reprovando anúncio...\n");
            facade.reprovar(anuncio);
            System.out.println("✅ Anúncio movido para SUSPENSO");
            return true;
        } else if (opcao == 3) {
            System.out.println("⏸️  Suspendendo anúncio...\n");
            facade.suspender(anuncio);
            System.out.println("✅ Anúncio movido para SUSPENSO");
            return true;
        }
        return false;
    }
    
    private boolean executarTransicaoAtivo(AnuncioFacade facade, Anuncio anuncio, int opcao) {
        if (opcao == 1) {
            System.out.println("🎉 Marcando anúncio como vendido...\n");
            facade.vender(anuncio);
            System.out.println("✅ Anúncio movido para VENDIDO");
            return true;
        } else if (opcao == 2) {
            System.out.println("⏸️  Suspendendo anúncio...\n");
            facade.suspender(anuncio);
            System.out.println("✅ Anúncio movido para SUSPENSO");
            return true;
        }
        return false;
    }
    
    private boolean executarTransicaoSuspenso(AnuncioFacade facade, Anuncio anuncio, int opcao) {
        if (opcao == 1) {
            System.out.println("🔄 Reativando anúncio...\n");
            facade.reativar(anuncio);
            System.out.println("✅ Anúncio enviado para RASCUNHO");
            System.out.println("   Próxima etapa: Validação (Chain of Responsibility)");
            return true;
        }
        return false;
    }
    
    /**
     * Pausa a execução para o usuário ler a mensagem
     */
    private void pausar(Scanner scanner) {
        System.out.print("\n⏸️  Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}
