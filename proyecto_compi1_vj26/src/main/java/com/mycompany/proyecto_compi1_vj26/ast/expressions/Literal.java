/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.models.ValType;

/**
 *
 * @author david
 */
public class Literal extends BaseNode {
    
    private final Object value;
    private final ValType type;

    public Literal(Object value, ValType type, int line, int column) {
        super(line, column);
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public ValType getType() {
        return type;
    }
    
}
