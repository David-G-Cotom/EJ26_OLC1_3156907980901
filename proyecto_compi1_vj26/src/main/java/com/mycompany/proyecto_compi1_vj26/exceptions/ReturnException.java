/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.exceptions;

import com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value.ValueWrapper;

/**
 *
 * @author david
 */
public class ReturnException extends RuntimeException {
    
    private final ValueWrapper value; // null si es return sin valor

    public ReturnException(ValueWrapper value) {
        super(null, null, true, false);
        this.value = value;
    }

    public ValueWrapper getValue() {
        return value;
    }
    
}
