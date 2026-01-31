package com.myhome.service;

import com.myhome.builder.AnuncioBuilder;
import com.myhome.builder.AnuncioBuilderImpl;
import com.myhome.builder.Director;
import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;
import com.myhome.observer.LogObserver;
import com.myhome.observer.NotificationObserver;
import com.myhome.strategy.NotificationManager;
/**
 * SERVIÇO DE GERENCIAMENTO DE ANÚNCIOS
 * 
 * RESPONSABILIDADE:
 * - Encapsular a lógica de criação de anúncios usando Builder Pattern
 * - Garantir configuração automática de observers (RF04)
 * - Fornecer interface de alto nível para criação de anúncios
 * 
 * PRINCÍPIOS SOLID APLICADOS:
 * - SRP: Responsável apenas por gerenciar anúncios
 * - DIP: Cliente depende desta interface, não do Builder concreto
 * - ISP: Interface coesa com métodos específicos para cada tipo de anúncio
 */
public class AnuncioService {
    
    private final AnuncioBuilder builder;
    private final Director director;
    
    /**
     * Construtor que inicializa o Builder e Director.
     * O cliente não precisa saber sobre essas dependências.
     */
    public AnuncioService() {
        this.builder = new AnuncioBuilderImpl();
        this.director = new Director(builder);
    }

    // =====================================================
    // CONFIGURAÇÃO DE OBSERVERS (RF04)
    // =====================================================

    /**
     * Configura automaticamente os observers do anúncio:
     * - Log de mudança de estado
     * - Notificação ao anunciante
     */
    private void configurarObservers(Anuncio anuncio) {
        LoggerService logger = new LoggerService();
        NotificationManager manager = new NotificationManager();

        anuncio.adicionarObserver(new LogObserver(logger));
        anuncio.adicionarObserver(new NotificationObserver(manager));
    }
    
    // =====================================================
    // MÉTODOS DE CRIAÇÃO DE ANÚNCIOS
    // =====================================================

    /**
     * Cria um anúncio simples (uso mais comum).
     */
    public Anuncio criarAnuncioSimples(String titulo, double preco, 
                                       Imovel imovel, Usuario anunciante) {

        Anuncio anuncio = director.construirAnuncioSimples(
            titulo, preco, imovel, anunciante
        );

        configurarObservers(anuncio);
        return anuncio;
    }

    /**
     * Cria um anúncio completo com descrição e fotos.
     */
    public Anuncio criarAnuncioCompleto(String titulo, double preco, String descricao,
                                        Imovel imovel, Usuario anunciante, String[] fotos) {

        Anuncio anuncio = director.construirAnuncioCompleto(
            titulo, preco, descricao, imovel, anunciante, fotos
        );

        configurarObservers(anuncio);
        return anuncio;
    }
    
    /**
     * Cria um anúncio profissional para imobiliárias.
     */
    public Anuncio criarAnuncioImobiliaria(String titulo, double preco, String descricao,
                                           Imovel imovel, Usuario imobiliaria, String[] fotos) {

        Anuncio anuncio = director.construirAnuncioImobiliaria(
            titulo, preco, descricao, imovel, imobiliaria, fotos
        );

        configurarObservers(anuncio);
        return anuncio;
    }
    
    /**
     * Cria um anúncio personalizado (quando os métodos acima não atendem).
     */
    public Anuncio criarAnuncioPersonalizado(String titulo, String descricao, 
                                             double preco, Imovel imovel, 
                                             Usuario anunciante, String[] fotos) {

        builder.reset();
        builder.setTitulo(titulo);
        builder.setDescricao(descricao);
        builder.setPreco(preco);
        builder.setImovel(imovel);
        builder.setAnunciante(anunciante);
        
        if (fotos != null) {
            for (String foto : fotos) {
                builder.addFoto(foto);
            }
        }
        
        Anuncio anuncio = builder.build();
        configurarObservers(anuncio);
        return anuncio;
    }
    
    /**
     * Valida se um anúncio está pronto para publicação.
     */
    public boolean validarAnuncio(Anuncio anuncio) {
        return anuncio.validar();
    }
}
