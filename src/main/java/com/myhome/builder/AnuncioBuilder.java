package com.myhome.builder;

import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;

/**
 * RF01 - BUILDER PATTERN (Interface)
 * 
 * Interface que define o contrato para construção de objetos Anuncio.
 * 
 * RESPONSABILIDADES:
 * - Definir os métodos de construção passo a passo
 * - Permitir construção fluente (method chaining)
 * - Separar a construção de um objeto complexo da sua representação
 * 
 * ESTRUTURA DO BUILDER:
 * 1. Builder (esta interface) - declara os passos de construção
 * 2. ConcreteBuilder (AnuncioBuilderImpl) - implementa os passos
 * 3. Director - coordena a sequência de construção (opcional)
 * 4. Product (Anuncio) - objeto complexo sendo construído
 * 
 * BENEFÍCIOS:
 * - Permite criar objetos passo a passo
 * - Suporta diferentes representações do mesmo objeto
 * - Isola código complexo de construção
 * - Permite construção fluente e legível
 * 
 * EXEMPLO DE USO:
 * <pre>
 * AnuncioBuilder builder = new AnuncioBuilderImpl();
 * Anuncio anuncio = builder
 *     .reset()
 *     .setTitulo("Casa em Boa Viagem")
 *     .setPreco(850000.00)
 *     .setImovel(imovel)
 *     .setAnunciante(usuario)
 *     .addFoto("foto1.jpg")
 *     .addFoto("foto2.jpg")
 *     .build();
 * </pre>
 */
public interface AnuncioBuilder {
    
    /**
     * Reseta o builder para um estado inicial limpo.
     * 
     * PADRÃO BUILDER:
     * Permite reutilizar o mesmo builder para criar múltiplos objetos.
     * 
     * IMPORTANTE: Este método deve ser chamado antes de iniciar
     * a construção de um novo anúncio.
     */
    void reset();
    
    /**
     * Define o título do anúncio.
     * 
     * CAMPO OBRIGATÓRIO.
     * 
     * FLUENT INTERFACE:
     * Retorna o próprio builder para permitir encadeamento de métodos.
     * 
     * @param titulo Título do anúncio
     * @return O próprio builder para encadeamento
     */
    AnuncioBuilder setTitulo(String titulo);
    
    /**
     * Define a descrição do anúncio.
     * 
     * CAMPO OPCIONAL, mas recomendado para passar pela moderação.
     * 
     * @param descricao Descrição detalhada
     * @return O próprio builder
     */
    AnuncioBuilder setDescricao(String descricao);
    
    /**
     * Define o preço do imóvel.
     * 
     * CAMPO OBRIGATÓRIO.
     * 
     * REGRA DE NEGÓCIO:
     * O preço será validado pelo PrecoValidoHandler (RF03)
     * durante a moderação.
     * 
     * @param preco Preço em reais
     * @return O próprio builder
     */
    AnuncioBuilder setPreco(double preco);
    
    /**
     * Define o imóvel sendo anunciado.
     * 
     * CAMPO OBRIGATÓRIO.
     * 
     * INTEGRAÇÃO COM FACTORY METHOD:
     * O imóvel pode ter sido criado por uma ImovelFactory.
     * 
     * @param imovel Imóvel a ser anunciado
     * @return O próprio builder
     */
    AnuncioBuilder setImovel(Imovel imovel);
    
    /**
     * Define o anunciante (usuário que cria o anúncio).
     * 
     * CAMPO OBRIGATÓRIO.
     * 
     * REGRA DE NEGÓCIO:
     * Apenas usuários dos tipos PROPRIETARIO, CORRETOR ou IMOBILIARIA
     * podem ser anunciantes.
     * 
     * @param anunciante Usuário anunciante
     * @return O próprio builder
     */
    AnuncioBuilder setAnunciante(Usuario anunciante);
    
    /**
     * Adiciona uma foto ao anúncio.
     * 
     * CAMPO OPCIONAL, mas recomendado.
     * 
     * REGRA DE NEGÓCIO (RF03):
     * O número mínimo de fotos é validado pelo FotoDescricaoHandler.
     * 
     * Este método pode ser chamado múltiplas vezes para adicionar
     * várias fotos.
     * 
     * @param urlFoto URL ou caminho da foto
     * @return O próprio builder
     */
    AnuncioBuilder addFoto(String urlFoto);
    
    /**
     * Constrói e retorna o objeto Anuncio.
     * 
     * PADRÃO BUILDER:
     * Este é o método final que cria o produto.
     * 
     * VALIDAÇÃO:
     * - Verifica se todos os campos obrigatórios foram preenchidos
     * - Lança exceção se o anúncio não estiver válido
     * 
     * IMPORTANTE:
     * Após chamar build(), o builder deve ser resetado
     * antes de construir um novo anúncio.
     * 
     * @return Anúncio construído
     * @throws IllegalStateException se campos obrigatórios estiverem faltando
     */
    Anuncio build();
}
