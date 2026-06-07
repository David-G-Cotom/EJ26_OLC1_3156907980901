/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.models;

/**
 *
 * @author david
 */
public enum ErrorType {
    
    LEXICO("Lexico"),
    SINTACTICO("Sintactico"),
    SEMANTICO("Semantico");
    
    private final String type;

    private ErrorType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
}
