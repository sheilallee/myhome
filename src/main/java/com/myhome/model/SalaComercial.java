package com.myhome.model;

/**
 * RF01 - PRODUTO CONCRETO (Factory Method Pattern)
 * 
 * Representa uma Sala Comercial no sistema MyHome.
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Andar da sala
 * - Presença de banheiro próprio
 * - Capacidade de pessoas
 * - Vagas de estacionamento
 */
public class SalaComercial extends Imovel {
    
    // ========================================
    // ATRIBUTOS ESPECÍFICOS DE SALA COMERCIAL
    // ========================================
    
    private int andar;
    private boolean temBanheiro;
    private int capacidadePessoas;
    private int vagasEstacionamento;
    
    // ========================================
    // CONSTRUTORES
    // ========================================
    
    public SalaComercial() {
        super();
        this.tipo = "Sala Comercial";
    }
    
    private SalaComercial(SalaComercial original) {
        super();
        this.id = original.id;
        this.tipo = original.tipo;
        this.area = original.area;
        this.endereco = original.endereco;
        this.descricao = original.descricao;
        this.caracteristicas.putAll(original.caracteristicas);
        
        this.andar = original.andar;
        this.temBanheiro = original.temBanheiro;
        this.capacidadePessoas = original.capacidadePessoas;
        this.vagasEstacionamento = original.vagasEstacionamento;
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
        return new SalaComercial(this);
    }
    
    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    public int getAndar() {
        return andar;
    }
    
    public void setAndar(int andar) {
        this.andar = andar;
    }
    
    public boolean isTemBanheiro() {
        return temBanheiro;
    }
    
    public void setTemBanheiro(boolean temBanheiro) {
        this.temBanheiro = temBanheiro;
    }
    
    public int getCapacidadePessoas() {
        return capacidadePessoas;
    }
    
    public void setCapacidadePessoas(int capacidadePessoas) {
        this.capacidadePessoas = capacidadePessoas;
    }
    
    public int getVagasEstacionamento() {
        return vagasEstacionamento;
    }
    
    public void setVagasEstacionamento(int vagasEstacionamento) {
        this.vagasEstacionamento = vagasEstacionamento;
    }
    
    @Override
    public String toString() {
        return String.format("Sala Comercial - %.2fm², %dº andar, Cap: %d pessoas - %s", 
            area, andar, capacidadePessoas, endereco);
    }
}
