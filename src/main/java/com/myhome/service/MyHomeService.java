package com.myhome.service;

import com.myhome.facade.ConfigFacade;
import com.myhome.model.*;

/**
 * SERVIÇO PRINCIPAL DA APLICAÇÃO MYHOME
 * 
 * PADRÃO FACADE DE ALTO NÍVEL:
 * - Encapsula TODOS os serviços da aplicação
 * - Fornece interface única e simplificada para o cliente
 * - O Main só precisa conhecer esta classe
 * 
 * RESPONSABILIDADE:
 * - Coordenar operações entre diferentes serviços
 * - Fornecer API de alto nível para casos de uso completos
 * - Ocultar complexidade interna do sistema
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Coordena serviços, mas não implementa lógica de negócio
 * - DIP: Main depende desta abstração, não de implementações concretas
 * - OCP: Novos serviços podem ser adicionados sem quebrar o cliente
 * - LSP: Pode ser substituído por implementações alternativas
 */
public class MyHomeService {
    
    // Serviços especializados (encapsulados)
    private final ConfigFacade configFacade;
    private final ImovelService imovelService;
    private final AnuncioService anuncioService;
    private final UsuarioService usuarioService;
    
    /**
     * Construtor inicializa todos os serviços necessários.
     * O cliente não precisa saber sobre essas dependências.
     */
    public MyHomeService() {
        this.configFacade = new ConfigFacade();
        this.imovelService = new ImovelService();
        this.anuncioService = new AnuncioService();
        this.usuarioService = new UsuarioService();
    }
    
    // =================================================================
    // OPERAÇÕES DE NEGÓCIO DE ALTO NÍVEL
    // =================================================================
    
    /**
     * Cria um anúncio completo de casa.
     * Este é um caso de uso completo que coordena vários serviços.
     */
    public Anuncio anunciarCasa(String tituloAnuncio, double preco, String descricao,
                               String endereco, double area, int quartos, int banheiros,
                               String nomeAnunciante, String emailAnunciante, 
                               String telefoneAnunciante, String[] fotos) {
        
        // 1. Criar usuário anunciante
        Usuario anunciante = usuarioService.criarProprietario(
            nomeAnunciante, emailAnunciante, telefoneAnunciante);
        
        // 2. Criar imóvel (Casa)
        Casa casa = imovelService.criarCasa(endereco, area, quartos, banheiros, 
                                           true, true, 2);
        
        // 3. Criar anúncio
        return anuncioService.criarAnuncioCompleto(
            tituloAnuncio, preco, descricao, casa, anunciante, fotos);
    }
    
    /**
     * Cria um anúncio completo de apartamento.
     */
    public Anuncio anunciarApartamento(String tituloAnuncio, double preco, String descricao,
                                      String endereco, double area, int quartos, int banheiros,
                                      int andar, String nomeCorretor, String emailCorretor,
                                      String telefoneCorretor, String[] fotos) {
        
        Usuario corretor = usuarioService.criarCorretor(
            nomeCorretor, emailCorretor, telefoneCorretor);
        
        Apartamento apartamento = imovelService.criarApartamento(
            endereco, area, quartos, banheiros, andar, andar > 2, 1);
        
        return anuncioService.criarAnuncioCompleto(
            tituloAnuncio, preco, descricao, apartamento, corretor, fotos);
    }
    
    /**
     * Cria um anúncio de terreno.
     */
    public Anuncio anunciarTerreno(String tituloAnuncio, double preco,
                                  String endereco, double area, String zoneamento,
                                  String nomeAnunciante, String emailAnunciante,
                                  String telefoneAnunciante) {
        
        Usuario anunciante = usuarioService.criarProprietario(
            nomeAnunciante, emailAnunciante, telefoneAnunciante);
        
        Terreno terreno = imovelService.criarTerrenoBasico(endereco, area, zoneamento);
        
        return anuncioService.criarAnuncioSimples(tituloAnuncio, preco, terreno, anunciante);
    }
    
    // =================================================================
    // ACESSO A CONFIGURAÇÕES (via Facade)
    // =================================================================
    
    /**
     * Obtém a taxa de comissão configurada.
     */
    public double getTaxaComissao() {
        return configFacade.getTaxaComissao();
    }
    
    /**
     * Obtém o limite de fotos por anúncio.
     */
    public int getLimiteFotos() {
        return configFacade.getLimiteUploadFotos();
    }
    
    /**
     * Obtém o preço mínimo para anúncios.
     */
    public double getPrecoMinimo() {
        return configFacade.getPrecoMinimoAnuncio();
    }
    
    /**
     * Obtém o preço máximo para anúncios.
     */
    public double getPrecoMaximo() {
        return configFacade.getPrecoMaximoAnuncio();
    }
    
    // =================================================================
    // MÉTODOS PARA DEMONSTRAÇÃO (podem ser removidos em produção)
    // =================================================================
    
    /**
     * Demonstra o uso dos padrões Singleton + Facade.
     */
    public void demonstrarSingletonFacade() {
        System.out.println(">>> RF07 - SINGLETON + FACADE <<<");
        System.out.println();
        System.out.println("Configurações do sistema:");
        System.out.println("- Taxa de comissão: " + getTaxaComissao());
        System.out.println("- Limite de fotos: " + getLimiteFotos());
        System.out.println("- Preço mínimo: R$ " + getPrecoMinimo());
        System.out.println("- Preço máximo: R$ " + getPrecoMaximo());
        System.out.println("✓ Singleton e Facade funcionando corretamente");
    }
    
    /**
     * Demonstra o uso dos padrões Factory Method + Builder.
     */
    public void demonstrarFactoryBuilder() {
        System.out.println("\n>>> RF01 - FACTORY METHOD + BUILDER <<<");
        System.out.println();
        
        // Criar diferentes tipos de imóveis
        System.out.println("Criando imóveis com Factory Method:");
        Casa casa = imovelService.criarCasaBasica("Rua das Flores, 123", 120.0);
        Apartamento ap = imovelService.criarApartamentoBasico("Av. Boa Viagem, 456", 80.0, 5);
        Terreno terreno = imovelService.criarTerrenoBasico("Rua do Futuro, 789", 300.0, "Residencial");
        
        System.out.println("✓ Casa criada: " + casa);
        System.out.println("✓ Apartamento criado: " + ap);
        System.out.println("✓ Terreno criado: " + terreno);
        System.out.println();
        
        // Criar anúncios com Builder
        System.out.println("Criando anúncios com Builder Pattern:");
        Usuario usuario = usuarioService.criarProprietario("João Silva", "joao@email.com", "81999999999");
        
        Anuncio anuncio = anuncioService.criarAnuncioCompleto(
            "Linda Casa em Boa Viagem",
            850000.00,
            "Casa espaçosa com quintal e garagem",
            casa,
            usuario,
            new String[]{"foto1.jpg", "foto2.jpg", "foto3.jpg"}
        );
        
        System.out.println("✓ Anúncio criado: " + anuncio.getTitulo());
        System.out.println("  ID: " + anuncio.getId());
        System.out.println("  Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
        System.out.println("  Fotos: " + anuncio.getQuantidadeFotos());
    }
    
    /**
     * Demonstra um caso de uso completo: criar anúncio do início ao fim.
     */
    public void demonstrarCasoDeUsoCompleto() {
        System.out.println("\n>>> CASO DE USO COMPLETO <<<");
        System.out.println();
        System.out.println("Criando anúncio de apartamento de alto padrão...");
        
        Anuncio anuncio = anunciarApartamento(
            "Apartamento de Alto Padrão com Vista Mar",
            1200000.00,
            "Apartamento luxuoso de 95m², 3 quartos (1 suíte), vista para o mar",
            "Av. Boa Viagem, 1000 - Boa Viagem",
            95.0,
            3,
            2,
            10,
            "Maria Santos (Corretora)",
            "maria@imobiliaria.com",
            "81988887777",
            new String[]{
                "sala.jpg", "quarto1.jpg", "quarto2.jpg", "cozinha.jpg", "vista.jpg"
            }
        );
        
        System.out.println("\n✓ ANÚNCIO CRIADO COM SUCESSO!");
        System.out.println("-".repeat(60));
        System.out.println("ID: " + anuncio.getId());
        System.out.println("Título: " + anuncio.getTitulo());
        System.out.println("Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
        System.out.println("Tipo: " + anuncio.getImovel().getTipo());
        System.out.println("Área: " + anuncio.getImovel().getArea() + "m²");
        System.out.println("Anunciante: " + anuncio.getAnunciante().getNome());
        System.out.println("Fotos: " + anuncio.getQuantidadeFotos());
        System.out.println("-".repeat(60));
    }
}
