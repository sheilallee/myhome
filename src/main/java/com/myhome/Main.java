package com.myhome;

import com.myhome.facade.MyHomeFacade;

/**
 * CLIENTE - PONTO DE ENTRAD* 
 * O cliente apenas inicia a aplicação.
 * Toda a lógica está encapsulada em MyHomeFacade (RF08).
 * 
 * PRINCÍPIOS SOLID:
 * - SRP: Responsável apenas por iniciar a aplicação
 * - DIP: Depende da abstração MyHomeFacade
 * - Baixo acoplamento: não conhece detalhes internos
 * 
 * @author MyHome Team
 * @version 2.0 - Interativo
 */
public class Main {
    
    public static void main(String[] args) {
        MyHomeFacade app = new MyHomeFacade();
        app.executar();
    }
}

