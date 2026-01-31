package com.myhome.chain;

public class ValidadorPreco extends ModeradorBase {
    private final double precoMinimo;
    private final double precoMaximo;

    /**
     * Construtor que define os limites de preço
     * Utiliza o Singleton ConfigurationManager para obter os valores
     */
    public ValidadorPreco() {
        this.precoMinimo = com.myhome.singleton.ConfigurationManager.getInstance().getPropertyAsDouble("preco.minimo");
        this.precoMaximo = com.myhome.singleton.ConfigurationManager.getInstance().getPropertyAsDouble("preco.maximo");
    }

    @Override
    public boolean handle(com.myhome.model.Anuncio anuncio) {
        double preco = anuncio.getPreco();
        if (preco < precoMinimo || preco > precoMaximo) {
            System.out.println("Anúncio rejeitado por preço fora dos limites: " + preco);
            return false; // Rejeita o anúncio
        }
        return handleNext(anuncio);
    }

}
