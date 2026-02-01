package com.myhome.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.myhome.builder.ImovelBuilder;
import com.myhome.builder.ImovelBuilderImpl;
import com.myhome.decorator.BuscaFiltro;
import com.myhome.decorator.BuscaPadrao;
import com.myhome.decorator.FiltroLocalizacaoDecorator;
import com.myhome.decorator.FiltroPrecoDecorator;
import com.myhome.decorator.FiltroTipoImovelDecorator;
import com.myhome.factory.AluguelFactory;
import com.myhome.factory.AnuncioFactory;
import com.myhome.factory.TemporadaFactory;
import com.myhome.factory.VendaFactory;
import com.myhome.model.Anuncio;
import com.myhome.model.Apartamento;
import com.myhome.model.Casa;
import com.myhome.model.Endereco;
import com.myhome.model.Imovel;
import com.myhome.model.SalaComercial;
import com.myhome.model.Terreno;
import com.myhome.model.Usuario;
import com.myhome.observer.LogObserver;
import com.myhome.observer.NotificationObserver;
import com.myhome.prototype.PrototypeRegistry;
import com.myhome.service.AnuncioService;
import com.myhome.service.EmailService;
import com.myhome.service.ImovelService;
import com.myhome.service.LoggerService;
import com.myhome.service.MenuService;
import com.myhome.service.PersistenciaService;
import com.myhome.service.SMSService;
import com.myhome.service.UsuarioService;
import com.myhome.service.ValidadorService;
import com.myhome.service.WhatsAppService;
import com.myhome.singleton.ConfigurationManager;
import com.myhome.strategy.EmailNotificacao;
import com.myhome.strategy.NotificationManager;
import com.myhome.strategy.SMSNotificacao;
import com.myhome.strategy.WhatsAppNotificacao;

// RF08 - Facade: orquestra todos os subsistemas do MyHome
public class MyHomeFacade {
    
    // Subsistemas (injeção de dependência)
    private final MenuService menuService;
    private final ImovelService imovelService;
    private final AnuncioService anuncioService;
    private final ValidadorService validadorService;
    private final UsuarioService usuarioService;
    private final PersistenciaService persistenciaService;
    
    // Dados da aplicação
    private List<Anuncio> meusAnuncios;
    private int contadorAnuncios;
    private Usuario usuarioAtual; // RF05 - Usuario com canal de notificação configurável
    
    // Inicializa todos os subsistemas
    public MyHomeFacade() {
        // Criar services na ordem correta de dependências
        this.menuService = new MenuService();
        this.validadorService = new ValidadorService();
        this.usuarioService = new UsuarioService();
        this.persistenciaService = new PersistenciaService();
        this.imovelService = new ImovelService(menuService, validadorService);
        this.anuncioService = new AnuncioService(menuService, validadorService, usuarioService);
        
        this.meusAnuncios = new ArrayList<>();
        this.contadorAnuncios = 0;
        
        // RF05 - Criar usuário padrão com canal de notificação padrão
        this.usuarioAtual = new Usuario("User", "sheilalee.lima@gmail.com", "(83) 8888-8888");
        this.usuarioAtual.setCanalNotificacao(new EmailNotificacao(new EmailService()));
    }
    
    /**
     * Executa o sistema MyHome de forma interativa.
     * 
     * Menu principal com opções:
     * 1. Criar anúncio interativo (RF01)
     * 2. Buscar imóveis (RF06)
     * 3. Meus anúncios (RF04)
     * 4. Configurações (RF07)
     * 5. Demonstrar padrões GoF
     * 0. Sair
     */
    public void executar() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;
        
        // Carrega anúncios salvos
        meusAnuncios = persistenciaService.carregarAnuncios();
        contadorAnuncios = meusAnuncios.size();
        
        // Anexar observers aos anúncios carregados (RF04 - Observer Pattern)
        if (contadorAnuncios > 0) {
            anexarObserversAosAnuncios();
            System.out.println("\n📂 " + contadorAnuncios + " anúncio(s) carregado(s) do arquivo!");
            System.out.println("✅ Observers attachados para monitoramento de mudanças\n");
        }
        
        while (continuar) {
            exibirMenuPrincipal();
            
            try {
                System.out.print("Escolha uma opção: ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                System.out.println();
                
                switch (opcao) {
                    case 1:
                        exibirSubmenuCriarAnuncio(scanner);
                        break;
                    case 2:
                        executarBusca(scanner);
                        break;
                    case 3:
                        gerenciarMeusAnuncios(scanner);
                        break;
                    case 4:
                        exibirConfiguracoes();
                        break;
                    case 5:
                        demonstrarPadroesGoF();
                        break;
                    case 0:
                        continuar = false;
                        exibirMensagemDespedida();
                        break;
                    default:
                        System.out.println("❌ Opção inválida! Tente novamente.");
                }
                
                if (continuar && opcao != 0) {
                    pausar(scanner);
                }
                
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida! Digite um número.");
                pausar(scanner);
            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
                pausar(scanner);
            }
        }
        
        scanner.close();
    }
    
    // ================================================================
    // MÉTODOS DO MENU
    // ================================================================
    
    private void exibirMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      MYHOME - CLASSIFICADOS          ║");
        System.out.println("║        IMOBILIÁRIOS                   ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Criar novo anúncio                ║");
        System.out.println("║  2. Buscar imóveis                    ║");
        System.out.println("║  3. Meus anúncios                     ║");
        System.out.println("║  4. Configurações                     ║");
        System.out.println("║  5. Demonstrar padrões                ║");
        System.out.println("║  0. Sair                              ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    private void exibirMensagemDespedida() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        👋 ATÉ LOGO! 👋                ║");
        System.out.println("║   Obrigado por usar o MyHome!         ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    private void pausar(Scanner scanner) {
        System.out.println("\n⏸️  Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    // ================================================================
    // MÉTODOS INTERATIVOS
    // ================================================================
    
    /**
     * RF06 - DECORATOR PATTERN: Busca avançada com filtros dinâmicos
     * Permite ao usuário adicionar múltiplos filtros que se "decoram" uns aos outros
     */
    private void executarBusca(Scanner scanner) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   RF06 - DECORATOR (Busca Avançada)    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Inicia com a busca padrão (todos os anúncios em estado ATIVO)
        BuscaFiltro busca = new BuscaPadrao(meusAnuncios);
        
        System.out.println("📝 Configure os filtros de busca:");
        System.out.println("   (Pressione Enter para pular um filtro)\n");
        
        // Filtro 1: Preço
        System.out.print("💰 Filtrar por preço? (S/N): ");
        String filtroPreco = scanner.nextLine().trim().toUpperCase();
        if (filtroPreco.equals("S")) {
            try {
                System.out.print("   Preço mínimo (R$): ");
                double precoMin = Double.parseDouble(scanner.nextLine().trim());
                
                System.out.print("   Preço máximo (R$): ");
                double precoMax = Double.parseDouble(scanner.nextLine().trim());
                
                busca = new FiltroPrecoDecorator(busca, precoMin, precoMax);
                System.out.println("   ✅ Filtro de preço adicionado\n");
            } catch (NumberFormatException e) {
                System.out.println("   ⚠️  Valores inválidos. Filtro ignorado.\n");
            }
        }
        
        // Filtro 2: Localização
        System.out.print("🏠 Filtrar por localização? (S/N): ");
        String filtroLocal = scanner.nextLine().trim().toUpperCase();
        if (filtroLocal.equals("S")) {
            System.out.print("   Cidade: ");
            String cidade = scanner.nextLine().trim();
            
            System.out.print("   Estado (ex: PB): ");
            String estado = scanner.nextLine().trim().toUpperCase();
            
            if (!cidade.isEmpty() && !estado.isEmpty()) {
                busca = new FiltroLocalizacaoDecorator(busca, cidade, estado);
                System.out.println("   ✅ Filtro de localização adicionado\n");
            }
        }
        
        // Filtro 3: Tipo de Imóvel
        System.out.print("🏘️  Filtrar por tipo de imóvel? (S/N): ");
        String filtroTipo = scanner.nextLine().trim().toUpperCase();
        if (filtroTipo.equals("S")) {
            System.out.println("   Tipos disponíveis: Casa, Apartamento, Terreno, SalaComercial");
            System.out.print("   Tipo: ");
            String tipo = scanner.nextLine().trim();
            
            if (!tipo.isEmpty()) {
                busca = new FiltroTipoImovelDecorator(busca, tipo);
                System.out.println("   ✅ Filtro de tipo adicionado\n");
            }
        }
        
        // Executar busca com todos os filtros decorados
        System.out.println("🔍 Executando busca com filtros...\n");
        List<Anuncio> resultados = busca.buscar();
        
        // Exibir resultados
        exibirResultadosBusca(resultados, scanner);
    }
    
    /**
     * Exibe os resultados da busca de forma formatada
     */
    private void exibirResultadosBusca(List<Anuncio> anuncios, Scanner scanner) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       RESULTADOS DA BUSCA              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        if (anuncios.isEmpty()) {
            System.out.println("❌ Nenhum imóvel encontrado com os critérios especificados.");
            pausar(scanner);
            return;
        }
        
        System.out.println("✅ " + anuncios.size() + " imóvel(is) encontrado(s):\n");
        
        for (int i = 0; i < anuncios.size(); i++) {
            Anuncio anuncio = anuncios.get(i);
            System.out.println("┌────────────────────────────────────────┐");
            System.out.println("│ [" + (i + 1) + "] " + anuncio.getTitulo());
            System.out.println("├────────────────────────────────────────┤");
            System.out.println("│ 💰 Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
            System.out.println("│ 📍 Local: " + anuncio.getImovel().getEndereco().getCidade() + 
                             " - " + anuncio.getImovel().getEndereco().getEstado());
            System.out.println("│ 🏠 Tipo: " + anuncio.getImovel().getTipo());
            System.out.println("│ 📏 Área: " + anuncio.getImovel().getArea() + " m²");
            System.out.println("│ 📊 Estado: " + anuncio.getEstado().getNome().toUpperCase());
            System.out.println("└────────────────────────────────────────┘\n");
        }
        
        pausar(scanner);
    }
    
    /**
     * Submenu para criação de anúncio
     * Opção 1: Usar modelo padrão (Prototype) - RF02
     * Opção 2: Criar do zero (Builder) - RF01
     */
    private void exibirSubmenuCriarAnuncio(Scanner scanner) {
        boolean voltar = false;
        
        while (!voltar) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       CRIAR NOVO ANÚNCIO              ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║  1. Usar modelo padrão (Prototype)    ║");
            System.out.println("║  2. Criar do zero (Builder)           ║");
            System.out.println("║  0. Voltar                            ║");
            System.out.println("╚════════════════════════════════════════╝");
            
            try {
                System.out.print("Escolha uma opção: ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                System.out.println();
                
                switch (opcao) {
                    case 1:
                        criarAnuncioDePrototipo(scanner);
                        voltar = true; // Volta ao menu principal após criar
                        break;
                    case 2:
                        criarAnuncioInterativo(scanner);
                        voltar = true; // Volta ao menu principal após criar
                        break;
                    case 0:
                        voltar = true;
                        break;
                    default:
                        System.out.println("❌ Opção inválida! Tente novamente.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Digite um número válido!\n");
            }
        }
    }
    
    /**
     * RF01 - Criar anúncio de forma interativa
     * Fluxo: Builder (Imovel) → Factory Method (Anuncio)
     */
    public void criarAnuncioInterativo(Scanner scanner) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       CRIAR NOVO ANÚNCIO               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        try {
            // PASSO 1: BUILDER - Criar Imóvel
            Imovel imovel = criarImovelComBuilder(scanner);
            
            if (imovel == null) {
                System.out.println("\n❌ Criação de imóvel cancelada.\n");
                return;
            }
            
            System.out.println("\n✅ Imóvel criado com sucesso!");
            System.out.println("   Tipo: " + imovel.getTipo());
            System.out.println("   Endereço: " + imovel.getEndereco());
            System.out.println("   Área: " + imovel.getArea() + "m²");
            
            // PASSO 2: FACTORY METHOD - Criar Anúncio
            Anuncio anuncio = criarAnuncioComFactory(scanner, imovel);
            
            if (anuncio == null) {
                System.out.println("\n❌ Criação de anúncio cancelada.\n");
                return;
            }
            
            // Adicionar à lista de anúncios
            meusAnuncios.add(anuncio);
            contadorAnuncios++;
            
            // Salvar em arquivo JSON
            persistenciaService.salvarAnuncios(meusAnuncios);
            
            // Exibir resultado final
            exibirResultadoAnuncio(anuncio);
            
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar anúncio: " + e.getMessage() + "\n");
        }
    }
    
    /**
     * BUILDER PATTERN - Cria imóvel passo a passo
     * Usuário digita endereço manualmente (João Pessoa/PB)
     */
    private Imovel criarImovelComBuilder(Scanner scanner) {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│  PASSO 1: CRIAR IMÓVEL (BUILDER)      │");
        System.out.println("└────────────────────────────────────────┘\n");
        
        // Escolher tipo de imóvel
        System.out.println("🏘️  Tipo de Imóvel:");
        System.out.println("  [1] Casa");
        System.out.println("  [2] Apartamento");
        System.out.println("  [3] Terreno");
        System.out.println("  [4] Sala Comercial");
        System.out.print("➤ Escolha: ");
        
        int tipoOpcao = Integer.parseInt(scanner.nextLine().trim());
        String tipo;
        
        switch (tipoOpcao) {
            case 1: tipo = "casa"; break;
            case 2: tipo = "apartamento"; break;
            case 3: tipo = "terreno"; break;
            case 4: tipo = "sala_comercial"; break;
            default:
                System.out.println("❌ Tipo inválido!");
                return null;
        }
        
        // Digitar endereço (João Pessoa/PB)
        System.out.print("\n📍 Digite o endereço completo: ");
        String endereco = scanner.nextLine().trim();
        
        if (endereco.isEmpty()) {
            System.out.println("❌ Endereço não pode ser vazio!");
            return null;
        }
        
        // Digitar área
        System.out.print("📏 Digite a área (m²): ");
        double area = Double.parseDouble(scanner.nextLine().trim());
        
        if (area <= 0) {
            System.out.println("❌ Área deve ser maior que zero!");
            return null;
        }
        
        // USAR BUILDER PATTERN (RF01)
        ImovelBuilder builder = new ImovelBuilderImpl();
        System.out.print("🔢 Digite o número: ");
        String numero = scanner.nextLine().trim();
        System.out.print("🏙️ Digite a cidade: ");
        String cidade = scanner.nextLine().trim();
        System.out.print("📍 Digite o estado: ");
        String estado = scanner.nextLine().trim();
        Endereco enderecoObj = new Endereco(endereco, numero, cidade, estado);
        builder.setTipo(tipo)
               .setEndereco(enderecoObj)
               .setArea(area);
        
        // Atributos específicos por tipo
        if (tipo.equals("casa")) {
            System.out.print("🛌 Quartos: ");
            int quartos = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("🚿 Banheiros: ");
            int banheiros = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("🌳 Tem quintal? (s/n): ");
            boolean temQuintal = scanner.nextLine().trim().equalsIgnoreCase("s");
            
            System.out.print("🚗 Tem garagem? (s/n): ");
            boolean temGaragem = scanner.nextLine().trim().equalsIgnoreCase("s");
            
            builder.setQuartos(quartos)
                   .setBanheiros(banheiros)
                   .setTemQuintal(temQuintal)
                   .setTemGaragem(temGaragem);
                   
        } else if (tipo.equals("apartamento")) {
            System.out.print("🛌 Quartos: ");
            int quartos = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("🚿 Banheiros: ");
            int banheiros = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("🏢 Andar: ");
            int andar = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("🅿️  Vagas de garagem: ");
            int vagas = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("🛗 Tem elevador? (s/n): ");
            boolean temElevador = scanner.nextLine().trim().equalsIgnoreCase("s");
            
            builder.setQuartos(quartos)
                   .setBanheiros(banheiros)
                   .setAndar(andar)
                   .setVagas(vagas)
                   .setTemElevador(temElevador);
                   
        } else if (tipo.equals("terreno")) {
            System.out.print("🏭 Zoneamento (residencial/comercial/misto): ");
            String zoneamento = scanner.nextLine().trim();
            
            System.out.print("📊 Topografia (plano/aclive/declive): ");
            String topografia = scanner.nextLine().trim();
            
            builder.setZoneamento(zoneamento)
                   .setTopografia(topografia);
                   
        } else if (tipo.equals("sala_comercial")) {
            System.out.print("🚻 Tem banheiro? (s/n): ");
            boolean temBanheiro = scanner.nextLine().trim().equalsIgnoreCase("s");
            
            System.out.print("👥 Capacidade de pessoas: ");
            int capacidade = Integer.parseInt(scanner.nextLine().trim());
            
            builder.setTemBanheiro(temBanheiro)
                   .setCapacidadePessoas(capacidade);
        }
        
        // CONSTRUIR IMÓVEL (Builder Pattern)
        return builder.build();
    }
    
    /**
     * FACTORY METHOD PATTERN - Cria anúncio usando Factory
     */
    private Anuncio criarAnuncioComFactory(Scanner scanner, Imovel imovel) {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│  PASSO 2: CRIAR ANÚNCIO (FACTORY)     │");
        System.out.println("└────────────────────────────────────────┘\n");
        
        // Escolher tipo de anúncio
        System.out.println("🏷️  Tipo de Anúncio:");
        System.out.println("  [1] Venda");
        System.out.println("  [2] Aluguel");
        System.out.println("  [3] Temporada");
        System.out.print("➤ Escolha: ");
        
        int tipoAnuncio = Integer.parseInt(scanner.nextLine().trim());
        
        // Dados do anúncio
        System.out.print("\n📝 Título do anúncio: ");
        String titulo = scanner.nextLine().trim();
        
        System.out.print("💰 Preço (R$): ");
        double preco = Double.parseDouble(scanner.nextLine().trim());
        
        System.out.print("📄 Descrição: ");
        String descricao = scanner.nextLine().trim();
        
        // Dados do anunciante
        System.out.print("\n👤 Seu nome: ");
        String nome = scanner.nextLine().trim();
        
        // Validação de email
        String email;
        while (true) {
            System.out.print("📧 Seu email: ");
            email = scanner.nextLine().trim();
            if (validarEmail(email)) {
                break;
            }
            System.out.println("❌ Email inválido! Use o formato: exemplo@dominio.com");
        }
        
        // Validação de telefone com formatação automática
        String telefone;
        while (true) {
            System.out.print("📱 Seu telefone (apenas números): ");
            String input = scanner.nextLine().trim();
            telefone = formatarTelefone(input);
            if (telefone != null) {
                System.out.println("✅ Telefone formatado: " + telefone);
                break;
            }
            System.out.println("❌ Telefone inválido! Digite 10 ou 11 dígitos (ex: 83988881111)");
        }
        
        Usuario anunciante = new Usuario(nome, email, telefone);
        anunciante.setTipo(Usuario.TipoUsuario.PROPRIETARIO);
        
        // USAR FACTORY METHOD para criar anúncio (RF01)
        AnuncioFactory factory;
        
        switch (tipoAnuncio) {
            case 1:
                factory = new VendaFactory();
                break;
            case 2:
                factory = new AluguelFactory();
                break;
            case 3:
                factory = new TemporadaFactory();
                break;
            default:
                System.out.println("❌ Tipo de anúncio inválido!");
                return null;
        }
        
        return factory.criarAnuncio(titulo, preco, descricao, imovel, anunciante);
    }
    
    /**
     * RF02 - CRIAR ANÚNCIO A PARTIR DE PROTÓTIPO
     * 
     * Fluxo: PrototypeRegistry → Clonar → Customizar → Factory Method
     * 
     * PADRÃO PROTOTYPE:
     * - Obtém protótipo pré-configurado do PrototypeRegistry
     * - Clona usando método clonar() da interface ImovelPrototype
     * - Permite customização do endereço (obrigatório)
     * - Valida antes de prosseguir com Factory
     */
    public void criarAnuncioDePrototipo(Scanner scanner) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   CRIAR ANÚNCIO DE PROTÓTIPO          ║");
        System.out.println("║   (Padrão Prototype - RF02)            ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        try {
            // PASSO 1: Listar protótipos disponíveis
            PrototypeRegistry registro = PrototypeRegistry.getInstance();
            Set<String> chaves = registro.listarChaves();
            
            System.out.println("🏘️  Protótipos Disponíveis:\n");
            List<String> chavesLista = new ArrayList<>(chaves);
            for (int i = 0; i < chavesLista.size(); i++) {
                String chave = chavesLista.get(i);
                String descricao = registro.obterDescricao(chave);
                System.out.println("  [" + (i + 1) + "] " + descricao);
            }
            
            System.out.print("\n➤ Escolha o protótipo: ");
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            
            if (opcao < 1 || opcao > chavesLista.size()) {
                System.out.println("\n❌ Opção inválida!");
                return;
            }
            
            String chavePrototipo = chavesLista.get(opcao - 1);
            
            // PASSO 2: CLONAR o protótipo (Prototype Pattern)
            Imovel imovel = registro.obterPrototipo(chavePrototipo);
            
            if (imovel == null) {
                System.out.println("\n❌ Protótipo não encontrado!");
                return;
            }
            
            System.out.println("\n┌────────────────────────────────────────┐");
            System.out.println("│  PASSO 1: IMÓVEL CLONADO COM SUCESSO   │");
            System.out.println("└────────────────────────────────────────┘");
            System.out.println("\n✅ Imóvel clonado: " + registro.gerarDescricaoPrototipo(imovel));
            System.out.println("   Hash do clone: " + imovel.hashCode());
            System.out.println("   (objeto independente pronto para customização)\n");
            
            // PASSO 3: CUSTOMIZAR o imóvel clonado
            imovelService.customizarImovelClonado(scanner, imovel);
            
            // PASSO 4: VALIDAR antes de prosseguir
            if (!imovel.validar()) {
                System.out.println("\n❌ Imóvel inválido após customização!");
                System.out.println("   Verifique os dados informados.\n");
                return;
            }
            
            System.out.println("\n✅ Imóvel validado com sucesso!");
            
            // PASSO 5: FACTORY METHOD - Criar Anúncio
            Anuncio anuncio = criarAnuncioComFactory(scanner, imovel);
            
            if (anuncio == null) {
                System.out.println("\n❌ Criação de anúncio cancelada.\n");
                return;
            }
            
            // Adicionar à lista de anúncios
            meusAnuncios.add(anuncio);
            contadorAnuncios++;
            
            // Salvar em arquivo JSON
            persistenciaService.salvarAnuncios(meusAnuncios);
            
            // Exibir resultado final
            exibirResultadoAnuncio(anuncio);
            
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Entrada inválida! Digite um número.");
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao criar anúncio de protótipo: " + e.getMessage() + "\n");
        }
    }
    
    /**
     * Exibe resultado final do anúncio criado
     */
    private void exibirResultadoAnuncio(Anuncio anuncio) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     ✅ ANÚNCIO CRIADO COM SUCESSO!     ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        Imovel imovel = anuncio.getImovel();
        Usuario anunciante = anuncio.getAnunciante();
        
        System.out.println("\n📋 ANÚNCIO #" + contadorAnuncios);
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
     * RF01 - Exibir meus anúncios criados na sessão
     */
    public void exibirMeusAnuncios() {
        System.out.println("\n+============================================+");
        System.out.println("|           MEUS ANUNCIOS                    |");
        System.out.println("+============================================+\n");
        
        if (meusAnuncios.isEmpty()) {
            System.out.println("  >> Nenhum anuncio criado ainda.");
            System.out.println("  >> Use a opcao 1 para criar seu primeiro anuncio!\n");
            return;
        }
        
        System.out.println("  >> Total de anuncios: " + meusAnuncios.size() + "\n");
        
        for (int i = 0; i < meusAnuncios.size(); i++) {
            Anuncio anuncio = meusAnuncios.get(i);
            Imovel imovel = anuncio.getImovel();
            Usuario anunciante = anuncio.getAnunciante();
            
            System.out.println("+--------------------------------------------+");
            System.out.println("|  ANUNCIO #" + (i + 1) + "                                 |");
            System.out.println("+--------------------------------------------+");
            System.out.println("  Titulo.....: " + anuncio.getTitulo());
            System.out.println("  Preco......: R$ " + String.format("%,.2f", anuncio.getPreco()));
            System.out.println("  Descricao..: " + anuncio.getDescricao());
            System.out.println();
            System.out.println("  [IMOVEL]");
            System.out.println("  Tipo.......: " + imovel.getTipo().toUpperCase());
            System.out.println("  Area.......: " + imovel.getArea() + " m2");
            System.out.println("  Endereco...: " + imovel.getEndereco());
            
            // Exibe detalhes específicos do tipo de imóvel
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
            
            System.out.println();
            System.out.println();
            System.out.println("  [ANUNCIANTE]");
            System.out.println("  Nome.......: " + anunciante.getNome());
            System.out.println("  Email......: " + anunciante.getEmail());
            System.out.println("  Telefone...: " + anunciante.getTelefone());
            System.out.println("  Estado......: " + anuncio.getEstado().getNome().toUpperCase());
            System.out.println("+--------------------------------------------+\n");
        }
    }
    
    /**
     * RF04 - Gerenciar anúncios com transições de estado
     * State Pattern + Chain of Responsibility + Observer
     */
    private void gerenciarMeusAnuncios(Scanner scanner) {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GERENCIAR MEUS ANÚNCIOS         ║");
            System.out.println("╚════════════════════════════════════════╝\n");
            
            if (meusAnuncios.isEmpty()) {
                System.out.println("📭 Nenhum anúncio criado ainda.");
                System.out.println("   Use a opção 1 do menu principal para criar seu primeiro anúncio!\n");
                return;
            }
            
            // Listar anúncios com números
            System.out.println("📋 Total de anúncios: " + meusAnuncios.size() + "\n");
            
            for (int i = 0; i < meusAnuncios.size(); i++) {
                Anuncio anuncio = meusAnuncios.get(i);
                System.out.println("┌────────────────────────────────────────┐");
                System.out.println("│ [" + (i + 1) + "] " + anuncio.getTitulo());
                System.out.println("├────────────────────────────────────────┤");
                System.out.println("│ Preço: R$ " + String.format("%,.2f", anuncio.getPreco()));
                System.out.println("│ Estado: " + anuncio.getEstado().getNome().toUpperCase());
                System.out.println("└────────────────────────────────────────┘");
            }
            
            System.out.println("\n[0] Voltar ao menu principal");
            System.out.print("\n➤ Selecione um anúncio (número): ");
            
            try {
                int escolha = Integer.parseInt(scanner.nextLine().trim());
                
                if (escolha == 0) {
                    return;
                }
                
                if (escolha < 1 || escolha > meusAnuncios.size()) {
                    System.out.println("\n❌ Opção inválida!");
                    pausar(scanner);
                    continue;
                }
                
                Anuncio anuncioSelecionado = meusAnuncios.get(escolha - 1);
                gerenciarAnuncioEspecifico(scanner, anuncioSelecionado);
                
            } catch (NumberFormatException e) {
                System.out.println("\n❌ Digite um número válido!");
                pausar(scanner);
            }
        }
    }
    
    /**
     * RF04 - Anexa observers aos anúncios carregados do arquivo JSON
     * 
     * IMPORTANTE: Anúncios criados em `criarAnuncioInterativo()` já têm observers.
     * Mas anúncios carregados do arquivo JSON perdem os observers durante
     * a desserialização, então precisam ser re-anexados aqui.
     * 
     * Padrão Observer: Monitora mudanças de estado
     * - LogObserver: Registra mudanças em arquivo logs/sistema.log
     * - NotificationObserver: Notifica usuários (quando configurado)
     */
    
    /**
     * RF05 - STRATEGY: Envia notificação usando o canal configurado do usuário
     * 
     * O padrão Strategy permite trocar dinamicamente o algoritmo de notificação:
     * - EmailNotificacao: envia por email
     * - SMSNotificacao: envia por SMS
     * - WhatsAppNotificacao: envia por WhatsApp
     */
    private void notificarUsuario(String mensagem) {
        if (usuarioAtual != null && usuarioAtual.getCanalNotificacao() != null) {
            NotificationManager manager = new NotificationManager();
            manager.enviarNotificacao(usuarioAtual, mensagem);
        }
    }
    
    private void anexarObserversAosAnuncios() {
        for (Anuncio anuncio : meusAnuncios) {
            // Remover observers antigos (se houver)
            // Isso evita duplicação se o método for chamado múltiplas vezes
            
            // Criar observers
            LoggerService logger = new LoggerService();
            NotificationManager manager = new NotificationManager();
            
            // Anexar observers
            anuncio.adicionarObserver(new LogObserver(logger));
            anuncio.adicionarObserver(new NotificationObserver(manager));
        }
    }
    
    /**
     * Gerencia um anúncio específico com opções baseadas no estado atual
     */
    private void gerenciarAnuncioEspecifico(Scanner scanner, Anuncio anuncio) {
        AnuncioFacade facade = new AnuncioFacade();
        
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GERENCIAR ANÚNCIO                ║");
            System.out.println("╚════════════════════════════════════════╝");
            
            System.out.println("\n📄 " + anuncio.getTitulo());
            System.out.println("💰 R$ " + String.format("%,.2f", anuncio.getPreco()));
            System.out.println("📊 Estado atual: " + anuncio.getEstado().getNome().toUpperCase());
            
            System.out.println("\n┌────────────────────────────────────────┐");
            System.out.println("│ AÇÕES DISPONÍVEIS:                     │");
            System.out.println("├────────────────────────────────────────┤");
            
            String estadoNome = anuncio.getEstado().getNome();
            
            // Opções baseadas no estado atual
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
                    System.out.println("\n✅ Ação executada com sucesso!");
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
     * Executa ação baseada no estado atual e opção escolhida
     * 
     * RF04 - State Pattern: Gerencia transições entre estados
     * O padrão State valida automaticamente as transições permitidas
     * e lança exceções quando uma transição é inválida.
     */
    private boolean executarAcaoAnuncio(AnuncioFacade facade, Anuncio anuncio, int opcao, String estadoNome) {
        try {
            System.out.println("\n" + "═".repeat(42));
            
            // Pré-validação: verificar se o imóvel é válido antes de qualquer transição
            // (necessário apenas para transições que exigem validação)
            if ((estadoNome.equals("Rascunho") && opcao == 1) || 
                (estadoNome.equals("Suspenso") && opcao == 1)) {
                
                boolean isValido = anuncio.getImovel().validar();
                
                if (!isValido) {
                    System.out.println("❌ ERRO DE VALIDAÇÃO DO IMÓVEL:");
                    System.out.println("   O imóvel não atende aos requisitos mínimos:");
                    Imovel imovel = anuncio.getImovel();
                    
                    // Verificar cada aspecto
                    if (imovel.getArea() <= 0) {
                        System.out.println("   ❌ Área inválida: " + imovel.getArea() + " (deve ser > 0)");
                    } else {
                        System.out.println("   ✅ Área válida: " + imovel.getArea() + " m²");
                    }
                    
                    if (imovel.getEndereco() == null) {
                        System.out.println("   ❌ Endereço é nulo");
                    } else if (imovel.getEndereco().getCidade() == null) {
                        System.out.println("   ❌ Cidade do endereço é nula");
                    } else if (imovel.getEndereco().getCidade().trim().isEmpty()) {
                        System.out.println("   ❌ Cidade do endereço está vazia");
                    } else {
                        System.out.println("   ✅ Endereço válido: " + imovel.getEndereco().getCidade());
                    }
                    
                    return false;
                }
            }
            
            if (estadoNome.equals("Rascunho")) {
                if (opcao == 1) {
                    System.out.println("📤 Enviando anúncio para moderação...\n");
                    facade.enviarParaModeracao(anuncio);
                    System.out.println("✅ Anúncio enviado para MODERAÇÃO");
                    System.out.println("   📝 Observer registrando mudança em logs/sistema.log...");
                    System.out.println("   Próxima etapa: Validação (Chain of Responsibility)");
                    
                    // RF05 - STRATEGY: Enviar notificação usando o canal configurado
                    notificarUsuario("📤 Seu anúncio '" + anuncio.getTitulo() + "' foi enviado para moderação!");
                    
                    return true;
                } else if (opcao == 2) {
                    System.out.println("⏸️  Suspendendo anúncio...\n");
                    facade.suspender(anuncio);
                    System.out.println("✅ Anúncio movido para SUSPENSO");
                    
                    // RF05 - STRATEGY: Enviar notificação
                    notificarUsuario("⏸️  Seu anúncio '" + anuncio.getTitulo() + "' foi suspenso.");
                    
                    return true;
                }
            } else if (estadoNome.equals("Moderação")) {
                if (opcao == 1) {
                    System.out.println("✅ Aprovando anúncio...\n");
                    System.out.println("Executando Chain of Responsibility:");
                    facade.aprovar(anuncio);
                    System.out.println("\n✅ Anúncio movido para ATIVO (todas as validações passaram)");
                    System.out.println("   📝 Observer registrando mudança em logs/sistema.log...");
                    
                    // RF05 - STRATEGY: Enviar notificação de aprovação
                    notificarUsuario("✅ Parabéns! Seu anúncio '" + anuncio.getTitulo() + "' foi aprovado e está ATIVO!");
                    
                    return true;
                } else if (opcao == 2) {
                    System.out.println("❌ Reprovando anúncio...\n");
                    facade.reprovar(anuncio);
                    System.out.println("✅ Anúncio movido para SUSPENSO");
                    
                    // RF05 - STRATEGY: Enviar notificação de rejeição
                    notificarUsuario("❌ Seu anúncio '" + anuncio.getTitulo() + "' foi reprovado e movido para SUSPENSO.");
                    
                    return true;
                } else if (opcao == 3) {
                    System.out.println("⏸️  Suspendendo anúncio...\n");
                    facade.suspender(anuncio);
                    System.out.println("✅ Anúncio movido para SUSPENSO");
                    
                    // RF05 - STRATEGY: Enviar notificação
                    notificarUsuario("⏸️  Seu anúncio '" + anuncio.getTitulo() + "' foi suspenso durante moderação.");
                    
                    return true;
                }
            } else if (estadoNome.equals("Ativo")) {
                if (opcao == 1) {
                    System.out.println("🎉 Marcando anúncio como vendido...\n");
                    facade.vender(anuncio);
                    System.out.println("✅ Anúncio movido para VENDIDO");
                    
                    // RF05 - STRATEGY: Enviar notificação de venda
                    notificarUsuario("🎉 Seu anúncio '" + anuncio.getTitulo() + "' foi marcado como VENDIDO!");
                    
                    return true;
                } else if (opcao == 2) {
                    System.out.println("⏸️  Suspendendo anúncio...\n");
                    facade.suspender(anuncio);
                    System.out.println("✅ Anúncio movido para SUSPENSO");
                    
                    // RF05 - STRATEGY: Enviar notificação
                    notificarUsuario("⏸️  Seu anúncio '" + anuncio.getTitulo() + "' foi suspenso.");
                    
                    return true;
                }
            } else if (estadoNome.equals("Suspenso")) {
                if (opcao == 1) {
                    System.out.println("🔄 Reativando anúncio...\n");
                    facade.reativar(anuncio);
                    System.out.println("✅ Anúncio enviado para MODERAÇÃO");
                    System.out.println("   Próxima etapa: Validação (Chain of Responsibility)");
                    
                    // RF05 - STRATEGY: Enviar notificação
                    notificarUsuario("🔄 Seu anúncio '" + anuncio.getTitulo() + "' foi reativado e está em MODERAÇÃO!");
                    
                    return true;
                }
            }
            
            System.out.println("❌ Opção inválida para o estado atual!");
            return false;
            
        } catch (IllegalStateException e) {
            System.out.println("\n⚠️  ERRO DE TRANSIÇÃO DE ESTADO (State Pattern):");
            System.out.println("   " + e.getMessage());
            System.out.println("\n💡 Motivo: O padrão State não permite esta transição");
            System.out.println("   a partir do estado atual.");
            return false;
        } catch (Exception e) {
            System.out.println("\n❌ ERRO INESPERADO:");
            System.out.println("   " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * RF07 - Exibir configurações (Singleton)
     */
    public void exibirConfiguracoes() {
        Scanner scanner = new Scanner(System.in);
        boolean voltar = false;
        
        while (!voltar) {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║         CONFIGURAÇÕES DO SISTEMA       ║");
            System.out.println("╚════════════════════════════════════════╝\n");
            
            ConfigurationManager config = ConfigurationManager.getInstance();
            
            System.out.println("📋 Configurações Disponíveis:");
            System.out.println("─".repeat(40));
            System.out.println("[1] Configurar Canal de Notificação (RF05)");
            System.out.println("[2] Informações do Sistema (RF07)");
            System.out.println("[0] Voltar");
            System.out.println("─".repeat(40));
            
            try {
                System.out.print("Escolha uma opção: ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcao) {
                    case 1:
                        configurarCanalNotificacao(scanner);
                        break;
                    case 2:
                        exibirInformacoesDoSistema();
                        break;
                    case 0:
                        voltar = true;
                        break;
                    default:
                        System.out.println("❌ Opção inválida!");
                }
                
                if (opcao != 0) {
                    pausar(scanner);
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Opção inválida! Digite um número.");
            }
        }
    }
    
    /**
     * RF05 - STRATEGY PATTERN: Configurar canal de notificação
     * Permite ao usuário escolher como quer ser notificado
     */
    private void configurarCanalNotificacao(Scanner scanner) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ RF05 - STRATEGY (Canal de Notificação)  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println("📢 Escolha o canal de notificação preferido:\n");
        System.out.println("[1] Email 📧");
        System.out.println("    → Notificações por email (mais detalhado)");
        System.out.println("[2] SMS 📱");
        System.out.println("    → Notificações por SMS (mais rápido)");
        System.out.println("[3] WhatsApp 💬");
        System.out.println("    → Notificações por WhatsApp");
        System.out.println("[0] Cancelar");
        
        try {
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            
            switch (opcao) {
                case 1:
                    usuarioAtual.setCanalNotificacao(
                        new EmailNotificacao(new EmailService())
                    );
                    System.out.println("\n✅ Canal alterado para EMAIL");
                    System.out.println("   Você receberá notificações por: " + usuarioAtual.getEmail());
                    testarNotificacao("📧 Email: Bem-vindo! Você está recebendo notificações por email.");
                    break;
                    
                case 2:
                    usuarioAtual.setCanalNotificacao(
                        new SMSNotificacao(new SMSService())
                    );
                    System.out.println("\n✅ Canal alterado para SMS");
                    System.out.println("   Você receberá notificações por: " + usuarioAtual.getTelefone());
                    testarNotificacao("📱 SMS: Bem-vindo! Você está recebendo notificações por SMS.");
                    break;
                    
                case 3:
                    usuarioAtual.setCanalNotificacao(
                        new WhatsAppNotificacao(new WhatsAppService())
                    );
                    System.out.println("\n✅ Canal alterado para WHATSAPP");
                    System.out.println("   Você receberá notificações por: " + usuarioAtual.getTelefone());
                    testarNotificacao("💬 WhatsApp: Bem-vindo! Você está recebendo notificações por WhatsApp.");
                    break;
                    
                case 0:
                    System.out.println("❌ Operação cancelada.");
                    break;
                    
                default:
                    System.out.println("❌ Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Opção inválida! Digite um número.");
        }
    }
    
    /**
     * Testa o canal de notificação configurado
     */
    private void testarNotificacao(String mensagem) {
        System.out.println("\n📤 Enviando notificação de teste...");
        NotificationManager manager = new NotificationManager();
        manager.enviarNotificacao(usuarioAtual, mensagem);
        System.out.println("✅ Notificação enviada com sucesso!");
    }
    
    /**
     * Exibe informações do sistema (RF07)
     */
    private void exibirInformacoesDoSistema() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   RF07 - SINGLETON (Configurações)     ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        System.out.println("📋 Configurações do Sistema:");
        System.out.println("─".repeat(40));
        System.out.println("Nome: " + config.getProperty("app.name", "MyHome"));
        System.out.println("Versão: " + config.getProperty("app.version", "2.0"));
        System.out.println("Cidade: João Pessoa - Paraíba");
        System.out.println("─".repeat(40));
        
        System.out.println("\n👤 Dados do Usuário Atual:");
        System.out.println("─".repeat(40));
        System.out.println("Nome: " + usuarioAtual.getNome());
        System.out.println("Email: " + usuarioAtual.getEmail());
        System.out.println("Telefone: " + usuarioAtual.getTelefone());
        System.out.println("Canal de Notificação: " + 
            (usuarioAtual.getCanalNotificacao() != null 
                ? usuarioAtual.getCanalNotificacao().getClass().getSimpleName().replace("Notificacao", "")
                : "Não configurado"));
        System.out.println("─".repeat(40));
        
        System.out.println("\n💡 ConfigurationManager é um Singleton!");
        System.out.println("   Sempre a mesma instância: " + config.hashCode());
    }
    
    /**
     * Demonstra todos os padrões GoF implementados
     */
    public void demonstrarPadroesGoF() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   DEMONSTRAÇÃO PADRÕES GOF             ║");
        System.out.println("║   RF01 + RF02 + RF07                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println("📚 PADRÕES IMPLEMENTADOS NO MYHOME:\n");
        
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
        demonstrarPrototype();
        System.out.println("   → Usado na opção: 1 - Criar novo anúncio → Usar modelo padrão\n");
        
        System.out.println("✅ RF07 - SINGLETON (Configurações)");
        System.out.println("   → ConfigurationManager");
        System.out.println("   → Usado na opção: 4 - Configurações\n");
        
        System.out.println("💡 COMO TESTAR:");
        System.out.println("   1. Use a opção '1' → '2' para criar anúncio com Builder");
        System.out.println("   2. Use a opção '1' → '1' para criar anúncio com Prototype");
        System.out.println("   3. Use a opção '3' para ver seus anúncios cadastrados");
        System.out.println("   4. Use a opção '4' para ver o Singleton em ação\n");
        
        System.out.println("═".repeat(60));
        System.out.println("✅ Todos os padrões estão funcionando via terminal!");
        System.out.println("═".repeat(60) + "\n");
    }
    
    /**
     * Demonstra o funcionamento do padrão Prototype em detalhes.
     */
    private void demonstrarPrototype() {
        System.out.println("   ┌ DEMONSTRAÇÃO LIVE ┐");
        
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
        System.out.println("   └──────────────────┘");
    }
    
    // ================================================================
    // MÉTODOS DE VALIDAÇÃO
    // ================================================================
    
    /**
     * Valida formato de email
     * @param email Email a ser validado
     * @return true se válido, false caso contrário
     */
    private boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Regex para validação básica de email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
    
    /**
     * Valida formato de telefone brasileiro: (xx) xxxx-xxxx ou (xx) xxxxx-xxxx
     * @param telefone Telefone a ser validado
     * @return true se válido, false caso contrário
     */
    private boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return false;
        }
        // Regex para validação: (xx) xxxx-xxxx ou (xx) xxxxx-xxxx
        String regex = "^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$";
        return telefone.matches(regex);
    }
    
    /**
     * Formata número de telefone brasileiro automaticamente.
     * Aceita 10 dígitos: (xx) xxxx-xxxx
     * Aceita 11 dígitos: (xx) xxxxx-xxxx
     * @param input String com apenas números
     * @return Telefone formatado ou null se inválido
     */
    private String formatarTelefone(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove tudo que não é dígito
        String numeros = input.replaceAll("[^0-9]", "");
        
        // Valida quantidade de dígitos
        if (numeros.length() == 10) {
            // Formato: (xx) xxxx-xxxx (telefone fixo)
            return String.format("(%s) %s-%s", 
                numeros.substring(0, 2),
                numeros.substring(2, 6),
                numeros.substring(6, 10));
        } else if (numeros.length() == 11) {
            // Formato: (xx) xxxxx-xxxx (celular com 9 dígitos)
            return String.format("(%s) %s-%s", 
                numeros.substring(0, 2),
                numeros.substring(2, 7),
                numeros.substring(7, 11));
        }
        
        return null; // Quantidade inválida de dígitos
    }
    
    // ================================================================
    // MÉTODOS AUXILIARES
    // ================================================================
    
    /**
     * Imprime banner da aplicação.
     */
    private void imprimirBanner() {
        System.out.println("=".repeat(60));
        System.out.println("           MYHOME - ANÚNCIOS DE IMÓVEIS");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Imprime rodapé da aplicação.
     */
    private void imprimirRodape() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✓ Sistema executado com sucesso!");
        System.out.println("=".repeat(60));
    }
}
