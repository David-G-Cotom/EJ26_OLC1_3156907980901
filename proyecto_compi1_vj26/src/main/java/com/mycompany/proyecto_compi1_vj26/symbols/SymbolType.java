/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.symbols;

/**
 *
 * @author david
 */
public enum SymbolType {

    VARIABLE("Variable"),
    FUNCION("Funcion"),
    CAMPO_STRUCT("Campo de Struct");

    private final String type;

    private SymbolType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
