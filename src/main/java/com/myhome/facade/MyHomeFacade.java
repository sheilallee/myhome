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
    
    private final MenuService menuService;
    private final UIController uiController;
    private final ImovelService imovelService;
    private final AnuncioService anuncioService;
    private final ValidadorService validadorService;
    private final UsuarioService usuarioService;
    private final PersistenciaService persistenciaService;
    private final SystemInfoService systemInfoService;
    private final PatternsService patternsService;

    private List<Anuncio> meusAnuncios;
    private int contadorAnuncios;
    // RF05 - Strategy: Usuário com canal de notificação configurável
    private Usuario usuarioAtual;
    public MyHomeFacade() {
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
        
        // RF05 - Strategy: Criar usuário com canal de notificação padrão
        this.usuarioAtual = new Usuario("User", "jayradpro@gmail.com", "(83) 8888-8888");
        this.usuarioAtual.setCanalNotificacao(new EmailNotificacao(new EmailService()));
    }
    
    // RF04 - Observer: Monitorar estado dos anúncios
    public void executar() {
        Scanner scanner = uiController.getScanner();
        boolean continuar = true;
        
        meusAnuncios = persistenciaService.carregarAnuncios();
        contadorAnuncios = meusAnuncios.size();
        
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
    
    // RF03 - Factory Method: Criar diferentes tipos de anúncio
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

    // RF06 - Decorator: Busca com filtros dinâmicos
    public void executarBusca(UIController uiController) {
        String[] filtros = uiController.coletarFiltrosBusca();
        String precoMin = filtros[0];
        String precoMax = filtros[1];
        String cidade = filtros[2];
        String estado = filtros[3];
        String tipo = filtros[4];
        
        SearchFilterService searchService = new SearchFilterService(uiController);
        BuscaFiltro busca = searchService.aplicarFiltros(meusAnuncios, precoMin, precoMax, cidade, estado, tipo);
        List<Anuncio> resultados = searchService.executar(busca);
        uiController.exibirResultadoBusca(resultados);
    }
    
    // RF01 - Factory: Visualizar anúncios
    public void exibirMeusAnuncios(UIController uiController) {
        Scanner scanner = uiController.getScanner();
        gerenciarMeusAnuncios(scanner);
    }
    
    // RF01 - Builder: Criar imóvel interativamente
    public void criarAnuncioInterativo(Scanner scanner) {
        menuService.exibirCabecalhoCriarAnuncioInterativo();
        
        try {
            Imovel imovel = imovelService.criarImovelInterativo(scanner);
            
            if (imovel == null) {
                menuService.exibirCancelamentoCriacaoImovel();
                return;
            }
            
            menuService.exibirSucessoCriacaoImovelInterativo(imovel.getTipo(), imovel.getEndereco().toString(), imovel.getArea());
            
            Anuncio anuncio = anuncioService.criarAnuncioInterativo(scanner, imovel);
            
            if (anuncio == null) {
                menuService.exibirCancelamentoCriacaoAnuncio();
                return;
            }
            
            meusAnuncios.add(anuncio);
            contadorAnuncios++;
            persistenciaService.salvarAnuncios(meusAnuncios);
            exibirResultadoAnuncio(anuncio);
            
        } catch (Exception e) {
            menuService.exibirErroCriacaoAnuncio(e.getMessage());
        }
    }
    
    // RF02 - Prototype: Clonar imóvel pré-configurado
    public void criarAnuncioDePrototipo(Scanner scanner) {
        menuService.exibirCabecalhoCriarAnuncioPrototipo();
        
        try {
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
            Imovel imovel = registro.obterPrototipo(chavePrototipo);
            
            if (imovel == null) {
                menuService.exibirErroPrototipoNaoEncontrado();
                return;
            }
            
            menuService.exibirCabecalhoImovelClonado();
            menuService.exibirDetalhesClonagemImovel(registro.gerarDescricaoPrototipo(imovel), imovel.hashCode());
            
            imovelService.customizarImovelClonado(scanner, imovel);
            
            if (!imovel.validar()) {
                menuService.exibirErroValidacaoImovel();
                return;
            }
            
            menuService.exibirSucessoValidacaoImovel();
            
            Anuncio anuncio = anuncioService.criarAnuncioInterativo(scanner, imovel);
            
            if (anuncio == null) {
                menuService.exibirCancelamentoCriacaoAnuncio();
                return;
            }
            
            meusAnuncios.add(anuncio);
            contadorAnuncios++;
            persistenciaService.salvarAnuncios(meusAnuncios);
            exibirResultadoAnuncio(anuncio);
            
        } catch (NumberFormatException e) {
            menuService.exibirErroEntradaInvalidaNumero();
        } catch (Exception e) {
            menuService.exibirErroCriacaoAnuncio("(Protótipo) " + e.getMessage());
        }
    }
    
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
    
    public void exibirMeusAnuncios() {
        menuService.exibirCabecalhoMeusAnuncios();
        
        if (meusAnuncios.isEmpty()) {
            menuService.exibirNenhumAnuncioMeusList();
            return;
        }
        
        menuService.exibirListaAnunciosCompleta(meusAnuncios);
    }
    
    // RF04 - State: Gerenciar estado dos anúncios com transições
    private void gerenciarMeusAnuncios(Scanner scanner) {
        boolean voltar = false;
        
        while (!voltar) {
            menuService.exibirCabecalhoGerenciarAnunciosMenu();
            
            if (meusAnuncios.isEmpty()) {
                menuService.exibirNenhumAnuncio();
                return;
            }
            
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
    
    private boolean processarSelecaoAnuncio(Scanner scanner, int escolha) {
        if (escolha == 0) {
            return true;
        }
        
        if (escolha < 1 || escolha > meusAnuncios.size()) {
            menuService.exibirOpcaoInvalida();
            menuService.pausar();
            return false;
        }
        
        Anuncio anuncioSelecionado = meusAnuncios.get(escolha - 1);
        gerenciarAnuncioEspecifico(scanner, anuncioSelecionado);
        return false;
    }
    // RF04 - Observer: Re-anexar observers aos anúncios carregados
    private void anexarObserversAosAnuncios() {
        anuncioService.anexarObserversEmLote(meusAnuncios);
    }
    
    // RF04 - State: Gerenciar transições de estado do anúncio
    private void gerenciarAnuncioEspecifico(Scanner scanner, Anuncio anuncio) {
        AnuncioManagementService managementService = new AnuncioManagementService(
            persistenciaService,
            uiController,
            meusAnuncios
        );
        managementService.gerenciarAnuncioEspecifico(scanner, anuncio);
    }
    
    // RF07 - Singleton: Gerenciar configurações do sistema
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
    
    // RF05 - Strategy: Configurar canal de notificação dinamicamente
    private void configurarCanalNotificacao(Scanner scanner) {
        NotificationConfigService configService = new NotificationConfigService(
            new EmailService(),
            new SMSService(),
            new WhatsAppService()
        );
        configService.configurarCanalNotificacao(scanner, usuarioAtual);
    }
    
    // RF07 - Singleton: Exibir informações do sistema
    private void exibirInformacoesDoSistema() {
        systemInfoService.exibirInformacoes(usuarioAtual);
    }
    
    // Demonstra todos os padrões GoF implementados
    public void demonstrarPadroesGoF() {
        patternsService.demonstrarTodosPadroes();
    }
}
