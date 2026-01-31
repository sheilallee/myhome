package com.myhome.model;

import java.util.HashMap;
import java.util.Map;
import com.myhome.prototype.ImovelPrototype;

public abstract class Imovel implements ImovelPrototype {
    
    protected Long id;
    protected String tipo;
    protected double area;
    protected Endereco endereco;
    protected String descricao;
    protected Map<String, Object> caracteristicas;
    
    protected Imovel() {
        this.caracteristicas = new HashMap<>();
    }
    
    public abstract String getTipo();
    
    @Override
    public abstract Imovel clonar();
    
    public boolean validar() {
        if (area <= 0) {
            return false;
        }
        if (endereco == null || endereco.getCidade() == null || endereco.getCidade().trim().isEmpty()) {
            return false;
        }
        return true;
    }
    
    public void adicionarCaracteristica(String chave, Object valor) {
        this.caracteristicas.put(chave, valor);
    }
    
    public Object getCaracteristica(String chave) {
        return this.caracteristicas.get(chave);
    }
    
    public boolean temCaracteristica(String chave) {
        return this.caracteristicas.containsKey(chave);
    }
    
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
    
    @Override
    public String toString() {
        return String.format("%s - %.2fm² - %s", getTipo(), area, endereco != null ? endereco.toString() : "Endereço não informado");
    }
}
