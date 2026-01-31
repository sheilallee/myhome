package com.myhome.decorator;

import java.util.ArrayList;
import java.util.List;

import com.myhome.model.Anuncio;

public class FiltroTipoImovelDecorator extends FiltroBaseDecorator {
    private String tipoImovel;

    public FiltroTipoImovelDecorator(BuscaFiltro wrappee, String tipoImovel) {
        super(wrappee);
        this.tipoImovel = tipoImovel;
    }

    @Override
    public List<Anuncio> buscar() {
        List<Anuncio> anunciosFiltrados = new ArrayList<>();
        for (Anuncio anuncio : this.wrappee.buscar()) {
            if (anuncio.getImovel().getTipo().equalsIgnoreCase(tipoImovel)) {
                anunciosFiltrados.add(anuncio);
            }
        }

        return anunciosFiltrados;
    }

}
