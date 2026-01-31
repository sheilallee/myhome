package com.myhome.decorator;

import java.util.List;

import com.myhome.model.Anuncio;

public class BuscaPadrao implements BuscaFiltro {
    List<Anuncio> anuncios;

    public BuscaPadrao(List<Anuncio> anuncios) {
        this.anuncios = anuncios;
    }

    @Override
    public List<Anuncio> buscar() {
        return anuncios;
    };
}
