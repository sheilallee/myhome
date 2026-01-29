package com.myhome.model;

/**
 * RF01 - PRODUTO CONCRETO (Factory Method Pattern)
 * 
 * Representa um Terreno no sistema MyHome.
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Área do terreno
 * - Tipo de zoneamento (Residencial, Comercial, Misto, Industrial)
 * - Topografia (Plano, Aclive, Declive)
 */
public class Terreno extends Imovel {
    
    // ========================================
    // ATRIBUTOS ESPECÍFICOS DE TERRENO
    // ========================================
    
    private double areaTerreno; // Pode ser diferente da área construível
    private String zoneamento; // Residencial, Comercial, Misto, Industrial
    private String topografia; // Plano, Aclive, Declive
    
    // ========================================
    // CONSTRUTORES
    // ========================================
    
    public Terreno() {
        super();
        this.tipo = "Terreno";
    }
    
    private Terreno(Terreno original) {
        super();
        this.id = original.id;
        this.tipo = original.tipo;
        this.area = original.area;
        this.endereco = original.endereco;
        this.descricao = original.descricao;
        this.caracteristicas.putAll(original.caracteristicas);
        
        this.areaTerreno = original.areaTerreno;
        this.zoneamento = original.zoneamento;
        this.topografia = original.topografia;
    }
    
    // ========================================
    // IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS
    // ========================================
    
    @Override
    public String getTipo() {
        return this.tipo;
    }
    
    @Override
    public Imovel clone() {
        return new Terreno(this);
    }
    
    // ========================================
    // VALIDAÇÃO ESPECÍFICA
    // ========================================
    
    @Override
    public boolean validar() {
        if (!super.validar()) {
            return false;
        }
        
        if (zoneamento == null || zoneamento.trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    public double getAreaTerreno() {
        return areaTerreno;
    }
    
    public void setAreaTerreno(double areaTerreno) {
        this.areaTerreno = areaTerreno;
    }
    
    public String getZoneamento() {
        return zoneamento;
    }
    
    public void setZoneamento(String zoneamento) {
        this.zoneamento = zoneamento;
    }
    
    public String getTopografia() {
        return topografia;
    }
    
    public void setTopografia(String topografia) {
        this.topografia = topografia;
    }
    
    @Override
    public String toString() {
        return String.format("Terreno - %.2fm², Zoneamento: %s, Topografia: %s - %s", 
            area, zoneamento, topografia, endereco);
    }
}
