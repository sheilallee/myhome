package com.myhome.builder;

import com.myhome.model.Apartamento;
import com.myhome.model.Casa;

// RF01 - Director: define sequências pré-configuradas de construção de imóveis
public class Director {
    
    private ImovelBuilder builder;
    
    public Director(ImovelBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Builder não pode ser null");
        }
        this.builder = builder;
    }
    
    // ========================================
    // MÉTODOS DE CONSTRUÇÃO
    // ========================================
    
    /**
     * Constrói uma casa simples com campos básicos.
     * 
     * PADRÃO BUILDER + DIRECTOR:
     * Este método demonstra como o Director pode encapsular uma sequência específica de construção.
     * 
     * CASA SIMPLES contém:
     * - Endereço
     * - Área
     * - Quartos
     * - Banheiros
     * 
     * @param endereco Endereço completo
     * @param area Área em m²
     * @param quartos Número de quartos
     * @param banheiros Número de banheiros
     * @return Casa construída
     */
    public Casa construirCasaSimples(
            String endereco, 
            double area, 
            int quartos, 
            int banheiros) {
        
        builder.reset();
        builder.setTipo("casa");
        builder.setEndereco(endereco);
        builder.setArea(area);
        builder.setQuartos(quartos);
        builder.setBanheiros(banheiros);
        return (Casa) builder.build();
    }
    
    /**
     * Constrói uma casa completa com todos os detalhes.
     * 
     * CASA COMPLETA contém:
     * - Todos os campos básicos
     * - Quintal
     * - Garagem
     * 
     * @param endereco Endereço completo
     * @param area Área em m²
     * @param quartos Número de quartos
     * @param banheiros Número de banheiros
     * @param temQuintal Se tem quintal
     * @param temGaragem Se tem garagem
     * @return Casa completa construída
     */
    public Casa construirCasaCompleta(
            String endereco,
            double area,
            int quartos,
            int banheiros,
            boolean temQuintal,
            boolean temGaragem) {
        
        builder.reset();
        builder.setTipo("casa");
        builder.setEndereco(endereco);
        builder.setArea(area);
        builder.setQuartos(quartos);
        builder.setBanheiros(banheiros);
        builder.setTemQuintal(temQuintal);
        builder.setTemGaragem(temGaragem);
        return (Casa) builder.build();
    }
    
    /**
     * Constrói um apartamento completo.
     * 
     * APARTAMENTO COMPLETO contém:
     * - Endereço, área, quartos, banheiros
     * - Andar
     * - Vagas de garagem
     * - Elevador
     * 
     * @param endereco Endereço
     * @param area Área em m²
     * @param quartos Número de quartos
     * @param banheiros Número de banheiros
     * @param andar Número do andar
     * @param vagas Vagas de garagem
     * @param temElevador Se tem elevador
     * @return Apartamento construído
     */
    public Apartamento construirApartamentoCompleto(
            String endereco,
            double area,
            int quartos,
            int banheiros,
            int andar,
            int vagas,
            boolean temElevador) {
        
        builder.reset();
        builder.setTipo("apartamento");
        builder.setEndereco(endereco);
        builder.setArea(area);
        builder.setQuartos(quartos);
        builder.setBanheiros(banheiros);
        builder.setAndar(andar);
        builder.setVagas(vagas);
        builder.setTemElevador(temElevador);
        return (Apartamento) builder.build();
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    /**
     * Permite trocar o builder em tempo de execução.
     * 
     * FLEXIBILIDADE:
     * Permite usar diferentes builders com o mesmo Director.
     * 
     * @param builder Novo builder
     */
    public void changeBuilder(ImovelBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Builder não pode ser null");
        }
        this.builder = builder;
    }
    
    /**
     * Retorna o builder atual.
     * 
     * @return Builder em uso
     */
    public ImovelBuilder getBuilder() {
        return this.builder;
    }
}
