package com.myhome.service;

import com.myhome.builder.ImovelBuilder;
import com.myhome.builder.ImovelBuilderImpl;
import com.myhome.model.*;
import java.util.Scanner;

// Serviço de criação de imóveis usando Builder Pattern
public class ImovelService {
    
    private final MenuService menuService;
    private final ValidadorService validadorService;
    
    public ImovelService(MenuService menuService, ValidadorService validadorService) {
        this.menuService = menuService;
        this.validadorService = validadorService;
    }
    
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
        String endereco = menuService.lerTexto("\n📍 Digite o endereço completo: ");
        if (!validadorService.validarTextoNaoVazio(endereco)) {
            menuService.exibirErro("Endereço não pode ser vazio!");
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
}
