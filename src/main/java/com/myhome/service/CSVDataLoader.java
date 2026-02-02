package com.myhome.service;

import com.myhome.factory.*;
import com.myhome.model.*;
import com.myhome.strategy.EmailNotificacao;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Carregador de dados a partir de arquivo CSV único
 * 
 * RESPONSABILIDADES (E1 - Requisito de Execução):
 * - Carregar 10 anúncios com 5 usuários seed data do CSV
 * - Criar arquivo CSV de exemplo se não existir
 * - Retornar mapa contendo usuários e anúncios já associados
 * 
 * FORMATO DO CSV:
 * usuario_nome,usuario_email,usuario_telefone,titulo,preco,descricao,tipo_anuncio,
 * tipo_imovel,rua,numero,cidade,estado,area,quartos,banheiros,andar,vagas
 * 
 * @author MyHome Team
 */
public class CSVDataLoader {
    
    private static final String DATA_DIR = "data";
    private static final String ANUNCIOS_CSV = DATA_DIR + "/anuncios_seed.csv";
    
    /**
     * Carrega dados iniciais e retorna Map contendo:
     * - "usuarios": List<Usuario>
     * - "anuncios": List<Anuncio>
     */
    public Map<String, Object> carregarDadosIniciais() {
        Map<String, Object> resultado = new HashMap<>();
        List<Usuario> usuarios = new ArrayList<>();
        List<Anuncio> anuncios = new ArrayList<>();
        
        System.out.println("\n📂 Carregando dados iniciais do CSV...\n");
        
        try {
            // Criar diretório se não existir
            Files.createDirectories(Paths.get(DATA_DIR));
            
            // Se arquivo não existe, criar com dados de exemplo
            if (!Files.exists(Paths.get(ANUNCIOS_CSV))) {
                criarArquivoExemploAnuncios();
            }
            
            // Carregar do CSV
            List<String> linhas = Files.readAllLines(Paths.get(ANUNCIOS_CSV));
            Map<String, Usuario> usuariosMap = new LinkedHashMap<>(); // Mantém ordem + evita duplicatas
            
            for (int i = 1; i < linhas.size(); i++) {
                String linha = linhas.get(i).trim();
                if (linha.isEmpty() || linha.startsWith("#")) continue;
                
                String[] campos = linha.split(",", -1); // -1 para manter campos vazios
                if (campos.length < 17) continue; // Mínimo de campos esperados
                
                try {
                    // ===== DADOS DO USUÁRIO =====
                    String usuarioNome = campos[0].trim();
                    String usuarioEmail = campos[1].trim();
                    String usuarioTelefone = campos[2].trim();
                    
                    // Criar usuário se não existe (usar email como chave única)
                    if (!usuariosMap.containsKey(usuarioEmail)) {
                        Usuario usuario = new Usuario(usuarioNome, usuarioEmail, usuarioTelefone);
                        usuario.setTipo(Usuario.TipoUsuario.PROPRIETARIO);
                        usuario.setCanalNotificacao(new EmailNotificacao(new EmailService()));
                        usuariosMap.put(usuarioEmail, usuario);
                    }
                    Usuario anunciante = usuariosMap.get(usuarioEmail);
                    
                    // ===== DADOS DO ANÚNCIO =====
                    String titulo = campos[3].trim();
                    double preco = parseDouble(campos[4]);
                    String descricao = campos[5].trim();
                    String tipoAnuncio = campos[6].trim(); // VENDA, ALUGUEL, TEMPORADA
                    
                    // ===== DADOS DO IMÓVEL =====
                    String tipoImovel = campos[7].trim(); // Casa, Apartamento, Terreno, SalaComercial
                    String rua = campos[8].trim();
                    String numero = campos[9].trim();
                    String cidade = campos[10].trim();
                    String estado = campos[11].trim();
                    double area = parseDouble(campos[12]);
                    int quartos = parseInt(campos[13]);
                    int banheiros = parseInt(campos[14]);
                    int andar = parseInt(campos[15]);
                    int vagas = parseInt(campos[16]);
                    
                    // Criar imóvel específico
                    Endereco endereco = new Endereco(rua, numero, cidade, estado);
                    Imovel imovel = criarImovel(tipoImovel, area, endereco, quartos, banheiros, andar, vagas);
                    
                    if (imovel == null) {
                        System.err.println("❌ Tipo de imóvel inválido: " + tipoImovel);
                        continue;
                    }
                    
                    // Usar Factory para criar anúncio
                    AnuncioFactory factory = selecionarFactory(tipoAnuncio);
                    if (factory == null) {
                        System.err.println("❌ Tipo de anúncio inválido: " + tipoAnuncio);
                        continue;
                    }
                    
                    Anuncio anuncio = factory.criarAnuncio(titulo, preco, descricao, imovel, anunciante);
                    anuncios.add(anuncio);
                    
                } catch (Exception e) {
                    System.err.println("❌ Erro ao processar linha " + (i + 1) + ": " + e.getMessage());
                    continue;
                }
            }
            
            usuarios.addAll(usuariosMap.values());
            
            if (!usuarios.isEmpty()) {
                System.out.println("✅ " + usuarios.size() + " usuário(s) carregado(s)");
            }
            if (!anuncios.isEmpty()) {
                System.out.println("✅ " + anuncios.size() + " anúncio(s) carregado(s)");
            }
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar CSV: " + e.getMessage());
        }
        
        resultado.put("usuarios", usuarios);
        resultado.put("anuncios", anuncios);
        return resultado;
    }
    
    /**
     * Seleciona a factory apropriada baseado no tipo de anúncio
     */
    private AnuncioFactory selecionarFactory(String tipo) {
        switch (tipo.toUpperCase()) {
            case "VENDA":
                return new VendaFactory();
            case "ALUGUEL":
                return new AluguelFactory();
            case "TEMPORADA":
                return new TemporadaFactory();
            default:
                return null;
        }
    }
    
    /**
     * Cria imóvel específico baseado no tipo
     */
    private Imovel criarImovel(String tipo, double area, Endereco endereco, 
                               int quartos, int banheiros, int andar, int vagas) {
        switch (tipo) {
            case "Casa":
                Casa casa = new Casa();
                casa.setArea(area);
                casa.setEndereco(endereco);
                if (quartos > 0) casa.setQuartos(quartos);
                if (banheiros > 0) casa.setBanheiros(banheiros);
                if (vagas > 0) casa.setTemGaragem(true);
                return casa;
                
            case "Apartamento":
                Apartamento apt = new Apartamento();
                apt.setArea(area);
                apt.setEndereco(endereco);
                if (quartos > 0) apt.setQuartos(quartos);
                if (banheiros > 0) apt.setBanheiros(banheiros);
                if (andar > 0) apt.setAndar(andar);
                if (vagas > 0) apt.setVagas(vagas);
                return apt;
                
            case "Terreno":
                Terreno terreno = new Terreno();
                terreno.setArea(area);
                terreno.setEndereco(endereco);
                terreno.setZoneamento("Urbano");
                return terreno;
                
            case "SalaComercial":
                SalaComercial sala = new SalaComercial();
                sala.setArea(area);
                sala.setEndereco(endereco);
                if (andar > 0) sala.setAndar(andar);
                if (vagas > 0) sala.setVagasEstacionamento(vagas);
                return sala;
                
            default:
                return null;
        }
    }
    
    /**
     * Cria arquivo CSV de exemplo com 10 anúncios de 5 usuários
     */
    private void criarArquivoExemploAnuncios() {
        String conteudo = "# Arquivo de Anúncios com Seed Data (E1 - Requisito de Execução)\n"
            + "# Formato: usuario_nome,usuario_email,usuario_telefone,titulo,preco,descricao,tipo_anuncio,tipo_imovel,rua,numero,cidade,estado,area,quartos,banheiros,andar,vagas\n"
            + "# Nota: 5 usuários com 2 anúncios cada (total: 10 anúncios)\n"
            + "\n"
            + "# USUÁRIO 1: João Silva (2 anúncios)\n"
            + "João Silva,joao@email.com,83988881111,Casa em João Pessoa,350000,Casa moderna com 3 quartos,VENDA,Casa,Rua das Flores,123,João Pessoa,PB,150.0,3,2,0,2\n"
            + "João Silva,joao@email.com,83988881111,Lote na Praia,250000,Terreno ideal para construção,VENDA,Terreno,Avenida Litoral,456,Cabedelo,PB,500.0,0,0,0,0\n"
            + "\n"
            + "# USUÁRIO 2: Maria Santos (2 anúncios)\n"
            + "Maria Santos,maria@email.com,83988882222,Apartamento em Campina Grande,180000,Apto confortável no centro,VENDA,Apartamento,Avenida Getúlio Vargas,789,Campina Grande,PB,85.0,2,1,5,1\n"
            + "Maria Santos,maria@email.com,83988882222,Aluguel de Sala Comercial,2500,Espaço comercial bem localizado,ALUGUEL,SalaComercial,Rua Comércio,321,Campina Grande,PB,120.0,0,1,2,2\n"
            + "\n"
            + "# USUÁRIO 3: Pedro Oliveira (2 anúncios)\n"
            + "Pedro Oliveira,pedro@email.com,83988883333,Casa com Piscina,450000,Casa de luxo com piscina,VENDA,Casa,Rua Privilégio,654,João Pessoa,PB,200.0,4,3,0,3\n"
            + "Pedro Oliveira,pedro@email.com,83988883333,Temporada Praia,500,Aluguel por temporada,TEMPORADA,Casa,Praia Bela,987,Guarabira,PB,180.0,3,2,0,1\n"
            + "\n"
            + "# USUÁRIO 4: Ana Costa (2 anúncios)\n"
            + "Ana Costa,ana@email.com,83988884444,Apto para Aluguel,1200,Apartamento moderno para alugar,ALUGUEL,Apartamento,Avenida Brasil,111,Patos,PB,90.0,2,2,3,1\n"
            + "Ana Costa,ana@email.com,83988884444,Terreno Comercial,150000,Grande área para comércio,VENDA,Terreno,Avenida Industrial,222,Patos,PB,1000.0,0,0,0,0\n"
            + "\n"
            + "# USUÁRIO 5: Carlos Mendes (2 anúncios)\n"
            + "Carlos Mendes,carlos@email.com,83988885555,Fazenda no Interior,800000,Grande propriedade rural,VENDA,Casa,Estrada Rural,333,Areia,PB,5000.0,5,4,0,5\n"
            + "Carlos Mendes,carlos@email.com,83988885555,Espaço de Trabalho,3000,Estúdio para profissionais,ALUGUEL,SalaComercial,Rua Profissional,444,Areia,PB,200.0,0,2,1,4\n";
        
        try {
            Files.write(Paths.get(ANUNCIOS_CSV), conteudo.getBytes());
            System.out.println("✅ Arquivo anuncios_seed.csv criado com dados de exemplo (10 anúncios de 5 usuários)");
        } catch (IOException e) {
            System.err.println("❌ Erro ao criar arquivo anuncios_seed.csv: " + e.getMessage());
        }
    }
    
    // Métodos auxiliares para parse seguro
    private double parseDouble(String valor) {
        try {
            return valor == null || valor.isEmpty() ? 0.0 : Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private int parseInt(String valor) {
        try {
            return valor == null || valor.isEmpty() ? 0 : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

