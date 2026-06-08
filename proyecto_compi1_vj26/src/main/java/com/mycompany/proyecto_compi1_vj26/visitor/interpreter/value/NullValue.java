/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value;

import com.mycompany.proyecto_compi1_vj26.models.VarType;

/**
 *
 * @author david
 */
public record NullValue(int line, int column) implements ValueWrapper {

    @Override
    public String getType() {
        return VarType.NIL.getType();
    }

    @Override
    public String toString() {
        return "Null";
    }

}
