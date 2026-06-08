/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value;

/**
 *
 * @author david
 */
public record VoidValue(int line, int column) implements ValueWrapper {

    @Override
    public String getType() {
        return "Void";
    }

    @Override
    public String toString() {
        return "Void";
    }

}
