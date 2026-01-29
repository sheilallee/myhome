package com.myhome.factory;

import com.myhome.model.Imovel;
import com.myhome.model.Terreno;

/**
 * RF01 - CONCRETE CREATOR (Factory Method Pattern)
 * 
 * Factory concreta para criação de objetos Terreno.
 */
public class TerrenoFactory implements ImovelFactory {
    
    /**
     * Implementação do Factory Method para criar Terrenos.
     * 
     * @return Nova instância de Terreno
     */
    @Override
    public Imovel criarImovel() {
        return new Terreno();
    }
    
    /**
     * Método auxiliar para criar terreno com configurações básicas.
     * 
     * @param area Área em m²
     * @param zoneamento Tipo de zoneamento
     * @return Terreno pré-configurado
     */
    public Terreno criarTerrenoBasico(double area, String zoneamento) {
        Terreno terreno = new Terreno();
        terreno.setArea(area);
        terreno.setAreaTerreno(area);
        terreno.setZoneamento(zoneamento);
        return terreno;
    }
}
