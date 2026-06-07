/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;

/**
 *
 * @author david
 */
public class IncDec extends BaseNode {
    
    private final String name;
    private final String operator;  // "++" o "--"

    public IncDec(String name, String operator, int line, int column) {
        super(line, column);
        this.name = name;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }
    
}
