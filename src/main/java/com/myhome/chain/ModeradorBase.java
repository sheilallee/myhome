package com.myhome.chain;

import com.myhome.model.Anuncio;

public abstract class ModeradorBase {
    protected ModeradorBase next;

    public ModeradorBase setNext(ModeradorBase next) {
        this.next = next;
        return next;
    }

    public abstract boolean handle(Anuncio anuncio);

    protected boolean handleNext(Anuncio anuncio) {
        if (next != null) {
            return next.handle(anuncio);
        }
        return true; // Se não houver próximo, considerar como aprovado
    }
}
