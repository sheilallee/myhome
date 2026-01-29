package com.myhome.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RF01 - ENTIDADE DE DOMÍNIO (Produto do Builder Pattern)
 * 
 * Representa um Anúncio de imóvel no sistema MyHome.
 * 
 * RESPONSABILIDADES:
 * - Armazenar informações de um anúncio
 * - Gerenciar o ciclo de vida (através do State Pattern - RF04)
 * - Notificar observadores (através do Observer Pattern - RF04)
 * 
 * PADRÕES RELACIONADOS:
 * - RF01: Builder - Anúncio é construído pelo AnuncioBuilder
 * - RF04: State - Gerencia estados do anúncio
 * - RF04: Observer - Notifica mudanças de estado
 */
public class Anuncio {
    
    // ========================================
    // ATRIBUTOS
    // ========================================
    
    /**
     * Identificador único do anúncio
     */
    private String id;
    
    /**
     * Título do anúncio
     * OBRIGATÓRIO no Builder
     */
    private String titulo;
    
    /**
     * Preço do imóvel anunciado
     * OBRIGATÓRIO no Builder
     */
    private double preco;
    
    /**
     * Descrição detalhada do anúncio
     */
    private String descricao;
    
    /**
     * Imóvel sendo anunciado
     * OBRIGATÓRIO no Builder
     */
    private Imovel imovel;
    
    /**
     * Usuário que criou o anúncio (anunciante)
     */
    private Usuario anunciante;
    
    /**
     * Lista de URLs das fotos do imóvel
     */
    private List<String> fotos;
    
    /**
     * Data de criação do anúncio
     */
    private Date dataCriacao;
    
    /**
     * Data de publicação (quando o anúncio foi aprovado)
     */
    private Date dataPublicacao;
    
    /**
     * Estado atual do anúncio (para o State Pattern - RF04)
     * Será do tipo EstadoAnuncio quando implementado
     */
    // private EstadoAnuncio estado; // Será implementado em RF04
    
    /**
     * Lista de observadores do anúncio (Observer Pattern - RF04)
     * Será do tipo List<AnuncioObserver> quando implementado
     */
    // private List<AnuncioObserver> observers; // Será implementado em RF04
    
    // ========================================
    // CONSTRUTOR
    // ========================================
    
    /**
     * Construtor padrão (público para o Builder).
     * 
     * IMPORTANTE: Este construtor NÃO deve ser chamado diretamente pelo cliente.
     * A criação de Anuncios deve ser feita APENAS através do AnuncioBuilder.
     * 
     * PADRÃO BUILDER:
     * Este construtor é público para permitir que o Builder (em outro pacote)
     * possa criar instâncias, mas a convenção do projeto é usar SEMPRE o Builder.
     */
    public Anuncio() {
        this.fotos = new ArrayList<>();
        this.dataCriacao = new Date();
    }
    
    // ========================================
    // MÉTODOS DE NEGÓCIO
    // ========================================
    
    /**
     * Valida se o anúncio possui as informações mínimas necessárias.
     * 
     * REGRA DE NEGÓCIO:
     * - Título não pode ser vazio
     * - Preço deve ser maior que zero
     * - Deve ter um imóvel associado
     * - O imóvel deve ser válido
     * 
     * @return true se válido, false caso contrário
     */
    public boolean validar() {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        
        if (preco <= 0) {
            return false;
        }
        
        if (imovel == null) {
            return false;
        }
        
        return imovel.validar();
    }
    
    /**
     * Adiciona uma foto ao anúncio.
     * 
     * @param urlFoto URL da foto
     */
    public void adicionarFoto(String urlFoto) {
        if (urlFoto != null && !urlFoto.trim().isEmpty()) {
            this.fotos.add(urlFoto);
        }
    }
    
    /**
     * Remove uma foto do anúncio.
     * 
     * @param urlFoto URL da foto a remover
     * @return true se removida com sucesso
     */
    public boolean removerFoto(String urlFoto) {
        return this.fotos.remove(urlFoto);
    }
    
    /**
     * Retorna o número de fotos do anúncio.
     * 
     * Usado pelo FotoDescricaoHandler (RF03) para validação.
     * 
     * @return Quantidade de fotos
     */
    public int getQuantidadeFotos() {
        return this.fotos.size();
    }
    
    // Os métodos abaixo serão implementados em RF04 (State + Observer)
    
    /**
     * Muda o estado do anúncio.
     * 
     * PADRÃO STATE (RF04):
     * Delega a mudança para o objeto EstadoAnuncio.
     * 
     * @param novoEstado Novo estado
     */
    /*
    public void mudarEstado(EstadoAnuncio novoEstado) {
        this.estado = novoEstado;
        notifyObservers(); // Notifica observadores da mudança
    }
    */
    
    /**
     * Adiciona um observador ao anúncio.
     * 
     * PADRÃO OBSERVER (RF04):
     * Permite que objetos sejam notificados de mudanças no anúncio.
     * 
     * @param observer Observador a adicionar
     */
    /*
    public void addObserver(AnuncioObserver observer) {
        this.observers.add(observer);
    }
    */
    
    /**
     * Remove um observador do anúncio.
     * 
     * @param observer Observador a remover
     */
    /*
    public void removeObserver(AnuncioObserver observer) {
        this.observers.remove(observer);
    }
    */
    
    /**
     * Notifica todos os observadores sobre mudanças.
     * 
     * PADRÃO OBSERVER (RF04):
     * Itera sobre todos os observadores e chama seus métodos update().
     */
    /*
    public void notifyObservers() {
        for (AnuncioObserver observer : observers) {
            observer.update(this);
        }
    }
    */
    
    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    public String getId() {
        return id;
    }
    
    // Setters públicos - usados pelo Builder
    // IMPORTANTE: Cliente deve usar Builder, não setters diretamente
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public double getPreco() {
        return preco;
    }
    
    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Imovel getImovel() {
        return imovel;
    }
    
    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }
    
    public Usuario getAnunciante() {
        return anunciante;
    }
    
    public void setAnunciante(Usuario anunciante) {
        this.anunciante = anunciante;
    }
    
    public List<String> getFotos() {
        return new ArrayList<>(fotos);
    }
    
    public void setFotos(List<String> fotos) {
        this.fotos = new ArrayList<>(fotos);
    }
    
    public Date getDataCriacao() {
        return dataCriacao;
    }
    
    public Date getDataPublicacao() {
        return dataPublicacao;
    }
    
    public void setDataPublicacao(Date dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    @Override
    public String toString() {
        return String.format("Anúncio #%s: %s - R$ %.2f (%s)", 
            id, titulo, preco, imovel != null ? imovel.getTipo() : "Sem imóvel");
    }
}
