package com.myhome.factory;

import com.myhome.model.Apartamento;
import com.myhome.model.Imovel;

/**
 * RF01 - CONCRETE CREATOR (Factory Method Pattern)
 * 
 * Factory concreta para criação de objetos Apartamento.
 */
public class ApartamentoFactory implements ImovelFactory {
    
    /**
     * Implementação do Factory Method para criar Apartamentos.
     * 
     * @return Nova instância de Apartamento
     */
    @Override
    public Imovel criarImovel() {
        return new Apartamento();
    }
    
    /**
     * Método auxiliar para criar apartamento com configurações básicas.
     * 
     * @param quartos Número de quartos
     * @param banheiros Número de banheiros
     * @param andar Andar do apartamento
     * @param area Área em m²
     * @return Apartamento pré-configurado
     */
    public Apartamento criarApartamentoBasico(int quartos, int banheiros, int andar, double area) {
        Apartamento apartamento = new Apartamento();
        apartamento.setQuartos(quartos);
        apartamento.setBanheiros(banheiros);
        apartamento.setAndar(andar);
        apartamento.setArea(area);
        return apartamento;
    }
}
