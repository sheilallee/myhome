package com.myhome.service;

import com.myhome.factory.*;
import com.myhome.model.*;
import com.myhome.observer.LogObserver;
import com.myhome.observer.NotificationObserver;
import com.myhome.strategy.NotificationManager;
import java.util.Scanner;

/**
 * SERVIÇO DE GERENCIAMENTO DE ANÚNCIOS
 * 
 * RESPONSABILIDADE:
 * - Encapsular a lógica de criação de anúncios usando Factory Method Pattern
 * - Garantir configuração automática de observers (RF04)
 * - Fornecer interface de alto nível para criação de anúncios
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por gerenciar anúncios
 * - DIP: Cliente depende desta interface, não do Builder concreto
 * - ISP: Interface coesa com métodos específicos para cada tipo de anúncio
 */
public class AnuncioService {
    
    private final MenuService menuService;
    private final ValidadorService validadorService;
    private final UsuarioService usuarioService;
    
    public AnuncioService(MenuService menuService, ValidadorService validadorService, UsuarioService usuarioService) {
        this.menuService = menuService;
        this.validadorService = validadorService;
        this.usuarioService = usuarioService;
    }

    // =====================================================
    // CONFIGURAÇÃO DE OBSERVERS (RF04)
    // =====================================================

    private void configurarObservers(Anuncio anuncio) {
        LoggerService logger = new LoggerService();
        NotificationManager manager = new NotificationManager();

        anuncio.adicionarObserver(new LogObserver(logger));
        anuncio.adicionarObserver(new NotificationObserver(manager));
    }
    
    /**
     * Cria um anúncio interativo através da linha de comando usando Factory Method.
     */
    public Anuncio criarAnuncioInterativo(Scanner scanner, Imovel imovel) {
        menuService.exibirPasso("PASSO 2: CRIAR ANÚNCIO (FACTORY)");
        
        // Escolher tipo de anúncio
        System.out.println("🏷️  Tipo de Anúncio:");
        System.out.println("  [1] Venda");
        System.out.println("  [2] Aluguel");
        System.out.println("  [3] Temporada");
        
        int tipoAnuncio = menuService.lerOpcao("➤ Escolha: ");
        
        // Dados do anúncio
        String titulo = menuService.lerTexto("\n📝 Título do anúncio: ");
        double preco = menuService.lerDecimal("💰 Preço (R$): ");
        String descricao = menuService.lerTexto("📄 Descrição: ");
        
        // Validar dados básicos
        if (!validadorService.validarTextoNaoVazio(titulo) || 
            !validadorService.validarNumeroPositivo(preco)) {
            menuService.exibirErro("Dados inválidos!");
            return null;
        }
        
        // Criar usuário anunciante
        Usuario anunciante = criarUsuarioAnunciante(scanner);
        if (anunciante == null) {
            return null;
        }
        
        // FACTORY METHOD - Criar anúncio
        AnuncioFactory factory = selecionarFactory(tipoAnuncio);
        if (factory == null) {
            menuService.exibirErro("Tipo de anúncio inválido!");
            return null;
        }
        
        Anuncio anuncio = factory.criarAnuncio(titulo, preco, descricao, imovel, anunciante);
        configurarObservers(anuncio);
        return anuncio;
    }
    
    /**
     * Cria o usuário anunciante com validações.
     */
    private Usuario criarUsuarioAnunciante(Scanner scanner) {
        String nome = menuService.lerTexto("\n👤 Seu nome: ");
        
        // Validação de email
        String email;
        while (true) {
            email = menuService.lerTexto("📧 Seu email: ");
            if (validadorService.validarEmail(email)) {
                break;
            }
            menuService.exibirErro("Email inválido! Use o formato: exemplo@dominio.com");
        }
        
        // Validação e formatação de telefone
        String telefone;
        while (true) {
            String input = menuService.lerTexto("📱 Seu telefone (apenas números): ");
            telefone = validadorService.formatarTelefone(input);
            if (telefone != null) {
                menuService.exibirSucesso("Telefone formatado: " + telefone);
                break;
            }
            menuService.exibirErro("Telefone inválido! Digite 10 ou 11 dígitos (ex: 83988881111)");
        }
        
        return usuarioService.criarProprietario(nome, email, telefone);
    }
    
    /**
     * Seleciona a factory apropriada.
     */
    private AnuncioFactory selecionarFactory(int tipo) {
        switch (tipo) {
            case 1: return new VendaFactory();
            case 2: return new AluguelFactory();
            case 3: return new TemporadaFactory();
            default: return null;
        }
    }
    
    /**
     * Exibe lista de anúncios formatada.
     */
    public void exibirAnuncios(java.util.List<Anuncio> anuncios) {
        if (anuncios.isEmpty()) {
            System.out.println("📂 Nenhum anúncio criado ainda.");
            System.out.println("💡 Use a opção 1 para criar seu primeiro anúncio!\n");
            return;
        }
        
        System.out.println("📊 Total de anúncios: " + anuncios.size() + "\n");
        
        for (int i = 0; i < anuncios.size(); i++) {
            exibirAnuncioDetalhado(anuncios.get(i), i + 1);
        }
    }
    
    /**
     * Exibe um anúncio detalhado.
     */
    private void exibirAnuncioDetalhado(Anuncio anuncio, int numero) {
        Imovel imovel = anuncio.getImovel();
        
        System.out.println("═".repeat(45));
        System.out.println("📋 ANÚNCIO #" + numero);
        System.out.println("─".repeat(45));
        System.out.println("🏷️  Título: " + anuncio.getTitulo());
        System.out.println("💰 Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
        System.out.println("📝 Descrição: " + anuncio.getDescricao());
        
        System.out.println("\n🏘️  IMÓVEL:");
        System.out.println("   Tipo: " + imovel.getTipo());
        System.out.println("   Área: " + imovel.getArea() + " m²");
        System.out.println("   📍 Endereço: " + imovel.getEndereco());
        
        // Detalhes específicos por tipo
        exibirDetalhesImovel(imovel);
        
        System.out.println("═".repeat(45) + "\n");
    }
    
    /**
     * Exibe detalhes específicos do tipo de imóvel.
     */
    private void exibirDetalhesImovel(Imovel imovel) {
        if (imovel instanceof Casa) {
            Casa casa = (Casa) imovel;
            System.out.println("   🛏️  Quartos: " + casa.getQuartos());
            System.out.println("   🚿 Banheiros: " + casa.getBanheiros());
            System.out.println("   🌳 Quintal: " + (casa.isTemQuintal() ? "Sim" : "Não"));
            System.out.println("   🚗 Garagem: " + (casa.isTemGaragem() ? "Sim" : "Não"));
        } else if (imovel instanceof Apartamento) {
            Apartamento apt = (Apartamento) imovel;
            System.out.println("   🛏️  Quartos: " + apt.getQuartos());
            System.out.println("   🚿 Banheiros: " + apt.getBanheiros());
            System.out.println("   🏢 Andar: " + apt.getAndar());
            System.out.println("   🚗 Vagas: " + apt.getVagas());
        } else if (imovel instanceof SalaComercial) {
            SalaComercial sala = (SalaComercial) imovel;
            System.out.println("   🏢 Andar: " + sala.getAndar());
            System.out.println("   🚿 Banheiro: " + (sala.isTemBanheiro() ? "Sim" : "Não"));
            System.out.println("   🚗 Vagas: " + sala.getVagasEstacionamento());
        }
    }
    
    /**
     * Exibe resultado do anúncio criado.
     */
    public void exibirResultadoAnuncio(Anuncio anuncio, int numero) {
        Imovel imovel = anuncio.getImovel();
        Usuario anunciante = anuncio.getAnunciante();
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     ✅ ANÚNCIO CRIADO COM SUCESSO!     ║");
        System.out.println("╚════════════════════════════════════════╝");
        
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
}
