/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.models;

/**
 *
 * @author david
 */
public enum VarType {
    
    INT("int"),
    FLOAT("float64"),
    STRING("string"),
    BOOL("bool"),
    RUNE("rune");
    
    private final String type;

    private VarType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
}
