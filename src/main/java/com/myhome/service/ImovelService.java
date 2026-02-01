package com.myhome.service;

import com.myhome.builder.ImovelBuilder;
import com.myhome.builder.ImovelBuilderImpl;
import com.myhome.model.*;
import java.util.Scanner;

/**
 * Serviço de criação de imóveis usando Builder Pattern e Factory Pattern
 */
public class ImovelService {
    
    private final MenuService menuService;
    private final ValidadorService validadorService;
    
    public ImovelService(MenuService menuService, ValidadorService validadorService) {
        this.menuService = menuService;
        this.validadorService = validadorService;
    }
    
    /**
     * Cria um imóvel interativamente através de linha de comando usando Builder Pattern.
     */
    public Imovel criarImovelInterativo(Scanner scanner) {
        menuService.exibirPasso("PASSO 1: CRIAR IMÓVEL (BUILDER)");
        
        // Escolher tipo de imóvel
        System.out.println("🏘️  Tipo de Imóvel:");
        System.out.println("  [1] Casa");
        System.out.println("  [2] Apartamento");
        System.out.println("  [3] Terreno");
        System.out.println("  [4] Sala Comercial");
        
        int tipoOpcao = menuService.lerOpcao("➤ Escolha: ");
        String tipo;
        
        switch (tipoOpcao) {
            case 1: tipo = "casa"; break;
            case 2: tipo = "apartamento"; break;
            case 3: tipo = "terreno"; break;
            case 4: tipo = "sala_comercial"; break;
            default:
                menuService.exibirErro("Tipo inválido!");
                return null;
        }
        
        // Dados básicos
        //ajustar para endereco do tipo Endereco
        Endereco endereco = new Endereco(menuService.lerTexto("\n📍 Digite a rua: "),
                                         menuService.lerTexto("🏠 Digite o número: "),
                                         menuService.lerTexto("🏙️ Digite a cidade: "),
                                         menuService.lerTexto("📍 Digite o estado: "));
        if (!validadorService.validarEndereco(endereco)) {
            menuService.exibirErro("Endereço inválido!");
            return null;
        }
        
        double area = menuService.lerDecimal("📏 Digite a área (m²): ");
        if (!validadorService.validarNumeroPositivo(area)) {
            menuService.exibirErro("Área deve ser maior que zero!");
            return null;
        }
        
        // Criar usando Builder
        ImovelBuilder builder = new ImovelBuilderImpl();
        builder.setTipo(tipo)
               .setEndereco(endereco)
               .setArea(area);
        
        // Atributos específicos por tipo
        coletarAtributosEspecificos(scanner, tipo, builder);
        
        return builder.build();
    }
    
    /**
     * Coleta atributos específicos de cada tipo de imóvel.
     */
    private void coletarAtributosEspecificos(Scanner scanner, String tipo, ImovelBuilder builder) {
        if (tipo.equals("casa")) {
            int quartos = menuService.lerOpcao("🛌 Quartos: ");
            int banheiros = menuService.lerOpcao("🚿 Banheiros: ");
            boolean temQuintal = menuService.lerConfirmacao("🌳 Tem quintal? (s/n): ");
            boolean temGaragem = menuService.lerConfirmacao("🚗 Tem garagem? (s/n): ");
            
            builder.setQuartos(quartos)
                   .setBanheiros(banheiros)
                   .setTemQuintal(temQuintal)
                   .setTemGaragem(temGaragem);
                   
        } else if (tipo.equals("apartamento")) {
            int quartos = menuService.lerOpcao("🛌 Quartos: ");
            int banheiros = menuService.lerOpcao("🚿 Banheiros: ");
            int andar = menuService.lerOpcao("🏢 Andar: ");
            int vagas = menuService.lerOpcao("🅿️  Vagas de garagem: ");
            boolean temElevador = menuService.lerConfirmacao("🛗 Tem elevador? (s/n): ");
            
            builder.setQuartos(quartos)
                   .setBanheiros(banheiros)
                   .setAndar(andar)
                   .setVagas(vagas)
                   .setTemElevador(temElevador);
                   
        } else if (tipo.equals("terreno")) {
            String zoneamento = menuService.lerTexto("🏭 Zoneamento (residencial/comercial/misto): ");
            String topografia = menuService.lerTexto("📊 Topografia (plano/aclive/declive): ");
            
            builder.setZoneamento(zoneamento)
                   .setTopografia(topografia);
                   
        } else if (tipo.equals("sala_comercial")) {
            boolean temBanheiro = menuService.lerConfirmacao("🚻 Tem banheiro? (s/n): ");
            int capacidade = menuService.lerOpcao("👥 Capacidade de pessoas: ");
            
            builder.setTemBanheiro(temBanheiro)
                   .setCapacidadePessoas(capacidade);
        }
    }
    
    
    // =====================================================
    // MÉTODOS DE CRIAÇÃO COM FACTORY PATTERN
    // =====================================================

    /**
     * Cria uma casa com configuração padrão básica.
     * 
     * @param endereco Endereço da casa
     * @param area Área total em m²
     * @return Casa configurada e validada
     */
    public Casa criarCasaBasica(Endereco endereco, double area) {
        Casa casa = new Casa();
        
        casa.setEndereco(endereco);
        casa.setArea(area);
        
        return casa;
    }
    
    /**
     * Cria uma casa com todas as configurações.
     */
    public Casa criarCasa(Endereco endereco, double area, int quartos, int banheiros, 
                         boolean temQuintal, boolean temGaragem, int vagas) {
        Casa casa = new Casa();
        
        casa.setEndereco(endereco);
        casa.setArea(area);
        casa.setQuartos(quartos);
        casa.setBanheiros(banheiros);
        casa.setTemQuintal(temQuintal);
        casa.setTemGaragem(temGaragem);
        casa.setVagas(vagas);
        
        return casa;
    }
    
    /**
     * Cria um apartamento com configuração padrão.
     */
    public Apartamento criarApartamentoBasico(Endereco endereco, double area, int andar) {
        Apartamento apartamento = new Apartamento();
        
        apartamento.setEndereco(endereco);
        apartamento.setArea(area);
        apartamento.setAndar(andar);
        apartamento.setQuartos(2);
        apartamento.setBanheiros(1);
        apartamento.setTemElevador(andar > 2);
        apartamento.setVagas(1);
        
        return apartamento;
    }
    
    /**
     * Cria um apartamento personalizado.
     */
    public Apartamento criarApartamento(Endereco endereco, double area, int quartos, 
                                       int banheiros, int andar, boolean temElevador, int vagas) {
        Apartamento apartamento = new Apartamento();
        
        apartamento.setEndereco(endereco);
        apartamento.setArea(area);
        apartamento.setQuartos(quartos);
        apartamento.setBanheiros(banheiros);
        apartamento.setAndar(andar);
        apartamento.setTemElevador(temElevador);
        apartamento.setVagas(vagas);
        
        return apartamento;
    }
    
    /**
     * Cria um terreno básico.
     */
    public Terreno criarTerrenoBasico(Endereco endereco, double area, String zoneamento) {
        Terreno terreno = new Terreno();
        
        terreno.setEndereco(endereco);
        terreno.setArea(area);
        terreno.setZoneamento(zoneamento);
        terreno.setTopografia("Plano");
        
        return terreno;
    }
    
    /**
     * Cria uma sala comercial básica.
     */
    public SalaComercial criarSalaComercialBasica(Endereco endereco, double area, int andar) {
        SalaComercial sala = new SalaComercial();
        
        sala.setEndereco(endereco);
        sala.setArea(area);
        sala.setAndar(andar);
        sala.setCapacidadePessoas(10);
        sala.setTemBanheiro(true);
        
        return sala;
    }
}
