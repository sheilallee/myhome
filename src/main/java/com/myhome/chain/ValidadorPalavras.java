package com.myhome.chain;

import com.myhome.model.Anuncio;
import com.myhome.singleton.ConfigurationManager;

public class ValidadorPalavras extends ModeradorBase {
    // Lista de palavras proibidas carregada do application.properties via Singleton
    private final java.util.List<String> termosProibidos;
    
    /**
     * Utilizando o Singleton ConfigurationManager para obter
     * a lista de termos proibidos do arquivo application.properties.
     */
    public ValidadorPalavras() {
        this.termosProibidos = ConfigurationManager.getInstance().getTermosProibidos();
    }

    @Override
    public boolean handle(Anuncio anuncio) {
        String descricao = anuncio.getDescricao();
        for (String termo : termosProibidos) {
            if (descricao != null && descricao.toLowerCase().contains(termo.toLowerCase())) {
                System.out.println("Anúncio rejeitado por conter termo proibido: " + termo);
                return false; // Rejeita o anúncio
            }
        }
        return handleNext(anuncio);
    }

}
