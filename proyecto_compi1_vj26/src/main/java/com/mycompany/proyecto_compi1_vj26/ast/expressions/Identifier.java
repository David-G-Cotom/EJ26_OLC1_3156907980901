/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;

/**
 *
 * @author david
 */
public class Identifier extends BaseNode {
    
    private final String name;

    public Identifier(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
