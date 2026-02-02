package com.myhome.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.myhome.controller.UIController;
import com.myhome.decorator.BuscaFiltro;
import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;
import com.myhome.prototype.PrototypeRegistry;
import com.myhome.service.AnuncioService;
import com.myhome.service.AnuncioManagementService;
import com.myhome.service.EmailService;
import com.myhome.service.ImovelService;
import com.myhome.service.MenuService;
import com.myhome.service.NotificationConfigService;
import com.myhome.service.PatternsService;
import com.myhome.service.PersistenciaService;
import com.myhome.service.SearchFilterService;
import com.myhome.service.SystemInfoService;
import com.myhome.service.SMSService;
import com.myhome.service.UsuarioService;
import com.myhome.service.ValidadorService;
import com.myhome.service.WhatsAppService;
import com.myhome.strategy.EmailNotificacao;

// RF08 - Facade: orquestra todos os subsistemas do MyHome
public class MyHomeFacade {
    
    // Subsistemas (injeção de dependência)
    private final MenuService menuService;
    private final UIController uiController;
    private final ImovelService imovelService;
    private final AnuncioService anuncioService;
    private final ValidadorService validadorService;
    private final UsuarioService usuarioService;
    private final PersistenciaService persistenciaService;
    private final SystemInfoService systemInfoService;
    private final PatternsService patternsService;

    // Dados da aplicação
    private List<Anuncio> meusAnuncios;
    private int contadorAnuncios;
    private Usuario usuarioAtual; // RF05 - Usuario com canal de notificação configurável
    
    // Inicializa todos os subsistemas
    public MyHomeFacade() {
        // Criar services na ordem correta de dependências
        this.menuService = new MenuService();
        this.validadorService = new ValidadorService();
        this.uiController = new UIController(menuService, validadorService);
        this.usuarioService = new UsuarioService();
        this.persistenciaService = new PersistenciaService();
        this.imovelService = new ImovelService(menuService, validadorService);
        this.anuncioService = new AnuncioService(menuService, validadorService, usuarioService);
        this.systemInfoService = new SystemInfoService(uiController);
        this.patternsService = new PatternsService();
        
        this.meusAnuncios = new ArrayList<>();
        this.contadorAnuncios = 0;
        
        // RF05 - Criar usuário padrão com canal de notificação padrão
        this.usuarioAtual = new Usuario("User", "jayradpro@gmail.com", "(83) 8888-8888");
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
        Scanner scanner = uiController.getScanner();
        boolean continuar = true;
        
        // Carrega anúncios salvos
        meusAnuncios = persistenciaService.carregarAnuncios();
        contadorAnuncios = meusAnuncios.size();
        
        // Anexar observers aos anúncios carregados (RF04 - Observer Pattern)
        if (contadorAnuncios > 0) {
            anexarObserversAosAnuncios();
            uiController.exibirInfo(contadorAnuncios + " anúncio(s) carregado(s) do arquivo!");
            uiController.exibirSucesso("Observers attachados para monitoramento de mudanças");
        }
        
        while (continuar) {
            uiController.exibirMenuPrincipal();
            
            try {
                int opcao = uiController.lerOpcao("Escolha uma opção: ");
                
                switch (opcao) {
                    case 1:
                        menuCriarAnuncio(scanner);
                        break;
                    case 2:
                        executarBusca(uiController);
                        break;
                    case 3:
                        exibirMeusAnuncios(uiController);
                        break;
                    case 4:
                        exibirConfiguracoes();
                        break;
                    case 5:
                        demonstrarPadroesGoF();
                        break;
                    case 0:
                        continuar = false;
                        uiController.exibirMensagemDespedida();
                        break;
                    default:
                        uiController.exibirErro("Opção inválida! Tente novamente.");
                }
                
                if (continuar && opcao != 0) {
                    uiController.pausar();
                }
                
            } catch (NumberFormatException e) {
                uiController.exibirErro("Entrada inválida! Digite um número.");
                uiController.pausar();
            } catch (Exception e) {
                uiController.exibirErro("Erro: " + e.getMessage());
                uiController.pausar();
            }
        }
        
        uiController.fechar();
    }
    
    /**
     * Menu para criar anúncio: Prototype ou Builder
     */
    private void menuCriarAnuncio(Scanner scanner) {
        boolean voltar = false;
        
        while (!voltar) {
            uiController.exibirSubmenuCriarAnuncio();
            
            try {
                int opcao = uiController.lerOpcao("\n➤ Escolha uma opção: ");
                
                switch (opcao) {
                    case 1:
                        criarAnuncioDePrototipo(scanner);
                        voltar = true;
                        break;
                    case 2:
                        criarAnuncioInterativo(scanner);
                        voltar = true;
                        break;
                    case 0:
                        voltar = true;
                        break;
                    default:
                        uiController.exibirErro("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                uiController.exibirErro("Digite um número válido!");
            }
        }
    }

    // ================================================================
    // MÉTODOS INTERATIVOS
    // ================================================================
    
    /**
     * RF06 - DECORATOR PATTERN: Busca avançada com filtros dinâmicos
     * Delegada a SearchFilterService para orquestração
     */
    public void executarBusca(UIController uiController) {
        // Coletar filtros via UIController
        String[] filtros = uiController.coletarFiltrosBusca();
        String precoMin = filtros[0];
        String precoMax = filtros[1];
        String cidade = filtros[2];
        String estado = filtros[3];
        String tipo = filtros[4];
        
        // Usar SearchFilterService para aplicar filtros (Decorator Pattern)
        SearchFilterService searchService = new SearchFilterService(uiController);
        BuscaFiltro busca = searchService.aplicarFiltros(meusAnuncios, precoMin, precoMax, cidade, estado, tipo);
        
        // Executar busca
        List<Anuncio> resultados = searchService.executar(busca);
        
        // Exibir resultados
        uiController.exibirResultadoBusca(resultados);
    }
    
    /**
     * RF01 - Exibir meus anúncios criados na sessão
     */
    public void exibirMeusAnuncios(UIController uiController) {
        Scanner scanner = uiController.getScanner();
        gerenciarMeusAnuncios(scanner);
    }
    
    /**
     * RF01 - Criar anúncio de forma interativa
     * Fluxo: ImovelService → AnuncioService → PersistenciaService
     */
    public void criarAnuncioInterativo(Scanner scanner) {
        menuService.exibirCabecalhoCriarAnuncioInterativo();
        
        try {
            // PASSO 1: Criar Imóvel (delegado a ImovelService)
            Imovel imovel = imovelService.criarImovelInterativo(scanner);
            
            if (imovel == null) {
                menuService.exibirCancelamentoCriacaoImovel();
                return;
            }
            
            menuService.exibirSucessoCriacaoImovelInterativo(imovel.getTipo(), imovel.getEndereco().toString(), imovel.getArea());
            
            // PASSO 2: Criar Anúncio (delegado a AnuncioService)
            Anuncio anuncio = anuncioService.criarAnuncioInterativo(scanner, imovel);
            
            if (anuncio == null) {
                menuService.exibirCancelamentoCriacaoAnuncio();
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
            menuService.exibirErroCriacaoAnuncio(e.getMessage());
        }
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
        menuService.exibirCabecalhoCriarAnuncioPrototipo();
        
        try {
            // PASSO 1: Listar protótipos disponíveis
            PrototypeRegistry registro = PrototypeRegistry.getInstance();
            Set<String> chaves = registro.listarChaves();
            
            menuService.exibirCabecalhoPrototiposDisponiveis();
            List<String> chavesLista = new ArrayList<>(chaves);
            for (int i = 0; i < chavesLista.size(); i++) {
                String chave = chavesLista.get(i);
                String descricao = registro.obterDescricao(chave);
                menuService.exibirItemPrototipoLista(i + 1, descricao);
            }
            
            menuService.exibirPromptSelecaoPrototipo();
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            
            if (opcao < 1 || opcao > chavesLista.size()) {
                menuService.exibirOpcaoInvalida();
                return;
            }
            
            String chavePrototipo = chavesLista.get(opcao - 1);
            
            // PASSO 2: CLONAR o protótipo (Prototype Pattern)
            Imovel imovel = registro.obterPrototipo(chavePrototipo);
            
            if (imovel == null) {
                menuService.exibirErroPrototipoNaoEncontrado();
                return;
            }
            
            menuService.exibirCabecalhoImovelClonado();
            menuService.exibirDetalhesClonagemImovel(registro.gerarDescricaoPrototipo(imovel), imovel.hashCode());
            
            // PASSO 3: CUSTOMIZAR o imóvel clonado
            imovelService.customizarImovelClonado(scanner, imovel);
            
            // PASSO 4: VALIDAR antes de prosseguir
            if (!imovel.validar()) {
                menuService.exibirErroValidacaoImovel();
                return;
            }
            
            menuService.exibirSucessoValidacaoImovel();
            
            // PASSO 5: Criar Anúncio (delegado a AnuncioService)
            Anuncio anuncio = anuncioService.criarAnuncioInterativo(scanner, imovel);
            
            if (anuncio == null) {
                menuService.exibirCancelamentoCriacaoAnuncio();
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
            menuService.exibirErroEntradaInvalidaNumero();
        } catch (Exception e) {
            menuService.exibirErroCriacaoAnuncio("(Protótipo) " + e.getMessage());
        }
    }
    
    /**
     * Exibe resultado final do anúncio criado
     * Delegado ao MenuService para desacoplamento
     */
    private void exibirResultadoAnuncio(Anuncio anuncio) {
        Imovel imovel = anuncio.getImovel();
        Usuario anunciante = anuncio.getAnunciante();
        
        menuService.exibirResultadoAnuncioCriadoCompleto(
            contadorAnuncios,
            anuncio.getTitulo(),
            anuncio.getPreco(),
            anuncio.getDescricao(),
            imovel.getTipo(),
            imovel.getEndereco().toString(),
            imovel.getArea(),
            anunciante.getNome(),
            anunciante.getEmail(),
            anunciante.getTelefone()
        );
    }
    
    /**
     * RF01 - Exibir meus anúncios criados na sessão
     */
    public void exibirMeusAnuncios() {
        menuService.exibirCabecalhoMeusAnuncios();
        
        if (meusAnuncios.isEmpty()) {
            menuService.exibirNenhumAnuncioMeusList();
            return;
        }
        
        menuService.exibirListaAnunciosCompleta(meusAnuncios);
    }
    
    /**
     * RF04 - Gerenciar anúncios com transições de estado
     * State Pattern + Chain of Responsibility + Observer
     */
    private void gerenciarMeusAnuncios(Scanner scanner) {
        boolean voltar = false;
        
        while (!voltar) {
            menuService.exibirCabecalhoGerenciarAnunciosMenu();
            
            if (meusAnuncios.isEmpty()) {
                menuService.exibirNenhumAnuncio();
                return;
            }
            
            // Listar anúncios com números
            menuService.exibirListaAnunciosParaSelecao(meusAnuncios);
            menuService.exibirPromptSelecaoAnuncioGerenciar();
            
            try {
                int escolha = Integer.parseInt(scanner.nextLine().trim());
                voltar = processarSelecaoAnuncio(scanner, escolha);
                
            } catch (NumberFormatException e) {
                menuService.exibirNumeroInvalido();
                menuService.pausar();
            }
        }
    }
    
    /**
     * Processa a seleção de um anúncio feita pelo usuário.
     * Reduz o tamanho e acoplamento do método gerenciarMeusAnuncios().
     * 
     * @return true se deve voltar ao menu anterior, false caso contrário
     */
    private boolean processarSelecaoAnuncio(Scanner scanner, int escolha) {
        if (escolha == 0) {
            return true; // Voltar ao menu principal
        }
        
        if (escolha < 1 || escolha > meusAnuncios.size()) {
            menuService.exibirOpcaoInvalida();
            menuService.pausar();
            return false; // Continuar no loop
        }
        
        Anuncio anuncioSelecionado = meusAnuncios.get(escolha - 1);
        gerenciarAnuncioEspecifico(scanner, anuncioSelecionado);
        return false; // Continuar no loop
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
    private void anexarObserversAosAnuncios() {
        // Delegado a AnuncioService
        anuncioService.anexarObserversEmLote(meusAnuncios);
    }
    
    /**
     * Gerencia um anúncio específico com opções baseadas no estado atual
     */
    /**
     * Gerencia um anúncio específico com opções baseadas no estado atual
     * Delegado a AnuncioManagementService
     */
    private void gerenciarAnuncioEspecifico(Scanner scanner, Anuncio anuncio) {
        AnuncioManagementService managementService = new AnuncioManagementService(
            persistenciaService,
            uiController,
            meusAnuncios
        );
        managementService.gerenciarAnuncioEspecifico(scanner, anuncio);
    }
    
    /**
     * RF07 - Exibir configurações (Singleton)
     */
    public void exibirConfiguracoes() {
        Scanner scanner = new Scanner(System.in);
        boolean voltar = false;
        
        while (!voltar) {
            menuService.exibirCabecalhoConfiguracoes();
            menuService.exibirOpcoesCofiguracoes();
            
            try {
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
                        menuService.exibirOpcaoInvalida();
                }
                
                if (opcao != 0) {
                    menuService.pausar();
                }
            } catch (NumberFormatException e) {
                menuService.exibirErro("Opção inválida! Digite um número.");
            }
        }
    }
    
    /**
     * RF05 - STRATEGY PATTERN: Configurar canal de notificação
     * Delegado a NotificationConfigService
     */
    private void configurarCanalNotificacao(Scanner scanner) {
        NotificationConfigService configService = new NotificationConfigService(
            new EmailService(),
            new SMSService(),
            new WhatsAppService()
        );
        configService.configurarCanalNotificacao(scanner, usuarioAtual);
    }
    
     /**
     * Testa o canal de notificação configurado
     */
    /**
     * Exibe informações do sistema (RF07)
     * Delegado a SystemInfoService para desacoplamento
     */
    private void exibirInformacoesDoSistema() {
        systemInfoService.exibirInformacoes(usuarioAtual);
    }
    
    /**
     * Demonstra todos os padrões GoF implementados
     * Delegado a PatternsService para desacoplamento
     */
    public void demonstrarPadroesGoF() {
        patternsService.demonstrarTodosPadroes();
    }
}
