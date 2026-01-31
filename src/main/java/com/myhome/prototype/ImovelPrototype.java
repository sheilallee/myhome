package com.myhome.prototype;

import com.myhome.model.Imovel;

/**
 * RF02 - INTERFACE DO PADRÃO PROTOTYPE
 * 
 * Define o contrato para objetos que podem ser clonados
 * (criados a partir de protótipos existentes).
 * 
 * Esta é uma interface customizada que permite controle total
 * sobre o processo de clonagem, ao invés de depender da interface
 * Cloneable padrão do Java.
 * 
 * PADRÃO PROTOTYPE (Gang of Four):
 * - Permite criar novos objetos clonando um objeto existente
 * - Evita criar subclasses apenas para variar objetos
 * - Permite adicionar/remover propriedades em tempo de execução
 * 
 * IMPLEMENTADORES:
 * - Imovel (e suas subclasses: Casa, Apartamento, Terreno, SalaComercial)
 */
public interface ImovelPrototype {
    
    /**
     * Clona o imóvel criando uma cópia profunda (deep copy).
     * 
     * IMPLEMENTAÇÃO ESPERADA:
     * - Copiar todos os atributos primitivos
     * - Deep copy do Map de características (não apenas referência)
     * - Preparado para deep copy de objetos mutáveis como Endereco (futura branch)
     * 
     * @return Nova instância com os mesmos valores
     */
    Imovel clonar();
}
