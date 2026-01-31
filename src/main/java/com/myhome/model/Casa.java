package com.myhome.model;

public class Casa extends Imovel {
    
    private int quartos;
    private int banheiros;
    private boolean temQuintal;
    private boolean temGaragem;
    private int vagas; // Vagas na garagem
    
    public Casa() {
        super();
        this.tipo = "Casa";
    }
    
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
    
    @Override
    public String getTipo() {
        return this.tipo;
    }
    
    @Override
    public Imovel clonar() {
        return new Casa(this);
    }
    
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
    
    @Override
    public String toString() {
        return String.format("Casa - %d quartos, %d banheiros, %.2fm² - %s%s%s", 
            quartos, banheiros, area, endereco,
            temQuintal ? " (com quintal)" : "",
            temGaragem ? String.format(" (%d vaga(s))", vagas) : "");
    }
}
