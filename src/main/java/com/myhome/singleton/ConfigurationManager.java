package com.myhome.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// RF07 - Singleton: instância única para gerenciar configurações globais
public class ConfigurationManager {
    
    // Instância única (eager initialization - thread-safe)
    private static final ConfigurationManager instance = new ConfigurationManager();
    
    private Properties properties;
    
    // Construtor privado (Singleton pattern)
    private ConfigurationManager() {
        properties = new Properties();
        loadProperties();
    }
    
    // Ponto de acesso global à instância única
    public static ConfigurationManager getInstance() {
        return instance;
    }
    
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            if (input == null) {
                System.err.println("Arquivo application.properties não encontrado!");
                return;
            }
            
            // Carrega as propriedades do arquivo
            properties.load(input);
            System.out.println("Configurações carregadas com sucesso!");
            
        } catch (IOException ex) {
            System.err.println("Erro ao carregar configurações: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // ========================================
    // MÉTODOS PÚBLICOS DE ACESSO
    // ========================================
    
    /**
     * Retorna o valor de uma propriedade como String.
     * 
     * @param key Chave da propriedade
     * @return Valor da propriedade ou null se não encontrada
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Retorna o valor de uma propriedade como String,
     * com valor padrão caso a propriedade não exista.
     * 
     * @param key Chave da propriedade
     * @param defaultValue Valor padrão
     * @return Valor da propriedade ou defaultValue
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Retorna o valor de uma propriedade como double.
     * 
     * Útil para configurações numéricas como preços, taxas, etc.
     * 
     * @param key Chave da propriedade
     * @return Valor convertido para double
     * @throws NumberFormatException se o valor não for numérico
     */
    public double getPropertyAsDouble(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Propriedade não encontrada: " + key);
        }
        return Double.parseDouble(value.trim());
    }
    
    /**
     * Retorna o valor de uma propriedade como int.
     * 
     * Útil para configurações numéricas inteiras como limites, quantidades, etc.
     * 
     * @param key Chave da propriedade
     * @return Valor convertido para int
     * @throws NumberFormatException se o valor não for numérico
     */
    public int getPropertyAsInt(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Propriedade não encontrada: " + key);
        }
        return Integer.parseInt(value.trim());
    }
    
    /**
     * Retorna o valor de uma propriedade como boolean.
     * 
     * @param key Chave da propriedade
     * @return true se o valor for "true" (case insensitive), false caso contrário
     */
    public boolean getPropertyAsBoolean(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(value.trim());
    }
    
    /**
     * Retorna uma lista de strings a partir de uma propriedade.
     * 
     * Espera que os valores estejam separados por vírgula.
     * Exemplo: termos.proibidos=golpe,fraude,spam
     * 
     * @param key Chave da propriedade
     * @return Lista de valores ou lista vazia se não encontrada
     */
    public List<String> getPropertyAsList(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Remove espaços em branco e divide por vírgula
        String[] items = value.split(",");
        List<String> result = new ArrayList<>();
        for (String item : items) {
            String trimmed = item.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }
    
    /**
     * Retorna a lista de termos proibidos para moderação.
     * 
     * REGRA DE NEGÓCIO (RF03):
     * Termos proibidos são usados na validação de anúncios
     * durante o processo de moderação.
     * 
     * @return Lista de termos proibidos
     */
    public List<String> getTermosProibidos() {
        return getPropertyAsList("moderacao.termos.proibidos");
    }
    
    /**
     * Retorna todas as propriedades carregadas.
     * 
     * CUIDADO: Este método expõe o objeto Properties interno.
     * Use apenas para debug ou necessidades especiais.
     * 
     * @return Objeto Properties com todas as configurações
     */
    public Properties getAllProperties() {
        return properties;
    }
}
