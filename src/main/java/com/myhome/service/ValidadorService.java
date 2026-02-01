package com.myhome.service;

import com.myhome.model.Endereco;

/**
 * SERVIÇO DE VALIDAÇÃO E FORMATAÇÃO
 * 
 * RESPONSABILIDADE:
 * - Validar emails, telefones e outros dados
 * - Formatar dados de entrada (máscaras)
 * - Centralizar regras de validação
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por validações e formatações
 * - OCP: Novas validações podem ser adicionadas sem modificar existentes
 * - DIP: Outras classes dependem desta interface de serviço
 * 
 * PADRÃO: Strategy (cada validação é uma estratégia diferente)
 * 
 * @author MyHome Team - João Pessoa/PB
 */
public class ValidadorService {
    
    /**
     * Valida formato de email.
     * Regex: texto@dominio.extensao
     * 
     * @param email Email a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Regex para validação básica de email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
    
    /**
     * Valida formato de telefone brasileiro: (xx) xxxx-xxxx ou (xx) xxxxx-xxxx
     * 
     * @param telefone Telefone a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return false;
        }
        // Regex para validação: (xx) xxxx-xxxx ou (xx) xxxxx-xxxx
        String regex = "^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$";
        return telefone.matches(regex);
    }
    
    /**
     * Formata número de telefone brasileiro automaticamente.
     * Aceita 10 dígitos: (xx) xxxx-xxxx (fixo)
     * Aceita 11 dígitos: (xx) xxxxx-xxxx (celular)
     * 
     * @param input String com apenas números ou com caracteres especiais
     * @return Telefone formatado ou null se inválido
     */
    public String formatarTelefone(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove tudo que não é dígito
        String numeros = input.replaceAll("[^0-9]", "");
        
        // Valida quantidade de dígitos
        if (numeros.length() == 10) {
            // Formato: (xx) xxxx-xxxx (telefone fixo)
            return String.format("(%s) %s-%s", 
                numeros.substring(0, 2),
                numeros.substring(2, 6),
                numeros.substring(6, 10));
        } else if (numeros.length() == 11) {
            // Formato: (xx) xxxxx-xxxx (celular com 9 dígitos)
            return String.format("(%s) %s-%s", 
                numeros.substring(0, 2),
                numeros.substring(2, 7),
                numeros.substring(7, 11));
        }
        
        return null; // Quantidade inválida de dígitos
    }
    
    /**
     * Valida se um número é positivo.
     * 
     * @param valor Valor a ser validado
     * @return true se positivo, false caso contrário
     */
    public boolean validarNumeroPositivo(double valor) {
        return valor > 0;
    }
    
    /**
     * Valida se um texto não está vazio.
     * 
     * @param texto Texto a ser validado
     * @return true se não vazio, false caso contrário
     */
    public boolean validarTextoNaoVazio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida um objeto Endereco verificando todos os seus campos.
     * 
     * Regras:
     * - Rua não pode ser vazia
     * - Número não pode ser vazio
     * - Cidade não pode ser vazia
     * - Estado deve ter exatamente 2 caracteres (sigla do estado)
     * 
     * @param endereco Endereco a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean validarEndereco(Endereco endereco) {
        if (endereco == null) {
            return false;
        }
        
        // Validar rua
        if (!validarTextoNaoVazio(endereco.getRua())) {
            return false;
        }
        
        // Validar número
        if (!validarTextoNaoVazio(endereco.getNumero())) {
            return false;
        }
        
        // Validar cidade
        if (!validarTextoNaoVazio(endereco.getCidade())) {
            return false;
        }
        
        // Validar estado (deve ter exatamente 2 caracteres - sigla)
        String estado = endereco.getEstado();
        if (estado == null || estado.trim().length() != 2) {
            return false;
        }
        
        return true;
    }
}
