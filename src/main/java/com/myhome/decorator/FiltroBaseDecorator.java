package com.myhome.decorator;

public abstract class FiltroBaseDecorator implements BuscaFiltro {
    protected BuscaFiltro wrappee;

    public FiltroBaseDecorator(BuscaFiltro buscaFiltro) {
        this.wrappee = buscaFiltro;
    }
}
