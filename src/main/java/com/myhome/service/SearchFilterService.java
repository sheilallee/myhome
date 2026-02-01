package com.myhome.service;

import java.util.List;

import com.myhome.controller.UIController;
import com.myhome.decorator.BuscaFiltro;
import com.myhome.decorator.BuscaPadrao;
import com.myhome.decorator.FiltroLocalizacaoDecorator;
import com.myhome.decorator.FiltroPrecoDecorator;
import com.myhome.decorator.FiltroTipoImovelDecorator;
import com.myhome.model.Anuncio;

/**
 * RF06 - SERVICE: Gerencia a busca avançada com filtros via Decorator Pattern
 * 
 * RESPONSABILIDADES:
 * - Orquestrar a composição de decorators
 * - Aplicar filtros de forma composável
 * - Encapsular a complexidade do Decorator Pattern
 * - Executar a busca
 * 
 * BENEFÍCIOS:
 * - Desacoplamento da lógica de filtros da Facade
 * - Facilita testes unitários dos filtros
 * - Centraliza lógica de construção dos decorators
 * - Permite fácil adição de novos filtros
 * 
 * PADRÃO: Decorator Pattern
 * - BuscaPadrao: Implementação base (retorna todos os anúncios)
 * - FiltroPrecoDecorator: Filtra por faixa de preço
 * - FiltroLocalizacaoDecorator: Filtra por cidade/estado
 * - FiltroTipoImovelDecorator: Filtra por tipo de imóvel
 */
public class SearchFilterService {
    
    private UIController uiController;
    
    /**
     * Construtor com injeção do UIController para mensagens de erro
     */
    public SearchFilterService(UIController uiController) {
        this.uiController = uiController;
    }
    
    /**
     * Aplica filtros aos anúncios de forma composável
     * 
     * Fluxo:
     * 1. Cria BuscaPadrao com todos os anúncios
     * 2. Envolve com FiltroPrecoDecorator se preços informados
     * 3. Envolve com FiltroLocalizacaoDecorator se cidade/estado informados
     * 4. Envolve com FiltroTipoImovelDecorator se tipo informado
     * 5. Retorna a chain completa (ou parcial se algum filtro estiver vazio)
     * 
     * @param anuncios Lista de anúncios a filtrar
     * @param precoMin Preço mínimo (pode estar vazio)
     * @param precoMax Preço máximo (pode estar vazio)
     * @param cidade Cidade para filtro de localização (pode estar vazia)
     * @param estado Estado para filtro de localização (pode estar vazio)
     * @param tipo Tipo de imóvel para filtro (pode estar vazio)
     * @return BuscaFiltro com todos os decorators aplicáveis compostos
     */
    public BuscaFiltro aplicarFiltros(
            List<Anuncio> anuncios,
            String precoMin,
            String precoMax,
            String cidade,
            String estado,
            String tipo) {
        
        // Iniciar com a busca padrão (retorna todos)
        BuscaFiltro busca = new BuscaPadrao(anuncios);
        
        // Aplicar filtro de preço se informado
        if (!precoMin.isEmpty() && !precoMax.isEmpty()) {
            try {
                double min = Double.parseDouble(precoMin);
                double max = Double.parseDouble(precoMax);
                busca = new FiltroPrecoDecorator(busca, min, max);
            } catch (NumberFormatException e) {
                uiController.exibirErro("Preços inválidos, filtro de preço ignorado.");
            }
        }
        
        // Aplicar filtro de localização se informado
        if (!cidade.isEmpty() && !estado.isEmpty()) {
            busca = new FiltroLocalizacaoDecorator(busca, cidade, estado);
        }
        
        // Aplicar filtro de tipo se informado
        if (!tipo.isEmpty()) {
            busca = new FiltroTipoImovelDecorator(busca, tipo);
        }
        
        return busca;
    }
    
    /**
     * Executar busca com filtros já aplicados
     * 
     * @param busca A chain de decorators já construída
     * @return Lista de anúncios que passaram por todos os filtros
     */
    public List<Anuncio> executar(BuscaFiltro busca) {
        System.out.println("🔍 Executando busca com filtros...\n");
        return busca.buscar();
    }
}
