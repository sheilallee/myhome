package com.myhome.factory;

import com.myhome.model.Casa;
import com.myhome.model.Imovel;

/**
 * RF01 - CONCRETE CREATOR (Factory Method Pattern)
 * 
 * Factory concreta para criação de objetos Casa.
 * 
 * RESPONSABILIDADES:
 * - Implementar o factory method para criar instâncias de Casa
 * - Encapsular a lógica de criação de casas
 * 
 * PADRÃO FACTORY METHOD:
 * Esta classe é um "Concrete Creator" que implementa o factory method
 * para retornar um "Concrete Product" específico (Casa).
 * 
 * EXEMPLO DE USO:
 * <pre>
 * ImovelFactory factory = new CasaFactory();
 * Imovel casa = factory.criarImovel();
 * 
 * // Configurar a casa
 * if (casa instanceof Casa) {
 *     Casa c = (Casa) casa;
 *     c.setQuartos(3);
 *     c.setBanheiros(2);
 *     c.setArea(120.0);
 * }
 * </pre>
 */
public class CasaFactory implements ImovelFactory {
    
    /**
     * Implementação do Factory Method para criar Casas.
     * 
     * FACTORY METHOD PATTERN:
     * Este método é a implementação concreta do factory method.
     * Retorna uma nova instância de Casa.
     * 
     * O tipo de retorno é Imovel (abstrato), mas a instância
     * criada é Casa (concreta). Isso permite polimorfismo.
     * 
     * @return Nova instância de Casa
     */
    @Override
    public Imovel criarImovel() {
        return new Casa();
    }
    
    /**
     * Método auxiliar para criar uma casa com configurações básicas.
     * 
     * Este não é parte obrigatória do padrão Factory Method,
     * mas demonstra como a factory pode fornecer métodos
     * convenientes para criação de objetos pré-configurados.
     * 
     * @param quartos Número de quartos
     * @param banheiros Número de banheiros
     * @param area Área em m²
     * @return Casa pré-configurada
     */
    public Casa criarCasaBasica(int quartos, int banheiros, double area) {
        Casa casa = new Casa();
        casa.setQuartos(quartos);
        casa.setBanheiros(banheiros);
        casa.setArea(area);
        return casa;
    }
}
