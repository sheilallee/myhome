package com.myhome.prototype;

import com.myhome.model.*;
import com.myhome.singleton.ConfigurationManager;
import java.util.*;

/**
 * RF02 - REGISTRY DE PROTÓTIPOS (Padrão Singleton + Prototype)
 * 
 * Gerencia e fornece acesso aos protótipos padrão de imóveis.
 * 
 * RESPONSABILIDADES:
 * - Armazenar protótipos pré-configurados
 * - Carregar configurações de application.properties via ConfigurationManager
 * - Fornecer clones de protótipos sob demanda
 * - Gerar descrições automáticas dos protótipos
 * 
 * PADRÃO SINGLETON:
 * - Garante apenas uma instância durante a execução
 * - Instância única compartilhada por toda a aplicação
 * 
 * PADRÃO PROTOTYPE:
 * - Fornece objetos clonados a partir de protótipos
 * - Evita recriação de objetos do zero
 * - Permite customização rápida de imóveis padrão
 * 
 * INTEGRAÇÃO:
 * - Lê configurações de application.properties
 * - Usada em MyHomeFacade.criarAnuncioDePrototipo()
 * - Demonstrada em MyHomeFacade.demonstrarPadroesGoF()
 */
public class PrototypeRegistry {
    
    // ========================================
    // SINGLETON
    // ========================================
    
    private static PrototypeRegistry instancia;
    
    /**
     * Obtém a instância única do PrototypeRegistry.
     * 
     * @return Instância Singleton
     */
    public static synchronized PrototypeRegistry getInstance() {
        if (instancia == null) {
            instancia = new PrototypeRegistry();
        }
        return instancia;
    }
    
    // ========================================
    // ATRIBUTOS
    // ========================================
    
    /**
     * Mapa de protótipos disponíveis.
     * Chave: identificador do protótipo (ex: "apartamento-padrao")
     * Valor: ImovelPrototype pronto para ser clonado
     */
    private Map<String, ImovelPrototype> prototipos;
    
    /**
     * Descrições geradas para cada protótipo.
     * Usado para exibição no menu interativo.
     */
    private Map<String, String> descricoes;
    
    // ========================================
    // CONSTRUTOR
    // ========================================
    
    /**
     * Construtor privado - Singleton.
     * 
     * Inicializa os protótipos padrão lendo
     * configurações de application.properties.
     */
    private PrototypeRegistry() {
        this.prototipos = new LinkedHashMap<>();
        this.descricoes = new LinkedHashMap<>();
        inicializarPrototipos();
    }
    
    // ========================================
    // INICIALIZAÇÃO DOS PROTÓTIPOS
    // ========================================
    
    /**
     * Inicializa todos os protótipos padrão.
     * 
     * Lê configurações de application.properties e cria
     * imóveis pré-configurados.
     */
    private void inicializarPrototipos() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        // ============================================
        // PROTÓTIPO: APARTAMENTO PADRÃO
        // ============================================
        Apartamento apartamentoPadrao = new Apartamento();
        apartamentoPadrao.setArea(
            Double.parseDouble(config.getProperty("prototipo.apartamento.area", "60.0"))
        );
        apartamentoPadrao.setQuartos(
            Integer.parseInt(config.getProperty("prototipo.apartamento.quartos", "2"))
        );
        apartamentoPadrao.setBanheiros(
            Integer.parseInt(config.getProperty("prototipo.apartamento.banheiros", "1"))
        );
        apartamentoPadrao.setAndar(
            Integer.parseInt(config.getProperty("prototipo.apartamento.andar", "3"))
        );
        apartamentoPadrao.setTemElevador(
            Boolean.parseBoolean(config.getProperty("prototipo.apartamento.elevador", "true"))
        );
        apartamentoPadrao.setVagas(
            Integer.parseInt(config.getProperty("prototipo.apartamento.vagas", "1"))
        );
        
        prototipos.put("apartamento-padrao", apartamentoPadrao);
        
        // ============================================
        // PROTÓTIPO: CASA PADRÃO
        // ============================================
        Casa casaPadrao = new Casa();
        casaPadrao.setArea(
            Double.parseDouble(config.getProperty("prototipo.casa.area", "120.0"))
        );
        casaPadrao.setQuartos(
            Integer.parseInt(config.getProperty("prototipo.casa.quartos", "3"))
        );
        casaPadrao.setBanheiros(
            Integer.parseInt(config.getProperty("prototipo.casa.banheiros", "2"))
        );
        casaPadrao.setTemQuintal(
            Boolean.parseBoolean(config.getProperty("prototipo.casa.quintal", "true"))
        );
        casaPadrao.setTemGaragem(
            Boolean.parseBoolean(config.getProperty("prototipo.casa.garagem", "true"))
        );
        casaPadrao.setVagas(
            Integer.parseInt(config.getProperty("prototipo.casa.vagas", "2"))
        );
        
        prototipos.put("casa-padrao", casaPadrao);
        
        // ============================================
        // PROTÓTIPO: TERRENO PADRÃO
        // ============================================
        Terreno terrenoPadrao = new Terreno();
        terrenoPadrao.setArea(
            Double.parseDouble(config.getProperty("prototipo.terreno.area", "200.0"))
        );
        terrenoPadrao.setZoneamento(
            config.getProperty("prototipo.terreno.zoneamento", "Residencial")
        );
        terrenoPadrao.setTopografia(
            config.getProperty("prototipo.terreno.topografia", "Plano")
        );
        
        prototipos.put("terreno-padrao", terrenoPadrao);
        
        // ============================================
        // PROTÓTIPO: SALA COMERCIAL PADRÃO
        // ============================================
        SalaComercial salaPadrao = new SalaComercial();
        salaPadrao.setArea(
            Double.parseDouble(config.getProperty("prototipo.sala.area", "40.0"))
        );
        salaPadrao.setAndar(
            Integer.parseInt(config.getProperty("prototipo.sala.andar", "2"))
        );
        salaPadrao.setTemBanheiro(
            Boolean.parseBoolean(config.getProperty("prototipo.sala.banheiro", "true"))
        );
        salaPadrao.setVagasEstacionamento(
            Integer.parseInt(config.getProperty("prototipo.sala.vagas", "1"))
        );
        
        prototipos.put("sala-comercial-padrao", salaPadrao);
        
        // Gera descrições para todos os protótipos
        gerarDescricoes();
    }
    
    /**
     * Gera descrições automáticas para cada protótipo.
     * 
     * Estas descrições são exibidas no menu interativo
     * para orientar o usuário na escolha.
     */
    private void gerarDescricoes() {
        for (Map.Entry<String, ImovelPrototype> entry : prototipos.entrySet()) {
            String chave = entry.getKey();
            Imovel prototipo = (Imovel) entry.getValue();
            descricoes.put(chave, gerarDescricaoPrototipo(prototipo));
        }
    }
    
    // ========================================
    // MÉTODOS PÚBLICOS
    // ========================================
    
    /**
     * Obtém um clone de um protótipo específico.
     * 
     * @param chave Identificador do protótipo (ex: "apartamento-padrao")
     * @return Clone do protótipo, ou null se não encontrado
     */
    public Imovel obterPrototipo(String chave) {
        ImovelPrototype prototipo = prototipos.get(chave);
        if (prototipo == null) {
            return null;
        }
        
        // Clona e retorna uma cópia independente
        Imovel clone = prototipo.clonar();
        
        // Log para fins didáticos (mostra o padrão em ação)
        System.out.println("🔧 Clonando protótipo: " + gerarDescricaoPrototipo(clone));
        
        return clone;
    }
    
    /**
     * Lista todas as chaves de protótipos disponíveis.
     * 
     * @return Set com as chaves (em ordem de inserção)
     */
    public Set<String> listarChaves() {
        return new LinkedHashSet<>(prototipos.keySet());
    }
    
    /**
     * Obtém a descrição de um protótipo.
     * 
     * @param chave Identificador do protótipo
     * @return Descrição formatada
     */
    public String obterDescricao(String chave) {
        return descricoes.getOrDefault(chave, "Protótipo desconhecido");
    }
    
    /**
     * Gera descrição automática para um imóvel.
     * 
     * Baseada no tipo e atributos específicos do imóvel.
     * 
     * @param imovel Imóvel para o qual gerar descrição
     * @return String descritiva
     */
    public String gerarDescricaoPrototipo(Imovel imovel) {
        if (imovel instanceof Casa) {
            Casa casa = (Casa) imovel;
            return String.format("Casa (%dQ, %dB, %.0fm²%s%s)",
                casa.getQuartos(),
                casa.getBanheiros(),
                casa.getArea(),
                casa.isTemQuintal() ? ", quintal" : "",
                casa.isTemGaragem() ? ", garagem" : "");
                
        } else if (imovel instanceof Apartamento) {
            Apartamento apt = (Apartamento) imovel;
            return String.format("Apartamento (%dQ, %dB, %.0fm²%s)",
                apt.getQuartos(),
                apt.getBanheiros(),
                apt.getArea(),
                apt.isTemElevador() ? ", elevador" : "");
                
        } else if (imovel instanceof Terreno) {
            Terreno terreno = (Terreno) imovel;
            return String.format("Terreno (%.0fm², %s, %s)",
                terreno.getArea(),
                terreno.getZoneamento(),
                terreno.getTopografia());
                
        } else if (imovel instanceof SalaComercial) {
            SalaComercial sala = (SalaComercial) imovel;
            return String.format("Sala Comercial (%.0fm², %d pessoas%s)",
                sala.getArea(),
                sala.getCapacidadePessoas(),
                sala.isTemBanheiro() ? ", banheiro" : "");
        }
        
        return "Imóvel desconhecido";
    }
    
    /**
     * Retorna todas as descrições dos protótipos.
     * 
     * @return Map com chave → descrição
     */
    public Map<String, String> obterTodasDescricoes() {
        return new LinkedHashMap<>(descricoes);
    }
}
