package com.myhome.service;

import com.myhome.builder.AnuncioBuilder;
import com.myhome.builder.AnuncioBuilderImpl;
import com.myhome.builder.Director;
import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;

/**
 * SERVIÇO DE GERENCIAMENTO DE ANÚNCIOS
 * 
 * RESPONSABILIDADE:
 * - Encapsular a lógica de criação de anúncios usando Builder Pattern
 * - Fornecer interface de alto nível para criação de anúncios
 * - Ocultar detalhes do Builder e Director do cliente
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
    
    /**
     * Cria um anúncio simples (uso mais comum).
     * 
     * @param titulo Título do anúncio
     * @param preco Preço do imóvel
     * @param imovel Imóvel a ser anunciado
     * @param anunciante Usuário que está anunciando
     * @return Anúncio criado e validado
     */
    public Anuncio criarAnuncioSimples(String titulo, double preco, 
                                       Imovel imovel, Usuario anunciante) {
        return director.construirAnuncioSimples(titulo, preco, imovel, anunciante);
    }
    
    /**
     * Cria um anúncio completo com descrição e fotos.
     */
    public Anuncio criarAnuncioCompleto(String titulo, double preco, String descricao,
                                       Imovel imovel, Usuario anunciante, String[] fotos) {
        return director.construirAnuncioCompleto(titulo, preco, descricao, 
                                                imovel, anunciante, fotos);
    }
    
    /**
     * Cria um anúncio profissional para imobiliárias.
     */
    public Anuncio criarAnuncioImobiliaria(String titulo, double preco, String descricao,
                                          Imovel imovel, Usuario imobiliaria, String[] fotos) {
        return director.construirAnuncioImobiliaria(titulo, preco, descricao, 
                                                   imovel, imobiliaria, fotos);
    }
    
    /**
     * Cria um anúncio personalizado (quando os métodos acima não atendem).
     * Ainda assim, encapsula o uso do Builder.
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
        
        return builder.build();
    }
    
    /**
     * Valida se um anúncio está pronto para publicação.
     */
    public boolean validarAnuncio(Anuncio anuncio) {
        return anuncio.validar();
    }
}
