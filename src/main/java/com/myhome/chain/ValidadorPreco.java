package com.myhome.chain;

import com.myhome.singleton.ConfigurationManager;

public class ValidadorPreco extends ModeradorBase {
    private final double precoMinimo;
    private final double precoMaximo;

    /**
     * Construtor que define os limites de preço
     * Utiliza o Singleton ConfigurationManager para obter os valores
     * Com fallback para valores padrão caso as propriedades não sejam encontradas
     */
    public ValidadorPreco() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        // Valores padrão (preços em reais)
        // Padrão: R$ 500,00 a R$ 3.000.000,00
        final double PRECO_MINIMO_DEFAULT = 500.0;
        final double PRECO_MAXIMO_DEFAULT = 3000000.0;
        
        // Tentar carregar do arquivo de propriedades
        // Se não encontrar, usar valores padrão
        this.precoMinimo = config.getPropertyAsDouble("moderacao.preco.minimo", PRECO_MINIMO_DEFAULT);
        this.precoMaximo = config.getPropertyAsDouble("moderacao.preco.maximo", PRECO_MAXIMO_DEFAULT);
        
        // Log de inicialização
        System.out.println("✅ ValidadorPreco inicializado:");
        System.out.println("   Preço mínimo: R$ " + String.format("%,.2f", this.precoMinimo));
        System.out.println("   Preço máximo: R$ " + String.format("%,.2f", this.precoMaximo));
    }

    @Override
    public boolean handle(com.myhome.model.Anuncio anuncio) {
        System.out.println("🔍 Validando faixa de preço...");
        
        double preco = anuncio.getPreco();
        
        if (preco < precoMinimo || preco > precoMaximo) {
            System.out.println("   ❌ Rejeitado: preço fora dos limites");
            System.out.println("   Preço informado: R$ " + String.format("%,.2f", preco));
            System.out.println("   Intervalo permitido: R$ " + String.format("%,.2f", precoMinimo) 
                + " a R$ " + String.format("%,.2f", precoMaximo));
            return false;
        }
        
        System.out.println("   ✅ Preço validado com sucesso!");
        return handleNext(anuncio);
    }
}
