package com.myhome.model;

public class Apartamento extends Imovel {
    
    private int quartos;
    private int banheiros;
    private int andar;
    private boolean temElevador;
    private int vagas;
    
    public Apartamento() {
        super();
        this.tipo = "Apartamento";
    }
    
    private Apartamento(Apartamento original) {
        super();
        this.id = original.id;
        this.tipo = original.tipo;
        this.area = original.area;
        this.endereco = original.endereco != null ? original.endereco.clone() : null;
        this.descricao = original.descricao;
        this.caracteristicas.putAll(original.caracteristicas);
        
        this.quartos = original.quartos;
        this.banheiros = original.banheiros;
        this.andar = original.andar;
        this.temElevador = original.temElevador;
        this.vagas = original.vagas;
    }
    
    @Override
    public String getTipo() {
        return this.tipo;
    }
    
    @Override
    public Imovel clonar() {
        return new Apartamento(this);
    }
    
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
