package com.myhome.builder;

import com.myhome.model.*;

// RF01 - Implementação concreta do Builder para criar imóveis
public class ImovelBuilderImpl implements ImovelBuilder {
    
    // ========================================
    // ATRIBUTOS
    // ========================================
    
    private String tipo;
    private String endereco;
    private double area;
    private int quartos;
    private int banheiros;
    private boolean temQuintal;
    private boolean temGaragem;
    private int vagas;
    private int andar;
    private boolean temElevador;
    private String zoneamento;
    private String topografia;
    private boolean temBanheiro;
    private int capacidadePessoas;
    
    // ========================================
    // CONSTRUTOR
    // ========================================
    
    public ImovelBuilderImpl() {
        this.reset();
    }
    
    // ========================================
    // IMPLEMENTAÇÃO DOS MÉTODOS DO BUILDER
    // ========================================
    
    @Override
    public void reset() {
        this.tipo = null;
        this.endereco = null;
        this.area = 0;
        this.quartos = 0;
        this.banheiros = 0;
        this.temQuintal = false;
        this.temGaragem = false;
        this.vagas = 0;
        this.andar = 0;
        this.temElevador = false;
        this.zoneamento = null;
        this.topografia = null;
        this.temBanheiro = false;
        this.capacidadePessoas = 0;
    }
    
    @Override
    public ImovelBuilder setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }
    
    @Override
    public ImovelBuilder setEndereco(String endereco) {
        this.endereco = endereco;
        return this;
    }
    
    @Override
    public ImovelBuilder setArea(double area) {
        this.area = area;
        return this;
    }
    
    @Override
    public ImovelBuilder setQuartos(int quartos) {
        this.quartos = quartos;
        return this;
    }
    
    @Override
    public ImovelBuilder setBanheiros(int banheiros) {
        this.banheiros = banheiros;
        return this;
    }
    
    @Override
    public ImovelBuilder setTemQuintal(boolean temQuintal) {
        this.temQuintal = temQuintal;
        return this;
    }
    
    @Override
    public ImovelBuilder setTemGaragem(boolean temGaragem) {
        this.temGaragem = temGaragem;
        return this;
    }
    
    @Override
    public ImovelBuilder setVagas(int vagas) {
        this.vagas = vagas;
        return this;
    }
    
    @Override
    public ImovelBuilder setAndar(int andar) {
        this.andar = andar;
        return this;
    }
    
    @Override
    public ImovelBuilder setTemElevador(boolean temElevador) {
        this.temElevador = temElevador;
        return this;
    }
    
    @Override
    public ImovelBuilder setZoneamento(String zoneamento) {
        this.zoneamento = zoneamento;
        return this;
    }
    
    @Override
    public ImovelBuilder setTopografia(String topografia) {
        this.topografia = topografia;
        return this;
    }
    
    @Override
    public ImovelBuilder setTemBanheiro(boolean temBanheiro) {
        this.temBanheiro = temBanheiro;
        return this;
    }
    
    @Override
    public ImovelBuilder setCapacidadePessoas(int capacidadePessoas) {
        this.capacidadePessoas = capacidadePessoas;
        return this;
    }
    
    @Override
    public Imovel build() {
        // Validar campos obrigatórios
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalStateException("Tipo é obrigatório");
        }
        if (endereco == null || endereco.isEmpty()) {
            throw new IllegalStateException("Endereço é obrigatório");
        }
        if (area <= 0) {
            throw new IllegalStateException("Área deve ser maior que zero");
        }
        
        // Criar o tipo correto de imóvel diretamente
        Imovel imovel = criarImovelPorTipo(tipo);
        
        // Configurar atributos comuns
        imovel.setEndereco(endereco);
        imovel.setArea(area);
        
        // Configurar atributos específicos por tipo
        if (imovel instanceof Casa) {
            Casa casa = (Casa) imovel;
            casa.setQuartos(quartos);
            casa.setBanheiros(banheiros);
            casa.setTemQuintal(temQuintal);
            casa.setTemGaragem(temGaragem);
            if (temGaragem && vagas > 0) {
                casa.setVagas(vagas);
            }
        } else if (imovel instanceof Apartamento) {
            Apartamento ap = (Apartamento) imovel;
            ap.setQuartos(quartos);
            ap.setBanheiros(banheiros);
            ap.setVagas(vagas);
            ap.setAndar(andar);
            ap.setTemElevador(temElevador);
        } else if (imovel instanceof Terreno) {
            Terreno terreno = (Terreno) imovel;
            if (zoneamento != null) {
                terreno.setZoneamento(zoneamento);
            }
            if (topografia != null) {
                terreno.setTopografia(topografia);
            }
        } else if (imovel instanceof SalaComercial) {
            SalaComercial sala = (SalaComercial) imovel;
            sala.setTemBanheiro(temBanheiro);
            sala.setCapacidadePessoas(capacidadePessoas);
        }
        
        // Resetar builder para próxima construção
        Imovel imovelConstruido = imovel;
        this.reset();
        
        return imovelConstruido;
    }
    
    /**
     * Cria o tipo correto de imóvel usando os construtores diretos.
     * 
     * PADRÃO BUILDER:
     * O Builder cria os objetos diretamente, sem usar Factory.
     * A responsabilidade do Builder é construir objetos complexos
     * passo a passo.
     * 
     * @param tipo Tipo do imóvel (casa, apartamento, terreno, sala_comercial)
     * @return Instância do imóvel criado
     * @throws IllegalArgumentException se tipo inválido
     */
    private Imovel criarImovelPorTipo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "casa":
                return new Casa();
            case "apartamento":
                return new Apartamento();
            case "terreno":
                return new Terreno();
            case "sala_comercial":
            case "salacomercial":
                return new SalaComercial();
            default:
                throw new IllegalArgumentException("Tipo de imóvel inválido: " + tipo);
        }
    }
}
