package com.myhome.builder;

import com.myhome.model.Endereco;
import com.myhome.model.Imovel;

// RF01 - Builder: interface para construção fluente de imóveis passo a passo
public interface ImovelBuilder {
    
    void reset();
    
    ImovelBuilder setTipo(String tipo);
    
    ImovelBuilder setEndereco(Endereco endereco);
    
    ImovelBuilder setArea(double area);
    
    ImovelBuilder setQuartos(int quartos);
    
    ImovelBuilder setBanheiros(int banheiros);
    
    ImovelBuilder setTemQuintal(boolean temQuintal);
    
    ImovelBuilder setTemGaragem(boolean temGaragem);
    
    ImovelBuilder setVagas(int vagas);
    
    ImovelBuilder setAndar(int andar);
    
    ImovelBuilder setTemElevador(boolean temElevador);
    
    ImovelBuilder setZoneamento(String zoneamento);
    
    ImovelBuilder setTopografia(String topografia);
    
    ImovelBuilder setTemBanheiro(boolean temBanheiro);
    
    ImovelBuilder setCapacidadePessoas(int capacidadePessoas);
    
    // Constrói o imóvel com os dados configurados
    Imovel build();
}
