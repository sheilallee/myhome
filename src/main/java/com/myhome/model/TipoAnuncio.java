package com.myhome.model;

/**
 * RF01 - ENUM DE TIPOS DE ANÚNCIO
 * 
 * Define os tipos de anúncio disponíveis no sistema MyHome.
 * Utilizado pelas AnuncioFactories (Factory Method Pattern).
 */
public enum TipoAnuncio {
    
    /**
     * Anúncio de venda de imóvel.
     * Transferência definitiva de propriedade.
     */
    VENDA("Venda"),
    
    /**
     * Anúncio de aluguel de longa duração.
     * Locação residencial ou comercial.
     */
    ALUGUEL("Aluguel"),
    
    /**
     * Anúncio de aluguel por temporada.
     * Locação de curta duração para férias.
     */
    TEMPORADA("Temporada");
    
    private final String descricao;
    
    TipoAnuncio(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}
