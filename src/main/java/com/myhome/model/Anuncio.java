package com.myhome.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myhome.state.AnuncioState;
import com.myhome.observer.AnuncioObserver;
import com.myhome.state.RascunhoState;


/**
 * RF01 - ENTIDADE DE DOMÍNIO (Produto do Builder Pattern)
 * 
 * Representa um Anúncio de imóvel no sistema MyHome.
 * 
 * RESPONSABILIDADES:
 * - Armazenar informações de um anúncio
 * - Gerenciar o ciclo de vida (State Pattern - RF04)
 * - Notificar observadores (Observer Pattern - RF04)
 */
public class Anuncio {

    // ========================================
    // ATRIBUTOS
    // ========================================

    private String id;
    private String titulo;
    private double preco;
    private String descricao;
    private Imovel imovel;
    private Usuario anunciante;
    private List<String> fotos;
    private Date dataCriacao;
    private Date dataPublicacao;

    /**
     * Estado atual do anúncio
     * State Pattern - RF04
     */
    private AnuncioState estado;

    /**
     * Observadores do anúncio
     * Observer Pattern - RF04
     */
    private List<AnuncioObserver> observers = new ArrayList<>();

    // ========================================
    // CONSTRUTOR
    // ========================================

    public Anuncio() {
        this.fotos = new ArrayList<>();
        this.dataCriacao = new Date();
        this.estado = new RascunhoState(this); // ESTADO INICIAL

    }
    // ========================================
    // STATE + OBSERVER (AJUSTE SOLICITADO)
    // ========================================

    public AnuncioState getEstado() {
        return estado;
    }

    /**
     * Restaura o estado do anúncio a partir de um nome salvo.
     * Usado ao carregar anúncios do JSON.
     */
    public void restaurarEstado(String estadoNome) {
        switch (estadoNome.trim().toLowerCase()) {
            case "rascunho":
                this.estado = new RascunhoState(this);
                break;
            case "moderação":
                this.estado = new com.myhome.state.ModeracaoState(this);
                break;
            case "ativo":
                this.estado = new com.myhome.state.AtivoState(this);
                break;
            case "suspenso":
                this.estado = new com.myhome.state.SuspensoState(this);
                break;
            case "vendido":
                this.estado = new com.myhome.state.VendidoState(this);
                break;
            default:
                // Se estado desconhecido, volta para Rascunho
                this.estado = new RascunhoState(this);
                System.out.println("⚠️  Estado desconhecido '" + estadoNome + "'. Restaurando para Rascunho.");
        }
    }

    /**
     * Altera o estado do anúncio e notifica os observadores.
     */
    public void mudarEstado(AnuncioState novoEstado) {
        AnuncioState estadoAnterior = this.estado;
        this.estado = novoEstado;
        notificarObservers(estadoAnterior, novoEstado);
    }

    /**
     * Adiciona um observador ao anúncio.
     */
    public void adicionarObserver(AnuncioObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove um observador do anúncio.
     */
    public void removerObserver(AnuncioObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores sobre a mudança de estado.
     */
    private void notificarObservers(
            AnuncioState estadoAnterior,
            AnuncioState novoEstado) {

        for (AnuncioObserver observer : observers) {
            observer.onEstadoAlterado(this, estadoAnterior, novoEstado);
        }
    }

    // ========================================
    // GETTERS E SETTERS
    // ========================================

    public String getId() {
        return id;
    }

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

    public int getQuantidadeFotos() {
        return this.fotos.size();
    }

    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================

    @Override
    public String toString() {
        return String.format(
            "Anúncio #%s: %s - R$ %.2f (%s)",
            id,
            titulo,
            preco,
            imovel != null ? imovel.getTipo() : "Sem imóvel"
        );
    }
}