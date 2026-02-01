package com.myhome.service;

import com.myhome.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Serviço de persistência de anúncios em arquivo JSON
 * 
 * RESPONSABILIDADES:
 * - Salvar anúncios em arquivo JSON
 * - Carregar anúncios do arquivo JSON
 * - Gerenciar o arquivo de dados
 */
public class PersistenciaService {
    
    private static final String DATA_DIR = "data";
    private static final String ARQUIVO_ANUNCIOS = DATA_DIR + "/anuncios.json";
    
    /**
     * Salva a lista de anúncios em arquivo JSON
     */
    public void salvarAnuncios(List<Anuncio> anuncios) {
        try {
            // Cria diretório se não existir
            Files.createDirectories(Paths.get(DATA_DIR));
            
            // Converte para JSON
            String json = toJson(anuncios);
            
            // Salva no arquivo
            Files.write(Paths.get(ARQUIVO_ANUNCIOS), json.getBytes());
            
            System.out.println("💾 Anúncios salvos com sucesso!");
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar anúncios: " + e.getMessage());
        }
    }
    
    /**
     * Carrega a lista de anúncios do arquivo JSON
     */
    public List<Anuncio> carregarAnuncios() {
        try {
            if (!Files.exists(Paths.get(ARQUIVO_ANUNCIOS))) {
                return new ArrayList<>();
            }
            
            String json = new String(Files.readAllBytes(Paths.get(ARQUIVO_ANUNCIOS)));
            return fromJson(json);
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar anúncios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Converte lista de anúncios para JSON (formato simples)
     */
    private String toJson(List<Anuncio> anuncios) {
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"anuncios\": [\n");
        
        for (int i = 0; i < anuncios.size(); i++) {
            Anuncio anuncio = anuncios.get(i);
            Imovel imovel = anuncio.getImovel();
            Usuario anunciante = anuncio.getAnunciante();
            
            json.append("    {\n");
            json.append("      \"titulo\": \"").append(escaparJson(anuncio.getTitulo())).append("\",\n");
            json.append("      \"preco\": ").append(anuncio.getPreco()).append(",\n");
            json.append("      \"descricao\": \"").append(escaparJson(anuncio.getDescricao())).append("\",\n");
            
            // Imovel
            json.append("      \"imovel\": {\n");
            json.append("        \"tipo\": \"").append(imovel.getTipo()).append("\",\n");
            json.append("        \"area\": ").append(imovel.getArea()).append(",\n");
            json.append("        \"endereco\": \"").append(escaparJson(imovel.getEndereco().toString())).append("\"");
            
            // Adiciona atributos específicos por tipo
            if (imovel instanceof Casa) {
                Casa casa = (Casa) imovel;
                json.append(",\n        \"quartos\": ").append(casa.getQuartos());
                json.append(",\n        \"banheiros\": ").append(casa.getBanheiros());
                json.append(",\n        \"temQuintal\": ").append(casa.isTemQuintal());
                json.append(",\n        \"temGaragem\": ").append(casa.isTemGaragem());
            } else if (imovel instanceof Apartamento) {
                Apartamento apt = (Apartamento) imovel;
                json.append(",\n        \"quartos\": ").append(apt.getQuartos());
                json.append(",\n        \"banheiros\": ").append(apt.getBanheiros());
                json.append(",\n        \"andar\": ").append(apt.getAndar());
                json.append(",\n        \"vagas\": ").append(apt.getVagas());
            } else if (imovel instanceof SalaComercial) {
                SalaComercial sala = (SalaComercial) imovel;
                json.append(",\n        \"andar\": ").append(sala.getAndar());
                json.append(",\n        \"temBanheiro\": ").append(sala.isTemBanheiro());
                json.append(",\n        \"vagas\": ").append(sala.getVagasEstacionamento());
            }
            
            json.append("\n      },\n");
            
            // Anunciante
            json.append("      \"anunciante\": {\n");
            json.append("        \"nome\": \"").append(escaparJson(anunciante.getNome())).append("\",\n");
            json.append("        \"email\": \"").append(escaparJson(anunciante.getEmail())).append("\",\n");
            json.append("        \"telefone\": \"").append(escaparJson(anunciante.getTelefone())).append("\"\n");
            json.append("      }\n");
            
            json.append("    }");
            
            if (i < anuncios.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("  ]\n}");
        return json.toString();
    }
    
    /**
     * Converte JSON para lista de anúncios (parser simples)
     */
    private List<Anuncio> fromJson(String json) {
        List<Anuncio> anuncios = new ArrayList<>();
        
        try {
            // Parser JSON simples (sem bibliotecas externas)
            String[] blocos = json.split("\\{\\s*\"titulo\"");
            
            for (int i = 1; i < blocos.length; i++) {
                String bloco = "{\"titulo\"" + blocos[i];
                
                // Extrai valores
                String titulo = extrairValor(bloco, "titulo");
                double preco = Double.parseDouble(extrairValor(bloco, "preco"));
                String descricao = extrairValor(bloco, "descricao");
                
                // Extrai dados do imóvel
                String tipoImovel = extrairValor(bloco, "tipo");
                double area = Double.parseDouble(extrairValor(bloco, "area"));
                String endereco = extrairValor(bloco, "endereco");
                
                // Cria imóvel baseado no tipo
                Imovel imovel = criarImovelDoJson(bloco, tipoImovel, area, endereco);
                
                // Extrai dados do anunciante
                String nomeAnunciante = extrairValor(bloco, "nome");
                String emailAnunciante = extrairValor(bloco, "email");
                String telefoneAnunciante = extrairValor(bloco, "telefone");
                
                Usuario anunciante = new Usuario();
                anunciante.setNome(nomeAnunciante);
                anunciante.setEmail(emailAnunciante);
                anunciante.setTelefone(telefoneAnunciante);
                
                // Cria anúncio
                Anuncio anuncio = new Anuncio();
                anuncio.setTitulo(titulo);
                anuncio.setPreco(preco);
                anuncio.setDescricao(descricao);
                anuncio.setImovel(imovel);
                anuncio.setAnunciante(anunciante);
                
                anuncios.add(anuncio);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar JSON: " + e.getMessage());
        }
        
        return anuncios;
    }
    
    /**
     * Cria imóvel a partir dos dados JSON
     */
    private Imovel criarImovelDoJson(String json, String tipo, double area, String endereco) {
        Imovel imovel = null;
        
        switch (tipo) {
            case "Casa":
                Casa casa = new Casa();
                casa.setArea(area);
                casa.setEndereco(new Endereco(endereco, json, tipo, endereco));
                String quartosStr = extrairValor(json, "quartos");
                if (!quartosStr.isEmpty()) casa.setQuartos(Integer.parseInt(quartosStr));
                String banheirosStr = extrairValor(json, "banheiros");
                if (!banheirosStr.isEmpty()) casa.setBanheiros(Integer.parseInt(banheirosStr));
                casa.setTemQuintal(extrairValor(json, "temQuintal").equals("true"));
                casa.setTemGaragem(extrairValor(json, "temGaragem").equals("true"));
                imovel = casa;
                break;
                
            case "Apartamento":
                Apartamento apt = new Apartamento();
                apt.setArea(area);
                apt.setEndereco(new Endereco(endereco, json, tipo, endereco));
                String quartosAptStr = extrairValor(json, "quartos");
                if (!quartosAptStr.isEmpty()) apt.setQuartos(Integer.parseInt(quartosAptStr));
                String banheirosAptStr = extrairValor(json, "banheiros");
                if (!banheirosAptStr.isEmpty()) apt.setBanheiros(Integer.parseInt(banheirosAptStr));
                String andarStr = extrairValor(json, "andar");
                if (!andarStr.isEmpty()) apt.setAndar(Integer.parseInt(andarStr));
                String vagasStr = extrairValor(json, "vagas");
                if (!vagasStr.isEmpty()) apt.setVagas(Integer.parseInt(vagasStr));
                imovel = apt;
                break;
                
            case "Terreno":
                Terreno terreno = new Terreno();
                terreno.setArea(area);
                terreno.setEndereco(new Endereco(endereco, json, tipo, endereco));
                imovel = terreno;
                break;
                
            case "Sala Comercial":
                SalaComercial sala = new SalaComercial();
                sala.setArea(area);
                sala.setEndereco(new Endereco(endereco, json, tipo, endereco));
                String andarSalaStr = extrairValor(json, "andar");
                if (!andarSalaStr.isEmpty()) sala.setAndar(Integer.parseInt(andarSalaStr));
                sala.setTemBanheiro(extrairValor(json, "temBanheiro").equals("true"));
                String vagasSalaStr = extrairValor(json, "vagas");
                if (!vagasSalaStr.isEmpty()) sala.setVagasEstacionamento(Integer.parseInt(vagasSalaStr));
                imovel = sala;
                break;
        }
        
        return imovel;
    }
    
    /**
     * Extrai valor de uma chave no JSON
     */
    private String extrairValor(String json, String chave) {
        String padrao = "\"" + chave + "\"\\s*:\\s*\"?([^,\"\\}\\n]+)\"?";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(padrao);
        java.util.regex.Matcher matcher = pattern.matcher(json);
        
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
    
    /**
     * Escapa caracteres especiais para JSON
     */
    private String escaparJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
