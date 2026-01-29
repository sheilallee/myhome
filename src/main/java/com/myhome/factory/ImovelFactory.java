package com.myhome.factory;

import com.myhome.model.Imovel;

/**
 * RF01 - FACTORY METHOD PATTERN
 * 
 * Interface que define o Factory Method para criação de imóveis.
 * 
 * RESPONSABILIDADES:
 * - Definir o contrato para criação de imóveis
 * - Permitir que subclasses decidam qual classe instanciar
 * - Encapsular a lógica de criação de objetos
 * 
 * ESTRUTURA DO FACTORY METHOD:
 * 1. Creator (esta interface) - declara o factory method
 * 2. ConcreteCreators (CasaFactory, ApartamentoFactory, etc.) - implementam o factory method
 * 3. Product (Imovel) - interface do produto
 * 4. ConcreteProducts (Casa, Apartamento, etc.) - produtos concretos
 * 
 * BENEFÍCIOS:
 * - Elimina acoplamento entre código cliente e classes concretas
 * - Facilita adicionar novos tipos de imóveis sem modificar código existente
 * - Segue o Princípio Aberto-Fechado (SOLID)
 * 
 * EXEMPLO DE USO:
 * <pre>
 * ImovelFactory factory = new CasaFactory();
 * Imovel imovel = factory.criarImovel();
 * // imovel é uma instância de Casa
 * </pre>
 */
public interface ImovelFactory {
    
    /**
     * Factory Method - Cria uma instância de Imovel.
     * 
     * PADRÃO FACTORY METHOD:
     * Este é o método fábrica que será implementado pelas
     * factories concretas (CasaFactory, ApartamentoFactory, etc.).
     * 
     * Cada factory concreta retorna um tipo específico de imóvel,
     * mas o tipo de retorno é a interface/classe abstrata Imovel.
     * 
     * POLIMORFISMO:
     * O código cliente trabalha com a interface Imovel,
     * sem precisar conhecer a classe concreta sendo criada.
     * 
     * @return Nova instância de um tipo específico de Imovel
     */
    Imovel criarImovel();
    
    /**
     * Método opcional que pode ser usado para operações adicionais
     * após a criação do imóvel.
     * 
     * Por exemplo: registrar em banco de dados, validar, etc.
     * 
     * TEMPLATE METHOD:
     * Este método pode chamar criarImovel() e adicionar
     * comportamento extra antes ou depois.
     * 
     * @return Imovel criado e registrado
     */
    default Imovel registrarImovel() {
        Imovel imovel = criarImovel();
        
        // Aqui poderiam ser adicionadas operações comuns
        // como validação, logging, registro em BD, etc.
        System.out.println("Imóvel criado: " + imovel.getTipo());
        
        return imovel;
    }
}
