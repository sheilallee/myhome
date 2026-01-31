package com.myhome.factory;

import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;

// RF01 - Factory concreta que cria anúncios de ALUGUEL
public class AluguelFactory implements AnuncioFactory {
    
    @Override
    public Anuncio criarAnuncio(String titulo, double preco, String descricao,
                               Imovel imovel, Usuario anunciante) {
        
        // Valida parâmetros obrigatórios
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (imovel == null) {
            throw new IllegalArgumentException("Imóvel não pode ser nulo");
        }
        if (anunciante == null) {
            throw new IllegalArgumentException("Anunciante não pode ser nulo");
        }
        
        // Cria e configura o anúncio de aluguel
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(titulo);
        anuncio.setPreco(preco);
        anuncio.setImovel(imovel);
        anuncio.setAnunciante(anunciante);
        
        if (descricao != null && !descricao.trim().isEmpty()) {
            anuncio.setDescricao(descricao);
        }
        
        return anuncio;
    }
}
