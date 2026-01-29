package com.myhome.model;

/**
 * RF01 - PRODUTO CONCRETO (Factory Method Pattern)
 * 
 * Representa um Apartamento no sistema MyHome.
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Número de quartos
 * - Número de banheiros
 * - Andar do apartamento
 * - Presença de elevador
 * - Número de vagas de garagem
 */
public class Apartamento extends Imovel {
    
    // ========================================
    // ATRIBUTOS ESPECÍFICOS DE APARTAMENTO
    // ========================================
    
    private int quartos;
    private int banheiros;
    private int andar;
    private boolean temElevador;
    private int vagas;
    
    // ========================================
    // CONSTRUTORES
    // ========================================
    
    /**
     * Construtor padrão.
     * Usado pela ApartamentoFactory.
     */
    public Apartamento() {
        super();
        this.tipo = "Apartamento";
    }
    
    /**
     * Construtor privado para clonagem (Prototype Pattern).
     */
    private Apartamento(Apartamento original) {
        super();
        this.id = original.id;
        this.tipo = original.tipo;
        this.area = original.area;
        this.endereco = original.endereco;
        this.descricao = original.descricao;
        this.caracteristicas.putAll(original.caracteristicas);
        
        this.quartos = original.quartos;
        this.banheiros = original.banheiros;
        this.andar = original.andar;
        this.temElevador = original.temElevador;
        this.vagas = original.vagas;
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
        return new Apartamento(this);
    }
    
    // ========================================
    // VALIDAÇÃO ESPECÍFICA
    // ========================================
    
    @Override
    public boolean validar() {
        if (!super.validar()) {
            return false;
        }
        
        if (quartos <= 0 || banheiros <= 0) {
            return false;
        }
        
        if (andar < 0) {
            return false;
        }
        
        return true;
    }
    
    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    public int getQuartos() {
        return quartos;
    }
    
    public void setQuartos(int quartos) {
        this.quartos = quartos;
    }
    
    public int getBanheiros() {
        return banheiros;
    }
    
    public void setBanheiros(int banheiros) {
        this.banheiros = banheiros;
    }
    
    public int getAndar() {
        return andar;
    }
    
    public void setAndar(int andar) {
        this.andar = andar;
    }
    
    public boolean isTemElevador() {
        return temElevador;
    }
    
    public void setTemElevador(boolean temElevador) {
        this.temElevador = temElevador;
    }
    
    public int getVagas() {
        return vagas;
    }
    
    public void setVagas(int vagas) {
        this.vagas = vagas;
    }
    
    @Override
    public String toString() {
        return String.format("Apartamento - %d quartos, %d banheiros, %dº andar, %.2fm²%s - %s", 
            quartos, banheiros, andar, area,
            temElevador ? " (com elevador)" : "",
            endereco);
    }
}
