package com.myhome.state;

import com.myhome.model.Anuncio;

public class ModeracaoState implements AnuncioState {

    /**
     * Aprova o anúncio somente se ele atender
     * aos critérios mínimos de moderação automática.
     */
    @Override
    public void aprovar(Anuncio anuncio) {

        // Regra 1: validação básica do anúncio
        if (!anuncio.validar()) {
            throw new IllegalStateException(
                "Anúncio inválido para aprovação."
            );
        }

        // Regra 2: deve possuir ao menos uma foto
        if (anuncio.getQuantidadeFotos() == 0) {
            throw new IllegalStateException(
                "Anúncio precisa ter ao menos uma foto."
            );
        }

        // Regra 3: descrição mínima
        if (anuncio.getDescricao() == null ||
            anuncio.getDescricao().trim().length() < 20) {

            throw new IllegalStateException(
                "Descrição muito curta para aprovação."
            );
        }

        // Se todas as regras passarem, o anúncio é ativado
        anuncio.mudarEstado(new AtivoState());
    }

    /**
     * Reprovação manual ou automática do anúncio.
     */
    @Override
    public void reprovar(Anuncio anuncio) {
        anuncio.mudarEstado(new SuspensoState());
    }

    @Override public void enviarParaModeracao(Anuncio anuncio) { erro(); }
    @Override public void vender(Anuncio anuncio) { erro(); }
    @Override public void suspender(Anuncio anuncio) { erro(); }

    @Override
    public String getNome() {
        return "Em Moderação";
    }

    private void erro() {
        throw new IllegalStateException(
            "Operação inválida no estado Moderação."
        );
    }
}
