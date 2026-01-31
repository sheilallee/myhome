package com.myhome.chain;

import com.myhome.model.Anuncio;
import com.myhome.singleton.ConfigurationManager;

public class ValidadorPalavras extends ModeradorBase {
    // Lista de palavras proibidas carregada do application.properties via Singleton
    private final java.util.List<String> termosProibidos;
    
    public ValidadorPalavras() {
        this.termosProibidos = ConfigurationManager.getInstance().getTermosProibidos();
    }

    @Override
    public boolean handle(Anuncio anuncio) {
        String descricao = anuncio.getDescricao();
        
        // Validar descrição contra lista de termos proibidos
        for (String termo : termosProibidos) {
            if (descricao != null && descricao.toLowerCase().contains(termo.toLowerCase())) {
                System.out.println("Anúncio rejeitado por conter termo proibido: '" + termo + "'");
                return false; // Rejeita o anúncio imediatamente
            }
        }
        
        // Nenhum termo proibido encontrado, passar para o próximo validador
        return handleNext(anuncio);
    }

}
