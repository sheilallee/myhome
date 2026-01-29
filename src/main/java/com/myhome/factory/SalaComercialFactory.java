package com.myhome.factory;

import com.myhome.model.Imovel;
import com.myhome.model.SalaComercial;

/**
 * RF01 - CONCRETE CREATOR (Factory Method Pattern)
 * 
 * Factory concreta para criação de objetos SalaComercial.
 */
public class SalaComercialFactory implements ImovelFactory {
    
    /**
     * Implementação do Factory Method para criar Salas Comerciais.
     * 
     * @return Nova instância de SalaComercial
     */
    @Override
    public Imovel criarImovel() {
        return new SalaComercial();
    }
    
    /**
     * Método auxiliar para criar sala comercial com configurações básicas.
     * 
     * @param area Área em m²
     * @param andar Andar da sala
     * @param capacidade Capacidade de pessoas
     * @return SalaComercial pré-configurada
     */
    public SalaComercial criarSalaComercialBasica(double area, int andar, int capacidade) {
        SalaComercial sala = new SalaComercial();
        sala.setArea(area);
        sala.setAndar(andar);
        sala.setCapacidadePessoas(capacidade);
        return sala;
    }
}
