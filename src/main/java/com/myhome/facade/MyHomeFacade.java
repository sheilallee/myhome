package com.myhome.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.myhome.service.CSVDataLoader;
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
    private final CSVDataLoader csvDataLoader;

    private List<Anuncio> meusAnuncios;
    private List<Usuario> usuariosRegistrados;
    private int contadorAnuncios;
    // RF05 - Strategy: Usuário com canal de notificação configurável
    private Usuario usuarioAtual;
    public MyHomeFacade() {
        this.menuService = new MenuService();
        this.validadorService = new ValidadorService();
        this.uiController = new UIController(menuService, validadorService);
        this.usuarioService = new UsuarioService();
        this.persistenciaService = new PersistenciaService();
        this.csvDataLoader = new CSVDataLoader();
        this.imovelService = new ImovelService(menuService, validadorService);
        this.anuncioService = new AnuncioService(menuService, validadorService, usuarioService);
        this.systemInfoService = new SystemInfoService(uiController);
        this.patternsService = new PatternsService();
        
        this.meusAnuncios = new ArrayList<>();
        this.usuariosRegistrados = new ArrayList<>();
        this.contadorAnuncios = 0;
    }
    
    // RF04 - Observer: Monitorar estado dos anúncios
    public void executar() {
        Scanner scanner = uiController.getScanner();
        boolean continuar = true;
        
        // E1 - Carregar seed data do CSV se JSON estiver vazio
        meusAnuncios = persistenciaService.carregarAnuncios();
        usuariosRegistrados = persistenciaService.carregarUsuarios();
        
        if (meusAnuncios.isEmpty()) {
            // Primeira execução - carregar seed data do CSV
            Map<String, Object> dadosCSV = csvDataLoader.carregarDadosIniciais();
            
            @SuppressWarnings("unchecked")
            List<Usuario> usuariosCSV = (List<Usuario>) dadosCSV.get("usuarios");
            @SuppressWarnings("unchecked")
            List<Anuncio> anunciosCSV = (List<Anuncio>) dadosCSV.get("anuncios");
            
            if (!usuariosCSV.isEmpty() && !anunciosCSV.isEmpty()) {
                usuariosRegistrados.addAll(usuariosCSV);
                meusAnuncios.addAll(anunciosCSV);
                
                // Persistir seed data
                persistenciaService.salvarUsuarios(usuariosRegistrados);
                persistenciaService.salvarAnuncios(meusAnuncios);
            }
        }
        
        contadorAnuncios = meusAnuncios.size();
        
        if (contadorAnuncios > 0) {
            anexarObserversAosAnuncios();
            uiController.exibirInfo(contadorAnuncios + " anúncio(s) carregado(s)!");
            uiController.exibirSucesso("Observers attachados para monitoramento de mudanças");
        }
        
        // Solicitar login/cadastro do usuário atual
        usuarioAtual = exibirTelaLogin(scanner);
        
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
            menuService.exibirCabecalho("⚙️  CONFIGURAÇÕES");
            System.out.println("\n  [1] Configurar canal de notificação");
            System.out.println("  [2] Editar perfil");
            System.out.println("  [3] Informações do sistema");
            System.out.println("  [0] Voltar\n");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                
                switch (opcao) {
                    case 1:
                        configurarCanalNotificacao(scanner);
                        break;
                    case 2:
                        editarPerfilUsuario(scanner);
                        break;
                    case 3:
                        exibirInformacoesDoSistema();
                        break;
                    case 0:
                        voltar = true;
                        break;
                    default:
                        menuService.exibirErro("Opção inválida!");
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
    
    // ===================================================================
    // TELA DE LOGIN E GERENCIAMENTO DE USUÁRIO
    // ===================================================================
    
    /**
     * Exibe tela de login e permite ao usuário selecionar conta ou criar nova
     */
    private Usuario exibirTelaLogin(Scanner scanner) {
        boolean loginValido = false;
        Usuario usuarioSelecionado = null;
        
        while (!loginValido) {
            menuService.exibirCabecalho("LOGIN / CADASTRO");
            System.out.println("\n🔐 Selecione uma opção:");
            System.out.println("\n  [1] Entrar com conta existente");
            System.out.println("  [2] Criar nova conta");
            System.out.println("  [0] Sair\n");
            
            int opcao = menuService.lerOpcao("Escolha: ");
            
            switch (opcao) {
                case 1:
                    usuarioSelecionado = selecionarUsuarioExistente();
                    if (usuarioSelecionado != null) {
                        loginValido = true;
                        menuService.exibirSucesso("Bem-vindo, " + usuarioSelecionado.getNome() + "!");
                        menuService.pausar();
                    }
                    break;
                    
                case 2:
                    usuarioSelecionado = criarNovoUsuario(scanner);
                    if (usuarioSelecionado != null) {
                        usuariosRegistrados.add(usuarioSelecionado);
                        persistenciaService.salvarUsuarios(usuariosRegistrados);
                        loginValido = true;
                        menuService.exibirSucesso("Conta criada com sucesso!");
                        menuService.pausar();
                    }
                    break;
                    
                case 0:
                    System.exit(0);
                    break;
                    
                default:
                    menuService.exibirErro("Opção inválida!");
            }
        }
        
        return usuarioSelecionado;
    }
    
    /**
     * Permite selecionar um usuário existente
     */
    private Usuario selecionarUsuarioExistente() {
        if (usuariosRegistrados.isEmpty()) {
            menuService.exibirErro("Nenhuma conta cadastrada!");
            menuService.pausar();
            return null;
        }
        
        menuService.exibirCabecalho("SELECIONE SUA CONTA");
        System.out.println();
        
        for (int i = 0; i < usuariosRegistrados.size(); i++) {
            Usuario u = usuariosRegistrados.get(i);
            System.out.println("  [" + (i + 1) + "] " + u.getNome() + " (" + u.getEmail() + ")");
        }
        System.out.println("  [0] Cancelar\n");
        
        int escolha = menuService.lerOpcao("Escolha: ");
        
        if (escolha > 0 && escolha <= usuariosRegistrados.size()) {
            return usuariosRegistrados.get(escolha - 1);
        }
        
        return null;
    }
    
    /**
     * Permite criar um novo usuário
     */
    private Usuario criarNovoUsuario(Scanner scanner) {
        menuService.exibirCabecalho("CRIAR NOVA CONTA");
        
        String nome = menuService.lerTexto("\n👤 Nome completo: ");
        
        String email;
        while (true) {
            email = menuService.lerTexto("📧 Email: ");
            final String emailTemp = email; // variável final para usar em lambda
            
            // Verificar se email já existe
            if (usuariosRegistrados.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(emailTemp))) {
                menuService.exibirErro("Este email já está cadastrado!");
                continue;
            }
            
            if (validadorService.validarEmail(email)) {
                break;
            }
            menuService.exibirErro("Email inválido! Use o formato: exemplo@dominio.com");
        }
        
        String telefone;
        while (true) {
            String input = menuService.lerTexto("📱 Telefone (apenas números): ");
            telefone = validadorService.formatarTelefone(input);
            if (telefone != null) {
                break;
            }
            menuService.exibirErro("Telefone inválido! Digite 10 ou 11 dígitos (ex: 83988881111)");
        }
        
        Usuario novoUsuario = new Usuario(nome, email, telefone);
        novoUsuario.setTipo(Usuario.TipoUsuario.PROPRIETARIO);
        novoUsuario.setCanalNotificacao(new EmailNotificacao(new EmailService()));
        
        return novoUsuario;
    }
    
    /**
     * Permite editar perfil do usuário (email e telefone)
     */
    private void editarPerfilUsuario(Scanner scanner) {
        menuService.exibirCabecalho("✏️  EDITAR PERFIL");
        
        System.out.println("\n👤 Usuário atual: " + usuarioAtual.getNome());
        System.out.println("📧 Email: " + usuarioAtual.getEmail());
        System.out.println("📱 Telefone: " + usuarioAtual.getTelefone());
        
        System.out.println("\n[1] Alterar email");
        System.out.println("[2] Alterar telefone");
        System.out.println("[0] Cancelar\n");
        
        int opcao = menuService.lerOpcao("Escolha: ");
        
        switch (opcao) {
            case 1:
                alterarEmail();
                break;
            case 2:
                alterarTelefone();
                break;
        }
    }
    
    /**
     * Altera o email do usuário atual
     */
    private void alterarEmail() {
        menuService.exibirPasso("ALTERAR EMAIL");
        
        String novoEmail;
        while (true) {
            novoEmail = menuService.lerTexto("\n📧 Novo email: ");
            final String emailTemp = novoEmail; // variável final para usar em lambda
            
            // Verificar se email já existe (excluindo o próprio usuário)
            if (usuariosRegistrados.stream()
                    .anyMatch(u -> !u.getEmail().equals(usuarioAtual.getEmail()) && 
                                 u.getEmail().equalsIgnoreCase(emailTemp))) {
                menuService.exibirErro("Este email já está cadastrado por outro usuário!");
                continue;
            }
            
            if (validadorService.validarEmail(novoEmail)) {
                break;
            }
            menuService.exibirErro("Email inválido! Use o formato: exemplo@dominio.com");
        }
        
        usuarioAtual.setEmail(novoEmail);
        persistenciaService.salvarUsuarios(usuariosRegistrados);
        menuService.exibirSucesso("Email alterado com sucesso!");
    }
    
    /**
     * Altera o telefone do usuário atual
     */
    private void alterarTelefone() {
        menuService.exibirPasso("ALTERAR TELEFONE");
        
        String novoTelefone;
        while (true) {
            String input = menuService.lerTexto("\n📱 Novo telefone (apenas números): ");
            novoTelefone = validadorService.formatarTelefone(input);
            if (novoTelefone != null) {
                break;
            }
            menuService.exibirErro("Telefone inválido! Digite 10 ou 11 dígitos (ex: 83988881111)");
        }
        
        usuarioAtual.setTelefone(novoTelefone);
        persistenciaService.salvarUsuarios(usuariosRegistrados);
        menuService.exibirSucesso("Telefone alterado com sucesso!");
    }
}
