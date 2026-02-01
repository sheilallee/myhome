package com.myhome.service;

import com.myhome.chain.ModeradorBase;
import com.myhome.chain.ValidadorPalavras;
import com.myhome.chain.ValidadorPreco;
import com.myhome.model.Anuncio;

/**
 * RF04 - SERVICE: Gerencia a validação de anúncios via Chain of Responsibility Pattern
 * 
 * RESPONSABILIDADES:
 * - Construir a chain de validadores
 * - Executar a validação de anúncios
 * - Encapsular a complexidade do Chain Pattern
 * 
 * BENEFÍCIOS:
 * - Desacoplamento do ModeracaoState da chain
 * - Facilita testes unitários da validação
 * - Centraliza lógica de construção da chain
 * - Permite fácil adição de novos validadores
 */
public class ChainValidationService {
    
    /**
     * Valida um anúncio usando a chain de validadores
     * Chain: ValidadorPalavras → ValidadorPreco
     * 
     * @param anuncio O anúncio a ser validado
     * @return true se passou em todos os validadores, false caso contrário
     */
    public boolean validarAnuncio(Anuncio anuncio) {
        // Construir a chain de validadores
        ModeradorBase primeiroValidador = new ValidadorPalavras();
        primeiroValidador.setNext(new ValidadorPreco());
        
        // Executar a validação
        return primeiroValidador.handle(anuncio);
    }
}
