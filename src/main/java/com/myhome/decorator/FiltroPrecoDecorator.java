package com.myhome.decorator;

import java.util.ArrayList;
import java.util.List;

import com.myhome.model.Anuncio;

public class FiltroPrecoDecorator extends FiltroBaseDecorator {
    private double precoMinimo;
    private double precoMaximo;

    public FiltroPrecoDecorator(BuscaFiltro wrappee, double precoMinimo, double precoMaximo) {
        super(wrappee);
        this.precoMinimo = precoMinimo;
        this.precoMaximo = precoMaximo;
    }

    @Override
    public List<Anuncio> buscar() {
        List<Anuncio> anunciosFiltrados = new ArrayList<>();
        for (Anuncio anuncio : this.wrappee.buscar()) {
            if (anuncio.getPreco() >= precoMinimo && anuncio.getPreco() <= precoMaximo) {
                anunciosFiltrados.add(anuncio);
            }
        }

        return anunciosFiltrados;
    }
}
