package com.myhome.service;

import com.myhome.factory.*;
import com.myhome.model.*;

/**
 * SERVIÇO DE GERENCIAMENTO DE IMÓVEIS
 * 
 * RESPONSABILIDADE:
 * - Encapsular a lógica de criação de imóveis usando Factory Method
 * - Fornecer interface de alto nível para o cliente
 * - Ocultar detalhes de implementação das factories
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por gerenciar imóveis
 * - DIP: Cliente depende desta interface, não das factories concretas
 * - OCP: Novos tipos podem ser adicionados sem modificar código existente
 */
public class ImovelService {
    
    /**
     * Cria uma casa com configuração padrão básica.
     * 
     * @param endereco Endereço da casa
     * @param area Área total em m²
     * @return Casa configurada e validada
     */
    public Casa criarCasaBasica(Endereco endereco, double area) {
        ImovelFactory factory = new CasaFactory();
        Casa casa = (Casa) factory.criarImovel();
        
        casa.setEndereco(endereco);
        casa.setArea(area);
        casa.setQuartos(2);
        casa.setBanheiros(1);
        casa.setTemGaragem(true);
        casa.setVagas(1);
        
        return casa;
    }
    
    /**
     * Cria uma casa personalizada com todas as características.
     */
    public Casa criarCasa(Endereco endereco, double area, int quartos, int banheiros, 
                         boolean temQuintal, boolean temGaragem, int vagas) {
        ImovelFactory factory = new CasaFactory();
        Casa casa = (Casa) factory.criarImovel();
        
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
        ImovelFactory factory = new ApartamentoFactory();
        Apartamento apartamento = (Apartamento) factory.criarImovel();
        
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
        ImovelFactory factory = new ApartamentoFactory();
        Apartamento apartamento = (Apartamento) factory.criarImovel();
        
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
        ImovelFactory factory = new TerrenoFactory();
        Terreno terreno = (Terreno) factory.criarImovel();
        
        terreno.setEndereco(endereco);
        terreno.setArea(area);
        terreno.setAreaTerreno(area);
        terreno.setZoneamento(zoneamento);
        terreno.setTopografia("Plano");
        
        return terreno;
    }
    
    /**
     * Cria uma sala comercial básica.
     */
    public SalaComercial criarSalaComercialBasica(Endereco endereco, double area, int andar) {
        ImovelFactory factory = new SalaComercialFactory();
        SalaComercial sala = (SalaComercial) factory.criarImovel();
        
        sala.setEndereco(endereco);
        sala.setArea(area);
        sala.setAndar(andar);
        sala.setCapacidadePessoas(10);
        sala.setTemBanheiro(true);
        
        return sala;
    }
}
