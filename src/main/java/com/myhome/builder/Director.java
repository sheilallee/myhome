package com.myhome.builder;

import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;

/**
 * RF01 - DIRECTOR (Builder Pattern)
 * 
 * Classe opcional do padrão Builder que define sequências
 * específicas de construção de anúncios.
 * 
 * RESPONSABILIDADES:
 * - Definir ordens específicas de chamadas ao Builder
 * - Encapsular diferentes formas de construir o produto
 * - Fornecer métodos convenientes para construções comuns
 * 
 * PADRÃO BUILDER:
 * O Director é um componente OPCIONAL que trabalha com o Builder.
 * Ele conhece quais passos executar para produzir configurações
 * específicas do produto.
 * 
 * BENEFÍCIOS:
 * - Reutilização de sequências de construção
 * - Código cliente mais limpo
 * - Separa lógica de construção do código cliente
 * 
 * QUANDO USAR:
 * - Quando há padrões comuns de construção
 * - Para simplificar o código cliente
 * - Para garantir sequências consistentes
 * 
 * EXEMPLO DE USO:
 * <pre>
 * AnuncioBuilder builder = new AnuncioBuilderImpl();
 * Director director = new Director(builder);
 * 
 * // Construir anúncio simples (apenas essenciais)
 * Anuncio simples = director.construirAnuncioSimples(
 *     "Casa em Boa Viagem", 
 *     850000.00, 
 *     imovel, 
 *     usuario
 * );
 * 
 * // Construir anúncio completo (com descrição e fotos)
 * Anuncio completo = director.construirAnuncioCompleto(
 *     "Apartamento Luxuoso", 
 *     1200000.00,
 *     "Apartamento de alto padrão...",
 *     imovel,
 *     usuario,
 *     fotos
 * );
 * </pre>
 */
public class Director {
    
    // ========================================
    // ATRIBUTOS
    // ========================================
    
    /**
     * Builder que será usado para construir os produtos.
     * 
     * PADRÃO BUILDER:
     * O Director trabalha com o Builder através da interface,
     * não conhecendo a implementação concreta.
     */
    private AnuncioBuilder builder;
    
    // ========================================
    // CONSTRUTOR
    // ========================================
    
    /**
     * Construtor do Director.
     * 
     * INJEÇÃO DE DEPENDÊNCIA:
     * O builder é injetado, permitindo trocar implementações.
     * 
     * @param builder Builder a ser usado
     */
    public Director(AnuncioBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Builder não pode ser null");
        }
        this.builder = builder;
    }
    
    // ========================================
    // MÉTODOS DE CONSTRUÇÃO
    // ========================================
    
    /**
     * Constrói um anúncio simples com apenas campos obrigatórios.
     * 
     * PADRÃO BUILDER + DIRECTOR:
     * Este método demonstra como o Director pode encapsular
     * uma sequência específica de construção.
     * 
     * ANÚNCIO SIMPLES contém:
     * - Título
     * - Preço
     * - Imóvel
     * - Anunciante
     * 
     * Não contém:
     * - Descrição detalhada
     * - Fotos
     * 
     * Útil para rascunhos ou anúncios rápidos.
     * 
     * @param titulo Título do anúncio
     * @param preco Preço do imóvel
     * @param imovel Imóvel a anunciar
     * @param anunciante Usuário anunciante
     * @return Anúncio simples construído
     */
    public Anuncio construirAnuncioSimples(
            String titulo, 
            double preco, 
            Imovel imovel, 
            Usuario anunciante) {
        
        // Sequência específica de construção para anúncio simples
        builder.reset();                    // 1. Limpa o estado
        builder.setTitulo(titulo);          // 2. Define título
        builder.setPreco(preco);            // 3. Define preço
        builder.setImovel(imovel);          // 4. Define imóvel
        builder.setAnunciante(anunciante);  // 5. Define anunciante
        return builder.build();             // 6. Constrói o produto
    }
    
    /**
     * Constrói um anúncio completo com todos os campos.
     * 
     * ANÚNCIO COMPLETO contém:
     * - Todos os campos obrigatórios
     * - Descrição detalhada
     * - Múltiplas fotos
     * 
     * Ideal para anúncios prontos para publicação.
     * 
     * @param titulo Título do anúncio
     * @param preco Preço do imóvel
     * @param descricao Descrição detalhada
     * @param imovel Imóvel a anunciar
     * @param anunciante Usuário anunciante
     * @param fotos Array de URLs das fotos
     * @return Anúncio completo construído
     */
    public Anuncio construirAnuncioCompleto(
            String titulo,
            double preco,
            String descricao,
            Imovel imovel,
            Usuario anunciante,
            String[] fotos) {
        
        // Inicia a construção
        builder.reset();
        builder.setTitulo(titulo);
        builder.setPreco(preco);
        builder.setDescricao(descricao);
        builder.setImovel(imovel);
        builder.setAnunciante(anunciante);
        
        // Adiciona todas as fotos
        if (fotos != null && fotos.length > 0) {
            for (String foto : fotos) {
                builder.addFoto(foto);
            }
        }
        
        // Constrói e retorna
        return builder.build();
    }
    
    /**
     * Constrói um anúncio com configuração padrão para imobiliárias.
     * 
     * EXEMPLO DE ESPECIALIZAÇÃO:
     * Demonstra como o Director pode ter métodos especializados
     * para diferentes tipos de usuários.
     * 
     * Anúncios de imobiliárias tipicamente incluem:
     * - Descrição profissional
     * - Mínimo de 5 fotos
     * - Informações de contato da imobiliária
     * 
     * @param titulo Título profissional
     * @param preco Preço
     * @param descricao Descrição comercial
     * @param imovel Imóvel
     * @param imobiliaria Usuário imobiliária
     * @param fotos Mínimo 5 fotos
     * @return Anúncio profissional
     */
    public Anuncio construirAnuncioImobiliaria(
            String titulo,
            double preco,
            String descricao,
            Imovel imovel,
            Usuario imobiliaria,
            String[] fotos) {
        
        // Validação específica para imobiliárias
        if (imobiliaria.getTipo() != Usuario.TipoUsuario.IMOBILIARIA) {
            throw new IllegalArgumentException(
                "Este método é apenas para usuários do tipo IMOBILIARIA");
        }
        
        if (fotos == null || fotos.length < 5) {
            throw new IllegalArgumentException(
                "Anúncios de imobiliária devem ter no mínimo 5 fotos");
        }
        
        // Usa o método completo com validações extras
        return construirAnuncioCompleto(titulo, preco, descricao, imovel, imobiliaria, fotos);
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    /**
     * Permite trocar o builder em tempo de execução.
     * 
     * FLEXIBILIDADE:
     * Permite usar diferentes builders com o mesmo Director.
     * 
     * @param builder Novo builder
     */
    public void changeBuilder(AnuncioBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Builder não pode ser null");
        }
        this.builder = builder;
    }
    
    /**
     * Retorna o builder atual.
     * 
     * @return Builder em uso
     */
    public AnuncioBuilder getBuilder() {
        return this.builder;
    }
}
