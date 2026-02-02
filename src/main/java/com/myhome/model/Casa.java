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
        this.descricao = original.descricao;
        
        // Copia características (Deep copy)
        this.endereco = original.endereco != null ? original.endereco.clone() : null;
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
