package com.myhome.decorator;

import java.util.ArrayList;
import java.util.List;

import com.myhome.model.Anuncio;

public class FiltroLocalizacaoDecorator extends FiltroBaseDecorator {
    private String cidade;
    private String estado;

    public FiltroLocalizacaoDecorator(BuscaFiltro wrappee, String cidade, String estado) {
        super(wrappee);
        this.cidade = cidade;
        this.estado = estado;
    }

    @Override
    public List<Anuncio> buscar() {
        List<Anuncio> anunciosFiltrados = new ArrayList<>();
        for (Anuncio anuncio : this.wrappee.buscar()) {
            if (anuncio.getImovel().getEndereco().getCidade().equalsIgnoreCase(cidade) && anuncio.getImovel().getEndereco().getEstado().equalsIgnoreCase(estado)) {
                anunciosFiltrados.add(anuncio);
            }
        }

        return anunciosFiltrados;
    }
}