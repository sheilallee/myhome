package com.myhome.chain;

import com.myhome.singleton.ConfigurationManager;

public class ValidadorPreco extends ModeradorBase {
    private final double precoMinimo;
    private final double precoMaximo;

    /**
     * Construtor que define os limites de preço
     * Utiliza o Singleton ConfigurationManager para obter os valores
     */
    public ValidadorPreco() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        this.precoMinimo = config.getPropertyAsDouble("moderacao.preco.minimo");
        this.precoMaximo = config.getPropertyAsDouble("moderacao.preco.maximo");
    }

    @Override
    public boolean handle(com.myhome.model.Anuncio anuncio) {
        double preco = anuncio.getPreco();
        
        // Verificar se o preço está dentro dos limites permitidos
        if (preco < precoMinimo || preco > precoMaximo) {
            System.out.println("Anúncio rejeitado por preço fora dos limites permitidos.");
            System.out.println("Preço informado: R$ " + String.format("%.2f", preco));
            System.out.println("Intervalo permitido: R$ " + String.format("%.2f", precoMinimo) 
                + " a R$ " + String.format("%.2f", precoMaximo));
            return false; // Rejeita o anúncio imediatamente
        }
        
        // Preço válido, passar para o próximo validador
        return handleNext(anuncio);
    }
}
