package com.myhome.prototype;

import com.myhome.model.*;
import com.myhome.singleton.ConfigurationManager;
import java.util.*;

public class PrototypeRegistry {
    
    // ========================================
    // SINGLETON
    // ========================================
    
    private static PrototypeRegistry instancia;
    
    /**
     * Obtém a instância única do PrototypeRegistry.
     * Também utiliza o padrão Singleton aqui
     */
    public static synchronized PrototypeRegistry getInstance() {
        if (instancia == null) {
            instancia = new PrototypeRegistry();
        }
        return instancia;
    }
    
    private PrototypeRegistry() {
        this.prototipos = new LinkedHashMap<>();
        this.descricoes = new LinkedHashMap<>();
        inicializarPrototipos();
    }

    private Map<String, ImovelPrototype> prototipos;
    private Map<String, String> descricoes;
    
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
        
        gerarDescricoes();
    }
    
    private void gerarDescricoes() {
        for (Map.Entry<String, ImovelPrototype> entry : prototipos.entrySet()) {
            String chave = entry.getKey();
            Imovel prototipo = (Imovel) entry.getValue();
            descricoes.put(chave, gerarDescricaoPrototipo(prototipo));
        }
    }
    
    public Imovel obterPrototipo(String chave) {
        ImovelPrototype prototipo = prototipos.get(chave);
        if (prototipo == null) {
            return null;
        }
        
        Imovel clone = prototipo.clonar();
        
        // Log para fins didáticos (mostra o padrão em ação)
        System.out.println("🔧 Clonando protótipo: " + gerarDescricaoPrototipo(clone));
        
        return clone;
    }
    
    public Set<String> listarChaves() {
        return new LinkedHashSet<>(prototipos.keySet());
    }
    
    public String obterDescricao(String chave) {
        return descricoes.getOrDefault(chave, "Protótipo desconhecido");
    }
    
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
    
    public Map<String, String> obterTodasDescricoes() {
        return new LinkedHashMap<>(descricoes);
    }
}
