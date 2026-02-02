package com.myhome.service;

import java.util.List;
import java.util.Scanner;
import com.myhome.model.Anuncio;
import com.myhome.model.Apartamento;
import com.myhome.model.Casa;
import com.myhome.model.Imovel;
import com.myhome.model.SalaComercial;
import com.myhome.model.Terreno;

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
        System.out.println("║  3. Visualizar anúncios               ║");
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
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - ANÚNCIOS (Refatoração Facade)
    // ================================================================
    
    /**
     * Exibe menu de gerenciamento de anúncios com lista de anúncios.
     */
    public void exibirCabecalhoGerenciarAnuncios() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           GERENCIAR ANÚNCIOS             ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Exibe mensagem de nenhum anúncio criado.
     */
    public void exibirNenhumAnuncio() {
        System.out.println("📭 Nenhum anúncio criado ainda.");
        System.out.println("   Use a opção 1 do menu principal para criar seu primeiro anúncio!\n");
    }
    
    /**
     * Exibe total de anúncios.
     */
    public void exibirTotalAnuncios(int total) {
        System.out.println("📋 Total de anúncios: " + total + "\n");
    }
    
    /**
     * Exibe um item de anúncio na lista com informações resumidas.
     */
    public void exibirItemAnuncioLista(int numero, String titulo, double preco, 
                                       String tipo, String cidade, String estado, String status) {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│ [" + numero + "] " + (titulo.length() > 33 ? titulo.substring(0, 30) + "..." : titulo));
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ Preço: R$ " + String.format("%,.2f", preco));
        System.out.println("│ Tipo: " + tipo);
        System.out.println("│ Local: " + cidade + " - " + estado);
        System.out.println("│ Estado: " + status.toUpperCase());
        System.out.println("└────────────────────────────────────────┘");
    }
    
    /**
     * Exibe prompt para seleção de anúncio.
     */
    public void exibirPromptSelecaoAnuncio() {
        System.out.println("[0] Voltar ao menu principal");
        System.out.print("\n➤ Selecione um anúncio (número): ");
    }
    
    /**
     * Exibe mensagem de opção inválida.
     */
    public void exibirOpcaoInvalida() {
        System.out.println("\n❌ Opção inválida!");
    }
    
    /**
     * Exibe mensagem de número inválido.
     */
    public void exibirNumeroInvalido() {
        System.out.println("\n❌ Digite um número válido!");
    }
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - ANÚNCIO COMPLETO
    // ================================================================
    
    /**
     * Exibe um anúncio com todos os detalhes.
     */
    public void exibirAnuncioCompleto(String titulo, double preco, String descricao,
                                      String tipoImovel, String endereco, double area,
                                      String nomeAnunciante, String emailAnunciante,
                                      String telefoneAnunciante, int numeroAnuncio) {
        System.out.println("\n═════════════════════════════════════════");
        System.out.println("          ✅ ANÚNCIO CRIADO!");
        System.out.println("═════════════════════════════════════════\n");
        
        System.out.println("📌 DETALHES DO ANÚNCIO:");
        System.out.println("   Número......: " + numeroAnuncio);
        System.out.println("   Título......: " + titulo);
        System.out.println("   Preço.......: R$ " + String.format("%,.2f", preco));
        System.out.println("   Descrição...: " + descricao);
        
        System.out.println("\n🏠 DETALHES DO IMÓVEL:");
        System.out.println("   Tipo........: " + tipoImovel);
        System.out.println("   Endereço....: " + endereco);
        System.out.println("   Área........: " + area + " m²");
        
        System.out.println("\n👤 DADOS DO ANUNCIANTE:");
        System.out.println("   Nome........: " + nomeAnunciante);
        System.out.println("   Email.......: " + emailAnunciante);
        System.out.println("   Telefone....: " + telefoneAnunciante);
        
        System.out.println("\n═════════════════════════════════════════\n");
    }
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - CONFIGURAÇÕES
    // ================================================================
    
    /**
     * Exibe cabeçalho do menu de configurações.
     */
    public void exibirCabecalhoConfiguracoes() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         CONFIGURAÇÕES DO SISTEMA       ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Exibe menu de configurações com opções.
     */
    public void exibirOpcoesCofiguracoes() {
        System.out.println("📋 Configurações Disponíveis:");
        System.out.println("─".repeat(40));
        System.out.println("[1] Configurar Canal de Notificação (RF05)");
        System.out.println("[2] Informações do Sistema (RF07)");
        System.out.println("[0] Voltar");
        System.out.println("─".repeat(40));
        System.out.print("Escolha uma opção: ");
    }
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - LISTA COMPLETA DE ANÚNCIOS
    // ================================================================
    
    /**
     * Exibe cabeçalho para lista de anúncios.
     */
    public void exibirCabecalhoMeusAnuncios() {
        System.out.println("\n+============================================+");
        System.out.println("|                   ANÚNCIOS                    |");
        System.out.println("+============================================+\n");
    }
    
    /**
     * Exibe lista completa de anúncios com todos os detalhes.
     */
    public void exibirListaAnunciosCompleta(List<Anuncio> anuncios) {
        if (anuncios == null || anuncios.isEmpty()) {
            return;
        }
        
        System.out.println("  >> Total de anuncios: " + anuncios.size() + "\n");
        
        for (int i = 0; i < anuncios.size(); i++) {
            Anuncio anuncio = anuncios.get(i);
            exibirDetalheAnuncioListagem(i + 1, anuncio);
        }
    }
    
    /**
     * Exibe detalhes de um anúncio específico na listagem.
     */
    private void exibirDetalheAnuncioListagem(int numero, Anuncio anuncio) {
        Imovel imovel = anuncio.getImovel();
        
        System.out.println("+--------------------------------------------+");
        System.out.println("|  ANUNCIO #" + numero + "                                 |");
        System.out.println("+--------------------------------------------+");
        System.out.println("  Titulo.....: " + anuncio.getTitulo());
        System.out.println("  Preco......: R$ " + String.format("%,.2f", anuncio.getPreco()));
        System.out.println("  Descricao..: " + anuncio.getDescricao());
        System.out.println();
        System.out.println("  [IMOVEL]");
        System.out.println("  Tipo.......: " + imovel.getTipo().toUpperCase());
        System.out.println("  Area.......: " + imovel.getArea() + " m2");
        System.out.println("  Endereco...: " + imovel.getEndereco());
        
        exibirDetalhesTipoImovel(imovel);
        
        System.out.println();
        System.out.println();
        System.out.println("  [ANUNCIANTE]");
        System.out.println("  Nome.......: " + anuncio.getAnunciante().getNome());
        System.out.println("  Email......: " + anuncio.getAnunciante().getEmail());
        System.out.println("  Telefone...: " + anuncio.getAnunciante().getTelefone());
        System.out.println("  Estado......: " + anuncio.getState().getNome().toUpperCase());
        System.out.println("+--------------------------------------------+\n");
    }
    
    /**
     * Exibe detalhes específicos do tipo de imóvel.
     */
    private void exibirDetalhesTipoImovel(Imovel imovel) {
        if (imovel instanceof Casa) {
            Casa casa = (Casa) imovel;
            System.out.println("  Quartos....: " + casa.getQuartos());
            System.out.println("  Banheiros..: " + casa.getBanheiros());
            System.out.println("  Quintal....: " + (casa.isTemQuintal() ? "Sim" : "Nao"));
            System.out.println("  Garagem....: " + (casa.isTemGaragem() ? "Sim" : "Nao"));
        } else if (imovel instanceof Apartamento) {
            Apartamento apt = (Apartamento) imovel;
            System.out.println("  Quartos....: " + apt.getQuartos());
            System.out.println("  Banheiros..: " + apt.getBanheiros());
            System.out.println("  Andar......: " + apt.getAndar());
            System.out.println("  Vagas......: " + apt.getVagas());
            System.out.println("  Elevador...: " + (apt.isTemElevador() ? "Sim" : "Nao"));
        } else if (imovel instanceof SalaComercial) {
            SalaComercial sala = (SalaComercial) imovel;
            System.out.println("  Andar......: " + sala.getAndar());
            System.out.println("  Banheiro...: " + (sala.isTemBanheiro() ? "Sim" : "Nao"));
            System.out.println("  Vagas......: " + sala.getVagasEstacionamento());
            System.out.println("  Capacidade.: " + sala.getCapacidadePessoas() + " pessoas");
        } else if (imovel instanceof Terreno) {
            Terreno terreno = (Terreno) imovel;
            if (terreno.getZoneamento() != null) {
                System.out.println("  Zoneamento.: " + terreno.getZoneamento());
            }
            if (terreno.getTopografia() != null) {
                System.out.println("  Topografia.: " + terreno.getTopografia());
            }
        }
    }
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - GERENCIAMENTO DE ANÚNCIOS
    // ================================================================
    
    /**
     * Exibe cabeçalho para gerenciar anúncios.
     */
    public void exibirCabecalhoGerenciarAnunciosMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║             GERENCIAR ANÚNCIOS           ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Exibe lista de anúncios resumida para seleção (gerenciamento).
     */
    public void exibirListaAnunciosParaSelecao(List<Anuncio> anuncios) {
        if (anuncios == null || anuncios.isEmpty()) {
            return;
        }
        
        System.out.println("📋 Total de anúncios: " + anuncios.size() + "\n");
        
        for (int i = 0; i < anuncios.size(); i++) {
            Anuncio anuncio = anuncios.get(i);
            Imovel imovel = anuncio.getImovel();
            System.out.println("┌────────────────────────────────────────┐");
            System.out.println("│ [" + (i + 1) + "] " + anuncio.getTitulo());
            System.out.println("├────────────────────────────────────────┤");
            System.out.println("│ Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
            System.out.println("│ Tipo: " + imovel.getTipo());
            System.out.println("│ Local: " + imovel.getEndereco().getCidade() + " - " + imovel.getEndereco().getEstado());
            System.out.println("│ Estado: " + anuncio.getState().getNome().toUpperCase());
            System.out.println("└────────────────────────────────────────┘");
        }
    }
    
    /**
     * Exibe prompt de seleção para gerenciamento.
     */
    public void exibirPromptSelecaoAnuncioGerenciar() {
        System.out.println("\n[0] Voltar ao menu principal");
        System.out.print("\n➤ Selecione um anúncio (número): ");
    }
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - CRIAR ANÚNCIO INTERATIVO
    // ================================================================
    
    /**
     * Exibe cabeçalho para criar novo anúncio interativamente.
     */
    public void exibirCabecalhoCriarAnuncioInterativo() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       CRIAR NOVO ANÚNCIO               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Exibe mensagem de cancelamento de criação de imóvel.
     */
    public void exibirCancelamentoCriacaoImovel() {
        System.out.println("\n❌ Criação de imóvel cancelada.\n");
    }
    
    /**
     * Exibe mensagem de cancelamento de criação de anúncio.
     */
    public void exibirCancelamentoCriacaoAnuncio() {
        System.out.println("\n❌ Criação de anúncio cancelada.\n");
    }
    
    /**
     * Exibe sucesso na criação de imóvel com detalhes.
     */
    public void exibirSucessoCriacaoImovelInterativo(String tipo, String endereco, double area) {
        System.out.println("\n✅ Imóvel criado com sucesso!");
        System.out.println("   Tipo: " + tipo);
        System.out.println("   Endereço: " + endereco);
        System.out.println("   Área: " + area + "m²");
    }
    
    /**
     * Exibe erro genérico na criação de anúncio.
     */
    public void exibirErroCriacaoAnuncio(String mensagem) {
        System.out.println("\n❌ Erro ao criar anúncio: " + mensagem + "\n");
    }
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - CRIAR ANÚNCIO DE PROTÓTIPO
    // ================================================================
    
    /**
     * Exibe cabeçalho para criar anúncio de protótipo.
     */
    public void exibirCabecalhoCriarAnuncioPrototipo() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   CRIAR ANÚNCIO DE PROTÓTIPO          ║");
        System.out.println("║   (Padrão Prototype - RF02)            ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Exibe cabeçalho de protótipos disponíveis.
     */
    public void exibirCabecalhoPrototiposDisponiveis() {
        System.out.println("🏘️  Protótipos Disponíveis:\n");
    }
    
    /**
     * Exibe um protótipo na lista.
     */
    public void exibirItemPrototipoLista(int numero, String descricao) {
        System.out.println("  [" + numero + "] " + descricao);
    }
    
    /**
     * Exibe prompt para seleção de protótipo.
     */
    public void exibirPromptSelecaoPrototipo() {
        System.out.print("\n➤ Escolha o protótipo: ");
    }
    
    /**
     * Exibe cabeçalho de sucesso ao clonar imóvel.
     */
    public void exibirCabecalhoImovelClonado() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│  PASSO 1: IMÓVEL CLONADO COM SUCESSO   │");
        System.out.println("└────────────────────────────────────────┘\n");
    }
    
    /**
     * Exibe detalhes do clone (descrição e hashcode).
     */
    public void exibirDetalhesClonagemImovel(String descricao, int hashcode) {
        System.out.println("✅ Imóvel clonado: " + descricao);
        System.out.println("   Hash do clone: " + hashcode);
        System.out.println("   (objeto independente pronto para customização)\n");
    }
    
    /**
     * Exibe mensagem de validação bem-sucedida.
     */
    public void exibirSucessoValidacaoImovel() {
        System.out.println("\n✅ Imóvel validado com sucesso!");
    }
    
    /**
     * Exibe erro de validação de imóvel.
     */
    public void exibirErroValidacaoImovel() {
        System.out.println("\n❌ Imóvel inválido após customização!");
        System.out.println("   Verifique os dados informados.\n");
    }
    
    /**
     * Exibe erro de protótipo não encontrado.
     */
    public void exibirErroPrototipoNaoEncontrado() {
        System.out.println("\n❌ Protótipo não encontrado!");
    }
    
    /**
     * Exibe erro de entrada inválida (número).
     */
    public void exibirErroEntradaInvalidaNumero() {
        System.out.println("\n❌ Entrada inválida! Digite um número.");
    }
    
    // ================================================================
    // MÉTODOS DE APRESENTAÇÃO - RESULTADO FINAL ANÚNCIO
    // ================================================================
    
    /**
     * Exibe resultado completo do anúncio criado.
     */
    public void exibirResultadoAnuncioCriadoCompleto(int numeroAnuncio, String titulo, double preco, 
                                                     String descricao, String tipo, String endereco, 
                                                     double area, String nomeAnunciante, 
                                                     String emailAnunciante, String telefoneAnunciante) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     ✅ ANÚNCIO CRIADO COM SUCESSO!     ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.println("\n📋 ANÚNCIO #" + numeroAnuncio);
        System.out.println("─".repeat(40));
        System.out.println("🏷️  Título: " + titulo);
        System.out.println("💰 Preço: R$ " + String.format("%,.2f", preco));
        System.out.println("📄 Descrição: " + descricao);
        
        System.out.println("\n🏘️  IMÓVEL:");
        System.out.println("   Tipo: " + tipo);
        System.out.println("   Endereço: " + endereco);
        System.out.println("   Área: " + area + "m²");
        
        System.out.println("\n👤 ANUNCIANTE:");
        System.out.println("   Nome: " + nomeAnunciante);
        System.out.println("   Email: " + emailAnunciante);
        System.out.println("   Telefone: " + telefoneAnunciante);
        
        System.out.println("\n💡 Status: RASCUNHO (pronto para publicação)");
        System.out.println("═".repeat(40) + "\n");
    }
    
    /**
     * Exibe mensagem de nenhum anúncio na exibição de anúncios.
     */
    public void exibirNenhumAnuncioMeusList() {
        System.out.println("  >> Nenhum anuncio criado ainda.");
        System.out.println("  >> Use a opcao 1 para criar seu primeiro anuncio!\n");
    }
}
