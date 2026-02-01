package com.myhome.chain;

import com.myhome.model.Anuncio;
import com.myhome.singleton.ConfigurationManager;

public class ValidadorPalavras extends ModeradorBase {
    // Lista de palavras proibidas carregada do application.properties via Singleton
    private final java.util.List<String> termosProibidos;
    
    public ValidadorPalavras() {
        this.termosProibidos = ConfigurationManager.getInstance().getTermosProibidos();
        
        // Log de inicialização
        if (termosProibidos.isEmpty()) {
            System.out.println("⚠️  ValidadorPalavras inicializado com lista vazia de termos proibidos");
        } else {
            System.out.println("✅ ValidadorPalavras inicializado: " + termosProibidos.size() + " termo(s) proibido(s)");
        }
    }

    @Override
    public boolean handle(Anuncio anuncio) {
        System.out.println("🔍 Validando palavras proibidas...");
        
        String descricao = anuncio.getDescricao();
        String titulo = anuncio.getTitulo();
        
        // Validar descrição e título contra lista de termos proibidos
        for (String termo : termosProibidos) {
            if ((descricao != null && descricao.toLowerCase().contains(termo.toLowerCase())) ||
                (titulo != null && titulo.toLowerCase().contains(termo.toLowerCase()))) {
                System.out.println("   ❌ Rejeitado: contém termo proibido '" + termo + "'");
                return false;
            }
        }
        
        System.out.println("   ✅ Palavras validadas com sucesso!");
        return handleNext(anuncio);
    }

}
