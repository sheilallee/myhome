package com.myhome.factory;

import com.myhome.model.Anuncio;
import com.myhome.model.Imovel;
import com.myhome.model.Usuario;

// RF01 - Factory Method: Interface abstrata para criação de anúncios
public interface AnuncioFactory {
    
    // Método abstrato: cada factory concreta implementa para seu tipo específico
    Anuncio criarAnuncio(String titulo, double preco, String descricao,
                        Imovel imovel, Usuario anunciante);
}
