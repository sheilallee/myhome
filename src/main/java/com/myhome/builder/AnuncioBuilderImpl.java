package com.myhome.builder;

import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;
import java.util.UUID;

/**
 * RF01 - CONCRETE BUILDER (Builder Pattern)
 * 
 * Implementação concreta do AnuncioBuilder.
 * 
 * RESPONSABILIDADES:
 * - Implementar os passos de construção do Anuncio
 * - Manter o estado intermediário durante a construção
 * - Validar campos obrigatórios antes de construir
 * - Fornecer interface fluente para construção
 * 
 * PADRÃO BUILDER:
 * Esta classe é o "Concrete Builder" que implementa a interface Builder
 * e mantém referência ao produto sendo construído.
 * 
 * EXEMPLO DE USO COMPLETO:
 * <pre>
 * // 1. Criar imóvel com Factory Method
 * ImovelFactory factory = new CasaFactory();
 * Imovel casa = factory.criarImovel();
 * casa.setArea(120.0);
 * casa.setEndereco("Rua da Paz, 123");
 * 
 * // 2. Criar usuário anunciante
 * Usuario usuario = new Usuario("João Silva", "joao@email.com", "81999999999");
 * usuario.setTipo(Usuario.TipoUsuario.PROPRIETARIO);
 * 
 * // 3. Construir anúncio com Builder
 * AnuncioBuilder builder = new AnuncioBuilderImpl();
 * Anuncio anuncio = builder
 *     .reset()
 *     .setTitulo("Casa Aconchegante em Boa Viagem")
 *     .setDescricao("Linda casa com 3 quartos, 2 banheiros, garagem para 2 carros")
 *     .setPreco(850000.00)
 *     .setImovel(casa)
 *     .setAnunciante(usuario)
 *     .addFoto("foto1.jpg")
 *     .addFoto("foto2.jpg")
 *     .addFoto("foto3.jpg")
 *     .build();
 * </pre>
 */
public class AnuncioBuilderImpl implements AnuncioBuilder {
    
    // ========================================
    // ATRIBUTO PRINCIPAL
    // ========================================
    
    /**
     * Produto sendo construído.
     * 
     * PADRÃO BUILDER:
     * O builder mantém referência ao objeto sendo construído
     * e vai configurando-o passo a passo.
     */
    private Anuncio anuncio;
    
    // ========================================
    // CONSTRUTOR
    // ========================================
    
    /**
     * Construtor do Builder.
     * 
     * Inicializa com um novo produto vazio.
     */
    public AnuncioBuilderImpl() {
        this.reset();
    }
    
    // ========================================
    // IMPLEMENTAÇÃO DOS MÉTODOS DO BUILDER
    // ========================================
    
    /**
     * Reseta o builder criando uma nova instância de Anuncio.
     * 
     * PADRÃO BUILDER:
     * Permite reutilizar o mesmo builder para criar múltiplos anúncios.
     * 
     * Este método é chamado automaticamente:
     * - No construtor
     * - Após build() ser chamado
     * - Manualmente pelo cliente quando necessário
     */
    @Override
    public void reset() {
        this.anuncio = new Anuncio();
    }
    
    /**
     * Define o título do anúncio.
     * 
     * VALIDAÇÃO:
     * - Não aceita null
     * - Não aceita string vazia
     * 
     * @param titulo Título do anúncio
     * @return O próprio builder (fluent interface)
     * @throws IllegalArgumentException se título for inválido
     */
    @Override
    public AnuncioBuilder setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        this.anuncio.setTitulo(titulo.trim());
        return this; // Retorna this para permitir encadeamento
    }
    
    /**
     * Define a descrição do anúncio.
     * 
     * @param descricao Descrição detalhada
     * @return O próprio builder
     */
    @Override
    public AnuncioBuilder setDescricao(String descricao) {
        if (descricao != null) {
            this.anuncio.setDescricao(descricao.trim());
        }
        return this;
    }
    
    /**
     * Define o preço do anúncio.
     * 
     * VALIDAÇÃO BÁSICA:
     * - Preço deve ser maior que zero
     * 
     * NOTA: Validação detalhada será feita pelo PrecoValidoHandler (RF03)
     * durante a moderação.
     * 
     * @param preco Preço em reais
     * @return O próprio builder
     * @throws IllegalArgumentException se preço for inválido
     */
    @Override
    public AnuncioBuilder setPreco(double preco) {
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.anuncio.setPreco(preco);
        return this;
    }
    
    /**
     * Define o imóvel sendo anunciado.
     * 
     * VALIDAÇÃO:
     * - Imóvel não pode ser null
     * - Imóvel deve ser válido (validar())
     * 
     * INTEGRAÇÃO COM FACTORY METHOD:
     * O imóvel normalmente é criado por uma ImovelFactory antes
     * de ser passado para o builder.
     * 
     * @param imovel Imóvel a anunciar
     * @return O próprio builder
     * @throws IllegalArgumentException se imóvel for inválido
     */
    @Override
    public AnuncioBuilder setImovel(Imovel imovel) {
        if (imovel == null) {
            throw new IllegalArgumentException("Imóvel não pode ser null");
        }
        if (!imovel.validar()) {
            throw new IllegalArgumentException("Imóvel inválido: falta área ou endereço");
        }
        this.anuncio.setImovel(imovel);
        return this;
    }
    
    /**
     * Define o anunciante.
     * 
     * VALIDAÇÃO:
     * - Anunciante não pode ser null
     * - Anunciante deve ser válido (validar())
     * - Anunciante deve poder anunciar (podeAnunciar())
     * 
     * REGRA DE NEGÓCIO:
     * Apenas PROPRIETARIO, CORRETOR ou IMOBILIARIA podem anunciar.
     * 
     * @param anunciante Usuário anunciante
     * @return O próprio builder
     * @throws IllegalArgumentException se anunciante for inválido
     */
    @Override
    public AnuncioBuilder setAnunciante(Usuario anunciante) {
        if (anunciante == null) {
            throw new IllegalArgumentException("Anunciante não pode ser null");
        }
        if (!anunciante.validar()) {
            throw new IllegalArgumentException("Anunciante inválido: dados incompletos");
        }
        if (!anunciante.podeAnunciar()) {
            throw new IllegalArgumentException(
                "Apenas PROPRIETARIO, CORRETOR ou IMOBILIARIA podem criar anúncios");
        }
        this.anuncio.setAnunciante(anunciante);
        return this;
    }
    
    /**
     * Adiciona uma foto ao anúncio.
     * 
     * PADRÃO BUILDER:
     * Este método pode ser chamado múltiplas vezes para
     * adicionar várias fotos ao mesmo anúncio.
     * 
     * VALIDAÇÃO:
     * - URL não pode ser null ou vazia
     * 
     * REGRA DE NEGÓCIO (RF03):
     * O número de fotos será validado pelo FotoDescricaoHandler.
     * 
     * @param urlFoto URL ou caminho da foto
     * @return O próprio builder
     * @throws IllegalArgumentException se URL for inválida
     */
    @Override
    public AnuncioBuilder addFoto(String urlFoto) {
        if (urlFoto == null || urlFoto.trim().isEmpty()) {
            throw new IllegalArgumentException("URL da foto não pode ser vazia");
        }
        this.anuncio.adicionarFoto(urlFoto.trim());
        return this;
    }
    
    /**
     * Constrói e retorna o objeto Anuncio.
     * 
     * PADRÃO BUILDER:
     * Este é o método final que valida e retorna o produto construído.
     * 
     * VALIDAÇÃO:
     * Verifica se todos os campos OBRIGATÓRIOS foram preenchidos:
     * - Título
     * - Preço
     * - Imóvel
     * - Anunciante
     * 
     * COMPORTAMENTO PÓS-BUILD:
     * - Gera ID único para o anúncio
     * - Reseta o builder automaticamente
     * - Retorna o anúncio construído
     * 
     * @return Anúncio completo e validado
     * @throws IllegalStateException se campos obrigatórios estiverem faltando
     */
    @Override
    public Anuncio build() {
        // Validar campos obrigatórios
        if (anuncio.getTitulo() == null || anuncio.getTitulo().isEmpty()) {
            throw new IllegalStateException("Título é obrigatório");
        }
        
        if (anuncio.getPreco() <= 0) {
            throw new IllegalStateException("Preço é obrigatório e deve ser maior que zero");
        }
        
        if (anuncio.getImovel() == null) {
            throw new IllegalStateException("Imóvel é obrigatório");
        }
        
        if (anuncio.getAnunciante() == null) {
            throw new IllegalStateException("Anunciante é obrigatório");
        }
        
        // Validação geral do anúncio
        if (!anuncio.validar()) {
            throw new IllegalStateException("Anúncio inválido: verifique todos os campos");
        }
        
        // Gerar ID único para o anúncio
        anuncio.setId(UUID.randomUUID().toString());
        
        // Guardar referência ao anúncio construído
        Anuncio anuncioConstruido = this.anuncio;
        
        // Resetar o builder para permitir construir novo anúncio
        this.reset();
        
        // Retornar o produto finalizado
        return anuncioConstruido;
    }
    
    /**
     * Retorna o anúncio em construção.
     * 
     * CUIDADO: Este método expõe o produto parcialmente construído.
     * Use apenas para inspeção/debug.
     * Para obter o produto final, use build().
     * 
     * @return Anúncio sendo construído (pode estar incompleto)
     */
    public Anuncio getAnuncio() {
        return this.anuncio;
    }
}
