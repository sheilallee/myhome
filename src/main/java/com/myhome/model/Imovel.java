package com.myhome.model;

import java.util.HashMap;
import java.util.Map;

/**
 * RF01 - ENTIDADE DE DOMÍNIO (Base para Factory Method + Prototype)
 * 
 * Classe abstrata que representa um Imóvel no sistema MyHome.
 * 
 * RESPONSABILIDADES:
 * - Definir atributos comuns a todos os tipos de imóveis
 * - Fornecer comportamento base compartilhado
 * - Servir como base para o padrão Template Method (getTipo)
 * - Suportar clonagem para o padrão Prototype (RF02)
 * 
 * HIERARQUIA:
 * Imovel (abstrata)
 *   ├── Casa
 *   ├── Apartamento
 *   ├── Terreno
 *   ├── SalaComercial
 *   └── Galpao
 * 
 * PADRÕES RELACIONADOS:
 * - RF01: Factory Method - Imóvel é o Product
 * - RF02: Prototype - Método clone() para clonar imóveis
 */
public abstract class Imovel implements Cloneable {
    
    // ========================================
    // ATRIBUTOS PROTEGIDOS (Compartilhados)
    // ========================================
    
    /**
     * Identificador único do imóvel
     */
    protected Long id;
    
    /**
     * Tipo do imóvel (Casa, Apartamento, Terreno, etc.)
     * Definido pelas subclasses
     */
    protected String tipo;
    
    /**
     * Área total do imóvel em metros quadrados
     */
    protected double area;
    
    /**
     * Endereço completo do imóvel
     * Em uma versão futura, pode ser um objeto Endereco
     */
    protected Endereco endereco;
    
    /**
     * Descrição adicional do imóvel
     */
    protected String descricao;
    
    /**
     * Características adicionais específicas de cada tipo
     * Permite extensibilidade sem alterar a classe base
     * 
     * Exemplos:
     * - "piscina" -> true
     * - "mobiliado" -> true
     * - "aceita_pet" -> false
     */
    protected Map<String, Object> caracteristicas;
    
    // ========================================
    // CONSTRUTOR
    // ========================================
    
    /**
     * Construtor protegido - apenas subclasses podem instanciar.
     * 
     * Inicializa o mapa de características vazio.
     */
    protected Imovel() {
        this.caracteristicas = new HashMap<>();
    }
    
    // ========================================
    // MÉTODOS ABSTRATOS (Template Method)
    // ========================================
    
    /**
     * Retorna o tipo específico do imóvel.
     * 
     * PADRÃO TEMPLATE METHOD:
     * Método abstrato que deve ser implementado pelas subclasses
     * concretas para retornar seu tipo específico.
     * 
     * @return Tipo do imóvel (ex: "Casa", "Apartamento")
     */
    public abstract String getTipo();
    
    /**
     * Clona o imóvel criando uma cópia profunda.
     * 
     * PADRÃO PROTOTYPE (RF02):
     * Cada subclasse deve implementar a lógica de clonagem
     * adequada para seus atributos específicos.
     * 
     * @return Cópia do imóvel
     */
    public abstract Imovel clone();
    
    // ========================================
    // MÉTODOS PÚBLICOS
    // ========================================
    
    /**
     * Valida se o imóvel possui as informações mínimas necessárias.
     * 
     * REGRA DE NEGÓCIO:
     * Todo imóvel deve ter área maior que zero e endereço informado.
     * 
     * @return true se válido, false caso contrário
     */
    public boolean validar() {
        if (area <= 0) {
            return false;
        }
        if (endereco == null || endereco.getCidade() == null || endereco.getCidade().trim().isEmpty()) {
            return false;
        }
        return true;
    }
    
    /**
     * Adiciona uma característica ao imóvel.
     * 
     * Permite extensibilidade para características específicas
     * sem necessidade de alterar a estrutura da classe.
     * 
     * @param chave Nome da característica
     * @param valor Valor da característica
     */
    public void adicionarCaracteristica(String chave, Object valor) {
        this.caracteristicas.put(chave, valor);
    }
    
    /**
     * Retorna uma característica específica.
     * 
     * @param chave Nome da característica
     * @return Valor da característica ou null se não existir
     */
    public Object getCaracteristica(String chave) {
        return this.caracteristicas.get(chave);
    }
    
    /**
     * Verifica se o imóvel possui uma característica.
     * 
     * @param chave Nome da característica
     * @return true se existir
     */
    public boolean temCaracteristica(String chave) {
        return this.caracteristicas.containsKey(chave);
    }
    
    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public double getArea() {
        return area;
    }
    
    public void setArea(double area) {
        this.area = area;
    }
    
    public Endereco getEndereco() {
        return endereco;
    }
    
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Map<String, Object> getCaracteristicas() {
        return new HashMap<>(caracteristicas);
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    @Override
    public String toString() {
        return String.format("%s - %.2fm² - %s", getTipo(), area, endereco != null ? endereco.toString() : "Endereço não informado");
    }
}
