package com.myhome.model;

/**
 * RF01 - PRODUTO CONCRETO (Factory Method Pattern)
 * 
 * Representa uma Casa no sistema MyHome.
 * 
 * RESPONSABILIDADES:
 * - Armazenar atributos específicos de casas
 * - Implementar comportamento de clonagem (Prototype - RF02)
 * - Ser criada pela CasaFactory (Factory Method)
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Número de quartos
 * - Número de banheiros  
 * - Presença de quintal
 * - Presença de garagem
 * - Número de vagas na garagem
 */
public class Casa extends Imovel {
    
    // ========================================
    // ATRIBUTOS ESPECÍFICOS DE CASA
    // ========================================
    
    private int quartos;
    private int banheiros;
    private boolean temQuintal;
    private boolean temGaragem;
    private int vagas; // Vagas na garagem
    
    // ========================================
    // CONSTRUTORES
    // ========================================
    
    /**
     * Construtor padrão.
     * Usado pela Factory para criar novas instâncias.
     */
    public Casa() {
        super();
        this.tipo = "Casa";
    }
    
    /**
     * Construtor privado para clonagem.
     * Usado pelo método clone() para criar cópias.
     * 
     * @param original Casa original a ser clonada
     */
    private Casa(Casa original) {
        super();
        // Copia atributos da classe base
        this.id = original.id;
        this.tipo = original.tipo;
        this.area = original.area;
        this.endereco = original.endereco;
        this.descricao = original.descricao;
        
        // Copia características (deep copy)
        this.caracteristicas.putAll(original.caracteristicas);
        
        // Copia atributos específicos de Casa
        this.quartos = original.quartos;
        this.banheiros = original.banheiros;
        this.temQuintal = original.temQuintal;
        this.temGaragem = original.temGaragem;
        this.vagas = original.vagas;
    }
    
    // ========================================
    // IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS
    // ========================================
    
    /**
     * Retorna o tipo do imóvel.
     * 
     * PADRÃO TEMPLATE METHOD:
     * Implementação concreta do método abstrato da classe pai.
     */
    @Override
    public String getTipo() {
        return this.tipo;
    }
    
    /**
     * Clona a casa criando uma cópia independente.
     * 
     * PADRÃO PROTOTYPE (RF02):
     * Cria uma nova instância com os mesmos valores,
     * mas como objeto independente (deep copy).
     * 
     * Útil para criar imóveis baseados em protótipos padrão.
     */
    @Override
    public Imovel clone() {
        return new Casa(this);
    }
    
    // ========================================
    // VALIDAÇÃO ESPECÍFICA
    // ========================================
    
    /**
     * Valida se a casa possui informações válidas.
     * 
     * REGRA DE NEGÓCIO:
     * - Validação base (área e endereço)
     * - Quartos e banheiros devem ser maior que zero
     * - Se tem garagem, deve ter pelo menos 1 vaga
     */
    @Override
    public boolean validar() {
        // Validação da classe base
        if (!super.validar()) {
            return false;
        }
        
        // Validações específicas de Casa
        if (quartos <= 0 || banheiros <= 0) {
            return false;
        }
        
        if (temGaragem && vagas <= 0) {
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
    
    public boolean isTemQuintal() {
        return temQuintal;
    }
    
    public void setTemQuintal(boolean temQuintal) {
        this.temQuintal = temQuintal;
    }
    
    public boolean isTemGaragem() {
        return temGaragem;
    }
    
    public void setTemGaragem(boolean temGaragem) {
        this.temGaragem = temGaragem;
    }
    
    public int getVagas() {
        return vagas;
    }
    
    public void setVagas(int vagas) {
        this.vagas = vagas;
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    @Override
    public String toString() {
        return String.format("Casa - %d quartos, %d banheiros, %.2fm² - %s%s%s", 
            quartos, banheiros, area, endereco,
            temQuintal ? " (com quintal)" : "",
            temGaragem ? String.format(" (%d vaga(s))", vagas) : "");
    }
}
